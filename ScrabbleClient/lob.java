package ScrabbleClient;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;


public class lob extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public lob() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Lobby");
		setBounds(100, 100, 640, 455);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 447, 326);
		contentPane.add(scrollPane);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.setBackground(Color.LIGHT_GRAY);
		btnNewButton.setBounds(52, 360, 120, 46);
		contentPane.add(btnNewButton);
		
		JButton button = new JButton("Logout");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		button.setBackground(Color.LIGHT_GRAY);
		button.setBounds(267, 360, 120, 46);
		contentPane.add(button);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(467, 10, 147, 312);
		contentPane.add(scrollPane_1);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		scrollPane_1.setColumnHeaderView(btnRefresh);
		
		JList list = new JList();
		scrollPane_1.setViewportView(list);
		 list.setBorder(BorderFactory.createTitledBorder("UserList"));
	        list.addMouseListener(new MouseAdapter() 
	                {
	                    public void mousePressed(MouseEvent e) 
	                    {

	                    	
	                    	
	                    	

	                    }
	                });
		 
		 JButton btnNewButton_1 = new JButton("Invite");
		 btnNewButton_1.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 	}
		 });
		 btnNewButton_1.setBounds(467, 319, 147, 23);
		 contentPane.add(btnNewButton_1);
		 
		 JTextArea textArea = new JTextArea();
		 textArea.setBounds(10, 11, 445, 324);
		 contentPane.add(textArea);
		 
	}
}
