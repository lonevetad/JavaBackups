package dataStructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class HeapFibonacci<T> implements Collection<T> {
	public static final boolean DEFAULT_CACHE_NODES_BY_DATA_HASH = true;
	public static final int DEFAULT_CACHE_NODES_HASH_MAP_SIZE = 128;

	public HeapFibonacci(Comparator<T> comparator) {
		this(comparator, DEFAULT_CACHE_NODES_BY_DATA_HASH);
	}

	public HeapFibonacci(Comparator<T> comparator, boolean cacheNodesByDataHash) {
		this(comparator, cacheNodesByDataHash ? DEFAULT_CACHE_NODES_HASH_MAP_SIZE : -1);
	}

	public HeapFibonacci(Comparator<T> comparator, int cacheNodesHashMapSize) {
		super();
		Objects.requireNonNull(comparator);
		this.comparator = comparator;
		this.size = 0;
		this.roots = null;
		this.nodesByData = cacheNodesHashMapSize > 2 ? (new HashMap<>(cacheNodesHashMapSize)) : null;
	}

	protected int size;
	protected final Map<T, LinkedList<NodeHF<T>>> nodesByData;
	protected NodeHF<T> min, roots; // roots == linked list, due to "previous; next" pointers; acts as "last in
									// list"
	protected final Comparator<T> comparator;

	// TODO protected methods

	protected static int bitsAmount(int n) {
		int r = 0, pow = 1;
		if (n < 0) {
			return 0;
		}
		if (n <= 1) {
			return 1;
		}
		while (n >= pow) {
			pow <<= 1;
			r++;
		}
		return r;
	}

	protected void addToCache(NodeHF<T> n) {
		final LinkedList<NodeHF<T>> list;

		if (this.nodesByData == null) {
			return;
		}

		if (this.nodesByData.containsKey(n.data)) {
			list = this.nodesByData.get(n.data);
		} else {
			list = new LinkedList<>();
			this.nodesByData.put(n.data, list);
		}
		if (!list.contains(n)) {
			list.addLast(n);
		}
	}

	protected void removeFromCache(NodeHF<T> n) {
		final LinkedList<NodeHF<T>> list;

		if (this.nodesByData == null) {
			return;
		}

		list = this.nodesByData.get(n.data);
		if (list == null) {
			return;
		}
		list.remove(n);
		if (list.isEmpty()) {
			this.nodesByData.remove(n.data);
		}
	}

	protected NodeHF<T> newNode(T data) {
		return new NodeHF<T>(this, data);
	}

	protected NodeHF<T> linearSearch(T data, Predicate<NodeHF<T>> tester, NodeHF<T> startingNode) { // depth-first
																									// search
		NodeHF<T> iter, recursionNode;
		if (startingNode == null) {
			return null;
		}
		iter = startingNode;
		do {
			if (tester.test(iter)) {
				return iter;
			}
			recursionNode = this.linearSearch(data, tester, iter.lastChild);
			if (recursionNode != null) {
				return recursionNode;
			}
		} while ((iter = iter.next) != startingNode);
		return null;
	}

	protected NodeHF<T> nodeOf(T data) {
		NodeHF<T> nIter, root;
		LinkedList<NodeHF<T>> list;
		Iterator<NodeHF<T>> iter;

		root = this.roots;
		if (root == null) {
			return null;
		}
		if (this.nodesByData == null) {
			return this.linearSearch( //
					data, //
					n -> (n.data == data || this.comparator.compare(n.data, data) == 0) //
					, root //
			);
		}

		// use the hashmap

		if (!nodesByData.containsKey(data)) {
			return null;
		}
		list = nodesByData.get(data);
		nIter = null;
		iter = list.iterator();
		while (nIter == null && iter.hasNext()) {
			nIter = iter.next(); // hypotesis
			if (nIter.data != data) { // false, no exact match
				nIter = null;
			}
		}
		iter = null;
		if (nIter == null) {
			nIter = list.get(0);
		}
		return nIter;
	}

	protected void addToRoots(NodeHF<T> n) {
		NodeHF<T> r;

		r = this.roots;
		n.unlinkHorizontally();
		this.roots = n;
		n.father = null;
//		n.degree = 0;

		if (r == null) {
			n.next = n.previous = n;
			this.min = n;
			return;
		}
		r.linkAfter(n);

		if (this.comparator.compare(n.data, this.min.data) < 0) {
			this.min = n;
		}
	}

	protected NodeHF<T> mergeSubtrees(NodeHF<T> n1, NodeHF<T> n2) {
		if (this.comparator.compare(n1.data, n2.data) < 0) {
			n1.addChild(n2);
			return n1;
		} else {
			n2.addChild(n1);
			return n2;
		}
	}

	public void cleanUp() {
		this.cleanUp(null);
	}

	@SuppressWarnings("unchecked")
	public void cleanUp(NodeHF<T> removedNode) {
		boolean hasMoreNodes;
		int max_degrees;
		NodeHF<T> nIter, sameDegreeNode, nextNode;
		Object[] degrees; // pointers to NodeHF<T>

		if (this.roots == null || this.min == null || this.roots.next == this.roots) { // 0 or 1 element
			return;
		}

		if (removedNode != null) {
			// due to a previous "extract min" or remove
			removedNode.unlinkHorizontally();
			removedNode.father = null;
		}

		max_degrees = 1 + bitsAmount(this.size);
		degrees = new Object[max_degrees];
		Arrays.fill(degrees, null);

		nIter = this.roots.next;
		this.roots = null;

		hasMoreNodes = true;
		do {
			nextNode = nIter.next;
			hasMoreNodes = nIter != nextNode;

			nIter.father = null; // just in case
			nIter.unlinkHorizontally();

			while (degrees[nIter.childrenAmount] != null) {

				sameDegreeNode = (NodeHF<T>) degrees[nIter.childrenAmount];
				degrees[nIter.childrenAmount] = null;

				nIter = mergeSubtrees(sameDegreeNode, nIter);
			}

			degrees[nIter.childrenAmount] = nIter;

			nIter = nextNode;
		} while (hasMoreNodes);

		this.min = null;
		for (int i = 0; i < max_degrees; i++) {
			if (degrees[i] != null) {
				NodeHF<T> n = (NodeHF<T>) degrees[i];
				addToRoots(n);
			}
		}
	}

	protected void decreaseKey(NodeHF<T> n) {
		n.cutOut();
	}

	protected void increaseKey(NodeHF<T> n) {
//		n.cutOut();
		NodeHF<T> firstChild, iteratorChild, lowestChild;

		boolean notFinished;
		// swap with the minimum child, recursively

		notFinished = n.lastChild != null;
		while (notFinished) {
			notFinished = n.lastChild != null;

			if (notFinished) {
				firstChild = iteratorChild = lowestChild = n.lastChild.next; // start with the first chronologically

				// searching the minimum
				do {
					if (this.comparator.compare(iteratorChild.data, lowestChild.data) < 0) {
						lowestChild = iteratorChild;
					}
				} while ((iteratorChild = iteratorChild.next) != firstChild);
				if (this.comparator.compare(n.data, lowestChild.data) < 0) {
					T tempData;
					// swap data
					tempData = n.data;
					n.data = lowestChild.data;
					lowestChild.data = tempData;

					lowestChild = n;
				} else {
					notFinished = false;
				}
			}
		}
	}

	//

	// TODO public methods

	//

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.roots == null || this.size == 0;
	}

	public T getMin() {
		return this.min == null ? null : this.min.data;
	}

	@Override
	public boolean add(T arg0) {
		return this.insert(arg0) != null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	public NodeHF<T> insert(T data) {
		NodeHF<T> n;
		n = newNode(data);
		addToRoots(n);
		this.size++;

		addToCache(n);

		return n;
	}

	@SuppressWarnings("unchecked")
	public T extractMin() {
		NodeHF<T> m, firstChild;
		if (this.min == null) {
			return null;
		}
		m = this.min;

		if (this.roots == this.roots.next && this.roots.lastChild == null) {
			// just one node, simple case
			this.roots = null;
			this.min = null;
			this.size = 0;
			m.father = null;
			m.unlinkHorizontally();
			return m.data;
		}

		if (m == this.roots) {
			this.roots = m.next;
		}

		firstChild = m.lastChild;
		if (firstChild != null) {
			// v2
			// m.forEachChild(this::addToRoots);
			// v3
			@SuppressWarnings("rawtypes")
			NodeHF[] children;
			final int[] index = { 0 };
			children = new NodeHF[m.childrenAmount];
			m.forEachChild(c -> children[index[0]++] = c);
			for (@SuppressWarnings("rawtypes")
			NodeHF child : children) {
				this.addToRoots(child);
			}
		}

		this.removeFromCache(m);

		this.cleanUp(m);

		this.size--;

		return m.data;
	}

	public void alterKey(NodeHF<T> nodeToDecrease, Function<T, T> alterDataAction) {
		int comparisonAlteration;
		T data;

		if (alterDataAction == null) {
			return;
		}

		data = nodeToDecrease.data;
		nodeToDecrease.data = alterDataAction.apply(data);

		comparisonAlteration = this.comparator.compare(data, nodeToDecrease.data);
		if (comparisonAlteration > 0) {
			this.decreaseKey(nodeToDecrease);
		} else {
			this.increaseKey(nodeToDecrease);
		}
	}

	public void alterKey(T data, Function<T, T> alterDataAction) {
		NodeHF<T> n;

		if (alterDataAction == null) {
			return;
		}
		n = this.nodeOf(data);
		this.alterKey(n, alterDataAction);
	}

	/**
	 * Merge the given heap into this one, emptying the given one.
	 */
	public void merge(HeapFibonacci<T> h) {
		if (h == null || h.roots == null) {
			return;
		}

		if (h.roots == null) {
			this.roots = h.roots;
			this.min = h.min;
			this.size = h.size;
			h.nodesByData.forEach((d, l) -> l.forEach(this::addToCache));
			h.roots = h.min = null;
			h.size = 0;
			h.nodesByData.clear();
			return;
		}

		h.forEachRoots(this::addToRoots);

		this.size += h.size;
		h.nodesByData.clear();
	}

	public void forEachRoots(Consumer<NodeHF<T>> action) {
		NodeHF<T> nIter, firstRoot;
		nIter = this.roots;
		if (nIter == null || action == null) {
			return;
		}
		nIter = firstRoot = nIter.next; // since the nodes are pushed in a stack-like
		do {
			action.accept(nIter);
		} while ((nIter = nIter.next) != firstRoot);
	}

	/**
	 * Based on {@link #iteratorNode()}, so it's not "fast fail-safe" with
	 * "modifications count": do not modify the heap while iterating
	 *
	 *
	 * @return
	 */
	@Override
	public Iterator<T> iterator() {
		return new Iterator<>() {
			final Iterator<NodeHF<T>> iter = iteratorNode();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public T next() {
				NodeHF<T> n = iter.next();
				return n == null ? null : n.data;
			}
		};
	}

	/***
	 * Warning: no "fast fail-safe" with "modifications count": do not modify the
	 * heap while iterating
	 *
	 * @return
	 */
	public Iterator<NodeHF<T>> iteratorNode() {
		return new Iterator<>() {
			boolean rootNotConsumed = roots != null;
			// since the nodes are pushed in a stack-like
			final NodeHF<T> r = roots != null ? roots.next : null;
			NodeHF<T> nIter = roots != null ? roots.next : null;

			@Override
			public boolean hasNext() {
				return nIter != r || rootNotConsumed;
			}

			@Override
			public NodeHF<T> next() {
				NodeHF<T> current;
				if (!hasNext()) {
					return null;
				}
				rootNotConsumed = false;
				current = nIter;
				nIter = nIter.next;
				return current;
			}

		};
	}

	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		if (arg0 == null) {
			return false;
		}
		if (arg0.isEmpty()) {
			return true;
		}
		boolean[] ret = { true };
		arg0.forEach(t -> {
			ret[0] &= this.add(t);
		});
		return ret[0];
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		if (arg0 == null) {
			return false;
		}
		if (arg0.isEmpty()) {
			return true;
		}
		boolean[] ret = { true };
		arg0.forEach(t -> {
			ret[0] &= this.contains(t);
		});
		return ret[0];
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		if (arg0 == null) {
			return false;
		}
		if (arg0.isEmpty()) {
			return true;
		}
		boolean[] ret = { true };
		arg0.forEach(t -> {
			ret[0] &= this.remove(t);
		});
		return ret[0];
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException("too lazy to implement retainAll");
	}

	@Override
	public <E> E[] toArray(E[] arg0) {
		throw new UnsupportedOperationException("too lazy to implement toArray<T>");
	}

	// TODO classes

	public static class NodeHF<E> {
//		protected Long id;
		protected boolean marked;
		protected int childrenAmount; // == degree
		protected E data;
		protected final HeapFibonacci<E> heap;
		protected NodeHF<E> father, previous, next, lastChild;

		protected NodeHF(HeapFibonacci<E> heap, E data) {
			super();
			this.heap = heap;
			this.data = data;
			this.marked = false;
			this.childrenAmount = 0;
			this.father = null;
			this.lastChild = null;
			this.previous = this.next = this;
		}

		/**
		 * @return the data
		 */
		public E getData() {
			return data;
		}

		/**
		 * @return the heap
		 */
		public HeapFibonacci<E> getHeap() {
			return heap;
		}

		/**
		 * @return the childrenAmount
		 */
		public int getChildrenAmount() {
			return this.childrenAmount;
		}

		public boolean isARoot() {
			return this.father == null;
		}

		// protected boolean canBeMarked() { return this.father != null; }

		/**
		 * @return the previous
		 */
		public NodeHF<E> getPrevious() {
			return previous;
		}

		/**
		 * @return the next
		 */
		public NodeHF<E> getNext() {
			return next;
		}

		/**
		 * @return the lastChild
		 */
		public NodeHF<E> getLastChild() {
			return lastChild;
		}

		protected void unlinkHorizontally() {
			if (this.previous != null) {
				this.previous.next = this.next;
			}
			if (this.next != null) {
				this.next.previous = this.previous;
			}
			this.previous = this.next = this; // keep the "this" for the doubly linked list
		}

		/***
		 * Link the provided node after this one.
		 */
		protected void linkAfter(NodeHF<E> n) {
			n.previous = this;
			n.next = this.next;
			if (this.next != null) {
				this.next.previous = n;
			}
			// close the link forwardly
			this.next = n;
		}

		protected void addChild(NodeHF<E> n) {
			n.father = this;
			if (this.lastChild == null) {
				this.childrenAmount = 1;
				this.lastChild = n;
				return;
			}
			n.unlinkHorizontally();
			this.lastChild.linkAfter(n);
			this.lastChild = n;
			this.childrenAmount++;
		}

		protected void cutOut() {
			NodeHF<E> f;
			f = this.father;
			this.marked = false;
			if (f == null) {
				return;
			}
			if (f.lastChild == this) {
				if (this.previous == this) {
					// I'm the only child here
					f.lastChild = null;
				} else {
					f.lastChild = this.previous;
				}
			}
			// else: TODO remove from father child ????
			if (f.childrenAmount-- <= 1) {
				f.childrenAmount = 0;
			}
			if (f.marked) {
				this.heap.addToRoots(this);
				f.cutOut();
			} else {
				if (f.father != null) {
					f.marked = true;
				}
				this.heap.addToRoots(this);
			}
		}

		public void forEachChild(Consumer<NodeHF<E>> action) {
			NodeHF<E> nIter, firstChild;
			nIter = this.lastChild;
			if (nIter == null || action == null) {
				return;
			}
			nIter = firstChild = nIter.next; // since the nodes are pushed in a stack-like
			do {
				action.accept(nIter);
			} while ((nIter = nIter.next) != firstChild);
		}
	}

}
