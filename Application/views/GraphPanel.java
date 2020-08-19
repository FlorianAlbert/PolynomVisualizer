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

	/**
	 * Create the GraphPanel
	 */

	// public GraphPanel() {
	// 	this(-10, 10, -10, 10, );
	// }

	public GraphPanel(double xMin, double xMax, double yMin, double yMax, GraphPanelModel model) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;

		Border border = getBorder();
		Border margin = new LineBorder(Color.black, 1);
		setBorder(new CompoundBorder(border, margin));
		
		this.model = model;
		GraphPanelController controller = new GraphPanelController(model);
		
		this.addMouseWheelListener(controller);
		this.addMouseListener(controller);
		this.addMouseMotionListener(controller);

		// this.addMouseWheelListener(new MouseWheelListener() {
		// 	@Override
		// 	public void mouseWheelMoved(MouseWheelEvent e) {
		// 		double scale = e.getPreciseWheelRotation() * 0.05;
		// 		double diff = (GraphPanel.this.xMax - GraphPanel.this.xMin) * scale;
		// 		double xMinDiff = diff * (((e.getX()) - GraphPanel.this.xMin) / (GraphPanel.this.xMax - GraphPanel.this.xMin));
		// 		double xMaxDiff = diff * ((model.pixelToX(panelWidth - e.getX()) - GraphPanel.this.xMin) / (GraphPanel.this.xMax - GraphPanel.this.xMin));
		// 		double yMinDiff = diff * ((model.pixelToY(panelHeight - e.getY()) - GraphPanel.this.yMax) / (GraphPanel.this.yMax - GraphPanel.this.yMin));
		// 		double yMaxDiff = diff * ((model.pixelToY(e.getY()) - GraphPanel.this.yMax) / (GraphPanel.this.yMax - GraphPanel.this.yMin));
		// 		GraphPanel.this.xMin -= xMinDiff;
		// 		GraphPanel.this.xMax += xMaxDiff;
		// 		GraphPanel.this.yMin += yMinDiff;
		// 		GraphPanel.this.yMax -= yMaxDiff;

		// 		coordinateSystem = new CoordinateSystem(model);

		// 		tPool.execute(GraphPanel.this);
		// 	}
		// });

		// this.addMouseListener((MouseListener) new MouseAdapter() {			
		// 	public void mousePressed(MouseEvent e) { // gets the position of the mouse when clicked
		// 		mousePressingPoint = e.getPoint();
		// 	}
			
		// 	public void mouseEntered(MouseEvent e) {
		// 		GraphPanel.this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		// 	}
		// });

		// this.addMouseMotionListener(new MouseMotionAdapter() {
		// 	@Override
		// 	public void mouseDragged(MouseEvent e) { // calculates the new window position
		// 		int xDifference = mousePressingPoint.x - e.getX();
		// 		int yDifference = mousePressingPoint.y - e.getY();

		// 		double x = model.pixelToX(xDifference) - GraphPanel.this.xMin;
		// 		double y = model.pixelToY(yDifference) - GraphPanel.this.yMax;
		// 		GraphPanel.this.xMin += x;
		// 		GraphPanel.this.xMax += x;
		// 		GraphPanel.this.yMin += y;
		// 		GraphPanel.this.yMax += y;

		// 		mousePressingPoint = e.getPoint();

		// 		coordinateSystem = new CoordinateSystem(model);

		// 		tPool.execute(GraphPanel.this);
		// 	}
		// });
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

		panelHeight = height;
		panelWidth = width;

		coordinateSystem = new CoordinateSystem(model);
		model.addValueChangedListener(coordinateSystem);
		values = new int[10][panelWidth];
	}

	private void paintFunctions(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

		for (int i = 0; i < functions.length; i++) {
			g2d.setColor(colors[i]);
			if (functions[i] != null) {
				Path2D path = new Path2D.Double();

				path.moveTo(0, values[i][0]);
				for (int j = 1; j < panelWidth; j++) {
					path.lineTo(j, values[i][j]);
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