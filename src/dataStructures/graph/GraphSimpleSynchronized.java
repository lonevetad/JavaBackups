package dataStructures.graph;

import java.util.Comparator;
import java.util.function.BiConsumer;

import dataStructures.isom.PathFinderIsomFrontierBased.NodePositionInFrontier;

// 2019-11-17 : DijkstraColor is changed to NodePositionInFrontier
public class GraphSimpleSynchronized<E, Distance extends Number> extends GraphSimple<E, Distance> {

	protected int pathFindRuns;

	//

	//

	public GraphSimpleSynchronized(boolean isDirected, PathFindStrategy<E, Distance> pathFinder,
			Comparator<E> comparatorElements) {
		super(isDirected, pathFinder, comparatorElements);
		this.pathFindRuns = 0;
	}

	public GraphSimpleSynchronized(PathFindStrategy<E, Distance> pathFinder, Comparator<E> comparatorElements) {
		super(pathFinder, comparatorElements);
		this.pathFindRuns = 0;
	}

	@Override
	public final boolean isSynchronized() { return true; }

	@Override
	protected NodeGraph newNodeGraph(E e) { return new NodeGraphSimpleSynchronized(e); }

	public int getPathFindRuns() { return pathFindRuns; }

	public GraphSimpleSynchronized<E, Distance> setPathFindRuns(int pathFindRuns) {
		this.pathFindRuns = pathFindRuns;
		return this;
	}

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

	// subclasses

	public class NodeGraphSimpleSynchronized extends NodeGraph {
		// for dijkstra
		protected int pathFindierIdRuns;
		protected NodePositionInFrontier color;
		private Distance distFromStart;
		private Distance distFromFather;
		private NodeGraphSimpleSynchronized father;

		protected NodeGraphSimpleSynchronized(E e) {
			super(e);
			pathFindierIdRuns = 0;
			checkAndReset(-1);
		}

		public void checkAndReset(int dr) {
			if (dr != pathFindierIdRuns) {
				pathFindierIdRuns = dr;
				color = NodePositionInFrontier.NeverAdded;
				distFromStart = distFromFather = null;
				father = null;
			}
		}

		@Override
		public String toString() {
			StringBuilder sb;
			sb = new StringBuilder(127);
			return sb.append(super.toString()).append(", color: ").append(this.color).append(", dist-start: ")
					.append(this.getDistFromStart()).append(", father: ")
					.append(this.getFather() == null ? "null" : String.valueOf(this.getFather().getElem())).toString();
		}

		public int getDijkstraRuns() { return pathFindierIdRuns; }

		public NodePositionInFrontier getColor() { return color; }

		public Distance getDistFromStart() { return distFromStart; }

		public Distance getDistFromFather() { return distFromFather; }

		public NodeGraphSimpleSynchronized getFather() { return father; }

		public NodeGraphSimpleSynchronized setDijkstraRuns(int dijkstraRuns) {
			this.pathFindierIdRuns = dijkstraRuns;
			return this;
		}

		public NodeGraphSimpleSynchronized setColor(NodePositionInFrontier color) {
			this.color = color;
			return this;
		}

		public NodeGraphSimpleSynchronized setDistFromStart(Distance distFromStart) {
			this.distFromStart = distFromStart;
			return this;
		}

		public NodeGraphSimpleSynchronized setDistFromFather(Distance distFromFather) {
			this.distFromFather = distFromFather;
			return this;
		}

		public NodeGraphSimpleSynchronized setFather(NodeGraphSimpleSynchronized father) {
			this.father = father;
			return this;
		}
	}
}
