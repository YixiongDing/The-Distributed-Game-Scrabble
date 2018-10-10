package ScrabbleClient;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ScrabbleClient.MyClient;

public class LobbyConnectionToServer {
	
    private MyClient myClient;
    private Thread t;
    private JSONParser parser = new JSONParser();
	private lob lob;
	public static int createstatus = 0;
	
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    public LobbyConnectionToServer(lob lob2, MyClient myClient) {
        this.lob = lob2;
        this.myClient = myClient;
    }
    
    public void lobbyConnectionToServer() throws IOException {
        // while (true) {
        String content = new String();
    while(myClient.getBufferReader() != null) {
    	
        String message = myClient.getBufferReader().readLine();
        System.out.println("Server:" + message);
        try {
            JSONObject serverJSON = (JSONObject) parser.parse(message);
            if (serverJSON.get("COMMAND").equals("START")) {
                    Crossword c = new Crossword(myClient);
                    ListenThread t = new ListenThread(c, myClient);
                    t.start();


            }else if (serverJSON.containsKey("CREATESTATUS")) {
                 createstatus = (int) serverJSON.get("CREATESTATUS");

            }else if (serverJSON.containsKey("INVITED")) {
                Vote v = new Vote("     Being invited to a game, join?", myClient);
                v.setVisible(true);
           
        }else if (serverJSON.containsKey("USERLIST")) {
        	ArrayList <String> list = (ArrayList <String>) serverJSON.get("USERLIST");
        	int size=list.size();
        	String[] userlist = (String[])list.toArray(new String[size]);
        }
        else if (serverJSON.containsKey("MESSAGES")) {
        	String messages = (String)serverJSON.get("MESSAGES");
        	lob.textappend(sdf.format(new Date()) + messages+ "\n");
        }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
}
