package games.generic.controlModel.items;

import java.util.function.Function;

import games.generic.controlModel.misc.AttributesUpgrade;

/**
 * Simple extension to mark an {@link AttributeUpgrade} designed for
 * {@link EquipmentItem}s.
 */
public interface EquipmentUpgrade extends AttributesUpgrade {
	public static final Function<EquipmentUpgrade, String> KEY_EXTRACTOR = eu -> eu.getName();

	public String getDescription();

	public void setDescription(String description);

	public EquipmentItem getEquipmentAssigned();

	public void setEquipmentAssigned(EquipmentItem equipmentAssigned);

}