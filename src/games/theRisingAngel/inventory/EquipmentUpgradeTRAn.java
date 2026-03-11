package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.subimpl.EquipmentUpgradeImpl;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;

public class EquipmentUpgradeTRAn extends EquipmentUpgradeImpl {
	private static final long serialVersionUID = 1L;

	public EquipmentUpgradeTRAn(int rarityIndex, String name) {
		super(rarityIndex, name);
	}

	// JSONed-related

	@Override
	public void loadAttributeUpgrade(GModality gm, String attributeName, int value) {
		this.getAttributesModifiers().add(new AttributeModification(AttributesTRAn.valueOf(attributeName), value));
	}

	@Override
	public void loadIEquipmentUpgradeCategory(GModality gm, String categoryName) {
		this.setUpgradeCategory(EquipmentUpgradeCategory.valueOf(categoryName));
	}

}
