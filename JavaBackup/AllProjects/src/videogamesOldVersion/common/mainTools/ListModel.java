package common.mainTools;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractListModel;

/** Copied from {@link java.swing.DefaultListModel}. */
public class ListModel<E> extends AbstractListModel<E> {
	private static final long serialVersionUID = -2306541818707L;

	public ListModel() {
		this(new LinkedList<>());
	}

	public ListModel(List<E> delegate) {
		this.delegate = delegate;
	}

	private List<E> delegate = null;

	public ListModel<E> setDelegate(List<E> delegate) {
		if (delegate == null) throw new IllegalArgumentException("Cannot set delegate as a null list");
		if (this.delegate != null && (!this.delegate.isEmpty())) // clear();
			fireIntervalRemoved(this, 0, this.delegate.size() - 1);
		this.delegate = delegate;
		if (!delegate.isEmpty()) fireIntervalAdded(this, 0, delegate.size() - 1);
		return this;
	}

	/**
	 * Returns the number of components in this list.
	 * <p>
	 * This method is identical to <code>size</code>, which implements the
	 * <code>List</code> interface defined in the 1.2 Collections framework.
	 * This method exists in conjunction with <code>setSize</code> so that
	 * <code>size</code> is identifiable as a JavaBean property.
	 *
	 * @return the number of components in this list
	 * @see #size()
	 */
	@Override
	public int getSize() {
		return delegate.size();
	}

	/**
	 * Returns the component at the specified index. <blockquote> <b>Note:</b>
	 * Although this method is not deprecated, the preferred method to use is
	 * <code>get(int)</code>, which implements the <code>List</code> interface
	 * defined in the 1.2 Collections framework. </blockquote>
	 *
	 * @param index
	 *            an index into this list
	 * @return the component at the specified index
	 * @exception ArrayIndexOutOfBoundsException
	 *                if the <code>index</code> is negative or greater than the
	 *                current size of this list
	 * @see #get(int)
	 */
	@Override
	public E getElementAt(int index) {
		return delegate.get(index);
	}

	/**
	 * Returns the number of components in this list.
	 *
	 * @return the number of components in this list
	 * @see Vector#size()
	 */
	public int size() {
		return delegate.size();
	}

	/**
	 * Tests whether this list has any components.
	 *
	 * @return <code>true</code> if and only if this list has no components,
	 *         that is, its size is zero; <code>false</code> otherwise
	 * @see Vector#isEmpty()
	 */
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	/**
	 * Tests whether the specified object is a component in this list.
	 *
	 * @param elem
	 *            an object
	 * @return <code>true</code> if the specified object is the same as a
	 *         component in this list
	 * @see Vector#contains(Object)
	 */
	public boolean contains(Object elem) {
		return delegate.contains(elem);
	}

	/**
	 * Searches for the first occurrence of <code>elem</code>.
	 *
	 * @param elem
	 *            an object
	 * @return the index of the first occurrence of the argument in this list;
	 *         returns <code>-1</code> if the object is not found
	 * @see Vector#indexOf(Object)
	 */
	public int indexOf(Object elem) {
		return delegate.indexOf(elem);
	}

	/**
	 * Returns the index of the last occurrence of <code>elem</code>.
	 *
	 * @param elem
	 *            the desired component
	 * @return the index of the last occurrence of <code>elem</code> in the
	 *         list; returns <code>-1</code> if the object is not found
	 * @see Vector#lastIndexOf(Object)
	 */
	public int lastIndexOf(Object elem) {
		return delegate.lastIndexOf(elem);
	}

	/**
	 * Returns the component at the specified index. Throws an
	 * <code>ArrayIndexOutOfBoundsException</code> if the index is negative or
	 * not less than the size of the list. <blockquote> <b>Note:</b> Although
	 * this method is not deprecated, the preferred method to use is
	 * <code>get(int)</code>, which implements the <code>List</code> interface
	 * defined in the 1.2 Collections framework. </blockquote>
	 *
	 * @param index
	 *            an index into this list
	 * @return the component at the specified index
	 * @see #get(int)
	 * @see java.util.List#get(int)
	 */
	public E elementAt(int index) {
		return delegate.get(index);
	}

	/**
	 * Returns the first component of this list. Throws a
	 * <code>NoSuchElementException</code> if this vector has no components.
	 *
	 * @return the first component of this list
	 * @see Vector#firstElement()
	 */
	public E firstElement() {
		return delegate.get(0);
	}

	/**
	 * Returns the last component of the list. Throws a
	 * <code>NoSuchElementException</code> if this vector has no components.
	 *
	 * @return the last component of the list
	 * @see Vector#lastElement()
	 */
	public E lastElement() {
		return delegate.get(delegate.size() - 1);
	}

	/**
	 * Sets the component at the specified <code>index</code> of this list to be
	 * the specified element. The previous component at that position is
	 * discarded.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is
	 * invalid. <blockquote> <b>Note:</b> Although this method is not
	 * deprecated, the preferred method to use is <code>set(int,Object)</code>,
	 * which implements the <code>List</code> interface defined in the 1.2
	 * Collections framework. </blockquote>
	 *
	 * @param element
	 *            what the component is to be set to
	 * @param index
	 *            the specified index
	 * @see #set(int,Object)
	 * @see Vector#setElementAt(Object,int)
	 */
	public void setElementAt(E element, int index) {
		delegate.set(index, element);
		fireContentsChanged(this, index, index);
	}

	/**
	 * Deletes the component at the specified index.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is
	 * invalid. <blockquote> <b>Note:</b> Although this method is not
	 * deprecated, the preferred method to use is <code>remove(int)</code>,
	 * which implements the <code>List</code> interface defined in the 1.2
	 * Collections framework. </blockquote>
	 *
	 * @param index
	 *            the index of the object to remove
	 * @see #remove(int)
	 * @see Vector#removeElementAt(int)
	 */
	public void removeElementAt(int index) {
		delegate.remove(index);
		fireIntervalRemoved(this, index, index);
	}

	/**
	 * Inserts the specified element as a component in this list at the
	 * specified <code>index</code>.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is
	 * invalid. <blockquote> <b>Note:</b> Although this method is not
	 * deprecated, the preferred method to use is <code>add(int,Object)</code>,
	 * which implements the <code>List</code> interface defined in the 1.2
	 * Collections framework. </blockquote>
	 *
	 * @param element
	 *            the component to insert
	 * @param index
	 *            where to insert the new component
	 * @exception ArrayIndexOutOfBoundsException
	 *                if the index was invalid
	 * @see #add(int,Object)
	 * @see Vector#insertElementAt(Object,int)
	 */
	public void insertElementAt(E element, int index) {
		delegate.add(index, element);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Adds the specified component to the end of this list.
	 *
	 * @param element
	 *            the component to be added
	 * @see Vector#addElement(Object)
	 */
	public void addElement(E element) {
		int index = delegate.size();
		delegate.add(element);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument from this
	 * list.
	 *
	 * @param obj
	 *            the component to be removed
	 * @return <code>true</code> if the argument was a component of this list;
	 *         <code>false</code> otherwise
	 * @see Vector#removeElement(Object)
	 */
	public boolean removeElement(Object obj) {
		int index = indexOf(obj);
		boolean rv = delegate.remove(obj);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return rv;
	}

	/**
	 * Removes all components from this list and sets its size to zero.
	 * <blockquote> <b>Note:</b> Although this method is not deprecated, the
	 * preferred method to use is <code>clear</code>, which implements the
	 * <code>List</code> interface defined in the 1.2 Collections framework.
	 * </blockquote>
	 *
	 * @see #clear()
	 * @see Vector#removeAllElements()
	 */
	public void removeAllElements() {
		int index1 = delegate.size() - 1;
		delegate.clear();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
	}

	/**
	 * Returns a string that displays and identifies this object's properties.
	 *
	 * @return a String representation of this object
	 */
	@Override
	public String toString() {
		return delegate.toString();
	}

	/*
	 * The remaining methods are included for compatibility with the Java 2
	 * platform Vector class.
	 */

	/**
	 * Returns an array containing all of the elements in this list in the
	 * correct order.
	 *
	 * @return an array containing the elements of the list
	 * @see Vector#toArray()
	 */
	public Object[] toArray() {
		return delegate.toArray();
	}

	/**
	 * Returns the element at the specified position in this list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out
	 * of range (<code>index &lt; 0 || index &gt;= size()</code>).
	 *
	 * @param index
	 *            index of element to return
	 */
	public E get(int index) {
		return delegate.get(index);
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out
	 * of range (<code>index &lt; 0 || index &gt;= size()</code>).
	 *
	 * @param index
	 *            index of element to replace
	 * @param element
	 *            element to be stored at the specified position
	 * @return the element previously at the specified position
	 */
	public E set(int index, E element) {
		E rv = delegate.get(index);
		delegate.set(index, element);
		fireContentsChanged(this, index, index);
		return rv;
	}

	/**
	 * Inserts the specified element at the specified position in this list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out
	 * of range (<code>index &lt; 0 || index &gt; size()</code>).
	 *
	 * @param index
	 *            index at which the specified element is to be inserted
	 * @param element
	 *            element to be inserted
	 */
	public void add(int index, E element) {
		delegate.add(index, element);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Removes the element at the specified position in this list. Returns the
	 * element that was removed from the list.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out
	 * of range (<code>index &lt; 0 || index &gt;= size()</code>).
	 *
	 * @param index
	 *            the index of the element to removed
	 * @return the element previously at the specified position
	 */
	public E remove(int index) {
		E rv = delegate.get(index);
		delegate.remove(index);
		fireIntervalRemoved(this, index, index);
		return rv;
	}

	/**
	 * Removes all of the elements from this list. The list will be empty after
	 * this call returns (unless it throws an exception).
	 */
	public void clear() {
		int index1 = delegate.size() - 1;
		delegate.clear();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
	}

	/**
	 * Deletes the components at the specified range of indexes. The removal is
	 * inclusive, so specifying a range of (1,5) removes the component at index
	 * 1 and the component at index 5, as well as all components in between.
	 * <p>
	 * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index was
	 * invalid. Throws an <code>IllegalArgumentException</code> if
	 * <code>fromIndex &gt; toIndex</code>.
	 *
	 * @param fromIndex
	 *            the index of the lower end of the range
	 * @param toIndex
	 *            the index of the upper end of the range
	 * @see #remove(int)
	 */
	public void removeRange(int fromIndex, int toIndex) {
		if (fromIndex > toIndex) {
			throw new IllegalArgumentException("fromIndex must be <= toIndex");
		}
		for (int i = toIndex; i >= fromIndex; i--) {
			delegate.remove(i);
		}
		fireIntervalRemoved(this, fromIndex, toIndex);
	}

	/*
	 * public void addAll(Collection c) { }
	 *
	 * public void addAll(int index, Collection c) { }
	 */
}