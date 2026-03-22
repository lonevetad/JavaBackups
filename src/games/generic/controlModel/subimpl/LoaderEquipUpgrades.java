package games.generic.controlModel.subimpl;

import games.generic.controlModel.items.IEquipmentUpgrade;
import games.generic.controlModel.loaders.LoaderGameObjects;
import games.generic.controlModel.misc.GameObjectsProvider;

public abstract class LoaderEquipUpgrades extends LoaderGameObjects<IEquipmentUpgrade> {

	public LoaderEquipUpgrades(GameObjectsProvider<IEquipmentUpgrade> objProvider) {
		super(objProvider);
	}
}