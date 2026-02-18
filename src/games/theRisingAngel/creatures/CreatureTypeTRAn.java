package games.theRisingAngel.creatures;

import games.generic.controlModel.misc.IndexableObject;
import games.theRisingAngel.enums.CreatureTypesTRAn;
import tools.ObjectNamedID;

/**
 * Marker interface, used in the enum {@link CreatureTypesTRAn}.
 *
 * @author ottin
 *
 */
public interface CreatureTypeTRAn extends ObjectNamedID, IndexableObject {

	@Override
	public default boolean setID(Long ID) { return false; }
}
