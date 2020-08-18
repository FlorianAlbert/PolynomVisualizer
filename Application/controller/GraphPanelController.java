package controller;

import java.awt.event.MouseAdapter;

import model.GraphPanelModel;

public class GraphPanelController extends MouseAdapter {

    GraphPanelModel model;
    
    public GraphPanelController(GraphPanelModel model) {
        this.model = model;
    }
}