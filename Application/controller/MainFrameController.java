package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Caret;

import model.MainFrameModel;

public class MainFrameController implements ActionListener, KeyListener, ListSelectionListener, MouseListener {

	MainFrameModel model;

	public MainFrameController(MainFrameModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (((Component) e.getSource()).getName()) {
		case "Bestätigen":
			model.addFunction();
			break;
		case "Entfernen":
			model.removeFunction();
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (((JTextField) e.getSource()).getName().equals("tfFunctionInput")) {
			model.setFunctionInput(((JTextField) e.getSource()).getText());
		} else {
			String input = ((JTextField) e.getSource()).getText();
			if (input.matches("-?(\\d+(\\.\\d*)?)?")) {
				if (input.matches("-?(\\d+\\.\\d+)?")) {
					try {
						switch (((JTextField) e.getSource()).getName()) {
						case "tfXMin":
							model.setXMin(Double.parseDouble(input));
							break;
						case "tfXMax":
							model.setXMax(Double.parseDouble(input));
							break;
						case "tfYMin":
							model.setYMin(Double.parseDouble(input));
							break;
						case "tfYMax":
							model.setYMax(Double.parseDouble(input));
							break;
						}
					} catch (NumberFormatException ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		model.setSelectedListIndex(e.getFirstIndex());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			switch (((JTextField) e.getSource()).getName()) {
			case "tfXMin":
			case "tfXMax":
			case "tfYMin":
			case "tfYMax":
				((JTextField) e.getSource()).selectAll();
				break;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
