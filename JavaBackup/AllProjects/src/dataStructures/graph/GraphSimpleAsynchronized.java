package dataStructures.graph;

import java.util.Comparator;
import java.util.function.BiConsumer;

public class GraphSimpleAsynchronized<E, Distance extends Number> extends GraphSimple<E, Distance> {

	public GraphSimpleAsynchronized(boolean isDirected, PathFindStrategy<E, Distance> pathFinder,
			Comparator<E> comparatorElements) {
		super(isDirected, pathFinder, comparatorElements);
	}

	public GraphSimpleAsynchronized(PathFindStrategy<E, Distance> pathFinder, Comparator<E> comparatorElements) {
		super(pathFinder, comparatorElements);
	}

	//

	@Override
	public final boolean isSynchronized() { return false; }

	@Override
	protected NodeGraph newNodeGraph(E e) { return new NodeGraphSimpleAsynchronized(e); }

	//

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
			n.forEachAdjacents(adjPrinter);
		});

		return sb.toString();
	}

	//

	public class NodeGraphSimpleAsynchronized extends NodeGraph {
		NodeGraphSimpleAsynchronized(E e) { super(e); }
	}
}