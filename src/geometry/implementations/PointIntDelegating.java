package geometry.implementations;

import java.awt.geom.Point2D;

import geometry.PointInt;

public class PointIntDelegating implements PointInt {
	private static final long serialVersionUID = 2584123646716000L;

	public PointIntDelegating(Point2D backPoint) {
		super();
		this.backPoint = backPoint;
	}

	protected Point2D backPoint;

	@Override
	public int getX() { return (int) backPoint.getX(); }

	@Override
	public int getY() { return (int) backPoint.getY(); }

	@Override
	public void setX(int x) { backPoint.setLocation(x, backPoint.getY()); }

	@Override
	public void setY(int y) { backPoint.setLocation(backPoint.getX(), y); }

	@Override
	public String toString() { return "PointIntDel [x=" + getX() + ", y=" + getY() + "]"; }
}