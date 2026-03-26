package games.theRisingAngel.loaders.factories;

import java.awt.Dimension;

import games.generic.controlModel.GModality;
import games.generic.controlModel.currency.Currency;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.misc.FactoryObjPrototypedGModalityBased;

public class FactoryItems implements FactoryObjPrototypedGModalityBased<InventoryItem> {
	/*
	 * public int rarity; public String name; public String description = ""; public
	 * Dimension dimensionInInventory = null; public int[] price;
	 */
	public InventoryItemFactory inventoryItemFactory = null;
	public InventoryItem prototype;

	@Override
	public InventoryItem getPrototype() {
		return prototype;
	}

	@Override
	public void setPrototype(InventoryItem prototype) {
		this.prototype = prototype;
	}

	@Override
	public InventoryItem newInstance(GModality gm) {
		InventoryItem ii;
		ii = inventoryItemFactory.newInstance(gm);
		this.setValuesInto(gm, ii);
		return ii;
	}

	public void setValuesInto(GModality gm, InventoryItem ii) {
		ii.setName(prototype.getName());
		ii.setDescription(prototype.getDescription());
		ii.setRarityIndex(prototype.getRarityIndex());
		if (prototype.getDimensionInInventory() != null) {
			ii.setDimensionInInventory(prototype.getDimensionInInventory());
		}
		if (prototype.getSellPrice() != null) {
			int n;
			CurrencySet cs, csPrototype;
			Currency[] currencies;
			cs = gm.getGameObjectsProvider().newCurrencyHolder();
			currencies = cs.getCurrencies();
			cs.setGameModaliy(gm); // not needed
			csPrototype = prototype.getSellPrice();
			n = csPrototype.getCurrencies().length;
			while (--n >= 0) {
				cs.setCurrencyAmount(currencies[n], csPrototype.getCurrencyAmount(currencies[n]));
			}
			ii.setSellPrice(cs);
		}
	}

	public int getRarity() {
		return prototype.getRarityIndex();
	}

	public String getName() {
		return prototype.getName();
	}

	public String getDescription() {
		return prototype.getDescription();
	}

	public Dimension getDimensionInInventory() {
		return prototype.getDimensionInInventory();
	}

	public int[] getPrice() {
		return prototype.getSellPrice().getCurrencyAmounts();
	}

	public InventoryItemFactory getInventoryItemFactory() {
		return inventoryItemFactory;
	}

	@Override
	public String toString() {
		/*
		 * return "FactoryEquip [\n name=" + name + ",\n rarity=" + rarity +
		 * ", sell price: " + Arrays.toString(price) + ",\n description: " + description
		 * + ",\n dimensions: " + dimensionInInventory + ",\n abilities=\n\t";
		 */
		return prototype.toString();
	}
}