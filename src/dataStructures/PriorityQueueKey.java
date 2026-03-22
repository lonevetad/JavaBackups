package dataStructures;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Data structure implementing the "priority queue" concept over the "Key" type
 * (the second generic type), instances that are mapped by the "ValueHoldingKey"
 * type (the first generic type).<br>
 * Things can get confusing, so let's have a look of a priority queue's summary
 * and the two problems solved by this class:
 * <ol>
 * <li>Usually, a simple priority queue holds just one type of element (for
 * example: numbers) and is implemented as a "Min-Heap", that is a support data
 * structure, providing the two basic operations: add an element and remove the
 * head of the queue (i.e.: the lowest element). {@link java.util.PriorityQueue}
 * implements this basic idea.
 * <p>
 * A priority queue like that is usually enough for the majority of tasks, but
 * an additional operation could be required: the alteration of a value.
 * Usually, this is called "decrease key", which lower a given existing value to
 * another given value. In general, it alters that value, increasing or
 * decreasing it, no matter. Obviously, to alter a value a look up operation is
 * required.<br>
 * This operation could be easily performed combining the previous two: remove
 * the value, alter it and then add the resulting one. An optimized version
 * could be useful, but it's not a big problem indeed
 * <p>
 * Currently (26/02/2019), in implementations like Java's ones a "MinHeap" is
 * used, as said before, but since the MinHeap it's implemented using an array
 * and the values held are not sorted in any way, then the search procedure has
 * a complexity of <i>O(N)</i>, where "N" is the size of the queue. That's
 * because it has no way to discard some elements (like a binary-search does in
 * a sorted array) and so needs to run all over the array to find that value.
 * <p>
 * Some algorithms, like Dijkstra's shortest path or "minimum spanning tree"
 * (M.S.T.) algorithms, requires a well performing "decrease key" operation (in
 * general, it could be named "modify key" or "alter key"), i.e. having a
 * complexity of <i>O(log2(N))</i>, but that implementation is linear so the
 * total complexity and the performance could explode.</li>
 * <li><!-- As said before, a simple priority queue holds and manage a single
 * value.<br>
 * --> What if that value is sorted just only through a part of it (like a
 * priority-measure of a task, or a arc's weight in a graph for a MST or
 * shortest path) instead of the whole value?<br>
 * Also, could the performance be enhanced using the order property of that
 * value's part?<br>
 * The answer is: yes, but a {@link java.util.Comparator} is required.</li>
 * </ol>
 * <p>
 * So, the idea is:
 * <ul>
 * <li>Make a priority queue of a type "Key" (the second generic type)</li>
 * <li>That type's instances are a part of, or the whole, "ValueHoldingKey"
 * instances (the first generic type)</li>
 * <li>Use "ValueHoldingKey" to look up the "Key" to be modified on the
 * operation "alter key". (The "value" will be used as a key to find the "key";
 * it's confusing in terminology, I'm sorry, further explanation will be
 * provided.)</li>
 * <li>Use a modified Binary Search Tree to tune the look up operation, required
 * in "alter key", and guarantee a head's peek or retrieve operation to be both
 * O(1).</li>
 * </ul>
 * <p>
 * How to use it?
 * <ul>
 * <li>Upon adding an element, add both the "key" and the "value".</li>
 * <li>Upon removing the head of the queue or a specific element (found through
 * a "value", a "ValueHoldingKey" instance), return a
 * {@link java.util.Map.Entry} of the associated pair, if any.</li>
 * <li>Upon altering a "key", use "value" to look for a "key" and silently
 * replace it with another one given as parameter.</li>
 * </ul>
 * Alteration of the "value"'s "key" could be performed at any time in the
 * user's code: in fact, the previous value of the "key" will NOT be used
 * because the "value" is used instead for the search operation.
 * <p>
 * What if the "key" and the "value" would be the same type, for instance
 * {@link Integer}?<br>
 * No problem: simply define both type as the desired type, like
 * {@code PriorityQueueKey<Integer,Integer> p}.
 */
public class PriorityQueueKey<ValueHoldingKey, Key> implements Serializable, Iterable<Entry<ValueHoldingKey, Key>> //
{ // AbstractPriorityQueue<ValueHoldingKey, Key> // SortedSet<K>
	private static final long serialVersionUID = 8967362530519483L;

//	/**
//	 * Specify the action to do if the key the user is tryiing to insert is yet
//	 * present.
//	 */
//	public static enum BehaviourOnKeyCollision {
//		KeepPrevious, Replace/* , AddItsNotASet it's senseless */
//	}
//
//	public static final BehaviourOnKeyCollision DEFAULT_BEHAVIOUR = BehaviourOnKeyCollision.Replace;

//

//

//	public PriorityQueueKey(Comparator<ValueHoldingKey> valueHoldingKeyComparator, Comparator<Key> keyComparator) {
//		this(valueHoldingKeyComparator, keyComparator, DEFAULT_BEHAVIOUR);
//	}

	public PriorityQueueKey(Comparator<ValueHoldingKey> valueHoldingKeyComparator, Comparator<Key> keyComparator,
			Function<ValueHoldingKey, Key> keyExtractor) {
		this(valueHoldingKeyComparator, keyComparator, MapTreeAVL.DEFAULT_BEHAVIOUR, keyExtractor);
	}

//	public PriorityQueueKey(Comparator<ValueHoldingKey> valueHoldingKeyComparator, Comparator<Key> keyComparator,
//			BehaviourOnKeyCollision b) {
//		this(valueHoldingKeyComparator, keyComparator, b, null);
//	}

	public PriorityQueueKey(Comparator<ValueHoldingKey> valueHoldingKeyComparator, Comparator<Key> keyComparator,
			MapTreeAVL.BehaviourOnKeyCollision b, Function<ValueHoldingKey, Key> keyExtractor) {

		if (keyExtractor == null)
			throw new IllegalArgumentException("The key extractor cannot be null");
		if (keyComparator == null)
			throw new IllegalArgumentException("The key Comparator cannot be null");
		if (valueHoldingKeyComparator == null)
			throw new IllegalArgumentException("The valueHoldingKey Comparator cannot be null");
		if (b == null)
			throw new IllegalArgumentException("The behaviour cannot be null");
		this.keyExtractor = keyExtractor;
		this.compKey = keyComparator;
		this.compValueHoldingKey = valueHoldingKeyComparator;
		this.behaviour = b;
		this.compEntry_KeySorter = (e1, e2) -> {
			int ck;
			ValueHoldingKey vhk1, vhk2;
			Key k1, k2;
			if (e1 == e2)
				return 0;
			if (e1 == null)
				return -1;
			if (e2 == null)
				return 1;
			k1 = e1.getValue();
			k2 = e2.getValue();
			vhk1 = e1.getKey();
			vhk2 = e2.getKey();
			if (k1 == null && k2 == null && vhk1 == null && vhk2 == null)
				return 0;
			ck = this.compKey.compare(k1, k2);
			if (ck != 0)
				return ck;
			return // this.comparator()
			this.compValueHoldingKey.compare(vhk1, vhk2);
		};

		this.entryFetcher = MapTreeAVL.newMap(//
				MapTreeAVL.Optimizations.Lightweight, //
				b, compValueHoldingKey);
		// new MapTreeAVL<>(b, compValueHoldingKey);
		this.keys = MapTreeAVL.newMap(//
				MapTreeAVL.Optimizations.MinMaxIndexIteration, //
				b, compEntry_KeySorter);
		// new MapTreeAVL<>(b, compEntry_KeySorter);
	}

	private final Function<ValueHoldingKey, Key> keyExtractor;
	private final Comparator<Key> compKey;
	private final Comparator<ValueHoldingKey> compValueHoldingKey;
	private final MapTreeAVL.BehaviourOnKeyCollision behaviour;
	private final SortedMap<ValueHoldingKey, Entry<ValueHoldingKey, Key>> entryFetcher;
	private final MapTreeAVL<Entry<ValueHoldingKey, Key>, Key> keys;
//
//private final Function<ValueHoldingKey, Key> keyExtractor;
	private final Comparator<Entry<ValueHoldingKey, Key>> compEntry_KeySorter; // compEntry

	/** Debug only */
	public MapTreeAVL<Entry<ValueHoldingKey, Key>, Key> getKeys() { return keys; }

	//

	public int size() { return entryFetcher.size(); }

	public boolean isEmpty() { return entryFetcher.isEmpty(); }

	public void clear() {
		entryFetcher.clear();
		keys.clear();
	}

	public Entry<ValueHoldingKey, Key> get(Object key) { return entryFetcher.get(key); }

	public Key put(ValueHoldingKey key) { return put(new EntryReturned(key, this.keyExtractor.apply(key))); }

	protected Key put(Entry<ValueHoldingKey, Key> e) {
		Key prevVal;
		Entry<ValueHoldingKey, Key> prev;
		if (e == null)
			return null;
		prevVal = null;
		prev = entryFetcher.get(e.getKey());
		if (prev != null) {
			if (this.behaviour == MapTreeAVL.BehaviourOnKeyCollision.Replace) {
				prevVal = prev.getValue();
				entryFetcher.remove(e.getKey());
				entryFetcher.put(e.getKey(), prev);
				keys.remove(prev);
				keys.put(e, this.keyExtractor.apply(e.getKey()));
				assert (entryFetcher.size() == keys.size()) : "After replacing size inconsistent: entries: "
						+ entryFetcher.size() + ", keys: " + keys.size();
				return prevVal;
			} else
				return null;
		}
		entryFetcher.put(e.getKey(), e);
		keys.put(e, e.getValue());
		if (entryFetcher.size() != keys.size()) {
			throw new IllegalStateException("Mapping of keys and values have different sizes: <" + keys.size() + "; "
					+ entryFetcher.size() + ">\n\tOne or more comparators are build in a wrong way");
		}
		return null;
	}

	public Entry<ValueHoldingKey, Key> remove(Object valueHolding) {
		Entry<ValueHoldingKey, Key> prev;
		prev = entryFetcher.get(valueHolding);
		if (prev == null)
			return null;
		entryFetcher.remove(valueHolding);
		keys.remove(prev);
		return prev;
	}

	@Override
	public Iterator<Entry<ValueHoldingKey, Key>> iterator() {
		return this.keys.iteratorKey(); // new IteratorEntryPQK();
	}

	public Comparator<? super ValueHoldingKey> comparator() { return compValueHoldingKey; }

//

	@Override
	public void forEach(Consumer<? super Entry<ValueHoldingKey, Key>> action) {
		this.keys.forEach((e, k) -> action.accept(e));
//		this.entryFetcher.forEach(action);
	}

	/**
	 * Retrieves but not removes the minimum value in this priority.<br>
	 * This method does not alter the collection, rather than
	 * {@link #removeMinimum()}.
	 */
	public Entry<ValueHoldingKey, Key> peekMinimum() { return keys.isEmpty() ? null : keys.peekMinimum().getKey(); }

	/**
	 * Retrieves but not removes the maximum value in this priority.<br>
	 * This method does not alter the collection, rather than
	 * {@link #removeMaximum()}.
	 * <p>
	 * Optional method because this is a <strong>minimum</strong> priority
	 * queue.<br>
	 * By defaults it returns {@code null}.
	 */
	public Entry<ValueHoldingKey, Key> peekMaximum() { return keys.isEmpty() ? null : keys.peekMaximum().getKey(); }

	/**
	 * Retrieves but not removes the maximum value in this priority.<br>
	 * This method does not alter the collection, rather than
	 * {@link #removeMaximum()}.
	 * <p>
	 * Optional method because this is a <strong>minimum</strong> priority
	 * queue.<br>
	 * By defaults it returns {@code null}.
	 */
	public Entry<ValueHoldingKey, Key> removeMinimum() {
		Entry<ValueHoldingKey, Key> e;
		e = this.peekMinimum();
		if (e == null)
			return null;
		this.keys.remove(e);
		this.entryFetcher.remove(e.getKey());
		return e;
	}

	/**
	 * Retrieves AND removes the maximum value in this priority.<br>
	 * This method alters the collection, rather than {@link #peekMaximum()}.
	 * {@link #removeMaximum()}.
	 * <p>
	 * Optional method because this is a <strong>minimum</strong> priority
	 * queue.<br>
	 * By defaults it returns {@code null}.
	 */
	public Entry<ValueHoldingKey, Key> removeMaximum() {
		Entry<ValueHoldingKey, Key> e;
		e = this.peekMaximum();
		if (e == null)
			return null;
		this.keys.remove(e);
		this.entryFetcher.remove(e.getKey());
		return e;
	}

	public ValueHoldingKey firstKey() {
//		return this.entryFetcher.;
		return ((MapTreeAVL<ValueHoldingKey, Entry<ValueHoldingKey, Key>>) entryFetcher).peekMinimum().getKey();
	}

	public ValueHoldingKey lastKey() {
		return ((MapTreeAVL<ValueHoldingKey, Entry<ValueHoldingKey, Key>>) entryFetcher).peekMaximum().getKey();
	}

	/**
	 * Returns an array of {@link java.util.Map.Entry} of both generics of this
	 * class.
	 */
	public Object[] toArray() {
		Object[] a;
		int[] index;
		/*
		 * no way to change a local variable on lambda expressions: all variables must
		 * be final, so use a array wrapping the integer
		 */
		a = new Object[this.size()];
		index = new int[] { 0 };
		this.keys.forEach(e -> { a[index[0]++] = e; });
		return a;
	}

	@Override
	public String toString() { return keys.toString(); }

	// TODO METHOD FROM ABSTRACT PRIORITY QUEUE

	public Comparator<ValueHoldingKey> getComparatorValueHoldingKey() { return this.compValueHoldingKey; }

	public Comparator<Key> getComparatorKey() { return this.compKey; }

	/**
	 * A function calculating the priority queue's "Key" given a "ValueHoldingKey".
	 * Used in methods like {@link #put(Object)}.
	 */
	public Function<ValueHoldingKey, Key> getKeyExtractor() { return this.keyExtractor; }

	public Entry<ValueHoldingKey, Key> alterKey(ValueHoldingKey key, Consumer<ValueHoldingKey> alterator) {
		Entry<ValueHoldingKey, Key> e, newe;
		Key k;
		e = // entryFetcher.
				get(key);
		if (e == null)
			return null;
		entryFetcher.remove(key);
		keys.remove(e);
		alterator.accept(key);
		newe = new EntryReturned(key, k = this.keyExtractor.apply(key));
		entryFetcher.put(key, newe);
		keys.put(newe, k);
		return e;
	}

	//

	public Set<Entry<ValueHoldingKey, Key>> entrySet() { return new SortedSetPQK(); }

	public boolean containsKey(Object key) { return this.entryFetcher.containsKey(key); }

	public boolean containsValue(Object value) { return this.keys.containsValue(value); }

	public void putAll(Map<? extends ValueHoldingKey, ? extends Key> m) {
		if (m != null)
			m.forEach((v, k) -> put(new EntryReturned(v, k)));
	}

	public SortedMap<ValueHoldingKey, Key> subMap(ValueHoldingKey fromKey, ValueHoldingKey toKey) {
		throw new UnsupportedOperationException("Operation not allowed");
	}

	public SortedMap<ValueHoldingKey, Key> headMap(ValueHoldingKey toKey) {
		throw new UnsupportedOperationException("Operation not allowed");
	}

	public SortedMap<ValueHoldingKey, Key> tailMap(ValueHoldingKey fromKey) {
		throw new UnsupportedOperationException("Operation not allowed");
	}

	public Set<ValueHoldingKey> keySet() { throw new UnsupportedOperationException("Operation not allowed"); }

	public Collection<Key> values() { throw new UnsupportedOperationException("Operation not allowed"); }

	public boolean containsAll(Collection<?> c) {
		for (Entry<ValueHoldingKey, Key> e : this)
			if (!this.containsKey(e.getKey()))
				return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean addAll(Collection<?> c) {
		if (c == null)
			return false;
		c.forEach(e -> {
			if ((e instanceof Entry<?, ?>) && (e != null))
				put((Entry<ValueHoldingKey, Key>) e);
		});
		return true;
	}

	public boolean retainAll(Collection<?> c) {
		QueueLightweight<Entry<ValueHoldingKey, Key>> l;
		if (c == null)
			return false;
		l = new QueueLightweight<>();
		this.forEach(e -> {
			if (!c.contains(e))
				l.add(e);
		});
		for (Entry<ValueHoldingKey, Key> e : l)
			remove(e);
		return true;
	}

	public boolean removeAll(Collection<?> c) {
		QueueLightweight<Object> l;
		if (c == null)
			return false;
		l = new QueueLightweight<>();
		c.forEach(e -> l.add(e));
		for (Object o : l)
			remove(o);
		return true;
	}

	//

	// TODO CLASSES

	protected class EntryReturned implements Entry<ValueHoldingKey, Key> {
		private Key key;
		private ValueHoldingKey value;

		EntryReturned(ValueHoldingKey v, Key k) {
			this.key = k;
			this.value = v;
		}

		@Override
		public ValueHoldingKey getKey() { return value; }

		@Override
		public Key getValue() { return key; }

		@Override
		public Key setValue(Key key) {
//			Key v = this.key;
//			this.key = key;
//			return v;
			throw new UnsupportedOperationException("SetValue not allowed");
		}

		@Override
		public String toString() { return "[k: " + key + " -> vhk: " + value + "]"; }
	}

	protected class SortedSetPQK implements SortedSet<Entry<ValueHoldingKey, Key>> {

		@Override
		public int size() { return PriorityQueueKey.this.size(); }

		@Override
		public void clear() { PriorityQueueKey.this.clear(); }

		@Override
		public boolean isEmpty() { return PriorityQueueKey.this.isEmpty(); }

		@Override
		public boolean contains(Object o) { return PriorityQueueKey.this.containsKey(o); }

		@Override
		public Iterator<Entry<ValueHoldingKey, Key>> iterator() { return PriorityQueueKey.this.iterator(); }

		@Override
		public boolean add(Entry<ValueHoldingKey, Key> e) {
			return PriorityQueueKey.this.put(new EntryReturned(e.getKey(), e.getValue())) == null;
		}

		@Override
		@SuppressWarnings("unchecked")

		public boolean remove(Object o) {
			return PriorityQueueKey.this.remove(((Entry<ValueHoldingKey, Key>) o).getKey()) != null;
		}

		@Override
		public Entry<ValueHoldingKey, Key> first() { return PriorityQueueKey.this.peekMinimum(); }

		@Override
		public Entry<ValueHoldingKey, Key> last() { return PriorityQueueKey.this.peekMaximum(); }

		@Override
		public Object[] toArray() { return PriorityQueueKey.this.toArray(); }

		@Override
		public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException("Operation not allowed"); }

		@Override
		public boolean containsAll(Collection<?> c) { return PriorityQueueKey.this.containsAll(c); }

		@Override
		public boolean addAll(Collection<? extends Entry<ValueHoldingKey, Key>> c) {
			return PriorityQueueKey.this.addAll(c);
		}

		@Override
		public boolean retainAll(Collection<?> c) { return PriorityQueueKey.this.retainAll(c); }

		@Override
		public boolean removeAll(Collection<?> c) { return PriorityQueueKey.this.removeAll(c); }

		@Override
		public Comparator<? super Entry<ValueHoldingKey, Key>> comparator() {
			return PriorityQueueKey.this.compEntry_KeySorter;
		}

		@Override
		public SortedSet<Entry<ValueHoldingKey, Key>> subSet(Entry<ValueHoldingKey, Key> fromElement,
				Entry<ValueHoldingKey, Key> toElement) {
			throw new UnsupportedOperationException("Operation not allowed");
		}

		@Override
		public SortedSet<Entry<ValueHoldingKey, Key>> headSet(Entry<ValueHoldingKey, Key> toElement) {
			throw new UnsupportedOperationException("Operation not allowed");
		}

		@Override
		public SortedSet<Entry<ValueHoldingKey, Key>> tailSet(Entry<ValueHoldingKey, Key> fromElement) {
			throw new UnsupportedOperationException("Operation not allowed");
		}

	}

}