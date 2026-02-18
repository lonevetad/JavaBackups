package geometry.implementations.shapes;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Arrays;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.ShapeFillableImpl;
import tools.MathUtilities;

public class ShapeTriangle extends ShapeFillableImpl {
	private static final long serialVersionUID = 1L;

	public ShapeTriangle(ShapeTriangle s) {
		super(s);
		Point2D p;
		this.vertexes = new Point2D[] { // clone it
				new Point2D.Double((p = s.vertexes[0]).getX(), p.getY()), //
				new Point2D.Double((p = s.vertexes[1]).getX(), p.getY()), //
				new Point2D.Double((p = s.vertexes[2]).getX(), p.getY()) //
		};
	}

	public ShapeTriangle(double angleRotation, boolean isFilled, Point2D[] vertexes) {
		super((isFilled ? ShapeRunnersImplemented.Triangle : ShapeRunnersImplemented.TriangleBorder), angleRotation, 0,
				0, isFilled);
		setVertexes(vertexes);
	}

	public ShapeTriangle(boolean isFilled) {
		super(isFilled ? ShapeRunnersImplemented.Triangle : ShapeRunnersImplemented.TriangleBorder);
		setVertexes(new Point2D[] { new Point2D.Double(0, 0), new Point2D.Double(0, 0), new Point2D.Double(0, 0) });
	}

	protected Point2D[] vertexes;

	//

	@Override
	public ShapeRunnersImplemented getShapeImplementing() {
		return isFilled ? ShapeRunnersImplemented.Triangle : ShapeRunnersImplemented.TriangleBorder;
	}

	/** The centroid, the center of gravity */
	protected void recalculateCenter() {
		this.center.x = (int) Math.round((vertexes[0].getX() + vertexes[1].getX() + vertexes[2].getX()) / 2.0);
		this.center.y = (int) Math.round((vertexes[0].getY() + vertexes[1].getY() + vertexes[2].getY()) / 2.0);
	}

	protected boolean hasNulls(Point2D[] vertexes) {
		for (Point2D p : vertexes)
			if (p == null)
				return true;
		return false;
	}

	public Point2D[] getVertexes() { return vertexes; }

	public ShapeTriangle setVertexes(Point2D[] vertexes) {
		if (vertexes == null || vertexes.length != 3 || hasNulls(vertexes))
			throw new IllegalArgumentException("Invalid vertexes set: " + Arrays.toString(vertexes));
		if (Arrays.deepEquals(this.vertexes, vertexes))
			return this;
		this.polygonCache = null;
		this.vertexes = vertexes;
		recalculateCenter();
		return this;
	}

	public double getArea() { /* return 0.5* due lati * sin(angolo compreso) */
		return MathUtilities.areaTriangle(vertexes[0], vertexes[1], vertexes[2]);
	}

	@Override
	public final int getCornersAmounts() { return 3; }

	@Override
	public AbstractShape2D setXCenter(int x) {
		int dx;
		Point2D p;
		dx = x - getXCenter();
		super.setXCenter(x);
		p = vertexes[0];
		p.setLocation((int) (p.getX() + dx), (int) p.getY());
		p = vertexes[1];
		p.setLocation((int) (p.getX() + dx), (int) p.getY());
		p = vertexes[2];
		p.setLocation((int) (p.getX() + dx), (int) p.getY());
		return this;
	}

	@Override
	public AbstractShape2D setYCenter(int y) {
		int dy;
		Point2D p;
		dy = y - getYCenter();
		super.setYCenter(y);
		p = vertexes[0];
		p.setLocation(p.getX(), (int) (p.getY() + dy));
		p = vertexes[1];
		p.setLocation(p.getX(), (int) (p.getY() + dy));
		p = vertexes[2];
		p.setLocation(p.getX(), (int) (p.getY() + dy));
		return this;
	}

	@Override
	public Polygon toPolygon() {
		int[] xx, yy;
		Point2D p;
		if (polygonCache != null)
			return polygonCache;
		xx = new int[3];
		yy = new int[3];
		xx[0] = (int) (p = vertexes[0]).getX();
		yy[0] = (int) p.getY();
		xx[1] = (int) (p = vertexes[1]).getX();
		yy[1] = (int) p.getY();
		xx[2] = (int) (p = vertexes[2]).getX();
		yy[2] = (int) p.getY();
		return polygonCache = new Polygon(xx, yy, 3);
	}

	@Override
	public boolean isRegular() {
		double d01, d12, d20;
		Point2D p0, p1, p2;
		d01 = (p0 = vertexes[0]).distance(p1 = vertexes[1]);
		d12 = p1.distance(p2 = vertexes[2]);
		d20 = p2.distance(p0);
		return d01 == d12 && d12 == d20 /* && for transitivity it's true: d20 == d01 */;
	}

	@Override
	public int getRadius() { return 0; }

	@Override
	public AbstractShape2D setRadius(int radius) { return this; }

	@Override
	public AbstractShape2D setDiameter(int radius) { return this; }

	@Override
	public AbstractShape2D setCornersAmounts(int cornersAmounts) { return this; }

	@Override
	public AbstractShape2D clone() { return new ShapeTriangle(this); }

}