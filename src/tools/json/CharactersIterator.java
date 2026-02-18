package tools.json;

/**
 * Iterator specifically designed for {@code char} iteration.
 */
public interface CharactersIterator {

	/**
	 * Get the index of the current char, the one that will be returned by
	 * {@link #currentCharAndMove()}. Starts at 0.
	 */
	public int currentIndex();

	public boolean hasMoreChars();

	/**
	 * Obtain the character pointed by {@link #currentIndex()} and move it to the
	 * next one.
	 */
	public char currentCharAndMove();

	public boolean hasPreviousChars();

	/**
	 * Works as the opposite of {@link #currentCharAndMove()}: first of all it
	 * reduces by one the value of {@link #currentIndex()}, then returns the
	 * {@code char} it is pointing to.
	 */
	public char previousCharAndStepBack();

	/**
	 * Makes this iterator no more useful.
	 */
	public void closeIterator();
}
