package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.AttributeIdentifier;
import games.generic.controlModel.items.IEquipmentUpgradeCategory;
import games.generic.controlModel.subimpl.EquipmentUpgradeImpl;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;

public class EquipmentUpgradeTRAn extends EquipmentUpgradeImpl {
	private static final long serialVersionUID = 348659410025301865L;

	public EquipmentUpgradeTRAn(int rarityIndex, String name) {
		super(rarityIndex, name);
	}

	@Override
	public IEquipmentUpgradeCategory loadIEquipmentUpgradeCategory(GModality gm, String categoryName) {
		return EquipmentUpgradeCategory.valueOf(categoryName);
	}

	@Override
	public AttributeIdentifier loadAttributeIdentifier(GModality gm, String attributeName, int attributeValue) {
		return AttributesTRAn.valueOf(attributeName);
	}

}
