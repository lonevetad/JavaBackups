package games.theRisingAngel.inventory;

import java.awt.Dimension;

import games.generic.controlModel.items.InventoryItems;

public class InventoryTRAn extends InventoryItems {
	public static final int INVENTORY_COLUMNS_BASE = 4, INVENTORY_COLUMNS_PER_LEVEL = 2, INVENTORY_ROWS_BASE = 1,
			INVENTORY_ROWS_PER_LEVEL = 1;

	public InventoryTRAn() { super(); }

	public InventoryTRAn(int level) { super(level); }

	@Override
	public Dimension levelToInventoryDimension(int levelTarget) {
		Dimension d;
		if (levelTarget < 0) { levelTarget = 0; }
		d = new Dimension();
		d.width = INVENTORY_COLUMNS_BASE + (INVENTORY_COLUMNS_PER_LEVEL * levelTarget);
		d.height = INVENTORY_ROWS_BASE + (INVENTORY_ROWS_PER_LEVEL * levelTarget);
		return d;
	}

}
