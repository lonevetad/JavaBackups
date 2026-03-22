package dataStructures.isom.pathFinders;

import java.awt.Point;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.PathFinderIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.NumberManager;

public class PathFinderIsomDijkstra<Distance extends Number> extends PathFinderIsomBaseImpl<Distance> {

	public PathFinderIsomDijkstra() { super(); }

	public PathFinderIsomDijkstra(NodeIsomProvider<Distance> nodeIsomProvider) { super(nodeIsomProvider); }

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumer(final NodeIsomProvider<Distance> nodeProvider,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
		return new UsynchronizedAdjacentForEacherDijkstra(isWalkableTester, distanceManager);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumerForObjectShaped(
			final NodeIsomProvider<Distance> nodeProvider, Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager, AbstractShape2D shape) {
		return new ShapedAdjacentForEacherDijkstra(this, nodeProvider, shape, isWalkableTester, distanceManager);
	}

	//

	// TODO

	@Override
	public List<Point> getPathGeneralized(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			AbstractAdjacentForEacher<Distance> forAdjacents, boolean returnPathToClosestNodeIfNotFound,
			NodeIsom<Distance> start, NodeIsom<Distance> dest) {
		// NodeIsomProvider m; // MatrixInSpaceObjectsManager<Distance> m;
		NodeInfoFrontierBased<Distance> ss, dd;
		final Map<NodeIsom<Distance>, NodeInfoFrontierBased<Distance>> nodeInfos;
		final PriorityQueueKey<NodeInfoFrontierBased<Distance>, Distance> frontier;
		final Comparator<Distance> comp;
//		AbstractAdjacentForEacherDijkstra fa;
		AbstractAdjacentForEacher<Distance> fa;
		// added for the boolean parameter
		NodeInfoFrontierBased<Distance> closestPointToDest = null;
		Distance lowestDistance = null;
		Point absoluteDestPoint;
		//
		comp = distanceManager.getComparator();
		fa = forAdjacents;
		//
		nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, //
				(n1, n2) -> NodeIsom.COMPARATOR_NODE_ISOM_POINT.compare(n1, n2));
		frontier = new PriorityQueueKey<>(COMPARATOR_NINFO_AS_POINT, comp, MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious,
				(NodeInfoFrontierBased<Distance> no) -> no.distFromStart);
		ss = new NodeInfoFrontierBased<Distance>(start);
		ss.father = ss;
		ss.distFromFather = ss.distFromStart = null;
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoFrontierBased<Distance>(dest);
		nodeInfos.put(dest, dd);
		absoluteDestPoint = dest.getLocationAbsolute();

		// set non-final parameters
		if (AbstractAdjacentForEacherDijkstra.class.isAssignableFrom(fa.getClass())) {
			AbstractAdjacentForEacherDijkstra ad;
			ad = (AbstractAdjacentForEacherDijkstra) fa;
			ad.nodeInfos = nodeInfos;
			ad.frontier = frontier;
		} else if (ShapedAdjacentForEacherDijkstra.class.isAssignableFrom(fa.getClass())) {
			ShapedAdjacentForEacherDijkstra ssssss;
			ssssss = (ShapedAdjacentForEacherDijkstra) fa;
			System.out.println("eheh");
			ssssss.afed.nodeInfos = nodeInfos;
			ssssss.afed.frontier = frontier;
		}

		while ((!frontier.isEmpty()) && dd.father == null) {
			final NodeInfoFrontierBased<Distance> n;
			n = frontier.removeMinimum().getKey();
			/*
			 * do not waste time computing nodes that have longer path of the alredy
			 * discovered ones
			 */
			if (dd.father == null ||
			// dd.distFromStart > n.distFromStart
					comp.compare(dd.distFromStart, n.distFromStart) > 0) {

//				if (this.getNodeIsomProvider().getNodeAt(n.thisNode.getLocationAbsolute()) == null) {
//					System.out.println("\t " + n.thisNode.getLocationAbsolute() + " is out of space !!");
////					throw new RuntimeException("\t " + n.thisNode.getLocationAbsolute() + " is out of space !!");
//					continue;
//				}

				// added for the boolean parameter
				if (returnPathToClosestNodeIfNotFound) {
					Distance newDistance;
					newDistance = distanceBetweenPoints(n.thisNode.getLocationAbsolute(), absoluteDestPoint,
							distanceManager);
					if (closestPointToDest == null || //
							distanceManager.getComparator().compare(newDistance, lowestDistance) < 0) {
						lowestDistance = newDistance;
						closestPointToDest = n;
					}
				}

				fa.setCurrentNode(n);
				nodeProvider.forEachAdjacents(n.thisNode, fa);
//				n.thisNode.forEachAdjacents(forAdjacents);
			} else
				/*
				 * destination has got a father and the nodes in the queue with MINIMUM
				 * "distance from start" has a distance greater than the destination itself ->
				 * iterating is useless -> exit: empty the frontier
				 */
				frontier.clear();
			n.color = NodePositionInFrontier.Closed;
		}
		nodeInfos.clear();
		return PathFinderIsom.extractPathFromEndOrClosest(ss, dd, returnPathToClosestNodeIfNotFound,
				closestPointToDest);
	}

	//

	// subclasses

	protected abstract class AbstractAdjacentForEacherDijkstra extends AbstractAdjacentForEacher<Distance> {

		protected Map<NodeIsom<Distance>, NodeInfoFrontierBased<Distance>> nodeInfos;
		protected PriorityQueueKey<NodeInfoFrontierBased<Distance>, Distance> frontier;
		protected final DistanceKeyAlterator<NodeInfoFrontierBased<Distance>, Distance> alterator;

		public AbstractAdjacentForEacherDijkstra(Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
			this.alterator = new DistanceKeyAlterator<>();
		}

		@Override
		public void accept(NodeIsom<Distance> adjacent, Distance distToAdjacent) {
			Distance distStartToNeighbour;
			NodeInfoFrontierBased<Distance> noInfoAdj;
			if (!isAdjacentNodeWalkable(adjacent))
				return;
			if (nodeInfos.containsKey(adjacent))
				noInfoAdj = nodeInfos.get(adjacent);
			else
				nodeInfos.put(adjacent, noInfoAdj = new NodeInfoFrontierBased<>(adjacent));

			Point adjAbsol = adjacent.getLocationAbsolute();
			Point currAbsol = currentNode.thisNode.getLocationAbsolute();
			if (Math.abs(adjAbsol.x - currAbsol.x) > 1 || Math.abs(adjAbsol.y - currAbsol.y) > 1)
				throw new RuntimeException("--- wrong delta: (dx:" + (adjAbsol.x - currAbsol.x) + ", dy:"
						+ (adjAbsol.y - currAbsol.y) + ")");

			if (noInfoAdj.color == NodePositionInFrontier.Closed)
				return;
			distStartToNeighbour = // distToAdj + currentNode.distFromStart;
					this.distanceManager.getAdder().apply(distToAdjacent, currentNode.distFromStart);
			if (noInfoAdj.father == null ||
			// distToNo < noInfo.distFromStart
					distanceManager.getComparator().compare(distStartToNeighbour, noInfoAdj.distFromStart) < 0) {
				// update
				noInfoAdj.father = currentNode;
				noInfoAdj.distFromFather = distToAdjacent; // Double.valueOf(distToAdj);
				if (noInfoAdj.color == NodePositionInFrontier.NeverAdded) {
					// add on queue
					noInfoAdj.color = NodePositionInFrontier.InFrontier;
					noInfoAdj.distFromStart = distStartToNeighbour;
					frontier.put(noInfoAdj);
				} else {
					// it's grey, it's actually in the queue
//					frontier.alterKey(noInfo, nodd -> (nodd).distFromStart = distToNo);
					alterator.newDistFromStart = distStartToNeighbour;
					frontier.alterKey(noInfoAdj, alterator);
				}
			}
		}
	}

	protected class UsynchronizedAdjacentForEacherDijkstra extends AbstractAdjacentForEacherDijkstra {
// nothing more to do here
		protected UsynchronizedAdjacentForEacherDijkstra(//
				Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
		}
	}

	protected class ShapedAdjacentForEacherDijkstra extends ShapedAdjacentForEacherBaseImpl {
		protected ShapedAdjacentForEacherDijkstra(PathFinderIsom<Distance> pathFinderIsom, NodeIsomProvider<Distance> m,
				AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(pathFinderIsom, m, shape, isWalkableTester, distanceManager);
			this.afed = new UsynchronizedAdjacentForEacherDijkstra(isWalkableTester, distanceManager) {
				@Override
				public boolean isAdjacentNodeWalkable(NodeIsom<Distance> adjacentNode) { return ianw(adjacentNode); }
			};
		}

		protected final AbstractAdjacentForEacherDijkstra afed;

		protected boolean ianw(NodeIsom<Distance> adjacentNode) { return isAdjacentNodeWalkable(adjacentNode); }

		@Override
		public void setCurrentNode(NodeInfo<Distance> currentNode) {
			super.setCurrentNode(currentNode);
			afed.setCurrentNode(currentNode);
		}

		@Override
		public void accept(NodeIsom<Distance> adjacent, Distance distToAdj) {
			if (adjacent == null)
				throw new RuntimeException("WHAT THE HELL");
			afed.accept(adjacent, distToAdj);
		}
	}

}