package common.mainTools;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListModel;

import common.mainTools.dataStruct.MapTreeAVL;

/**
 * This class represent a {@link ListModel} (more precisely, a
 * {@link AbstractListModel}) of a generic <code>E</code> type that is mapped
 * through a second type <code>K</code>. The instances of <code>K</code> are the
 * keys for that mapping and can be obtained by the given "key extractor"
 * (implementation of {@link Function}&ltE,K>) passed as parameter
 */
public class JListModelTreeMap<K, E> extends AbstractListModel<E> implements Set<E> {
	private static final long serialVersionUID = -777775641098194444L;
	private static final int NEVER_STARTED_ITER = -1;

	public static <K, E> JListModelTreeMap<K, E> newInstance(MyComparator<K> comparatorKeys,
			Function<E, K> keyExtractor) {
		return new JListModelTreeMap<K, E>(comparatorKeys, keyExtractor);
	}

	public static <Same> JListModelTreeMap<Same, Same> newInstance(MyComparator<Same> comparatorKeys) {
		return newInstance(comparatorKeys, k -> k);
	}

	public static <MapTree extends SortedMap<K, E> & Iterable<K>, K, E> JListModelTreeMap<K, E> newInstance(
			MapTreeAVL<K, E> rbtree, Function<E, K> keyExtractor) {
		return new JListModelTreeMap<K, E>(rbtree, keyExtractor);
	}

	public static <Same, MapTree extends SortedMap<Same, Same> & Iterable<Same>> JListModelTreeMap<Same, Same> newInstance(
			MapTreeAVL<Same, Same> rbtree) {
		return newInstance(rbtree, k -> k);
	}

	protected JListModelTreeMap(Function<E, K> keyExtractor) {
		super();
		if (keyExtractor == null)
			throw new IllegalArgumentException("The key extractor (function transforming E to K) must not be null");
		this.keyExtractor = keyExtractor;
		// listDataListenerCollection = new LinkedList<>();
		this.iter = null;
		this.currentIndex = NEVER_STARTED_ITER;
	}

	protected JListModelTreeMap(MyComparator<K> comparatorKeys, Function<E, K> keyExtractor) {
		this(keyExtractor);
		if (comparatorKeys == null)
			throw new IllegalArgumentException("The comparator must not be null");
		this.delegate = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				(MyComparator<K>) (this.comparatorKeys = comparatorKeys));
		this.setEntryDelegate = this.delegate.entrySet();
	}

	@SuppressWarnings("unchecked")
	protected <MapTree extends SortedMap<K, E> & Iterable<K>> JListModelTreeMap(MapTreeAVL<K, E> rbtree,
			Function<E, K> keyExtractor) {
		this(keyExtractor);
		if (rbtree == null)
			throw new IllegalArgumentException("The given RBTree must not be null");
		this.delegate = rbtree;
		this.comparatorKeys = (Comparator<K>) rbtree.comparator();
		this.setEntryDelegate = rbtree.entrySet();
	}

	protected transient int currentIndex;
//	protected transient K currentKey;
//	protected transient E currentVal;
	protected transient // RedBlackTree<K, E>.RBTIterator
//	SortedMap<K, E>.IteratorAVL iter;
	Iterator<Entry<K, E>> iter;

	protected Comparator<K> comparatorKeys;
	protected Function<E, K> keyExtractor;
//	RedBlackTree
	// TreeAVL
//	<MapTree extends SortedMap<K, E>&Iterable<K>>
	protected MapTreeAVL<K, E> delegate;
	protected Set<Entry<K, E>> setEntryDelegate;

	//

	public Function<E, ?> getKeyExtractor() {
		return keyExtractor;
	}

	// public List<ListDataListener> getListDataListenerCollection() {return
	// listDataListenerCollection;}

	//

	public JListModelTreeMap<K, E> setKeyExtractor(Function<E, K> keyExtractor) {
		this.keyExtractor = keyExtractor;
		return this;
	}

	//

	@Override
	public int getSize() {
		return delegate.size();
	}

	protected void checkIterator() {
		if (iter == null || (!iter.hasNext()))
			resetIterator();
	}

	protected void resetIterator() {
		if (iter == null || currentIndex != NEVER_STARTED_ITER) {
			currentIndex = NEVER_STARTED_ITER;
			iter = setEntryDelegate.iterator();
//			currentKey = null;
//			currentVal = null;
		}
	}

	/**
	 * BEWARE: do NOT use because this method is NOT stateless : it MUST be used
	 * ONLY inside a {@link JList}.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public E getElementAt(int index) {
		Entry<K, E> e;
		e = null;
		checkIterator();
		System.out.println("currentIndex: " + currentIndex + ", index: " + index);
		if (index < 0 || index >= this.delegate.size())
			return null;
		if (index != currentIndex) {
			/**
			 * if (index == currentIndex + 1) { //<br>
			 * currentIndex++; //<br>
			 * iter.next(); //<br>
			 * // return iter.n.value; //<br>
			 * } else { //<br>
			 */
			if (index < currentIndex)
				resetIterator();
			System.out.println("AAAAAAA currentIndex: " + currentIndex + ", index: " + index);
			if (index > currentIndex) {
				do {
					e = iter.next();
					if (e == null)
						System.out.println("ERR e is null at " + (currentIndex + 1));
				} while (++currentIndex < index);
				System.out.println("currentthen Index: " + currentIndex);
			}
			// }
		}
		return e.getValue();
	}

	public E addElement(E e) {
		E prev;
		int index = delegate.size();
		prev = delegate.put(keyExtractor.apply(e), e);
		if (prev == null)
			fireIntervalAdded(this, index, index);
		else
			fireContentsChanged(this, index, index);
		return prev;
	}

	public List<E> addElements(Iterable<E> some) {
		return addElements(some, false);
	}

	public List<E> addElements(Iterable<E> some, boolean collectPreviousValues) {
		List<E> l;
		E prev;
		int index, size;
		if (some == null)
			return null;
		size = 0;
		index = delegate.size();
		l = collectPreviousValues ? new LinkedList<>() : null;
		for (E e : some) {
			prev = delegate.put(keyExtractor.apply(e), e);
			if (collectPreviousValues && prev != null)
				l.add(prev);
			size++;
		}
		fireContentsChanged(this, 0, index);
		fireIntervalAdded(this, index, index + size);
		return l;
	}

	public int indexOf(E elem) {
		boolean notFound;
		int index;
//		RedBlackTree<K, E>.RBTIterator
		Iterator<Entry<K, E>> iter;
		K keyElem;
		index = 0;
		notFound = true;
		keyElem = keyExtractor.apply(elem);
		iter = this.setEntryDelegate.iterator();
		checkIterator();
		while (iter.hasNext() && //
				(notFound = (elem == null ? (iter.next().getKey() != null)
						: (comparatorKeys.compare(keyElem, iter.next().getKey()) != 0))))
			index++;
		return notFound ? -1 : index;
	}

	public boolean removeElement(E elem) {
		boolean r;
		int index;
		if (listenerList.getListenerCount() > 0) {
			index = indexOf(elem);
			if (index >= 0) {
				delegate.remove(keyExtractor.apply(elem));
				resetIterator();
				fireIntervalRemoved(this, index, index);
				return true;
			}
			return false;
		}
		// no listener to be advised
		r = delegate.remove(keyExtractor.apply(elem)) != null;
		if (r)
			resetIterator();
		return r;
	}

	@Override
	public void clear() {
		int prevSize;
		if ((prevSize = size()) > 0) {
			delegate.clear();
			currentIndex = NEVER_STARTED_ITER;
			iter = null;
			fireIntervalRemoved(this, 0, prevSize);
		}
	}

	@Override
	public void forEach(Consumer<? super E> consumer) {
		forEach((k, e) -> consumer.accept(e));
	}

	public void forEach(BiConsumer<? super K, ? super E> doSome) {
		delegate.forEach(doSome);
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return delegate.containsKey(o);
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException("Too lazy to implement");
//		return null;
	}

	@Override
	public Object[] toArray() {
		return delegate.entrySet().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
//		return delegate.toArray(a);
		throw new UnsupportedOperationException("Too lazy to implement");
	}

	@Override
	public boolean add(E e) {
		return addElement(e) == null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o) {
		return removeElement(o);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsAll(Collection<?> c) {
		if (c != null && c.size() > 0) {
			for (Object o : c)
				if (!delegate.containsKey(keyExtractor.apply(o)))
					return false;
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean changed;
		if (c != null && c.size() > 0) {
			changed = false;
			for (Object o : c)
				changed |= add(o);
			return changed;
		}
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Retainer re;
		if (c != null && c.size() > 0 && size() > 0) {
			forEach(re = new Retainer(c));
			re.applyRetain();
			return re.changed;
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed;
		if (c != null && c.size() > 0) {
			changed = false;
			for (Object o : c)
				changed |= remove(o);
			return changed;
		}
		return false;
	}

	//

	// TODO CLASSES

	@Override
	public String toString() {
		return "JListModelRBTree [delegate=\n" + delegate + "\n]";
	}

	class Retainer implements BiConsumer<K, E> {
		boolean changed;
		LinkedList<E> toBeRemoved;
		Collection<?> c;

		public Retainer(Collection<?> c) {
			super();
			this.c = c;
			this.changed = false;
			this.toBeRemoved = new LinkedList<>();
		}

		@Override
		public void accept(K t, E e) {
			if (c.contains(e)) {
				changed = true;
				toBeRemoved.add(e);
			}
		}

		void applyRetain() {
			c = null;
			while (!toBeRemoved.isEmpty())
				removeElement(toBeRemoved.removeFirst());
		}
	}

	//

	public static void main(String[] args) {
		JListModelTreeMap<String, String> lm;
		String s;
		lm = JListModelTreeMap.newInstance(Comparators.STRING_COMPARATOR_2);
		lm.add("ciao");
		lm.add("stronzooo");
		lm.add(s = "capra");
		lm.add("allooooora");
		lm.add("helo");
		lm.add("moci");

		System.out.println("size:" + lm.size());
		System.out.println(lm);
		printIndex(lm, s);
		printIndex(lm, "moci");
		printIndex(lm, "moci");
		lm.add("stronzoo");
		lm.add("stronzo");
		System.out.println("....size:" + lm.size());
		System.out.println(lm);
		printIndex(lm, "stronzo");
		printIndex(lm, "moci");
		lm.add("strunz");
		System.out.println("......");
		System.out.println(lm);
		printIndex(lm, "stronzo");
		printIndex(lm, "moci");

		System.out.println("\n......\n");
		System.out.println("size: " + lm.getSize());
		System.out.println(lm.getElementAt(4));
		System.out.println(lm.getElementAt(lm.indexOf("moci")));
		System.out.println("\n......\n");

		lm.removeElement("stronzoo");
		System.out.println(lm);
		printIndex(lm, "stronzo");
		printIndex(lm, "moci");

		lm.removeElement("moci");
		System.out.println(lm);
		printIndex(lm, "stronzo");
		printIndex(lm, "moci");
	}

	static <K, E> void printIndex(JListModelTreeMap<K, E> lm, E s) {
		System.out.println("index of --" + s + "-- : " + lm.indexOf(s));
	}

}