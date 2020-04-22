package geometry.pointTools.impl;

import java.awt.Point;
import java.util.Set;
import java.util.function.Predicate;

public interface ObjCollector<E> extends PointConsumerRestartable {

	public Predicate<E> getTargetsFilter();

	public E getAt(Point location);

	public Set<E> getCollectedObjects();

	@Override
	public default void restart() {
		this.getCollectedObjects().clear();
	}

	@Override
	public default void accept(Point location) {
		E n;
		n = this.getAt(location);
		if (getTargetsFilter().test(n))
			getCollectedObjects().add(n);
	}
}