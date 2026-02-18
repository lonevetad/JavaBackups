package dataStructures.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

import dataStructures.graph.PathGraph.PathStep;
import tools.NumberManager;

/**
 * TODO: mettere a disposizione l'iterazione dei vari step e nacondere la
 * {@link java.util.List} sottostante.
 *
 * @author lonevetad
 *
 * @param <E>
 */
public class PathGraph<E, Distance> implements List<PathStep<E, Distance>>, Serializable {
	private static final long serialVersionUID = 804954560330304L;

	public PathGraph(NumberManager<Distance> distanceManager) {
		distanceTotal = null;
		path = new LinkedList<>();
		this.distanceManager = distanceManager;
	}

	private Distance distanceTotal;
	private final NumberManager<Distance> distanceManager;
	// The first step has 0 distance
	private LinkedList<PathStep<E, Distance>> path;

	//

	public Distance getDistanceTotal() {
		return distanceTotal;
	}

	public List<PathStep<E, Distance>> getPath() {
		return path;
	}

	protected void setDistanceTotal(Distance distanceTotal) {
		this.distanceTotal = distanceTotal;
	}

	public NumberManager<Distance> getDistanceManager() {
		return distanceManager;
	}

	public List<E> toListNodes() {
		List<E> l;
		l = new ArrayList<>(path.size());
		for (PathStep<E, Distance> p : path)
			l.add(p.elem);
		return l;
	}

	//

	public void addStep(E e, Distance distance) {
//		if (distance >= 0) {
		this.distanceTotal = distanceManager.getAdder().apply(distanceTotal, distance);
		this.path.addFirst(new PathStep<E, Distance>(e, distance));
//		}
	}

	public void setStartStep(E e) {
		if (!Objects.equals(this.path.getFirst().elem, e))
			this.path.addFirst(new PathStep<E, Distance>(e, distanceManager.getZeroValue()));
	}

	public void forEachStep(Consumer<E> elementConsumer) {
		this.forEachStep((e, d) -> elementConsumer.accept(e));
	}

	public void forEachStep(BiConsumer<E, Distance> elementDistanceConsumer) {
		if (this.path != null && (!this.path.isEmpty()))
			this.path.forEach(ps -> elementDistanceConsumer.accept(ps.elem, ps.distFromPrevious));
	}

	@Override
	public String toString() {
		int i;
		StringBuilder sb;
		sb = new StringBuilder(1024);
		sb.append("Path long ").append(distanceTotal).append(": [\n");
		i = 0;
		for (PathStep<E, Distance> p : path) {
			sb.append(i++).append(") ").append(p.elem).append(", far: ").append(p.distFromPrevious).append('\n');
		}
		return sb.append(']').toString();
	}

	//

	@Override
	public boolean isEmpty() {
		return path.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return path.contains(o);
	}

	@Override
	public Iterator<PathStep<E, Distance>> iterator() {
		return path.iterator();
	}

	@Override
	public int size() {
		return path.size();
	}

	@Override
	public boolean add(PathStep<E, Distance> e) {
		return path.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return path.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return path.containsAll(c);
	}

	@Override
	public ListIterator<PathStep<E, Distance>> listIterator() {
		return path.listIterator();
	}

	@Override
	public boolean addAll(Collection<? extends PathStep<E, Distance>> c) {
		return path.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return path.removeAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends PathStep<E, Distance>> c) {
		return path.addAll(index, c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return path.retainAll(c);
	}

	@Override
	public void clear() {
		path.clear();
	}

	@Override
	public PathStep<E, Distance> get(int index) {
		return path.get(index);
	}

	@Override
	public PathStep<E, Distance> set(int index, PathStep<E, Distance> element) {
		return path.set(index, element);
	}

	@Override
	public void add(int index, PathStep<E, Distance> element) {
		path.add(index, element);
	}

	@Override
	public List<PathStep<E, Distance>> subList(int fromIndex, int toIndex) {
		return path.subList(fromIndex, toIndex);
	}

	@Override
	public PathStep<E, Distance> remove(int index) {
		return path.remove(index);
	}

	@Override
	public <T> T[] toArray(IntFunction<T[]> generator) {
		return path.toArray(generator);
	}

	@Override
	public int indexOf(Object o) {
		return path.indexOf(o);
	}

	@Override
	public boolean equals(Object o) {
		return path.equals(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return path.lastIndexOf(o);
	}

	@Override
	public void replaceAll(UnaryOperator<PathStep<E, Distance>> operator) {
		path.replaceAll(operator);
	}

	@Override
	public ListIterator<PathStep<E, Distance>> listIterator(int index) {
		return path.listIterator(index);
	}

	@Override
	public Object clone() {
		return path.clone();
	}

	@Override
	public Object[] toArray() {
		return path.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return path.toArray(a);
	}

	//

	//

	public static class PathStep<T, D> {
		D distFromPrevious;
		T elem;

		PathStep(T e, D d) {
			this.elem = e;
			this.distFromPrevious = d;
		}
	}

}