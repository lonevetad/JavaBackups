package dataStructures.graph.old;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import dataStructures.graph.PathFindStrategy;
import dataStructures.graph.PathGraph;
import tools.LoggerMessages;

@Deprecated
public abstract class GraphSimple_Old<E> {

	public static enum DijkstraColor {
		White, Grey, Black;
	}

	public static final Comparator<Integer> INT_COMPARATOR = // Integer::compare;
			(i1, i2) -> {
				if (i1 == null && i2 == null)
					return 0;
				if (i1 == null)
					return -1;
				if (i2 == null)
					return 1;
				return Integer.compare(i1, i2);
			};
	public static final Comparator<Double> DOUBLE_COMPARATOR = // Integer::compare;
			(i1, i2) -> {
				if (i1 == null && i2 == null)
					return 0;
				if (i1 == null)
					return -1;
				if (i2 == null)
					return 1;
				return Double.compare(i1, i2);
			};

	//

	//

	protected final Comparator<NodeGraph> compNodeGraph;
	//
//			private int idProgNodeGraph = 0;
	protected final boolean isDirected;
	protected int linksAmount;
	protected final Map<E, NodeGraph> nodes;
	protected final Comparator<E> comparatorElements;
	protected LoggerMessages log;
	protected PathFindStrategy<E> pathFinder;

	public GraphSimple_Old(PathFindStrategy<E> pathFinder, Comparator<E> comparatorElements) {
		this(false, pathFinder, comparatorElements);
	}

	public GraphSimple_Old(boolean isDirected, PathFindStrategy<E> pathFinder, Comparator<E> comparatorElements) {
		if (comparatorElements == null)
			throw new IllegalArgumentException("Element's comparator cannot be null");
		this.isDirected = isDirected;
		this.pathFinder = pathFinder;
		this.comparatorElements = comparatorElements;
		this.linksAmount = 0;
//				idProgNodeGraph = 0;
		nodes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, MapTreeAVL.BehaviourOnKeyCollision.Replace,
				comparatorElements);

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
	public Map<E, NodeGraph> getNodeGraphs() {
		return nodes;
	}

	public boolean isDirected() {
		return isDirected;
	}

	public int getLinksAmount() {
		return linksAmount;
	}

	public PathFindStrategy<E> getPathFinder() {
		return pathFinder;
	}

	public Comparator<E> getComparatorElements() {
		return comparatorElements;
	}

	public Comparator<NodeGraph> getCompNodeGraph() {
		return compNodeGraph;
	}

	public LoggerMessages getLog() {
		return log;
	}

	//

	public GraphSimple_Old<E> setLog(LoggerMessages log) {
		this.log = log;
		return this;
	}

	public GraphSimple_Old<E> setPathFinder(PathFindStrategy<E> pathFinder) {
		this.pathFinder = pathFinder;
		return this;
	}
	//

	public abstract boolean isSynchronized();

	protected abstract NodeGraph newNodeGraph(E e);

	//
	public NodeGraph getNode(E e) {
		return nodes.get(e);
	}

	public void addNode(E e) {
		NodeGraph n;
		if (nodes.containsKey(e))
			return;
		n = newNodeGraph(e);
		nodes.put(e, n);
	}

	public void addLink(E from, E dest, int distance) {
		Integer dist;
		NodeGraph f, d;
		f = nodes.get(from);
		d = nodes.get(dest);
		if (f == null)
			nodes.put(from, f = newNodeGraph(from));
		if (d == null)
			nodes.put(dest, d = newNodeGraph(dest));
//				if(!f.getAdjacent().containsKey(d)) {}
		dist = Integer.valueOf(distance);
		if (f.adjacents.put(d, dist) == null)
			this.linksAmount++;
		if (!this.isDirected)
			if (d.adjacents.put(f, dist) == null)
				this.linksAmount++;
	}

	/** Remove the given node and all links associated with */
	public boolean removeNode(E e) {
		NodeGraph n;
		n = getNode(e);
		if (n == null)
			return false;
		this.nodes.remove(e);
		if (isDirected)
			this.nodes.forEach((ee, node) -> node.adjacents.remove(e));
		else // only known adjacents are needed, because is undirect
			n.adjacents.forEach((node, dist) -> node.adjacents.remove(e));
		return true;
	}

	public boolean removeLink(E from, E dest) {
		NodeGraph nf, nd;
		nd = null;
		nf = getNode(from);
		if (nf == null)
			return false;

		nf.adjacents.remove(dest);
		if (isDirected)
			return true;

		nd = getNode(dest);
		if (nd == null)
			return false;
		nd.adjacents.remove(from);
		return true;
	}

	public boolean hasNode(E e) {
		return getNode(e) != null;
	}

	public boolean hasLink(E from, E dest) {
		NodeGraph nf;
		nf = getNode(from);
		if (nf == null)
			return false;
		return nf.adjacents.containsKey(dest);
	}

	public PathGraph<E> getPath(E start, E dest) {
		throw new UnsupportedOperationException("That's an old version, don't use it");
//		return this.pathFinder == null ? null : this.pathFinder.getPath(this, start, dest);
	}

	@Override
	public String toString() {
		StringBuilder sb;
		BiConsumer<NodeGraph, Integer> adjPrinter;
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

	//

	@Deprecated
	protected E getElem(Entry<NodeGraph, Integer> e) {
		if (e == null || e.getKey() == null)
			return null;
		return e.getKey().elem;
	}

	// subclasses

	public abstract class NodeGraph {
//				Integer id;NodePositionInFrontier
		private E elem;
		protected Map<NodeGraph, Integer> adjacents; // the "value" is the distance
		// for dijkstra

		protected NodeGraph(E e) {
//					this.id = idProgNodeGraph++;
			this.setElem(e);
			this.adjacents = null;
		}

		// public Map<NodeGraph, Integer> getAdjacents() {
//			checkAdj();
//			return adjacents;
//		}

		public void forEachAdjacents(BiConsumer<NodeGraph, Integer> consumer) {
			checkAdj();
			if (!adjacents.isEmpty())
				adjacents.forEach(consumer);
		}

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

		public E getElem() {
			return elem;
		}

		public void setElem(E elem) {
			this.elem = elem;
		}
	}
}