package ScrabbleServer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;

public class Server {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            ServerSocketFactory factory = ServerSocketFactory.getDefault();
            ServerSocket server = factory.createServerSocket(port);
            Map<Integer, ConnectionToClient> idleClients = new HashMap<Integer, ConnectionToClient>();
            Map<Integer, ConnectionToClient> readyClients = new HashMap<Integer, ConnectionToClient>();
            ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue<String>();
            GameAssistant gameAssitant = null;
            String username = null;
            int id = 0;
            boolean startGame = false;
            System.out.println("Server is up!");

            while (true) {
                Socket client = server.accept();
                DataInputStream input = new DataInputStream(client.getInputStream());
                DataOutputStream output = new DataOutputStream(client.getOutputStream());
                InputStreamReader isr = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(isr);
                username = br.readLine();

                if (checkClient(username, idleClients)) {
                    System.out.println(username + "has already logged in!");
                    output.writeUTF(username + "has already logged in!\n");
                } else {
                    System.out.println(username + " connected");
                    ConnectionToClient newclient = new ConnectionToClient(id, client, username);
                    idleClients.put(id, newclient);
                    Thread clientThread = new Thread(new HandleClient(newclient, messages, idleClients, readyClients, gameAssitant));
                    clientThread.start();
                    id += 1;
                }

                if (gameAssitant.getStarted()) {
                    Thread gameThread = new Thread(new Game(readyClients, messages));
                    gameThread.start();
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("\"" + args[0] + "\"" + " is not a valid integer");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkClient(String newclient, Map<Integer, ConnectionToClient> clients) {
        for (ConnectionToClient c : clients.values()) {
            if (c.getUserName().equals(newclient)) {
                return true;
            }
        }
        return false;
    }
}