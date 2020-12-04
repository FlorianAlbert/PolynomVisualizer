package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import controller.GraphPanelController;
import model.GraphPanelModel;
import service.ValueChangedListener;

/**
 * @author Florian Albert
 * 
 * @version 0.2
 * 
 */

public class GraphPanel extends JPanel implements ValueChangedListener {

	private final CoordinateSystem coordinateSystem;
	private final GraphPanelModel model;

	public GraphPanel(GraphPanelModel model) {
		Border border = getBorder();
		Border margin = new LineBorder(Color.black, 1);
		setBorder(new CompoundBorder(border, margin));

		this.model = model;
		GraphPanelController controller = new GraphPanelController(model);

		coordinateSystem = new CoordinateSystem(this.model);

		this.addMouseWheelListener(controller);
		this.addMouseListener(controller);
		this.addMouseMotionListener(controller);
	}

	private void paintFunctions(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

		for (int i = 0; i < model.getFunctionPaths().length; i++) {
			g2d.setColor(model.getColors()[i]);
			if (model.getFunctionPaths()[i] != null) {
				g2d.draw(model.getFunctionPaths()[i]);
			}
		}
	}

	@Override
	protected synchronized void paintComponent(Graphics g) {
		super.paintComponent(g);

		coordinateSystem.paint(g);

		paintFunctions(g);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);

		model.setPanelWidth(width);
		model.setPanelHeight(height);
	}

	@Override
	public void onValueChanged() {
		repaint();
		setCursor(new Cursor(model.getCursorType()));
	}
}