package games.theRisingAngel.inventory;

import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentTypesTRAn;

public class EINotJewelry extends EquipItemTRAn {
	private static final long serialVersionUID = 1L;

	public EINotJewelry(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name,
			AttributeModification[] baseAttributeMods) {
		super(gmrpg, et, name, baseAttributeMods);
		if (et == null || EquipmentTypesTRAn.isJewelry(et)) {
			throw new IllegalArgumentException("It's a jewelry: " + (et == null ? "Null" : et.getName()));
		}
	}

	@Override
	public void onDrop(GModalityRPG gmRPG) {
	}

	@Override
	public void onPickUp(GModalityRPG gmRPG) {
	}
}