package games.generic.controlModel.player;

import java.util.Map;

import games.generic.controlModel.holders.CurrencyHolder;
import games.generic.controlModel.holders.InventoryHolder;
import games.generic.controlModel.items.InventoryAdder;
import games.generic.controlModel.items.InventoryItems;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;

/** Base definition of a "player" of a "Rule Play Game" (RPG) */
public interface BasePlayerRPG extends PlayerWithExperience, BaseCreatureRPG, CurrencyHolder, InventoryHolder {

	/**
	 * After leveling up, a certain amount of points are aquired and should be spent
	 * o increase values of {@link CreatureAttributes}. This is that counter.
	 */
	public int getAttributePointsLeftToApply();

	public InventoryItems getBaseInventory();

	public Map<InventoryAdder, InventoryItems> getAdditionalInventories();

	/** See {@link #getAttributePointsLeftToApply()} */
	public void setAttributePointsLeftToApply(int attributePointsLeftToApply);

	public default void increasettributePointsLeftBy(int delta) {
		setAttributePointsLeftToApply(getAttributePointsLeftToApply() + delta);
	}

}