package ScrabbleClient;
//this is a scoreboard window in which the scores received from the server will be displayed.

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTextField;
import java.awt.Color;

public class Scoreboard extends JFrame {

	private JPanel contentPane;


	public Scoreboard(ArrayList<String> toPrint) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextArea txtrScoreboard = new JTextArea();
		txtrScoreboard.setBackground(Color.ORANGE);
		txtrScoreboard.setForeground(Color.DARK_GRAY);
		txtrScoreboard.setFont(new Font("Monospaced", Font.BOLD, 40));
		txtrScoreboard.setText("SCOREBOARD");
		contentPane.add(txtrScoreboard, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JTextArea txtrUsername = new JTextArea();
		txtrUsername.setBackground(Color.LIGHT_GRAY);
		txtrUsername.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		txtrUsername.setLineWrap(true);
		for (String a:toPrint) {
		    txtrUsername.append(a+"\n");
		}

		scrollPane.setViewportView(txtrUsername);
	}

}
