package ScrabbleClient;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ScrabbleClient.MyClient;

public class ConnectionToServer {
    private MyClient myClient;
    private Thread t;
    private JSONParser parser = new JSONParser();
    private Crossword cw;

    public ConnectionToServer(Crossword cw, MyClient myClient) {
        this.cw = cw;
        this.myClient = myClient;
    }

    public void connectionToServer() throws IOException {
        // while (true) {
        String content = new String();
        while (myClient.getBufferReader() != null) {
            String message = myClient.getBufferReader().readLine();
            System.out.println("Server:" + message);
            readMsg(message);
        }
        // System.out.println(1);
        // }
    }

    public void readMsg(String message) {
        try {
            JSONObject serverJSON = (JSONObject) parser.parse(message);
            if (serverJSON.containsKey("TURN")) {
                if (serverJSON.get("TURN").equals(myClient.getUserName())) {
                    // PromptWindow v = new PromptWindow("It is your turn! Please input!");
                    // v.setVisible(true);
                    cw.setTurnLabel("                   It's " + serverJSON.get("TURN") + "'s turn");

                    cw.setTurn(true);
                }
            } else if (serverJSON.containsKey("VOTE")) {
                String voteWord = (String) serverJSON.get("VOTE");
                Vote v = new Vote("     Is \"" + voteWord + "\" a word?", myClient);
                v.setVisible(true);

            } else if (serverJSON.containsKey("COMMAND")) {
                if (serverJSON.get("COMMAND").equals("NEW")) {
                    JSONObject newLetter = (JSONObject) serverJSON.get("MESSAGE");
                    int index = ((Long) newLetter.get("INDEX")).intValue();
                    String letter = (String) newLetter.get("LETTER");
                    Crossword.CrosswordPanel.textFields.get(index).setText(letter);
                }

            }

            else if (serverJSON.containsKey("SCORE")) {
                ArrayList<String> userName_list = new ArrayList<String>();
                JSONObject a = new JSONObject();
                a = (JSONObject) serverJSON.get("SCORE");
                for (Object key : a.keySet()) {
                    String sc = a.get(key).toString();
                    userName_list.add("Username: " + key.toString() + ";    Score:" + sc);
                    System.out.println(sc);
                }
                System.out.println(userName_list);

                Scoreboard v = new Scoreboard(userName_list);
                v.setVisible(true);

            }

            else if (serverJSON.containsKey("END")) {
                ArrayList<String> userName_list = new ArrayList<String>();
                JSONObject a = new JSONObject();
                a = (JSONObject) serverJSON.get("END");
                for (Object key : a.keySet()) {
                    String sc = a.get(key).toString();
                    userName_list.add("Username: " + key.toString() + ";    Score:" + sc);
                    System.out.println(sc);
                }
                System.out.println(userName_list);

                Scoreboard v = new Scoreboard(userName_list);
                v.setVisible(true);

            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
