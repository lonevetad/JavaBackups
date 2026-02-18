package geometry.implementations;

import geometry.PointInt;

public class PointIntImpl implements PointInt {
	private static final long serialVersionUID = -10651051510561L;

	public PointIntImpl() { this(0, 0); }

	public PointIntImpl(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	protected int x, y;

	@Override
	public int getX() { return x; }

	@Override
	public int getY() { return y; }

	@Override
	public void setX(int x) { this.x = x; }

	@Override
	public void setY(int y) { this.y = y; }

	@Override
	public String toString() { return "PointInt [x=" + x + ", y=" + y + "]"; }
}