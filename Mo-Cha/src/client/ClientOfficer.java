package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import control.CChatWindow;

public class ClientOfficer implements Runnable {
	// ソケット通信
	public Socket clientSocket = null;
	InputStream input = null;
	BufferedReader reader = null;
	OutputStream output = null;
	PrintWriter writer = null;
	CChatWindow chatWindow = null;
	
	// サーバのIPアドレス
	private String HOST = "localhost";
	// ポート
	private int PORT = 10000;
	// runメソッド呼び出すため
	Thread thread = null;
	// 自分の名前
	String myName = null;

	/**
	 * コンストラクタ
	 */
	public ClientOfficer(String myName, CChatWindow chatWindow) {
		this.myName = myName;
		this.chatWindow = chatWindow;
		
		try {
			// サーバに接続
			clientSocket = new Socket(HOST, PORT);
			// サーバからの出力を受け付ける
			input = clientSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
			// サーバへの出力を確保する
			output = clientSocket.getOutputStream();
			writer = new PrintWriter(output);
		} catch (UnknownHostException e) {
			// サーバに接続できなかった場合
			System.out.println("サーバに接続できませんでした。");
			e.printStackTrace();
		} catch (IOException e) {
			// それ以外
			e.printStackTrace();
		}
		
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * メッセージをサーバに送信する
	 * @param message 送信したいメッセージ
	 */
	public void sendMessage(String message) {
		// "close"はクローズ時
		if (!(message.equals("") || message.equals("close"))) {
			message = myName + "\n" + message;
			message = message.replaceAll("\n", "\n  ");
		}
		// サーバに送信
		writer.println(message);
		writer.flush();
	}
	
	/**
	 * サーバとの接続を切断する
	 */
	public void close() {
		try {
			clientSocket.close();
			input.close();
			reader.close();
			output.close();
			writer.close();
		} catch (IOException e) {
			// サーバとの接続を切断できなかった場合
			e.printStackTrace();
		}
	}
	
	/**
	 * サーバからのメッセージ受信を待つ
	 */
	@Override
	public void run() {
		try {
			while (!clientSocket.isClosed()) {
				String line = reader.readLine();
				chatWindow.reachedMessage(line);
			}
		} catch (Exception err) {
			// メッセージ受信に失敗したとき
		}
	}
}
