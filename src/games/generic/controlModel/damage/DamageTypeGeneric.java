package games.generic.controlModel.damage;

import tools.ObjectNamedID;

/** Simple marker interface for damages types. */
public interface DamageTypeGeneric extends ObjectNamedID {
	@Override
	public default boolean setID(Long ID) { return false; }
}