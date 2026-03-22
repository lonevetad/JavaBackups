package games.generic.controlModel.providers;

import games.generic.controlModel.items.IEquipmentUpgradeCategory;

public abstract class EquipmentUpgradesCategoryProvider<E extends Enum<E> & IEquipmentUpgradeCategory>
		extends EnumBasedObjectProvider<E> {

	public EquipmentUpgradesCategoryProvider() {
		super();
	}

}
