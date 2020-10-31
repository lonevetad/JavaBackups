package dataStructures;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import tools.DifferenceCalculator;
import tools.Stringable;

/**
 * Node of a tree-like structure (no check if it's a graph or a single rooted
 * tree) that can be compared (through
 * {@link #computeDissonanceAsLong(NodeComparable)}) to produce a "score": the
 * lower, the better.
 */
public interface NodeComparable<K> extends Stringable {

	// STATIC STUFFS

	public static final int HEIGHT_OF_NEW_NODE = -1;

	public static final DissonanceWeights WEIGHTS_DEFAULT = new DissonanceWeights() {
		private static final long serialVersionUID = -5410794L;

		@Override
		public DissonanceWeights setWeightMissingNode(int weightMissingNode) { return this; }

		@Override
		public DissonanceWeights setWeightExceedingNode(int weightExceedingNode) { return this; }

		@Override
		public DissonanceWeights setWeightDepth(int weightDepth) { return this; }
	};

	//

	/** Gets a new Node comparator, given a key comparator. */
	public static <T> Comparator<NodeComparable<T>> newNodeComparatorDefault(Comparator<T> keyComp) {
		return new NodeComparatorDefault<>(keyComp);
	}

	/** Provides a simple implementation of the {@link NodeComparable}. */
	public static <T> NodeComparable<T> newDefaultNodeComparable(T value, Comparator<T> comparatorKey) {
		return new DefaultNodeComparable<>(value, comparatorKey);
	}

	public static <T> DifferenceCalculator<NodeComparable<T>> newDifferenceCalculator(Comparator<T> keyComparator) {
		return (s1, s2) -> { return s2.computeDissonanceAsLong(s1); };
	}

	//

	// TODO INSTANCE METHODS

	//

	public K getKeyIdentifier();

	public Comparator<K> getKeyComparator();

	public default Comparator<NodeComparable<K>> getNodeComparator() {
		return newNodeComparatorDefault(getKeyComparator());
	}

	public default int getHeightNode() { return HEIGHT_OF_NEW_NODE; }

	public NodeComparable<K> setHeightNode(int height);

	/**
	 * Constructor-designed method, produces a "backing map" for
	 * {@link #getChildrenNC()}.<br>
	 * Invokes {@link #newChildrenBackmap(Comparator)} passing it
	 * {@link #getNodeComparator()} as parameter;
	 */
	public default MapTreeAVL<NodeComparable<K>, NodeComparable<K>> newChildrenBackmap() {
		return newChildrenBackmap(getNodeComparator());
	}

	public default MapTreeAVL<NodeComparable<K>, NodeComparable<K>> newChildrenBackmap(
			Comparator<NodeComparable<K>> comparatorNode) {
		MapTreeAVL<NodeComparable<K>, NodeComparable<K>> m;
		m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.FullButHeavyNodes,
				MapTreeAVL.BehaviourOnKeyCollision.AddItsNotASet, comparatorNode);
		return m;
	}

	/**
	 * Constructor-designed method, should be chained with a value (a field?)
	 * obtained through ways like {@link #newChildrenBackmap(Comparator)}.
	 */
	public default SortedSetEnhanced<NodeComparable<K>> newChildrenSet(
			MapTreeAVL<NodeComparable<K>, NodeComparable<K>> backmap) {
		return backmap.toSetKey();
	}

	/**
	 * Constructor-designed method, chaining {@link #newChildrenBackmap()} and
	 * {@link #newChildrenSet(MapTreeAVL)}
	 */
	public default SortedSetEnhanced<NodeComparable<K>> newChildrenSet() {
		return newChildrenSet(newChildrenBackmap());
	}

	/**
	 * in future releases, the score (compute
	 * througj{@link #computeDissonanceAsLong(NodeComparable)}
	 */
	public default long scoreKeyCompatibilityWith(K anotherKey) { return 1; }

	/** Returns the set of all children held by this node. */
	public SortedSetEnhanced<NodeComparable<K>> getChildrenNC();

	/**
	 * See {@link #getChildNCMostSimilarTo(NodeComparable)}, giving a node produced
	 * with the given key and a way to instantiate a wrapper node.
	 */
	public default NodeComparable<K> getChildNCMostSimilarTo(K key, Function<K, NodeComparable<K>> nodeGenerator) {
		return getChildNCMostSimilarTo(nodeGenerator.apply(key));
	}

	/**
	 * Beware: could be imprecise: it returns the best matching child for the given
	 * similar node (this node could be produced by
	 * {@link #getChildNCMostSimilarTo(Object, Function)}).
	 */
	public NodeComparable<K> getChildNCMostSimilarTo(NodeComparable<K> copy);

	public default boolean isLeafNC() {
		Set<NodeComparable<K>> children;
		children = getChildrenNC();
		return children == null || children.isEmpty();
	}

	/**
	 * See {@link #getChildNCMostSimilarTo(Object, Function)} for a explanation
	 * (because it's similar) and {@link #containsChildNC(NodeComparable)}, giving
	 * to the last method a node produced with the given key and a way to
	 * instantiate a wrapper node.
	 */
	public default boolean containsChildNC(K key, Function<K, NodeComparable<K>> nodeGenerator) {
		return this.containsChildNC(nodeGenerator.apply(key));
	}

	public default boolean containsChildNC(NodeComparable<K> copy) {
		return this.getChildNCMostSimilarTo(copy) != null;
	}

	/** Scans all children depending on their sorting. */
	public default void forEachChildNC(Consumer<NodeComparable<K>> action) {
		Set<NodeComparable<K>> children;
		children = getChildrenNC();
		if (children == null)
			return;
		children.forEach(action);
	}

	public void forEachChildNCFIFOOrdering(Consumer<NodeComparable<K>> action);

	public default NodeComparable<K> addChildNC(NodeComparable<K> child) {
		int nh, childHeight;
		Set<NodeComparable<K>> children;
		if (child == null)
			return this;
		childHeight = child.getHeightNode();
		if (childHeight != HEIGHT_OF_NEW_NODE && childHeight < getHeightNode()) { return this; } // no cycles allowed
		children = getChildrenNC();
		if (children == null) { return this; }
		children.add(child);
		nh = getHeightNode();
		if (nh != Integer.MAX_VALUE) { nh++; }
		child.setHeightNode(nh);
		return this;
	}

	public default int getTreeSize() {
		int[] s = { 1 };
		Set<NodeComparable<K>> children;
		children = getChildrenNC();
		if (children != null) { children.forEach(n -> s[0] += n.getTreeSize()); }
		return s[0];
	}

	//

	// abstract STUFFS

	//

//	

	/**
	 * Compute how much <code>this</code> node differs to the given one, which is
	 * considered as the "base". Default weights (instance of
	 * {@link DissonanceWeights}) is provided.
	 * <p>
	 * {@link DissonanceWeights#getWeightMissingNode()} weights the nodes that are
	 * missing in <code>this</code> "children set" (obtained by
	 * <code>this.{@link #getChildrenNC()}</code>), while
	 * {@link DissonanceWeights#getWeightExceedingNode()} weights the nodes that are
	 * present in <code>this</code> "children set" but not contained in the "base".
	 * {@link DissonanceWeights#getWeightDepth()} instead weights exponentially the
	 * recursion depth: deeper dependency could be considered having a higher weight
	 * ("less than one" exponent base are not implemented yet).
	 */
	public default long computeDissonanceAsLong(NodeComparable<K> nodeBase) {
		return computeDissonanceAsLong(nodeBase, WEIGHTS_DEFAULT);
	}

//	public default long computeDissonanceAsLong(NodeComparable<K> nodeBase, boolean checkRecursion) {
//		return computeDissonanceAsLong(nodeBase, WEIGHTS_DEFAULT, checkRecursion);
//	}

//	/** See {@link #computeDissonanceAsLong(NodeComparable)}. */
//	public default long computeDissonanceAsLong(NodeComparable<K> nodeBase, DissonanceWeights weights) {
//		return computeDissonanceAsLong(nodeBase, weights, false); // make it fast
//	}

	/** See {@link #computeDissonanceAsLong(NodeComparable)}. */
	public long computeDissonanceAsLong(NodeComparable<K> nodeBase, DissonanceWeights weights);
	// , boolean checkRecursion);

	//

	/**
	 * See {@link #computeDissonanceAsLong(NodeComparable)}, it's the same but
	 * returning and computing a {@link BigInteger} instead.
	 */
	public default BigInteger computeDissonanceAsBigInt(NodeComparable<K> nodeBase) {
		return computeDissonanceAsBigInt(nodeBase, WEIGHTS_DEFAULT);
	}

//	/** See {@link #computeDissonanceAsLong(NodeComparable)}. */
//	public default BigInteger computeDissonanceAsBigInt(NodeComparable<K> nodeBase, boolean checkRecursion) {
//		return computeDissonanceAsBigInt(nodeBase, WEIGHTS_DEFAULT, checkRecursion);
//	}

//	public default BigInteger computeDissonanceAsBigInt(NodeComparable<K> nodeBase, DissonanceWeights weights) {
//		return computeDissonanceAsBigInt(nodeBase, weights, false); // make it fast
//	}

	/** See {@link #computeDissonanceAsLong(NodeComparable)}. */
	public BigInteger computeDissonanceAsBigInt(NodeComparable<K> nodeBase, DissonanceWeights weights);
//	, boolean checkRecursion);

	//

	//

	@Override
	public default void toString(StringBuilder sb) { this.toString(sb, 0); }

	/** Call <code>super</code> before doing anything. */
	public default void toStringNonCollectionFields(StringBuilder sb) {
		sb.append(" --> ");
	}

	@Override
	public default void toString(StringBuilder sb, int level) {
		int lev;
		K k;
		addTab(sb, level, false);
		sb.append("NC: ");
		k = getKeyIdentifier();
		if (k instanceof Stringable) {
			((Stringable) k).toString(sb);
		} else {
			sb.append(k);
		}
		toStringNonCollectionFields(sb);
		sb.append(" -----> children:");
		lev = level + 1;
		this.forEachChildNC((child) -> child.toString(sb.append('\n'), lev));
	}

	//

	// TODO CLASSES

	//

	/** See {@link NodeComparable#computeDissonanceAsLong(NodeComparable)}. */
	public static class DissonanceWeights implements Serializable {
		private static final long serialVersionUID = 23263214070008L;
		protected int weightMissingNode, weightExceedingNode, weightDepth;
		protected BigInteger weightMissingNodeBigInt, weightExceedingNodeBigInt, weightDepthBigInt;

		public DissonanceWeights() {
			this.weightMissingNode = this.weightExceedingNode = this.weightDepth = 1;
			this.weightMissingNodeBigInt = this.weightExceedingNodeBigInt = this.weightDepthBigInt = BigInteger.ONE;
		}

		public int getWeightMissingNode() { return weightMissingNode; }

		public int getWeightExceedingNode() { return weightExceedingNode; }

		public int getWeightDepth() { return weightDepth; }

		public DissonanceWeights setWeightMissingNode(int weightMissingNode) {
			if (weightMissingNode >= 0) {
				this.weightMissingNode = weightMissingNode;
				this.weightMissingNodeBigInt = BigInteger.valueOf(weightMissingNode);
			}
			return this;
		}

		public DissonanceWeights setWeightExceedingNode(int weightExceedingNode) {
			if (weightExceedingNode >= 0) {
				this.weightExceedingNode = weightExceedingNode;
				this.weightExceedingNodeBigInt = BigInteger.valueOf(weightExceedingNode);
			}
			return this;
		}

		public DissonanceWeights setWeightDepth(int weightDepth) {
			if (weightDepth >= 1) {
				this.weightDepth = weightDepth;
				this.weightDepthBigInt = BigInteger.valueOf(weightDepth);
			}
			return this;
		}
	}

	//

	// TODO CLASSES

	//

	public static abstract class NodeComparableDefaultAlghoritms<T> implements NodeComparable<T> {
		private static final long serialVersionUID = -651032512L;

		protected int heightNode = 0;

		@Override
		public int getHeightNode() { return heightNode; }

		@Override
		public NodeComparable<T> setHeightNode(int height) {
			this.heightNode = height < 0 ? 0 : height;
			return this;
		}

		@Override
		public int getTreeSize() {
			int[] s = { 1 };
			Set<NodeComparable<T>> children;
			children = getChildrenNC();
			if (children != null) { children.forEach(n -> s[0] += n.getTreeSize()); }
			return s[0];
		}

		protected void getTreeSize(int[] accumulator) {
			Set<NodeComparable<T>> children;
			accumulator[0]++;
			children = getChildrenNC();
			if (children != null) {
				children.forEach(n -> ((NodeComparableDefaultAlghoritms<T>) n).getTreeSize(accumulator));
			}
		}

		@Override
		public long computeDissonanceAsLong(NodeComparable<T> nodeBase, DissonanceWeights weights) {
			return computeDissonanceAsLong_NoRecursion(nodeBase, weights, weights.weightDepth);
		}

		protected long computeDissonanceAsLong_NoRecursion(NodeComparable<T> nodeBase, DissonanceWeights weights,
				long exponentialWeightDepth) {
			SortedSetEnhanced<NodeComparable<T>> childrenBase;
			long[] dissonance = { Objects.equals(nodeBase.getKeyIdentifier(), this.getKeyIdentifier()) ? 0 : 1 };
			childrenBase = nodeBase.getChildrenNC();
			this.forEachChildNC(child -> {
				if (!childrenBase.contains(child)) {// looks for a exact match
//					dissonance[0]+=
//				} else {
					dissonance[0] += weights.weightExceedingNode * child.getTreeSize();
				}
////				T key;
//				NodeComparable<T> childBase;
////				key = child.getKeyIdentifier();
//				childBase = nodeBase.getChildNCMostSimilarTo(child);
////				if (nodeBase.containsChildNC(key)) {
//				if (childBase != null) {
//					// recursion :D
//					dissonance[0] += ((NodeComparableDefaultAlghoritms<T>) child).computeDissonanceAsLong_NoRecursion(
//							childBase, //
//							weights, //
//							exponentialWeightDepth * weights.weightDepth);
//				} else {
//					dissonance[0] += weights.weightMissingNode * exponentialWeightDepth;
//				}
			});
			nodeBase.forEachChildNC(child -> {
//				T key;
//				key = child.getKeyIdentifier();
				if (!this.containsChildNC(child)) {
					dissonance[0] += weights.weightExceedingNode * exponentialWeightDepth;
				}
			});
			return dissonance[0];
		}

		//

		@Override
		public BigInteger computeDissonanceAsBigInt(NodeComparable<T> nodeBase, DissonanceWeights weights) {
			return checkRecursion ? computeDissonanceAsBigInt_WithRecursion(nodeBase, weights, //
					nodeBase.newChildrenSet(), BigInteger.valueOf(weights.weightDepth))
					: computeDissonanceAsBigInt_NoRecursionCheck(nodeBase, weights,
							BigInteger.valueOf(weights.weightDepth));
		}

		protected BigInteger computeDissonanceAsBigInt_NoRecursionCheck(NodeComparable<T> nodeBase,
				DissonanceWeights weights, BigInteger exponentialWeightDepth) {
			BigInteger[] dissonance = {
					Objects.equals(nodeBase.getKeyIdentifier(), this.getKeyIdentifier()) ? BigInteger.ZERO
							: BigInteger.ONE };
			this.forEachChildNC(child -> {
//				T key;
				NodeComparable<T> childBase;
//				key = child.getKeyIdentifier();
				childBase = nodeBase.getChildNCMostSimilarTo(child);
//				if (nodeBase.containsChildNC(key)) {
				if (childBase != null) {
					// recursion :D
					dissonance[0] = dissonance[0].add(((NodeComparableDefaultAlghoritms<T>) child)
							.computeDissonanceAsBigInt_NoRecursionCheck(childBase, //
									weights, //
									exponentialWeightDepth.multiply(weights.weightDepthBigInt))//
					);
				} else {
					dissonance[0] = dissonance[0].add(exponentialWeightDepth.multiply(weights.weightMissingNodeBigInt));
				}
			});
			nodeBase.forEachChildNC(child -> {
//				T key;
//				key = child.getKeyIdentifier();
				if (!this.containsChildNC(child)) {
					dissonance[0] = dissonance[0]
							.add(exponentialWeightDepth.multiply(weights.weightExceedingNodeBigInt));
				}
			});
			return dissonance[0];
		}

		protected BigInteger computeDissonanceAsBigInt_WithRecursion(NodeComparable<T> nodeBase,
				DissonanceWeights weights, SortedSetEnhanced<NodeComparable<T>> nodesVisited,
				BigInteger exponentialWeightDepth) {
			T thisKey;
			thisKey = this.getKeyIdentifier();
			BigInteger[] dissonance = {
					Objects.equals(nodeBase.getKeyIdentifier(), thisKey) ? BigInteger.ZERO : BigInteger.ONE };
			this.forEachChildNC(child -> {
//				T key;
				NodeComparable<T> childBase;
//				key = child.getKeyIdentifier();
				childBase = nodeBase.getChildNCMostSimilarTo(child);
//				if (nodeBase.containsChildNC(key)) {
				if (childBase != null) {
					// recursion :D
					if (!nodesVisited.contains(childBase)) {
						BigInteger dissGot;
						dissGot = ((DefaultNodeComparable<T>) child).computeDissonanceAsBigInt_WithRecursion(childBase, //
								weights, //
								nodesVisited, //
								exponentialWeightDepth.multiply(weights.weightDepthBigInt));
						if (dissGot != BigInteger.ZERO)
							dissonance[0] = dissonance[0].add(dissGot);
					}
				} else {
					dissonance[0] = dissonance[0].add(exponentialWeightDepth.multiply(weights.weightMissingNodeBigInt));
				}
			});
			nodeBase.forEachChildNC(child -> {
//				T key;
//				key = child.getKeyIdentifier();
				if (!this.containsChildNC(child)) {
					BigInteger dissGot;
					dissGot = exponentialWeightDepth.multiply(weights.weightExceedingNodeBigInt);
					if (dissGot != BigInteger.ZERO)
						dissonance[0] = dissonance[0].add(dissGot);
				}
			});
			return dissonance[0];
		}

		@Override
		public String toString() {
			StringBuilder sb;
			sb = new StringBuilder();
			toString(sb);
			return sb.toString();
		}
	}

	public static class DefaultNodeComparable<T> extends NodeComparableDefaultAlghoritms<T> {
		private static final long serialVersionUID = 1L;
		protected T keyIdentifier;
		protected MapTreeAVL<NodeComparable<T>, NodeComparable<T>> backMap;
		protected SortedSetEnhanced<NodeComparable<T>> children;
		protected final Comparator<T> comparatorKey;

		public DefaultNodeComparable(T value, Comparator<T> comparatorKey) {
			super();
			this.comparatorKey = comparatorKey;
			this.keyIdentifier = value;
			this.backMap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, // comparatorKey
					NodeComparable.newNodeComparatorDefault(comparatorKey));
//			this.children = this.backMap.toSetValue(n -> n.getKeyIdentifier());
			this.children = this.backMap.toSetKey();
		}

		@Override
		public T getKeyIdentifier() { return this.keyIdentifier; }

		public void setKeyIdentifier(T value) { this.keyIdentifier = value; }

		@Override
		public Comparator<T> getKeyComparator() { return comparatorKey; }

		@Override
		public SortedSetEnhanced<NodeComparable<T>> getChildrenNC() { return this.children; }

		@Override
		public NodeComparable<T> getChildNCMostSimilarTo(NodeComparable<T> copy) { return this.backMap.get(copy); }

		@Override
		public void forEachChildNCFIFOOrdering(Consumer<NodeComparable<T>> action) {}
	}

	//

	public static class NodeComparatorDefault<T> implements Comparator<NodeComparable<T>> {
		protected final Comparator<T> keyComp;
		protected final Comparator<SortedSetEnhanced<NodeComparable<T>>> childrenSetComparator; // huuuuge generics :D

		public NodeComparatorDefault(Comparator<T> keyComp) {
//			this(keyComp, null); }
//
//		public NodeComparatorDefault(Comparator<T> keyComp, Comparator<SortedSetEnhanced<NodeComparable<T>>> c) {
			super();
			this.keyComp = keyComp;
			this.childrenSetComparator = /* c != null ? c : */ newChildrenSetComparator();
		}

		protected Comparator<SortedSetEnhanced<NodeComparable<T>>> newChildrenSetComparator() {
			return SortedSetEnhanced.COMPARATOR_FACTORY_PREFERRED.newComparator(this); // RECURSION
		}

		@Override
		public int compare(NodeComparable<T> n1, NodeComparable<T> n2) {
			int c;
			if (n1 == n2) { return 0; }
			if (n1 == null) { return -1; }
			if (n2 == null) { return 1; }
			c = keyComp.compare(n1.getKeyIdentifier(), n2.getKeyIdentifier());
			if (c != 0)
				return c;
			/*
			 * v1: iterate through children, as
			 * SortedSetEnhanced.ComparatorFactoriesSSE.KEY_ORDER.newComparator(this) does
			 */
			return this.childrenSetComparator.compare(n1.getChildrenNC(), n2.getChildrenNC());
		}
	}
}