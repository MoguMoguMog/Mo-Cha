/**
 * ソケット通信部分と画面制御部でクラス分けたい
 * */
package control;

import client.ClientOfficer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import view.RChatWindow;

public class CChatWindow extends RChatWindow {
	// サーバとのやり取りを行うためのクラス
	ClientOfficer clientOfficer = null;

	// イベントハンドラ
	private EventHandler<MouseEvent> mouseClick = null;
	private EventHandler<WindowEvent> windowClose = null;

	/**
	 * コンストラクタ
	 */
	public CChatWindow() {
	}

	/**
	 * 画面初期化処理
	 */
	public void initializeCreation() {
		// サーバとのやり取り用のクラス
		clientOfficer = new ClientOfficer(myName, this);
		// 送信ボタンイベントリスナ登録
		mouseClick = (event) -> this.actionExecute(event);
		sendButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClick);
		// ウィンドウイベントリスナ登録
		windowClose = (event) -> this.close();
		root.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, windowClose);

		myNameText.setText(myName + "\n\n");
		myNameText.setFill(Color.LIGHTSEAGREEN);
		otherNameText.setText(otherName);

		clientOfficer.initConnectServer();
	}

	/**
	 * 画面クローズ時の処理
	 */
	private void close() {
		mouseClick = null;
		windowClose = null;

		String closeMessage = "close";
		clientOfficer.sendMessage(closeMessage);

		clientOfficer.close();
	}

	public void actionExecute(Event e) {
		// 送信ボタン押下
		if (e.getSource() == sendButton) {
			executeSendButton();
		}
	}

	private void executeSendButton() {
		String message = inputTextField.getText();
		clientOfficer.sendMessage(message);
		inputTextField.clear();
	}

	/**
	 * 受信したメッセージを表示
	 * @param msgValue 受信したメッセージ
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
		outputTextField.setText(msgValue);
	}

	public static void main(String[] args) {
		myName = args[0];
		otherName = args[1];
		launch(args);
	}
}
