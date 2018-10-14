// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 

//ConnectionToServer.java is to make connection to the server and read the message from server

package ScrabbleClient;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ScrabbleClient.MyClient;

public class ConnectionToServer {

	private MyClient myClient;
	private JSONParser parser = new JSONParser();
	private Crossword cw;
	private lob lob;
	public static int createstatus = 0;
	public static int loginstatus = 0;
	public static ArrayList <String> userlist;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public ConnectionToServer(lob lob,Crossword cw, MyClient myClient) {
		this.cw = cw;
		this.lob=lob;
		this.myClient = myClient;
	}

	public void connectionToServer() {
		//listen the message received from the server
		while (myClient.getBufferReader() != null) {
			String message;
			try {
				message = myClient.getBufferReader().readLine();
				System.out.println("Server:" + message);
				readMsg(message);
			} catch (Exception e) {
				new MessageUI("Server is unavailable",myClient.getUserName());
				break;
			}

		}
	}
	public void readMsg(String message) {
		try {
			//handle the message received from the server
			JSONObject serverJSON = (JSONObject) parser.parse(message);
			if (serverJSON.containsKey("CREATESTATUS")) {
				//Createstatus Protocol
				createstatus = Integer.parseInt(serverJSON.get("CREATESTATUS").toString());
				if(createstatus==0) {
					new MessageUI("Waiting for the server to respond, please try again later.",myClient.getUserName());

				}else if(createstatus==1) {
					new MessageUI("Create a game room successfully.",myClient.getUserName());

					lob.turnCreateButton(false);
					lob.turnStartButton(true);
					lob.inviteButton(true);
				}else if(createstatus==2) {
					new MessageUI("There is a game in progress, please try again later.",myClient.getUserName());
				}



				System.out.println(createstatus);
			}
			else if (serverJSON.containsKey("LOGINSTATUS")) {
				//Loginstatus Protocol
				loginstatus = Integer.parseInt(serverJSON.get("LOGINSTATUS").toString());
				if(loginstatus==0) {
					new MessageUI("Waiting for the server to respond, please try again later.",myClient.getUserName());

				}else if(loginstatus==1) {

					lob.setlobby(true);
					new MessageUI("login successfully.",myClient.getUserName());
				}else if(loginstatus==2) {
					new MessageUI("Username already exists, please re-enter",myClient.getUserName());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.exit(0);

				}



				System.out.println(createstatus);
			}
			else if (serverJSON.containsKey("INVITED")) {
				//Invited Protocol

				Invited v = new Invited("     Being invited to a game, join?", myClient);
				v.setVisible(true);
				
				//Invite reply Protocol

			}else if (serverJSON.containsKey("INVITEREPLYYES")) {
				String user = (String)serverJSON.get("INVITEREPLYYES");
				new MessageUI("Invite "+user+" successfully",myClient.getUserName());

			}
			else if (serverJSON.containsKey("INVITEREPLYNO")) {
				String user = (String)serverJSON.get("INVITEREPLYNO");
				new MessageUI(user+" refused.",myClient.getUserName());

			}
			else if (serverJSON.containsKey("DISPLAY")) {
				//Information display protocol
				String messages = (String) serverJSON.get("DISPLAY");
				lob.textappend(sdf.format(new Date())+ "   "+messages+"\n");

			}else if (serverJSON.containsKey("TURN")) {
				//Turn Protocol
				cw.setTurnLabel("                   It's " + serverJSON.get("TURN") + "'s turn");
				if (serverJSON.get("TURN").equals(myClient.getUserName())) {
					cw.setTurn(true);
					Crossword.CrosswordPanel.setColorAll();

				}
			} else if (serverJSON.containsKey("VOTE")) {
				//Vote INIT Protocol
				String voteWord = (String) serverJSON.get("VOTE");
				Vote v = new Vote("     Is \"" + voteWord + "\" a word?", myClient);
				v.setVisible(true);

			} else if (serverJSON.containsKey("COMMAND")) {
				if (serverJSON.get("COMMAND").equals("START")) {
					//Start game protocol
					cw.setcrossword(true);
				}
				if (serverJSON.get("COMMAND").equals("NEW")) {
					//New Letter Protocol
					JSONObject newLetter = (JSONObject) serverJSON.get("MESSAGE");
					int index = ((Long) newLetter.get("INDEX")).intValue();
					String letter = (String) newLetter.get("LETTER");
					Crossword.CrosswordPanel.textFields.get(index).setText(letter);
				}
				if (serverJSON.get("COMMAND").equals("USERLIST")) {
					//userlist display protocol
					int count = 0;
					lob.setuserlist();
					while(true) {
						if(serverJSON.containsKey(String.valueOf(count))) { 
							lob.appenduser((String) serverJSON.get(String.valueOf(count))+"\n");
							count += 1;
						}else {
							break;
						}
					}


				}
				if (serverJSON.get("COMMAND").equals("COORD")) {
					// COORD Protocol
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

			else if (serverJSON.containsKey("SCOREBOARD")) {
				//Scoreboard Protocol
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


			}



			else if (serverJSON.containsKey("END")) {
				//Game End Protocol
				cw.setTurnLabel("                   Game ended");

				ArrayList<String> userName_list = new ArrayList<String>();
				JSONObject a = new JSONObject();
				a = (JSONObject) serverJSON.get("END");
				for (Object key : a.keySet()) {
					String sc = a.get(key).toString();
					userName_list.add("Username: " + key.toString() + ";    Score:" + sc);
					System.out.println(sc);
				}
				Crossword.CrosswordPanel.removeALL();
				lob.setlobby(true);
				cw.setVisible(false);
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