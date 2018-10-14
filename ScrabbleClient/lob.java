// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 
package ScrabbleClient;

//This is the lobby window.

import java.awt.TextField;

import javax.swing.JFrame;

import org.json.simple.JSONObject;



import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import java.util.List;

import org.json.simple.parser.JSONParser;






public class lob extends JFrame {



	private BufferedReader bufferRead;
	private BufferedWriter bufferWrite;
	private MyClient myClient;
	private JSONParser parser = new JSONParser();
	public static int createstatus = 0;
	public static ArrayList <String> userlist1;

	String inviteduser= null;






	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	JButton StartButton = new JButton("Start");

	JButton Logoutbutton = new JButton("Logout");

	JButton btnRefresh = new JButton("Refresh");

	JButton InviteButton = new JButton("Invite");

	JButton CreateButton = new JButton("Create");

	JTextArea textArea = new JTextArea();

	JScrollPane jsptextArea = new JScrollPane(textArea);

	JTextArea userlistArea = new JTextArea();

	TextField textField = new TextField();


	public lob(MyClient mc) throws IOException {

		this.myClient = mc;
		this.bufferRead = myClient.getBufferReader();
		this.bufferWrite = myClient.getBufferWrite();
		initialize();

	}

	public  void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 455);
		setTitle(myClient.getUserName()+"'s Lobby");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null); 

		setBounds(100, 100, 640, 455);

		textField.setBounds(467, 321, 147, 21);
		StartButton.setBounds(172, 360, 120, 46);

		Logoutbutton.setBounds(324, 360, 120, 46);
		InviteButton.setBounds(467, 359, 147, 23);
		CreateButton.setBounds(27, 360, 120, 46);
		textArea.setBounds(10, 11, 440, 327);
		userlistArea.setBounds(467, 10, 147, 312);
		userlistArea.setBorder(BorderFactory.createTitledBorder("PlayerList"));

		StartButton.setBackground(Color.LIGHT_GRAY);
		Logoutbutton.setBackground(Color.LIGHT_GRAY);
		CreateButton.setBackground(Color.LIGHT_GRAY);
		textArea.setWrapStyleWord(true); 
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		userlistArea.setWrapStyleWord(true); 
		userlistArea.setLineWrap(true);
		userlistArea.setEditable(false);


		add(StartButton);
		add(Logoutbutton);
		add(InviteButton);
		add(CreateButton);
		add(textArea);
		add(userlistArea);
		add(textField);
		StartButton.setEnabled(false);
		InviteButton.setEnabled(false);
		StartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//send the apply of start to server
					JSONObject sent = new JSONObject();
					sent.put("COMMAND", "APPLYSTART");

					myClient.getBufferWrite().write(sent.toJSONString() + "\n");
					myClient.getBufferWrite().flush();
					System.out.println(sent.toJSONString());




				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


			}
		});
		Logoutbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//logout game
				System.exit(0);
			}
		});
		InviteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//send invitation to server
					inviteduser=textField.getText().trim();
					String[] strs = userlistArea.getText().split("\n");
					List<String> tempList = Arrays.asList(strs);
					if ("".equals(inviteduser)) {
						new MessageUI("Input can not be empty,please input again.",myClient.getUserName());
					}
					else if(myClient.getUserName().equals(inviteduser)) {
						new MessageUI("Can't invite yourself",myClient.getUserName());
					}		                	

					else if(tempList.contains(inviteduser)) {

						JSONObject sent = new JSONObject();
						sent.put("COMMAND", "INVITE");
						sent.put("MESSAGE", inviteduser);
						myClient.getBufferWrite().write(sent.toJSONString() + "\n");
						myClient.getBufferWrite().flush();
						new MessageUI("Sent invition to "+inviteduser,myClient.getUserName());
						System.out.println(sent.toJSONString());
						System.out.println(inviteduser);
					}


					else 
					{
						new MessageUI("Please input a player who exists in the playerlist.",myClient.getUserName());
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println("IOException");
					e1.printStackTrace();
				}


			}
		});

		CreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					//send the apply of create to server
					JSONObject sent = new JSONObject();
					sent.put("COMMAND", "CREATE");
					myClient.getBufferWrite().write(sent.toJSONString() + "\n");
					myClient.getBufferWrite().flush();
					System.out.println(sent.toJSONString());
					System.out.println(ConnectionToServer.createstatus);
					System.out.println(ConnectionToServer.createstatus);


				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}



			}
		});
	}
	public  void textappend(String string) {
		textArea.append(string);

	}
	public  void turnCreateButton(boolean b) {
		CreateButton.setEnabled(b);

	}
	public  void turnStartButton(boolean b) {
		StartButton.setEnabled(b);

	}
	public  void inviteButton(boolean b) {
		InviteButton.setEnabled(b);

	}
	public void setlobby(boolean b) {
		this.setVisible(b);
	}
	public  void appenduser(String user) {
		userlistArea.append(user);

	}

	public  void setuserlist() {
		userlistArea.setText("");

	}



}