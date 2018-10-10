package ScrabbleClient;



import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import ScrabbleClient.Lobby.DataModel;


import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import org.json.simple.parser.JSONParser;






 public class lob extends JFrame {

	
	    private DataModels model;
	    private BufferedReader bufferRead;
	    private BufferedWriter bufferWrite;
	    private MyClient myClient;
	    private Thread t;
	    private JSONParser parser = new JSONParser();
		public static int createstatus = 0;

	    String inviteduser;
	    String[] userlist = {"A","B","C","D","E"};
	    
	    
	    
	    

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
	JButton StartButton = new JButton("Start");
	
	JButton Logoutbutton = new JButton("Logout");
	
	JButton btnRefresh = new JButton("Refresh");
	
//    model = new DataModel(); 
    
//	JList list = new JList(model);
	
	JButton InviteButton = new JButton("Invite");
	 
	JButton CreateButton = new JButton("Create");
	 
	JTextArea textArea = new JTextArea();

	JScrollPane jsptextArea = new JScrollPane(textArea);
	
	JScrollPane scrollPane_1 = new JScrollPane();
	
	
    public lob(MyClient mc) throws IOException {
        this.myClient = mc;
        this.bufferRead = myClient.getBufferReader();
        this.bufferWrite = myClient.getBufferWrite();
        initialize();
        
/*        while(myClient.getBufferReader() != null) {
        	
            String message = myClient.getBufferReader().readLine();
            System.out.println("Server:" + message);
            try {
                JSONObject serverJSON = (JSONObject) parser.parse(message);
                if (serverJSON.get("COMMAND").equals("START")) {
                        Crossword c = new Crossword(myClient);
                        ListenThread t = new ListenThread(c, myClient);
                        t.start();


                }else if (serverJSON.containsKey("CREATESTATUS")) {
                     createstatus = (int) serverJSON.get("CREATESTATUS");

                }else if (serverJSON.containsKey("INVITED")) {
                    Vote v = new Vote("     Being invited to a game, join?", myClient);
                    v.setVisible(true);
               
            }else if (serverJSON.containsKey("USERLIST")) {
            	ArrayList <String> list = (ArrayList <String>) serverJSON.get("USERLIST");
            	int size=list.size();
            	String[] userlist = (String[])list.toArray(new String[size]);
            }
            else if (serverJSON.containsKey("MESSAGES")) {
            	String messages = (String)serverJSON.get("MESSAGES");
            	textArea.append(sdf.format(new Date()) + messages+ "\n");
            }

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }*/
        
        
        
    }
		
		   public  void initialize()
		    {
				setBounds(100, 100, 640, 455);
		        setTitle("Lobby");
		        setSize(WIDTH, HEIGHT);
		        setResizable(false);
		        setLayout(null);
		        
				setBounds(100, 100, 640, 455);
				StartButton.setBounds(172, 360, 120, 46);
				Logoutbutton.setBounds(324, 360, 120, 46);
				InviteButton.setBounds(467, 319, 147, 23);
				CreateButton.setBounds(27, 360, 120, 46);
				textArea.setBounds(10, 11, 440, 327);
				scrollPane_1.setBounds(467, 10, 147, 312);
				
				StartButton.setBackground(Color.LIGHT_GRAY);
				Logoutbutton.setBackground(Color.LIGHT_GRAY);
				CreateButton.setBackground(Color.LIGHT_GRAY);
				textArea.setWrapStyleWord(true); 
				textArea.setLineWrap(true);
				textArea.setLineWrap(true);
				textArea.setEditable(false);
				scrollPane_1.setColumnHeaderView(btnRefresh);
				

		        this.add(StartButton);
		        this.add(Logoutbutton);
		        this.add(InviteButton);
		        this.add(CreateButton);
		        this.add(textArea);
		        this.add(scrollPane_1);
		        
		        StartButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
		                try {
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
						System.exit(0);
	                try {
		                    JSONObject sent = new JSONObject();
		                    sent.put("COMMAND", "LOGOUT");
		                   
		                    myClient.getBufferWrite().write(sent.toJSONString() + "\n");
		                    myClient.getBufferWrite().flush();
		                    System.out.println(sent.toJSONString());
		                    

		                    
		                    
		                } catch (IOException e1) {
		                    // TODO Auto-generated catch block
		                    e1.printStackTrace();
		                }


					}
				});
				
				btnRefresh.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
		                try {
	                    JSONObject sent = new JSONObject();
	                    sent.put("COMMAND", "RFLIST");
	                    
	                    myClient.getBufferWrite().write(sent.toJSONString() + "\n");
	                    myClient.getBufferWrite().flush();
	                    System.out.println(sent.toJSONString());
	    		        model = new DataModels(); 
	    				JList list = new JList(model);
	    				scrollPane_1.setViewportView(list);
	                } catch (IOException e1) {
	                    // TODO Auto-generated catch block
	                    e1.printStackTrace();
	                }


					}
				});
				 InviteButton.addActionListener(new ActionListener() {
				 	public void actionPerformed(ActionEvent e) {
		                try {
	                    JSONObject sent = new JSONObject();
	                    sent.put("COMMAND", "INVITE");
	                    // JSONObject word = new JSONObject();
	                    // word.put("WORD", getWord());
	                    JSONObject message = new JSONObject();
	                    message.put("INVITE", inviteduser);
	                    sent.put("MESSAGE", message);
	                    myClient.getBufferWrite().write(sent.toJSONString() + "\n");
	                    myClient.getBufferWrite().flush();
	                    System.out.println(sent.toJSONString());
				 		
		                System.out.println(inviteduser);
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
		                    JSONObject sent = new JSONObject();
		                    sent.put("COMMAND", "CREATE");
		                    JSONObject message = new JSONObject();
		                    message.put("CREATE", myClient.getUserName());
		                    sent.put("MESSAGE", message);
		                    myClient.getBufferWrite().write(sent.toJSONString() + "\n");
		                    myClient.getBufferWrite().flush();
		                    System.out.println(sent.toJSONString());
		                    
		                    if(ConnectionToServer.createstatus==0) {
		                        new MessageUI("Waiting for the server to respond, please try again later.");

		                    }else if(ConnectionToServer.createstatus==1) {
		                        new MessageUI("Create a game room successfully.");
		                        CreateButton.setEnabled(false);
		                        StartButton.setEnabled(true);
		                    }else if(ConnectionToServer.createstatus==2) {
		                        new MessageUI("There is a game in progress, please try again later.");
		                    }
		                    
		                    
		                    
		                } catch (IOException e1) {
		                    // TODO Auto-generated catch block
		                    e1.printStackTrace();
		                }
				 		
				 		
				 		
				 	}
				 });
		        model = new DataModels(); 
				JList list = new JList(model);
				scrollPane_1.setViewportView(list);

				 list.setBorder(BorderFactory.createTitledBorder("UserList"));
			        list.addMouseListener(new MouseAdapter() 
			                {
			                    public void mousePressed(MouseEvent e) 
			                    {

			                        int i = list.getSelectedIndex();

			                        inviteduser = userlist[i];
			                        System.out.println(inviteduser);
			                        

			                    	

			                    }
			                });
				 

				
				
	}
			public  void textappend(String string) {
				textArea.append(string);
				
			}

}
 class DataModels extends DefaultListModel
 {
	    String[] userlist = {"A","B","C","D","E"};

     public DataModels()
     {
         for (int i = 0; i < userlist.length; i++) 
         {
             addElement(userlist[i]);
         }
     }
 }


    