package games.generic.controlModel.subimpl;

import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.loaders.LoaderGameObjects;
import games.generic.controlModel.misc.GameObjectsProvider;

public abstract class LoaderItems extends LoaderGameObjects<InventoryItem> {

	public LoaderItems(GameObjectsProvider<InventoryItem> objProvider) {
		super(objProvider);
	}
}