package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.subimpl.GModalityRPG;

public class InventoryItemBaseTRAn extends InventoryItem {
	private static final long serialVersionUID = -251454102L;

	public InventoryItemBaseTRAn(GModality gameModality, String name) {
		super(gameModality, name);

	}

	@Override
	public void onDrop(GModalityRPG gmRPG) { this.resetStuffs(); }

	@Override
	public void onPickUp(GModalityRPG gmRPG) { this.resetStuffs(); }

	@Override
	public void onAddedToGame(GModality gm) { this.resetStuffs(); }

	@Override
	public void onRemovedFromGame(GModality gm) { this.resetStuffs(); }

	@Override
	public void resetStuffs() {}
}