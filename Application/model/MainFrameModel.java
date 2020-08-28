package model;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import service.SuperModel;

public class MainFrameModel extends SuperModel {

	private GraphPanelModel graphPanelModel;

	private String functionInput = "";

	private int selectedListIndex;

	private DefaultListModel<String> listModel = new DefaultListModel<>();

	public String getFunctionInput() {
		return functionInput;
	}

	public void setFunctionInput(String functionInput) {
		this.functionInput = functionInput;
		ValueChanged();
	}

	public void addFunction() {
		if (listModel.getSize() < 10) {
			graphPanelModel.addFunction(functionInput);
			listModel.add(listModel.getSize(), functionInput);
			functionInput = "";
		} else {
			// Warnmeldung ausgeben
		}
		ValueChanged();
	}

	public void removeFunction() {
		listModel.remove(selectedListIndex);
		graphPanelModel.removeFunction(selectedListIndex);
		ValueChanged();
	}

	public void setXMin(double xMin) {
		graphPanelModel.setxMin(xMin);
	}

	public void setXMax(double xMax) {
		graphPanelModel.setxMax(xMax);
	}

	public void setYMin(double yMin) {
		graphPanelModel.setyMin(yMin);
	}

	public void setYMax(double yMax) {
		graphPanelModel.setyMax(yMax);
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

}
