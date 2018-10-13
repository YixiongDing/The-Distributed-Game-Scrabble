package ScrabbleClient;

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
    // Status has listening (the server), Inputting (the word), Voting, Preping (the
    // vote)
    private static int xCoordinate = -1; // record the last xCoordinate
    private static int yCoordinate = -1; // record the last yCoordinate
    private static int ind = -1;
    private static int firstX = -1;
    private static int firstY = -1;
    private static ArrayList<String> cord;
    private static int inputX = -1;
    private static int inputY = -1;
    private static String word = "";
   
    private static BufferedReader bufferRead;
    private static BufferedWriter bufferWrite;
    private static MyClient myClient;
    private JSONParser parser = new JSONParser();
    private static JTextArea txtrUsername;

    private static boolean turn = false;
    private static JLabel turnLabel = new JLabel("");

    public static void setScorebd(ArrayList<String> a) {
        txtrUsername.setText("");
        txtrUsername.append("Scoreboard:\n");
        for (String s : a) {
            txtrUsername.append(s+"\n");
        }

    }

    public static void setTurnLabel(String text) {

        turnLabel.setText(text);
    }

    public static void setTurn(boolean a) {
        turn = a;
    }

    public static boolean getTurn() {
        return turn;
    }

    /**
     * Launch the application.
     */

    // public static void main(String[] args) {
    // EventQueue.invokeLater(new Runnable() {
    // public void run() {
    // try {
    // Crossword window = new Crossword();
    // window.f.setVisible(true);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // /*
    // * charSet = new char[20][20]; for (int i=0;i<20;i++) { for (int j = 0;
    // * j<20;j++) { charSet[i][j]= } }
    // */
    // }
    // });
    // }

    public static void setWord(String a) {
        word = a;
    }

    public static String getWord() {
        return word;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String a) {
        status = a;
    }

    public static void setFirstX(int x) {
        firstX = x;
    }

    public static void setFirstY(int y) {
        firstY = y;
    }

    public static void setX(int x) {
        xCoordinate = x;
    }

    public static void setInd(int index) {
        ind = index;
    }

    public static int getInd() {
        return ind;
    }

    public static int getFirstX() {
        return firstX;
    }

    public static int getFirstY() {
        return firstY;
    }

    public static void setY(int y) {
        yCoordinate = y;
    }

    public static int getinputX() {
        return inputX;
    }

    public static int getinputY() {
        return inputY;
    }

    public static void setinputX(int x) {
        inputX = x;
    }

    public static void setinputY(int y) {
        inputY = y;
    }

    public static int getX() {
        return xCoordinate;
    }

    public static int getY() {
        return yCoordinate;
    }

    /**
     * Create the application.
     */
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
        f.getContentPane().setLayout(new BorderLayout());
        f.setTitle(myClient.getUserName() + "'s game");

        JPanel container = new JPanel(new FlowLayout());
        final CrosswordPanel panel = new CrosswordPanel();
        container.add(panel);
        f.getContentPane().add(container, BorderLayout.CENTER);

        generate(panel);

        JPanel InputContainer = new JPanel(new FlowLayout());

        /*
         * InputContainer.add(new JLabel("Please enter:")); InputContainer.add(new
         * JLabel("x:"));
         * 
         * // JTextField xTextfield = new JTextField(); // xTextfield.setColumns(1); //
         * InputContainer.add(xTextfield);
         * 
         * Integer[] indexData = new Integer[20]; for (int i = 0; i < 20; i++) {
         * indexData[i] = i + 1; } JComboBox<Integer> xComboBox = new
         * JComboBox<>(indexData); InputContainer.add(xComboBox);
         * 
         * InputContainer.add(new JLabel("y:")); // JTextField yTextfield = new
         * JTextField(); // yTextfield.setColumns(1); // InputContainer.add(yTextfield);
         * JComboBox<Integer> yComboBox = new JComboBox<>(indexData);
         * InputContainer.add(yComboBox);
         * 
         * InputContainer.add(new JLabel("Letter:")); // JTextField xTextfield = new
         * JTextField(); // xTextfield.setColumns(1); // InputContainer.add(xTextfield);
         * 
         * Character[] list = new Character[26]; for (int i = 0; i < 26; i++) {
         * list[i]=(char) (65 + i); } JComboBox<Character> letterComboBox = new
         * JComboBox<>(list); InputContainer.add(letterComboBox);
         */
        JButton voteButton = new JButton("Initiate a Vote");
        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JSONObject vote = new JSONObject();
                // vote.put("WORD", textField.getText());
                // vote.put("COMMAND", "ADD");
                // vote.put("MEANING",textArea.getText());
                // bufferWrite.write(text.toJSONString()+"\n");
                // bufferWrite.write("ADD"+"$$$"+textField.getText()+"$$$"+textArea.getText()+"\n");
                // bufferWrite.flush();
                // bufferWrite.write(arg0);
                //
                if (turn && getStatus().equals("AFTER_SECOND_CLICK")) {

                    try {
                        JSONObject sent = new JSONObject();
                        sent.put("COMMAND", "VOTING");
                        // JSONObject word = new JSONObject();
                        // word.put("WORD", getWord());
                        JSONObject message = new JSONObject();
                        message.put("INIT", getWord());
                        sent.put("MESSAGE", message);
                        bufferWrite.write(sent.toJSONString() + "\n");
                        bufferWrite.flush();


                        JSONObject sent2 = new JSONObject();
                        sent.put("COMMAND", "COORD");
                        // JSONObject word = new JSONObject();
                        // word.put("WORD", getWord());
                        JSONObject message2 = new JSONObject();
                        message2.put("X1", getFirstX());
                        message2.put("Y1", getFirstY());
                        message2.put("X2", getX());
                        message2.put("Y2", getY());

                        sent.put("MESSAGE", message2);
                        bufferWrite.write(sent.toJSONString() + "\n");
                        bufferWrite.flush();

                        /*
                        sent.put("MESSAGE", message);
                        bufferWrite.write(sent.toJSONString() + "\n");
                        bufferWrite.flush();
                        
                        JSONObject sent3 = new JSONObject();
                        sent.put("COMMAND", "COORD");
                        // JSONObject word = new JSONObject();
                        // word.put("WORD", getWord());
                        JSONObject message3 = new JSONObject();
                        message.put("Y1", getFirstY());
                        sent.put("MESSAGE", message);
                        bufferWrite.write(sent.toJSONString() + "\n");
                        bufferWrite.flush();
                        JSONObject sent4 = new JSONObject();
                        sent.put("COMMAND", "COORD");
                        // JSONObject word = new JSONObject();
                        // word.put("WORD", getWord());
                        JSONObject message4 = new JSONObject();
                        message.put("X2", getX());
                        sent.put("MESSAGE", message);
                        bufferWrite.write(sent.toJSONString() + "\n");
                        bufferWrite.flush();
                        JSONObject sent5 = new JSONObject();
                        sent.put("COMMAND", "COORD");
                        // JSONObject word = new JSONObject();
                        // word.put("WORD", getWord());
                        JSONObject message5 = new JSONObject();
                        message.put("Y2", getY());
                        */
                        
                        
                        
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
        // submitButton.addMouseListener(new MouseAdapter(){
        // @Override
        // public void mouseClicked(MouseEvent e) {
        // int x = xComboBox.getSelectedIndex();
        // int y = yComboBox.getSelectedIndex();
        // int index = (x)*20+y;
        // char l = (char) (65+letterComboBox.getSelectedIndex());
        // if
        // ((CrosswordPanel.textFields.get(index).getText().equals(String.valueOf("\u0020"))))
        // {
        // CrosswordPanel.textFields.get(index).setText(Character.toString(l));}
        // Crossword.setStatus("WAITING");

        // else

        ;
        // }

        //
        // });
        // HandleJSON.sendCommand();

        JButton passButton = new JButton("Pass this Turn");
        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JSONObject vote = new JSONObject();
                // vote.put("WORD", textField.getText());
                // vote.put("COMMAND", "ADD");
                // vote.put("MEANING",textArea.getText());
                // bufferWrite.write(text.toJSONString()+"\n");
                // bufferWrite.write("ADD"+"$$$"+textField.getText()+"$$$"+textArea.getText()+"\n");
                // bufferWrite.flush();
                // bufferWrite.write(arg0);
                //
                if (turn && getStatus().equals("INPUT") || turn && getStatus().equals("AFTER_INPUT")) {
                    Crossword.setWord("");

                    try {
                        JSONObject sent = new JSONObject();
                        sent.put("COMMAND", "PASS");
                        // JSONObject word = new JSONObject();
                        // word.put("WORD", getWord());
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

        JButton scoreButton = new JButton("Scoreboard");
        scoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // JSONObject vote = new JSONObject();
                // vote.put("WORD", textField.getText());
                // vote.put("COMMAND", "ADD");
                // vote.put("MEANING",textArea.getText());
                // bufferWrite.write(text.toJSONString()+"\n");
                // bufferWrite.write("ADD"+"$$$"+textField.getText()+"$$$"+textArea.getText()+"\n");
                // bufferWrite.flush();
                // bufferWrite.write(arg0);
                //

                try {
                    JSONObject sent = new JSONObject();
                    sent.put("COMMAND", "SCORE");
                    // JSONObject word = new JSONObject();
                    // word.put("WORD", getWord());
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

        InputContainer.add(voteButton);
        InputContainer.add(passButton);
        InputContainer.add(scoreButton);
        InputContainer.add(turnLabel);
        f.getContentPane().add(InputContainer, BorderLayout.SOUTH);

        JPanel InputContainer2 = new JPanel(new FlowLayout());
        txtrUsername = new JTextArea();
        txtrUsername.setBackground(Color.LIGHT_GRAY);
        txtrUsername.setBounds(0, 0, 300, 800);
        txtrUsername.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        txtrUsername.setText("SCROEBOARD");
        txtrUsername.setLineWrap(true);

        InputContainer2.add(txtrUsername);

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
            textFields = new ArrayList<JButton>();
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

                                    // System.out.println(inputX);
                                    // System.out.println(inputY);
                                    // System.out.println(lastX);
                                    // System.out.println(lastY);

                                    // only one point is chosen
                                    if (xco == inputX && xco == lastX && yco == lastY && yco == inputY) {
                                        Crossword.setWord(
                                                Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).getText());
                                        // System.out.println(Crossword.getInd());
                                        // System.out.println(Crossword.CrosswordPanel.textFields.get(Crossword.getInd()));
                                        // System.out.println(
                                        // Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).getText());
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
                                        // Crossword.CrosswordPanel.textFields.get(Crossword.getInd())
                                        // .setBackground(Color.yellow);
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
