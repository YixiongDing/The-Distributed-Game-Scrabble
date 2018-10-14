// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu package ScrabbleClient;
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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.JLabel;
//This is the login window.
public class Login {

    private JFrame frame;
    private JTextField textField;
    private static JSONParser parser = new JSONParser();
    private static BufferedReader bufferRead;
    private static BufferedWriter bufferWrite;
    private static MyClient myClient;
    private JTextField textField_1;
    private JTextField textField_2;
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
                String userName = textField.getText().trim();               
                String address = textField_1.getText().trim();
                String portNumber = textField_2.getText().trim();
                //get the username,address,portnumber
                if("".equals(userName)||"".equals(address)||"".equals(address)) {
                    new MessageUI2("Input can not be empty, please input again.");
                }else {
                myClient = new MyClient(address, portNumber, userName);
                myClient.buildBufferRead();
                myClient.buildBufferWrite();
                bufferRead = myClient.getBufferReader();
                bufferWrite = myClient.getBufferWrite();
                
//                try {
//
//                } catch (IOException e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//                
                if (myClient.getReady()) {

					try { 
	                	//send username to server
	                    bufferWrite.write(userName + "\n");
	                    bufferWrite.flush();
	                    
						Crossword c = new Crossword(myClient);
						lob lobbyFrame1 = new lob(myClient);
	                    ListenThread t = new ListenThread(lobbyFrame1,c, myClient);
	                    t.start();
	                    lobbyFrame1.turnStartButton(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
                    frame.dispose();
                }
            }
            }
        });
        
        btnLogin.setBounds(65, 153, 93, 23);
        frame.getContentPane().add(btnLogin);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });



        btnCancel.setBounds(216, 153, 93, 23);
        frame.getContentPane().add(btnCancel);

        textField = new JTextField();
        textField.setBounds(143, 93, 186, 26);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(49, 98, 78, 16);
        frame.getContentPane().add(lblUsername);
        
        JLabel lblAddress = new JLabel("Address");
        lblAddress.setBounds(49, 26, 61, 16);
        frame.getContentPane().add(lblAddress);
        
        textField_1 = new JTextField();
        textField_1.setBounds(143, 21, 186, 26);
        frame.getContentPane().add(textField_1);
        textField_1.setColumns(10);
        
        JLabel lblPortNumber = new JLabel("Port Number");
        lblPortNumber.setBounds(49, 59, 109, 16);
        frame.getContentPane().add(lblPortNumber);
        
        textField_2 = new JTextField();
        textField_2.setBounds(143, 55, 186, 26);
        frame.getContentPane().add(textField_2);
        textField_2.setColumns(10);
    }


		
	}

   


