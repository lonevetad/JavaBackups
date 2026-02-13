package games.generic.controlModel.misc.factories;

import java.io.Serializable;

import games.generic.controlModel.items.InventoryItems;

public interface InventoryItemsFactory extends Serializable {
	public InventoryItems newInventoryItems(int level);

	public default InventoryItems newInventoryItems() { return this.newInventoryItems(0); }
}