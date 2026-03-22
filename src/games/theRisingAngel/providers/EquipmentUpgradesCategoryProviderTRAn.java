package games.theRisingAngel.providers;

import games.generic.controlModel.providers.EquipmentUpgradesCategoryProvider;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;

public class EquipmentUpgradesCategoryProviderTRAn extends EquipmentUpgradesCategoryProvider<EquipmentUpgradeCategory> {

	public EquipmentUpgradesCategoryProviderTRAn() {
		super();
	}

	@Override
	public EquipmentUpgradeCategory[] getEnumValues() {
		return EquipmentUpgradeCategory.VALUES;
	}
}
