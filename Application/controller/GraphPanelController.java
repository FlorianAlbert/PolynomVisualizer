package controller;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import model.GraphPanelModel;

public class GraphPanelController implements MouseListener, MouseWheelListener, MouseMotionListener {

    GraphPanelModel model;
    
    public GraphPanelController(GraphPanelModel model) {
        this.model = model;
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		
		model.calculateXYAfterMoving(e.getPoint());
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		model.calculateXYAfterWheel(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		model.setMousePressingPoint(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		model.setCursorType(Cursor.MOVE_CURSOR);
	}
}