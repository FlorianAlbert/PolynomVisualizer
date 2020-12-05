package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.MainFrameController;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

public class ErrorDialog extends JDialog {

	JLabel lblError;
	
	public ErrorDialog(MainFrameController controller) {

		setTitle("Fehler!");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("images/ausruf.png"));
		setBounds(200, 200, 400, 200);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblHeader = new JLabel("Es ist ein Fehler aufgetreten!");
			lblHeader.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblHeader.setBounds(10, 11, 350, 27);
			contentPanel.add(lblHeader);
			
			lblError = new JLabel("");
			lblError.setFont(new Font("Tahoma", Font.ITALIC, 14));
			lblError.setBounds(10, 50, 350, 27);
			contentPanel.add(lblError);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Schlie\u00dfen");
				okButton.setName("ErrorDialogOkButton");
				okButton.addActionListener(controller);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}   
    
	public void setContent(String error) {
	    lblError.setText(error);
	}
}
