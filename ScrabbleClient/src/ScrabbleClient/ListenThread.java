// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 

//ListenThread.java is the thread that continuously listen to the server
package ScrabbleClient;

import java.io.IOException;

public class ListenThread extends Thread {
    private Thread t;
    private ConnectionToServer c;

    ListenThread(Crossword cw, MyClient myClient) {
        this.c = new ConnectionToServer(cw, myClient);
    }

    public void run() {
        // Synchronize the threads!
        synchronized (c) {
            try {
                c.connectionToServer();
            } catch (IOException e) {
            }
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
