package ScrabbleServer;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HandleClient implements Runnable {
	private ConnectionToClient client;
	private ConcurrentLinkedQueue<String> inGameMessages;
	private ConcurrentLinkedQueue<String> inLobbyMessages;
	private JSONParser parser = new JSONParser();
	private Map<Integer, ConnectionToClient> idleClients;
	private Map<Integer, ConnectionToClient> readyClients;

	HandleClient(ConnectionToClient client, ConcurrentLinkedQueue<String> inLobbyMessages, ConcurrentLinkedQueue<String> inGameMessages,
				 Map<Integer, ConnectionToClient> idleClients, Map<Integer, ConnectionToClient> readyClients) {
		this.client = client;
		this.inGameMessages = inGameMessages;
		this.inLobbyMessages = inLobbyMessages;
		this.idleClients = idleClients;
		this.readyClients = readyClients;
	}

	@Override
	public void run() {
		try {
			DataInputStream input = new DataInputStream(client.getSocket().getInputStream());
			DataOutputStream output = new DataOutputStream(client.getSocket().getOutputStream());
			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(isr);
			while (true) {
				if (br != null) {
					try {
						if(idleClients.containsKey(client.getClientId())){
							System.out.println("check 1\n");
							JSONObject messageJSON = (JSONObject) parser.parse(br.readLine());
							messageJSON.put("USER", client.getUserName());
							String messageString = messageJSON.toJSONString();
							inLobbyMessages.add(messageString);
							System.out.println(messageString);

						}
						else{
							System.out.println("check 2\n");
							JSONObject messageJSON = (JSONObject) parser.parse(br.readLine());
							messageJSON.put("USER", client.getUserName());
							String messageString = messageJSON.toJSONString();
							inGameMessages.add(messageString);
						}
					} catch (ParseException e) {
						//e.printStackTrace();

					}
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println(client.getUserName() + " connection lost");
			System.out.println(client.getUserName() + " has left the room!");
			readyClients.remove(client.getClientId());
			idleClients.remove(client.getClientId());
		}
	}
}