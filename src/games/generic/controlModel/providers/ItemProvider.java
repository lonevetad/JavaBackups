package games.generic.controlModel.providers;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.InventoryItem;

public class ItemProvider extends GObjProviderRarityPartitioning<InventoryItem> {
	public static final String NAME = "ItemP";

	public ItemProvider() { super(); }

	public InventoryItem getItemByName(GModality gm, String name) {
		if (gm == null || name == null)
			return null;
//		return getObjByName(name).newInstance(gm);
		return super.getNewObjByName(gm, name);
	}
}