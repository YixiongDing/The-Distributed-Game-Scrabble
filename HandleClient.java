import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HandleClient implements Runnable {
	private Socket socket;
	private ConnectionToClient client;
	private ConcurrentLinkedQueue<String> messages;

	HandleClient(ConnectionToClient client,ConcurrentLinkedQueue<String> messages ) {
		this.client = client;
		this.messages = messages;
	}
	
	@Override
	public void run() {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(isr);
			int id = 1;
			String username;
			while (true) {

				
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("connection lost");
		}

	}

}
