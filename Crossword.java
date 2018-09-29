//package Client;

//12345
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;

import java.awt.event.ActionEvent;

import javax.swing.*;

public class Crossword {

	private static JFrame f;
	private static char[][] charSet;
	private static String status = "INPUT";
	// Status has listening (the server), Inputting (the word), Voting, Preping (the vote)
	private static int xCoordinate=-1; // record the last xCoordinate
	private static int yCoordinate=-1; // record the last yCoordinate
	private static int ind = -1;
	private static int lastX=-1;
	private static int lastY=-1;
	
	private static int inputX=-1;
	private static int inputY=-1;
	private static String word="";
	
	
	private static String command ="";
	private static String pushString = "";
	private static JSONObject pushJs;
	
	
	/**
	 * Launch the application.
	 */


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Crossword window = new Crossword();
					window.f.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				/*
				 * charSet = new char[20][20]; for (int i=0;i<20;i++) { for (int j = 0;
				 * j<20;j++) { charSet[i][j]= } }
				 */
			}
		});
	}

	public static void setWord(String a) {
		word=a;
	}
	public static String getWord() {
		return word;
	}
	public static String getStatus() {
		return status;
	}
	public static void setStatus(String a) {
		status=a;
	}
	public static void setlastX(int x) {
		lastX = x;
	}
	public static void setlastY(int y) {
		lastY= y;
	}


	public static void setX(int x) {
		xCoordinate = x;
	}
	public static void setInd(int index) {
		ind = index;
	}
	public static  int  getInd() {
		return ind;
	}
	public static  int  getlastX() {
		return lastX;
	}
	
	public static  int  getlastY() {
		return lastY;
	}

	public static void setY(int y) {
		yCoordinate = y;
	}
	public static  int  getinputX() {
		return inputX;
	}
	public static  int  getinputY() {
		return inputY;
	}
	public static void setinputX(int x) {
		inputX = x;
	}
	public static void setinputY(int y) {
		inputY= y;
	}
	public static  int  getX() {
		return xCoordinate;
	}
	public static  int  getY() {
		return yCoordinate;
	}

	public static void setColor() {
		for (int i=0;i<400;i++) {
			Crossword.CrosswordPanel.textFields.get(i).setBackground(Color.LIGHT_GRAY);

		}
	}

	/**
	 * Create the application.
	 */
	public Crossword() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		f = new JFrame();
		f.setBounds(100, 100, 800, 800);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().setLayout(new BorderLayout());

		JPanel container = new JPanel(new FlowLayout());
		final CrosswordPanel panel = new CrosswordPanel();
		container.add(panel);
		f.getContentPane().add(container, BorderLayout.CENTER);

		generate(panel);

		JPanel InputContainer = new JPanel(new FlowLayout());

		/*
        InputContainer.add(new JLabel("Please enter:"));
        InputContainer.add(new JLabel("x:"));

        // JTextField xTextfield = new JTextField();
        // xTextfield.setColumns(1);
        // InputContainer.add(xTextfield);

        Integer[] indexData = new Integer[20];
        for (int i = 0; i < 20; i++) {
            indexData[i] = i + 1;
        }
        JComboBox<Integer> xComboBox = new JComboBox<>(indexData);
        InputContainer.add(xComboBox);

        InputContainer.add(new JLabel("y:"));
        // JTextField yTextfield = new JTextField();
        // yTextfield.setColumns(1);
        // InputContainer.add(yTextfield);
        JComboBox<Integer> yComboBox = new JComboBox<>(indexData);
        InputContainer.add(yComboBox);

        InputContainer.add(new JLabel("Letter:"));
        // JTextField xTextfield = new JTextField();
        // xTextfield.setColumns(1);
        // InputContainer.add(xTextfield);

        Character[] list = new Character[26];
        for (int i = 0; i < 26; i++) {
            list[i]=(char) (65 + i);
        }
        JComboBox<Character> letterComboBox = new JComboBox<>(list);
        InputContainer.add(letterComboBox);
		 */      
		JButton voteButton = new JButton("Initiate a Vote");
		//submitButton.addMouseListener(new MouseAdapter(){
		//@Override
		//public void mouseClicked(MouseEvent e) {
		//int x = xComboBox.getSelectedIndex();
		//int y = yComboBox.getSelectedIndex();
		//int index = (x)*20+y;
		//char l =  (char) (65+letterComboBox.getSelectedIndex());
		//if ((CrosswordPanel.textFields.get(index).getText().equals(String.valueOf("\u0020")))) {
		//CrosswordPanel.textFields.get(index).setText(Character.toString(l));}
		//Crossword.setStatus("WAITING");

		//else    				

		;
		//}

		//
		//});

		JButton passButton = new JButton("Pass this Turn");

		JButton scoreButton = new JButton("Scoreboard");

		InputContainer.add(voteButton);
		InputContainer.add(passButton);
		InputContainer.add(scoreButton);

		f.getContentPane().add(InputContainer, BorderLayout.SOUTH);

		f.setSize(1500, 800);
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




	class InputPanel extends JPanel {

	}

	static class CrosswordPanel extends JPanel {
		public static ArrayList<JButton> textFields;
		private JLabel label[][];
		private static InputPanel input;
		private static Crossword cr;

		void setCrossword(char array[][]) {
			removeAll();
			int w = array.length;
			int h = array[0].length;
			setLayout(new GridLayout(w + 1, h + 1));
			textFields = new ArrayList <JButton>();
			label = new JLabel[w + 1][h + 1];
			for (int x = -1; x < w; x++) {
				// label[x][y] = new JLabel(String.valueOf(x+1));
				add(new JLabel(String.valueOf(x + 1)));
			}


			for (int x = 0; x < h; x++) {
				for (int y = 0; y < w; y++) {
					char c = array[x][y];
					if (y == 0) {
						add(new JLabel(String.valueOf(x + 1)));
					}
					if (c != 0) {
						JButton newButton = new JButton(String.valueOf(c));
						newButton.setBackground(Color.LIGHT_GRAY);
						//textFields[x][y].setFont(textFields[x][y].getFont().deriveFont(20.0f));
						newButton.setPreferredSize(new Dimension(60, 30)); // the widths of the textfileds
						newButton.addMouseListener(new MouseAdapter(){
							@Override
							public void mouseClicked(MouseEvent e) {

								int ind = textFields.indexOf((JButton)e.getSource())+1;
								Crossword.setInd(ind-1);
								int xco=(ind-(ind%20))/20+1;
								int yco=ind%20;
								Crossword.setX(xco);
								Crossword.setY(yco);  

								boolean isEmpty =  Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).getText().equals(String.valueOf("\u0020"));


								if (Crossword.getStatus().equals("INPUT")&&isEmpty) {
									InputWindow newFrame = new InputWindow(cr);
									newFrame.setVisible(true);

								}


								else if (Crossword.getStatus().equals("INPUT")&&!isEmpty) {
									PromptWindow newFrame = new PromptWindow("You can only type in an empty block!");
									newFrame.setVisible(true);
								}


								else if (Crossword.getStatus().equals("AFTER_INPUT")&&isEmpty) {
									PromptWindow newFrame = new PromptWindow("    Choose a block which has a letter!");
									newFrame.setVisible(true);
								}

								else if (Crossword.getStatus().equals("AFTER_INPUT")&&!isEmpty) {
									PromptWindow newFrame = new PromptWindow("    Click the last letter of the word!");
									newFrame.setVisible(true);
									Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).setBackground(Color.yellow);
									Crossword.setStatus("AFTER_FIRST_CLICK");
								}
								else if (Crossword.getStatus().equals("AFTER_FIRST_CLICK")&&isEmpty) {
									PromptWindow newFrame = new PromptWindow("    Choose a block which has a letter!");
									newFrame.setVisible(true);
								}

								else if (Crossword.getStatus().equals("AFTER_FIRST_CLICK")&&!isEmpty) {
									PromptWindow newFrame = new PromptWindow(" Press Initiate a vote to start vote!");
									newFrame.setVisible(true);
									Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).setBackground(Color.yellow);
									Crossword.setStatus("AFTER_FIRST_CLICK");
									Crossword.setlastX(-1);
									Crossword.setlastY(-1);  

								}

								else if (Crossword.getStatus().equals("AFTER_FIRST_CLICK")&&!isEmpty) {
									int inputX=Crossword.getinputX();
									int inputY=Crossword.getinputY();
									int lastX=Crossword.getlastX();
									int lastY=Crossword.getlastY();
									int index=-1;
									// only one point is chosen
									if(xco==inputX&&xco==lastX&&yco==lastY&&yco==inputY) {
										Crossword.setWord(Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).getText());
										index=0;
									}
									
									if(xco==inputX&&xco==lastX&&inputY>=lastY&&yco>=inputY) {
										for (int i = lastY;i<=yco;i++) {
											index =(xco-1)*20+i-1;
											String s=Crossword.CrosswordPanel.textFields.get(index).getText();
											if(s.equals(String.valueOf("\u0020"))) 
											{
												Crossword.setColor();
												PromptWindow newFrame = new PromptWindow(" Empty block in between. Start again!");
												Crossword.setStatus("AFTER_INPUT");
												newFrame.setVisible(true);
												break;
											}
											Crossword.setWord(Crossword.getWord()+s);
											Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
										}
										
									}
									else if(xco==inputX&&xco==lastX&&inputY<=lastY&&yco<=inputY) {
										for (int i = yco;i<= lastY;i++) {
											index =(xco-1)*20+i-1;
											String s=Crossword.CrosswordPanel.textFields.get(index).getText();
											if(s.equals(String.valueOf("\u0020"))) 
											{
												Crossword.setColor();
												PromptWindow newFrame = new PromptWindow(" Empty block in between. Start again!");
												Crossword.setStatus("AFTER_INPUT");
												newFrame.setVisible(true);
												break;
											}
											Crossword.setWord(Crossword.getWord()+s);
											Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
										}

									}

									else if(yco==inputY&&yco==lastY&&lastX<=inputX&&xco>=inputX) {
										for (int i = lastX;i<= xco;i++) {
											index =(xco-1)*20+i-1;
											String s=Crossword.CrosswordPanel.textFields.get(index).getText();
											if(s.equals(String.valueOf("\u0020"))) 
											{
												Crossword.setColor();
												PromptWindow newFrame = new PromptWindow(" Empty block in between. Start again!");
												Crossword.setStatus("AFTER_INPUT");
												newFrame.setVisible(true);
												break;
											}
											Crossword.setWord(Crossword.getWord()+s);
											Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
										}

									}
									else if(yco==inputY&&yco==lastY&&lastX>=inputX&&xco<=inputX) {
										for (int i = xco;i<=lastX ;i++) {
											index =(xco-1)*20+i-1;
											String s=Crossword.CrosswordPanel.textFields.get(index).getText();
											if(s.equals(String.valueOf("\u0020"))) 
											{
												Crossword.setColor();
												PromptWindow newFrame = new PromptWindow(" Empty block in between. Start again!");
												Crossword.setStatus("AFTER_INPUT");
												newFrame.setVisible(true);
												break;
											}
											Crossword.setWord(Crossword.getWord()+s);
											Crossword.CrosswordPanel.textFields.get(index).setBackground(Color.yellow);
										}

									}
									
									if (index>-1) {
										PromptWindow newFrame = new PromptWindow(" Press Initiate a vote to start vote!");
										newFrame.setVisible(true);
										Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).setBackground(Color.yellow);
										Crossword.setStatus("AFTER_SECOND_CLICK");
									}
									else if (index==-1) {
										PromptWindow newFrame = new PromptWindow(" Input point not on a line! Restart!");
										newFrame.setVisible(true);
										Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).setBackground(Color.yellow);
										Crossword.setStatus("AFTER_INPUT");
										Crossword.setColor();

									}

								}


																}

						});
						textFields.add(newButton) ;

						add(newButton);
					} else {
						add(new JLabel());
					}
				}
			}
			getParent().validate();
			repaint();
		}

		
		
		public char[][] getCrossword() {
			int w = 20;
			int h = 20;
			char crossword[][] = new char[w][h];

			return crossword;
		}

	}
}