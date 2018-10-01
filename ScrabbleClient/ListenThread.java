package ScrabbleClient;

import java.io.IOException;

public class ListenThread extends Thread {
    private Thread t;
    ConnectionToServer c;

    ListenThread(MyClient myClient) {
        c = new ConnectionToServer(myClient);
    }

    public void run() {
        // Synchronize the threads!
        synchronized (c) {
            try {
                c.connectionToServer();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                 e.printStackTrace();
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
