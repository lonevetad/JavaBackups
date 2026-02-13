package games.generic.controlModel.misc;

/** Comfortable for defining set of attributes using enumerations. */
public interface AttributeIdentifier extends IndexableObject {

	/**
	 * Returns the minimum value this attribute can have.
	 */
	public default int lowerBound() { return Integer.MIN_VALUE; }

	/**
	 * Returns the maximum value this attribute can have.
	 */
	public default int upperBound() { return Integer.MAX_VALUE; }
}