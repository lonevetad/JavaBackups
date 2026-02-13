package games.theRisingAngel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentType;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.subimpl.GModalityRPG;

public class EquipItemTRAn extends EquipmentItem {
	private static final long serialVersionUID = -893723490864580L;

	public EquipItemTRAn(GModalityRPG gmrpg, EquipmentType equipmentType, String name) {
		super(gmrpg, equipmentType, name);
	}

	public EquipItemTRAn(GModalityRPG gmrpg, EquipmentType equipmentType, String name,
			AttributeModification[] baseAttributeMods) {
		super(gmrpg, equipmentType, name, baseAttributeMods);
	}

	@Override
	public void onDrop(GModalityRPG gmRPG) {}

	@Override
	public void onPickUp(GModalityRPG gmRPG) {}

	@Override
	protected void enrichEquipment(GModality gm, GameObjectsProvidersHolder providersHolder) {}
}