package ScrabbleClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ScrabbleClient.MyClient;

public class LobbyConnectionToServer {
	
    private MyClient myClient;
    private Thread aaa;
    private JSONParser parser = new JSONParser();
	private lob lob;
	public static int createstatus = 0;
	public static int loginstatus = 0;
	public static ArrayList <String> userlist;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    public LobbyConnectionToServer(lob lob2, MyClient myClient) {
        this.lob = lob2;
        this.myClient = myClient;
    }
    
    public void lobbyConnectionToServer() throws IOException {
        // while (true) {

    while(myClient.getBufferReader() != null) {
    	
        String message = myClient.getBufferReader().readLine();
        System.out.println("Server:" + message);
        
        readMsg(message);
        }
    }
    public void readMsg(String message) {
    	 try {
             JSONObject serverJSON = (JSONObject) parser.parse(message);
             System.out.println("! "+serverJSON.toJSONString());
             if (serverJSON.containsKey("COMMAND")) {
             if (serverJSON.get("COMMAND").equals("START")) {
                 System.out.println("starting game");


             }
             }else if (serverJSON.containsKey("CREATESTATUS")) {
                  createstatus = Integer.parseInt(serverJSON.get("CREATESTATUS").toString());
                  if(createstatus==0) {
                      new MessageUI("Waiting for the server to respond, please try again later.");

                  }else if(createstatus==1) {
                      new MessageUI("Create a game room successfully.");
                      lob.setlobby(true);
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
                     new MessageUI("login successfully.");
                     lob.turnCreateButton(false);
                     lob.turnStartButton(true);
                 }else if(loginstatus==2) {
                     new MessageUI("Username already exists, please re-enter");
                 }
                 
                 
                 
                 System.out.println(createstatus);
            }else if (serverJSON.containsKey("INVITED")) {
                 Vote v = new Vote("     Being invited to a game, join?", myClient);
                 v.setVisible(true);
            
         }else if (serverJSON.containsKey("USERLIST")) {
         	 userlist = (ArrayList <String>) serverJSON.get("USERLIST");

         }
         else if (serverJSON.containsKey("MESSAGE")) {
         	String messages = (String)serverJSON.get("MESSAGE");
         	lob.textappend(sdf.format(new Date()) + messages+ "\n");
         }

         } catch (ParseException e) {
             // TODO Auto-generated catch block
         	System.out.println("ParseException");
             e.printStackTrace();
         }
    }
    
    
    
    public static ArrayList <String> getuserlist() {
        return userlist;
    }
}
/*        
        
        try {
            JSONObject serverJSON = (JSONObject) parser.parse(message);
            System.out.println("! "+serverJSON.toJSONString());

            if (serverJSON.get("COMMAND").equals("START")) {
                    Crossword c = new Crossword(myClient);
                    ListenThread t = new ListenThread(c, myClient);
                    t.start();


            }else if (serverJSON.containsKey("CREATESTATUS")) {
                 createstatus = (int) serverJSON.get("CREATESTATUS");
                 System.out.println(createstatus);
            }else if (serverJSON.containsKey("INVITED")) {
                Vote v = new Vote("     Being invited to a game, join?", myClient);
                v.setVisible(true);
           
        }else if (serverJSON.containsKey("USERLIST")) {
        	ArrayList <String> list = (ArrayList <String>) serverJSON.get("USERLIST");
        	int size=list.size();
        	String[] userlist = (String[])list.toArray(new String[size]);
        }
        else if (serverJSON.containsKey("MESSAGE")) {
        	String messages = (String)serverJSON.get("MESSAGE");
        	lob.textappend(sdf.format(new Date()) + messages+ "\n");
        }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
        	System.out.println("ParseException");
            e.printStackTrace();
        }
    }
}
}
*/