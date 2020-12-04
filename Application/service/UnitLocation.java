package service;

import java.awt.Point;

public class UnitLocation {

	private Point neg;
    private Point pos;
    private Point valLocation;
    private int value;

    public Point getNeg() {
		return neg;
	}

	public void setNeg(Point neg) {
		this.neg = neg;
	}

	public Point getPos() {
		return pos;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	public Point getValLocation() {
		return valLocation;
	}

	public void setValLocation(Point valLocation) {
		this.valLocation = valLocation;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
