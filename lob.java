package ScrabbleClient;



import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;



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

	

	    private BufferedReader bufferRead;
	    private BufferedWriter bufferWrite;
	    private static JFrame f;
	    private MyClient myClient;
	    private Thread t;
	    private JSONParser parser = new JSONParser();
		public static int createstatus = 0;
		public JList list;
		private ArrayList <String> userlist=LobbyConnectionToServer.getuserlist();

	    String inviteduser= null;

	    
	    
	    
	    

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
	
//	JScrollPane scrollPane_1 = new JScrollPane();
	
	JTextArea userlistArea = new JTextArea();
	
	TextField textField = new TextField();
	
	
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

		        textField.setBounds(467, 321, 147, 21);
				StartButton.setBounds(172, 360, 120, 46);
				
				Logoutbutton.setBounds(324, 360, 120, 46);
				InviteButton.setBounds(467, 319, 147, 23);
				CreateButton.setBounds(27, 360, 120, 46);
				textArea.setBounds(10, 11, 440, 327);
				userlistArea.setBounds(467, 10, 147, 312);
//				scrollPane_1.setBounds(467, 10, 147, 312);
				userlistArea.setBorder(BorderFactory.createTitledBorder("UserList"));
				
				StartButton.setBackground(Color.LIGHT_GRAY);
				Logoutbutton.setBackground(Color.LIGHT_GRAY);
				CreateButton.setBackground(Color.LIGHT_GRAY);
				textArea.setWrapStyleWord(true); 
				textArea.setLineWrap(true);
				textArea.setEditable(false);
				userlistArea.setWrapStyleWord(true); 
				userlistArea.setLineWrap(true);
				userlistArea.setEditable(false);
//				scrollPane_1.setColumnHeaderView(btnRefresh);
				

				add(StartButton);
				add(Logoutbutton);
				add(InviteButton);
				add(CreateButton);
				add(textArea);
				add(userlistArea);
		        StartButton.setEnabled(false);
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
				
/*				btnRefresh.addActionListener(new ActionListener() {
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
				});*/
				 InviteButton.addActionListener(new ActionListener() {
				 	public void actionPerformed(ActionEvent e) {
		                try {
		                	inviteduser=textField.getText().trim();
		                	if (inviteduser == null) {
		                	new MessageUI("please find a player.");
		                	}else {
		                		JSONObject sent = new JSONObject();
		                		sent.put("COMMAND", "INVITE");
		                		sent.put("MESSAGE", inviteduser);
		                		myClient.getBufferWrite().write(sent.toJSONString() + "\n");
		                		myClient.getBufferWrite().flush();
		                		new MessageUI("Sent invition to "+inviteduser);
		                		System.out.println(sent.toJSONString());
				 				System.out.println(inviteduser);
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
		                    JSONObject sent = new JSONObject();
		                    sent.put("COMMAND", "CREATE");
//		                    JSONObject message = new JSONObject();
//		                    message.put("CREATE", myClient.getUserName());
//		                    sent.put("MESSAGE", message);
		                    myClient.getBufferWrite().write(sent.toJSONString() + "\n");
		                    myClient.getBufferWrite().flush();
		                    System.out.println(sent.toJSONString());
		                    System.out.println(ConnectionToServer.createstatus);
/*		                    if(ConnectionToServer.createstatus==0) {
		                        new MessageUI("Waiting for the server to respond, please try again later.");

		                    }else if(ConnectionToServer.createstatus==1) {
		                        new MessageUI("Create a game room successfully.");
		                        CreateButton.setEnabled(false);
		                        StartButton.setEnabled(true);
		                    }else if(ConnectionToServer.createstatus==2) {
		                        new MessageUI("There is a game in progress, please try again later.");
		                    }*/
		                    System.out.println(ConnectionToServer.createstatus);
		                    
		                    
		                } catch (IOException e1) {
		                    // TODO Auto-generated catch block
		                    e1.printStackTrace();
		                }
				 		
				 		
				 		
				 	}
				 });
//		        model = new DataModels(); 
//				JList list = new JList(model);


/*				 list.setBorder(BorderFactory.createTitledBorder("UserList"));
			        list.addMouseListener(new MouseAdapter() 
			                {
			                    public void mousePressed(MouseEvent e) 
			                    {
			                    	if(inviteduser != null) {
			                        int i = list.getSelectedIndex();

			                        inviteduser = userlist.get(i);
			                        System.out.println(inviteduser);
			                        

			                    	}

			                    }
			                });
				 */

				
//			        f.setVisible(true);
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
		    public void setlobby(boolean b) {
		        this.setVisible(b);
		    }
			public  void appenduser(String user) {
				userlistArea.setText(user);
				
			}
			public  void setuserlist() {
				userlistArea.setText("");
				
			}

}
/* class DataModels extends DefaultListModel
 {
	

     public DataModels()
     {
    	 if (ConnectionToServer.getuserlist() != null) {
         for (int i = 0; i < ConnectionToServer.getuserlist().size(); i++) 
         {

        		 addElement(ConnectionToServer.getuserlist().get(i));
        	 
         }
     }
    }
 }*/


    