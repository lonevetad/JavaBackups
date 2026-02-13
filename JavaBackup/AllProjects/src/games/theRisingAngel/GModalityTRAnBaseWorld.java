package games.theRisingAngel;

import java.awt.Dimension;

import games.generic.controlModel.GController;
import games.generic.controlModel.GMap;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.items.InventoryItems;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.BasePlayerRPG;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.events.GEventInterfaceTRAn;
import games.theRisingAngel.inventory.InventoryTRAn;
import games.theRisingAngel.misc.CurrencySetTRAn;
import games.theRisingAngel.misc.EssenceExtractorTRAn;
import games.theRisingAngel.misc.PlayerCharacterTypesTRAn.PlayerCharacterTypes;
import tools.ObjectNamedID;

// TODO todo tons of stuffs
public class GModalityTRAnBaseWorld extends GModalityRPG {
	public static final String NAME = "Basic World";
	public static final int ATTRIBUTES_POINTS_GAINED_ON_LEVELING = 5;
	/** See {@link GModalityRPG#SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE} */
	public static final int SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN = 20;
	/** Milliseconds :D */
	public static final int TIME_SUBUNITS_EACH_TIME_UNIT_TRAn = 1000;

	public GModalityTRAnBaseWorld(GController controller, String modalityName) {
		super(controller, modalityName);
		essenceExtractor = new EssenceExtractorTRAn();
	}

	protected final EssenceExtractorTRAn essenceExtractor;

	// NEW-STUFF

	@Override
	public GEventInterface newEventInterface() { return new GEventInterfaceTRAn(); }

	@Override
	protected GameObjectsManager newGameObjectsManager(GEventInterface gei) { return new GameObjectsManagerTRAn(this); }

	@Override
	public CurrencySet newCurrencyHolder() { return new CurrencySetTRAn(this); }

	@Override
	protected PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer, ObjectNamedID characterType) {
		PlayerTRAn p;
		p = new PlayerTRAn(this, (PlayerCharacterTypes) characterType);
		setStartingBaseAttributes(p);
		p.setCurrencies(newCurrencyHolder());
		return p;
	}

	@Override
	protected GMap newGameMap(String mapName) {
		GMapTRAn gmap;
		gmap = new GMapTRAn(mapName);
		gmap.setGOISMDelegated(getGObjectInSpaceManager());
		return gmap;
	}

	@Override
	public InventoryItems newInventoryItems() { return new InventoryTRAn(); }

	@Override
	public InventoryItems newInventoryItems(int level) { return new InventoryTRAn(level); }

	@Override
	public InventoryItems newInventoryItems(Dimension size) {
		InventoryTRAn inv;
		inv = new InventoryTRAn();
		inv.resizeBy(size);
		return inv;
	}

	// GETTERS

	public EssenceExtractorTRAn getEssenceExtractor() { return essenceExtractor; }

	@Override
	public int getAttributesPointGainedOnLevelingUp(BasePlayerRPG p) { return ATTRIBUTES_POINTS_GAINED_ON_LEVELING; }

	/** Given a {@link PlayerTRAn}, set its initial set of attributes */
	public void setStartingBaseAttributes(PlayerTRAn player) {
		player.getCharacterType().applyStartingAttributes(player);
	}

	@Override
	public void startGame() {
//		((GameObjectsProvidersHolderRPG) this.getGameObjectsProvider()).setgModality(this);
		super.startGame();
		// and then? TODO
	}

	// TODO to do definire un metodo di dropping degli oggetti, con abilità e
	// modificatori annessi

//

	// TODO DAMAGE CALCULATION

	//

	// TODO class

}