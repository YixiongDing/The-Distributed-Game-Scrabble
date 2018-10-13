package ScrabbleClient;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ScrabbleClient.MyClient;

public class ConnectionToServer {
	private MyClient myClient;
	private Thread t;
	private JSONParser parser = new JSONParser();
	private Crossword cw;
	private lob lob;
    public static DataModels model;
	public static int createstatus = 0;
	public static int loginstatus = 0;
	public static ArrayList <String> userlist;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public ConnectionToServer(lob lob,Crossword cw, MyClient myClient) {
		this.cw = cw;
		this.lob=lob;
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
    public static ArrayList <String> getuserlist() {
        return userlist;
    }
    class DataModels extends DefaultListModel
    {
   	

        public DataModels()
        {
       	 if (ConnectionToServer.getuserlist() != null) {
            for (int i = 0; i < ConnectionToServer.getuserlist().size(); i++) 
            {

           		 addElement(ConnectionToServer.getuserlist().get(i));
           	 
            }
        }
       }
    }

	public void readMsg(String message) {
		try {
			JSONObject serverJSON = (JSONObject) parser.parse(message);
            if (serverJSON.containsKey("COMMAND")) {
            if (serverJSON.get("COMMAND").equals("START")) {
                cw.setcrossword(true);


            }
            }else if (serverJSON.containsKey("CREATESTATUS")) {
                 createstatus = Integer.parseInt(serverJSON.get("CREATESTATUS").toString());
                 if(createstatus==0) {
                     new MessageUI("Waiting for the server to respond, please try again later.");

                 }else if(createstatus==1) {
                     new MessageUI("Create a game room successfully.");
                     lob.turnCreateButton(false);
                     lob.turnStartButton(true);
                 }else if(createstatus==2) {
                     new MessageUI("There is a game in progress, please try again later.");
                 }
                 
                 
                 
                 System.out.println(createstatus);
            }else if (serverJSON.containsKey("LOGINSTATUS")) {
           	 loginstatus = Integer.parseInt(serverJSON.get("LOGINSTATUS").toString());
                if(loginstatus==0) {
                    new MessageUI("Waiting for the server to respond, please try again later.");

                }else if(loginstatus==1) {

                    lob.setlobby(true);
                    lob.turnStartButton(true);
                    new MessageUI("login successfully.");
                }else if(loginstatus==2) {
                    new MessageUI("Username already exists, please re-enter");
                }
                
                
                
                System.out.println(createstatus);
           }else if (serverJSON.containsKey("INVITED")) {
                Invited v = new Invited("     Being invited to a game, join?", myClient);
                v.setVisible(true);
           
        }
        else if (serverJSON.containsKey("MESSAGE")) {
        	String messages = (String)serverJSON.get("MESSAGE");
        	lob.textappend(sdf.format(new Date()) + messages+ "\n");
        }else if (serverJSON.containsKey("TURN")) {
				cw.setTurnLabel("                   It's " + serverJSON.get("TURN") + "'s turn");
				if (serverJSON.get("TURN").equals(myClient.getUserName())) {
					// PromptWindow v = new PromptWindow("It is your turn! Please input!");
					// v.setVisible(true);
					cw.setTurn(true);
                    Crossword.CrosswordPanel.setColorAll();

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
				if (serverJSON.get("COMMAND").equals("USERLIST")) {
		        	 int count = 0;
		        	 lob.setuserlist();
		        	 while(true) {
		        		 if(serverJSON.containsKey(String.valueOf(count))) { 
		        			 lob.appenduser((String) serverJSON.get(String.valueOf(count))+"/n");
		        			 count += 1;
		        		 }else {
		        			 break;
		        		 }
		        	 }


		        }
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
				// Scoreboard v = new Scoreboard(userName_list);
				//v.setVisible(true);


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