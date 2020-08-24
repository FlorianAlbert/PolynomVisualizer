package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.GraphPanelModel;

public class MainFrameController implements ActionListener{
	
	GraphPanelModel model;

	public MainFrameController(GraphPanelModel model) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	model.setInput(e);
		
	}
	
}
