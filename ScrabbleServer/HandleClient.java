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
	private JSONParser parser = new JSONParser();
	private Map<Integer, ConnectionToClient> idleClients = new HashMap<Integer, ConnectionToClient>();
	private Map<Integer, ConnectionToClient> readyClients = new HashMap<Integer, ConnectionToClient>();
	private Collection<ConnectionToClient> readyClientSockets;
	private GameAssistant gameAssitant;


	HandleClient(ConnectionToClient client, ConcurrentLinkedQueue<String> inGameMessages, Map<Integer, ConnectionToClient> idleClients, Map<Integer, ConnectionToClient> readyClients, GameAssistant gameAssitant) {
		this.client = client;
		this.inGameMessages = inGameMessages;
		this.idleClients = idleClients;
		this.readyClients = readyClients;
		this.readyClientSockets = readyClients.values();
		this.gameAssitant = gameAssitant;
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
						JSONObject messageJSON = (JSONObject) parser.parse(br.readLine());
						String command = (String) messageJSON.get("COMMAND");
						JSONObject messageUser = (JSONObject) messageJSON.get("MESSAGE");
						String messageString; 
						
						switch(command) 
						{
						case "CREATE":
							messageJSON.put("USER", client.getUserName());
							messageString = messageJSON.toJSONString();
							inGameMessages.add(messageString);
							readyClients.put(client.getClientId(), client);
							idleClients.remove(client.getClientId());
							break;

						case "JOIN":
							messageJSON.put("USER", client.getUserName());
							messageString = messageJSON.toJSONString();
							inGameMessages.add(messageString);
							readyClients.put(client.getClientId(), client);
							idleClients.remove(client.getClientId());
							break;

						case "INVITE":
							sendToUser(messageJSON, (String) messageJSON.get("INVITE"));
							gameAssitant.updateInviter(client);
							break;

						case "ACCEPTANCE":
							if(messageUser.get("ACCEPTANCE").equals("YES")) {
								messageJSON.put("USER", client.getUserName());
								messageString = messageJSON.toJSONString();
								readyClients.put(client.getClientId(), client);
								idleClients.remove(client.getClientId());
								break;
							}else {
								sendToUser(messageUser, gameAssitant.getInviterName());
								break;
							}
							
						case "START": 
							if(gameAssitant.getStarterName().equals(client.getUserName())) {
								gameAssitant.startGame(true);
								break;
							}else {
								JSONObject messageBack = new JSONObject();
								messageBack.put("Message", "Only the room owner can start the game!");
								sendToUser(messageBack, client.getUserName());
								break;
							}
							
						case "LOGOUT":
							idleClients.put(client.getClientId(), client);
							readyClients.remove(client.getClientId());
							break;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(client.getUserName() + "connection lost");
		}
	}


	private void sendToUser(JSONObject message, String user) {
		for (ConnectionToClient c : this.readyClientSockets) {
			if (c.getUserName().equals(user)) {
				c.sendMessage(message);
				break;
			}
		}
	}

}