package games.generic.controlModel.misc;

import tools.ObjectNamedID;

/**
 * Identify an object that is unique through its index (beware: it's NOT an
 * "ID"!) and its name. <br>
 * See {@link #getIndex()} and {@link ObjectNamedID} for more details.
 */
public interface IndexableObject extends ObjectNamedID {
	/**
	 * It differs substantially by {@link #getID()}: this is an <b>index</b>, not an
	 * <i>ID</i>, and it's defined in a greater environment (for instance,
	 * {@link Enum}'s {@link Enum#ordinal()})
	 */
	public int getIndex();

	/**
	 * Returns an instance of {@link IndexToObjectBackmapping}.
	 *
	 * @return an instance of {@link IndexToObjectBackmapping}.
	 */
	public IndexToObjectBackmapping getFromIndexBackmapping();

	/**
	 * Simple function that returns an {@link IndexableObject} given the index that
	 * instance would return by calling {@link IndexableObject#getIndex().}
	 *
	 * @author ottin
	 *
	 */
	public static interface IndexToObjectBackmapping {

		/**
		 * See {@link IndexToObjectBackmapping}.
		 */
		public IndexableObject fromIndex(int index);
	}
}