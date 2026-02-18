package gridObjectManager.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;

public interface PathOptimizer extends Serializable {

	public static interface CollinearCalculator<E> extends Serializable {
		public boolean areCollinear(E pfirst, E psecond, E pthird);
	}

	public static final CollinearCalculator<Point> ccPoint = PathOptimizer::areCollinear;
	public static final CollinearCalculator<NodeGOM> ccNodeGOM = PathOptimizer::areCollinear;

	//

	public <E> LinkedList<E> optimizePath(LinkedList<E> l, CollinearCalculator<E> cc);

	@SuppressWarnings("unchecked")
	public default <E> LinkedList<E> optimizePath(LinkedList<E> l) {
		E first;
		if (l == null)
			return null;
		if (l.size() <= 2)
			// base case: not enought element to be filtered
			// while (nfirst != null) { lr.add(nfirst.getItem()); }
			return l;
		first = l.getFirst();
		if (first instanceof Point)
			return (LinkedList<E>) optimizePath((l), ccPoint);
		if (first instanceof NodeGOM)
			return (LinkedList<E>) optimizePath((l), ccNodeGOM);
		return null;
	}

	//

	// static stuffs

	/**
	 * Compute if the three points P1(a,b), P2(m,n) and P3(x,y) are collinear, that
	 * means they belong to the same segment / line.
	 */
	public static boolean areCollinear(int x1, int y1, int x2, int y2, int x3, int y3) {
		return (x1 == x2 && x2 == x3) || //
				(y1 == y2 && y2 == y3) || //
				(y2 - y1) * (x3 - x2) == (y3 - y2) * (x2 - x1);
	}

	/**
	 * See {@link #areCollinear(int, int, int, int, int, int)}.<br>
	 * Effectively, it calls
	 * <code> areCollinear(pfirst.x, pfirst.y, psecond.x, psecond.y, pthird.x, pthird.y);</code>
	 *
	 * @param pfirst  the first point
	 * @param psecond the second point
	 * @param pthird  the second point
	 * @return {@link #areCollinear(int, int, int, int, int, int)}.
	 */
	public static boolean areCollinear(Point pfirst, Point psecond, Point pthird) {
		if (pfirst == null || psecond == null || pthird == null) {
			return false;
		}
		return areCollinear(pfirst.x, pfirst.y, psecond.x, psecond.y, pthird.x, pthird.y);
	}

	/**
	 * See {@link #areCollinear(int, int, int, int, int, int)}.<br>
	 * Effectively, it calls
	 * <code> areCollinear(pfirst.getX(), pfirst.getY(), psecond.getX(), psecond.getY(), pthird.getX(), pthird.getY());</code>
	 *
	 * @param pfirst  the first point
	 * @param psecond the second point
	 * @param pthird  the second point
	 * @return {@link #areCollinear(int, int, int, int, int, int)}.
	 */
	public static boolean areCollinear(NodeGOM pfirst, NodeGOM psecond, NodeGOM pthird) {
		if (pfirst == null || psecond == null || pthird == null) {
			return false;
		}
		return areCollinear(pfirst.getX(), pfirst.getY(), psecond.getX(), psecond.getY(), pthird.getX(), pthird.getY());
	}
}