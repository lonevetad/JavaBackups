package geometry;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;

/** Detects if two {@link AbstractShape2D}s are intersecting each others. */
public interface ShapesIntersectionDetector extends Serializable {
	/**
	 * Detects if the given two {@link AbstractShape2D}s are intersecting each
	 * others.
	 */
	public default boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		List<Point2D> l;
		l = computeIntersectionPoints(s1, s2);
		return (l != null) && (!l.isEmpty());
	}

	/**
	 * Compute a set of intersections points, if they intersects, or
	 * <code>null</code> otherwise.<br>
	 * If there is a "complete" overlapping of at least one side (or the full curved
	 * line, like for two circles), then return simply the centre of that line (or
	 * circle).
	 */
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2);

}