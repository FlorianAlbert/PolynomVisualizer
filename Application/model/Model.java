package model;

import service.SuperModel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Model extends SuperModel {

    public double xMin;
	public double xMax;
	public double yMin;
	public double yMax;

	public int panelWidth;
	public int panelHeight;

	public ArrayList<Integer> xNumberList = new ArrayList<>();
	public ArrayList<Integer> yNumberList = new ArrayList<>();
    
    public int xToPixel(double x) { // calculates the x-position in the Coord-System
		return (int) ((x - xMin) / (xMax - xMin) * panelWidth);
	}

    public int yToPixel(double y) {
		return panelHeight - (int) ((y - yMin) / (yMax - yMin) * panelHeight);
	}

    private void drawXNumbers(Graphics2D g2d, int xAxisY) {
		int yneg = xAxisY + 3; // vertical Lines
		int ypos = xAxisY - 3;

		int help = (int) ((yMax - yMin) / 6 + 1);

		//int heightDifference = g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent();

		for (int i = (int) (Math.floor(xMin) + 1); i < xMax; i++) {

			if (i % help == 0 && i != 0) {
				int x = xToPixel(i);

				// int middleDifference = g2d.getFontMetrics().stringWidth(Integer.toString(i)) / 2;

				g2d.setColor(Color.LIGHT_GRAY);
				g2d.drawLine(x, 0, x, panelHeight);
				g2d.setColor(Color.BLACK);
				g2d.drawLine(x, yneg, x, ypos);
				
				xNumberList.add(i);
				
//				if (xAxisY >= panelHeight - heightDifference - 15) {
//					if (xAxisY >= panelHeight) {
//						g2d.drawString(Integer.toString(i), x - middleDifference, panelHeight - 10);
//						
//					} else {
//						g2d.drawString(Integer.toString(i), x - middleDifference, xAxisY - 10);
//					}
//				} else {
//					if (xAxisY >= 0) {
//						g2d.drawString(Integer.toString(i), x - middleDifference, xAxisY + 10 + heightDifference);
//					} else {
//						g2d.drawString(Integer.toString(i), x - middleDifference, 10 + heightDifference);
//					}
//				}
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
				//int lengthDifference = g2d.getFontMetrics().stringWidth(Integer.toString(i));

				g2d.setColor(Color.LIGHT_GRAY);
				g2d.drawLine(0, y, panelWidth, y);
				g2d.setColor(Color.BLACK);
				g2d.drawLine(xneg, y, xpos, y);

				yNumberList.add(i);

				// if (yAxisX - 2 * lengthDifference - 10 > 0) {
				// if (yAxisX <= panelWidth) {
				// 	g2d.drawString(Integer.toString(i), yAxisX - lengthDifference - 12,
				// 			y + g2d.getFontMetrics().getDescent());
				// 	} else {
				// 	g2d.drawString(Integer.toString(i), panelWidth - lengthDifference - 12,
				// 			y + g2d.getFontMetrics().getDescent());
				// 	}
				// } else {
				//  	if (yAxisX >= 0) {
				// 		g2d.drawString(Integer.toString(i), yAxisX + 12, y + g2d.getFontMetrics().getDescent());
				// } else {
				//  		g2d.drawString(Integer.toString(i), 12, y + g2d.getFontMetrics().getDescent());
				// 	}
				//  }
			}
		}
	}

}
