package ScrabbleClient;

import java.io.IOException;

public class ListenThread extends Thread {
    private Thread t;
    private LobbyConnectionToServer lobby1;


    ListenThread(lob lobby1, MyClient myClient) {
        this.lobby1 = new LobbyConnectionToServer(lobby1, myClient);
    }

    public void run() {
        // Synchronize the threads!
        synchronized (lobby1) {
            try {
            	lobby1.lobbyConnectionToServer();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("IOException");
            }
        }
    }
/*    public void run() {
        // Synchronize the threads!
        synchronized (c) {
            try {
                c.connectionToServer();
            } catch (IOException e) {
                // TODO Auto-generated catch block
//                System.out.println("IOException");
            }
        }
    }*/

    public void start() {
        // System.out.println("Starting " + threadName );
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
