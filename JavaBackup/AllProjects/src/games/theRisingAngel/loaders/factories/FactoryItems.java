package games.theRisingAngel.loaders.factories;

import java.awt.Dimension;
import java.util.Arrays;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.misc.Currency;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.FactoryObjGModalityBased;

public class FactoryItems implements FactoryObjGModalityBased<InventoryItem> {
	public int rarity;
	public String name;
	public String description = "";
	public Dimension dimensionInInventory = null;
	public int[] price;
	public InventoryItemFactory inventoryItemFactory = null;

	@Override
	public InventoryItem newInstance(GModality gm) {
		InventoryItem ii;
		ii = inventoryItemFactory.newInstance(gm);
		this.setValuesInto(gm, ii);
		return ii;
	}

	public void setValuesInto(GModality gm, InventoryItem ii) {
		ii.setName(name);
		ii.setDescription(description);
		ii.setRarityIndex(rarity);
		if (dimensionInInventory != null)
			ii.setDimensionInInventory(dimensionInInventory);
		if (price != null) {
			int n;
			CurrencySet cs;
			Currency[] currencies;
			cs = gm.newCurrencyHolder();
			currencies = cs.getCurrencies();
			cs = gm.newCurrencyHolder();
			cs.setGameModaliy(gm); // not needed
			n = price.length;
			while (--n >= 0)
				cs.setCurrencyAmount(currencies[n], price[n]);
			ii.setSellPrice(cs);
		}
	}

	public int getRarity() { return rarity; }

	public String getName() { return name; }

	public String getDescription() { return description; }

	public Dimension getDimensionInInventory() { return dimensionInInventory; }

	public int[] getPrice() { return price; }

	public InventoryItemFactory getInventoryItemFactory() { return inventoryItemFactory; }

	@Override
	public String toString() {
		return "FactoryEquip [\n name=" + name + ",\n rarity=" + rarity + ", sell price: " + Arrays.toString(price)
				+ ",\n description: " + description + ",\n dimensions: " + dimensionInInventory + ",\n abilities=\n\t";
	}
}