package dataStructures.graph.pathfind;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import dataStructures.graph.GraphSimple;
import dataStructures.graph.GraphSimpleAsynchronized;
import dataStructures.graph.PathFindStrategy;
import dataStructures.graph.PathGraph;
import dataStructures.isom.PathFinderIsomFrontierBased.NodePositionInFrontier;
import geometry.ObjectShaped;
import tools.NumberManager;

public class PathFindDijkstraWithEuristic<E, Distance extends Number> implements PathFindStrategy<E, Distance> {
	private static final long serialVersionUID = 56409561023300330L;

	public PathFindDijkstraWithEuristic(BiFunction<E, E, Distance> heuristic) {
		super();
		this.heuristic = heuristic;
	}

	protected BiFunction<E, E, Distance> heuristic;

	public BiFunction<E, E, Distance> getHeuristic() { return heuristic; }

	public PathFindDijkstraWithEuristic<E, Distance> setHeuristic(BiFunction<E, E, Distance> heuristic) {
		this.heuristic = heuristic;
		return this;
	}

	//

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

	@SuppressWarnings("unchecked")
	@Override
	public PathGraph<E, Distance> getPath(GraphSimple<E, Distance> graph, E start, E dest,
			NumberManager<Distance> distanceManager, Predicate<E> isWalkableTester) {
		boolean walkableNull;
//		final double distanceTotal;
		PathGraph<E, Distance> p;
		GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized s, d;
		NodeInfoAStar<E, Distance> ss, dd;
		final Map<E, NodeInfoAStar<E, Distance>> nodeInfos;
		PriorityQueueKey<NodeInfoAStar<E, Distance>, Distance> frontier;
		UsynchronizedAdjacentForEacherAStar<E, Distance> forAdjacents;
		Comparator<Distance> comp;
		comp = distanceManager.getComparator();

		walkableNull = isWalkableTester == null;
		if ((!walkableNull) && ((!isWalkableTester.test(start)) || (!isWalkableTester.test(dest)))) { return null; }

		{ // scope for the heuristic, to pick the variable as soon as possible and then
			// free its space on the stack
			BiFunction<E, E, Distance> h;
			h = heuristic;
			if (h == null)
				return null;

			nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
					MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, graph.getComparatorElements());
			frontier = new PriorityQueueKey<>((i1, i2) -> graph.getCompNodeGraph().compare(i1.thisNode, i2.thisNode),
					comp, no -> no.fScore);
			forAdjacents = new UsynchronizedAdjacentForEacherAStar<E, Distance>(nodeInfos, frontier, h, distanceManager,
					isWalkableTester);
		}
		s = (GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized) graph.getNode(start);
		d = (GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized) graph.getNode(dest);
		if (s == null || d == null || d == s)
			return null;
		ss = new NodeInfoAStar<E, Distance>(s, distanceManager);
		ss.father = ss;
		ss.distFromFather = ss.distFromStart = distanceManager.getZeroValue();
		ss.fScore = distanceManager.getZeroValue(); // Double.valueOf(0);
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoAStar<E, Distance>(d, distanceManager);
		nodeInfos.put(dest, dd);

		while (!frontier.isEmpty()) {
			final NodeInfoAStar<E, Distance> n;
			n = frontier.removeMinimum().getKey();
			n.color = NodePositionInFrontier.Closed;
			/*
			 * continue if there's a path to reach the destination shorter to the already
			 * found one. This condition will force to exit from the cycle if the "minimum"
			 * node is already the destination because of the lack of equality check
			 */
			/*
			 * do not waste time computing nodes that have longer path of the alredy
			 * discovered ones
			 */
			if (dd.father == null ||
			// dd.fScore > n.fScore
					comp.compare(dd.fScore, n.fScore) > 0) {
				forAdjacents.setCurrentNode(n);
				n.thisNode.forEachAdjacents(forAdjacents);
			} else
				/*
				 * destination has got a father and the node with MINIMUM "distance from start"
				 * has a distance greater than the destination itself -> iterating is useless ->
				 * empty the frontier
				 */
				frontier.clear();
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

	protected static class NodeInfoAStar<E, Distance extends Number> {
		protected NodePositionInFrontier color;
		protected Distance distFromStart, distFromFather, fScore;
		protected GraphSimple<E, Distance>.NodeGraph thisNode;
		protected NodeInfoAStar<E, Distance> father;

		protected NodeInfoAStar(GraphSimple<E, Distance>.NodeGraph thisNode, NumberManager<Distance> distanceManager) {
			this.thisNode = thisNode;
			color = NodePositionInFrontier.NeverAdded;
			distFromStart = distanceManager.getZeroValue();
			distFromFather = distanceManager.getZeroValue();
			fScore = distanceManager.getZeroValue();
			father = null;
		}
	}

	private static class UsynchronizedAdjacentForEacherAStar<E, Distance extends Number>
			implements BiConsumer<GraphSimple<E, Distance>.NodeGraph, Distance> {

		UsynchronizedAdjacentForEacherAStar(Map<E, NodeInfoAStar<E, Distance>> nodeInfos,
				PriorityQueueKey<NodeInfoAStar<E, Distance>, Distance> frontier, BiFunction<E, E, Distance> heuristic,
				NumberManager<Distance> distanceManager, Predicate<E> isWalkableTester) {
			this.nodeInfos = nodeInfos;
			this.frontier = frontier;
			this.heuristic = heuristic;
			this.distanceManager = distanceManager;
			this.walkableNull = (this.isWalkableTester = isWalkableTester) == null;
		}

		protected final boolean walkableNull;
		protected final Predicate<E> isWalkableTester;
		private NodeInfoAStar<E, Distance> currentNode;
		private final Map<E, NodeInfoAStar<E, Distance>> nodeInfos;
		private final PriorityQueueKey<NodeInfoAStar<E, Distance>, Distance> frontier;
		private final BiFunction<E, E, Distance> heuristic;
		private final NumberManager<Distance> distanceManager;

		public void setCurrentNode(NodeInfoAStar<E, Distance> n) { this.currentNode = n; }

//		public void accept(GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized nod, Integer distToAdj) {
		@SuppressWarnings("unchecked")
		@Override
		public void accept(GraphSimple<E, Distance>.NodeGraph neighbourNode, Distance distToAdj) {
			Distance distToNo, distToAdjDouble;
			E e;
			GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized no;
			NodeInfoAStar<E, Distance> neighbourInfo;
			if (walkableNull || isWalkableTester.test(neighbourNode.getElem())) {
				no = (GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized) neighbourNode;
				e = no.getElem();
				if (nodeInfos.containsKey(e))
					neighbourInfo = nodeInfos.get(e);
				else
					nodeInfos.put(e, neighbourInfo = new NodeInfoAStar<E, Distance>(no, distanceManager));

//			if (neighbourInfo.color == NodePositionInFrontier.Closed) // equivalent of being in closed set
//				return;
				distToAdjDouble = distToAdj;
				distToNo = // distToAdjDouble + currentNode.distFromStart;
						distanceManager.getAdder().apply(distToAdjDouble, currentNode.distFromStart);
				// create the new node or try to re-opening it
				if (neighbourInfo.father == null ||
//					distToNo < neighbourInfo.distFromStart
						distanceManager.getComparator().compare(distToNo, neighbourInfo.distFromStart) < 0) {
//				final Double newDistanceFromStart,
					Distance fScore;
					// update
//				newDistanceFromStart = Double.valueOf(distToNo);
					neighbourInfo.father = currentNode;
					neighbourInfo.distFromFather = distToAdjDouble;
					neighbourInfo.distFromStart = distToNo; // newDistanceFromStart;
					fScore =
//						newDistanceFromStart + this.heuristic.apply(currentNode.thisNode.getElem(), neighbourInfo.thisNode.getElem());
							distanceManager.getAdder().apply(distToNo, this.heuristic
									.apply(currentNode.thisNode.getElem(), neighbourInfo.thisNode.getElem()));
					if (neighbourInfo.color == NodePositionInFrontier.NeverAdded) {
						// track that's in open set, e.g. it has been seen almost one time
						neighbourInfo.color = NodePositionInFrontier.InFrontier;
						neighbourInfo.fScore = fScore;
						// add on queue
						frontier.put(neighbourInfo);
					} else // it's grey, it's actually in the queue / open set
						frontier.alterKey(neighbourInfo, nodd -> nodd.fScore = fScore);
				}
			}
		}
	}

}