package dataStructures.isom.pathFinders;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.PathFinderIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.NumberManager;

public class PathFinderIsomBFS<Distance extends Number> extends PathFinderIsomBaseImpl<Distance> {

	public PathFinderIsomBFS(NodeIsomProvider<Distance> matrix) { super(matrix); }

	@Override
	public List<Point> getPathGeneralized(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			AbstractAdjacentForEacher<Distance> forAdjacents, boolean returnPathToClosestNodeIfNotFound,
			NodeIsom<Distance> start, NodeIsom<Distance> dest) {
		Map<NodeIsom<Distance>, NodeInfoFrontierBased<Distance>> nodes;
		Queue<NodeInfoFrontierBased<Distance>> queue;
		NodeInfoFrontierBased<Distance> ss, ee, niCurrent;
		// added for the boolean parameter
		NodeInfoFrontierBased<Distance> closestPointToDest = null;
		Distance lowestDistance = null;
		Point absoluteDestPoint;
		AAFE_BFS fa;
		//
		fa = null;
		queue = new LinkedList<>();
		nodes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				(n1, n2) -> NodeIsom.COMPARATOR_NODE_ISOM_POINT.compare(n1, n2));
		if (Shaped_AAFE_BFS.class.isAssignableFrom(forAdjacents.getClass())) {
			fa = ((Shaped_AAFE_BFS) forAdjacents).afed;
		} else if (AAFE_BFS.class.isAssignableFrom(forAdjacents.getClass())) { fa = (AAFE_BFS) forAdjacents; }
		fa.queue = queue;
		fa.nodes = nodes;
		ss = new NodeInfoFrontierBased<Distance>(start);
		ee = new NodeInfoFrontierBased<Distance>(dest);
		nodes.put(start, ss);
		nodes.put(dest, ee);
		ee.color = NodePositionInFrontier.NeverAdded;
		ee.father = null;
		queue.add(ss);
		ss.color = NodePositionInFrontier.InFrontier;
		absoluteDestPoint = dest.getLocationAbsolute();
		while (!queue.isEmpty()) {
			niCurrent = queue.poll();
			if (ee.father != null || niCurrent.thisNode == dest) {
				// found
				queue.clear();
				if (ee.father == null)
					ee = niCurrent;
			} else {
				// added for the boolean parameter
				if (returnPathToClosestNodeIfNotFound) {
					Distance newDistance;
					newDistance = distanceBetweenPoints(niCurrent.thisNode.getLocationAbsolute(), absoluteDestPoint,
							distanceManager);
					if (closestPointToDest == null || //
							distanceManager.getComparator().compare(newDistance, lowestDistance) < 0) {
						lowestDistance = newDistance;
						closestPointToDest = niCurrent;
					}
				}
				fa.setCurrentNode(niCurrent);
				// this.getNodeIsomProvider()
				nodeProvider.forEachAdjacents(niCurrent.thisNode, fa);
				niCurrent.color = NodePositionInFrontier.Closed;
			}
		}
		return PathFinderIsom.extractPathFromEndOrClosest(ss, ee, returnPathToClosestNodeIfNotFound,
				closestPointToDest);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumer(final NodeIsomProvider<Distance> nodeProvider,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
		return new AAFE_BFS(isWalkableTester, distanceManager);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumerForObjectShaped(
			final NodeIsomProvider<Distance> nodeProvider, Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager, AbstractShape2D shape) {
		return new Shaped_AAFE_BFS(this, nodeProvider, shape, isWalkableTester, distanceManager);
	}

	//

	protected class AAFE_BFS extends AbstractAdjacentForEacher<Distance> {
		protected Map<NodeIsom<Distance>, NodeInfoFrontierBased<Distance>> nodes;
		protected Queue<NodeInfoFrontierBased<Distance>> queue;

		public AAFE_BFS(Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
		}

		@Override
		public void accept(NodeIsom<Distance> adjacent, Distance distToAdjacent) {
			NodeInfoFrontierBased<Distance> niAdj;
//			Distance distStartToNeighbour;
//			Integer adjID;
			if (!isAdjacentNodeWalkable(adjacent))
				return;
//			adjID = adjacent.getID();
			niAdj = null;
//			distStartToNeighbour = this.distanceManager.getAdder().apply(distToAdjacent, currentNode.distFromStart);
			niAdj = nodes.get(adjacent);
//			if ( niAdj != null// nodes.containsKey(adjID)
//					&& niAdj.color != NodePositionInFrontier.NeverAdded)
//				return; // unable to handle this adjacent
			if (niAdj == null) {
				// not contained
				niAdj = new NodeInfoFrontierBased<Distance>(adjacent);
				niAdj.color = NodePositionInFrontier.NeverAdded;
			} // else: discard it, i has been yet used
			if (niAdj.color == NodePositionInFrontier.NeverAdded) {
				niAdj.color = NodePositionInFrontier.InFrontier;
				niAdj.father = currentNode;
//				niAdj.distFromFather = distToAdjacent;
//				niAdj.distFromStart = distStartToNeighbour;
				nodes.put(adjacent, niAdj);
				queue.add(niAdj);
			}
		}
	}

	protected class Shaped_AAFE_BFS extends ShapedAdjacentForEacherBaseImpl {

		protected final AAFE_BFS afed;

		protected Shaped_AAFE_BFS(PathFinderIsom<Distance> pathFinderIsom, NodeIsomProvider<Distance> m,
				AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(pathFinderIsom, m, shape, isWalkableTester, distanceManager);
			this.afed = new AAFE_BFS(isWalkableTester, distanceManager) {
				@Override
				public boolean isAdjacentNodeWalkable(NodeIsom<Distance> adjacentNode) { return ianw(adjacentNode); }
			};
		}

		@Override
		public void setCurrentNode(NodeInfo<Distance> currentNode) {
			super.setCurrentNode(currentNode);
			afed.setCurrentNode(currentNode);
		}

		protected boolean ianw(NodeIsom<Distance> adjacentNode) { return isAdjacentNodeWalkable(adjacentNode); }

		@Override
		public void accept(NodeIsom<Distance> adjacent, Distance distToAdj) { afed.accept(adjacent, distToAdj); }
	}
}