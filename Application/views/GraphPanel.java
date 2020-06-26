package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import service.Calculator;

/**
 * @author Florian Albert
 * 
 * @version 0.2
 * 
 */

public class GraphPanel extends JPanel implements Runnable {

	private static final long serialVersionUID = -4286522701957270175L;
	private int panelHeight;
	private int panelWidth;

	private CoordinateSystem coordinateSystem;

	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;

	private String[] functions = new String[10];
	private volatile int[][] values;

	private Color[] colors = { Color.BLUE, Color.RED, Color.GREEN, Color.BLACK, Color.CYAN, Color.MAGENTA, Color.ORANGE,
			Color.GRAY, Color.PINK, Color.YELLOW };

	private Calculator calculator = new Calculator();
	ThreadPoolExecutor tPool = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4));

	Thread calculatingThread;

	/**
	 * Create the panel
	 */
	public GraphPanel() {
		xMin = -10;
		xMax = 10;
		yMin = -10;
		yMax = 10;

		Border border = getBorder();
		Border margin = new LineBorder(Color.black, 1);
		setBorder(new CompoundBorder(border, margin));
	}

	public GraphPanel(int xMin, int xMax, int yMin, int yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;

		Border border = getBorder();
		Border margin = new LineBorder(Color.black, 1);
		setBorder(new CompoundBorder(border, margin));
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

		coordinateSystem = new CoordinateSystem(panelWidth, panelHeight, xMin, xMax, yMin, yMax);
		values = new int[10][panelWidth];
	}

	public void setFunctions(String[] functions) {
		if (functions.length <= 10) {
			this.functions = functions;

			tPool.execute(this);

		}
	}

	private synchronized void paintFunctions(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		for (int i = 0; i < functions.length; i++) {
			g2d.setColor(colors[i]);
			if (functions[i] != null) {
				for (int j = 0; j < panelWidth - 1; j++) {
					g2d.drawLine(j, panelHeight - values[i][j], j + 1, panelHeight - values[i][j + 1]);
				}
			}
		}
	}

	private void evaluateFunctions() {
		double difference = (double) (xMax - xMin) / panelWidth;
		for (int i = 0; i < functions.length; i++) {
			if (calculator.setTerm(functions[i])) {
				for (int j = 0; j < panelWidth; j++) {
					values[i][j] = (int) Math.round(
							panelHeight * (calculator.calculateValue(xMin + j * difference) - yMin) / (yMax - yMin));
				}
			}
		}
	}

	@Override
	public void run() {
		synchronized (this) {
			evaluateFunctions();
		}

		repaint();
	}

}
