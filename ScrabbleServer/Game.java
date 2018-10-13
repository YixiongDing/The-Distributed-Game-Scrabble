// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 

//Game.java is a thread class that handle clients request during gaming phase. Game thread will process 
//data sent from clients, and broadcast game related information to other clients

package ScrabbleServer;
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
    private String word_vote = null;

    public Game(Map<Integer, ConnectionToClient> clients, ConcurrentLinkedQueue<String> queue) {
        // TODO Auto-generated constructor stub
        this.clients = clients.values();
        this.queue = queue;
    }

    @Override
    public void run() {
        this.init();
        this.sendNextTurn();
        this.sendScoreBoard();
        while (true) {
            // Thread read request from queue
            String message = this.queue.poll();
            // Thread process requests and broadcast information to other clients
            if (message != null) {
                try {
                    JSONObject messageJSON = (JSONObject) parser.parse(message);
                    String username = (String) messageJSON.get("USER");
                    JSONObject messageUser = (JSONObject) messageJSON.get("MESSAGE");
                    String command = (String) messageJSON.get("COMMAND");
                    System.out.println(command);
                    System.out.println(username + ":" + command + "," + messageUser);// test info
                    if (command.equals("NEW")) {
                        JSONObject sendmessage = messageJSON;
                        this.sendAllFromOne(sendmessage, username);
                        this.initPassCount();
                        // send turn to all
                    } 	else if (command.equals("COORD")) {
                        JSONObject sendmessage = messageJSON;
                        this.sendAllFromOne(sendmessage, username);
                        // send turn to all
                    }


                    else if (command.equals("VOTING")) {
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
                                    this.updateScore(username, this.word_vote.length());
                                    JSONObject voteResult = new JSONObject();
                                    voteResult.put("VOTE_RESULT", "YES");
                                    this.sendAll(voteResult);
                                    this.sendScoreBoard();
                                    this.word_vote = null;
                                    this.initVoteCount();
                                    this.initVoteYes();
                                    this.sendNextTurn();
                                } else {
                                    JSONObject voteResult = new JSONObject();
                                    voteResult.put("VOTE_RESULT", "NO");
                                    this.sendAll(voteResult);
                                    this.word_vote = null;
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
                            this.word_vote = (voteRequest.get("INIT")).toString();
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
                        JSONObject display = new JSONObject();
                        display.put("DISPLAY","game ended");
                        this.sendAll(display);
                        break;
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    // sendAll will brocast an JSONObject message to all clients registered in ConnectionToclient list
    private void sendAll(JSONObject message) {
        for (ConnectionToClient c : this.clients) {
            c.sendMessage(message);
        }
    }
    // sendAllFromOne will brocast an JSONObject message to all clients registered in ConnectionToclient list execpt a specified user
    private void sendAllFromOne(JSONObject message, String user) {
        for (ConnectionToClient c : this.clients) {
            if (!c.getUserName().equals(user)) {
                c.sendMessage(message);
            }
        }
    }
    // sendToUser will only send an JSONObject message to a specified user in the ConnectionToclient list
    private void sendToUser(JSONObject message, String user) {
        for (ConnectionToClient c : this.clients) {
            if (c.getUserName().equals(user)) {
                c.sendMessage(message);
                break;
            }
        }
    }
    // init will init the game state at the begining of the game
    private void init() {
        this.initPassCount();
        this.initVoteCount();
        this.initVoteYes();
        this.turn = 0;
        this.getNamelist();
        this.initScoreBoard();
    }
    // getNamelist will initilise the list of username
    private void getNamelist() {
        for (ConnectionToClient c : this.clients) {
            this.nameList.add(c.getUserName());
        }
    }
    // initPassCount will initilise the passCount variable
    private void initPassCount() {
        this.passcount = 0;
    }
    // initScoreBoard will initilise the ScoreBoard
    private void initScoreBoard() {
        for (String c : this.nameList) {
            this.scoreboard.put(c, 0);
        }
    }
    // initVoteCount will initilise the VoteCount variable
    private void initVoteCount() {
        this.votecount = 0;
    }
    // initVoteYes will initilise the VoteYes variable
    private void initVoteYes() {
        this.voteYes = 0;
    }
    // updateScore will update score for a user if the user gain a score
    private void updateScore(String user, int score){
        this.scoreboard.put(user, Integer.valueOf(this.scoreboard.get(user).toString()) + score);
    }
    // sendNextTurn will broadcst turn information to all clients
    private void sendNextTurn() {
        this.turn = this.turn + 1;
        String username = this.nameList.get(this.turn % this.nameList.size());
        JSONObject userturn = new JSONObject();
        userturn.put("TURN", username);
        this.sendAll(userturn);
    }
    // sendScoreBoard will brocast latest Scoreboad information to all clients
    private void sendScoreBoard(){
        JSONObject sendScore = new JSONObject();
        sendScore.put("SCOREBOARD",scoreboard);
        this.sendAll(sendScore);
    }
}