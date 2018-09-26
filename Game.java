import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Game implements Runnable{
	private Map<Integer, ConnectionToClient> clients;
	ConcurrentLinkedQueue<JSONObject> queue;
	public Game(Map<Integer, ConnectionToClient> clients, ConcurrentLinkedQueue<JSONObject> queue) {
		// TODO Auto-generated constructor stub
		this.clients = clients;
		this.queue = queue;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if (queue:) {
				loop send
			}
		}
	}
}
