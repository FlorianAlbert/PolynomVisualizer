package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5394447692344610358L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 451, 337);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		GraphPanel panel = new GraphPanel(-2, 2, -2, 2);
		panel.setBounds(10, 11, 418, 244);
		contentPane.add(panel);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setFunctions(new String[] {"x^2", "x^2-0.2", "x^2+0.2", "x^2-0.4", "-x^+3", "x^2+0.4", "x^2-0.6", "x^2+0.6", "x^2-0.8", "x^2+0.8"});
			}
		});
		btnRefresh.setBounds(10, 269, 89, 23);
		contentPane.add(btnRefresh);
	}
}
