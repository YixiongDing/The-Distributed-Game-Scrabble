import java.net.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
public class Server {
	private ServerSocket s;
	private JSONParser parser = new JSONParser();
	private Map<Integer, ConnectionToClient> clients; 
	
	public Server() {
		try {
			this.s = new ServerSocket(1234);
			this.clients = Collections.synchronizedMap(new LinkedHashMap<>());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}
	
	private boolean checkClient(String newclient) {
		for (ConnectionToClient c : this.clients.values()) {
			if (c.getUserName().equals(newclient)) {
				return true;
			}
		}
		return false; 
	}

	public void run() {
		int id = 1;
		while(true){
			Socket clientSocket = null;
			String username;
			try {
				clientSocket=s.accept();
				InputStream is = clientSocket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				username = br.readLine();

				if (checkClient(username)) {
					System.out.println(username + "has already logged in!");
				}else {
					clients.put(id,new ConnectionToClient(id,clientSocket, username));
					id += 1;
				}
				// Send a string!

				if (id >= 5) {
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Wait and accept a connection
			// Get a communication stream associated with the socket

		}
	}
}