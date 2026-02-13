package dataStructures.graph;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.graph.EdgesIntersectionDetector.IntersectionInstantiator;
import dataStructures.graph.cycles.SubcyclesCollector;
import tools.LoggerMessages;
import tools.NumberManager;

public abstract class GraphSimple<E, Distance extends Number> {

//	public static enum NodePositionInFrontier {NeverAdded, InFrontier, Closed;}

	//

	//

	protected final Comparator<NodeGraph> compNodeGraph;
	//
//			private int idProgNodeGraph = 0;
	protected final boolean isDirected;
	protected int linksAmount;
	protected final MapTreeAVL<E, NodeGraph> nodes;
	protected final Comparator<E> comparatorElements;
	protected LoggerMessages log;
	protected PathFindStrategy<E, Distance> pathFinder;

	/**
	 * Should NOT be used !! Only for reflection and wrappers (which are weird,
	 * surely).
	 */
	protected GraphSimple() {
		isDirected = false;
		this.nodes = null;
		comparatorElements = null;
		compNodeGraph = null;
	}

	public GraphSimple(PathFindStrategy<E, Distance> pathFinder, Comparator<E> comparatorElements) {
		this(false, pathFinder, comparatorElements);
	}

	public GraphSimple(boolean isDirected, PathFindStrategy<E, Distance> pathFinder, Comparator<E> comparatorElements) {
		if (comparatorElements == null)
			throw new IllegalArgumentException("Element's comparator cannot be null");
		this.isDirected = isDirected;
		this.pathFinder = pathFinder;
		this.comparatorElements = comparatorElements;
		this.linksAmount = 0;
//				idProgNodeGraph = 0;
		nodes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				MapTreeAVL.BehaviourOnKeyCollision.Replace, comparatorElements);

		compNodeGraph = (n1, n2) -> {
			if (n1 == n2)
				return 0;
			return comparatorElements.compare(n1.getElem(), n2.getElem());
		};

		if (nodes == null)
			throw new RuntimeException("nodes are null");
	}

	// getter and setter

	/** DO NOT MODIFY THE RETURNED MAP */
	public Map<E, NodeGraph> getNodeGraphs() { return nodes; }

	public boolean isDirected() { return isDirected; }

	public int getLinksAmount() {
		final GraphSimple<E, Distance> g;
		if (linksAmount < 0) {
			linksAmount = 0;
			g = this;
			nodes.forEach((e, n) -> { n.adjacents.forEach((adj, dist) -> g.linksAmount++); });
		}
		return linksAmount;
	}

	public PathFindStrategy<E, Distance> getPathFinder() { return pathFinder; }

	public Comparator<E> getComparatorElements() { return comparatorElements; }

	public Comparator<NodeGraph> getCompNodeGraph() { return compNodeGraph; }

	public LoggerMessages getLog() { return log; }

	//

	public GraphSimple<E, Distance> setLog(LoggerMessages log) {
		this.log = log;
		return this;
	}

	public GraphSimple<E, Distance> setPathFinder(PathFindStrategy<E, Distance> pathFinder) {
		this.pathFinder = pathFinder;
		return this;
	}

	//

	public abstract boolean isSynchronized();

	protected abstract NodeGraph newNodeGraph(E e);

	//

	public int size() { return nodes.size(); }

	public boolean isEmpty() { return size() == 0; }

	/** Beware: if the map modified, the graph will be modified as well */
	public SortedSet<E> getElementsSet() {
		return this.nodes.toSetKey();
	}

	/** As like as {@link #getElementsSet()}. */
	public SortedSet<Entry<E, NodeGraph>> getNodesSet() {
		return this.nodes.toSetEntry();
	}

	/** USE WITH CAUTION */
	public MapTreeAVL<E, NodeGraph> getNodes() { return this.nodes; }

	public boolean hasNode(E e) { return getNode(e) != null; }

	public NodeGraph getNode(E e) { return nodes.get(e); }

	public boolean hasLink(E from, E dest) {
		NodeGraph nf;
		nf = getNode(from);
		if (nf == null)
			return false;
		return nf.adjacents.containsKey(dest);
	}

	public Distance getDistance(E from, E to) {
		NodeGraph nf;
		nf = getNode(from);
		if (nf == null)
			return null;
		return nf.adjacents.get(to);
	}

	public void addNode(E e) {
		NodeGraph n;
		if (nodes.containsKey(e))
			return;
		n = newNodeGraph(e);
		nodes.put(e, n);
		linksAmount = -1;
	}

	public void addLink(E from, E dest, Distance distance) {
		NodeGraph f, d;
		f = nodes.get(from);
		d = nodes.get(dest);
		if (f == null)
			nodes.put(from, f = newNodeGraph(from));
		if (d == null)
			nodes.put(dest, d = newNodeGraph(dest));
//				if(!f.getAdjacent().containsKey(d)) {}
		f.checkAdj();
		if (f.adjacents.put(d, distance) == null)
			this.linksAmount++; // why shoyld be: this.linksAmount = -1; ????
		if (!this.isDirected) {
			d.checkAdj();
			if (d.adjacents.put(f, distance) == null)
				this.linksAmount++; // why shoyld be: this.linksAmount = -1; ????
		}
	}

	/** Remove the given node and all links associated with */
	public boolean removeNode(E e) {
		NodeGraph n;
		n = getNode(e);
		if (n == null)
			return false;
		this.nodes.remove(e);
		if (isDirected)
			this.nodes.forEach((ee, node) -> node.adjacents.remove(n));
		else // only known adjacents are needed, because is undirect
			n.adjacents.forEach((nodeAdjacent, dist) -> nodeAdjacent.adjacents.remove(n));
		linksAmount = -1;
		return true;
	}

	public boolean removeLink(E from, E dest) {
		NodeGraph nf, nd;
		nd = null;
		nf = getNode(from);
		if (nf == null)
			return false;
		nd = getNode(dest);
		if (nd == null)
			return false;

		nf.adjacents.remove(nd);
		linksAmount = -1;
		if (!isDirected) {
			nd.adjacents.remove(nf);
			linksAmount = -1;
		}
		return true;
	}

	public PathGraph<E, Distance> getPath(E start, E dest, NumberManager<Distance> distanceManager,
			Predicate<E> isWalkableTester) {
		return this.pathFinder == null ? null
				: this.pathFinder.getPath(this, start, dest, distanceManager, isWalkableTester);
	}

	@Override
	public String toString() {
		StringBuilder sb;
		BiConsumer<NodeGraph, Distance> adjPrinter;
		sb = new StringBuilder(1024);
		sb.append("Graph ").append(isDirected ? "directed" : "undirected").append(" having ").append(this.linksAmount)
				.append(" links and this nodes:\n");

		adjPrinter = (n, dist) -> sb.append("\n-\t").append(", dist: ").append(dist).append(" -- node: ")
				.append(n.toString());
		this.nodes.forEach((e, n) -> {
			sb.append("\n+ ").append(e).append(" ->");
			n.adjacents.forEach(adjPrinter);
		});

		return sb.toString();
	}

	public void forEach(Consumer<E> c) { nodes.forEach((e, n) -> c.accept(e)); }

	public void forEach(BiConsumer<E, NodeGraph> c) { nodes.forEach(c); }

	public GraphSimple<E, Distance> deepCopy(GraphSimpleGenerator<E, Distance> gsg) {
		GraphSimple<E, Distance> copy;
		copy = gsg.newGraph(isDirected(), getPathFinder(), getComparatorElements());
		this.forEach((e, n) -> { n.forEachAdjacents((ad, dist) -> { copy.addLink(e, ad.elem, dist); }); });
		return copy;
	}

	/** The original implementation runs in at least <code>O(|E|^2)</code> */
	public void splitIntersectingEdges(EdgesIntersectionDetector<E, Distance> eid,
			IntersectionInstantiator<E, Distance> ii, DistanceCalculator<E, Distance> distanceCalculator) {
		final GraphSimple<E, Distance> g;
		if (eid == null || ii == null || distanceCalculator == null)
			return;
		g = this;
//for each node
		this.forEach((e1, n1) -> {
			// take the neighbour to create the first edge
			n1.forEachAdjacents((ad1, d1) -> {
				if (n1 != ad1)
					// search for the second edge
					this.forEach((e2, n2) -> {
						if (n1 != n2 && ad1 != n2)
							n2.forEachAdjacents((ad2, d2) -> {
								if (n2 != ad2 && n1 != ad2 && ad1 != ad2) {
									// all different :D
									if (!eid.performIntersectionSplit(g, e1, ad1.elem, e2, ad2.elem, ii,
											distanceCalculator)) {
										System.out.println("ERROR on splitting edges:\n\t e1: (" + e1 + "->" + ad1.elem
												+ ")\n\t e2: (" + e2 + "->" + ad2.elem + ")");
									}
								}
							});
					});
			});
		});
	}

	/**
	 * Useful for dividing a self-intersecting polygon into its
	 * non-self-intersecting sub-polygons (further subdivisation could be performed
	 * by providing a way to search for all convex subdivisions that are maximal).
	 */
	public List<GraphSimple<E, Distance>> splitAndCollect(EdgesIntersectionDetector<E, Distance> eid,
			IntersectionInstantiator<E, Distance> ii, DistanceCalculator<E, Distance> distanceCalculator,
			SubcyclesCollector<E, Distance> scc, boolean keepSource, GraphSimpleGenerator<E, Distance> gsg) {
		final GraphSimple<E, Distance> g;
		g = keepSource ? this.deepCopy(gsg) : this;
		g.splitIntersectingEdges(eid, ii, distanceCalculator);
		return scc.collectCycles(false, g, gsg);
	}

	//

	@Deprecated
	protected E getElem(Entry<NodeGraph, Long> e) {
		if (e == null || e.getKey() == null)
			return null;
		return e.getKey().elem;
	}

	//

	// subclasses

	public abstract class NodeGraph {
		private E elem;
		protected Map<NodeGraph, Distance> adjacents; // the "value" is the distance
		// for dijkstra

		protected NodeGraph(E e) {
//					this.id = idProgNodeGraph++;
			this.setElem(e);
			this.adjacents = null;
		}

		public E getElem() { return elem; }

		public void setElem(E elem) { this.elem = elem; }

		// public Map<NodeGraph, Integer> getAdjacents() {
//			checkAdj();
//			return adjacents;
//		}

		public int adjacentsSize() {
			checkAdj();
			return adjacents.size();
		}

		public void forEachAdjacents(BiConsumer<NodeGraph, Distance> consumer) {
			checkAdj();
			if (!adjacents.isEmpty())
				adjacents.forEach(consumer);
		}

		public Iterator<NodeGraph> iteratorAdjacent() {
			checkAdj();
			if (!adjacents.isEmpty())
				return adjacents.keySet().iterator();
			return null;
		}

		public boolean hasAdjacent(NodeGraph n) {
			checkAdj();
			return adjacents.containsKey(n);
		}

//		public Distance getAdjacentDistance(NodeGraph n) {
//			checkAdj();
//			return adjacents.get(n);
//		}

		void checkAdj() {
			if (adjacents == null)
				adjacents = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
						MapTreeAVL.BehaviourOnKeyCollision.Replace, compNodeGraph);
		}

		@Override
		public String toString() {
			StringBuilder sb;
			sb = new StringBuilder(127);
			return sb.append("Node's elem: ").append(this.getElem()).toString();
		}

	}
}