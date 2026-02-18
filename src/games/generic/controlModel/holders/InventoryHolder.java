package games.generic.controlModel.holders;

import java.awt.Point;
import java.util.Iterator;
import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.InventoryAdder;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.items.InventoryItems;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.ObjectWithID;

/**
 * Mark an object as having at least one {@link InventoryItems} (the one
 * returned by {@link #getBaseInventory()})
 */
public interface InventoryHolder extends GModalityHolder {

	public InventoryItems getBaseInventory();

	/**
	 * Returns some additional inventories, if any.q
	 */
	public default Map<InventoryAdder, InventoryItems> getAdditionalInventories() { return null; }

	public void setBaseInventory(InventoryItems inventory);

	public default boolean hasAdditionalInventory(InventoryAdder inventoryAdder) {
		return this.getAdditionalInventories().containsKey(inventoryAdder);
	}

	public default void addAdditionalInventory(InventoryAdder inventoryAdder) {
		this.getAdditionalInventories().put(inventoryAdder, inventoryAdder.getInventoryToAdd());
	}

	public default void removeAdditionalInventory(InventoryAdder inventoryAdder) {
		Map<InventoryAdder, InventoryItems> additionalInvs;
		InventoryItems inventoryAdditional;
		Map<Long, InventoryItem> itemsToDrop;
		GModality gm;
		GModalityRPG gmRPG;
		additionalInvs = this.getAdditionalInventories();
		inventoryAdditional = additionalInvs.get(inventoryAdder);
		itemsToDrop = inventoryAdditional.getAllItems();
		additionalInvs.remove(inventoryAdder);

		gm = this.getGameModality();
		if (!(gm instanceof GModalityRPG)) { return; }
		gmRPG = (GModalityRPG) gm;
		itemsToDrop.forEach((id, item) -> gmRPG.dropItem(item));

	}

	public default boolean addToInventory(InventoryItem item) {
		boolean added;
		Point locationOfAddition;
		Map<InventoryAdder, InventoryItems> additionalInvs;
		Iterator<java.util.Map.Entry<InventoryAdder, InventoryItems>> invIter;
		if (item == null) { return false; }
		locationOfAddition = this.getBaseInventory().addItem(item);
		if (locationOfAddition != null) {
			if (this instanceof ObjectWithID) { item.setOwner((ObjectWithID) this); }
			return true;
		}
		// try all additional inventories
		additionalInvs = this.getAdditionalInventories();
		if (additionalInvs == null) { return false; }

		invIter = additionalInvs.entrySet().iterator();
		added = false;
		while (invIter.hasNext() && //
				(added = (locationOfAddition = invIter.next().getValue().addItem(item)) == null))
			;
		if (added && this instanceof ObjectWithID) { item.setOwner((ObjectWithID) this); }
		return added;
	}
}
