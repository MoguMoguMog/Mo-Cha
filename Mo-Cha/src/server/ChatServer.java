package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.ChatClient;

public class ChatServer {
	//PORT Number
	public static final int PORT = 10000; //待ち受けポート番号
	// クライアント
	public static ChatClient clients[];
	
//	private ArrayList<ChatClientUser> userList;
	/**
	 * コンストラクタ
	 */
	public ChatServer() {
		clients = new ChatClient[2];
		Socket socket;
		
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			while(true) {
				int i;
				socket = serverSocket.accept();
				for(i=0; i < clients.length;i++) {
					if(clients[i] == null) {
						clients[i] = new ChatClient(socket, this);
						new Thread(clients[i]).start();
						break;
					}
				}
				
				if(i == clients.length) {
					System.out.println("Full");
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}
	
	public synchronized void sendAll(String message) {
		int i;
		for(i=0;i<clients.length; i++) {
			if(clients[i] != null) {
				clients[i].send(message);
			}
		}
	}
}