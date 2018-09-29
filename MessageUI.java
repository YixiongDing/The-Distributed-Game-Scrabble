//Student Name: Wuang Shen, Student ID:716090
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MessageUI {

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public MessageUI(String message) {
		initialize(message);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String message) {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 100);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 450, 78);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(166, 43, 117, 29);
		panel.add(btnOk);
		
		JLabel lblNewLabel = new JLabel(message);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(3, 16, 444, 16);
		panel.add(lblNewLabel);
		btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnOk.addKeyListener(new KeyAdapter() {
			  public void keyPressed(KeyEvent e) {
				    if (e.getKeyCode()==KeyEvent.VK_ENTER){
						frame.dispose();
				    }
			  }
			});
		
		frame.setVisible(true);
	}
}
