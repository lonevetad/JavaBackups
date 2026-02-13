package geometry.implementations.shapes;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.ShapeFillableImpl;

public class ShapePolygon extends ShapeFillableImpl {
	private static final long serialVersionUID = 326178704L;

	public ShapePolygon(ShapePolygon s) {
		super(s);
		this.points = s.points;
	}

	public ShapePolygon(Point2D[] points, boolean isFilled) {
		super(isFilled ? ShapeRunnersImplemented.Polygon : ShapeRunnersImplemented.PolygonBorder, 0.0, 0, 0, isFilled);
		setPoints(points);
	}

	public ShapePolygon() { this(false); }

	public ShapePolygon(boolean isFilled) {
		super(isFilled ? ShapeRunnersImplemented.Polygon : ShapeRunnersImplemented.PolygonBorder);
	}

	protected Point2D[] points;

	public Point2D[] getPoints() { return points; }

	public ShapePolygon setPoints(Point2D[] points) {
		double meanx, meany, len;
		if (this.points != points) {
			this.polygonCache = null;
			this.points = points;
			if (points != null) {
				len = points.length;
				meanx = meany = 0.0;
				for (Point2D p : points) {
					meanx += p.getX() / len;
					meany += p.getY() / len;
				}
				this.center.x = (int) Math.round(meanx);
				this.center.y = (int) Math.round(meany);
				System.out.println(this.getCenter());
			}
		}
		return this;
	}

	@Override
	public boolean isRegular() {
		return false; // too lazy
	}

	@Override
	public int getCornersAmounts() { return 0; }

	@Override
	public AbstractShape2D setRadius(int radius) { return this; }

	@Override
	public AbstractShape2D setDiameter(int diameter) { return this; }

	@Override
	public AbstractShape2D setCornersAmounts(int cornersAmounts) { return this; }

	@Override
	public AbstractShape2D clone() { return new ShapePolygon(this); }

	//
	@Override
	protected void setCenterImpl(int dx, int dy, int x, int y) {
		if (this.points != null)
			for (Point2D p : this.points)
				p.setLocation(p.getX() + dx, p.getY() + dy);
		super.setCenterImpl(dx, dy, x, y);
	}

	@Override
	public Polygon toPolygon() {
		int len;
		int[] xx, yy;
		Point2D[] pp;
		Point2D p;
		if (this.polygonCache != null)
			return this.polygonCache;
		if ((pp = this.points) == null)
			return null;
		len = pp.length;
		xx = new int[len];
		yy = new int[len];
		while (--len >= 0) {
			p = pp[len];
			xx[len] = (int) p.getX();
			yy[len] = (int) p.getY();
		}
		return this.polygonCache = new Polygon(xx, yy, xx.length);
	}
}