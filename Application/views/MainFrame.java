package views;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.MainFrameController;
import model.GraphPanelModel;
import service.ValueChangedListener;

public class MainFrame extends JFrame implements ValueChangedListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2853729456871453019L;
	private JPanel contentPane;
	
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
		GraphPanelModel model = new GraphPanelModel(-5, 5, -5, 5);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 451, 337);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		GraphPanel panel = new GraphPanel(model);
		model.addValueChangedListener(panel);
		panel.setBounds(10, 11, 418, 244);
		contentPane.add(panel);

		JTextField tfFunctionInput = new JTextField("", 20);
		tfFunctionInput.setBounds(10, 269, 250, 25);
		contentPane.add(tfFunctionInput);
		
		MainFrameController controller = new MainFrameController(model, tfFunctionInput);

		JButton btnBestaetig = new JButton("Bestätigen");
		btnBestaetig.addActionListener(controller);
		
		JButton btnDelete = new JButton("Entfernen");
		btnDelete.addActionListener(controller);
		
		btnBestaetig.setActionCommand("Bestätigen");
		btnBestaetig.setBounds(270, 269, 70, 23);
		contentPane.add(btnBestaetig);	
		
		btnDelete.setActionCommand("Entfernen");
		btnDelete.setBounds(350, 269, 70, 23);
		contentPane.add(btnDelete);			
	}

	@Override
	public void onValueChanged() {

		
	}
}