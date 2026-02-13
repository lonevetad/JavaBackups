package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentTypesTRAn;

/**
 * Simple marker class to represents all kinds of jewelry: rings, necklaces,
 * bracelets, earrings.
 */
public class EIJewelry extends EquipItemTRAn {
	private static final long serialVersionUID = 1L;

	public EIJewelry(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name,
			AttributeModification[] baseAttributeMods) {
		super(gmrpg, et, name, baseAttributeMods);
		if (et == null || (!EquipmentTypesTRAn.isJewelry(et))) {
			throw new IllegalArgumentException("Not a jewelry: " + (et == null ? "Null" : et.getName()));
		}
	}

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {
		// do nothing: already done in loading time
	}

	@Override
	public void onDrop(GModalityRPG gmRPG) {}

	@Override
	public void onPickUp(GModalityRPG gmRPG) {}
}