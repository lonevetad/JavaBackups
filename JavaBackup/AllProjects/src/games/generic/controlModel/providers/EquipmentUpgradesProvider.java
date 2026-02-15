package games.generic.controlModel.providers;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.IEquipmentUpgrade;

public class EquipmentUpgradesProvider extends GObjProviderRarityPartitioning<IEquipmentUpgrade> {
	public static final String NAME = "EqUpP";

	public EquipmentUpgradesProvider() {
		super();
	}

	/** Should be preferred over {@link #getObjIdentifiedByID(Integer)}. */
	public IEquipmentUpgrade getAbilityByName(GModality gm, String name) {
		if (name == null)
			return null;
//		return getObjByName(name).newInstance(gm);
		return getNewObjByName(gm, name);
	}
}