package games.generic.controlModel.damage;

import games.generic.controlModel.misc.IEnumAlike;
import tools.ObjectNamedID;

/** Simple marker interface for damages types. */
public interface DamageTypeGeneric extends ObjectNamedID, IEnumAlike {
	@Override
	public default boolean setID(Long ID) {
		return false;
	}
}