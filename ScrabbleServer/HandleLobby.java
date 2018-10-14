// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu

// HandleLobby.java is a thread class that handle clients request while they are in the lobby. Lobby thread will process 
// data sent from clients, and broadcast related information to other clients. Once a user starts the game, a game thread will
// be created and take control of the game 

package ScrabbleServer;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HandleLobby implements Runnable {
	private Map<Integer, ConnectionToClient> inLobbyClients;
	private Map<Integer, ConnectionToClient> inRoomClients = new HashMap<Integer, ConnectionToClient>();
	private Map<Integer, ConnectionToClient> inGameClients = new HashMap<Integer, ConnectionToClient>();

	private JSONParser parser = new JSONParser();
	ConcurrentLinkedQueue<String> inLobbyMsg;
	ConcurrentLinkedQueue<String> inGameMsg = new ConcurrentLinkedQueue<String>();
	ArrayList<String> onlineclients = new ArrayList<>();

	public HandleLobby(Map<Integer, ConnectionToClient> inLobbyClients, ConcurrentLinkedQueue<String> inLobbyMsg) {
		this.inLobbyClients = inLobbyClients;
		this.inLobbyMsg = inLobbyMsg;
	}

	@Override
	public void run() {
		String gameCreator = null;
		String inviteFrom = null;
		String inviteTo = null;
		Boolean startGame = false;

		while (true) {
			
            // Thread read request from messages
			String lobbyMsg = this.inLobbyMsg.poll();
			boolean inLobby = true;
			
            // Thread process requests and broadcast information to other clients
			if (lobbyMsg != null) {
				try {
					System.out.println(lobbyMsg);
					JSONObject messageJSON = (JSONObject) parser.parse(lobbyMsg);
					String messageFrom = (String) messageJSON.get("USER");
					String command = (String) messageJSON.get("COMMAND");
					JSONObject response = new JSONObject();
					JSONObject display = new JSONObject();
					
					// Check wether the user is in the game, if yes, put all messages from the client to game message queue
					if(locateClient(messageFrom, inGameClients)!= -1 && !command.equals("NEWLOGOUT")&& !command.equals("APPLYSTART")){
						inGameMsg.add(lobbyMsg);
						inLobby = false;
					}

					if(inLobby){
						
						// Based on the potocols sent from the clients, the thread will handle the request
						switch (command) {
						
						// New user logs in
						case "NEWLOGIN":
							if(!onlineclients.contains(messageFrom)){
								onlineclients.add(messageFrom);
								System.out.println(onlineclients);
								for(int i=0; i<onlineclients.size(); i++) {
									response.put(String.valueOf(i),onlineclients.get(i));
								}
								response.put("COMMAND", "USERLIST");
								sendToUser(response, messageFrom, inLobbyClients);
								response.put("NEWLOGIN", messageFrom);
								sendAllFromOne(response, messageFrom, inLobbyClients);
								display.put("DISPLAY","User "+messageFrom+" login");
								sendAll(display, inLobbyClients);
							}
							break;
							
						// User logs out
						case "NEWLOGOUT":
							if(locateClient(messageFrom, inGameClients)!= -1){
								onlineclients.remove(messageFrom);
								inLobbyClients.remove(locateClient(messageFrom, inLobbyClients));
								inGameClients.remove(locateClient(messageFrom, inGameClients));
								JSONObject exitMsgJSON = new JSONObject();
								exitMsgJSON.put("COMMAND", "EXIT");
								String exitMsgString = exitMsgJSON.toJSONString();
								for(int i=0; i<onlineclients.size(); i++) {
									response.put(String.valueOf(i),onlineclients.get(i));
								}
								response.put("COMMAND", "USERLIST");
								response.put("NEWLOGOUT", messageFrom);
								sendAll(response, inLobbyClients);
								display.put("DISPLAY","User "+messageFrom+" logout");
								sendAll(display, inLobbyClients);
								inGameMsg.add(exitMsgString);
								break;
							}
							if(onlineclients.contains(messageFrom)){
								onlineclients.remove(messageFrom);
								inLobbyClients.remove(locateClient(messageFrom, inLobbyClients));
								for(int i=0; i<onlineclients.size(); i++) {
									response.put(String.valueOf(i),onlineclients.get(i));
								}
								response.put("COMMAND", "USERLIST");
								response.put("NEWLOGOUT", messageFrom);
								sendAll(response, inLobbyClients);
								display.put("DISPLAY","User "+messageFrom+" logout");
								sendAll(display, inLobbyClients);
							}
							break;
							
						// Create the game
						case "CREATE":
							if (gameCreator == null) {
								gameCreator = messageFrom;
								inRoomClients.put(locateClient(messageFrom, inLobbyClients),
										inLobbyClients.get(locateClient(messageFrom, inLobbyClients)));
								response.put("CREATESTATUS", "1");
								sendToUser(response, messageFrom, inLobbyClients);
								System.out.println(response.toJSONString() + "\n");
								display.put("DISPLAY","User "+messageFrom+" created game room");
								sendAll(display, inLobbyClients);
								break;
							} else {
								response.put("CREATESTATUS", "2");
								System.out.println(response.toJSONString() + "\n");
								sendToUser(response, messageFrom, inLobbyClients);
								break;
							}

						// Invite user to the game
						case "INVITE":
							inviteFrom = messageFrom;
							inviteTo = (String) messageJSON.get("MESSAGE");
							response.put("INVITED", "");
							sendToUser(response, inviteTo, inLobbyClients);
							System.out.println(response.toJSONString() + "\n");
							display.put("DISPLAY","User "+inviteFrom+" invited user "+inviteTo);
							sendAll(display, inLobbyClients);
							break;

						// Response to an invitation
						case "ACCEPTANCE":
							String messageAccept = (String) messageJSON.get("MESSAGE");
							if (messageAccept.equals("YES")) {
								inRoomClients.put(locateClient(messageFrom, inLobbyClients),
										inLobbyClients.get(locateClient(messageFrom, inLobbyClients)));
								response.put("INVITEREPLYYES", "");
								sendToUser(response, inviteFrom, inLobbyClients);
								display.put("DISPLAY","User "+messageFrom+" accepted invitation from "+inviteFrom);
								sendAll(display, inLobbyClients);
								break;
							} else {
								response.put("INVITEREPLYNO", inviteTo);
								sendToUser(response, inviteFrom, inLobbyClients);
								display.put("DISPLAY","User "+messageFrom+" denied invitation from "+inviteFrom);
								sendAll(display, inLobbyClients);
								break;
							}
						
						// Start the game
						case "APPLYSTART":
							if (gameCreator.equals(messageFrom)) {
								response.put("COMMAND", "START");
								sendAll(response, inRoomClients);
								for(int i=0; i<inRoomClients.size(); i++){
									inGameClients.put(i,inRoomClients.get(i));
								}
								//inRoomClients.clear();
								startGame = true;
								display.put("DISPLAY","User "+messageFrom+" start the game");
								sendAll(display, inLobbyClients);
								break;
							} else {
								response.put("Message", "Only the room owner can start the game!");
								System.out.println(response.toJSONString() + "\n");
								sendToUser(response, messageFrom, inRoomClients);
								break;
							}
						}
					}
					if(startGame){
						Thread gameThread = new Thread(new Game(inGameClients, inGameMsg));
						gameThread.start();
						startGame = false;
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// locateClient will retrun the user id for users in the collection, if not, will return -1 instead
	private static int locateClient(String newclient,  Map<Integer, ConnectionToClient> clients) {
		for (ConnectionToClient c : clients.values()) {
			if (c.getUserName().equals(newclient)) {
				return c.getClientId();
			}
		}
		return -1;
	}
	
    // sendToUser will only send an JSONObject message to a specified user in the ConnectionToclient list
	private static void sendToUser(JSONObject message, String user, Map<Integer, ConnectionToClient> clients) {
		for (ConnectionToClient c : clients.values()) {
			if (c.getUserName().equals(user)) {
				c.sendMessage(message);
				break;
			}
		}
	}
	
    // sendAllFromOne will brocast an JSONObject message to all clients registered in ConnectionToclient list execpt a specified user
	private void sendAllFromOne(JSONObject message, String user, Map<Integer, ConnectionToClient> clients) {
		for (ConnectionToClient c : clients.values()) {
			if (!c.getUserName().equals(user)) {
				c.sendMessage(message);
			}
		}
	}

    // sendAll will brocast an JSONObject message to all clients registered in ConnectionToclient list
	private static void sendAll(JSONObject message, Map<Integer, ConnectionToClient> clients) {
		for (ConnectionToClient c : clients.values()) {
			c.sendMessage(message);
		}
	}
}
