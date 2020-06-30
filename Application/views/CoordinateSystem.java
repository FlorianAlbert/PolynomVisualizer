package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import service.Calculator;



public class CoordinateSystem extends JPanel implements MouseWheelListener,Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;

	private BufferedImage buffImg;
	private Graphics2D g2d;
	
	private double axisLength, axisHeight, centerX, centerY;
	
	private Point mousePt;
	
	private Calculator calculator = new Calculator();
	
	private String[] functions = new String[10];
	
	private Color[] colors = { Color.BLUE, Color.RED, Color.GREEN, Color.BLACK, Color.CYAN, Color.MAGENTA, Color.ORANGE,
			Color.GRAY, Color.PINK, Color.YELLOW };
	
	// Threadpool with 4 Mainpoolsize and 8 Maxpoolsize
	ThreadPoolExecutor tPool = new ThreadPoolExecutor(4, 8, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4));
	
	public CoordinateSystem() {
		
		setFocusable(true);														
		requestFocusInWindow();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		mouseMovement();
		
		buffImg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g2d = buffImg.createGraphics();
		
		axisHeight = 10.0; 							//length of the Y-Axis at the start
		axisLength = axisHeight * WIDTH / HEIGHT;	//length of the X-Axis based on the Y-Axis and the Screen Ratio
		
		centerX = 0.0;									//x-value of the center at the start
		centerY = 2.0;									//y-value of the center at the start
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 	//smoother looking lines
		g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));		//makes the Lines bigger

		drawCoordSystem();
		
		drawFunction();
		
		g.drawImage(buffImg, 0, 0, null);
	}
	
	@Override
	public void run() {						//repaints the image constantly
		boolean running = true;
		
		while (running) {
			repaint();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void mouseMovement() {
		addMouseWheelListener(this);
		this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {		//gets the position of the mouse when clicked
                mousePt = e.getPoint();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {		//calculates the new window position
                int xpos = e.getX() - mousePt.x;
                int ypos = e.getY() - mousePt.y;
                centerX -= xpos / (double)WIDTH * axisLength;
                centerY += ypos / (double)HEIGHT * axisHeight;
                mousePt = e.getPoint();
            }
        });
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {					//calculates the scale when scrolling
		double scale = Math.pow(1.15, e.getPreciseWheelRotation());
		double mxReal = pixelToX(e.getX());
		double myReal = pixelToY(e.getY());
		double sx = (centerX - mxReal) / axisLength;
		double sy = (centerY - myReal) / axisHeight;
		axisLength *= scale;
		axisHeight *= scale;
		centerX = mxReal + sx * axisLength;
		centerY = myReal + sy * axisHeight;
	}
	
	public void drawCoordSystem() {	
		
		int xAxisY = yToPixel(0.0);
		int yAxisX = xToPixel(0.0);
		
		int yneg = yToPixel(-0.15);					//vertical Lines
		int ypos = yToPixel(0.15);
		for (int i = WIDTH*-1; i < WIDTH; i++)
		{
			int x = xToPixel(i);
			g2d.setColor(Color.LIGHT_GRAY);				
			g2d.drawLine(x, 0, x, HEIGHT);
			g2d.setColor(Color.BLACK);	
			g2d.drawLine(x, yneg, x, ypos);
			g2d.drawString(""+i, x, ypos);
		}
		
		int xneg = xToPixel(-0.15);					//horizontal Lines
		int xpos = xToPixel(0.15);
		for (int i = HEIGHT*-1; i < HEIGHT; i++)
		{

			int y = yToPixel(i);
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.drawLine(0, y, WIDTH, y);
			g2d.setColor(Color.BLACK);	
			g2d.drawLine(xneg, y, xpos, y);
			g2d.drawString(""+i, xpos, y);
		}

		
		g2d.setColor(Color.BLACK);						//Y- and X-Axis
		g2d.drawLine(0, xAxisY, WIDTH, xAxisY);
		g2d.drawLine(yAxisX, 0, yAxisX, HEIGHT);
		g2d.drawString("x", 0, xAxisY - 10);
		g2d.drawString("y", yAxisX - 10, 0);
		
	}
	
	public void drawFunction() {
		
		for ( int i = 0; i < functions.length; i++) {
			if (functions[i] != null) {
			
				Path2D path = new Path2D.Double();
				path.moveTo(-10,-10);
				
				for (int x = 0; x < WIDTH; x++) {			//calculates the y-value for every x-pixel of a function
					if (calculator.setTerm(functions[i])) {
				
						double xx = pixelToX(x);
						double yy = yToPixel(calculator.calculateValue(xx));			
						
						double scaledX = x;
						double scaledY = yy;
						
						path.lineTo(scaledX, scaledY);
					}
				
				}
				g2d.setColor(colors[i]);
				g2d.draw(path);
			}
		}
	}
	
	public void setFunctions(String[] functions) {
		if (functions.length <= 10) {
			this.functions = functions;

			tPool.execute(this);

		}
	}
	
	private double bottom() {
		return centerY - axisHeight / 2.0;
	}
	
	private double right() {
		return centerX - axisLength / 2.0;
	}
	
	private double pixelToX(int pixelX) {							//calculates corresponding x-value of a pixel
		return pixelX / (double)WIDTH * axisLength + right();
	}
	
	private double pixelToY(int pixelY) {							//calculates corresponding y-value of a pixel
		return (HEIGHT - pixelY) / (double)HEIGHT * axisHeight + bottom();
	}
	
	private int xToPixel(double x) {							//calculates the x-position in the Coord-System
		return (int) ((x - right()) / axisLength * WIDTH);
	}
	
	private int yToPixel(double y) {							//calculates the y-position in the Coord-System
		return HEIGHT - (int) ((y - bottom()) / axisHeight * HEIGHT);
	}
	

}
