package games.theRisingAngel.inventory;

import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.enums.EquipmentTypesTRAn;

public abstract class EIWeapon extends EINotJewelry {
	private static final long serialVersionUID = -63485105886988L;

	public EIWeapon(GModalityRPG gmrpg, EquipmentTypesTRAn et, String name, AttributeModification[] baseAttributeMods) {
		super(gmrpg, et, name, baseAttributeMods);
	}

	protected boolean isRightHand, isSingleHanded;

	//

	public boolean isRightHand() { return isRightHand; }

	public boolean isSingleHanded() { return isSingleHanded; }

	//

	public void setRightHand(boolean isRightHand) { this.isRightHand = isRightHand; }

	public void setSingleHanded(boolean isSingleHanded) { this.isSingleHanded = isSingleHanded; }
}