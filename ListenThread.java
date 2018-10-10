package ScrabbleClient;

import java.io.IOException;

public class ListenThread extends Thread {
    private Thread t;
    private ConnectionToServer c;
    private LobbyConnectionToServer l;

    ListenThread(Crossword cw, MyClient myClient) {
        this.c = new ConnectionToServer(cw, myClient);
    }
    ListenThread(lob lob, MyClient myClient) {
        this.l = new LobbyConnectionToServer(lob, myClient);
    }

    public void run() {
        // Synchronize the threads!
        synchronized (c) {
            try {
                c.connectionToServer();
            } catch (IOException e) {
                // TODO Auto-generated catch block
//                System.out.println("IOException");
            }
        }
    }

    public void start() {
        // System.out.println("Starting " + threadName );
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
