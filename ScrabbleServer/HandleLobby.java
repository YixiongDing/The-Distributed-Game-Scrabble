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
    ConcurrentLinkedQueue<String> inGameMsg = new ConcurrentLinkedQueue<String>();;
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
            String lobbyMsg = this.inLobbyMsg.poll();
            boolean inLobby = true;

            if (lobbyMsg != null) {
                try {
                    System.out.println(lobbyMsg);
                    JSONObject messageJSON = (JSONObject) parser.parse(lobbyMsg);
                    String messageFrom = (String) messageJSON.get("USER");
                    String command = (String) messageJSON.get("COMMAND");
                    JSONObject response = new JSONObject();
                    JSONObject display = new JSONObject();

                    if(locateClient(messageFrom, inGameClients)!= -1 && !command.equals("NEWLOGOUT")&& !command.equals("APPLYSTART")){
                        inGameMsg.add(lobbyMsg);
                        inLobby = false;
                    }

                    if(inLobby){
                        switch (command) {
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

                            case "NEWLOGOUT":
                                if(locateClient(messageFrom, inGameClients)!= -1){
                                    //onlineclients.remove(messageFrom);
                                    inLobbyClients.remove(locateClient(messageFrom, inLobbyClients));
                                    inGameClients.remove(locateClient(messageFrom, inGameClients));
                                    JSONObject exitMsgJSON = new JSONObject();
                                    exitMsgJSON.put("COMMAND", "EXIT");
                                    String exitMsgString = exitMsgJSON.toJSONString();
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

                            case "INVITE":
                                    inviteFrom = messageFrom;
                                    inviteTo = (String) messageJSON.get("MESSAGE");
                                    response.put("INVITED", "");
                                    sendToUser(response, inviteTo, inLobbyClients);
                                    System.out.println(response.toJSONString() + "\n");
                                    display.put("DISPLAY","User "+inviteFrom+" invited user "+inviteTo);
                                    sendAll(display, inLobbyClients);
                                break;

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

    private static int locateClient(String newclient,  Map<Integer, ConnectionToClient> clients) {
        for (ConnectionToClient c : clients.values()) {
            if (c.getUserName().equals(newclient)) {
                return c.getClientId();
            }
        }
        return -1;
    }

    private static void sendToUser(JSONObject message, String user, Map<Integer, ConnectionToClient> clients) {
        for (ConnectionToClient c : clients.values()) {
            if (c.getUserName().equals(user)) {
                c.sendMessage(message);
                break;
            }
        }
    }

    private void sendAllFromOne(JSONObject message, String user, Map<Integer, ConnectionToClient> clients) {
        for (ConnectionToClient c : clients.values()) {
            if (!c.getUserName().equals(user)) {
                c.sendMessage(message);
            }
        }
    }

    private static void sendAll(JSONObject message, Map<Integer, ConnectionToClient> clients) {
        for (ConnectionToClient c : clients.values()) {
            c.sendMessage(message);
        }
    }
}
