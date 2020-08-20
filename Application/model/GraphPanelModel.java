package model;

import service.Calculator;
import service.SuperModel;
import service.UnitLocation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GraphPanelModel extends SuperModel implements Runnable {

    public double xMin;
	public double xMax;
	public double yMin;
	public double yMax;

	public int panelWidth;
	public int panelHeight;
    
	private Calculator calculator = new Calculator();

	public String[] functions = new String[10];
	
	private Point mousePressingPoint;
	private int cursorType;

//	private int xAxisY;
	
	private ArrayList<UnitLocation> unitPoints = new ArrayList<UnitLocation>();
	private int[][] functionValues;

	private FontMetrics fontMetrics;

	

	// Threadpool with 4 Mainpoolsize and 8 Maxpoolsize
	ThreadPoolExecutor tPool = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4));
	
	public GraphPanelModel(double xMin, double xMax, double yMin, double yMax) {
		setXYUnits(xMin, xMax, yMin, yMax);
	}



	public void setFunctions(String[] functions) {
		if (functions.length <= 10) {
			this.functions = functions;

			tPool.execute(this);

		}
	}

	public int[][] evaluateFunctions(String[] functions) {
		int[][] values = new int[functions.length][panelWidth];
		double difference = (double) (xMax - xMin) / panelWidth;
		for (int i = 0; i < functions.length; i++) {
			if (functions[i] != null) {
				if (calculator.setTerm(functions[i])) {
					for (int j = 0; j < panelWidth; j++) {
						values[i][j] = (int) yToPixel(calculator.calculateValue(xMin + j * difference));
					}
				}
			}
		}
		return values;
	}

	public double pixelToX(int pixelX) { // calculates corresponding x-value of a pixel
		return xMin + pixelX * (xMax - xMin) / panelWidth;
	}

	public double pixelToY(int pixelY) { // calculates corresponding y-value of a pixel
		return yMax - pixelY * (yMax - yMin) / panelHeight; // (panelHeight - pixelY) / panelHeight * (yMax - yMin) +
															// yMax;

	}

	public int xToPixel(double x) { // calculates the x-position in the Coord-System
		return (int) ((x - xMin) / (xMax - xMin) * panelWidth);
	}

	public int yToPixel(double y) {
		return panelHeight - (int) ((y - yMin) / (yMax - yMin) * panelHeight); // evtl +yMax
	}

	@Override
	public void run() {
		synchronized (this) {
			functionValues = evaluateFunctions(functions);
		}
		
		ValueChanged();
	}
	
	public void setXYUnits(double xMin, double xMax, double yMin, double yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;

		if(fontMetrics != null) {
		calculateXUnits();
		calculateYUnits();
		}
		tPool.execute(this);
	}
	
	public void setxMin(double xMin) {
		this.xMin = xMin;
		
		if(fontMetrics != null) {
		calculateXUnits();
		}
		
		tPool.execute(this);
	}

	public void setxMax(double xMax) {
		this.xMax = xMax;
		
		if(fontMetrics != null) {
		calculateXUnits();
		}
		
		tPool.execute(this);
	}
	public void setyMin(double yMin) {
		this.yMin = yMin;
		if(fontMetrics != null) {
		calculateYUnits();
		}
		tPool.execute(this);
	}
	public void setyMax(double yMax) {
		this.yMax = yMax;
		if(fontMetrics != null) {
		calculateYUnits();
		}
		tPool.execute(this);
	}
	
	public void setMousePressingPoint(Point point) {
		mousePressingPoint = point;
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
		
		unitPoints = new ArrayList<UnitLocation>();
		
		tPool.execute(this);
	}

	public void calculateXYAfterWheel(MouseWheelEvent e) {
		double scale = e.getPreciseWheelRotation() * 0.05;
		double diff = (this.xMax - this.xMin) * scale;
		double xMinDiff = diff * ((pixelToX(e.getX()) - this.xMin) / (this.xMax - this.xMin));
		double xMaxDiff = diff * ((pixelToX(panelWidth - e.getX()) - this.xMin) / (this.xMax - this.xMin));
		double yMinDiff = diff * ((pixelToY(panelHeight - e.getY()) - this.yMax) / (this.yMax - this.yMin));
		double yMaxDiff = diff * ((pixelToY(e.getY()) - this.yMax) / (this.yMax - this.yMin));
		this.xMin -= xMinDiff;
		this.xMax += xMaxDiff;
		this.yMin += yMinDiff;
		this.yMax -= yMaxDiff;
		
		unitPoints = new ArrayList<UnitLocation>();

		tPool.execute(this);
	}





	private void calculateYUnits() {    //yNumbers
		int xneg = xToPixel(0) - 3; // horizontal Lines
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
						unitPoint.setValLocation(new Point(xToPixel(0) - lengthDifference - 12, y + fontMetrics.getDescent()));
					} else {
						unitPoint.setValLocation(new Point(getPanelWidth() - lengthDifference - 12,
								y + fontMetrics.getDescent()));
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


	private void calculateXUnits() {    //xNumbers
		int yneg = yToPixel(0) - 3; // vertical Lines
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
						unitPoint.setValLocation(new Point( x - middleDifference, getPanelHeight() - 10));
					} else {
						unitPoint.setValLocation(new Point( x - middleDifference, yToPixel(0) - 10));
					}
				} else {
					if (yToPixel(0) >= 0) {
						unitPoint.setValLocation(new Point( x - middleDifference, yToPixel(0) + 10 + heightDifference));
					} else {
						unitPoint.setValLocation(new Point( x - middleDifference, 10 + heightDifference));
					}
				}
				unitPoints.add(unitPoint);
			}
		}
	}	







	
	public int getCursorType() {
		return cursorType;
	}

	public void setCursorType(int cursorType) {
		this.cursorType = cursorType;
		ValueChanged();
	}

	public String[] getFunctions() {
		return functions;
	}

	public int[][] getFunctionValues() {
		return functionValues;
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

	public int getPanelHeight(){
		return panelHeight;
	}

//	public int getxAxisY() {
//		return xAxisY;
//	}
//
//	public void setxAxisY(int xAxisY) {
//		this.xAxisY = xAxisY;
//	}

	public double getYMax(){
		return yMax;
	}

	public double getYMin(){
		return yMin;
	}

	public double getXMax(){
		return xMax;
	}

	public double getXMin(){
		return xMin;
	}
	
	public ArrayList<UnitLocation> getUnitPoints() {
		return unitPoints;
	}

	public void setUnitPoints(ArrayList<UnitLocation> unitPoints) {
		this.unitPoints = unitPoints;
	}
	public FontMetrics getFontMetrics() {
		return fontMetrics;
	}

	public void setFontMetrics(FontMetrics fontMetrics) {
		this.fontMetrics = fontMetrics;
		
		calculateXUnits();
		calculateYUnits();
	}
}
