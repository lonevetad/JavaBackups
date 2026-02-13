package dataStructures.isom;

import java.awt.Point;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectLocated.PointWrapper;
import tools.Comparators;

public abstract class NodeIsom<D extends Number> extends PointWrapper implements Iterable<ObjectLocated>, Serializable {
	private static final long serialVersionUID = 4052487990441743L;
	public static final Comparator<NodeIsom<?>> COMPARATOR_NODE_ISOM_ID = (n1, n2) -> {
		if (n1 == n2)
			return 0;
		if (n1 == null)
			return -1;
		if (n2 == null)
			return 1;
		return Comparators.LONG_COMPARATOR.compare(n1.getID(), n2.getID());
	}, //
			COMPARATOR_NODE_ISOM_POINT = (p1, p2) -> {
				if (p1 == p2)
					return 0;
				if (p1 == null)
					return -1;
				if (p2 == null)
					return 1;
				return Comparators.POINT_2D_COMPARATOR_LOWEST_LEFTMOST_FIRST.compare(p1.getLocationAbsolute(),
						p2.getLocationAbsolute());
			};

	public NodeIsom(InSpaceObjectsManager<D> isom) { this(isom, 0, 0); }

	public NodeIsom(InSpaceObjectsManager<D> isom, int x, int y) {
		super(x, y);
		this.isomBelonging = isom;
	}

	protected InSpaceObjectsManager<D> isomBelonging;
//	protected Point isomLocation = null; // used to not create new instance every time

	/**
	 * Returns the location of this node in absolute coordinates: it's computed
	 * adding to the value returned by {@link #getLocation()} the coordinates of the
	 * {@link AbstractShape2D} that locates into space the belonging
	 * {@link InSpaceObjectsManager} (obtained through {@link #getIsomBelonging()}).
	 */
	public Point getLocationAbsolute() {
		Point relativeLocation, isomLocation;
		relativeLocation = this;
//		isomLocation = this.isomLocation;
		/*
		 * if (isomLocation == null) { this.isomLocation =
		 */
		isomLocation = (Point) isomBelonging.getBoundingShape().getTopLeftCorner();
		/*
		 * } else { AbstractShape2D s; s = isomBelonging.getBoundingShape();
		 * isomLocation.x = s.getXTopLeft(); isomLocation.y = s.getYTopLeft(); }
		 */
		// isomLocation it's a newly created point, so I can modify it
		isomLocation.x += relativeLocation.x;
		isomLocation.y += relativeLocation.y;
		return isomLocation;
	}

	/** See {@link #getLocationAbsolute()}. */
	public int getXAbsolute() { return getLocationAbsolute().x; }

	/** See {@link #getLocationAbsolute()}. */
	public int getYAbsolute() { return getLocationAbsolute().y; }

	public InSpaceObjectsManager<D> getIsomBelonging() { return isomBelonging; }

	/**
	 * This coordinates are meant as <i>relative to the containing
	 * {@link InSpaceObjectsManager}</i> (obtained through
	 * {@link #getIsomBelonging()}).
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Point getLocation() { return this; }

	public void setIsomBelonging(InSpaceObjectsManager<D> isomBelonging) { this.isomBelonging = isomBelonging; }

	/**
	 * Add the given object to this node. This node could store just a single object
	 * or a {@link Set }, it depends on implementation.
	 */
	public abstract boolean addObject(ObjectLocated o);

	public abstract int countObjectAdded();

	/** Return an object given an index */
	public abstract ObjectLocated getObject(int i);

	/** Return an object given an identifier */
	public abstract ObjectLocated getObject(Long ID);

	/** Return an object satisfying a filer (no order is guaranteed)-. */
	public abstract ObjectLocated getObject(Predicate<ObjectLocated> filter);

	public abstract boolean removeObject(Long ID);

	public abstract boolean removeObject(ObjectLocated o);

	public abstract boolean removeObject(Predicate<ObjectLocated> filter);

	public abstract boolean removeAllObjects();

	/**
	 * Optionally considering the zero or more {@link ObjectLocated} stored in this
	 * node, check if this node could be considered as a "walkable node" or just a
	 * "wall", depending on the given test checker (the parameter
	 * {@link Predicate}).<br>
	 * That predicate MUST return <code>true</code> if this node could be considered
	 * walkable.
	 */
	public abstract boolean isWalkable(Predicate<ObjectLocated> isWalkableTester);

	/** BEWARE of NULLS */
	public abstract void forEachHeldObject(Consumer<ObjectLocated> action);

	/**
	 * Perform an action to all held objects that passes a given filter (the first
	 * parameter).
	 */
	public void forEachAcceptableObject(Predicate<ObjectLocated> objFilter, Consumer<ObjectLocated> action) {
		this.forEachHeldObject(o -> {
			if (objFilter == null || objFilter.test(o))
				action.accept(o);
		});
	}

//	public void setLocation(Point location) {
//		this.x = location.x;
//		this.y = location.y;
//	}
//	public void setLocation(int x, int y) {
//		this.x = x;
//		this.y = y;
//	}
}