package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame extends JFrame{
	
	JFrame frame = new JFrame("Polynom Grapher");
	
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
	
	public MainFrame() {
		setLayout(new BorderLayout());
		
		centerPanel();
		leftPanel();
		bottomPanel();
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		
		
		
	}
	
	public void centerPanel() {
		CoordinateSystem cs = new CoordinateSystem();
		add(cs, BorderLayout.CENTER);
		cs.setFunctions(new String[] {"x^2", "x^2-0.2", "x^2+0.2", "x^3+x^2", "-x^+3", "x^6-3", "x^5", "x^2+0.6", "-x^2-5", "x^2+0.8"});
		new Thread(cs).start();
	}
	
	public void leftPanel() {
		JPanel leftP = new JPanel();
		leftP.setPreferredSize(new Dimension(250, 768));
		add(leftP, BorderLayout.WEST);
	}
	
	public void bottomPanel() {
		JTextField textbox = new JTextField();
		add(textbox, BorderLayout.SOUTH);
	}

}