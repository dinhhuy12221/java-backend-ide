
import Server.socket.server;
public class Main {
    public static void main(String[] args) throws Exception {
        server server = new server(1234);
		server.connect();
		server.close();
    }
    
}