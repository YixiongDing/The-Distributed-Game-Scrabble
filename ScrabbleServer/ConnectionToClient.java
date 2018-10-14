// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 

// ConnectionToClient.java is a Client connection related class, which store connection information, and enable 
// the Server communicate with client through sendMessage and getMessage methods.

package ScrabbleServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConnectionToClient {
	private InputStream in;
	private OutputStream out;
	private Socket socket;
	private int clientId;
	private BufferedReader br;
	private BufferedWriter bw;
	private JSONParser parser = new JSONParser();
	private String userName;
	
	public ConnectionToClient(int id, Socket socket, String userName) {
		this.clientId = id;
		this.socket = socket;
		this.userName = userName;
		try {
			this.in = this.socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			this.br = new BufferedReader(isr);
			this.out = this.socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out);
            this.bw = new BufferedWriter(osw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.out = out;
	}
	// getClientId will return Client ID
	public int getClientId() {
		return clientId;
	}
	// getReader will return bufferedreader
	protected BufferedReader getReader() {
		return br;
	}
	// getWriter will return bufferedwriter
	protected BufferedWriter getWriter() {
		return bw;
	}
	// getSocket will return connection socket
	protected Socket getSocket() {
		return socket;
	}
	// getUsername will return client's username
	protected String getUserName() {
		return this.userName;
	}
	// sendMessage will send JSONObject message to the client
	protected boolean sendMessage(JSONObject message) {
		try {
			this.bw.write(message.toJSONString()+"\n");
			this.bw.flush();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	// getMessage will read message from the client
	protected JSONObject getMessage() {
		try {
			String inputRead = br.readLine();
			if (inputRead != null) { 
            	JSONObject messageJSON = (JSONObject) parser.parse(inputRead);
		        String message = (String) messageJSON.get("COMMAND"); 
		        String word = (String) messageJSON.get("WORD");
				return messageJSON;
			}else {
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}
}