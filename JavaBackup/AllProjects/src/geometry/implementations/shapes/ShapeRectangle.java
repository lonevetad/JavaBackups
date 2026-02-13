package geometry.implementations.shapes;

import java.awt.Polygon;
import java.awt.Rectangle;

import geometry.AbstractShape2D;
import geometry.PointInt;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.AbstractShapeRotated;
import geometry.implementations.shapes.subHierarchy.ShapeFillableImpl;
import tools.MathUtilities;

public class ShapeRectangle extends ShapeFillableImpl {
	private static final long serialVersionUID = 716984256106843689L;

	public ShapeRectangle() { this(true); }

	public ShapeRectangle(boolean isFilled) {
		super((isFilled ? ShapeRunnersImplemented.Rectangle : ShapeRunnersImplemented.RectangleBorder));
		this.width = 0;
		this.height = 0;
		this.diameterCache = 0;
		this.isFilled = isFilled;
	}

	public ShapeRectangle(ShapeRectangle s) {
		super(s);
		this.width = s.width;
		this.height = s.height;
		this.diameterCache = s.diameterCache;
	}

	public ShapeRectangle(boolean isFilled, int width, int height) { this(0, 0, 0, isFilled, width, height); }

	public ShapeRectangle(double angleRotation, int xCenter, int yCenter, boolean isFilled, int width, int height) {
		super((isFilled ? ShapeRunnersImplemented.Rectangle : ShapeRunnersImplemented.RectangleBorder), angleRotation,
				xCenter, yCenter, isFilled);
		this.width = width;
		this.height = height;
	}

	protected int width, height, diameterCache;

	//
	@Override
	public ShapeRunnersImplemented getShapeImplementing() {
		return isFilled ? ShapeRunnersImplemented.Rectangle : ShapeRunnersImplemented.RectangleBorder;
	}

	@Override
	public int getWidth() { return width; }

	@Override
	public int getHeight() { return height; }

	@Override
	public boolean isRegular() { return width == height; }

	@Override
	public int getDiameter() {
		if (isRegular())
			return width == 0 ? 0 : ((int) Math.round(width * MathUtilities.sqrtTwo));
		if (this.polygonCache == null)
			toPolygon(); // update cache
		return diameterCache;// (int) Math.round(Math.hypot(width, height));
	}

	@Override
	public int getRadius() {
		return getDiameter() >> 1;// (int) Math.round(Math.hypot(width, height) / 2.0);
	}

	@Override
	public final int getCornersAmounts() { return 4; }

	//

	public ShapeRectangle setWidth(int width) {
		if (width >= 0) {
			if (this.width != width)
				this.polygonCache = null;
			this.width = width;
		}
		return this;
	}

	public ShapeRectangle setHeight(int height) {
		if (height >= 0) {
			if (this.height != height)
				this.polygonCache = null;
			this.height = height;
		}
		return this;
	}

	public AbstractShape2D setRectangle(Rectangle r) { return setRectangle(r.x, r.y, r.width, r.height); }

	public AbstractShape2D setRectangle(int xLeftTop, int yLeftTop, int width, int height) {
		int xCOrig, yCOrig;
		Polygon p;
		if (width < 1 || height < 1)
			return null;
		this.width = width;
		this.height = height;
		xCOrig = this.center.x;
		yCOrig = this.center.y;
		this.center.x = xLeftTop + (width >> 1);
		this.center.y = yLeftTop + (height >> 1);
		p = this.polygonCache;
		this.polygonCache = null;// force not to move the polygon
		xCOrig = this.center.x - xCOrig; // recycle for deltas
		yCOrig = this.center.y - yCOrig; // recycle for deltas
//		setCenterImpl(xCOrig, yCOrig, this.center.x, this.center.y); // already done
		this.polygonCache = p;
		movePolygon(xCOrig, yCOrig, true, true);
		return this;
	}

	@Override
	public AbstractShape2D setCornersAmounts(int cornersAmounts) { return this; }

	@Override
	public AbstractShape2D setRadius(int radius) { return setDiameter(radius << 1); }

	@Override
	public AbstractShape2D setDiameter(int diameter) {
		double ratio, radius, angleRad;
		if (diameter != this.diameterCache) {
			if (diameter == 0) {
				this.diameterCache = width = height = 0;
			} else {
				if (height == 0)
					this.diameterCache = height = 0;
				else if (width == 0)
					this.diameterCache = width = 0;
				else {
					this.diameterCache = diameter;
					if (width == height)
						width = height = (int) Math.round((diameter / 2.0) * MathUtilities.sqrtTwo);
					else {
						// ratio = h/w = tan(alpha) -> alpha ? arctan(h/w)
						// -> h = prevRadius * sin( arctan(ratio)), w = similar
						ratio = height / width;
						angleRad = Math.atan(ratio);
						radius = diameter / 2.0;
						height = (int) Math.round(radius * Math.sin(angleRad));
						width = (int) Math.round(radius * Math.cos(angleRad));
					}
				}
			}
			this.polygonCache = null;
		}
		return this;
	}

	//

	@Override
	public boolean contains(int x, int y) {
		int xtl, ytl;
		PointInt topLeft;
		topLeft = getTopLeftCorner();
		xtl = topLeft.getX();
		ytl = topLeft.getY();
		if (angleRotation == 0.0 || angleRotation == 180.0) {
			return (xtl <= x && x <= (xtl + width))//
					&& (ytl <= y && y <= (ytl + height));
		} else if (angleRotation == 90.0 || angleRotation == 270.0) {
			xtl = getXCenter() - (height >> 1);
			ytl = getYCenter() - (width >> 1);
			return (xtl <= x && x <= (xtl + height))//
					&& (ytl <= y && y <= (ytl + width));
		} // else ..
		return super.contains(x, y);
	}

	@Override
	public Polygon toPolygon() {
		int counter;
		double tempx, tempy, rad, halfWidth, halfHeight, radius, angRotation;
		double[] deltasx, deltasy;
		int[] xx, yy;
		Polygon p;
		if (polygonCache != null)
			return polygonCache;

		angRotation = this.getAngleRotation();
		{
			int minusw, plusw, minush, plush;
			minusw = (width - 1) >> 1;
			minush = (height - 1) >> 1;
			plusw = minusw + ((width & 0x1) == 1 ? 0 : 1);
			plush = minush + ((height & 0x1) == 1 ? 0 : 1);
			p = null;
			if (angRotation == 0.0 | angRotation == 180.0)
				p = polygonCache = new Polygon(//
						new int[] { minusw = center.x - minusw, plusw += center.x, plusw, minusw }, //
						new int[] { minush = center.y - minush, minush, plush += center.y, plush }//
						, 4);
			else if (angRotation == 90.0 | angRotation == 270.0)
				p = polygonCache = new Polygon(//
						new int[] { minush = center.x - minush, plush += center.x, plush, minush }, //
						new int[] { minusw = center.y - minusw, minusw, plusw += center.y, plusw }//
						, 4);
			if (p != null) {
				diameterCache = (int) Math.round(Math.hypot(width, height));
				return p;
			}
		}
		radius = Math.hypot(halfWidth = width, halfHeight = height);// used as temp
		diameterCache = (int) Math.round(radius);
		radius /= 2.0;
		halfHeight /= 2.0;
		halfWidth /= 2.0;
		// for all corners: they are 4 -> 2 booleans iterating
		counter = -1;
		// use xx and yy as deltas, then as they are designed for
		xx = new int[4];
		yy = new int[4];
		deltasx = new double[] { -halfWidth, halfWidth, halfWidth, -halfWidth };
		deltasy = new double[] { -halfHeight, -halfHeight, halfHeight, halfHeight };
		while (++counter < 4) {
			tempx = (center.x + deltasx[counter]);
			tempy = (center.y + deltasy[counter]);
			rad = MathUtilities.angleDegrees(center.x, center.y, tempx, tempy) + angRotation;//
			if (rad < 0.0)
				rad += 360.0;
			else if (rad > 360.0)
				rad -= 360.0;
			rad = Math.toRadians(rad);
//			System.out.println("center.x: " + center.x + ", center.y: " + center.y + "\n\t tempx: " + tempx + ", tempy: "
//					+ tempy + ",\n\t ang between" + MathUtilities.angleDegrees(center.x, center.y, tempx, tempy));
			xx[counter] = (int) Math.round(//
					center.x + radius * Math.cos(rad));
			yy[counter] = (int) Math.round(//
					center.y + radius * Math.sin(rad));
		}
		return polygonCache = new Polygon(xx, yy, 4);
	}

	/**
	 * NOTE: again, no cache is performed, as like as for
	 * {@link #getTopLeftCorner()}.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Rectangle getBoundingBox() {
		PointInt ltc;
		ltc = getTopLeftCorner();
		if (ltc == null)
			return null;
		return new Rectangle(ltc.getX(), ltc.getY(), width, height);
	}

	@Override
	public AbstractShapeRotated setAngleRotation(double angleRotation) {
		super.setAngleRotation(angleRotation);
		this.polygonCache = null;
		return this;
	}

	@Override
	public AbstractShape2D clone() { return new ShapeRectangle(this); }

	//

	//

	@Deprecated
	public Polygon toPolygon_BROKEN() {
		boolean addingx, addingy;
		int counter;
		double tempx, tempy, rad, halfWidth, halfHeight, radius, angRotation;
		int[] xx, yy;
		Polygon p;

		if (polygonCache != null)
			return polygonCache;

		angRotation = this.getAngleRotation();
		{
			int minusw, plusw, minush, plush;
			minusw = width >> 1;
			minush = height >> 1;
			plusw = minusw + (width & 1);
			plush = minush + (height & 1);
			p = null;
			if (angRotation == 0.0 | angRotation == 180.0)
				p = polygonCache = new Polygon(//
						new int[] { minusw = center.x - minusw, plusw += center.x, plusw, minusw }, //
						new int[] { minush = center.y - minush, minush, plush += center.y, plush }//
						, 4);
			else if (angRotation == 90.0 | angRotation == 270.0)
				p = polygonCache = new Polygon(//
						new int[] { minush = center.x - minush, plush += center.x, plush, minush }, //
						new int[] { minusw = center.y - minusw, minusw, plusw += center.y, plusw }//
						, 4);
			if (p != null) {
				diameterCache = (int) Math.round(Math.hypot(width, height));
				return p;
			}
		}
		radius = Math.hypot(halfWidth = width, halfHeight = height);// used as temp
		diameterCache = (int) Math.round(radius);
		radius /= 2.0;
		halfHeight /= 2.0;
		halfWidth /= 2.0;
		// for all corners: they are 4 -> 2 booleans iterating
		counter = 0;
		xx = new int[4];
		yy = new int[4];
		addingy = false;
		do {
			addingx = false;
			tempy = addingy ? (center.y + halfHeight) : (center.y - halfHeight);
			do {
				tempx = addingx ? (center.x + halfWidth) : (center.x - halfWidth);

				rad = MathUtilities.angleDegrees(center.x, center.y, tempx, tempy) + angRotation;//
				if (rad < 0.0)
					rad += 360.0;
				else if (rad > 360.0)
					rad -= 360.0;
				rad = Math.toRadians(rad);
				System.out.println("center.x: " + center.x + ", center.y: " + center.y + "\n\t tempx: " + tempx
						+ ", tempy: " + tempy + ",\n\t ang between"
						+ MathUtilities.angleDegrees(center.x, center.y, tempx, tempy));
				xx[counter] = (int) Math.round(//
						center.x + radius * Math.cos(rad));
				yy[counter++] = (int) Math.round(//
						center.y + radius * Math.sin(rad));
			} while (addingx = !addingx);
		} while (addingy = !addingy);
		return polygonCache = new Polygon(xx, yy, 4);
	}
}