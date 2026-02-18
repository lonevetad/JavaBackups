package tools;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public interface CloserGetter<K> extends Serializable {
	/**
	 * Default implementation of {@link #getCloserTo(Object, Object, Object)} given
	 * a way to compute its difference.<br>
	 * In case of tie, the first element should be returned.
	 */
	public static <T> T getCloserTo(T target, DifferenceCalculator<T> diffCalc, T option1, T option2) {
		long d1, d2;
		d1 = diffCalc.getDifference(target, option1);
		d2 = diffCalc.getDifference(target, option2);
//		return d1 <= d2 ? option1 : option2;
		/*
		 * turn them to negative to: 1) take advantage of the extra bit for a double
		 * interval of values -> do not risk overflow 2) make the Different parameters'
		 * order indifferent
		 */
		if (d1 > 0) { d1 = -d1; }
		if (d2 > 0) { d2 = -d2; }
		return d1 >= d2 ? option1 : option2;
	}

	// and a way to compare its class
	/**
	 * No null check performed.<br>
	 * Given a value (first parameter), check which one of two (last) values is the
	 * other two value is closer to the given (first) one.<br>
	 * In case of tie, the first element should be returned.
	 * <p>
	 * May invoke {@link #getCloserTo(Object, DifferenceCalculator, Object, Object)}
	 * by providing a well designed {@link DifferenceCalculator}.
	 */
	public K getCloserTo(K target, K option1, K opt2);

	/**
	 * Iterate over the {@link Iterator} to search for the closest value to the
	 * given one (first parameter).
	 */
	public default K getCloserTo(K target, Iterator<K> iter) {
		K nearest;
		nearest = null;
		if (iter.hasNext()) {
			nearest = iter.next();
			while (iter.hasNext()) {
				nearest = getCloserTo(target, nearest, iter.next());
			}
		}
		return nearest;
	}

	/**
	 * See {@link #getCloserTo(Object, Iterator)}, but iterating over a
	 * {@link Collection} instead.
	 */
	@SuppressWarnings("unchecked")
	public default K getCloserTo(K target, Collection<K> c) {
		boolean[] flag = { true };
		Object[] notYetCheckedAnElement = { null };
		if (!c.isEmpty()) {
			c.forEach(k -> {
				if (flag[0]) {
					flag[0] = false;
					notYetCheckedAnElement[0] = k;
				} else {
					notYetCheckedAnElement[0] = getCloserTo(target, notYetCheckedAnElement[0], k);
				}
			});
		}
		return (K) notYetCheckedAnElement[0];
	}
}