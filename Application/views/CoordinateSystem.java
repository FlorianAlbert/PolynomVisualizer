package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import model.Model;
import java.util.ArrayList;

public class CoordinateSystem {
	
	private Model model;


	public CoordinateSystem(int width, int height, double xMin, double xMax, double yMin, double yMax, Model m) {
		//  this.xMin = xMin;
		//  this.xMax = xMax;
		//  this.yMin = yMin;
		//  this.yMax = yMax;

		// this.panelWidth = width;
		// this.panelHeight = height;
		model = m;
	}

	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int xAxisY = model.yToPixel(0.0);
		int yAxisX = model.xToPixel(0.0);

		g2d.setColor(Color.BLACK); // Y- and X-Axis
		g2d.drawLine(0, xAxisY, model.panelWidth, xAxisY);
		g2d.drawLine(yAxisX, 0, yAxisX, model.panelHeight);

		//drawXNumbers(g2d, xAxisY);
		int heightDifference = g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent();
		
		for(int i = 0; i < model.xNumberList.size(); i++){
			int x = model.xToPixel(i);
			int middleDifference = g2d.getFontMetrics().stringWidth(Integer.toString(model.xNumberList.get(i))) / 2;
			if (xAxisY >= model.panelHeight - heightDifference - 15) {
				if (xAxisY >= model.panelHeight) {
					g2d.drawString(Integer.toString(model.xNumberList.get(i)), x - middleDifference, model.panelHeight - 10);	
				} else {
					g2d.drawString(Integer.toString(model.xNumberList.get(i)), x - middleDifference, xAxisY - 10);
				}
			} else {
				if (xAxisY >= 0) {
					g2d.drawString(Integer.toString(model.xNumberList.get(i)), x - middleDifference, xAxisY + 10 + heightDifference);
				} else {
				g2d.drawString(Integer.toString(model.xNumberList.get(i)), x - middleDifference, 10 + heightDifference);
				}
			}
		}

		for(int i = 0; i < model.yNumberList.size(); i++){
			int y = model.yToPixel(i);
			int lengthDifference = g2d.getFontMetrics().stringWidth(Integer.toString(model.yNumberList.get(i)));

			if (yAxisX - 2 * lengthDifference - 10 > 0) {
				if (yAxisX <= model.panelWidth) {
					g2d.drawString(Integer.toString(model.yNumberList.get(i)), yAxisX - lengthDifference - 12,
						y + g2d.getFontMetrics().getDescent());
				} else {
					g2d.drawString(Integer.toString(model.yNumberList.get(i)), model.panelWidth - lengthDifference - 12,
						y + g2d.getFontMetrics().getDescent());
				}
			} else {
				if (yAxisX >= 0) {
					g2d.drawString(Integer.toString(model.yNumberList.get(i)), yAxisX + 12, y + g2d.getFontMetrics().getDescent());
				} else {
					g2d.drawString(Integer.toString(model.yNumberList.get(i)), 12, y + g2d.getFontMetrics().getDescent());
				}
			}
		}
	}

	public void drawNumber(Graphics g, int number, int x, int y){
		g.drawString( Integer.toString(number), x, y);
	}
}
