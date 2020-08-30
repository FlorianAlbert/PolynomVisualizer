package model;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import service.FunctionParser;
import service.SuperModel;

public class MainFrameModel extends SuperModel {

	private GraphPanelModel graphPanelModel;

	private String functionInputView = "";
	private String functionInput = "";

	private int selectedListIndex;

	private DefaultListModel<String> listModel = new DefaultListModel<>();

	private boolean showInfoDialog;

	public String getFunctionInputView() {
		return functionInputView;
	}

	public void setFunctionInput(String functionInput) {
		String[] newAndOldParts = functionInput.split("\\^");
		String result = newAndOldParts[0];

		if (newAndOldParts.length == 2) {
			String newChar = newAndOldParts[1].substring(0, 1);
			newAndOldParts[1] = newAndOldParts[1].substring(1, newAndOldParts[1].length());
			result = result.concat("<sup>").concat(newChar).concat("</sup>").concat(newAndOldParts[1]);
		} else {
			String[] inputAndRest = functionInput.split("</p>");
			inputAndRest[0].trim();

			if (inputAndRest[0].substring(0, inputAndRest[0].length()).trim().endsWith("-</sup>")
					|| inputAndRest[0].substring(0, inputAndRest[0].length()).trim().endsWith("+</sup>")) {
				inputAndRest[0] = inputAndRest[0].replace("-</sup>", "</sup>-");
				inputAndRest[0] = inputAndRest[0].replace("+</sup>", "</sup>+");
				result = inputAndRest[0].concat(inputAndRest[1]);
			}
		}

		this.functionInputView = result;

		Reader reader = new StringReader(this.functionInputView);
		HTMLEditorKit.Parser parser = new ParserDelegator();
		try {
			this.functionInput = "";
			parser.parse(reader, new HTMLParser(), true);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ValueChanged();
	}

	public void addFunction() {
		if (FunctionParser.checkTerm(functionInput)) {
			if (listModel.getSize() < 10) {
				graphPanelModel.addFunction(functionInput);
				listModel.add(listModel.getSize(), functionInputView);
				functionInputView = "";
				functionInput = "";
			} else {
				// Warnmeldung ausgeben
			}
		} else {
			// Fehlermeldung
			functionInputView = "<html></html>";
			functionInput = "";
		}

		ValueChanged();
	}

	public void removeFunction() {
		graphPanelModel.removeFunction(selectedListIndex);
		listModel.remove(selectedListIndex);
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

	private class HTMLParser extends HTMLEditorKit.ParserCallback {
		private boolean encounteredAnExponent = false;
		private boolean encounteredAFunction = false;

		public void handleText(char[] data, int pos) {
			if (encounteredAFunction) {
				if (encounteredAnExponent) {
					functionInput = functionInput.concat("^");
				}
				functionInput = functionInput.concat(String.valueOf(data));
			}
		}

		public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
			if (t == HTML.Tag.SUP)
				encounteredAnExponent = true;
			if (t == HTML.Tag.P)
				encounteredAFunction = true;
		}

		public void handleEndTag(HTML.Tag t, int pos) {
			if (t == HTML.Tag.SUP)
				encounteredAnExponent = false;
			if (t == HTML.Tag.P)
				encounteredAFunction = false;
		}
	}

}
