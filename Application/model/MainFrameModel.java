package model;

import java.awt.Color;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.DefaultListModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;

import service.Calculator;
import service.FunctionParser;
import service.SuperModel;

public class MainFrameModel extends SuperModel {

	private GraphPanelModel graphPanelModel;
	
	private final Calculator calc = new Calculator();
	
	private final HTMLEditorKit.Parser parser = new ParserDelegator();

	private String functionInput = "";
	
	private String functionBuffer;

	private String errorDialogInfo;

	private int selectedListIndex;

	private int nDerivativeInput = 0;

	private final DefaultListModel<String> listModel = new DefaultListModel<>();

	private boolean showInfoDialog;

	private boolean showErrorDialog;

	private boolean isInputAddedByButton;

	private boolean isDerivativeAddedByButton;

	public void setFunctionInput(String functionInput) {
		this.functionInput = functionInput;
		ValueChanged();
	}

	public void addFunction(boolean isInputAddedByButton) {
		this.isInputAddedByButton = isInputAddedByButton;

		if (FunctionParser.checkTerm(functionInput)) {
			if (listModel.getSize() < 10) {
				graphPanelModel.addFunction(functionInput);

				Color currentColor = graphPanelModel.getColors()[listModel.getSize()];
				String[] parts = functionInput.split("\\^");
				String functionInputView = "<html><body style=\"color: rgb(" + currentColor.getRed() + ", "
						+ currentColor.getGreen() + ", " + currentColor.getBlue() + ");\">f(x) = " + parts[0];
				for (int i = 1; i < parts.length; i++) {
					int endIndexPlus;
					if (parts[i].startsWith("+")) {
						endIndexPlus = parts[i].indexOf("+", 1);
					} else {
						endIndexPlus = parts[i].indexOf("+");
					}
					int endIndexMinus = parts[i].indexOf("-");

					int actualEndIndex;
					if (endIndexPlus >= 0 && endIndexMinus >= 0) {
						actualEndIndex = Math.min(endIndexPlus, endIndexMinus);
					} else {
						actualEndIndex = Math.max(endIndexPlus, endIndexMinus);
					}

					if (actualEndIndex >= 0) {
						functionInputView = functionInputView.concat("<sup>")
								.concat(parts[i].substring(0, actualEndIndex)).concat("</sup>")
								.concat(parts[i].substring(actualEndIndex));
					} else {
						functionInputView = functionInputView.concat("<sup>").concat(parts[i]).concat("</sup>");
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

	public void addDerivative(boolean isDerivativeAddedByButton) {
		this.isDerivativeAddedByButton = isDerivativeAddedByButton;
		
		functionBuffer = "";
		Reader reader = new StringReader(listModel.get(selectedListIndex));
		try {
			parser.parse(reader, new HTMLFunctionParser(), true);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		functionBuffer = functionBuffer.substring(7);
		calc.setTerm(functionBuffer);
		functionBuffer = calc.getDerivative(nDerivativeInput);
		
		String functionInputSave = functionInput;
		functionInput = functionBuffer;
		
		addFunction(false);
		
		functionInput = functionInputSave;
		
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

	public int getSelectedListIndex() {
		return selectedListIndex;
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

	public String getErrorDialogInfo() {
		return errorDialogInfo;
	}

	public boolean isInputAddedByButton() {
		return isInputAddedByButton;
	}

	public void setIsInputAddedByButton(boolean isInputAddedByButton) {
		this.isInputAddedByButton = isInputAddedByButton;
	}

	public void setNDerivativeInput(String text) {
		if (text.isBlank()) {
			nDerivativeInput = 0;
		} else {
			try {
				nDerivativeInput = Integer.parseInt(text);
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		ValueChanged();
	}

	public int getNDerivativeInput() {
		return nDerivativeInput;
	}
	
	private class HTMLFunctionParser extends HTMLEditorKit.ParserCallback {
		private boolean encounteredAFunction = false;
		private boolean encounteredAnExponent = false;
		
		@Override
		public void handleText(char[] data, int pos) {
			if (encounteredAFunction) {
				if (encounteredAnExponent) {
					functionBuffer = functionBuffer.concat("^");
				}
				functionBuffer = functionBuffer.concat(String.valueOf(data));
			}
		}
		
		@Override
		public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
			if (t == HTML.Tag.BODY) {
				encounteredAFunction = true;
			}
			
			if (t == HTML.Tag.SUP) {
				encounteredAnExponent = true;
			}
		}
		
		@Override
		public void handleEndTag(Tag t, int pos) {
			if (t == HTML.Tag.BODY) {
				encounteredAFunction = false;
			}
			
			if (t == HTML.Tag.SUP) {
				encounteredAnExponent = false;
			}
		}
	}

	public boolean isDerivativeAddedByButton() {
		return isDerivativeAddedByButton;
	}

	public void setIsDerivativeAddedByButton(boolean isDerivativeAddedByButton) {
		this.isDerivativeAddedByButton = isDerivativeAddedByButton;
	}
}
