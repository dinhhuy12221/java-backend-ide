package Server.socket;

import java.io.File;
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
	private ExecutorService executorService = Executors.newFixedThreadPool(10);
	private LogFileHelper logFileHelper;
	public server(int port) {
		this.port = port;
	}

	public void connect() {
		try {
			this.serverSocket = new ServerSocket(this.port);
			writeToLog("[Notification] Server: " + this.serverSocket + " is opening....");
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
				writeToLog("[Notification] Server: " + this.serverSocket + " is closed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	private void writeToLog(String line) {
		try {
			logFileHelper = new LogFileHelper(new File(".\\src\\Server\\socket\\log.txt"));
			logFileHelper.tryWriteLine(line);
			logFileHelper.tryClose();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
 