package games.theRisingAngel.providers;

import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.providers.AttributesProvider;
import games.generic.controlModel.providers.DamageTypesProvider;
import games.generic.controlModel.providers.EquipmentUpgradesCategoryProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.DamageTypesTRAn;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;
import games.theRisingAngel.misc.CurrencySetTRAn;

public class GameObjectsProvidersHolderTRAn extends GameObjectsProvidersHolderRPG {
	public GameObjectsProvidersHolderTRAn(GModalityRPG gModality) {
		super(gModality);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.registerEnumBasedProvider(EquipmentUpgradeCategory.NAME, this.equipUpgradesCategoryProvider);
		this.registerEnumBasedProvider(AttributesTRAn.NAME, this.equipUpgradesCategoryProvider);
		this.registerEnumBasedProvider(DamageTypesTRAn.NAME, this.damageTypesProvider);
	}

	// enum-based providers

	@Override
	public EquipmentUpgradesCategoryProvider<?> newEquipUpgradesCategoryProvider() {
		return new EquipmentUpgradesCategoryProviderTRAn();
	}

	@Override
	public AttributesProvider<?> newAttributesProvider() {
		return new AttributesProviderTRAn();
	}

	@Override
	public DamageTypesProvider<?> newDamageTypesProvider() {
		return new DamageTypesProviderTRAn();
	}

	// various "new-" methods

	@Override
	public CurrencySet newCurrencyHolder() {
		return new CurrencySetTRAn(this.getGameModality());
	}

	@Override
	public AbilityGeneric newAbilityGeneric(String abilityName, Object abilityContext) {
		return this.getAbilitiesProvider().getNewObjByName(this.getGameModality(), abilityName);
	}

	// @Override
	// public EquipItemProvider newEquipItemProvider() { return new
	// EquipItemProvider(); }
	//
	// @Override
	// public EquipmentUpgradesProvider newEquipUpgradesProvider() { return new
	// EquipmentUpgradesProvider(); }
	//
	// @Override
	// public CreaturesProvider<BaseCreatureRPG> newCreatureProvider() { return new
	// CreaturesProvider<>(); }
	//
	// @Override
	// public GMapProvider newMapsProvider() { return new GMapProvider(); }
	//
	// @Override
	// public ItemProvider newItemProvider() { return new ItemProvider(); }
}