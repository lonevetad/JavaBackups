package games.generic.controlModel.items;

import games.generic.controlModel.GModality;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.player.PlayerGeneric;

/**
 * Set of {@link EquipmentItem} equipped to a creature (like a the player:
 * {@link PlayerGeneric}.
 */
public abstract class EquipmentSet {

	public static interface ConsumerEquipmentIndex {
		public void apply(EquipmentItem equipment, int index);
	}

	public EquipmentSet() {
	}

	/** Should be final */
	protected BaseCreatureRPG creatureWearingEquipments;

	// public abstract EquipmentsHolder getEquipmentsHolder();
//	public abstract void setEquipmentsHolder(EquipmentsHolder equipmentsHolder);

	public BaseCreatureRPG getCreatureWearingEquipments() {
		return this.creatureWearingEquipments;
	}

	public void setCreatureWearingEquipments(BaseCreatureRPG creatureWearingEquipments) {
		this.creatureWearingEquipments = creatureWearingEquipments;
	}

	//

	/**
	 * Get the set of {@link EquipmentItem}. Should not be used.<br>
	 * NOTE: unused slots (i.e. "places" where there is no equipped objects for that
	 * type) are <code>null</code>.
	 */
	public abstract EquipmentItem[] getEquippedItems();

	public EquipmentItem getEquippedItemAt(int index) {
		return getEquippedItems()[index];
	}

	/**
	 * Equip the given item into the set NOTE: Should check if is yet equipped an
	 * intem in that slot
	 */
	public abstract void addEquipmentItem(GModality gm, EquipmentItem ei);

	/**
	 * TODO use player's inventory and
	 * {@link EquipmentItem#getLocationInInventory()} (of the second parameter, if
	 * not <code>null</code>).
	 */
	public abstract void swapEquipmentItem(GModality gm, EquipmentItem newEI, EquipmentItem oldEI);

	public abstract void forEachEquipment(ConsumerEquipmentIndex consumer);
}