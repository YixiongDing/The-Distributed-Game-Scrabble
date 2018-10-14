// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 

//ConnectionToServer.java is to make connection to the server and read the message from server

package ScrabbleClient;

import java.awt.Color;
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
			String content = new String();
			while (myClient.getBufferReader() != null) {
				String message = myClient.getBufferReader().readLine();
				System.out.println("Server:" + message);
				readMsg(message);
			}
	}

	//handle the message received from the server
	public void readMsg(String message) {
		try {
			//Turn Protocol
			JSONObject serverJSON = (JSONObject) parser.parse(message);
			if (serverJSON.containsKey("TURN")) {
				cw.setTurnLabel("                   It's " + serverJSON.get("TURN") + "'s turn");
				if (serverJSON.get("TURN").equals(myClient.getUserName())) {
					cw.setTurn(true);
                    Crossword.CrosswordPanel.setColorAll();
				}
				//Vote INIT Protocol

			} else if (serverJSON.containsKey("VOTE")) {
				String voteWord = (String) serverJSON.get("VOTE");
				Vote v = new Vote("     Is \"" + voteWord + "\" a word?", myClient);
				v.setVisible(true);

				//New Letter Protocol

			} else if (serverJSON.containsKey("COMMAND")) {
				if (serverJSON.get("COMMAND").equals("NEW")) {
					JSONObject newLetter = (JSONObject) serverJSON.get("MESSAGE");
					int index = ((Long) newLetter.get("INDEX")).intValue();
					String letter = (String) newLetter.get("LETTER");
					Crossword.CrosswordPanel.textFields.get(index).setText(letter);
				}

				// COORD Protocol

				if (serverJSON.get("COMMAND").equals("COORD")) {
					JSONObject Coord = (JSONObject) serverJSON.get("MESSAGE");
					int lastX =  ((Long) Coord.get("X1")).intValue();
					int lastY =  ((Long) Coord.get("Y1")).intValue();
					int xco =  ((Long) Coord.get("X2")).intValue();
					int yco =  ((Long) Coord.get("Y2")).intValue();

					int index;

					if ( xco == lastX  && yco >= lastY) {
						for (int i = lastY; i <= yco; i++) {
							index = (xco - 1) * 20 + i - 1;
							Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
						}

					} else if (  xco == lastX && yco <= lastY
							) {
						for (int i = yco; i <= lastY; i++) {
							index = (xco - 1) * 20 + i - 1;
							Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
						}

					}

					else if ( yco == lastY && lastX <= xco
							) {
						for (int i = lastX; i <= xco; i++) {
							index = (i - 1) * 20 + yco - 1;
							Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
						}

					} else if ( yco == lastY && lastX >= xco
							) {
						for (int i = xco; i <= lastX; i++) {
							index = (i - 1) * 20 + yco - 1;
							Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
						}
					}
				}
			}
			//Scoreboard Protocol

			else if (serverJSON.containsKey("SCOREBOARD")) {
				ArrayList <String> userName_list = new ArrayList<String>();
				JSONObject a = new JSONObject();
				a = (JSONObject) serverJSON.get("SCOREBOARD");
				for (Object key:  a.keySet()) {
					String sc=a.get(key).toString();
					userName_list.add("Username: " + key.toString()+" ;Score:" + sc+";");  
					System.out.println(sc);
				}
				System.out.println(userName_list);
				Crossword.setScorebd(userName_list);
			//Game End Protocol
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
