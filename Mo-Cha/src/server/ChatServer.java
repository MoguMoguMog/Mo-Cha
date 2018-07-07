package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import client.ChatClient;

public class ChatServer {
	//PORT Number
	public static final int PORT = 10000; //待ち受けポート番号
	// クライアント
	public ArrayList<ChatClient> userList = null;
	
	int number = 0;

	/**
	 * コンストラクタ
	 */
	public ChatServer() {
		userList = new ArrayList<ChatClient>();
		Socket socket;

		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			while (true) {
				socket = serverSocket.accept();
				if (userList.size() < 2) {
					userList.add(new ChatClient(socket, this, userList.size()));
					new Thread(userList.get(userList.size()-1)).start();
					System.out.println(userList.size());

				} else if (userList.size() == 2) {
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

	public synchronized void sendAll(String message, int number) {
		if (message.equals("close")) { 
			// 無限ループを止めるための処理
			userList.get(number).setCanRun(false);
			userList.remove(number);
			if(number == 0 && userList.size() == 1) {
				userList.get(0).setNumber(0);
			}
			message = "";
		}
		for (ChatClient client : userList) {
			if (userList.size() == 1) {
				// 接続数が1の場合
				client.send(message + "," + "close");
			} else if(userList.size() == 2){
				// 接続数が2の場合
				client.send(message + "," + "connect");
			}
		}
	}
}