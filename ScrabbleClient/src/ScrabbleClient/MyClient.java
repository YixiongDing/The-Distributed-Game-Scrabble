// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 

//MyClient.java is used to save the variable about connection and connect to the Server

package ScrabbleClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient {
    private static Socket socket;
    private static BufferedWriter bw;
    private static BufferedReader br;
    private String address;
    private String port;
    private Boolean ready = true;
    private String userName = "";

    public MyClient(String address, String port, String userName) {
        try {
            this.userName = userName;
            this.address = address;
            this.port = port;
            this.socket = new Socket(address, Integer.parseInt(port));

        } catch (NumberFormatException e1) {
            new MessageUI("please use right format of addressa and port number");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            new MessageUI("this address is unkown");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            new MessageUI("Can not connect to the Server, try another address and port number");
        }
    }

    public void buildBufferWrite() {
        // Send the message to the server
        OutputStream os;
        try {
            os = this.socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            this.bw = new BufferedWriter(osw);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            this.ready = false;
        }
    }

    public void buildBufferRead() {
        // Get the return message from the server
        InputStream is;
        try {
            is = this.socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            this.br = new BufferedReader(isr);
        } catch (Exception e) {
            this.ready = false;
        }
    }

    public BufferedWriter getBufferWrite() {
        return this.bw;
    }

    public BufferedReader getBufferReader() {
        return this.br;
    }

    public Boolean getReady() {
        return this.ready;
    }

    public Socket getSocket() {
        return this.socket;
    }
    public String getUserName() {
        return this.userName;
    }
    public void close() {
        {
            // Closing the socket
            try {
                this.socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}