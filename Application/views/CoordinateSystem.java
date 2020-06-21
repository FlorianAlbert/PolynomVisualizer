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
				g2d.drawLine(width*i/(xMax - xMin), y-3, width*i/(xMax - xMin), y+3);
			}
		}
		
		if (xMax > 0 && xMin < 0) {
			int x = width * Math.abs(xMin) / (xMax - xMin);
			g2d.drawLine(x, 0, x, height);
			
			for (int i = 1; i < yMax - yMin; i++) {
				g2d.drawLine(x-3, height*i/(yMax - yMin), x+3, height*i/(yMax - yMin));
			}
		}
	}
}
