package games.generic.controlModel.subimpl;

import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.loaders.LoaderGameObjects;
import games.generic.controlModel.misc.GameObjectsProvider;

public abstract class LoaderEquipments extends LoaderGameObjects<EquipmentItem> {

	public LoaderEquipments(GameObjectsProvider<EquipmentItem> objProvider) {
		super(objProvider);
	}
}