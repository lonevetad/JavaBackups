package games.generic.controlModel.items;

import java.util.function.Function;

import games.generic.controlModel.attributes.AttributesUpgrade;

/**
 * Simple extension to mark an {@link AttributeUpgrade} designed for
 * {@link EquipmentItem}s.
 */
public interface IEquipmentUpgrade extends AttributesUpgrade {
	public static final Function<IEquipmentUpgrade, String> KEY_EXTRACTOR = eu -> eu.getName();

	public String getDescription();

	public EquipmentItem getEquipmentAssigned();
	
	public IEquipmentUpgradeCategory getUpgradeCategory();
	
	//

	public void setDescription(String description);

	public void setEquipmentAssigned(EquipmentItem equipmentAssigned);

	public void setUpgradeCategory(IEquipmentUpgradeCategory upgradeCategory);
}