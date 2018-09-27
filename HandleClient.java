import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class HandleClient implements Runnable {
	private Socket socket;
	private Map<Integer, ConnectionToClient> clients; 

	HandleClient(Socket socket) {
		this.socket = socket;
	}

	private boolean checkClient(String newclient) {
		for (ConnectionToClient c : this.clients.values()) {
			if (c.getUserName().equals(newclient)) {
				return true;
			}
		}
		return false; 
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

				username = br.readLine();

				if (checkClient(username)) {
					System.out.println(username + "has already logged in!");
				}else {
					clients.put(id,new ConnectionToClient(id,socket, username));
					id += 1;
				}
				// Send a string!

				if (id >= 5) {
					break;
				}

			}
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("connection lost");
		}

	}

}
