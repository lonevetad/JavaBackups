package dataStructures;

import tools.EditDistance;

/**
 * Defines the costs of a set of operations (insertion, deletion, substitution,
 * etc if any).
 * <P>
 * Mainly used in {@link EditDistance}.
 *
 * @author ottin
 *
 * @param <T>
 */
public interface EditCosts<T> {

	public long insertion(T element);

	public long deletion(T element);

	public long substitution(T previous, T newOne);

	//

	public static <T> EditCosts<T> newDefaultCosts() {
		return new EditCosts<>() {
			@Override
			public long insertion(T element) { return 1; }

			@Override
			public long deletion(T element) { return 1; }

			@Override
			public long substitution(T previous, T newOne) { return 1; }
		};
	}

	/**
	 * Auxiliary class, to transform methods like
	 * {@link AlteringCosts#insertion(Object)} and
	 * {@link AlteringCosts#deletion(Object)} into functional interfaces.
	 */
	public static interface ActionCost<K> {
		public long getCost(K element);
	}
}
