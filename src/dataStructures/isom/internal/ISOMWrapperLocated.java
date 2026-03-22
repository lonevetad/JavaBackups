package dataStructures.isom.internal;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.MultiISOMRetangularMap;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import games.generic.controlModel.objects.ObjectInSpace;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.PointInt;

/**
 * Wrapper of a {@link InSpaceObjectsManager} (its usual implementation is
 * {@link MatrixInSpaceObjectsManager} ) that specify where that matrix-map is
 * located in space: this class delegates the actual location tracking to the
 * provided {@link InSpaceObjectsManager} but manages everything, like the
 * top-left corner location, the angle of rotation and the coordinates offsets
 * while adding, removing and querying {@link ObjectInSpace} instances depending
 * of the said angle of rotation and the location offset .<br>
 *
 * <p>
 * OLD DOCUMENTATION
 * <p>
 * Mainly used for internal stuffs like:
 * <ul>
 * <li>{@link MultiISOMRetangularMap#addMap(MatrixInSpaceObjectsManager, Point)}</li>
 * <li>{@link NodeIsomProviderCachingMISOM}</li>
 * <li>Produced {@link NodeIsomProviderCachingMISOM#getMisomAt(Point)}</li>
 * </ul>
 * <p>
 * NOTE: as like as it's said in
 * {@link MultiISOMRetangularMap#getNodeAt(int, int)}, each matrix's coordinates
 * are relative to it, so a point like <code>{x:5, y:10}</code> of a map located
 * at <code>{x:1, y:3}</code> is translated (using a notation abuse) to
 * <code>{x:5, y:10} - {x:1, y:3} = {x:4, y:7}</code>, so the {@link NodeIsom}
 * located at <code>{x:4, y:7}</code> will be taken into account.
 */
public class ISOMWrapperLocated<Distance extends Number> implements ObjectLocated {
	private static final long serialVersionUID = 1L;
	/** In Degreed */
	protected int isomCacheWidth, isomCacheHeight;
	protected double angleRotationDegrees, sinCache, cosCache, sinInverseCache, cosInverseCache,
			/**
			 * The angle between the top left corner and the "right" section of the
			 * horizontal axes.
			 * <p>
			 * Top Left corner<br>
			 *
			 * <pre>
			 * |____________________________<br>
			 * |  \                        |<br>
			 * |     \                     |<br>
			 * |        \---,angle ; > 180�|<br>
			 * |-----------\--'------------|<br>
			 * |                           |<br>
			 * |                           |<br>
			 * |___________________________|<br>
			 * </pre>
			 */
			angleTopLeftCornerCache;
//	public final Long ID;
	protected final InSpaceObjectsManager<Distance> isomHeld;
	protected Entry<InSpaceObjectsManager<Distance>, PointInt> isomAndLocation;

	public ISOMWrapperLocated(InSpaceObjectsManager<Distance> isom) {
		super();
		this.isomHeld = isom;
//		this.ID = isom.getID(); // OLD VERSION: uidProvider.getNewID();
		// sinCache =
		sinInverseCache = 0;
		// cosCache =
		cosInverseCache = 1;
		isomAndLocation = new IsomAndTopLefCornerAbs<>(isom, isom.getTopLeftCorner());
		isomCacheWidth = isomCacheHeight = 0;
		this.resetCacheTopLeftAngleFromXAxis();
	}

	//

	public AbstractShape2D getShape() { return isomHeld.getShape(); }

	@Override
	public Long getID() { return this.isomHeld.getID(); }

	/**
	 * See {@link InSpaceObjectsManager#getLocation()}.
	 * <p>
	 * Inherited documentation:
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Point getLocation() { return isomHeld.getLocation(); }

	public InSpaceObjectsManager<Distance> getIsomHeld() { return isomHeld; }

	public double getSinCache() { return sinCache; }

	public double getCosCache() { return cosCache; }

	public double getAngleRotationDegrees() { return angleRotationDegrees; }

	public Entry<InSpaceObjectsManager<Distance>, PointInt> getIsomAndLocation() {
		Point isomLoc;
		isomLoc = this.isomHeld.getLocation(); // it is absolute and about center
		((IsomAndTopLefCornerAbs<Distance>) isomAndLocation).updateLocation(isomLoc.x, isomLoc.y);
		return isomAndLocation;
	}

	//

	@Override
	public boolean setID(Long newID) { return false; }

	public void setAngleRotationDegrees(double angleRotationDegrees) {
		double temp;
		this.angleRotationDegrees = angleRotationDegrees % 360.0;
		if (this.angleRotationDegrees < 0.0)
			this.angleRotationDegrees += 360.0;
		temp = Math.toRadians(this.angleRotationDegrees);
		temp = (sinInverseCache = -(sinCache = Math.sin(temp)));
//		cosInverseCache = cosCache = Math.cos(rad);
		cosInverseCache = cosCache = Math.sqrt(1.0 - (temp * temp));
	}

	/** See {@link #setLocationAbsolute(int, int)}. */
	public void setLocationAbsolute(Point locationAbsolute) {
		setLocation(locationAbsolute);
	}

	/** To be meant as absolute location of the centre. */
	public void setLocationAbsolute(int x, int y) {
		setLocation(x, y);
	}

	@Override
	public void setLocation(Point p) { this.setLocation(p.x, p.y); }

	@Override
	public void setLocation(int x, int y) {
		// convert center to top left
		isomHeld.setTopLeftCorner(x, y);
	}

	//

	/**
	 * Change the provided {@link Point}'s internal values equal to the relative
	 * top-left corner of the {@link InSpaceObjectsManager} held by this class.
	 */
	public Point makePointRelativeToTopLeftCorner(Point p) {
		PointInt location;
		location = this.isomHeld.getTopLeftCorner();
		p.x -= location.getX();
		p.y -= location.getY();
		return p;
	}

	/**
	 * Calls {@link #makePointRelativeToTopLeftCorner(Point)} providing a newly
	 * created {@link Point}. The given point is to be meant as <b>absolute</b>,
	 * i.e. NOT relative to this {@link InSpaceObjectsManager}.
	 */
	public Point makePointRelativeToTopLeftCorner(int x, int y) {
		return makePointRelativeToTopLeftCorner(new Point(x, y));
	}

	public Point makePointAbsoluteToTopLeftCorner(Point p) {
		PointInt location;
		location = this.isomHeld.getTopLeftCorner();
		p.x += location.getX();
		p.y += location.getY();
		return p;
	}

	/** See {@link #makePointRelativeToTopLeftCorner(int, int)}. */
	public Point makePointRelativeToCenter(Point p) {
		Point location;
//		location = this.isomHeld.getTopLetCorner();
//		p.x += (this.isomHeld.getWidth() >> 1) - location.x;
//		p.y += (this.isomHeld.getHeight() >> 1) - location.y;
//		return p;
		location = this.isomHeld.getLocation();
		p.x -= location.x;
		p.y -= location.y;
		return p;
	}

	public Point makePointAbsoluteToCenter(int x, int y) { return makePointAbsoluteToCenter(new Point(x, y)); }

	public Point makePointAbsoluteToCenter(Point p) {
		Point location;
//		location = this.isomHeld.getTopLetCorner();
//		p.x += location.x - (this.isomHeld.getWidth() >> 1);
//		p.y += location.y - (this.isomHeld.getHeight() >> 1);
		location = this.isomHeld.getLocation();
		p.x += location.x;
		p.y += location.y;
		return p;
	}

	/**
	 * The given {@link InSpaceObjectsManager} has a rotation in degrees (i.e.:
	 * {@link #getAngleRotationDegrees()}), so the <b>relative</b> given point is
	 * rotated relatively to the center so that it could be used as a
	 * <i>2D-index</i>.
	 */
	public Point applyIsomsRotation(Point p) {
		int x, y;
		x = p.x;
		y = p.y;
		p.x = (int) (x * cosInverseCache + y * sinInverseCache);
		p.y = (int) ((y * cosInverseCache) - (x * sinInverseCache));
		return p;
	}

	/**
	 * Calls {@link #makePointRelativeToTopLeftCorner(Point)} providing a newly
	 * created {@link Point}.
	 */
	public Point applyIsomsRotation(int x, int y) { return applyIsomsRotation(new Point(x, y)); }

	/**
	 * Apply both {@link #makePointRelativeToTopLeftCorner(Point)} and then
	 * {@link #applyIsomsRotation(Point)} to the given <b>absolute</b> point.
	 */
	public Point makeRelativeToCenterAndRotate(Point p) { return applyIsomsRotation(makePointRelativeToCenter(p)); }

	public Point makeRelativeAndRotate(int x, int y) { return makeRelativeToCenterAndRotate(new Point(x, y)); }

	/** Absolute coordinates. */
	public NodeIsom<Distance> getNodeAt(int x, int y) {
//		double xtemp, ytemp;
		Point location;
		if (this.angleRotationDegrees != 0.0) {
			// NOTE: actions in "makeRelativeAndRotate" are make here just to make them fast
			location = this.isomHeld.getLocation();
			// make coordinates relative to the centre
//			x += location.x - (this.isomHeld.getWidth() >> 1);
//			y += location.y - (this.isomHeld.getHeight() >> 1);
			x -= location.x;
			y -= location.y;
//		s = Math.sin(a); // a is in radians
//		c = Math.cos(a);
//		rotationMatrix = [
//			[c, -s],
//			[s, c]
//		];
			// [x,y] = dotProduct( [x,y], rotationMatrix);
			x = (int) (x * cosInverseCache + y * sinInverseCache);
			y = (int) ((y * cosInverseCache) - (x * sinInverseCache));
		}
		return isomHeld.getNodeAt(x, y);
	}

	public boolean add(ObjectLocated o) {
		boolean c;
		int xo, yo;// , x, y;
		Point oldLocation;
		if (o == null)
			return false;
		oldLocation = o.getLocation(); // it's the center of the object
		xo = oldLocation.x;
		yo = oldLocation.y;
//		o.setLocation(xo - misomLocation.x, yo - misomLocation.y);
		makeRelativeToCenterAndRotate(oldLocation);
		relativeFromCentreToLefttop(oldLocation);
		c = isomHeld.add(o);
		o.setLocation(xo, yo);
		return c;
	}

	public boolean contains(ObjectLocated o) {
		boolean c;
		int xo, yo;// , x, y;
		Point oldLocation;
		if (o == null)
			return false;
		oldLocation = o.getLocation();
		xo = oldLocation.x;
		yo = oldLocation.y;
//		o.setLocation(xo - misomLocation.x, yo - misomLocation.y);
		makeRelativeToCenterAndRotate(oldLocation);
		c = isomHeld.contains(o);
		o.setLocation(xo, yo);
		return c;
	}

	public boolean remove(ObjectLocated o) {
		boolean c;
		int xo, yo;// , x, y;
		Point oldLocation;
//		misomLocation = this.isomHeld.getLocation();
		if (o == null)
			return false;
		oldLocation = o.getLocation();
		xo = oldLocation.x;
		yo = oldLocation.y;
//		o.setLocation(xo - misomLocation.x, yo - misomLocation.y);
		makeRelativeToCenterAndRotate(oldLocation);
		c = isomHeld.remove(o);
		o.setLocation(xo, yo);
		return c;
	}

	public void forEachNode(BiConsumer<NodeIsom<Distance>, Point> action) {
		int hw, hh;
//		Point isomLocationCentre;
//		isomLocationCentre = this.isomHeld.getLocation();
		PointInt isomTopLeftCorner;
		isomTopLeftCorner = this.isomHeld.getTopLeftCorner();
		hw = (this.isomHeld.getWidth() >> 1);
		hh = (this.isomHeld.getHeight() >> 1);
//		System.out.println("\n\n\n for eaching NODE and location");
		this.isomHeld.forEachNode((n, p) -> {
			int xRotated, yRotated;
			// p is relative right now
			if (this.angleRotationDegrees == 0.0) {
				// must be made absolute ..
//				System.out.print("p was .. " + p);
				p.x += isomTopLeftCorner.getX();
				p.y += isomTopLeftCorner.getY();
//				System.out.println("and now p is: " + p);
			} else {
				// ..and rotated
				// 1) make it from relative to top-left to relative to center
				p.x -= hw;
				p.y -= hh;
				// 2) apply the rotation
				xRotated = (int) (p.x * cosCache + p.y * sinCache);
				yRotated = (int) ((p.y * cosCache) - (p.x * sinCache));
				p.x = xRotated;
				p.y = yRotated;
				// 3) make it back relative to top-left corner and 4) absolute
//				p.x += isomLocationCentre.x - hw;
//				p.y += isomLocationCentre.y - hh;
				p.x += hw + isomTopLeftCorner.getX();
				p.y += hh + isomTopLeftCorner.getY();
				// 5) DONE
			}
			action.accept(n, p);
		});
	}

	// protected methods

	protected void relativeFromCentreToLefttop(Point p) {
		p.x += this.isomHeld.getWidth() >> 1;
		p.y += this.isomHeld.getHeight() >> 1;
	}

	protected void resetCacheTopLeftAngleFromXAxis() {
		int w, h;
		Dimension isomDimension;
		isomDimension = this.isomHeld.getBoundingShape().getDimension();
		if (this.isomCacheWidth != isomDimension.width || this.isomCacheHeight != isomDimension.height) {
			w = this.isomCacheWidth = isomDimension.width;
			h = this.isomCacheHeight = isomDimension.height;
			if (h == 0) {
				this.angleTopLeftCornerCache = Math.PI / 2.0;

			} else if (w == 0) {
				this.angleTopLeftCornerCache = Math.PI;
			} else {
				this.angleTopLeftCornerCache = Math.atan(((double) w) / ((double) h));
			}
		}
	}

	//

	/**
	 * Pair of {@link InSpaceObjectsManager} and a {@link PointInt}.
	 */
	public static class IsomAndTopLefCornerAbs<D extends Number> implements Entry<InSpaceObjectsManager<D>, PointInt> {
		protected InSpaceObjectsManager<D> key;
		protected PointInt value;

		protected IsomAndTopLefCornerAbs(InSpaceObjectsManager<D> key, PointInt value) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		public InSpaceObjectsManager<D> getKey() { return key; }

		@Override
		public PointInt getValue() { return value; }

		/**
		 * Try to update the location, given a floating-point {@link Point2D}.
		 */
		protected boolean updateLocation(Point2D p) {
			if (p == null) { return false; }
			return this.updateLocation((int) p.getX(), (int) p.getY());
		}

		protected boolean updateLocation(Point p) {
			if (p == null) { return false; }
			return this.updateLocation(p.x, p.y);
		}

		protected boolean updateLocation(int x, int y) {
			boolean changed = false;
			if (x != this.value.getX()) {
				this.value.setX(x);
				changed = true;
			}
			if (y != this.value.getY()) {
				this.value.setY(y);
				changed = true;
			}
			return changed;
		}

		@Override
		public PointInt setValue(PointInt value) {
			PointInt oldValue;
			oldValue = this.value;
			this.value = value;
			return oldValue;
		}
	}
}
