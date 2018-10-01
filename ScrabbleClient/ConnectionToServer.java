package ScrabbleClient;

import java.io.IOException;
import java.net.Socket;

import ScrabbleClient.MyClient;

public class ConnectionToServer {
    private MyClient myClient;
    private Thread t;

    public ConnectionToServer(MyClient myClient) {
        this.myClient = myClient;
    }

    public void connectionToServer() throws IOException {
        // while (true) {
        String content = new String();
        while (myClient.getBufferReader() != null) {
            String message = myClient.getBufferReader().readLine();
            System.out.println("Server:" + message);
            
        }
        // System.out.println(1);
        // }
    }

}
