package dataStructures.graph;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * See
 * {@link #computeIntersection(GraphSimple, Object, Object, Object, Object, IntersectionInstantiator)}
 * documentation.<br>
 * Used to split all of {@link GraphSimple}'s edges to create new nodes and
 * non-intersecting edges.
 */
public interface EdgesIntersectionDetector<E, D extends Number> {

	/**
	 * See
	 * {@link IntersectionInstantiator#computeValueOnIntersection(GraphSimple, Object, Object, Object, Object)}
	 * documentation.
	 */
	public default IntersectionResult<E> computeIntersection(GraphSimple<E, D> graphSource, E start1, E end1, E start2,
			E end2, IntersectionInstantiator<E, D> ii) {
		E e;
		e = ii.computeValueOnIntersection(graphSource, start1, end1, start2, end2);
		return new IntersectionResult<>(e, e != null);
	}

	/**
	 * Invoke
	 * {@link #computeIntersection(GraphSimple, Object, Object, Object, Object, IntersectionInstantiator)}
	 * and, if so, modify the given graph to add the intersection.<br>
	 * Returns true if the intersection happened.
	 *
	 * @parameter distanceCalculator a function computing the distance between two
	 *            nodes
	 */
	public default boolean performIntersectionSplit(GraphSimple<E, D> graphSource, E start1, E end1, E start2, E end2,
			IntersectionInstantiator<E, D> ii, DistanceCalculator<E, D> distanceCalculator) {
		IntersectionResult<E> inters;
		E x;
		inters = this.computeIntersection(graphSource, start1, end1, start2, end2, ii);
		if (inters != null && inters.isIntersecting()) {
			x = inters.valuesAtIntersection;
			if (Objects.equals(x, start1) || Objects.equals(x, end1) || Objects.equals(x, start2)
					|| Objects.equals(x, end2))
				return false;
			graphSource.removeLink(start1, end1);
			graphSource.removeLink(start2, end2);
			graphSource.addLink(start1, x, distanceCalculator.apply(start1, x));
			graphSource.addLink(x, end1, distanceCalculator.apply(x, end1));
			graphSource.addLink(start2, x, distanceCalculator.apply(start2, x));
			graphSource.addLink(x, end2, distanceCalculator.apply(x, end2));
			return true;
		}
		return false;
	}

	//

	//

	/**
	 * See
	 * {@link #computeValueOnIntersection(GraphSimple, Object, Object, Object, Object)}
	 * documentation.
	 */
	public static interface IntersectionInstantiator<T, Dd extends Number> {
		/**
		 * Assuming that:
		 * <ul>
		 * <li>the given graph exists</li>
		 * <li>the four given values are all different</li>
		 * <li>those values are belonging to the given graph</li>
		 * <li>the edge connecting the first value to the second exists, as well for the
		 * third and fourth</li>
		 * <li>and those edges are intersecting</li>
		 * </ul>
		 * then is returned a (possibly new) value that lies on the intersection and
		 * represents it.<br>
		 * If the values are 2D-points (like {@link Point2D}, then the new value is the
		 * point lying at the intersection of the two straight lines connecting the two
		 * edges.
		 *
		 * @parameter graphSource the {@link GraphSimple} where it's ASSUMED the four
		 *            values belongs to
		 * @parameter start1 the starting point of the first edge
		 * @parameter end1 the ending point of the first edge
		 * @parameter start2 the starting point of the second edge
		 * @parameter end2 the ending point of the second edge
		 * @return the value representing the intersection, if any, of the two given
		 *         edges
		 */
		public T computeValueOnIntersection(GraphSimple<T, Dd> graphSource, T start1, T end1, T start2, T end2);
	}

	public static class IntersectionResult<T> {
		final boolean isIntersecting;
		final T valuesAtIntersection;

		public IntersectionResult(T valuesAtIntersection, boolean isIntersecting) {
			this.isIntersecting = isIntersecting;
			this.valuesAtIntersection = valuesAtIntersection;
		}

		public boolean isIntersecting() { return isIntersecting; }

		public T getValuesAtIntersection() { return valuesAtIntersection; }
	}
}