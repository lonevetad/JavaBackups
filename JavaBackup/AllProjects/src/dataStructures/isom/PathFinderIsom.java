package dataStructures.isom;

import java.awt.Point;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.isom.pathFinders.HeuristicEuclidean;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.implementations.shapes.subHierarchy.ShapeFillable;
import geometry.pointTools.impl.PointConsumerRestartable;
import tools.NumberManager;
import tools.PathFinder;

public interface PathFinderIsom<Distance extends Number> extends PathFinder<Point, ObjectLocated, Distance> {

	public NodeIsomProvider<Distance> getNodeIsomProvider();

	//

	// override from super interface

	@Override
	public default List<Point> getPath(Point start, Point dest, NumberManager<Distance> distanceManager,
			Predicate<ObjectLocated> isWalkableTester, boolean returnPathToClosestNodeIfNotFound) {
		return getPath(getNodeIsomProvider(), distanceManager, isWalkableTester, returnPathToClosestNodeIfNotFound,
				start, dest);
	}

	@Override
	public default List<Point> getPath(ObjectShaped objPlanningToMove, Point dest,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			boolean returnPathToClosestNodeIfNotFound) {
		return getPath(getNodeIsomProvider(), distanceManager, isWalkableTester, returnPathToClosestNodeIfNotFound,
				objPlanningToMove, dest);
	}

	//

	public default List<Point> getPath(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			boolean returnPathToClosestNodeIfNotFound, ObjectShaped objPlanningToMove, Point destination) {
		boolean prevFilled, isFillable;
		NodeIsom<Distance> start, dest;
		AbstractShape2D shape, sOnlyBorder;
		Point originalLocation;
		AbstractAdjacentForEacher<Distance> forAdjacents;
		List<Point> l;
		shape = objPlanningToMove.getShape();
		originalLocation = new Point(objPlanningToMove.getLocation());
		start = nodeProvider.getNodeAt(originalLocation);
		dest = nodeProvider.getNodeAt(destination);
		if (start == null || dest == null || dest == start)
			return null;
		// make the shape just a "border shape" to optimize the code
		isFillable = shape instanceof ShapeFillable;
		prevFilled = isFillable && ((ShapeFillable) shape).isFilled();
		sOnlyBorder = shape.toBorder();
		// perform the algorithm
		forAdjacents = newAdjacentConsumerForObjectShaped(nodeProvider, isWalkableTester, distanceManager, sOnlyBorder);
		l = getPathGeneralized(nodeProvider, distanceManager, isWalkableTester, forAdjacents,
				returnPathToClosestNodeIfNotFound, start, dest);
		/// restore previous state
		if (isFillable) { ((ShapeFillable) shape).setFilled(prevFilled); }
		shape.setCenter(originalLocation);
		return l;
	}

	public default List<Point> getPath(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			boolean returnPathToClosestNodeIfNotFound, Point startingPoint, Point destination) {
		NodeIsom<Distance> start, dest;
		AbstractAdjacentForEacher<Distance> forAdjacents;
		System.out.println("Path Finder Isom .. nodeProvider class: " + nodeProvider.getClass().getName());
		System.out.println(nodeProvider);
		System.out.println("start: " + startingPoint);
		System.out.println("end: " + destination);
		start = nodeProvider.getNodeAt(startingPoint);
		dest = nodeProvider.getNodeAt(destination);
		if (start == null || dest == null || dest == start)
			return null;
		forAdjacents = newAdjacentConsumer(nodeProvider, isWalkableTester, distanceManager);
		return getPathGeneralized(nodeProvider, distanceManager, isWalkableTester, forAdjacents,
				returnPathToClosestNodeIfNotFound, start, dest);
	}

	/**
	 * Extract the path, if any, from the second parameter (the end node) back to
	 * the first parameter (the start node). If no path is available
	 */
	public static <D extends Number> List<Point> extractPathFromEndOrClosest(NodeInfo<D> start, NodeInfo<D> end,
			boolean returnPathToClosestNodeIfNotFound, NodeInfo<D> closest) {
		if (end == null || end.father == null) {
			if (returnPathToClosestNodeIfNotFound) {
				end = closest;
				if (end == null || end.father == null) { // again? no closest found? -> ERROR
					return null;
				}
			}
		}
		return extractPath(start, end);
	}

	public static <D extends Number> List<Point> extractPath(NodeInfo<D> start, NodeInfo<D> end) {
		LinkedList<Point> l;
		if (end.father == null)
			return null;
		l = new LinkedList<>();
		while (end != start && end != null) {
			l.addFirst(end.thisNode.getLocationAbsolute());
//			System.out.println("extracting " + end.thisNode.getLocationAbsolute() + " with dist " + end.distFromStart);
			end = end.father;
		}
		l.addFirst(start.thisNode.getLocationAbsolute());
		return l;
	}

//

	/**
	 * The real implementation part of the path-finding that will be implemented. If
	 * it's not fitted to the algorithm, just let it blank and override
	 * {@link #getPath(NodeIsomProvider, NumberManager, Predicate, Point, Point)}.
	 */
	public abstract List<Point> getPathGeneralized(final NodeIsomProvider<Distance> m,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			AbstractAdjacentForEacher<Distance> forAdjacents, boolean returnPathToClosestNodeIfNotFound,
			NodeIsom<Distance> start, NodeIsom<Distance> dest);

//

	/** Helper method instancing a part of the point-based strategy. */
	public abstract AbstractAdjacentForEacher<Distance> newAdjacentConsumer(final NodeIsomProvider<Distance> m,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager);

	/**
	 * Helper method instancing a part of the {@link AbstractShape2D}-based
	 * strategy.
	 */
	public abstract AbstractAdjacentForEacher<Distance> newAdjacentConsumerForObjectShaped(
			final NodeIsomProvider<Distance> m, Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager, AbstractShape2D shape);

	@SuppressWarnings("unchecked")
	public default Distance distanceBetweenPoints_OLD1(Point pStart, Point pEnd,
			NumberManager<Distance> distanceManager) {
		double distance;
		var pt = distanceManager.getClass().getTypeParameters()[0];
		Class<?> dmClass = pt.getGenericDeclaration();
		distance = Math.hypot(pStart.x - pEnd.x, pStart.y - pEnd.y);
		if (Double.class.isAssignableFrom(dmClass)) {
			return (Distance) Double.valueOf(distance);
		} else if (Integer.class.isAssignableFrom(dmClass)) {
			return (Distance) Integer.valueOf((int) distance);
		} else {
			return distanceManager.fromDouble(distance);
		}
	}

	@SuppressWarnings("unchecked")
	public default Distance distanceBetweenPoints_OLD2(Point pStart, Point pEnd,
			NumberManager<Distance> distanceManager) {
		double distance;
		try {
			Method thisMethod = PathFinderIsom.class.getMethod("distanceBetweenPoints",
					new Class<?>[] { Point.class, Point.class, NumberManager.class });
			Parameter distManagerParam = thisMethod.getParameters()[2];
			ParameterizedType pt = null;
			Type typeParameter = distManagerParam.getParameterizedType();
			distance = Math.hypot(pStart.x - pEnd.x, pStart.y - pEnd.y);
			if (typeParameter instanceof ParameterizedType) {
				pt = (ParameterizedType) typeParameter;
				Type[] typeArguments = pt.getActualTypeArguments();
				Type thatDistanceType = typeArguments[0];
				System.out.println("EHEHEHE typeArguments: ");
				for (Type t : typeArguments) {
					System.out.println("\t t.getTypeName():" + t.getTypeName());
				}
				String distanceClassName = thatDistanceType.getTypeName();
				if (distanceClassName.equals(Double.class.getName())) {
					return (Distance) Double.valueOf(distance);
				} else if (distanceClassName.equals(Double.class.getName())) {
					return (Distance) Integer.valueOf((int) distance);
				} else {
					return distanceManager.fromDouble(distance);
				}
			} else
				return distanceManager.fromDouble(distance);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	public default Distance distanceBetweenPoints(NodeIsom<Distance> pStart, NodeIsom<Distance> pEnd,
			NumberManager<Distance> distanceManager) {
		return distanceBetweenPoints(pStart.getLocationAbsolute(), pEnd.getLocationAbsolute(), distanceManager);
	}

	public default Distance distanceBetweenPoints(Point pStart, Point pEnd, NumberManager<Distance> distanceManager) {
		return (new HeuristicEuclidean<>(distanceManager)).apply(pStart, pEnd);
	}

//

//

//

//

	public static class NodeInfo<D extends Number> {
		// Compiler Error: "cannot make a static reference to the non-static type
		// Distance" .. so I redefine it
		public D distFromStart, distFromFather;
		public NodeIsom<D> thisNode;
		public NodeInfo<D> father;

		public NodeInfo(NodeIsom<D> thisNode) {
			this.thisNode = thisNode;
			this.father = null;
			this.distFromStart = this.distFromFather = null;
		}
	}

	public static class DistanceKeyAlterator<NIF extends NodeInfo<D>, D extends Number> implements Consumer<NIF> {
		public D newDistFromStart;

		@Override
		public void accept(NIF nodd) { nodd.distFromStart = newDistFromStart; }
	}

//

	public static abstract class AbstractAdjacentForEacher<D extends Number> implements BiConsumer<NodeIsom<D>, D> {
		protected final Predicate<ObjectLocated> isWalkableTester;
		protected final NumberManager<D> distanceManager;
		protected NodeInfo<D> currentNode;

		public AbstractAdjacentForEacher(Predicate<ObjectLocated> isWalkableTester, NumberManager<D> distanceManager) {
			super();
			this.isWalkableTester = isWalkableTester;
			this.distanceManager = distanceManager;
		}

		public Predicate<ObjectLocated> getIsWalkableTester() { return isWalkableTester; }

		public NumberManager<D> getDistanceManager() { return distanceManager; }

		public NodeInfo<D> getCurrentNode() { return currentNode; }

		public void setCurrentNode(NodeInfo<D> currentNode) { this.currentNode = currentNode; }

		public boolean isAdjacentNodeWalkable(NodeIsom<D> adjacentNode) {
			return adjacentNode != null && adjacentNode.isWalkable(isWalkableTester);
		}
	}

//

	public static abstract class AbstractShapedAdjacentForEacher<D extends Number>
			extends AbstractAdjacentForEacher<D> {

		public AbstractShapedAdjacentForEacher(PathFinderIsom<D> pathFinderIsom, final NodeIsomProvider<D> m,
				AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester, NumberManager<D> distanceManager) {
			super(isWalkableTester, distanceManager);
			this.m = m;
			this.pathFinderIsom = pathFinderIsom;
			this.shape = shape;
			System.out.println("shape: " + shape);
			this.shapeWalkableChecker = new WholeShapeWalkableChecker<D>(m, isWalkableTester);
		}

		protected final NodeIsomProvider<D> m;
		protected final PathFinderIsom<D> pathFinderIsom;
		protected final AbstractShape2D shape;
		protected final WholeShapeWalkableChecker<D> shapeWalkableChecker;

		@Override
		public boolean isAdjacentNodeWalkable(NodeIsom<D> adjacentNode) {
			shapeWalkableChecker.restart();
//			shape.setCenter(adjacentNode.x, adjacentNode.y);
			shape.setCenter(adjacentNode.getLocationAbsolute());
//			pathFinderIsom.getSpaceToRunThrough()
//			System.out.println("shape isAdjacentNodeWalkable adjacent of " + adjacentNode.getLocationAbsolute());
			m.runOnShape(shape, shapeWalkableChecker);
			return shapeWalkableChecker.isWalkable();
		}
	}

	/** Utility class, for some reason (for example: "walk until I can get"). */
	public static class WholeShapeWalkableChecker<D extends Number>
			implements NodeIsomConsumer<D>, PointConsumerRestartable {
		private static final long serialVersionUID = -7895211411002L;
		protected boolean isWalkable;
//		protected InSpaceObjectsManager<D> isom;
		protected NodeIsomProvider<D> m;
		protected Predicate<ObjectLocated> isWalkableTester;

		public WholeShapeWalkableChecker(final NodeIsomProvider<D> m, Predicate<ObjectLocated> isWalkableTester) {
//			this.isom = isom;
			this.m = m;
			this.isWalkable = true;// assumption
			this.isWalkableTester = isWalkableTester;
		}

//		public InSpaceObjectsManager<D> getInSpaceObjectsManager() { return isom; }

		public boolean isWalkable() { return isWalkable; }

		public Predicate<ObjectLocated> getIsWalkableTester() { return isWalkableTester; }

//		public void setInSpaceObjectsManager(InSpaceObjectsManager<D> isom) { this.isom = isom; }

		public void setIsWalkableTester(Predicate<ObjectLocated> isWalkableTester) {
			this.isWalkableTester = isWalkableTester;
		}

		@Override
		public boolean canContinue() { return this.isWalkable; }

//		public void accept(Point t) { this.isWalkable &= this.isom.getNodeAt(t).isWalkable(isWalkableTester); }

		@Override
		public void restart() { this.isWalkable = true; }

		@Override
		public NodeIsomProvider<D> getNodeIsomProvider() { return m; }

		@Override
		public void consume(NodeIsom<D> n) {
//			System.out.println("\t ehh " + (n == null ? "null" : n.getLocationAbsolute()));
			this.isWalkable &= n != null && n.isWalkable(isWalkableTester);
		}

		@Override
		public void setNodeIsomProvider(NodeIsomProvider<D> nodeIsomProvider) {
//			if (nodeIsomProvider instanceof InSpaceObjectsManager<?>)
			this.m = nodeIsomProvider;
		}
	}
}