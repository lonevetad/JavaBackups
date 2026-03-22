package dataStructures.graph.pathfind;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import dataStructures.graph.GraphSimple;
import dataStructures.graph.GraphSimpleAsynchronized;
import dataStructures.graph.GraphSimpleSynchronized;
import dataStructures.graph.PathFindStrategy;
import dataStructures.graph.PathGraph;
import dataStructures.isom.PathFinderIsomFrontierBased.NodePositionInFrontier;
import geometry.ObjectShaped;
import tools.NumberManager;

/**
 * Changes from DijkstraColor to NodePositionInFrontier:<ul>
 * <li>Black -> Closed</li>
 * <li>Grey -> InFrontier</li>
 * <li>White -> NeverAdded</li>
 * </ul>
 * */
/***/
public class PathFinderDijkstra<E, Distance extends Number> implements PathFindStrategy<E, Distance> {
	private static final long serialVersionUID = -26510959489410020L;

	public PathFinderDijkstra() {}

	//

	@Override
	public PathGraph<E, Distance> getPath(GraphSimple<E, Distance> graph, E start, E dest,
			NumberManager<Distance> distanceManager, Predicate<E> isWalkableTester) {
		if (graph == null)
			return null;
		return this.getPath(graph.isSynchronized(), graph, start, dest, distanceManager, isWalkableTester);
	}

	public PathGraph<E, Distance> getPath(boolean isSynchronized, GraphSimple<E, Distance> graph, E start, E dest,
			NumberManager<Distance> distanceManager, Predicate<E> isWalkableTester) {
		if (graph == null)
			return null;
		return isSynchronized ? this.getPathSynchronized(graph, start, dest, distanceManager, isWalkableTester)
				: this.getPathASynchronized(graph, start, dest, distanceManager, isWalkableTester);
	}

	@SuppressWarnings("unchecked")
	public PathGraph<E, Distance> getPathSynchronized(GraphSimple<E, Distance> ggg, E start, E dest,
			NumberManager<Distance> distanceManager, Predicate<E> isWalkableTester) {
		boolean walkableNull;
		final int dr;
		PathGraph<E, Distance> p;
		GraphSimpleSynchronized<E, Distance> graph;
		GraphSimpleSynchronized<E, Distance>.NodeGraphSimpleSynchronized s, d;
		PriorityQueueKey<GraphSimple<E, Distance>.NodeGraph, Distance> frontier;
		Comparator<Distance> comp;
		DistanceKeyAlterator<E, Distance> alterator;

//				BiConsumer<NodeGraph, Integer> forAdjacents;
		if (!(ggg instanceof GraphSimpleSynchronized<?, ?>))
			return null;

		walkableNull = isWalkableTester == null;
		if ((!walkableNull) && ((!isWalkableTester.test(start)) || (!isWalkableTester.test(dest)))) { return null; }

		comp = distanceManager.getComparator();
		graph = (GraphSimpleSynchronized<E, Distance>) ggg;
		//
		s = (GraphSimpleSynchronized<E, Distance>.NodeGraphSimpleSynchronized) graph.getNode(start);
		d = (GraphSimpleSynchronized<E, Distance>.NodeGraphSimpleSynchronized) graph.getNode(dest);
		if (s == null || d == null || d == s)
			return null;
		frontier = new PriorityQueueKey<>(graph.getCompNodeGraph(), comp,
				no -> ((GraphSimpleSynchronized<E, Distance>.NodeGraphSimpleSynchronized) no).getDistFromStart());
		alterator = new DistanceKeyAlterator<>();
		//
		dr = graph.getPathFindRuns();
		s.checkAndReset(dr);
		d.checkAndReset(dr);
		s.setFather(s);
		s.setDistFromFather(distanceManager.getZeroValue());
		s.setDistFromStart(distanceManager.getZeroValue());
		frontier.put(s);

//				forAdjacents =
		while (!frontier.isEmpty()) {
			final GraphSimpleSynchronized<E, Distance>.NodeGraphSimpleSynchronized n;
			n = (GraphSimpleSynchronized<E, Distance>.NodeGraphSimpleSynchronized) frontier.removeMinimum().getKey();
			/*
			 * do not waste time computing nodes that have longer path of the alredy
			 * discovered ones
			 */
			if (d.getFather() == null ||
			// d.getDistFromStart() > n.getDistFromStart()
					comp.compare(d.getDistFromStart(), n.getDistFromStart()) > 0)
				n.forEachAdjacents((nod, distToAdj) -> {
					Distance distToNo;
					GraphSimpleSynchronized<E, Distance>.NodeGraphSimpleSynchronized no;

					if (walkableNull || isWalkableTester.test(nod.getElem())) {
						no = (GraphSimpleSynchronized<E, Distance>.NodeGraphSimpleSynchronized) nod;
						no.checkAndReset(dr);
						if (no.getColor() == NodePositionInFrontier.Closed)
							return;
						distToNo = // distToAdj + n.getDistFromStart();
								distanceManager.getAdder().apply(distToAdj, n.getDistFromStart());
						if (no.getFather() == null || // distToNo < no.getDistFromStart()
						comp.compare(distToNo, no.getDistFromStart()) > 0) {
//						final Integer newDistance;
							// update
//						newDistance = Integer.valueOf(distToNo);
							no.setFather(n);
							no.setDistFromFather(distToAdj);
							if (no.getColor() == NodePositionInFrontier.NeverAdded) {
								// add on queue
								no.setColor(NodePositionInFrontier.InFrontier);
								no.setDistFromStart(distToNo);
								frontier.put(no);
							} else {
								// it's grey, it's actually in the queue
								alterator.distToNo = distToNo;
								frontier.alterKey(no, alterator);
							}
						}
					}
				});
			else
				/*
				 * destination has got a father and the node with MINIMUM "distance from start"
				 * has a distance greater than the destination itself -> iterating is useless ->
				 * empty the frontier
				 */
				frontier.clear();
			n.setColor(NodePositionInFrontier.Closed);
		}
		graph.setPathFindRuns(graph.getPathFindRuns() + 1);
		if (d.getFather() == null)
			return null;
		//

		p = new PathGraph<>(distanceManager);

//		distanceTotal = d.getDistFromStart();
		while (d != s) {
			p.addStep(d.getElem(), d.getDistFromFather());
			d = d.getFather();
		}
		p.setStartStep(s.getElem());
//		p.setDistanceTotal(distanceTotal);
		return p;
	}

	//

	@SuppressWarnings("unchecked")
	public PathGraph<E, Distance> getPathASynchronized(GraphSimple<E, Distance> ggg, E start, E dest,
			NumberManager<Distance> distanceManager, Predicate<E> isWalkableTester) {
		boolean walkableNull;
//		final double distanceTotal;
		NodeInfoDikstra<E, Distance> ss, dd;
		final Map<E, NodeInfoDikstra<E, Distance>> nodeInfos;
		PriorityQueueKey<NodeInfoDikstra<E, Distance>, Distance> frontier;
		PathGraph<E, Distance> p;
		GraphSimpleAsynchronized<E, Distance> graph;
		GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized s, d;
		UsynchronizedAdjacentForEacherDijkstra<E, Distance> forAdjacents;
		Comparator<Distance> comp;

		if (!(ggg instanceof GraphSimpleAsynchronized<?, ?>))
			return null;
		graph = (GraphSimpleAsynchronized<E, Distance>) ggg;
		s = (GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized) graph.getNode(start);
		d = (GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized) graph.getNode(dest);
		if (s == null || d == null || d == s)
			return null;

		walkableNull = isWalkableTester == null;
		if ((!walkableNull) && ((!isWalkableTester.test(start)) || (!isWalkableTester.test(dest)))) { return null; }

		comp = distanceManager.getComparator();
		nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, graph.getComparatorElements());
		frontier = new PriorityQueueKey<>((i1, i2) -> graph.getCompNodeGraph().compare(i1.thisNode, i2.thisNode), comp,
				no -> no.distFromStart);
		ss = new NodeInfoDikstra<E, Distance>(s, distanceManager);
		ss.father = ss;
		ss.distFromFather = ss.distFromStart = null; // Double.valueOf(0);
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoDikstra<E, Distance>(d, distanceManager);
		nodeInfos.put(dest, dd);

		forAdjacents = new UsynchronizedAdjacentForEacherDijkstra<>(nodeInfos, frontier, distanceManager,
				isWalkableTester);

		while (!frontier.isEmpty()) {
			final NodeInfoDikstra<E, Distance> n;
			n = frontier.removeMinimum().getKey();
			/*
			 * do not waste time computing nodes that have longer path of the alredy
			 * discovered ones
			 */
			if (dd.father == null ||
			// dd.distFromStart > n.distFromStart
					comp.compare(dd.distFromStart, n.distFromStart) > 0) {
				forAdjacents.setCurrentNode(n);
				n.thisNode.forEachAdjacents(forAdjacents);
			} else
				/*
				 * destination has got a father and the node with MINIMUM "distance from start"
				 * has a distance greater than the destination itself -> iterating is useless ->
				 * empty the frontier
				 */
				frontier.clear();
			n.color = NodePositionInFrontier.Closed;
		}
		if (dd.father == null)
			return null;
		//
		nodeInfos.clear();
		p = new PathGraph<>(distanceManager);
//		distanceTotal = dd.distFromStart;
		while (dd != ss) {
			p.addStep(dd.thisNode.getElem(), dd.distFromFather);
			dd = dd.father;
		}
		p.setStartStep(s.getElem());
//		p.setDistanceTotal(distanceTotal);
		return p;
	}

	//

	// subclasses

	protected static class NodeInfoDikstra<E, Distance extends Number> {
		protected NodePositionInFrontier color;
		protected Distance distFromStart, distFromFather;
		protected GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized thisNode;
		protected NodeInfoDikstra<E, Distance> father;
		//
		protected NumberManager<Distance> distanceManager;

		protected NodeInfoDikstra(GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized thisNode,
				NumberManager<Distance> distanceManager) {
			this.distanceManager = distanceManager;
			this.thisNode = thisNode;
			color = NodePositionInFrontier.NeverAdded;
			distFromStart = distFromFather = null;
			father = null;
		}
	}

	private static class UsynchronizedAdjacentForEacherDijkstra<E, Distance extends Number>
			implements BiConsumer<GraphSimple<E, Distance>.NodeGraph, Distance> {

		UsynchronizedAdjacentForEacherDijkstra(Map<E, NodeInfoDikstra<E, Distance>> nodeInfos,
				PriorityQueueKey<NodeInfoDikstra<E, Distance>, Distance> frontier,
				NumberManager<Distance> distanceManager, Predicate<E> isWalkableTester) {
			this.nodeInfos = nodeInfos;
			this.frontier = frontier;
			this.distanceManager = distanceManager;
			this.walkableNull = (this.isWalkableTester = isWalkableTester) == null;
		}

		protected final boolean walkableNull;
		protected final Predicate<E> isWalkableTester;
		protected NumberManager<Distance> distanceManager;
		private NodeInfoDikstra<E, Distance> currentNode;
		private final Map<E, NodeInfoDikstra<E, Distance>> nodeInfos;
		private final PriorityQueueKey<NodeInfoDikstra<E, Distance>, Distance> frontier;

		//

		public void setCurrentNode(NodeInfoDikstra<E, Distance> n) { this.currentNode = n; }

//		public void accept(GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized nod, Integer distToAdj) {
		@SuppressWarnings("unchecked")
		@Override
		public void accept(GraphSimple<E, Distance>.NodeGraph neigbourNode, Distance distToAdj) {
			Distance distToNo;
			E e;
			GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized no;
			NodeInfoDikstra<E, Distance> noInfo;
			if (walkableNull || isWalkableTester.test(neigbourNode.getElem())) {
				no = (GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized) neigbourNode;
				e = no.getElem();
				if (nodeInfos.containsKey(e))
					noInfo = nodeInfos.get(e);
				else
					nodeInfos.put(e, noInfo = new NodeInfoDikstra<E, Distance>(no, distanceManager));

				if (noInfo.color == NodePositionInFrontier.Closed)
					return;
				distToNo = // distToAdj + currentNode.distFromStart;
						this.distanceManager.getAdder().apply(distToAdj, currentNode.distFromStart);
				if (noInfo.father == null ||
				// distToNo < noInfo.distFromStart
						distanceManager.getComparator().compare(distToNo, noInfo.distFromStart) < 0) {
//				final Double newDistance;
					// update
//				newDistance = Double.valueOf(distToNo);
					noInfo.father = currentNode;
					noInfo.distFromFather = distToAdj; // Double.valueOf(distToAdj);
					if (noInfo.color == NodePositionInFrontier.NeverAdded) {
						// add on queue
						noInfo.color = NodePositionInFrontier.InFrontier;
						noInfo.distFromStart = distToNo;
						frontier.put(noInfo);
					} else // it's grey, it's actually in the queue
						frontier.alterKey(noInfo, nodd -> (nodd).distFromStart = distToNo);
				}
			}
		}
	}

	protected static class DistanceKeyAlterator<E, Distance extends Number>
			implements Consumer<GraphSimple<E, Distance>.NodeGraph> {
		Distance distToNo;

		@SuppressWarnings("unchecked")
		@Override
		public void accept(GraphSimple<E, Distance>.NodeGraph nodd) {
			((GraphSimpleSynchronized<E, Distance>.NodeGraphSimpleSynchronized) nodd).setDistFromStart(distToNo);
		}
	}

	@Override
	public List<E> getPath(E start, E dest, NumberManager<Distance> distanceManager, Predicate<E> isWalkableTester,
			boolean returnPathToClosestNodeIfNotFound) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> getPath(ObjectShaped objPlanningToMove, E dest, NumberManager<Distance> distanceManager,
			Predicate<E> isWalkableTester, boolean returnPathToClosestNodeIfNotFound) {
		// TODO Auto-generated method stub
		return null;
	}
}