package ScrabbleServer;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HandleLobby implements Runnable {
    private Map<Integer, ConnectionToClient> idleClients;
    private Map<Integer, ConnectionToClient> readyClients;

    private JSONParser parser = new JSONParser();
    List<String> nameList = new ArrayList<>();
    ConcurrentLinkedQueue<String> inLobbyMsg;
    ConcurrentLinkedQueue<String> inGameMsg;

    Map<Integer, ConnectionToClient> gameClients = new HashMap<Integer, ConnectionToClient>();


    public HandleLobby(Map<Integer, ConnectionToClient> idleClients, ConcurrentLinkedQueue<String> inLobbyMsg, Map<Integer,
            ConnectionToClient> readyClients, ConcurrentLinkedQueue<String> inGameMsg) {
        this.idleClients = idleClients;
        this.readyClients = readyClients;
        this.inLobbyMsg = inLobbyMsg;
        this.inGameMsg = inGameMsg;
    }

    @Override
    public void run() {
        while (true) {
            String lobbyMsg = this.inLobbyMsg.poll();
            if (lobbyMsg != null) {
                try {
                    System.out.println("check 3\n");

                    System.out.println(lobbyMsg);
                    JSONObject messageJSON = (JSONObject) parser.parse(lobbyMsg);
                    String messageFrom = (String) messageJSON.get("USER");
                    String command = (String) messageJSON.get("COMMAND");
                    String gameCreator = null;
                    String inviteFrom = null;
                    String inviteTo = null;
                    Boolean startGame = false;
                    System.out.println("here "+lobbyMsg+"\n");
                    JSONObject response = new JSONObject();

                    System.out.println(command);
                    System.out.println(messageFrom + ":" + command + "\n");

                    switch (command) {

                        case "CREATE":
                            if(gameCreator == null){
                                gameCreator = messageFrom;
                                gameClients.put(locateClient(messageFrom, idleClients),
                                        idleClients.get(locateClient(messageFrom, idleClients)));
                                idleClients.remove(locateClient(messageFrom, idleClients));
                                break;
                            }else{
                                response.put("CREATESTATUS", "2");
                                System.out.println(response.toJSONString()+"\n");
                                sendToUser(response, messageFrom, idleClients);
                                break;
                            }

                        case "JOIN":
                            break;

                        case "INVITE":
                            String messageTo = (String) messageJSON.get("MESSAGE");
                            inviteFrom = messageFrom;
                            inviteTo = messageTo;
                            response.put("INVITED", "1");
                            sendToUser(response, messageTo, idleClients);
                            System.out.println(response.toJSONString()+"\n");
                            break;

                        case "ACCEPTANCE":
                            String messageAccept = (String) messageJSON.get("MESSAGE");
                            if(messageAccept.equals("YES")) {
                                readyClients.put(locateClient(inviteTo, idleClients),
                                        idleClients.get(locateClient(inviteTo, idleClients)));
                                idleClients.remove(locateClient(inviteTo, idleClients));
                                break;
                            }else {
                                sendToUser(response, inviteFrom, readyClients);
                                break;
                            }

                        case "APPLYSTART":
                            if(gameCreator.equals(messageFrom)) {
                                response.put("COMMAND","START");
                                sendAll(response, readyClients);
                                startGame = true;
                                break;
                            }else {
                                response.put("Message", "Only the room owner can start the game!");
                                System.out.println(response.toJSONString()+"\n");
                                sendToUser(response, messageFrom, readyClients);
                                break;
                            }
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    private static int locateClient(String newclient,  Map<Integer, ConnectionToClient> clients) {
        for (ConnectionToClient c : clients.values()) {
            if (c.getUserName().equals(newclient)) {
                return c.getClientId();
            }
        }
        return 99;
    }

    private static void sendToUser(JSONObject message, String user, Map<Integer, ConnectionToClient> clients) {
        for (ConnectionToClient c : clients.values()) {
            if (c.getUserName().equals(user)) {
                c.sendMessage(message);
                break;
            }
        }
    }

    private static void sendAll(JSONObject message, Map<Integer, ConnectionToClient> clients) {
        for (ConnectionToClient c : clients.values()) {
            c.sendMessage(message);
        }
    }
}
