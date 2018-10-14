// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu

// HandleClient.java is a thread class that collects messages from a user sent to server,
// the thread will add the client name in the JSON format message and put it in the queue

package ScrabbleServer;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HandleClient implements Runnable {
	private ConnectionToClient client;
	private ConcurrentLinkedQueue<String> inLobbyMessages;
	private JSONParser parser = new JSONParser();

	HandleClient(ConnectionToClient client, ConcurrentLinkedQueue<String> inLobbyMessages) {
		this.client = client;
		this.inLobbyMessages = inLobbyMessages;
	}

	@Override
	public void run() {
		try {
			DataInputStream input = new DataInputStream(client.getSocket().getInputStream());
			DataOutputStream output = new DataOutputStream(client.getSocket().getOutputStream());
			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(isr);
			JSONObject loginMessage = new JSONObject();
			loginMessage.put("COMMAND", "NEWLOGIN");
			loginMessage.put("USER", client.getUserName());
			String loginMessageString = loginMessage.toJSONString();
			inLobbyMessages.add(loginMessageString);
			while (true) {
				if (br != null) {
					try {
							JSONObject messageJSON = (JSONObject) parser.parse(br.readLine());
							
							// Add the name of the client to the message
							messageJSON.put("USER", client.getUserName());
							String messageString = messageJSON.toJSONString();
							inLobbyMessages.add(messageString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			
			// Handle the case when user logs out
			System.out.println(client.getUserName() + " has left the room!");
			JSONObject logoutMessage = new JSONObject();
			logoutMessage.put("COMMAND", "NEWLOGOUT");
			logoutMessage.put("USER", client.getUserName());
			String logoutMessageString = logoutMessage.toJSONString();
			inLobbyMessages.add(logoutMessageString);
		}
	}
}