package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

public class GraphPanel extends JPanel implements Runnable, ValueChangedListener{

	private static final long serialVersionUID = -4286522701957270175L;
	private int panelHeight;
	private int panelWidth;

	private CoordinateSystem coordinateSystem;
	private GraphPanelModel model;

	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;

	private String[] functions = new String[10];
	private volatile int[][] values;

	private Point mousePressingPoint;

	private Color[] colors = { Color.BLUE, Color.RED, Color.GREEN, Color.BLACK, Color.CYAN, Color.MAGENTA, Color.ORANGE,
			Color.GRAY, Color.PINK, Color.YELLOW };


	// Threadpool with 4 Mainpoolsize and 8 Maxpoolsize
	ThreadPoolExecutor tPool = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4));


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

	private void paintFunctions(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

		for (int i = 0; i < model.getFunctions().length; i++) {
			g2d.setColor(colors[i]);
			if (model.getFunctions()[i] != null) {
				Path2D path = new Path2D.Double();

				path.moveTo(0, model.getFunctionValues()[i][0]);
				for (int j = 1; j < model.getPanelWidth(); j++) {
					path.lineTo(j, model.getFunctionValues()[i][j]);
				}

				g2d.draw(path);
			}
		}
	}

	@Override
	public void run() {
		synchronized (this) {
			values = model.evaluateFunctions(functions);
		}

		repaint();
	}

	@Override
	public void onValueChanged() {
		repaint();
		setCursor(new Cursor(model.getCursorType()));
	}
}