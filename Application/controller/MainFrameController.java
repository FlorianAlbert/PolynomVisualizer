package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import model.GraphPanelModel;

public class MainFrameController implements ActionListener{
	
	GraphPanelModel model;
	JTextField tfFunctionInput;

	public MainFrameController(GraphPanelModel model, JTextField textField) {
		tfFunctionInput = textField;
		this.model = model;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Bestätigen")) {
		model.addFunction(tfFunctionInput.getText());
		tfFunctionInput.setText("");
		}
		else if(e.getActionCommand().equals("Entfernen")){
			model.resetFunctions();
		}
	}
	
}
