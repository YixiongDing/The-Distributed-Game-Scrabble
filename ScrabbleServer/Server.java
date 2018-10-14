// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu

// Server.java contains the main function of the Scrabble program. The job of this class is
// to listen to the sockets of connected clients, check wether the username is already taken 
// and assign each of them a thread to collect their messages sent to server

package ScrabbleServer;

import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;

public class Server {
	public static void main(String[] args) {
		try {
			int port = Integer.parseInt(args[0]);
			ServerSocketFactory factory = ServerSocketFactory.getDefault();
			ServerSocket server = factory.createServerSocket(port);
			Map<Integer, ConnectionToClient> idleClients = new HashMap<Integer, ConnectionToClient>();
			ConcurrentLinkedQueue<String> inLobbyMessages = new ConcurrentLinkedQueue<String>();

			String username = null;
			int id = 0;
			JSONObject response = new JSONObject();

			System.out.println("Server is up!");

			while (true) {
				
				// Listen to the socket 
				Socket client = server.accept();
				DataInputStream input = new DataInputStream(client.getInputStream());
				DataOutputStream output = new DataOutputStream(client.getOutputStream());
				InputStreamReader isr = new InputStreamReader(input);
				OutputStreamWriter osw = new OutputStreamWriter(output);
				BufferedReader br = new BufferedReader(isr);
				BufferedWriter bw = new BufferedWriter(osw);
				username = br.readLine();
				boolean startLobby = false;
				
				// Check wethter the username is already taken by other users 
				if (checkClient(username, idleClients)) {
					System.out.println(username + " has already logged in!");
					response.put("LOGINSTATUS", "2");
					bw.write(response.toJSONString()+"\n");
					bw.flush();
				}else {
					System.out.println(username + " connected");
					response.put("LOGINSTATUS", "1");
					ConnectionToClient newclient = new ConnectionToClient(id, client, username);
					idleClients.put(id, newclient);
					
					// Assign each client a thread to collect their messages to the server
					Thread clientThread = new Thread(new HandleClient(newclient, inLobbyMessages));
					clientThread.start();
					sendToUser(response, username, idleClients);
					id += 1;
					
					// If any user connected, start the lobby to handle game request
					if(id == 1) {
						startLobby = true;
					}
				}
				
				// Assign the lobby thread
				if (startLobby) {
					Thread lobbyThread = new Thread(new HandleLobby(idleClients, inLobbyMessages));
					lobbyThread.start();
				}
			}
		} catch (NumberFormatException e) {
			System.err.println("\"" + args[0] + "\"" + " is not a valid integer");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// This method is used to check wether is already taken by other users
	private static boolean checkClient(String newclient,  Map<Integer, ConnectionToClient> clients) {
		for (ConnectionToClient c : clients.values()) {
			if (c.getUserName().equals(newclient)) {
				return true;
			}
		}
		return false;
	}

	// This method is used to send the response message to the particular user in the userlist
	private static void sendToUser(JSONObject message, String user, Map<Integer, ConnectionToClient> clients) {
		for (ConnectionToClient c : clients.values()) {
			if (c.getUserName().equals(user)) {
				c.sendMessage(message);
				break;
			}
		}
	}

}