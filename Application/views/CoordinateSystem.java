package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class CoordinateSystem {

	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;

	private int width;
	private int height;

	public CoordinateSystem(int width, int height, double xMin, double xMax, double yMin, double yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;

		this.width = width;
		this.height = height;
	}

	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int xAxisY = yToPixel(0.0);
		int yAxisX = xToPixel(0.0);

		drawXNumbers(g2d, xAxisY);

		drawYNumbers(g2d, yAxisX);

		g2d.setColor(Color.BLACK); // Y- and X-Axis
		g2d.drawLine(0, xAxisY, width, xAxisY);
		g2d.drawLine(yAxisX, 0, yAxisX, height);
		g2d.drawString("x", 0, xAxisY - 10);
		g2d.drawString("y", yAxisX - 10, 0);

	}

	private int xToPixel(double x) { // calculates the x-position in the Coord-System
		return (int) ((x - xMin) / (xMax - xMin) * width);
	}

	private int yToPixel(double y) {
		return height - (int) ((y - yMin) / (yMax - yMin) * height); // evtl +yMax
	}

	private void drawXNumbers(Graphics2D g2d, int xAxisY) {
		int yneg = xAxisY + 3; // vertical Lines
		int ypos = xAxisY - 3;

		int help = (int) ((yMax - yMin) / 6 + 1);
		
		int heightDifference = g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent();

		for (int i = (int) (Math.floor(xMin) + 1); i < xMax; i++) {

			if (i % help == 0 && i != 0) {
				int x = xToPixel(i);

				int middleDifference = g2d.getFontMetrics().stringWidth(Integer.toString(i)) / 2;

				g2d.setColor(Color.LIGHT_GRAY);
				g2d.drawLine(x, 0, x, height);
				g2d.setColor(Color.BLACK);
				g2d.drawLine(x, yneg, x, ypos);

				if (xAxisY >= height - heightDifference - 15) {
					g2d.drawString(Integer.toString(i), x - middleDifference, xAxisY - 10);
				} else {
					g2d.drawString(Integer.toString(i), x - middleDifference, xAxisY + 10 + heightDifference);
				}
			}
		}
	}

	private void drawYNumbers(Graphics2D g2d, int yAxisX) {
		int xneg = yAxisX - 3; // horizontal Lines
		int xpos = yAxisX + 3;

		int help = (int) ((yMax - yMin) / 6 + 1);

		for (int i = (int) (Math.floor(yMin) + 1); i < yMax; i++) {

			if (i % help == 0 && i != 0) {

				int y = yToPixel(i);
				int lengthDifference = g2d.getFontMetrics().stringWidth(Integer.toString(i));
				
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.drawLine(0, y, width, y);
				g2d.setColor(Color.BLACK);
				g2d.drawLine(xneg, y, xpos, y);
				
				if (yAxisX - 2 * lengthDifference - 10 > 0) {
					g2d.drawString(Integer.toString(i), yAxisX - lengthDifference - 12, y + g2d.getFontMetrics().getDescent());
				} else {
					g2d.drawString(Integer.toString(i), yAxisX + 12, y + g2d.getFontMetrics().getDescent());
				}
			}
		}
	}
}
