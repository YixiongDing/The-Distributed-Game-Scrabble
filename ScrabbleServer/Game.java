import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Game implements Runnable {
    private Collection<ConnectionToClient> clients;
    private JSONParser parser = new JSONParser();
    List<String> nameList = new ArrayList<>();
    JSONObject scoreboard = new JSONObject();
    ConcurrentLinkedQueue<String> queue;
    private int passcount;
    private int voteYes;
    private int votecount;
    private int turn;

    public Game(Map<Integer, ConnectionToClient> clients, ConcurrentLinkedQueue<String> queue) {
        // TODO Auto-generated constructor stub
        this.clients = clients.values();
        this.queue = queue;
    }

    @Override
    public void run() {
        this.init();
        this.sendNextTurn();
        while (true) {
            String message = this.queue.poll();
            if (message != null) {
                try {
                    // System.out.println(message);
                    JSONObject messageJSON = (JSONObject) parser.parse(message);
                    String username = (String) messageJSON.get("USER");
                    JSONObject messageUser = (JSONObject) messageJSON.get("MESSAGE");
                    String command = (String) messageJSON.get("COMMAND");
                    System.out.println(command);
                    System.out.println(username + ":" + command + "," + messageUser);// test info
                    if (command.equals("NEW")) {
                        // new
                        JSONObject sendmessage = messageJSON;// (JSONObject) messageUser.get("NEW");
                        this.sendAllFromOne(sendmessage, username);
                        this.initPassCount();
                        // send turn to all
                    } else if (command.equals("VOTING")) {
                        // JSONObject voteRequest = (JSONObject) messageUser.get("VOTING");
                        JSONObject voteRequest = messageUser;
                        // vote process
                        if (voteRequest.containsKey("VOTE")) {
                            this.votecount += 1;
                            if (voteRequest.get("VOTE").equals("YES")) {
                                this.voteYes += 1;
                            }
                            // decide process
                            if (this.votecount >= this.nameList.size()) {
                                if (this.voteYes >= (this.votecount / 2)) {
                                    JSONObject voteResult = new JSONObject();
                                    voteResult.put("VOTE_RESULT", "YES");
                                    this.sendAll(voteResult);
                                    this.initVoteCount();
                                    this.initVoteYes();
                                    this.sendNextTurn();
                                } else {
                                    JSONObject voteResult = new JSONObject();
                                    voteResult.put("VOTE_RESULT", "NO");
                                    this.sendAll(voteResult);
                                    this.initVoteCount();
                                    this.initVoteYes();
                                    this.sendNextTurn();
                                }
                            }
                        } else if (voteRequest.containsKey("INIT")) {
                            this.votecount += 1;
                            this.voteYes += 1;
                            JSONObject sendmessage = new JSONObject();
                            sendmessage.put("VOTE", voteRequest.get("INIT"));
                            this.sendAllFromOne(sendmessage, username);
                            this.initPassCount();
                        }
                    } else if (command.equals("PASS")) {
                        this.passcount += 1;
                        if (this.passcount >= this.nameList.size()) {
                            JSONObject endmessage = new JSONObject();
                            endmessage.put("END", scoreboard);
                            this.sendAll(endmessage);
                            break;
                        }
                        this.sendNextTurn();
                    } else if (command.equals("SCORE")) {
                        JSONObject scoreMessage = new JSONObject();
                        scoreMessage.put("SCORE", scoreboard);
                        this.sendToUser(scoreMessage, username);
                    } else if (command.equals("EXIT")) {
                        JSONObject endmessage = new JSONObject();
                        endmessage.put("END", scoreboard);
                        this.sendAll(endmessage);
                        break;
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendAll(JSONObject message) {
        for (ConnectionToClient c : this.clients) {
            c.sendMessage(message);
        }
    }

    private void sendAllFromOne(JSONObject message, String user) {
        for (ConnectionToClient c : this.clients) {
            if (!c.getUserName().equals(user)) {
                c.sendMessage(message);
            }
        }
    }

    private void sendToUser(JSONObject message, String user) {
        for (ConnectionToClient c : this.clients) {
            if (c.getUserName().equals(user)) {
                c.sendMessage(message);
                break;
            }
        }
    }

    private void init() {
        this.initPassCount();
        this.initVoteCount();
        this.initVoteYes();
        this.turn = 0;
        this.getNamelist();
        this.initScoreBoard();
    }

    private void getNamelist() {
        for (ConnectionToClient c : this.clients) {
            this.nameList.add(c.getUserName());
        }
    }

    private void initPassCount() {
        this.passcount = 0;
    }

    private void initScoreBoard() {
        for (String c : this.nameList) {
            this.scoreboard.put(c, 0);
        }
    }

    private void initVoteCount() {
        this.votecount = 0;
    }

    private void initVoteYes() {
        this.voteYes = 0;
    }

    private void sendNextTurn() {
        this.turn = this.turn + 1;
        String username = this.nameList.get(this.turn % this.nameList.size());
        JSONObject userturn = new JSONObject();
        userturn.put("TURN", username);
        this.sendAll(userturn);
    }
}