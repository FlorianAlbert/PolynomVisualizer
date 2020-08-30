package model;

import service.Calculator;
import service.SuperModel;
import service.UnitLocation;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GraphPanelModel extends SuperModel implements Runnable {

	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;

	private int panelWidth;
	private int panelHeight;
	
	private boolean valueSetByGraphPanel;

	private ArrayList<String> functions = new ArrayList<>();
	private Calculator calculator = new Calculator();

	private Point mousePressingPoint;
	private int cursorType;

	private ArrayList<UnitLocation> unitPoints = new ArrayList<UnitLocation>();
	private Path2D[] functionPaths = new Path2D[10];

	private FontMetrics fontMetrics;

	private Color[] colors = { Color.BLUE, Color.RED, Color.GREEN, Color.BLACK, Color.CYAN, Color.MAGENTA, Color.ORANGE,
			Color.GRAY, Color.PINK, Color.YELLOW };

	// Threadpool with 4 Mainpoolsize and 8 Maxpoolsize
	ThreadPoolExecutor tPool = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4));

	public GraphPanelModel(double xMin, double xMax, double yMin, double yMax) {
		setXYUnits(xMin, xMax, yMin, yMax);
	}

	public int getCursorType() {
		return cursorType;
	}

	public void setCursorType(int cursorType) {
		this.cursorType = cursorType;
		ValueChanged();
	}

	public void setMousePressingPoint(Point point) {
		mousePressingPoint = point;
	}

	public void addFunction(String function) {
		functions.add(function);
		tPool.execute(this);
	}

	public void removeFunction(int index) {
		functions.remove(index);
		tPool.execute(this);
	}

	public Path2D[] getFunctionPaths() {
		return functionPaths;
	}

	public int getPanelWidth() {
		return panelWidth;
	}

	public void setPanelWidth(int width) {
		this.panelWidth = width;
	}

	public void setPanelHeight(int height) {
		this.panelHeight = height;
	}

	public int getPanelHeight() {
		return panelHeight;
	}

	public double getYMax() {
		return yMax;
	}

	public void setyMax(double yMax, boolean setByGraphPanel) {
		this.valueSetByGraphPanel = setByGraphPanel;
		this.yMax = yMax;
		if (fontMetrics != null) {
			calculateYUnits();
		}
		tPool.execute(this);
	}

	public double getYMin() {
		return yMin;
	}

	public void setyMin(double yMin, boolean setByGraphPanel) {
		this.valueSetByGraphPanel = setByGraphPanel;
		this.yMin = yMin;
		if (fontMetrics != null) {
			calculateYUnits();
		}
		tPool.execute(this);
	}

	public double getXMax() {
		return xMax;
	}

	public void setxMax(double xMax, boolean setByGraphPanel) {
		this.valueSetByGraphPanel = setByGraphPanel;
		this.xMax = xMax;

		if (fontMetrics != null) {
			calculateXUnits();
		}

		tPool.execute(this);
	}

	public double getXMin() {
		return xMin;
	}

	public void setxMin(double xMin, boolean setByGraphPanel) {
		this.valueSetByGraphPanel = setByGraphPanel;
		this.xMin = xMin;

		if (fontMetrics != null) {
			calculateXUnits();
		}

		tPool.execute(this);
	}

	public void setXYUnits(double xMin, double xMax, double yMin, double yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;

		if (fontMetrics != null) {
			calculateXUnits();
			calculateYUnits();
		}
		tPool.execute(this);
	}

	public ArrayList<UnitLocation> getUnitPoints() {
		return unitPoints;
	}

	public FontMetrics getFontMetrics() {
		return fontMetrics;
	}

	public void setFontMetrics(FontMetrics fontMetrics) {
		this.fontMetrics = fontMetrics;

		unitPoints = new ArrayList<UnitLocation>();

		calculateXUnits();
		calculateYUnits();
	}

	public Color[] getColors() {
		return colors;
	}

	public boolean isValueSetByGraphPanel() {
		return valueSetByGraphPanel;
	}

	public Path2D[] evaluateFunctions(ArrayList<String> functions) {
		Path2D[] functionPathsIntern = new Path2D[10];
		double difference = (double) (xMax - xMin) / panelWidth;
		for (String function : functions) {
			if (function != null) {
				if (calculator.setTerm(function)) {
					functionPathsIntern[functions.indexOf(function)] = new Path2D.Double();
					functionPathsIntern[functions.indexOf(function)].moveTo(0,
							yToPixel(calculator.calculateValue(xMin)));
					for (int j = 1; j < panelWidth; j++) {
						functionPathsIntern[functions.indexOf(function)].lineTo(j,
								yToPixel(calculator.calculateValue(xMin + j * difference)));
					}
				}
			}
		}
		return functionPathsIntern;
	}

	public double pixelToX(int pixelX) {
		return xMin + pixelX * (xMax - xMin) / panelWidth;
	}

	public double pixelToY(int pixelY) {
		return yMax - pixelY * (yMax - yMin) / panelHeight;

	}

	public int xToPixel(double x) {
		return (int) ((x - xMin) / (xMax - xMin) * panelWidth);
	}

	public int yToPixel(double y) {
		return panelHeight - (int) ((y - yMin) / (yMax - yMin) * panelHeight);
	}

	public void calculateXYAfterMoving(Point newPoint) {
		int xDifference = mousePressingPoint.x - newPoint.x;
		int yDifference = mousePressingPoint.y - newPoint.y;

		double x = pixelToX(xDifference) - xMin;
		double y = pixelToY(yDifference) - yMax;
		xMin += x;
		xMax += x;
		yMin += y;
		yMax += y;

		mousePressingPoint = newPoint;
		
		valueSetByGraphPanel = true;

		tPool.execute(this);
	}

	public void calculateXYAfterWheel(MouseWheelEvent e) {
		double scale = e.getPreciseWheelRotation() * 0.05;
		double xDiff = (this.xMax - this.xMin) * scale;
		double yDiff = (this.yMax - this.yMin) * scale;
		double xMinDiff = xDiff * ((pixelToX(e.getX()) - this.xMin) / (this.xMax - this.xMin));
		double xMaxDiff = xDiff * ((pixelToX(panelWidth - e.getX()) - this.xMin) / (this.xMax - this.xMin));
		double yMinDiff = yDiff * ((pixelToY(panelHeight - e.getY()) - this.yMax) / (this.yMax - this.yMin));
		double yMaxDiff = yDiff * ((pixelToY(e.getY()) - this.yMax) / (this.yMax - this.yMin));
		this.xMin -= xMinDiff;
		this.xMax += xMaxDiff;
		this.yMin += yMinDiff;
		this.yMax -= yMaxDiff;
		
		valueSetByGraphPanel = true;

		tPool.execute(this);
	}

	private void calculateYUnits() {
		int xneg = xToPixel(0) - 3;
		int xpos = xToPixel(0) + 3;

		int help = (int) ((yMax - yMin) / 6 + 1);

		for (int i = (int) (Math.floor(yMin + 1)); i < yMax; i++) {

			if (i % help == 0 && i != 0) {

				UnitLocation unitPoint = new UnitLocation();
				unitPoint.setValue(i);

				int y = yToPixel(i);
				int lengthDifference = fontMetrics.stringWidth(Integer.toString(i));

				unitPoint.setNeg(new Point(xneg, y));
				unitPoint.setPos(new Point(xpos, y));

				if (xToPixel(0) - 2 * lengthDifference - 10 > 0) {
					if (xToPixel(0) <= getPanelWidth()) {
						unitPoint.setValLocation(
								new Point(xToPixel(0) - lengthDifference - 12, y + fontMetrics.getDescent()));
					} else {
						unitPoint.setValLocation(
								new Point(getPanelWidth() - lengthDifference - 12, y + fontMetrics.getDescent()));
					}
				} else {
					if (xToPixel(0) >= 0) {
						unitPoint.setValLocation(new Point(xToPixel(0) + 12, y + fontMetrics.getDescent()));
					} else {
						unitPoint.setValLocation(new Point(12, y + fontMetrics.getDescent()));
					}
				}
				unitPoints.add(unitPoint);
			}
		}
	}

	private void calculateXUnits() {
		int yneg = yToPixel(0) - 3;
		int ypos = yToPixel(0) + 3;

		int help = (int) ((yMax - yMin) / 6 + 1);

		for (int i = (int) (Math.floor(xMin) + 1); i < xMax; i++) {

			if (i % help == 0 && i != 0) {

				UnitLocation unitPoint = new UnitLocation();
				unitPoint.setValue(i);

				int x = xToPixel(i);
				int heightDifference = fontMetrics.getAscent() - fontMetrics.getDescent();
				int middleDifference = fontMetrics.stringWidth(Integer.toString(i)) / 2;

				unitPoint.setNeg(new Point(x, yneg));
				unitPoint.setPos(new Point(x, ypos));

				if (yToPixel(0) >= getPanelHeight() - heightDifference - 15) {
					if (yToPixel(0) >= getPanelHeight()) {
						unitPoint.setValLocation(new Point(x - middleDifference, getPanelHeight() - 10));
					} else {
						unitPoint.setValLocation(new Point(x - middleDifference, yToPixel(0) - 10));
					}
				} else {
					if (yToPixel(0) >= 0) {
						unitPoint.setValLocation(new Point(x - middleDifference, yToPixel(0) + 10 + heightDifference));
					} else {
						unitPoint.setValLocation(new Point(x - middleDifference, 10 + heightDifference));
					}
				}
				unitPoints.add(unitPoint);
			}
		}
	}

	@Override
	public void run() {
		synchronized (this) {
			functionPaths = evaluateFunctions(functions);
		}

		ValueChanged();
	}
}
