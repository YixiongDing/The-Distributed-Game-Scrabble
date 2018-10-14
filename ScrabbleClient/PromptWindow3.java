// Project Name: Distributed System Project 2
// Team name: Onmyoji
// Team member: Yixiong Ding, Guangzhe Lan, Sihan Liu, Wuang Shen, Zhenhao Yu 
package ScrabbleClient;
//This is the promptwindow where the prompt message is coded.
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class PromptWindow3 extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JLabel lblIsA;
	private static String promptText="Prompt Message";
    private static PromptWindow3 frame;
    

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public PromptWindow3(String a,MyClient myClient) {
		setTitle(myClient.getUserName() + "'s game");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		lblIsA = new JLabel(a);
		GridBagConstraints gbc_lblIsA = new GridBagConstraints();
		gbc_lblIsA.gridwidth = 5;
		gbc_lblIsA.insets = new Insets(0, 0, 5, 5);
		gbc_lblIsA.gridx = 2;
		gbc_lblIsA.gridy = 3;
		contentPane.add(lblIsA, gbc_lblIsA);
		
		JButton btnGoBack = new JButton("Go Back");
		btnGoBack.addActionListener(this);
		btnGoBack.addMouseListener(new MouseAdapter(){
        	@Override
    		public void mouseClicked(MouseEvent e) {
    			
        		setEnabled(true);
        	}
    				
    		
    		
    	});
		GridBagConstraints gbc_btnYes = new GridBagConstraints();
		gbc_btnYes.insets = new Insets(30, 100, 5, 5);
		gbc_btnYes.gridx = 5;
		gbc_btnYes.gridy = 5;
		contentPane.add(btnGoBack, gbc_btnYes);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		setEnabled(true);

		this.dispose();
	}

}
