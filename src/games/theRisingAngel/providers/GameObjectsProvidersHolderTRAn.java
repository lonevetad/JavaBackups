package games.theRisingAngel.providers;

import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.providers.EquipmentUpgradesCategoryProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;
import games.theRisingAngel.misc.CurrencySetTRAn;

public class GameObjectsProvidersHolderTRAn extends GameObjectsProvidersHolderRPG {
	public GameObjectsProvidersHolderTRAn(GModalityRPG gModality) {
		super(gModality);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.providers.put(EquipmentUpgradeCategory.NAME, this.equipUpgradesCategoryProvider);
	}

	@Override
	public EquipmentUpgradesCategoryProvider<?> newEquipUpgradesCategoryProvider() {
		return new EquipmentUpgradesCategoryProviderTRAn();
	}

	//

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