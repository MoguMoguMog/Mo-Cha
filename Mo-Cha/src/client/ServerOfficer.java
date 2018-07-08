package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.ChatServer;

public class ServerOfficer extends Thread {
	// 通信のためのソケット
	private Socket sc;
	// そのソケットから作成した入出力用のストリーム
	private BufferedReader br = null;;
	private PrintWriter pw = null;;
	// サーバ本体のメソッドを呼び出すために記憶
	public ChatServer chatServer;
	private String message;
	private int number = 0;
	private boolean canRun = true;
	
	public ServerOfficer(Socket s, ChatServer chatServer, int number) {
		sc = s;
		this.chatServer = chatServer;
		this.number = number;
		try {
			// クライアントにメッセージ送信するため
			pw = new PrintWriter(sc.getOutputStream(),true);
			// クライアントからのメッセージを受信するため
			br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * クライアントにメッセージを送信する
	 * @param message メッセージ
	 */
	public void send(String message) {
		pw.println(message);
	}
	
	/**
	 * 接続しているクライアントのナンバリング
	 * @param number クライアントナンバー
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * スレッドを止めるためのフラグを設定
	 */
	public void setCanRun(boolean flag) {
		this.canRun = flag;
	}
	
	public void run() {
		while (canRun) {
			try {
				// メッセージを受信したら
				if((message = br.readLine()) != null) {
					chatServer.sendAll(message,number);
				}
			} catch (Exception e) {
				try {
					br.close();
					pw.close();
					sc.close();
					System.out.println("Good Bye !!");
					break;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}