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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import view.RChatWindow;

public class CChatWindow extends RChatWindow implements Runnable{
	public Socket clientSocket = null;
	private String HOST = "localhost";
	private int PORT = 10000;
	
	Thread thread = null;
	
	public CChatWindow() {
		
	}
	
	@Override
	public void initializeCreation() {
		// イベントリスナ登録
		EventHandler<MouseEvent> mouseClick = (event)->this.actionExecute(event);
		sendButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);
		
		myNameText.setText(myName+"\n\n");
		otherNameText.setText(otherName);
		
		initConnectServer();
		
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * サーバに接続する処理
	 */
	private void initConnectServer() {
		try {
			clientSocket = new Socket(HOST,PORT);
//			outputTextField.setText(">サーバに接続しました\n");
		} catch (Exception err) {
			outputTextField.setText("ERROR"+err + "\n");
		}
	}
	
	public void paintLoginStatus() {
		
		myNameText.setFont(Font.font("Arial", FontWeight.BOLD,16));
		myNameText.setFill(Color.DARKCYAN);
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
			InputStream input = clientSocket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			while(!clientSocket.isClosed()) {
				String line = reader.readLine();

				reachedMessage(line);
			}
		}
		catch(Exception err) { }
	}

	/**
	 * 受信したメッセージを表示
	 */
	private void reachedMessage(String msgValue) {
		outputTextField.appendText(msgValue + "\n");
	}
	
	/**
	 * ソケット閉じる
	 */
	public void close() throws IOException {
		sendMessage("close");
		clientSocket.close();
	}

	/**
	 * メッセージを送る
	 */
	public void sendMessage(String msg) {
		try {
			OutputStream output = clientSocket.getOutputStream();
			PrintWriter writer = new PrintWriter(output);

			writer.println(msg);
			writer.flush();
		}
		catch(Exception err) { outputTextField.appendText("ERROR>" + err + "\n"); }
	}
}
