// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 

package ScrabbleClient;

// This is the main Scramble game window.
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.event.ActionEvent;

import javax.swing.*;


public class Crossword {


	private static JFrame f;
	private static char[][] charSet;
	private static String status = "INPUT";
	private static int xCoordinate = -1; // record the current xCoordinate
	private static int yCoordinate = -1; // record the current yCoordinate
	private static int ind = -1;		// record the current index number in tha arraylist
	private static int firstX = -1;		// record the xCoordinate of the first click during voting process
	private static int firstY = -1;   	// record the yCoordinate of the first click during voting process
	private static int inputX = -1;		// record the xCoordinate of input 
	private static int inputY = -1;		// record the xCoordinate of input 
	private static String word = "";	// the word of voting to be sent

	private static BufferedReader bufferRead;
	private static BufferedWriter bufferWrite;
	private static MyClient myClient;
	private JSONParser parser = new JSONParser();
	private static JTextArea txtSCORE;

	private static boolean turn = false;
	private static JLabel turnLabel = new JLabel("");

	// Parse the score from the server 
	public static void setScorebd(ArrayList<String> a) {
		txtSCORE.setText("");
		txtSCORE.append("Scoreboard:\n");
		for (String s : a) {
			txtSCORE.append(s+"\n");
		}
	}

	// display the turn info
	public static void setTurnLabel(String text) {
		turnLabel.setText(text);
	}
	// set the turn
	public static void setTurn(boolean a) {
		turn = a;
	}

	public static boolean getTurn() {
		return turn;
	}

	// set the vote word to be sent to the server
	public static void setWord(String a) {
		word = a;
	}

	public static String getWord() {
		return word;
	}

	// get the current stage of the game
	public static String getStatus() {
		return status;
	}
	
	// set the current stage of the game
	public static void setStatus(String a) {
		status = a;
	}

	// set the coordinates 
	
	public static void setFirstX(int x) {
		firstX = x;
	}

	public static void setFirstY(int y) {
		firstY = y;
	}
	public static void setinputX(int x) {
		inputX = x;
	}

	public static void setinputY(int y) {
		inputY = y;
	}

	public static void setX(int x) {
		xCoordinate = x;
	}
	public static void setY(int y) {
		yCoordinate = y;
	}

	public static void setInd(int index) {
		ind = index;
	}

	
	
	// the followings get the coordinates from the server.
	public static int getInd() {
		return ind;
	}

	public static int getFirstX() {
		return firstX;
	}

	public static int getFirstY() {
		return firstY;
	}


	public static int getinputX() {
		return inputX;
	}

	public static int getinputY() {
		return inputY;
	}


	public static int getX() {
		return xCoordinate;
	}

	public static int getY() {
		return yCoordinate;
	}


	
	public Crossword(MyClient mc) {
		this.myClient = mc;
		this.bufferRead = myClient.getBufferReader();
		this.bufferWrite = myClient.getBufferWrite();
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		f = new JFrame();
		f.setBounds(100, 100, 800, 800);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		// this is set as exit on close to allow the player to exit if closing the window.
		f.getContentPane().setLayout(new BorderLayout());
		f.setTitle(myClient.getUserName() + "'s game");

		JPanel container = new JPanel(new FlowLayout());
		final CrosswordPanel panel = new CrosswordPanel();
		container.add(panel);
		f.getContentPane().add(container, BorderLayout.CENTER);

		generate(panel);

		JPanel InputContainer = new JPanel(new FlowLayout());

		// a vote button
		JButton voteButton = new JButton("Initiate a Vote");
		voteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (turn && getStatus().equals("AFTER_SECOND_CLICK")) {

					try {
						// this will send two JSONObject: first one is the voting word
						// second JSONObject is the coordinate to be highlighted.
						JSONObject sent = new JSONObject();
						sent.put("COMMAND", "VOTING");
						JSONObject message = new JSONObject();
						message.put("INIT", getWord());
						sent.put("MESSAGE", message);
						bufferWrite.write(sent.toJSONString() + "\n");
						bufferWrite.flush();


						JSONObject sent2 = new JSONObject();
						sent.put("COMMAND", "COORD");
						JSONObject message2 = new JSONObject();
						message2.put("X1", getFirstX());
						message2.put("Y1", getFirstY());
						message2.put("X2", getX());
						message2.put("Y2", getY());

						sent.put("MESSAGE", message2);
						bufferWrite.write(sent.toJSONString() + "\n");
						bufferWrite.flush();

						System.out.println(sent.toJSONString());
						setTurn(false);
						setStatus("INPUT");
						Crossword.CrosswordPanel.setColorAll();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Crossword.setWord("");

				}
			}

		});

		// the button will pass the turn.
		JButton passButton = new JButton("Pass this Turn");
		passButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (turn && getStatus().equals("INPUT") || turn && getStatus().equals("AFTER_INPUT")) {
					Crossword.setWord("");

					try {
						JSONObject sent = new JSONObject();
						sent.put("COMMAND", "PASS");
						JSONObject message = new JSONObject();
						message.put("PASS", "YES");
						sent.put("MESSAGE", message);
						bufferWrite.write(sent.toJSONString() + "\n");
						bufferWrite.flush();
						System.out.println(sent.toJSONString());
						setTurn(false);
						setStatus("INPUT");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

		});

		// this will start a new window with Scoreboard
		JButton scoreButton = new JButton("Scoreboard");
		scoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					JSONObject sent = new JSONObject();
					sent.put("COMMAND", "SCORE");
					JSONObject message = new JSONObject();
					message.put("SCORE", "YES");
					sent.put("MESSAGE", message);
					bufferWrite.write(sent.toJSONString() + "\n");
					bufferWrite.flush();
					System.out.println(sent.toJSONString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});

		// add all the buttons on the panel
		
		InputContainer.add(voteButton);
		InputContainer.add(passButton);
		InputContainer.add(scoreButton);
		InputContainer.add(turnLabel);
		f.getContentPane().add(InputContainer, BorderLayout.SOUTH);

		// add a panel to display scores
		JPanel InputContainer2 = new JPanel(new FlowLayout());
		txtSCORE = new JTextArea(); // this is the score display textarea
		txtSCORE.setBackground(Color.LIGHT_GRAY);
		txtSCORE.setBounds(0, 0, 300, 800);
		txtSCORE.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		txtSCORE.setText("SCROEBOARD");
		txtSCORE.setLineWrap(true);

		InputContainer2.add(txtSCORE);

		f.getContentPane().add(InputContainer2, BorderLayout.EAST);

		f.setSize(1650, 800);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

	}

	private static void generate(CrosswordPanel panel) {
		int w = 20;
		int h = 20;
		char crossword[][] = new char[w][h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				crossword[x][y] = '\u0020'; // null character

			}
		}

		panel.setCrossword(crossword);
	}

	
	// another class to build the panel where 20*20 input buttons are located
	class InputPanel extends JPanel {

	}

	static class CrosswordPanel extends JPanel {
		public static ArrayList<JButton> textFields;
		private JLabel label[][];
		private static InputPanel input;
		private static Crossword cr;

		void setCrossword(char array[][]) {
			removeAll();
			int w = array.length; // w = 20 in this case
			int h = array[0].length;	// h = 20 in this case
			setLayout(new GridLayout(w + 1, h + 1)); 
			textFields = new ArrayList<JButton>();
			label = new JLabel[w + 1][h + 1];
			for (int x = -1; x < w; x++) {
				// label[x][y] = new JLabel(String.valueOf(x+1));
				add(new JLabel(String.valueOf(x + 1)));
			}

			// create the 20*20 buttons
			for (int x = 0; x < h; x++) {
				for (int y = 0; y < w; y++) {
					char c = array[x][y];
					if (y == 0) {
						add(new JLabel(String.valueOf(x + 1)));
					}
					if (c != 0) {
						JButton newButton = new JButton(String.valueOf(c));
						newButton.setBackground(Color.LIGHT_GRAY);
						// textFields[x][y].setFont(textFields[x][y].getFont().deriveFont(20.0f));
						newButton.setPreferredSize(new Dimension(60, 30)); // the widths of the textfileds
						newButton.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {

								int ind = textFields.indexOf((JButton) e.getSource()) + 1;
								Crossword.setInd(ind - 1);
								int xco = (ind - (ind % 20)) / 20 + 1;
								int yco = ind % 20;
								Crossword.setX(xco);
								Crossword.setY(yco);
								boolean isEmpty = Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).getText()
										.equals(String.valueOf("\u0020"));

								// based on different status, the clicking on the button will allow different moves
								if (turn && Crossword.getStatus().equals("INPUT") && isEmpty) {
									Crossword.setinputX(xco);
									Crossword.setinputY(yco);

									InputWindow newFrame = new InputWindow(cr, myClient);
									newFrame.setVisible(true);

								}

								else if (turn && Crossword.getStatus().equals("INPUT") && !isEmpty) {
									PromptWindow newFrame = new PromptWindow("You can only type in an empty block!");
									newFrame.setVisible(true);

								}

								else if (turn && Crossword.getStatus().equals("AFTER_INPUT") && isEmpty) {
									PromptWindow newFrame = new PromptWindow("    Choose a block which has a letter!");
									newFrame.setVisible(true);
								}

								else if (turn && Crossword.getStatus().equals("AFTER_INPUT") && !isEmpty) {
									PromptWindow newFrame = new PromptWindow("    Click the last letter of the word!");
									newFrame.setVisible(true);
									Crossword.CrosswordPanel.textFields.get(Crossword.getInd())
									.setBackground(Color.yellow);
									Crossword.setStatus("AFTER_FIRST_CLICK");
									Crossword.setFirstX(xco);
									Crossword.setFirstY(yco);

								} else if (turn && Crossword.getStatus().equals("AFTER_FIRST_CLICK") && isEmpty) {
									PromptWindow newFrame = new PromptWindow("    Choose a block which has a letter!");
									newFrame.setVisible(true);
								}

								else if (turn && Crossword.getStatus().equals("AFTER_FIRST_CLICK") && !isEmpty) {
									int inputX = Crossword.getinputX(); // (input X coord)
									int inputY = Crossword.getinputY();
									int lastX = Crossword.getFirstX();// first click (coord)
									int lastY = Crossword.getFirstY();
									int index = -1;


									// only one point is chosen
									if (xco == inputX && xco == lastX && yco == lastY && yco == inputY) {
										Crossword.setWord(
												Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).getText());
										index = 0;
									}

									else if (xco == inputX && xco == lastX && inputY >= lastY && yco >= inputY) {
										for (int i = lastY; i <= yco; i++) {
											index = (xco - 1) * 20 + i - 1;
											String s = Crossword.CrosswordPanel.textFields.get(index).getText();
											if (s.equals(String.valueOf("\u0020"))) {
												PromptWindow newFrame = new PromptWindow(
														" Empty block in between. Start again!");
												Crossword.setStatus("AFTER_INPUT");
												newFrame.setVisible(true);
												index = -2;
												setColorAll();
												Crossword.setWord("");

												break;
											}
											Crossword.setWord(Crossword.getWord() + s);
											Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
										}

									} else if (turn && xco == inputX && xco == lastX && inputY <= lastY
											&& yco <= inputY) {
										for (int i = yco; i <= lastY; i++) {
											index = (xco - 1) * 20 + i - 1;
											String s = Crossword.CrosswordPanel.textFields.get(index).getText();
											if (s.equals(String.valueOf("\u0020"))) {
												PromptWindow newFrame = new PromptWindow(
														" Empty block in between. Start again!");
												Crossword.setStatus("AFTER_INPUT");
												newFrame.setVisible(true);
												index = -2;
												setColorAll();
												Crossword.setWord("");

												break;
											}
											Crossword.setWord(Crossword.getWord() + s);
											Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
										}

									}

									else if (turn && yco == inputY && yco == lastY && lastX <= inputX
											&& xco >= inputX) {
										for (int i = lastX; i <= xco; i++) {
											index = (i - 1) * 20 + yco - 1;
											String s = Crossword.CrosswordPanel.textFields.get(index).getText();
											if (s.equals(String.valueOf("\u0020"))) {
												System.out.println(lastX);
												System.out.println(inputX);
												System.out.println(xco);

												PromptWindow newFrame = new PromptWindow(
														" Empty block in between. Start again!");
												Crossword.setStatus("AFTER_INPUT");
												newFrame.setVisible(true);
												index = -2;
												setColorAll();
												Crossword.setWord("");

												break;
											}
											Crossword.setWord(Crossword.getWord() + s);
											Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
										}

									} else if (turn && yco == inputY && yco == lastY && lastX >= inputX
											&& xco <= inputX) {
										for (int i = xco; i <= lastX; i++) {
											index = (i - 1) * 20 + yco - 1;
											String s = Crossword.CrosswordPanel.textFields.get(index).getText();
											if (s.equals(String.valueOf("\u0020"))) {

												PromptWindow newFrame = new PromptWindow(
														" Empty block in between. Start again!");
												Crossword.setStatus("AFTER_INPUT");
												newFrame.setVisible(true);
												index = -2;
												setColorAll();
												Crossword.setWord("");

												break;
											}
											Crossword.setWord(Crossword.getWord() + s);
											Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
										}

									}

									if (turn && index > -1) {
										PromptWindow newFrame = new PromptWindow(
												" Press Initiate a vote to start vote!");
										newFrame.setVisible(true);
										Crossword.setStatus("AFTER_SECOND_CLICK");
									} else if (turn && index == -1) {
										PromptWindow newFrame = new PromptWindow(" Input point not on the line!");
										newFrame.setVisible(true);
										Crossword.setWord("");

										setColorAll();
										Crossword.setStatus("AFTER_INPUT");
									}
								}

							}

						});
						textFields.add(newButton);

						add(newButton);
					} else {
						add(new JLabel());
					}
				}
			}
			getParent().validate();
			repaint();
		}

		// this is to set the color to be the original light_gray
		public static void setColorAll() {
			for (JButton a : textFields) {
				a.setBackground(Color.LIGHT_GRAY);
			}
		}

		public char[][] getCrossword() {
			int w = 20;
			int h = 20;
			char crossword[][] = new char[w][h];

			return crossword;
		}

	}
}
