package games.generic.controlModel.subimpl;

import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.loaders.LoaderGameObjects;
import games.generic.controlModel.providers.ItemProvider;

public abstract class LoaderItems extends LoaderGameObjects<InventoryItem> {

	public LoaderItems(ItemProvider objProvider) { super(objProvider); }
}