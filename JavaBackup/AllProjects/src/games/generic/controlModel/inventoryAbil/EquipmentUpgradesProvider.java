package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.subimpl.GObjProviderRarityPartitioning;

public class EquipmentUpgradesProvider extends GObjProviderRarityPartitioning<EquipmentUpgrade> {
	public static final String NAME = "EqUpP";

	public EquipmentUpgradesProvider() {
		super();
	}

	/** Should be preferred over {@link #getObjIdentifiedByID(Integer)}. */
	public EquipmentUpgrade getAbilityByName(GModality gm, String name) {
		if (name == null)
			return null;
//		return getObjByName(name).newInstance(gm);
		return getNewObjByName(gm, name);
	}
}