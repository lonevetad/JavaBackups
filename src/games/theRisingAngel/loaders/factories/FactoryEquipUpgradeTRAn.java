package games.theRisingAngel.loaders.factories;

import games.generic.controlModel.items.FactoryEquipUpgrade;
import games.generic.controlModel.items.IEquipmentUpgrade;
import games.theRisingAngel.inventory.EquipmentUpgradeTRAn;

public class FactoryEquipUpgradeTRAn extends FactoryEquipUpgrade {

	public FactoryEquipUpgradeTRAn() {
		super();
	}

	@Override
	protected IEquipmentUpgrade newEquipmentUpgrade(String equipUpgradeName, int rarityIndex) {
		return new EquipmentUpgradeTRAn(rarityIndex, equipUpgradeName);
	}

}
