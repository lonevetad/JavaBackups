package games.theRisingAngel.loaders;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.items.EssenceStorage;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.providers.ItemProvider;
import games.generic.controlModel.subimpl.LoaderItems;
import games.theRisingAngel.inventory.InventoryItemBaseTRAn;

public class LoaderItemsTRAn extends LoaderItems {

	public LoaderItemsTRAn(ItemProvider objProvider) { super(objProvider); }

	@Override
	public LoadStatusResult loadInto(GController gm) {
		// TODO use it

		// nothing to load wight now

		return LoadStatusResult.Success;
	}

	protected InventoryItem newItemByName(GModality gm, String name) {
		switch (name) {
		case EssenceStorage.ESSENCE_STORAGE_NAME: {
			return new EssenceStorage(gm);
		}
		default: {
// right new, there's only one type of inventory item
			return new InventoryItemBaseTRAn(gm, name);
		}
		}
	}
}