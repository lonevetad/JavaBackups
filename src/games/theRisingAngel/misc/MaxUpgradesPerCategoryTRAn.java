package games.theRisingAngel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.MaxUpgradesPerCategory;
import games.generic.controlModel.items.IEquipmentUpgradeCategory;
import games.generic.controlModel.misc.RangedAmountInt;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;

public class MaxUpgradesPerCategoryTRAn extends MaxUpgradesPerCategory {
	private static final long serialVersionUID = -65410458250L;

	public MaxUpgradesPerCategoryTRAn(RangedAmountInt maxUpgradableAmounts, IEquipmentUpgradeCategory upgradeCategory) {
		super(maxUpgradableAmounts, upgradeCategory);
	}

	public MaxUpgradesPerCategoryTRAn(RangedAmountInt maxUpgradableAmounts) {
		super(maxUpgradableAmounts);
	}

	public MaxUpgradesPerCategoryTRAn() {
		super();
	}

	public MaxUpgradesPerCategoryTRAn(IEquipmentUpgradeCategory upgradeCategory) {
		super(upgradeCategory);
	}

	@Override
	public void loadEquipmentUpgradeCategory(GModality gm, String name) {
		super.setUpgradeCategory(EquipmentUpgradeCategory.valueOf(name));
	}

	@Override
	public MaxUpgradesPerCategory clone() {
		return new MaxUpgradesPerCategoryTRAn(this.getMaxUpgradableAmounts(), this.getUpgradeCategory());
	}

}
