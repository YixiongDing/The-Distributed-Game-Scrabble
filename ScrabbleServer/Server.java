    package ScrabbleServer;
    import org.json.simple.JSONObject;
    import org.json.simple.parser.JSONParser;
    import org.json.simple.parser.ParseException;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.concurrent.ConcurrentLinkedQueue;
    import java.util.concurrent.ConcurrentHashMap;
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
                ConcurrentLinkedQueue<String> idleclients = new ConcurrentLinkedQueue<String>();
                ConcurrentLinkedQueue<String> readyclients = new ConcurrentLinkedQueue<String>();
                ConcurrentLinkedQueue<String> inGameMessages = new ConcurrentLinkedQueue<String>();
                ConcurrentLinkedQueue<String> inLobbyMessages = new ConcurrentLinkedQueue<String>();
                ConcurrentLinkedQueue<String> startGameSignal = new ConcurrentLinkedQueue<String>();
                JSONParser parser = new JSONParser();

                String username = null;
                int id = 0;
                JSONObject response = new JSONObject();

                System.out.println("Server is up!");

                while (true) {
                    Socket client = server.accept();
                    DataInputStream input = new DataInputStream(client.getInputStream());
                    DataOutputStream output = new DataOutputStream(client.getOutputStream());
                    InputStreamReader isr = new InputStreamReader(input);
                    BufferedReader br = new BufferedReader(isr);
                    username = br.readLine();

                    if (checkClient(username, idleClients)) {
                        System.out.println(username + " has already logged in!");
                        response.put("ERROR", "This username is occupied!");
                    } else {
                        System.out.println(username + " connected");
                        response.put("MESSAGE", "Login Successfully!");
                        ConnectionToClient newclient = new ConnectionToClient(id, client, username);
                        idleclients.add(username);
                        idleClients.put(id, newclient);
                        Thread clientThread = new Thread(new HandleClient(newclient, inGameMessages,
                                inLobbyMessages, idleClients, readyClients));
                        clientThread.start();
                        id += 1;
                    }

                    Thread lobbyThread = new Thread(new HandleLobby(idleClients, inLobbyMessages, readyClients, inGameMessages));
                    lobbyThread.start();
                }
            } catch (NumberFormatException e) {
                System.err.println("\"" + args[0] + "\"" + " is not a valid integer");
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static boolean checkClient(String newclient,  Map<Integer, ConnectionToClient> clients) {
            for (ConnectionToClient c : clients.values()) {
                if (c.getUserName().equals(newclient)) {
                    return true;
                }
            }
            return false;
        }

        private static int locateClient(String newclient,  Map<Integer, ConnectionToClient> clients) {
            for (ConnectionToClient c : clients.values()) {
                if (c.getUserName().equals(newclient)) {
                    return c.getClientId();
                }
            }
            return 99;
        }

        private static void sendToUser(JSONObject message, String user, Map<Integer, ConnectionToClient> clients) {
            for (ConnectionToClient c : clients.values()) {
                if (c.getUserName().equals(user)) {
                    c.sendMessage(message);
                    break;
                }
            }
        }

        private static void sendAll(JSONObject message, Map<Integer, ConnectionToClient> clients) {
            for (ConnectionToClient c : clients.values()) {
                c.sendMessage(message);
            }
        }
    }