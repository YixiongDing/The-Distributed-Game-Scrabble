package ScrabbleClient;

import java.io.IOException;

public class ListenThread extends Thread {
    private Thread t;
    private ConnectionToServer cts;


    ListenThread(lob lobby1,Crossword cs, MyClient myClient) {
        this.cts = new ConnectionToServer(lobby1,cs, myClient);
    }

    public void run() {
        // Synchronize the threads!
        synchronized (cts) {
            try {
            	cts.connectionToServer();
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
