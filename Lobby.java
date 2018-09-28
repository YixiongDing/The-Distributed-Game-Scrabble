import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.FlowLayout;
import java.awt.Color;

public class Lobby extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Lobby frame = new Lobby();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Lobby() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500				);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel scrollPane = new JPanel();
		contentPane.add(scrollPane, BorderLayout.EAST);
		GridBagLayout gbl_scrollPane = new GridBagLayout();
		gbl_scrollPane.columnWidths = new int[]{140, 0};
		gbl_scrollPane.rowHeights = new int[]{20, 0, 0};
		gbl_scrollPane.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_scrollPane.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		scrollPane.setLayout(gbl_scrollPane);
		JLabel lblUsers = new JLabel("Other Players         ");
		GridBagConstraints gbc_lblUsers = new GridBagConstraints();
		gbc_lblUsers.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblUsers.gridx = 0;
		gbc_lblUsers.gridy = 1;
		scrollPane.add(lblUsers, gbc_lblUsers);

		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnStartGame = new JButton("Start game");
		panel.add(btnStartGame);
		
		
		
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.NORTH);
		
		JLabel lblGameLobby = new JLabel("Game Lobby");
		lblGameLobby.setForeground(Color.BLUE);
		lblGameLobby.setFont(new Font("Verdana", Font.PLAIN, 40));
		panel_1.add(lblGameLobby);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.WEST);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{82, 146, 81, 0};
		gbl_panel_3.rowHeights = new int[]{29, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JButton btnNewButton = new JButton("Sign in");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JLabel lblUserName = new JLabel("User name:");
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 16));
		GridBagConstraints gbc_lblUserName = new GridBagConstraints();
		gbc_lblUserName.insets = new Insets(0, 0, 5, 5);
		gbc_lblUserName.gridx = 0;
		gbc_lblUserName.gridy = 1;
		panel_3.add(lblUserName, gbc_lblUserName);
		
		JTextArea txtrTheFollowingPlayers = new JTextArea();
		txtrTheFollowingPlayers.setFont(new Font("Monospaced", Font.PLAIN, 18));
		GridBagConstraints gbc_txtrTheFollowingPlayers = new GridBagConstraints();
		gbc_txtrTheFollowingPlayers.gridwidth = 2;
		gbc_txtrTheFollowingPlayers.insets = new Insets(0, 0, 5, 0);
		gbc_txtrTheFollowingPlayers.gridx = 1;
		gbc_txtrTheFollowingPlayers.gridy = 1;
		panel_3.add(txtrTheFollowingPlayers, gbc_txtrTheFollowingPlayers);
		txtrTheFollowingPlayers.setText("Players that have joined your game:");
		
		textField = new JTextField();
		textField.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 2;
		panel_3.add(textField, gbc_textField);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 3;
		panel_3.add(btnNewButton, gbc_btnNewButton);
		
		JButton btnSingOut = new JButton("Sing out");
		GridBagConstraints gbc_btnSingOut = new GridBagConstraints();
		gbc_btnSingOut.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSingOut.insets = new Insets(0, 0, 0, 5);
		gbc_btnSingOut.gridx = 0;
		gbc_btnSingOut.gridy = 4;
		panel_3.add(btnSingOut, gbc_btnSingOut);
	}

}
