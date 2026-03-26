package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.attributes.AttributeIdentifier;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.attributes.MaxUpgradesPerCategory;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentType;
import games.generic.controlModel.items.IEquipmentUpgrade;
import games.generic.controlModel.items.IEquipmentUpgradeCategory;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EquipmentTypesTRAn;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;
import games.theRisingAngel.misc.AttributeModificationTRAn;
import games.theRisingAngel.misc.MaxUpgradesPerCategoryTRAn;
import games.theRisingAngel.providers.GameObjectsProvidersHolderTRAn;

public class EquipItemTRAn extends EquipmentItem {
	private static final long serialVersionUID = -893723490864580L;

	public EquipItemTRAn(GModalityRPG gmrpg, EquipmentType equipmentType, String name) {
		super(gmrpg, equipmentType, name);
	}

	public EquipItemTRAn(GModalityRPG gmrpg, EquipmentType equipmentType, String name,
			AttributeModification[] baseAttributeMods) {
		super(gmrpg, equipmentType, name, baseAttributeMods);
	}

	public EquipItemTRAn(GModalityRPG gmrpg, String name) {
		this(gmrpg, null, name);
	}

	public EquipItemTRAn(GModalityRPG gmrpg, String name, AttributeModification[] baseAttributeMods) {
		super(gmrpg, null, name, baseAttributeMods);
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
		this.defineDefaultMaxUpgradesPerCategory(gm);
		// other things to do?
	}

	@Override
	public void loadEquipmentType(GModality gm, String typeName) {
		this.setEquipmentType(EquipmentTypesTRAn.valueOf(typeName));
	}

	//

	// JSON-related

	//

	@Override
	public IEquipmentUpgradeCategory getEquipmentUpgradeCategoryByName(GModality gm,
			String equipmentUpgradeCategoryName) {
		return EquipmentUpgradeCategory.valueOf(equipmentUpgradeCategoryName);
	}

	@Override
	public MaxUpgradesPerCategory newMaxUpgradesPerCategory(GModality gm,
			IEquipmentUpgradeCategory equipUpgradeCategory) {
		return new MaxUpgradesPerCategoryTRAn(equipUpgradeCategory);
	}

	@Override
	public AttributeIdentifier getAttributeIdentifierByName(GModality gm, String attributeName) {
		return AttributesTRAn.valueOf(attributeName);
	}

	@Override
	public AttributeModification newAttributeModification(GModality gm, AttributeIdentifier attributeModified,
			int value) {
		return new AttributeModificationTRAn(attributeModified, value);
	}

	@Override
	public IEquipmentUpgrade newEquipmentUpgrade(GModality gm, String equipUpgradeName) {
		return new EquipmentUpgradeTRAn(0, equipUpgradeName);
	}

	@Override
	public AbilityGeneric newAbility(GModality gm, String abilityName) {
		GModalityTRAnBaseWorld gmTRAn = (GModalityTRAnBaseWorld) gm;
		GameObjectsProvidersHolderTRAn gophTRAn = (GameObjectsProvidersHolderTRAn) gmTRAn.getGameObjectsProvider();
		return gophTRAn.newAbilityGeneric(abilityName, null);
	}

	@Override
	public IEquipmentUpgradeCategory[] getAllEquipmentUpgradeCategory() {
		return EquipmentUpgradeCategory.VALUES;
	}
}