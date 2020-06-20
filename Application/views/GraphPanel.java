package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

/**
 * @author Florian Albert
 * 
 * @version 0.1
 * 
 */

public class GraphPanel extends JPanel {
	
	private static final long serialVersionUID = -4286522701957270175L;
	private int panelHeight;
	private int panelWidth;
	
	private CoordinateSystem coordinateSystem;
	
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;

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
	
	public GraphPanel(double xMin, double xMax, double yMin, double yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		
		Border border = getBorder();
		Border margin = new LineBorder(Color.black, 1);
		setBorder(new CompoundBorder(border, margin));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		coordinateSystem.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		
		panelHeight = height;
		panelWidth = width;
		
		coordinateSystem = new CoordinateSystem(panelWidth, panelHeight, xMin, xMax, yMin, yMax);
	}
}
