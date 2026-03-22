package geometry;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;

import geometry.implementations.PointIntDelegating;

public interface PointInt extends Serializable {

	public int getX();

	public int getY();

	public default Point getLocation() { return toPoint(); }

	public void setX(int x);

	public void setY(int y);

	public default void setLocation(int x, int y) {
		setX(x);
		setY(y);
	}

	public default void setLocation(Point p) { setLocation(p.x, p.y); }

	public default Point toPoint() { return new Point(getX(), getY()); }

	public static PointInt fromPoint2D(Point2D p) {
//		return new PointIntImpl((int) p.getX(), (int) p.getY());
		return new PointIntDelegating(p);
	}
}