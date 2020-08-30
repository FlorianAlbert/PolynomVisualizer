package model;

import java.awt.Color;

import javax.swing.DefaultListModel;

import service.FunctionParser;
import service.SuperModel;

public class MainFrameModel extends SuperModel {

	private GraphPanelModel graphPanelModel;

	private String functionInput = "";
	
	private String errorDialogInfo;

	private int selectedListIndex;

	private DefaultListModel<String> listModel = new DefaultListModel<>();

	private boolean showInfoDialog;
	
	private boolean showErrorDialog;

	private boolean isInputFunctionChanged = false;

	public void setFunctionInput(String functionInput) {
		this.functionInput = functionInput;
		ValueChanged();
	}

	public void addFunction() {
		if (FunctionParser.checkTerm(functionInput)) {
			if (listModel.getSize() < 10) {
				graphPanelModel.addFunction(functionInput);

				Color currentColor = graphPanelModel.getColors()[listModel.getSize()];
				String[] parts = functionInput.split("\\^");
				String functionInputView = "<html><body style=\"color: rgb(" 
				+ currentColor.getRed() + ", " + currentColor.getGreen() + ", " + currentColor.getBlue() + ");\">f(x) = "
						+ parts[0];
				for (int i = 1; i < parts.length; i++) {
					int endIndexPlus;
					int endIndexMinus;
					if (parts[i].startsWith("+")) {
						endIndexPlus = parts[i].indexOf("+", 1);
						endIndexMinus = parts[i].indexOf("-");
					} else {
						endIndexPlus = parts[i].indexOf("+");
						endIndexMinus = parts[i].indexOf("-");
					}

					int actualEndIndex;
					if (endIndexPlus >= 0 && endIndexMinus >= 0) {
						actualEndIndex = Math.min(endIndexPlus, endIndexMinus);
					} else {
						actualEndIndex = Math.max(endIndexPlus, endIndexMinus);
					}

					if (actualEndIndex >= 0) {
						functionInputView = functionInputView.concat("<sup>")
								.concat(parts[i].substring(0, actualEndIndex)).concat("</sup>")
								.concat(parts[i].substring(actualEndIndex, parts[i].length()));
					} else {
						functionInputView = functionInputView.concat("<sup>")
								.concat(parts[i]).concat("</sup>");
					}
				}
				
				functionInputView = functionInputView.concat("</body></html>");

				listModel.add(listModel.getSize(), functionInputView);
				functionInput = "";
			} else {
				errorDialogInfo = "Sie k\u00f6nnen maximal zehn Funktionen eingeben.";
				showErrorDialog = true;
			}
		} else {
		    	errorDialogInfo = "Die eingegebene Funktion ist nicht korrekt.";
			showErrorDialog = true;
			functionInput = "";
		}

		isInputFunctionChanged = true;
		ValueChanged();
	}

	public void removeFunction() {
		graphPanelModel.removeFunction(selectedListIndex);
		listModel.remove(selectedListIndex);
		
		int limit = listModel.getSize();
		for (int i = 0; i < limit; i++) {
			String entry = listModel.get(i);
			listModel.remove(i);
			String oldRGB = entry.substring(entry.indexOf("("), entry.indexOf(")") + 1);
			Color newColor = graphPanelModel.getColors()[i];
			String newRGB = "(" + newColor.getRed() + ", " + newColor.getGreen() + ", " + newColor.getBlue() + ")";
			entry = entry.replace(oldRGB, newRGB);
			listModel.add(i, entry);
		}
		
		ValueChanged();
	}

	public void openInfoDialog() {
		showInfoDialog = true;
		ValueChanged();
	}

	public void closeInfoDialog() {
		showInfoDialog = false;
		ValueChanged();
	}
	
	public void openErrorDialog() {
		showErrorDialog = true;
		ValueChanged();
	}

	public void closeErrorDialog() {
		showErrorDialog = false;
		ValueChanged();
	}

	public void setXMin(double xMin) {
		graphPanelModel.setxMin(xMin, false);
	}

	public void setXMax(double xMax) {
		graphPanelModel.setxMax(xMax, false);
	}

	public void setYMin(double yMin) {
		graphPanelModel.setyMin(yMin, false);
	}

	public void setYMax(double yMax) {
		graphPanelModel.setyMax(yMax, false);
	}

	public double getXMin() {
		return graphPanelModel.getXMin();
	}

	public double getXMax() {
		return graphPanelModel.getXMax();
	}

	public double getYMin() {
		return graphPanelModel.getYMin();
	}

	public double getYMax() {
		return graphPanelModel.getYMax();
	}

	public void setGraphPanelModel(GraphPanelModel graphPanelModel) {
		this.graphPanelModel = graphPanelModel;
	}

	public DefaultListModel<String> getListModel() {
		return listModel;
	}

	public void setSelectedListIndex(int selectedListIndex) {
		this.selectedListIndex = selectedListIndex;
		ValueChanged();
	}

	public boolean isValueSetByGraphPanel() {
		return graphPanelModel.isValueSetByGraphPanel();
	}

	public String getFunctionInput() {
		return functionInput;
	}

	public boolean getShowInfoDialog() {
		return showInfoDialog;
	}
	
	public boolean getShowErrorDialog() {
	    return showErrorDialog;
	}

	public boolean isInputFunctionChanged() {
		return isInputFunctionChanged;
	}

	public void setIsInputFunctionChanged(boolean isInputFunctionChanged) {
		this.isInputFunctionChanged = isInputFunctionChanged;
	}
	
	public String getErrorDialogInfo() {
	    return errorDialogInfo;
	}
}
