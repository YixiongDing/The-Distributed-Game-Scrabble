// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 

//ListenThread.java is the thread that continuously listen to the server
package ScrabbleClient;

public class ListenThread extends Thread {
    private Thread t;
    private ConnectionToServer cts;


    ListenThread(lob lobby1,Crossword cs, MyClient myClient) {
        this.cts = new ConnectionToServer(lobby1,cs, myClient);
    }

    public void run() {
        // Synchronize the threads!
        synchronized (cts) {
            cts.connectionToServer();
        }
    }
    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
