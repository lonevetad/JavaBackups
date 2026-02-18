package games.generic.controlModel.subimpl;

import java.awt.Dimension;

import games.generic.GameOptions;
import games.generic.controlModel.GController;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.items.InventoryItems;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.ObjectInSpace;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.generic.controlModel.player.BasePlayerRPG;
import geometry.ObjectLocated;
import tools.ObjectWithID;

public abstract class GModalityRPG extends GModalityET {

	public GModalityRPG(GController controller, String modalityName) {
		super(controller, modalityName);
//		this.gameObjectsProviderHolderRPG = gomp;
	}

	//

	protected abstract GameObjectsManager newGameObjectsManager(GEventInterface gei);

	@Override
	protected GameObjectsManager newGameObjectsManager() {
		return newGameObjectsManager(getEventInterface());
//		return newGameObjectsManager(newEventInterface());
	}

	public abstract InventoryItems newInventoryItems();

	public abstract InventoryItems newInventoryItems(int level);

	public abstract InventoryItems newInventoryItems(Dimension size);

//	protected final GameObjectsProviderHolderRPG gameObjectsProviderHolderRPG;

	/**
	 * After each levelling up, each player can gain points to spend to increase its
	 * attribues (as described in {@link CreatureAttributes} and
	 * {@link BasePlayerRPG#getAttributePointsLeftToApply()}).
	 */
	public abstract int getAttributesPointGainedOnLevelingUp(BasePlayerRPG p);

	/**
	 * See {@link GameOptionsRPG#getSpaceSubunitsEachUnit()}.
	 *
	 * @return
	 */
	public int getSpaceSubunitsEachUnit() {
		GameOptionsRPG gopt;
		gopt = (GameOptionsRPG) this.getGameController().getGameOptions();
		return gopt.getSpaceSubunitsEachUnit();
	}

	/**
	 * See {@link GameOptionsRPG#getTimeSubunitsEachUnit()}.
	 *
	 * @return
	 */
	@Override
	public int getTimeSubunitsEachUnit() {
		GameOptionsRPG gopt;
		gopt = (GameOptionsRPG) this.getGameController().getGameOptions();
		return gopt.getTimeSubunitsEachUnit();
	}

	//

	// TODO PUBLIC METHODS

	@Override
	public void loadFrom(GController gc, GameOptions gameOpt, LoaderGeneric loader) {

	}

	public boolean dropItem(InventoryItem item) {
		GEventInterface gei;
		GEventInterfaceRPG geiRPG;
		ObjectWithID owner;

		if (!this.addGameObject(item)) { return false; }

		gei = this.getEventInterface();
		if (!(gei instanceof GEventInterfaceRPG)) { return false; }
		geiRPG = (GEventInterfaceRPG) gei;

		if (this.getModel().contains(item)) {
			if (this.getModel().remove(item)) { geiRPG.fireGameObjectRemoved(this, item); }
		}

		owner = item.getOwner();
		if (owner instanceof ObjectLocated) { item.setLocation(((ObjectLocated) owner).getLocation()); }

		item.onDrop(this);
		geiRPG.fireGameObjectAdded(this, item);
		return true;
	}

	//

	//

	// TODO PROXYS

	/** Just a proxy */
	public void dealsDamageTo(DamageDealerGeneric source, CreatureSimple target, DamageGeneric damage) {
		this.getGameObjectsManager().dealsDamageTo(source, target, damage);
	}

	/** Just a proxy */
	public void spawnObjInMap(ObjectInSpace ois) { this.getGameObjectsManager().addToSpace(ois); }
}