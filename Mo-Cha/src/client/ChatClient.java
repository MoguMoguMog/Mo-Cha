package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import server.ChatServer;

public class ChatClient extends Thread {
	// 通信のためのソケット
	private Socket sc;
	// そのソケットから作成した入出力用のストリーム
	private BufferedReader br;
	private PrintWriter pw;
	// サーバ本体のメソッドを呼び出すために記憶
	public ChatServer chatServer;
	private String message;
	private String name;
	
	public ChatClient(Socket s, ChatServer chatServer) {
		sc = s;
		this.chatServer = chatServer;
		pw = null;
		br = null;
	}
	
	public void send(String message) {
		pw.println(message);
	}
	
	public void run() {
		try {
//			br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
//			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sc.getOutputStream())));
			pw = new PrintWriter(sc.getOutputStream(),true);
			br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (true) {
			
			try {
				if((message = br.readLine()) != null) {
					chatServer.sendAll(message);
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