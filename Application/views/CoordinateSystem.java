package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class CoordinateSystem {
	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;

	private int width;
	private int height;

	public CoordinateSystem(int width, int height, int xMin, int xMax, int yMin, int yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;

		this.width = width;
		this.height = height;
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);

		if (yMax > 0 && yMin < 0) {
			int y = (height * yMax / (yMax - yMin));

			g2d.drawLine(0, y, width, y);

			for (int i = 1; i < xMax - xMin; i++) {
				int x = width * i / (xMax - xMin);
				
				drawXNumber(g, i, x, y, height);
			}
		}

		if (xMax > 0 && xMin < 0) {
			int x = width * Math.abs(xMin) / (xMax - xMin);
			g2d.drawLine(x, 0, x, height);

			for (int i = 1; i < yMax - yMin; i++) {
				g2d.drawLine(x - 3, height * i / (yMax - yMin), x + 3, height * i / (yMax - yMin));
			}
		}
	}

	public void drawXNumber(Graphics g, int counter, int x, int y, int height) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);

		g2d.drawLine(x, y - 3, x, y + 3);

		int middleDifference = g2d.getFontMetrics().stringWidth(Integer.toString(counter + xMin)) / 2;
		int heightDifference = g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent();

		int help = (xMax - xMin - 1) / 6 + 1;

		if (counter % help == 0 && counter + xMin != 0) {
			if (y >= height - heightDifference - 15) {
				g2d.drawString(Integer.toString(counter + xMin), x - middleDifference, y - 10);
			} else {
				g2d.drawString(Integer.toString(counter + xMin), x - middleDifference, y + 10 + heightDifference);
			}
		}
	}
}
