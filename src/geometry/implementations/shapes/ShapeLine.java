package geometry.implementations.shapes;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.AbstractShapeImpl;
import geometry.implementations.shapes.subHierarchy.AbstractShapeRotated;
import geometry.pointTools.PolygonUtilities;
import tools.MathUtilities;

public class ShapeLine extends AbstractShapeImpl {
	private static final long serialVersionUID = -1545584051L;

	public ShapeLine() { super(ShapeRunnersImplemented.Line); }

	public ShapeLine(ShapeLine s) {
		super(s);
		this.length = s.length;
	}

	public ShapeLine(double angleRotation, int x, int y, int length) {
		super(ShapeRunnersImplemented.Line, angleRotation, x, y);
		this.length = 0;
		this.setLength(length);
	}

	public ShapeLine(Line2D l) { this(l.getP1(), l.getP2()); }

	public ShapeLine(Point2D p1, Point2D p2) {
		super(ShapeRunnersImplemented.Line);
		int x1, y1, x2, y2;
//		double slope;
		this.p1 = p1;
		this.p2 = p2;
		x1 = (int) Math.round(p1.getX());
		x2 = (int) Math.round(p2.getX());
		y1 = (int) Math.round(p1.getY());
		y2 = (int) Math.round(p2.getY());
		this.angleRotation = MathUtilities.angleDegrees(p1, p2);
		this.length = (int) Math.round(MathUtilities.distance(p1, p2));
		this.center.x = (int) Math.round((p1.getX() + p2.getX()) / 2.0);
		this.center.y = (int) Math.round((p1.getY() + p2.getY()) / 2.0);
//		slope=MathUtilities.slope(p1, p2);
		this.polygonCache = new Polygon(new int[] { x1, x2 }, new int[] { y1, y2 }, 2);
//		setDiameter(diameter)
	}

	protected int length;
	protected Point2D p1, p2;

	//

	public int getLength() { return length; }

	@Override
	public boolean isRegular() { return true; }

	@Override
	public int getDiameter() { return length; }

	@Override
	public int getRadius() { return length >> 1; }

	@Override
	public int getCornersAmounts() { return 2; }

	public Point2D getP1() {
		if (p1 == null || polygonCache == null) {
			polygonCache = null;
			recalculatePolygon();
		}
		return (Point2D) p1.clone();
	}

	public Point2D getP2() {
		if (p2 == null || polygonCache == null) {
			polygonCache = null;
			recalculatePolygon();
		}
		return (Point2D) p2.clone();
	}

	//

	@Override
	public AbstractShape2D setDiameter(int diameter) {
		if (diameter >= 0) {
			if (this.length != diameter) {
				this.length = diameter;
				this.polygonCache = null;
				// recalculation
				this.polygonCache = recalculatePolygon();
			}
		}
		return this;
	}

	public void setLength(int length) { setDiameter(length); }

	@Override
	public AbstractShape2D setCornersAmounts(int cornersAmounts) { return this; }

	@Override
	public AbstractShape2D setXCenter(int x) {
		int delta;
		delta = x - center.x;
		p1.setLocation(p1.getX() + delta, p1.getY());
		p2.setLocation(p2.getX() + delta, p2.getY());
		super.setXCenter(x);
		return this;
	}

	@Override
	public AbstractShape2D setYCenter(int y) {
		int delta;
		delta = y - center.y;
		p1.setLocation(p1.getX(), p1.getY() + delta);
		p2.setLocation(p2.getX(), p2.getY() + delta);
		super.setYCenter(y);
		return this;
	}

	@Override
	public AbstractShapeRotated setAngleRotation(double angleRotation) {
		double prev;
		prev = this.angleRotation;
		super.setAngleRotation(angleRotation);
		if (prev != this.angleRotation) {
			this.polygonCache = null;
			this.polygonCache = recalculatePolygon();
		}
		return this;
	}

	//

	@Override
	public AbstractShape2D toBorder() { return this; }

	@Override
	public boolean contains(int x, int y) {
		Polygon p;
		p = toPolygon();
		if (p == null)
			return false;
		return MathUtilities.areCollinear(p.xpoints[0], p.ypoints[0], x, y, p.xpoints[1], p.ypoints[1]);
	}

	@Override
	public Polygon toPolygon() {
		int[] xx, yy;
		xx = new int[] { (int) Math.round(p1.getX()), (int) Math.round(p2.getX()) };
		yy = new int[] { (int) Math.round(p1.getY()), (int) Math.round(p2.getY()) };
		return polygonCache = new Polygon(xx, yy, 2);
	}

	public Polygon recalculatePolygon() {
		double dx, dy, rad, halfLength;
		int[] xx, yy;
//		if (polygonCache != null)
//			return polygonCache;
		if (length == 0) {
			xx = new int[] { center.x, center.x };
			yy = new int[] { center.y, center.y };
		} else {
			halfLength = length / 2.0;
			rad = this.getAngleRotation();
			if (rad == 0.0 || rad == 180.0) {
				xx = new int[] { (int) (center.x - halfLength), (int) (center.x + halfLength) };
				yy = new int[] { (center.y), (center.y) };
			} else if (rad == 90.0 || rad == 270.0) {
				xx = new int[] { (center.x), (center.x) };
				yy = new int[] { (int) (center.y - halfLength), (int) (center.y + halfLength) };
			} else {
//			rad = Math.toRadians(rad / 2.0);
				rad = Math.toRadians(rad);
				dx = Math.cos(rad) * halfLength;
				dy = Math.sin(rad) * halfLength;
				halfLength = length / 4.0;
//			xx = new int[] { (int) (center.x - dx), (int) (center.x + dx) };
//			yy = new int[] { (int) (center.y - dy), (int) (center.y + dy) };
				if (dx < 0) {
					/*
					 * angle would range in (90;270), but angle's calculus would be wrong: swap
					 * points by swapping deltas' signs
					 */
					dx = -dx;
					dy = -dy;
				}
				xx = new int[] { (int) Math.round(center.x - dx), (int) Math.round(center.x + dx) };
				yy = new int[] { (int) Math.round(center.y - dy), (int) Math.round(center.y + dy) };
//			return polygonCache = super.toPolygon();
			}
		}
		if (this.p1 == null)
			this.p1 = new Point2D.Double(xx[0], yy[0]);
		else
			this.p1.setLocation(xx[0], yy[0]);
		if (this.p2 == null)
			this.p2 = new Point2D.Double(xx[1], yy[1]);
		else
			this.p2.setLocation(xx[1], yy[1]);
		return polygonCache = new Polygon(xx, yy, 2);
	}

	@Override
	public AbstractShape2D clone() { return new ShapeLine(this); }

	public Line2D toLine() {
//		Polygon p;
//		p = this.toPolygon();
//		if (p == null)
//			return null;
//		return new Line2D.Double(p.xpoints[0], p.ypoints[0], p.xpoints[1], p.ypoints[1]);
		return new Line2D.Double(p1, p2);
	}

	public static void main(String[] args) {
		ShapeLine l;

		l = new ShapeLine(new Point(100, 100), new Point(300, 200));
		System.out.println(l.getCenter());
		System.out.println(l.getAngleRotation());
		System.out.println(PolygonUtilities.polygonToString(l.toPolygon()));
		System.out.println(PolygonUtilities.polygonToString(l.recalculatePolygon()));
	}
}