package games.theRisingAngel.inventory;

import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentTypesTRAn;

public class EIRing extends EIJewelry {
	private static final long serialVersionUID = 1L;

	public EIRing(GModalityRPG gmrpg, String name, AttributeModification[] baseAttributeMods) {
		super(gmrpg, EquipmentTypesTRAn.Ring, name, baseAttributeMods);
//		this.slotFingerSize = 1;
	}

	public int getSlotFingerSize() { return dimensionInInventory.width; }

	public void setSlotFingerSize(int slotFingerSize) {
		if (slotFingerSize > 0)
			dimensionInInventory.width = slotFingerSize;
	}
}