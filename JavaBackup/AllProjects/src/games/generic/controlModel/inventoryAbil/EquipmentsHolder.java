package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.GModalityHolder;
import games.generic.controlModel.gObj.GameObjectGeneric;
import games.generic.controlModel.inventoryAbil.EquipmentSet.ConsumerEquipmentIndex;

public interface EquipmentsHolder extends GModalityHolder, GameObjectGeneric {
	public EquipmentSet getEquipmentSet();

	/**
	 * Remember to call
	 * {@link EquipmentSet#setCreatureWearingEquipments(BaseCreatureRPG)}
	 */
	public void setEquipmentSet(EquipmentSet equips);

	//

	public EquipmentSet newEquipmentSet();

	//

	public default void equip(EquipmentItem equipment) {
		EquipmentSet es;
		es = this.getEquipmentSet();
		if (es != null) { es.addEquipmentItem(getGameModality(), equipment); }
	}

	public default void forEachEquipment(ConsumerEquipmentIndex action) { getEquipmentSet().forEachEquipment(action); }

	@Override
	public default void addMeToGame(GModality gm) {
		GameObjectGeneric.super.addMeToGame(gm);
		forEachEquipment((e, i) -> {
			if (e != null)
				e.addMeToGame(gm);
		});
	}

	@Override
	public default void onAddedToGame(GModality gm) {
		// nothing in particular to be done: each ability will behave by its own
	}

	@Override
	public default void removeMeToGame(GModality gm) {
		GameObjectGeneric.super.removeMeToGame(gm);
		forEachEquipment((e, i) -> {
			if (e != null)
				e.removeMeToGame(gm);
		});
	}

	@Override
	public default void onRemovedFromGame(GModality gm) {
		// nothing in particular to be done: each ability will behave by its own
	}
}