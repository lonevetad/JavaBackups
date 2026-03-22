package tools.predicatesExpressions;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import tools.predicatesExpressions.filters.AndPredicate;
import tools.predicatesExpressions.filters.NotPredicate;
import tools.predicatesExpressions.filters.OrPredicate;
import tools.predicatesExpressions.filters.XnorPredicate;
import tools.predicatesExpressions.filters.XorPredicate;

public interface MyPredicate<E> extends Predicate<E>, Serializable {

	/**
	 * Query the given collection of Something extracting those who are accepted by
	 * {@code this} instance, <b>hoping</b> that the returning
	 * {@link java.util.List} actually behaves as a {@link java.util.Set} (even if
	 * not implementing it): no checks are performed to avoid duplicated values to
	 * not degrade performance.
	 */
	public default List<E> query(Iterable<E> coll) {
		List<E> l;
		if (coll == null // || coll.isEmpty()
		)
			return null;
		l = new LinkedList<>();
		// coll.forEach((i, ato) -> {
		for (E ato : coll)//
			if (test(ato))
				l.add(ato);
		// });
		return l;
	}

	/**
	 * Query the given collection of something "E" returning a {@link java.util.Set}
	 * of "E" accepted by {@code this} instance.
	 */
	public default Set<E> query(Iterable<E> coll, Comparator<E> comp) {
		Set<E> set;
		if (coll == null || comp == null)
			return null;
		set = MapTreeAVL.newMap(// MapTreeAVL.Optimizations.MinMaxIndexIteration,
				comp).toSetKey();
		for (E ato : coll)//
			if (test(ato))
				set.add(ato);
		// });
		return set;
	}

	///

	//

	public static <T> MyPredicate<T> not(MyPredicate<T> filter) {
		return new NotPredicate<T>(filter);
	}

	public static <T> MyPredicate<T> and(MyPredicate<T> filter1, MyPredicate<T> filter2) {
		return new AndPredicate<T>(filter1, filter2);
	}

	public static <T> MyPredicate<T> or(MyPredicate<T> filter1, MyPredicate<T> filter2) {
		return new OrPredicate<T>(filter1, filter2);
	}

	public static <T> MyPredicate<T> xor(MyPredicate<T> filter1, MyPredicate<T> filter2) {
		return new XorPredicate<T>(filter1, filter2);
	}

	public static <T> MyPredicate<T> xnor(MyPredicate<T> filter1, MyPredicate<T> filter2) {
		return new XnorPredicate<T>(filter1, filter2);
	}

	public static <T> MyPredicate<T> equality(MyPredicate<T> filter1, MyPredicate<T> filter2) {
		return xnor(filter1, filter2);
	}

	public static <T> MyPredicate<T> disequality(MyPredicate<T> filter1, MyPredicate<T> filter2) {
		return xor(filter1, filter2);
	}
}