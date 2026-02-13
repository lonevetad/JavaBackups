package videogamesOldVersion.games.theRisingAngel.main;

import games.generic.controlModel.subimpl.ExperienceLevelHolderImpl;
import videogamesOldVersion.games.theRisingAngel.equipment.AbstractEquipment;

public class PlayerModel {

	public PlayerModel() { equipments = null; }

	int pointsGained;
	ExperienceLevelHolderImpl expLevelHolder;
	StatisticheTRAn statBaseClassDependentValues, statNow, statMax;
	AbstractEquipment[] equipments;

	//

	//

	/**
	 * Everytime the player levels up, it gains points. Them will be assigned to
	 * some statistics (Strength, life, mana, agility, ecc) that will modify
	 * something. Calculate here that modifications.
	 */
	protected void resetStatMax_BasePlusPointsAssigned() {
		// TODO to-do resetStatMax_BasePlusPointsAssigned
	}

	protected void checkStatNowConsinstency() {
		// int i, j, len;
		// int[] n, m;
		if (statMax != null && statNow != null) {
			// n = statNow.getStats();
			// m = statMax.getStats();
			// len = n.length;
			// i=j=-1;
			// while(++i)
			statNow.forEachStat(e -> {
				int now, max;
				if (e.isUpperBounded()) {
					now = statNow.getStat(e);
					max = statMax.getStat(e);
					if (now >= max)
						statNow.setStatInt(e, max);
				}
			});
		}
	}

	public void recalculateStatMax() {
		resetStatMax_BasePlusPointsAssigned();
		if (equipments != null) {
			for (AbstractEquipment e : equipments) {
				e.applyEquipmentsEffects(statMax);
			}
			checkStatNowConsinstency();
		}
	}

	public boolean setEquipment(AbstractEquipment e, int index) {
		if (e != null && equipments != null && index >= 0 && index < equipments.length) {
			removeEquipmentAt(index, false);
			equipments[index] = e;
			e.applyEquipmentsEffects(statMax);
			checkStatNowConsinstency();
			return true;
		}
		return false;
	}

	public boolean removeEquipmentAt(int index) { return removeEquipmentAt(index, true); }

	protected boolean removeEquipmentAt(int index, boolean checkConsinstency) {
		AbstractEquipment oldEquip;
		if (equipments != null && index >= 0 && index < equipments.length) {
			oldEquip = equipments[index];
			if (oldEquip != null) {
				oldEquip.removeEquipmentsEffects(statMax);
				if (checkConsinstency)
					checkStatNowConsinstency();
			}
			return true;
		}
		return false;
	}
}
