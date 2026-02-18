package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentType;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;

public class EquipItemTRAn extends EquipmentItem {
	private static final long serialVersionUID = -893723490864580L;

	public EquipItemTRAn(GModalityRPG gmrpg, EquipmentType equipmentType, String name) {
		super(gmrpg, equipmentType, name);
	}

	public EquipItemTRAn(GModalityRPG gmrpg, EquipmentType equipmentType, String name,
			AttributeModification[] baseAttributeMods) {
		super(gmrpg, equipmentType, name, baseAttributeMods);
	}

	@Override
	public void onDrop(GModalityRPG gmRPG) {
	}

	@Override
	public void onPickUp(GModalityRPG gmRPG) {
	}

	//

	protected void defineDefaultMaxUpgradesPerCategory(GModality gm) {
		for (EquipmentUpgradeCategory category : EquipmentUpgradeCategory.values()) {
			super.addUpgradeCategoryMax(category, category.getDefaultMaxUpgradesPerCategory());
		}
	}

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
		super.enrichEquipment(gm, providersHolder);
		this.defineDefaultMaxUpgradesPerCategory(gm);
		// other things to do?
	}
}