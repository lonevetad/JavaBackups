package geometry;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;

import geometry.implementations.PointIntImpl;
import geometry.implementations.ShapeUIDProvider;
import geometry.pointTools.PolygonUtilities;
import tools.impl.OWIDLongImpl;

public abstract class AbstractShape2D extends OWIDLongImpl implements ObjectLocated, Serializable, Cloneable {
	public static final int MINIMUM_CORNERS_AMOUNT = 2; // a triangle
	/** Use negative values to move clockwise */
	protected static final double MINUS_360 = -360.0;
	private static final long serialVersionUID = -5641048035L;

	public AbstractShape2D(AbstractShape2D s) { this(s.shapeImplementing); }

	public AbstractShape2D(ShapeRunnersImplemented shapeImplementing) {
		super();
		if (shapeImplementing == null)
			throw new IllegalArgumentException("The shapeImplementing cannot be null");
		this.setID(ShapeUIDProvider.SHAPE_UNIQUE_ID_PROVIDER.getNewID());
		this.shapeImplementing = shapeImplementing;
	}

	protected final ShapeRunnersImplemented shapeImplementing;
	protected Polygon polygonCache;

	/**
	 * Returns a (possibly new) instance of this shape but defined just by its
	 * border, if possible. Returns <code>this</code> otherwise.
	 */
	public abstract AbstractShape2D toBorder();

	//

	public boolean contains(int x, int y) {
		Polygon p;
		p = toPolygon();
		return (p == null) ? false : PolygonUtilities.isInside(x, y, p);
		// p.contains(x, y);
	}

	public final boolean contains(Point2D p) { return contains((int) p.getX(), (int) p.getY()); }

	/** The x-component of {@link #getCenter()}. */
	public int getXCenter() { return getx(); }

	/** The y-component of {@link #getCenter()}. */
	public int getYCenter() { return gety(); }

	/**
	 * It's meant to be the centre of the mass, the centroid, not the bounding
	 * circle's centre. <br>
	 * See {@link #getBoundingBox()}.
	 */
	public abstract Point getCenter();// { return new Point(getXCenter(), getYCenter()); }

	@Override
	public int getx() { // faster and lighter implementation
		return getXCenter();
	}

	@Override
	public int gety() { // faster and lighter implementation
		return getYCenter();
	}

	/**
	 * Get the top-left corner of the bounding box, not of the shape. This must
	 * include the shape's rotation in the calculus( (i.e. if a rectangle is rotated
	 * in the center by 22°, then the point is a bit higher and a bit
	 * on-the-left).<br>
	 * See
	 * <p>
	 * NOTE: a "higher" y-coordinate is meant to have "lower" values, as the origin
	 * (0,0) lies at the left-top corner
	 * <p>
	 * NOTE: no cache is performed to calculate this point!
	 */
	public PointInt getTopLeftCorner() {
		// return getLeftTopCorner(getBoundingBoxCorners());
		return new PointIntImpl(getXTopLeft(), getYTopLeft());
	}

	/**
	 * Get the x-component of the of the {@link Point} returned by
	 * {@link #getTopLeftCorner()}.
	 */
	public int getXTopLeft() {
		// return (int) getLeftTopCorner().getX();
		return getXCenter() - (getWidth() >> 1);
	}

	/**
	 * Get the y-component of the of the {@link Point} returned by
	 * {@link #getTopLeftCorner()}.
	 */
	public int getYTopLeft() {
//		return (int) getLeftTopCorner().getY();
		return getYCenter() - (getHeight() >> 1);
	}

	public ShapeRunnersImplemented getShapeImplementing() { return shapeImplementing; }

	/**
	 * Returns the angle of the shape's rotation, expressed in degrees, relative to
	 * the center.
	 */
	public abstract double getAngleRotation();

	// for regular polugons

	public abstract boolean isRegular();

	/** Returns the diameter from the centre to the farthest point. */
	public int getDiameter() {
//throw new UnsupportedOperationException("Before defining the diameter, a unique definition of centre must be provided: curre");
		return getDiameter(getBoundingBox());
	}

	/**
	 * Returns the radius, got by dividing (and rounding down) the diameter returned
	 * by {@link #getDiameter()}.
	 */
	public int getRadius() { return getDiameter() >> 1; }

	/**
	 * Returns the amount of corners represented by this regular polygon. A value
	 * less than <code>2</code> ({@link MINIMUM_CORNERS_AMOUNT}) means <i>infinite
	 * or not defined</i>, like for circle or other curved shapes.<br>
	 * For instance, a line will return <code>2</code>, an equilateral triangle
	 * <code>3</code>, a square <code>4</code>, a pentagon <code>5</code>, an
	 * hexagon <code>6</code> and so on.
	 */
	public abstract int getCornersAmounts();

	/** Simply an alias for {@link #getCornersAmounts()}. */
	public final int getSidesAmounts() {
		return getCornersAmounts();
	}

	//

	public AbstractShape2D setXCenter(int x) {
		movePolygon(x - getXCenter(), getYCenter(), true, false);
		return this;
	}

	public AbstractShape2D setYCenter(int y) {
		movePolygon(getXCenter(), y - getYCenter(), false, true);
		return this;
	}

	public AbstractShape2D setCenter(Point p) { return p == null ? null : setCenter(p.x, p.y); }

	public AbstractShape2D setCenter(int x, int y) {
		int dx, dy;
		Polygon p;
		dx = x - getXCenter();
		dy = y - getYCenter();
		if (dx == 0 && dy == 0)
			return this;
		p = this.polygonCache;
		this.polygonCache = null;// force not to move the polygon
		setCenterImpl(dx, dy, x, y);
		this.polygonCache = p;
		movePolygon(dx, dy, true, true);
		return this;
	}

	public void setLeftTopCorner(int x, int y) { setCenter(x + (getWidth() >> 1), y + (getHeight() >> 1)); }

	public void setLeftTopCorner(Point p) { setLeftTopCorner(p.x, p.y); }

	@Override
	public void setLocation(Point location) { setCenter(location); }

	@Override
	public void setLocation(int x, int y) { setCenter(x, y); }

	/**
	 * Override-designed, but remember to call <code>super.setCenterImpl()</code> at
	 * the end.
	 */
	protected void setCenterImpl(int dx, int dy, int newx, int newy) {
		setXCenter(newx);
		setYCenter(newy);
	}

	public abstract AbstractShape2D setAngleRotation(double angleRotation);

	public AbstractShape2D setRadius(int radius) { return radius < 0 ? null : setDiameter(radius << 1); }

	public abstract AbstractShape2D setDiameter(int diameter);

	/** See {@link #getCornersAmounts()}. */
	public abstract AbstractShape2D setCornersAmounts(int cornersAmounts);

	/** Simply an alias for {@link #setCornersAmounts()}. */
	public final AbstractShape2D setSidesAmounts(int sidesAmounts) {
		return setCornersAmounts(sidesAmounts);
	}

	public Rectangle getBoundingBox() {
		Polygon p;
		p = toPolygon();
		return p == null ? null : p.getBounds();
	}

	/**
	 * Beware: it's a heavy and non-cached computation!<br>
	 * It's not cached because the method it uses tu build the rectangle,
	 * {@link #getBoundingBoxCorners()} is not cached.<br>
	 * (Cache needs to be maintained and updated, currently it's not kept). <br>
	 * NOTE: this {@link Rectangle}'s centre is probably NOT the one returned by
	 * {@link #getCenter()}.
	 */
	@Deprecated
	protected Rectangle getBoundingBox_OLD() {
		Point2D ltc, corners[];
		Dimension dim;
		corners = getBoundingBoxCorners();
		if (corners == null)
			return null;
		ltc = getLeftTopCorner(corners);
		if (ltc == null)
			return null;
		dim = getDimension(corners);
		if (dim == null)
			return null;
		return new Rectangle((int) ltc.getX(), (int) ltc.getY(), (int) dim.getWidth(), (int) dim.getHeight());
	}

	public int getWidth() { return (int) getDimension().getWidth(); }

	public int getHeight() { return (int) getDimension().getHeight(); }

	/**
	 * Returns all of four corners of the bounding, in clockwise order: top-left,
	 * top-right, bottom-right, bottom-left.
	 * <p>
	 * Note: no cache performed!
	 */
	public Point2D[] getBoundingBoxCorners() {
		int len, temp, lowerx, lowery, greaterx, greatery;
		int[] xp, yp;
		Polygon p;
		Point2D.Double topLeftVirtualCorner, bottomRightVirtualCorner;
		p = toPolygon();
		len = p.npoints - 1;
		xp = p.xpoints;
		yp = p.ypoints;
		// scan for points
		greaterx = lowerx = xp[len];
		greatery = lowery = yp[len];
		topLeftVirtualCorner = new Point2D.Double(lowerx, lowery);
		bottomRightVirtualCorner = new Point2D.Double(greaterx, greatery);
		while (--len >= 0) {
			if ((temp = xp[len]) < lowerx)
				topLeftVirtualCorner.x = lowerx = temp;
			if (temp > greaterx)
				bottomRightVirtualCorner.x = greaterx = temp;
			if ((temp = yp[len]) < lowery)
				topLeftVirtualCorner.y = lowery = temp;
			if (temp > greatery)
				bottomRightVirtualCorner.y = greatery = temp;
		}
		return new Point2D[] { //
				topLeftVirtualCorner//
				, new Point2D.Double(bottomRightVirtualCorner.x, topLeftVirtualCorner.y) //
				, bottomRightVirtualCorner //
				, new Point2D.Double(topLeftVirtualCorner.x, bottomRightVirtualCorner.y) //
		};
	}

	/** Returns a 2D-dimension, composed by the width and the heigth. */
	public Dimension getDimension() {
		return getDimension(getBoundingBoxCorners());
	}

	//

	// TODO OTHER PUBLIC METHODS

	public Polygon toPolygon() {
		int cornersAmount, xCenter, yCenter;
		double rad, angRotation, cornAmDouble, radius;
		int[] xx, yy;
		if (polygonCache != null)
			return polygonCache;
		if (!this.isRegular())
			return null;
		angRotation = this.getAngleRotation();
		cornAmDouble = cornersAmount = getCornersAmounts();
		if (cornersAmount < MINIMUM_CORNERS_AMOUNT)
			return null;
		radius = getDiameter() / 2.0; // diameter could be odd
		if (radius <= 0) {
			return polygonCache = new Polygon(new int[] { xCenter = getXCenter() },
					new int[] { yCenter = getYCenter() }, 1);
		}

		xCenter = this.getXCenter();
		yCenter = this.getYCenter();
		xx = new int[cornersAmount];
		yy = new int[cornersAmount];
		for (int indexCorner = 0; indexCorner < cornersAmount; indexCorner++) {
			rad = Math.toRadians(((indexCorner * MINUS_360) / cornAmDouble) + angRotation);
			xx[indexCorner] = (int) Math.round(//
					xCenter + radius * Math.cos(rad));
			yy[indexCorner] = (int) Math.round(//
					yCenter + radius * Math.sin(rad));
		}
		return polygonCache = new Polygon(xx, yy, cornersAmount);
	}

	@Override
	public abstract AbstractShape2D clone();

	//

	// TODO PROTECTED METHODS

	/** Test whether this shape can move */
	protected boolean shouldMove() { return true; }

	protected final void movePolygon(int dx, int dy, boolean hasx, boolean hasy) {
		// move the polygon
//		int len;
//		int[] xp, yp;
//		Polygon p;
		if (polygonCache == null) // do not recreate: it's required
			return;
		if (shouldMove()) {
			polygonCache.translate(dx, dy);
//			p = polygonCache;
//			len = p.npoints - 1;
//			xp = p.xpoints;
//			yp = p.ypoints;
//			if (hasx && hasy)
//				while (--len >= 0) {
//					xp[len] += dx;
//					yp[len] += dy;
//				}
//			else {
//				if (hasx)
//					while (--len >= 0)
//						xp[len] += dx;
//				else if (hasy)
//					while (--len >= 0)
//						yp[len] += dy;
//		}
		}

	}

	protected Point2D getLeftTopCorner(Point2D[] corners) { return corners[0]; }

	protected Dimension getDimension(Point2D[] corners) {
		Point2D topLeftVirtualCorner, bottomRightVirtualCorner;
		topLeftVirtualCorner = corners[0];
		bottomRightVirtualCorner = corners[2];
		return new Dimension((int) Math.abs(topLeftVirtualCorner.getX() - bottomRightVirtualCorner.getX()), //
				(int) Math.abs(topLeftVirtualCorner.getY() - bottomRightVirtualCorner.getY()));
	}

	protected int getDiameter(Rectangle r) { return getDiameter(r.getSize()); }

	protected int getDiameter(Dimension r) {
		// throw new UnsupportedOperationException("Before defining the diameter, a
		// unique definition of centre must be provided: curre");
		return (int) Math.hypot(r.getWidth(), r.getHeight());
	}
}