package dataStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A circular linked list implementation of {@link List}, without any check
 * (also, size ones), that is easily reversibly iterated.
 */
public class LinkedListLightweight<T> implements List<T>, Serializable, Cloneable {
	private static final long serialVersionUID = -540642504852222L;

	public LinkedListLightweight() { this.clear(); }

	protected int size;
	protected CircularListNode<T> first, last;

	/** It will be a circular list */
	protected static class CircularListNode<E> implements Serializable, Cloneable {
		private static final long serialVersionUID = -LinkedListLightweight.serialVersionUID;
		protected E item;
		protected CircularListNode<E> prev, next;

		public CircularListNode(E item, CircularListNode<E> prev) {
			super();
			this.item = item;
			linkToPrev(prev);
		}

		protected void linkToPrev(CircularListNode<E> prev) {
			this.prev = prev;
			if (prev != null) {
				(this.next = prev.next).prev = this;
				prev.next = this;
			}
		}
	}

	//

	//

	//

	//

	protected void removeNode(CircularListNode<T> n) {
		if (n == null || isEmpty()) { return; }
		if (first == last) {
			if (first == n) {
				first = last = null;
				n.next = n.prev = null;
				size = 0;
			} else {
				throw new IllegalArgumentException("Not belonging node: " + n.item);
			}
			return;
		}
		if (first == n) { first = n.next; }
		if (last == n) { last = n.prev; }
		n.prev.next = n.next;
		n.next.prev = n.prev;
		n.next = n.prev = null;
		size--;
	}

	@Override
	public void forEach(Consumer<? super T> a) {
		if (a == null || isEmpty()) { return; }
		CircularListNode<T> iter, la;
		iter = la = this.first;
		do {
			a.accept(iter.item);
		} while ((iter = iter.next) != la);
	}

	public void forEachInOrder(Consumer<T> a) { forEach(a); }

	public void forEachReverseOrder(Consumer<T> a) {
		if (a == null || isEmpty()) { return; }
		CircularListNode<T> iter, la;
		iter = la = this.last;
		do {
			a.accept(iter.item);
		} while ((iter = iter.prev) != la);
	}

	public List<T> toList() {
		ArrayList<T> l;
		l = new ArrayList<>(size);
		forEachInOrder(l::add);
		return l;
	}

	protected static class CirculaLLLIterator<E> implements ListIterator<E> {
		protected final LinkedListLightweight<E> list;
		protected byte lastMovementOperation;
		protected int jumps;
		protected CircularListNode<E> node, last;

		public CirculaLLLIterator(LinkedListLightweight<E> list) {
			super();
			this.list = list;
			this.node = list.first;
			this.last = list.last;
			this.jumps = 0;
			this.lastMovementOperation = 0;
		}

		public CirculaLLLIterator(LinkedListLightweight<E> list, int index) {
			this(list);
			if (index < 0 || index >= list.size) { throw new IndexOutOfBoundsException(index); }
			while (--index >= 0) {
				next();
			}
		}

		@Override
		public boolean hasNext() {
			return this.node != null && (//
			this.jumps == 0 || this.node != this.last);
		}

		@Override
		public E next() {
			E e;
			if (!hasNext())
				return null;
			e = this.node.item;
			this.jumps++;
			this.node = this.node.next;
			if (this.jumps != 0 && this.node == this.last) { this.jumps = 0; }
			this.lastMovementOperation = 1;
			return e;
		}

		@Override
		public boolean hasPrevious() { return hasNext(); }

		@Override
		public E previous() {
			E e;
			if (!hasPrevious())
				return null;
			e = this.node.item;
			this.jumps--;
			this.node = this.node.prev;
			if (this.jumps != 0 && this.node == this.last) { this.jumps = 0; }
			this.lastMovementOperation = 2;
			return e;
		}

		@Override
		public int nextIndex() {
			return hasNext() ? //
					((this.jumps >= 0) ? (this.jumps + 1) : (list.size + this.jumps))//
					: this.list.size;
		}

		@Override
		public int previousIndex() {
			return hasPrevious() ? //
					((this.jumps >= 0) ? (this.jumps - 1) : ((list.size - this.jumps) - 1))//
					: -1;
		}

		@Override
		public void remove() {
			if (this.lastMovementOperation == 0) {
				throw new IllegalStateException("No next or previous already called");
			}
			list.removeNode(lastMovementOperation == 1 ? this.node.prev : this.node.next);
		}

		@Override
		public void set(E e) {
			if (this.lastMovementOperation == 0) {
				throw new IllegalStateException("No next or previous already called");
			}
			(lastMovementOperation == 1 ? this.node.prev : this.node.next).item = e;
		}

		@Override
		public void add(E e) { // copied from "add"
			CircularListNode<E> n;
			n = new CircularListNode<>(e, null);
			if (list.first == null) {
				list.first = list.last = n;
				n.next = n.prev = n;
				this.last = this.node = n;
				list.size = 1;
			} else {
				n.linkToPrev(this.node);
				list.size++;
			}
		}
	}

	//

	//

	// TODO INSTANCE METHODS

	//

	//

	@Override
	public int size() { return this.size; }

	@Override
	public void clear() {
		this.size = 0;
		this.first = this.last = null;
	}

	@Override
	public boolean add(T e) {
		CircularListNode<T> n;
		n = new CircularListNode<>(e, null);
		if (first == null) {
			this.first = this.last = n;
			n.next = n.prev = n;
			this.size = 1;
		} else {
			n.linkToPrev(last);
			last = n;
			this.size++;
		}
		return true;
	}

	@Override
	public boolean isEmpty() { return this.first == null; }

	@Override
	public boolean contains(Object o) {
		if (isEmpty()) { return false; }
		for (T t : this) {
			if (Objects.equals(t, o)) { return true; }
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() { return listIterator(); }

	@Override
	public Object[] toArray() { return toList().toArray(); }

	@Override
	public <G> G[] toArray(G[] a) { return toList().toArray(a); }

	@Override
	public boolean remove(Object o) {
		if (isEmpty()) { return false; }
		CircularListNode<T> iter, la;
		iter = la = this.first;
		do { // for each in order
			if (Objects.equals(iter.item, o)) {
				removeNode(iter);
				return true;
			}
		} while ((iter = iter.next) != la);
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) { return false; }

	@Override
	public boolean addAll(Collection<? extends T> c) { return toList().addAll(c); }

	@Override
	public boolean addAll(int index, Collection<? extends T> c) { return toList().addAll(index, c); }

	@Override
	public boolean removeAll(Collection<?> c) { return toList().removeAll(c); }

	@Override
	public boolean retainAll(Collection<?> c) { return toList().retainAll(c); }

	protected CircularListNode<T> nodeAt(int i) {
		if (i < 0 || i >= size) { throw new IndexOutOfBoundsException(i); }
		CircularListNode<T> iter;
		iter = this.first;
		if (i <= (size >> 1)) {
			while (--i >= 0) {
				iter = iter.next;
			}
		} else {
			int s = size;
			iter = this.last;
			while (++i < s) {
				iter = iter.prev;
			}
		}
		return iter;
	}

	@Override
	public T get(int index) {
		if (isEmpty()) { return null; }
		return nodeAt(index).item;
	}

	@Override
	public T set(int index, T element) {
		CircularListNode<T> n;
		T t;
		if (isEmpty()) { return null; }
		n = nodeAt(index);
		t = n.item;
		n.item = element;
		return t;
	}

	@Override
	public void add(int index, T element) {
		if (isEmpty()) {
			if (index == 0) {
				add(element);
				return;
			} else {
				throw new IndexOutOfBoundsException(index);
			}
		}
		if (index == 0) {
			CircularListNode<T> oldFirst, n;
			oldFirst = this.first;
			this.first = n = new CircularListNode<>(element, null);
			n.next = oldFirst;
			oldFirst.prev = ((n.prev = oldFirst.prev).next = n); // too optimized, look the next 2 statements
//			n.prev = oldFirst.prev;
//			oldFirst.prev = (oldFirst.prev.next = n);
		} else if (index == size) {
			add(element);
		} else {
			CircularListNode<T> n, newNode;
			n = nodeAt(index);
			newNode = new CircularListNode<>(element, null);
			(newNode.prev = n.prev).next = newNode;
			(n.prev = newNode).next = n;
		}
	}

	@Override
	public T remove(int index) {
		T e;
		CircularListNode<T> n;
		if (isEmpty()) { throw new NoSuchElementException(); }
		if (index < 0 || index >= this.size) { throw new IndexOutOfBoundsException(index); }
		if (index == 0) {
			n = this.first;
		} else if (index == (this.size - 1)) {
			n = this.last;
		} else {
			n = nodeAt(index);
		}
		e = n.item;
		removeNode(n);
		return e;
	}

	@Override
	public int indexOf(Object o) {
		int i, k;
		CircularListNode<T> iter, la;
		if (isEmpty())
			return -1;
		k = -1;
		iter = la = this.first;
		i = 0;
		do { // for each in order
			if (Objects.equals(iter.item, o)) {
				k = i;
			} else {
				i++;
			}
		} while (k == -1 && (iter = iter.next) != la);
		return k;
	}

	@Override
	public int lastIndexOf(Object o) {
		int i, k;
		CircularListNode<T> iter, la;
		if (isEmpty())
			return -1;
		k = -1;
		iter = la = this.last;
		i = 0;
		do { // for each in inverse order
			if (Objects.equals(iter.item, o)) {
				k = i;
			} else {
				i++;
			}
		} while (k == -1 && (iter = iter.prev) != la);
		return k;
	}

	@Override
	public ListIterator<T> listIterator() { return new CirculaLLLIterator<>(this); }

	@Override
	public ListIterator<T> listIterator(int index) { return new CirculaLLLIterator<>(this, index); }

	@Override
	public List<T> subList(int fromIndex, int toIndex) { return this.toList().subList(fromIndex, toIndex); }
}