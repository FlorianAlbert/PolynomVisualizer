package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import model.GraphPanelModel;
import service.UnitLocation;
import service.ValueChangedListener;

public class CoordinateSystem {

	private GraphPanelModel model;

	public CoordinateSystem(GraphPanelModel m) {
		model = m;
	}

	public void paint(Graphics g) {

		model.setFontMetrics(g.getFontMetrics());

		Graphics2D g2d = (Graphics2D) g;

		int xAxisY = model.yToPixel(0.0);
		int yAxisX = model.xToPixel(0.0);

		g2d.setColor(Color.BLACK); // Y- and X-Axis
		g2d.drawLine(0, xAxisY, model.panelWidth, xAxisY);
		g2d.drawLine(yAxisX, 0, yAxisX, model.panelHeight);

		for (UnitLocation unitPoint : model.getUnitPoints()) {
			g2d.drawString(Integer.toString(unitPoint.getValue()), unitPoint.getValLocation().x,
					unitPoint.getValLocation().y);
			g2d.drawLine(unitPoint.getNeg().x, unitPoint.getNeg().y, unitPoint.getPos().x, unitPoint.getPos().y);
		}
	}

	public GraphPanelModel getModel() {
		return model;
	}

	public void setModel(GraphPanelModel model) {
		this.model = model;
	}

	public void drawNumber(Graphics g, int number, int x, int y) {
		g.drawString(Integer.toString(number), x, y);
	}
}
