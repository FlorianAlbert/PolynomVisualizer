package views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		MainFrameController controller = new MainFrameController(model);
		
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

		JButton btnBestaetig = new JButton("Bestätigen");
		btnBestaetig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = tfFunctionInput.getText();
				tfFunctionInput.setText("");
				String[] inputArray = new String[] {input};
				model.setFunctions(inputArray);
			}
		});
		
		btnBestaetig.setBounds(300, 269, 89, 23);
		contentPane.add(btnBestaetig);		
	}

	@Override
	public void onValueChanged() {

		
	}
}