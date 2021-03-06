package games.generic.controlModel.misc;

import tools.ObjectNamedID;

/**
 * Identify an object that is unique through it index (beware: it's NOT an
 * "ID"!) and its name.
 */
public interface IndexableObject extends ObjectNamedID {
	/** It differs substantially by {@link #getID()}. */
	public int getIndex();
}