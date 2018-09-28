import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class HandleClient implements Runnable {
	private Socket socket;
	private ConnectionToClient client;
	private ConcurrentLinkedQueue<String> messages;
	private JSONParser parser = new JSONParser();

	HandleClient(ConnectionToClient client,ConcurrentLinkedQueue<String> messages ) {
		this.client = client;
		this.messages = messages;
	}

	@Override
	public void run() {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader br = new BufferedReader(isr);
			while (true) {
				if(br != null) {
					try {
						JSONObject messageJSON = (JSONObject) parser.parse(br);
						messageJSON.put("USER", client.getUserName());
						String messageString = messageJSON.toJSONString();
						messages.add(messageString);
					}catch(ParseException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("connection lost");
		}

	}

}
