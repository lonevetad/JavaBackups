package geometry;

import java.util.List;

public interface PathOptimizer<E> {

	public static interface CollinearCalculator<T> {
		public boolean areCollinear(T pfirst, T psecond, T pthird);
	}

	public List<E> optimizePath(List<E> pathList);
}