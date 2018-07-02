package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import view.RChatWindow;

public class CChatWindow extends RChatWindow implements Runnable {
	public Socket clientSocket = null;
	InputStream input = null;
	BufferedReader reader = null;

	private String HOST = "localhost";
	private int PORT = 10000;
	private EventHandler<MouseEvent> mouseClick = null;
	private EventHandler<WindowEvent> windowClose = null;
	Thread thread = null;

	public CChatWindow() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void initializeCreation() {
		// 送信ボタンイベントリスナ登録
		mouseClick = (event) -> this.actionExecute(event);
		sendButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);
		// ウィンドウイベントリスナ登録
		windowClose = (event) -> this.close();
		root.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, windowClose);

		myNameText.setText(myName + "\n\n");
		myNameText.setFill(Color.LIGHTSEAGREEN);
		otherNameText.setText(otherName);

		initConnectServer();

		thread = new Thread(this);
		thread.start();
	}

	private void close() {
		mouseClick = null;
		windowClose = null;

		String closeMessage = "close";
		sendMessage(closeMessage);

		try {
			clientSocket.close();
			input.close();
			reader.close();
		} catch (IOException e) {
			System.err.println("正常に終了できませんでした。管理者に報告してください。");
		}

	}

	/**
	 * サーバに接続する処理
	 */
	private void initConnectServer() {
		try {
			clientSocket = new Socket(HOST, PORT);
			input = clientSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
			
			sendMessage("");
			
		} catch (Exception err) {
			outputTextField.setText("ERROR" + err + "\n");
		}
	}

	public void actionExecute(Event e) {
		// 送信ボタン押下
		if (e.getSource() == sendButton) {
			executeSendButton();
		}
	}

	private void executeSendButton() {
		String message = inputTextField.getText();
		sendMessage(message);
		inputTextField.clear();
	}

	public static void main(String[] args) {
		myName = args[0];
		otherName = args[1];
		launch(args);
	}

	@Override
	public void run() {
		try {
			while (!clientSocket.isClosed()) {
				String line = reader.readLine();
				reachedMessage(line);
			}
		} catch (Exception err) {
		}
	}

	/**
	 * 
	 * 受信したメッセージを表示
	 */
	public void reachedMessage(String msgValue) {
		String[] message = new String[2];
		message = msgValue.split(",");
		
		if (message[1].equals("close")) {
			// 相手が画面を閉じている場合
			otherNameText.setFill(Color.WHITE);
		} else if (message[1].equals("connect")) {
			// 相手がサーバに接続している場合
			otherNameText.setFill(Color.LIGHTSEAGREEN);
		}
		if (!message[0].equals("")) {
			outputTextField.appendText(message[0] + "\n");
		}
	}
	
	/**
	 * メッセージを送る
	 */
	public void sendMessage(String msg) {
		try {
			OutputStream output = clientSocket.getOutputStream();
			PrintWriter writer = new PrintWriter(output);
			
			if(!(msg.equals("") || msg.equals("close"))) {
				msg = myName + "\n" + msg;
				msg = msg.replaceAll("\n", "\n  ");
			}
			
			writer.println(msg);
			writer.flush();
		} catch (Exception err) {
			System.out.println("error");
			this.close();
		}
	}
}
