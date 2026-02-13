package tools;

import java.util.List;

import dataStructures.EditCosts;

public interface EditDistance {
//	public static interface EqualityChecker<T> {
//		public boolean isEqual(T e1, T e2);
//
//		public static <K> EqualityChecker<K> fromComparator(Comparator<K> comp) {
//			return (e1, e2) -> comp.compare(e1, e2) == 0;
//		}
//	}

	public default <T> long editDistance(T[] firstSequence, T[] secondSequence) {
		return editDistance(firstSequence, secondSequence, EditCosts.newDefaultCosts());
	}

//	/**
//	 * If the access is of the given {@link List} does NOT offer good <i>random access</i> performances, then
//	 * {@link #editDistance(Object[], Object[], EqualityChecker)} should be used
//	 * instead.
//	 */
	public default <T> long editDistance(List<T> firstSequence, List<T> secondSequence) {
		return editDistance(firstSequence, secondSequence, EditCosts.newDefaultCosts());
	}

	public default <T> long editDistance(List<T> firstSequence, List<T> secondSequence, EditCosts<T> editCosts) {
		return editDistance(IterableSized.from(firstSequence), IterableSized.from(secondSequence));
	}

	public default <T> long editDistance(IterableSized<T> firstSequence, IterableSized<T> secondSequence) {
		return editDistance(firstSequence, secondSequence, EditCosts.newDefaultCosts());
	}

	public default long editDistance(String firstSequence, String secondSequence) {
		return editDistance(firstSequence, secondSequence, EditCosts.newDefaultCosts());
	}

	public default long editDistance(String firstSequence, String secondSequence, EditCosts<Byte> cac) {
		int i;
		final Byte[] c1, c2;
		i = 0;
		c1 = new Byte[i = firstSequence.length()];
		for (byte b : firstSequence.getBytes()) {
			c1[i++] = Byte.valueOf(b);
		}
		i = 0;
		c2 = new Byte[i = secondSequence.length()];
		for (byte b : secondSequence.getBytes()) {
			c2[i++] = Byte.valueOf(b);
		}
		return editDistance(c1, c2, cac);
	}

	//

	public <T> long editDistance(T[] firstSequence, T[] secondSequence, EditCosts<T> cac);

	public <T> long editDistance(IterableSized<T> firstSequence, IterableSized<T> secondSequence, EditCosts<T> cac);
}