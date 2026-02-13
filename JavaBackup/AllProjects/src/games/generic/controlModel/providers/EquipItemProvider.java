package games.generic.controlModel.providers;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.EquipmentItem;

public class EquipItemProvider extends GObjProviderRarityPartitioning<EquipmentItem> {
	public static final String NAME = "EquipP";

	public EquipItemProvider() {
		super();
	}

	public EquipmentItem getEquipByName(GModality gm, String name) {
		if (gm == null || name == null)
			return null;
//		return getObjByName(name).newInstance(gm);
		return super.getNewObjByName(gm, name);
	}
}