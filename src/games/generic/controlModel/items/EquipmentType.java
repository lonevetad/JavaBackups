package games.generic.controlModel.items;

import games.generic.controlModel.misc.IndexableObject;

/**
 * Identifier to distinguish one equipment type from another (for example, a
 * "weapon" is not a "shoes" or a "ring").
 */
public interface EquipmentType extends IndexableObject {
	@Override
	public default boolean setID(Long ID) { return false; }
}