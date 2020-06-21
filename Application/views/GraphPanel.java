package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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
	
	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;
	
	private String[] functions = new String[10];
	
	private ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

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
	protected void paintComponent(Graphics g) {
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
	}
	
	public void setFunctions(String[] functions) {
		this.functions = functions;
		
		repaint();
	}
	
	private void paintFunctions(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		
		int[] values = new int[panelWidth];
		double difference = (double) (xMax - xMin) / panelWidth;
		for (String function: functions) {
			if (function != null) {
				for (int i = 0; i < panelWidth; i++) {
					try {
						values[i] = (int) Math.round(panelHeight * ((double) (engine.eval(function.replace("x", Double.toString(xMin + i * difference)))) - yMin) / (yMax - yMin));
					} catch (ScriptException e) {
						e.printStackTrace();
					}
				}
				
				for (int i = 0; i < panelWidth - 1; i++) {
					g2d.drawLine(i, panelHeight - values[i], i+1, panelHeight - values[i+1]);
				}
			}
		}
	}
}
