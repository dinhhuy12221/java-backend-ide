package Server.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server {
	private ServerSocket serverSocket;
	private Socket socket;
	private int port;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private ExecutorService executorService = Executors.newCachedThreadPool();

	public server(int port) {
		this.port = port;
	}

	public void connect() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			System.out.println("Server is waiting...");
			while (true) {
				this.socket = this.serverSocket.accept();
				System.out.println("[Notification] Client: " + this.socket + " is connected");
				executorService.execute(new thread(socket));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void close() {
		try {
			if (socket != null) {
				this.in.close();
				this.out.close();
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
 