package dataStructures.isom;

import java.util.Comparator;

public interface PathFinderIsomFrontierBased<Distance extends Number> extends PathFinderIsom<Distance> {
//	protected final Comparator<NodeInfoFrontierBased<Distance>> COMPARATOR_NINFO =
	public default Comparator<NodeInfoFrontierBased<Distance>> newComparatorNodeInfo() {
		return (ni1, ni2) -> {
			if (ni1 == ni2)
				return 0;
			if (ni1 == null)
				return -1;
			if (ni2 == null)
				return 1;
			return NodeIsom.COMPARATOR_NODE_ISOM_POINT.compare(ni1.thisNode, ni2.thisNode);
		};
	}

	public static enum NodePositionInFrontier {
		NeverAdded, InFrontier, Closed;
	}

	public static class NodeInfoFrontierBased<D extends Number> extends NodeInfo<D> {

		public NodePositionInFrontier color;

		public NodeInfoFrontierBased(NodeIsom<D> thisNode) {
			super(thisNode);
			this.color = NodePositionInFrontier.NeverAdded;
		}
	}
}