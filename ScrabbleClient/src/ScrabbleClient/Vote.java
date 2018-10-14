// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 

package ScrabbleClient;
//this is a vote window in which the voting messages received from the server will be displayed.
// this allows the player to vote whether a word is valid or not.

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONObject;

import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Vote extends JFrame {

    private JPanel contentPane;
    private JLabel lblIsA;


    /**
     * Create the frame.
     */
    public Vote(String word, MyClient myClient) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 350, 197);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        contentPane.setLayout(gbl_contentPane);

        lblIsA = new JLabel(word);
        GridBagConstraints gbc_lblIsA = new GridBagConstraints();
        gbc_lblIsA.gridwidth = 5;
        gbc_lblIsA.insets = new Insets(0, 0, 5, 5);
        gbc_lblIsA.gridx = 2;
        gbc_lblIsA.gridy = 3;
        contentPane.add(lblIsA, gbc_lblIsA);

        // two buttons are allowed yes and no
        JButton btnYes = new JButton("Yes");
        btnYes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    JSONObject sent = new JSONObject();
                    sent.put("COMMAND", "VOTING");
                    JSONObject message = new JSONObject();
                    message.put("VOTE", "YES");
                    sent.put("MESSAGE", message);
                    myClient.getBufferWrite().write(sent.toJSONString() + "\n");
                    // if pressed yes, a json message is sent.
                    myClient.getBufferWrite().flush();
                    System.out.println(sent.toJSONString());
                    Crossword.CrosswordPanel.setColorAll();

                    dispose();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        });
        GridBagConstraints gbc_btnYes = new GridBagConstraints();
        gbc_btnYes.insets = new Insets(0, 0, 5, 5);
        gbc_btnYes.gridx = 3;
        gbc_btnYes.gridy = 5;
        contentPane.add(btnYes, gbc_btnYes);

        JButton btnNo = new JButton("No");
        btnNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JSONObject sent = new JSONObject();
                    sent.put("COMMAND", "VOTING");
                    JSONObject message = new JSONObject();
                    message.put("VOTE", "NO");
                    sent.put("MESSAGE", message);
                    myClient.getBufferWrite().write(sent.toJSONString() + "\n");
                    // if pressed no, another json message is sent.

                    myClient.getBufferWrite().flush();
                    System.out.println(sent.toJSONString());
                    Crossword.CrosswordPanel.setColorAll();
                    dispose();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        GridBagConstraints gbc_btnNo = new GridBagConstraints();
        gbc_btnNo.insets = new Insets(0, 0, 5, 5);
        gbc_btnNo.gridx = 5;
        gbc_btnNo.gridy = 5;
        contentPane.add(btnNo, gbc_btnNo);
    }
}
