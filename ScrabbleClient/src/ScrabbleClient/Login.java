package ScrabbleClient;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.simple.parser.JSONParser;

import javax.swing.JLabel;

public class Login {

    private JFrame frame;
    private JTextField textField;
    private static JSONParser parser = new JSONParser();
    private static BufferedReader bufferRead;
    private static BufferedWriter bufferWrite;
    private static MyClient myClient;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login window = new Login();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Create the application.
     */
    public Login() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    public void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 390, 228);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // try {
                // lob frame = new lob();
                // frame.setVisible(true);
                // } catch (Exception e) {
                // e.printStackTrace();
                // }
                // frame.setVisible(false);
                // Random r = new Random();
                // String userName = "user" + r.nextInt(999999);
                String userName = textField.getText();
                myClient = new MyClient("localhost", String.valueOf(12345), userName);
                // MyClient myClient = new MyClient(textField.getText(), textField2.getText());
                myClient.buildBufferRead();
                myClient.buildBufferWrite();
                bufferRead = myClient.getBufferReader();
                bufferWrite = myClient.getBufferWrite();
                try {
                    bufferWrite.write(userName + "\n");
                    bufferWrite.flush();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                if (myClient.getReady()) {
                    // lob lobbyFrame = new lob();
                    // lobbyFrame.setVisible(true);
                    Crossword c = new Crossword(myClient);
                    ListenThread t = new ListenThread(c, myClient);
                    t.start();
                    frame.dispose();
                }
            }
        });
        btnLogin.setBounds(61, 137, 93, 23);
        frame.getContentPane().add(btnLogin);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnCancel.setBounds(219, 137, 93, 23);
        frame.getContentPane().add(btnCancel);

        textField = new JTextField();
        textField.setBounds(143, 63, 186, 23);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(55, 67, 78, 15);
        frame.getContentPane().add(lblUsername);
    }

}
