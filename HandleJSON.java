//package Client;

import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;


public class HandleJSON {

	private static MyClient myClient;
	private static JSONObject jsFromServer;
	private static JSONObject jsSendServer;
	
	private static String command;
	private static String commandMsg;
	private static JSONObject commandJS;
	
	//JSONObject meaningJSON = (JSONObject) parser.parse(bufferRead.readLine());
	//							String check = (String) meaningJSON.get("CHECK");
////							String check = bufferRead.readLine();
	/*
	System.out.println(check);
	if (check.equals("YES")) {
		new MessageUI("Successfully deleted");
		frame.dispose();
		//warning 
	}else {
		new MessageUI("This word is not in the dictionary");
	}

	*/
	
	public HandleJSON() {
		
		
		
	}
	
	
	public static void getCommand(MyClient myClient) {
		
	}
	
	
	public static void sendCommand() {
		if (commandMsg.equals("")) {
			
		}
		
		else {
			
		}
	}

	
}
