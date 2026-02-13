package common.mainTools;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListModel;

import common.removed.TreeAVL;

/**
 * This class represent a {@link ListModel} (more precisely, a
 * {@link AbstractListModel}) of a generic <code>E</code> type that is mapped
 * through a second type <code>K</code>. The instances of <code>K</code> are the
 * keys for that mapping and can be obtained by the given "key extractor"
 * (implementation of {@link Function}&ltE,K>) passed as parameter
 */
public class JListModelRBTree<K, E> extends AbstractListModel<E> implements Set<E> {
	private static final long serialVersionUID = -777775641098194444L;

	public static <K, E> JListModelRBTree<K, E> newInstance(MyComparator<K> comparatorKeys,
			Function<E, K> keyExtractor) {
		return new JListModelRBTree<K, E>(comparatorKeys, keyExtractor);
	}

	public static <Same> JListModelRBTree<Same, Same> newInstance(MyComparator<Same> comparatorKeys) {
		return newInstance(comparatorKeys, k -> k);
	}

	public static <MapTree extends SortedMap<K, E> & Iterable<K>, K, E> JListModelRBTree<K, E> newInstance(
			MapTree rbtree, Function<E, K> keyExtractor) {
		return new JListModelRBTree<K, E>(rbtree, keyExtractor);
	}

	public static <Same, MapTree extends SortedMap<Same, Same> & Iterable<Same>> JListModelRBTree<Same, Same> newInstance(
			MapTree rbtree) {
		return newInstance(rbtree, k -> k);
	}

	protected JListModelRBTree(Function<E, K> keyExtractor) {
		super();
		if (keyExtractor == null)
			throw new IllegalArgumentException("The key extractor (function transforming E to K) must not be null");
		this.keyExtractor = keyExtractor;
		// listDataListenerCollection = new LinkedList<>();
		currentIndex = -1;
	}

	protected JListModelRBTree(MyComparator<K> comparatorKeys, Function<E, K> keyExtractor) {
		this(keyExtractor);
		if (comparatorKeys == null)
			throw new IllegalArgumentException("The comparator must not be null");
		this.delegate = new TreeAVL<K, E>((MyComparator<K>) (this.comparatorKeys = comparatorKeys));
	}

	@SuppressWarnings("unchecked")
	protected <MapTree extends SortedMap<K, E> & Iterable<K>> JListModelRBTree(MapTree rbtree,
			Function<E, K> keyExtractor) {
		this(keyExtractor);
		if (rbtree == null)
			throw new IllegalArgumentException("The given RBTree must not be null");
		this.delegate = rbtree;
		this.comparatorKeys = rbtree.comparator();
	}

	protected transient int currentIndex;
	protected transient // RedBlackTree<K, E>.RBTIterator
//	SortedMap<K, E>.IteratorAVL iter;
	Iterator<K> iter;
	Comparator<K> comparatorKeys;
	Function<E, K> keyExtractor;
//	RedBlackTree
	// TreeAVL
//	<MapTree extends SortedMap<K, E>&Iterable<K>>
	SortedMap<K, E> delegate;

	//

	public Function<E, ?> getKeyExtractor() {
		return keyExtractor;
	}

	// public List<ListDataListener> getListDataListenerCollection() {return
	// listDataListenerCollection;}

	//

	public JListModelRBTree<K, E> setKeyExtractor(Function<E, K> keyExtractor) {
		this.keyExtractor = keyExtractor;
		return this;
	}

	//

	@Override
	public int getSize() {
		return delegate.size();
	}

	protected void checkIterator() {
		if (iter == null)
			resetIterator();
	}

	@SuppressWarnings("unchecked")
	protected void resetIterator() {
		if (currentIndex != 0) {
			currentIndex = 0;
			iter = ((Set<K>) delegate).iterator();
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
		checkIterator();
		if (index == 0)
			resetIterator();
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
			if (index > currentIndex) {
				do {
					iter.next();
				} while (++currentIndex < index);
			}
			// }
		}
		return iter.current.v;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int indexOf(E elem) {
		boolean notFound;
		int index;
//		RedBlackTree<K, E>.RBTIterator
		Iterator<K> iter;
		K keyElem;
		index = 0;
		notFound = true;
		keyElem = keyExtractor.apply(elem);
		iter = ((Set<E>) delegate).iterator();
		while (iter.hasNext() && //
				(notFound = (elem == null ? (iter.n.keyIdentifier != null)
						: (!(comparatorKeys.compare(keyElem, iter.next()) == 0))))/**/)
			index++;
		return notFound ? -1 : index;
	}

	public boolean removeElement(E elem) {
		boolean r;
		int index;
		if (listenerList.getListenerCount() > 0) {
			index = indexOf(elem);
			if (index >= 0) {
				delegate.delete(keyExtractor.apply(elem));
				resetIterator();
				fireIntervalRemoved(this, index, index);
				return true;
			}
			return false;
		}
		// no listener to be advised
		r = delegate.delete(keyExtractor.apply(elem)) != null;
		if (r)
			resetIterator();
		return r;
	}

	@Override
	public void clear() {
		int prevSize;
		if ((prevSize = size()) > 0) {
			delegate.clear();
			currentIndex = -1;
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
		return delegate.toArray();
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
		JListModelRBTree<String, String> lm;
		String s;
		lm = JListModelRBTree.newInstance(Comparators.STRING_COMPARATOR_2);
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

	static <K, E> void printIndex(JListModelRBTree<K, E> lm, E s) {
		System.out.println("index of --" + s + "-- : " + lm.indexOf(s));
	}

}