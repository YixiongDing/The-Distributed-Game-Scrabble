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
			System.out.println(client.getUserName() + " has left the room!");
			JSONObject logoutMessage = new JSONObject();
			logoutMessage.put("COMMAND", "NEWLOGOUT");
			logoutMessage.put("USER", client.getUserName());
			String logoutMessageString = logoutMessage.toJSONString();
			inLobbyMessages.add(logoutMessageString);
		}
	}
}