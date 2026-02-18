package dataStructures.treeSimilStrat;

import dataStructures.EditCosts;
import dataStructures.NodeComparable;

public interface NodeAlteringCosts<E> extends EditCosts<NodeComparable<E>> {
	public default long insertNodeCost(NodeComparable<E> node) { return insertion(node); }

	public default long deleteNodeCost(NodeComparable<E> node) { return deletion(node); }

	public long renameNodeCost(NodeComparable<E> node, E newLabel);

	@Override
	public default long substitution(NodeComparable<E> node, NodeComparable<E> newNode) {
		return renameNodeCost(node, newNode.getKeyIdentifier());
	}

	//

	public static <T> NodeAlteringCosts<T> newDefaultNAC() {
		return new NodeAlteringCosts<>() {
			@Override
			public long insertion(NodeComparable<T> node) { return 1; }

			@Override
			public long deletion(NodeComparable<T> node) { return 1; }

			@Override
			public long renameNodeCost(NodeComparable<T> node, T newLabel) {
				return node.getKeyComparator().compare(node.getKeyIdentifier(), newLabel) == 0 ? 0 : 1;
			}
		};
	}

	/**
	 * Auxiliary class, to transform methods like
	 * {@link NodeAlteringCosts#insertNodeCost(NodeComparable)} and
	 * {@link NodeAlteringCosts#deleteNodeCost(NodeComparable)} into functional
	 * interfaces.
	 */
	public static interface ActionOnNodeCost<K> extends ActionCost<NodeComparable<K>> {
		public default long getNodeCost(NodeComparable<K> node) { return getCost(node); }
	}
}