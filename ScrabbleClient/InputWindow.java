// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 
package ScrabbleClient;
// this is the input window where the input of the letter is conducted.
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.awt.Window.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class InputWindow extends JFrame implements ActionListener {

    private JPanel contentPane;
    private static ArrayList<JButton> characterList;
    private JLabel label[][];
    private static InputWindow frame;
    private static JComboBox<Character> comboBox;
    private static Crossword c;
    private static MyClient myClient;
    private JSONParser parser = new JSONParser();

    /**
     * Launch the application.
     */

    public static InputWindow getFrame() {
        return frame;
    }

    /**
     * Create the frame.
     */
    public InputWindow(Crossword c, MyClient mc) {
        this.myClient = mc;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        Character[] list = new Character[26];
        for (int i = 0; i < 26; i++) {
            list[i] = (char) (65 + i);
        }

        comboBox = new JComboBox<>(list);
        GridBagConstraints gbc_comboBox = new GridBagConstraints();
        gbc_comboBox.insets = new Insets(0, 0, 5, 0);
        gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBox.gridx = 2;
        gbc_comboBox.gridy = 3;
        panel.add(comboBox, gbc_comboBox);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this);
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

        });
        GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
        gbc_btnSubmit.gridx = 2;
        gbc_btnSubmit.gridy = 5;
        panel.add(submitButton, gbc_btnSubmit);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        char l = (char) (65 + comboBox.getSelectedIndex());
        Crossword.CrosswordPanel.textFields.get(Crossword.getInd()).setText(Character.toString(l));
        Crossword.setStatus("AFTER_INPUT");
        Crossword.setinputX(Crossword.getX());
        Crossword.setinputY(Crossword.getY());

        try {
            //sending a json message of new letter and its coord.
            JSONObject sendMessage = new JSONObject();
            JSONObject message = new JSONObject();
            message.put("INDEX", Crossword.getInd());
            message.put("LETTER", Character.toString(l));
            sendMessage.put("COMMAND", "NEW");
            sendMessage.put("MESSAGE", message);

            myClient.getBufferWrite().write(sendMessage.toJSONString() + "\n");
            myClient.getBufferWrite().flush();
            System.out.println(sendMessage.toJSONString());

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        this.dispose();
        PromptWindow2 newFrame = new PromptWindow2("       Click a block with a letter as the start of a word!",myClient.getUserName());
        newFrame.setVisible(true);

    }
}
