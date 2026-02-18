package geometry;

import java.awt.Point;
import java.util.Comparator;
import java.util.function.Function;

import tools.Comparators;
import tools.ObjectWithID;

/**
 * Object that is located into some <i>space</i> concept and this is described
 * by {@link ObjectLocated#getLocation()}: as this method's documentation
 * explains, that point is the center.
 */
public interface ObjectLocated extends ObjectWithID {
	public static final Function<ObjectLocated, Long> KEY_EXTRACTOR = o -> o.getID();
	public static final Comparator<ObjectLocated> COMPARATOR = (ol1, ol2) -> {
		int c;
		Long id1, id2;
		if (ol1 == ol2)
			return 0;
		if (ol1 == null)
			return -1;
		if (ol2 == null)
			return 1;
		id1 = ol1.getID(); // from KEY_EXTRACTOR
		id2 = ol2.getID(); // from KEY_EXTRACTOR
		c = Comparators.LONG_COMPARATOR.compare(id1, id2);
		if (c != 0)
			return c;
		return Comparators.POINT_2D_COMPARATOR_LOWEST_LEFTMOST_FIRST.compare(ol1.getLocation(), ol2.getLocation());
	};

	/**
	 * As described by {@link #getLocation()}, it's the center's <code>x</code>
	 * coordinate value.
	 */
	public default int getx() { return getLocation().x; }

	/**
	 * As described by {@link #getLocation()}, it's the center's <code>y</code>
	 * coordinate value.
	 */
	public default int gety() { return getLocation().y; }

	/**
	 * The location is referred to be the center of this object.
	 * <p>
	 * {@inheritDoc}
	 */
	public Point getLocation();
//	public Point2D.Double getLocation();

	/** See {@link #getLocation()}. */
	public void setLocation(Point location);
//	public void setLocation(Point2D.Double location);

	/** See {@link #getLocation()}. */
	public void setLocation(int x, int y);

	//

	//

	public static class PointWrapper extends Point implements ObjectLocated {
		private static final long serialVersionUID = 84458516150L;
		protected static long idProg = 0;

		{
			this.ID = idProg++;
		}

		protected PointWrapper() { super(); }

		protected PointWrapper(int x, int y) { super(x, y); }

		protected PointWrapper(Point p) { super(p); }

		protected final Long ID;

		@Override
		public int getx() { return x; }

		@Override
		public int gety() { return y; }

		@Override
		public Long getID() { return this.ID; }

		@Override
		public boolean setID(Long newID) { return false; }
	}
}