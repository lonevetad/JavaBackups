package gridObjectManager.core;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.NodeMatrix;

/* 15-8-2019: those compilations errors are left because it's nonsense to made a refactoring when a refactoring operation will be performed for the Git migration*/
@Deprecated
public interface AbstractPathFinder_OLD extends Serializable {

	//

	/**
	 * Simply calls
	 * {@link AbstractPathFinder_OLD#findPath(MatrixObjectLocationManager, int, int, ShapeSpecification, int, int)}
	 * passing <code>ShapeSpecification.getPointShapeSpec(xStart, yStart)</code> as
	 * additional parameter.
	 */
	public default List<Point> findPath(MatrixObjectLocationManager molm, int xStart, int yStart, int xDest,
			int yDest) {
		return findPath(molm, xStart, yStart, xDest, yDest, ShapeSpecification.newPoint(xStart, yStart));
	}

	/**
	 * The {@link ShapeSpecification} is used just to specify the form of the object
	 * that is looking for the shortest path. Its variables <code>x</code> and
	 * <code>y</code> will be ignored.<br>
	 * Note: if just the path is required without referring any kind of object, use
	 * its overload:
	 * {@link AbstractPathFinder_OLD#findPath(MatrixObjectLocationManager, int, int, int, int)}.
	 */
	public default List<Point> findPath(MatrixObjectLocationManager molm, int xStart, int yStart, int xDest, int yDest,
			ShapeSpecification ss) {
		if (molm == null || ss == null || xStart < 0 || yStart < 0 || xDest < 0 || yDest < 0 || xStart > molm.getWidth()
				|| yStart > molm.getHeight() || xDest > molm.getWidth() || yDest > molm.getHeight())
			return null;
		return findPath(molm, molm.getNodeMatrix(xStart, yStart), molm.getNodeMatrix(xDest, yDest), ss);
	}

	public default List<Point> findPath(MatrixObjectLocationManager molm, NodeMatrix nodeStart, NodeMatrix nodeDest) {
		if (molm == null || nodeStart == null || nodeDest == null)
			return null;
		return findPath(molm, nodeStart, nodeDest, ShapeSpecification.newPoint(nodeStart.getX(), nodeStart.getY()));
	}

	/**
	 * Get the shortest Path from one node to another.<br>
	 * Refer to
	 * {@link AbstractPathFinder_OLD#findPath(MatrixObjectLocationManager, int, int, ShapeSpecification, int, int)}
	 * for further informations.
	 */
	public default List<Point> findPath(MatrixObjectLocationManager molm, NodeMatrix nodeStart, NodeMatrix nodeDest,
			ShapeSpecification ss) {
		return findPath(molm, nodeStart, nodeDest, ss, true);
	}

	/**
	 * Refer to {@link AbstractPathFinder#findPath(MatrixObjectLocationManager,
	 * NodeMatrix, NodeMatrix, ShapeSpecification).
	 */
	public List<Point> findPath(MatrixObjectLocationManager molm, NodeMatrix nodeStart, NodeMatrix nodeDest,
			ShapeSpecification ss, boolean strechingPath);

	//

	public static AbstractPathFinder_OLD getOrDefault(AbstractPathFinder_OLD apf) {
		return apf != null ? apf : PathFinderIsom.getInstance();
	}

	//

	// TODO UTILS

	//

	/**
	 * As {@link #extractPath_NodeList(NodeMatrix, NodeMatrix, boolean)} passing
	 * true.
	 */
	@Deprecated
	public default LinkedList<NodeMatrix> extractPath_NodeList(NodeMatrix start, NodeMatrix dest) {
		return extractPath_NodeList(start, dest, true);
	}

	/**
	 * As
	 * {@link #extractPath_NodeList( LinkedList, NodeMatrix, NodeMatrix, boolean)}
	 * passing null as list.
	 */
	@Deprecated
	public default LinkedList<NodeMatrix> extractPath_NodeList(NodeMatrix start, NodeMatrix dest,
			boolean strechingPath) {
		return extractPath_NodeList(null, start, dest, strechingPath);
	}

	/**
	 * As
	 * {@link #extractPath_NodeList( LinkedList, NodeMatrix, NodeMatrix, boolean)}
	 * passing true.
	 */
	@Deprecated
	public default LinkedList<NodeMatrix> extractPath_NodeList(LinkedList<NodeMatrix> l, NodeMatrix start,
			NodeMatrix dest) {
		return extractPath_NodeList(l, start, dest, true);
	}

	/**
	 * Extract the path from the given destination and put it onto the given Linked
	 * List.<br>
	 * If the linked list is null, then a new list is created.
	 */
	@Deprecated
	public default LinkedList<NodeMatrix> extractPath_NodeList(LinkedList<NodeMatrix> l, NodeMatrix start,
			NodeMatrix dest, boolean strechingPath) {
		if (dest == null || dest.getPreviousOnPathfinding() == null || start == null) {
			// NO PATH FOUND
			l = null;
		} else {

			if (l == null) {
				l = new LinkedList<NodeMatrix>();
			} else {
				l.clear();
			}

			l.add(dest);
			while (((dest = dest.getPreviousOnPathfinding()) != null) && (dest != start)) {
				l.addFirst(dest);
			}
			if (start == dest) {
				l.add(start);
			}
			if (strechingPath)
				l = optimizePath(l, start);
		}
		return l;
	}

	//

	@Deprecated
	public default LinkedList<Point> extractPath_PointList(NodeMatrix start, NodeMatrix dest) {
		return extractPath_PointList(start, dest, true);
	}

	@Deprecated
	public default LinkedList<Point> extractPath_PointList(NodeMatrix start, NodeMatrix dest, boolean strechingPath) {
		return extractPath_PointList(null, start, dest, strechingPath);
	}

	@Deprecated
	public default LinkedList<Point> extractPath_PointList(LinkedList<Point> l, NodeMatrix start, NodeMatrix dest) {
		return extractPath_PointList(l, start, dest, true);
	}

	@Deprecated
	public default LinkedList<Point> extractPath_PointList(LinkedList<Point> l, NodeMatrix start, NodeMatrix dest,
			boolean strechingPath) {
		Point pstart;
		if (dest == null || dest.getPreviousOnPathfinding() == null || start == null) {
			// NO PATH FOUND
			System.out.println("NO PATH FOUND : d null? " + (dest == null) + ", dest.father null? "
					+ (dest.getPreviousOnPathfinding() == null));
			l = null;
		} else {

			if (l == null) {
				l = new LinkedList<Point>();
			} else {
				l.clear();
			}

			l.add(pstart = new Point(dest.getX(), dest.getY()));
			if (dest.getPreviousOnPathfinding() != null) {
				dest = dest.getPreviousOnPathfinding();
				do {
					l.addFirst(new Point(dest.getX(), dest.getY()));
				} while (((dest = dest.getPreviousOnPathfinding()) != null) && (dest != start));
				if (start == dest) {
					l.addFirst(new Point(dest.getX(), dest.getY()));
				}
			}
			if (strechingPath)
				l = optimizePath(l, pstart);
		}
		return l;
	}

}