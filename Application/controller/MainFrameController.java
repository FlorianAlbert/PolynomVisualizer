package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.MainFrameModel;

public class MainFrameController implements ActionListener, KeyListener, ListSelectionListener, MouseListener, ComponentListener {

	MainFrameModel model;

	public MainFrameController(MainFrameModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (((Component) e.getSource()).getName()) {
			case "Bestätigen" -> model.addFunction(true);
			case "Entfernen" -> model.removeFunction();
			case "infoMenuItem" -> model.openInfoDialog();
			case "DialogOkButton" -> model.closeInfoDialog();
			case "ErrorDialogOkButton" -> model.closeErrorDialog();
			case "btnEnterDerivative" -> model.addDerivative(true);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (((Component) e.getSource()).getName()) {
		case "tfFunctionInput":
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				model.addFunction(false);
			}
			break;
		case "tfNDerivative":
			if (e.getKeyCode() == KeyEvent.VK_ENTER && !((JTextField)e.getSource()).getText().isBlank()) {
				model.addDerivative(false);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (((Component) e.getSource()).getName()) {
			case "tfFunctionInput" -> model.setFunctionInput(((JTextField) e.getSource()).getText());
			case "tfNDerivative" -> model.setNDerivativeInput(((JTextField) e.getSource()).getText());
			default -> {
				String input = ((JTextField) e.getSource()).getText();
				if (input.matches("-?(\\d+(\\.\\d*)?)?")) {
					if (input.matches("-?(\\d+(\\.\\d+)?)?")) {
						try {
							switch (((JTextField) e.getSource()).getName()) {
								case "tfXMin" -> model.setXMin(Double.parseDouble(input));
								case "tfXMax" -> model.setXMax(Double.parseDouble(input));
								case "tfYMin" -> model.setYMin(Double.parseDouble(input));
								case "tfYMax" -> model.setYMax(Double.parseDouble(input));
							}
						} catch (NumberFormatException ex) {
							System.out.println(ex.getMessage());
						}
					}
				} else {
					if (!(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)) {
						int indexToDelete = ((JTextField) e.getSource()).getCaretPosition() - 1;
						char[] result = new char[input.length() - 1];

						for (int i = 0; i < input.length(); i++) {
							if (i < indexToDelete) {
								result[i] = input.toCharArray()[i];
							} else if (i > indexToDelete) {
								result[i - 1] = input.toCharArray()[i];
							}
						}

						((JTextField) e.getSource()).setText(String.valueOf(result));
					}
				}
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		model.setSelectedListIndex(((JList) e.getSource()).getSelectedIndex());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			switch (((JTextField) e.getSource()).getName()) {
				case "tfXMin", "tfXMax", "tfYMin", "tfYMax" -> ((JTextField) e.getSource()).selectAll();
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

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		model.setNDerivativeInput("");
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}
