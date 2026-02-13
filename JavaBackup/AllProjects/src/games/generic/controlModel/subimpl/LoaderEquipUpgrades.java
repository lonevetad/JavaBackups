package games.generic.controlModel.subimpl;

import games.generic.controlModel.items.EquipmentUpgrade;
import games.generic.controlModel.loaders.LoaderGameObjects;
import games.generic.controlModel.misc.GameObjectsProvider;

public abstract class LoaderEquipUpgrades extends LoaderGameObjects<EquipmentUpgrade> {

	public LoaderEquipUpgrades(GameObjectsProvider<EquipmentUpgrade> objProvider) {
		super(objProvider);
	}
}