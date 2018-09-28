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
import java.awt.event.ActionEvent;

public class Vote extends JFrame {

	private JPanel contentPane;
	private JLabel lblIsA;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public Vote() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 197);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		lblIsA = new JLabel("Is \"\" a word?");
		GridBagConstraints gbc_lblIsA = new GridBagConstraints();
		gbc_lblIsA.gridwidth = 5;
		gbc_lblIsA.insets = new Insets(0, 0, 5, 5);
		gbc_lblIsA.gridx = 2;
		gbc_lblIsA.gridy = 3;
		contentPane.add(lblIsA, gbc_lblIsA);
		
		JButton btnYes = new JButton("Yes");
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
			}
		});
		GridBagConstraints gbc_btnNo = new GridBagConstraints();
		gbc_btnNo.insets = new Insets(0, 0, 5, 5);
		gbc_btnNo.gridx = 5;
		gbc_btnNo.gridy = 5;
		contentPane.add(btnNo, gbc_btnNo);
	}

}
