package games.theRisingAngel;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.holders.ExperienceLevelHolder;
import games.generic.controlModel.items.InventoryAdder;
import games.generic.controlModel.items.InventoryItems;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.BasePlayerRPG;
import games.generic.controlModel.subimpl.ExperienceLevelHolderImpl;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.creatures.BaseCreatureTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.inventory.EquipmentSetTRAn;
import games.theRisingAngel.inventory.InventoryTRAn;
import games.theRisingAngel.misc.PlayerCharacterTypesTRAn.PlayerCharacterTypes;

public class PlayerTRAn extends BaseCreatureTRAn implements BasePlayerRPG {
	private static final long serialVersionUID = -3336623605789L;
	public static final int INVENTORY_LEVEL = 4;

	public PlayerTRAn(GModalityRPG gameModality, PlayerCharacterTypes characterType) {
		super(gameModality, "No name currently provided");
		this.characterType = characterType;
		this.attributePointsLeftToApply = 0;
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAn.Destroyed.getName());
		this.experienceLevelHolder = new ExperienceLevelHolderImpl();
		this.additionalInventories = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				InventoryAdder.COMPARATOR_INVENTORY_ADDER);
		this.setEquipmentSet(new EquipmentSetTRAn());
		this.setBaseInventory(new InventoryTRAn(INVENTORY_LEVEL));
	}

	protected int attributePointsLeftToApply;
	protected final PlayerCharacterTypes characterType;
	protected CurrencySet currencies;
	protected ExperienceLevelHolder experienceLevelHolder;
	protected InventoryItems baseInventory;
	protected Map<InventoryAdder, InventoryItems> additionalInventories;

	public PlayerCharacterTypes getCharacterType() { return characterType; }

	public ExperienceLevelHolder getExperienceLevelHolder() { return experienceLevelHolder; }

	@Override
	public ExperienceLevelHolder getExpLevelHolder() { return experienceLevelHolder; }

	@Override
	public int getExperienceNow() { return experienceLevelHolder.getExperienceNow(); }

	@Override
	public CurrencySet getCurrencies() { return currencies; }

	@Override
	public int getAttributePointsLeftToApply() { return attributePointsLeftToApply; }

	@Override
	public InventoryItems getBaseInventory() { return this.baseInventory; }

	@Override
	public Map<InventoryAdder, InventoryItems> getAdditionalInventories() { return this.additionalInventories; }

	//

	@Override
	public void setCurrencies(CurrencySet currencies) {
		this.currencies = currencies;
		if (currencies != null) { currencies.setCanFireCurrencyChangeEvent(true); }
	}

	@Override
	public void setExpLevelHolder(ExperienceLevelHolder expLevelHolder) { this.experienceLevelHolder = expLevelHolder; }

	@Override
	public void setAttributePointsLeftToApply(int attributePointsLeftToApply) {
		this.attributePointsLeftToApply = attributePointsLeftToApply;
	}

	@Override
	public void setBaseInventory(InventoryItems inventory) { this.baseInventory = inventory; }

	//

	//

	// TODO other methods

	@Override
	public ExperienceLevelHolder reset() {
		experienceLevelHolder.reset();
		return this;
	}

	@Override
	public void recalculateExpToLevelUp() { this.experienceLevelHolder.recalculateExpToLevelUp(); }

//	public void act(GModality modality, int milliseconds) { super.act(modality, milliseconds); }

	@Override
	public void onAddedToGame(GModality gm) {
		super.onAddedToGame(gm);
// TODO something else?
	}

	@Override
	public void onRemovedFromGame(GModality gm) {
		BiConsumer<String, AbilityGeneric> abilityAdderToGModality;
		super.onRemovedFromGame(gm);
		// Add equips and abilities on GMod
		abilityAdderToGModality = (n, ab) -> ab.onRemovedFromGame(gm);
		this.getAbilities().forEach(abilityAdderToGModality);
		this.getEquipmentSet().forEachEquipment((e, i) -> { if (e != null) { e.onRemovedFromGame(gm); } });

	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		super.notifyEvent(modality, ge); // destructible-object
// TODO altro?
	}

	@Override
	public boolean destroy() {
		if (!isDestroyed) {
			isDestroyed = true;
			super.destroy();
			return true;
		}
		return false;
	}

	//
	//

	// TODO FIRE EVENTS

}