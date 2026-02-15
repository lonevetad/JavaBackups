package games.generic.controlModel.attributes;

import java.io.Serializable;


import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.IEquipmentUpgrade;
import games.generic.controlModel.items.IEquipmentUpgradeCategory;
import games.generic.controlModel.misc.RangedAmountInt;

/**
 * An {@link EquipmentItem} might have some restrictions about the amount of {@link IEquipmentUpgrade} it could hold at the same time.
 * That amount could be either a general one, or a specific one, i.e. a per-{@link IEquipmentUpgradeCategory}
 * */
public class MaxUpgradesPerCategory implements Serializable {
	private static final long serialVersionUID = -4000000102054402248L;

	protected RangedAmountInt maxUpgradableAmounts;
	protected IEquipmentUpgradeCategory upgradeCategory;
	
	public MaxUpgradesPerCategory(RangedAmountInt maxUpgradableAmounts, IEquipmentUpgradeCategory upgradeCategory) {
		super();
		this.maxUpgradableAmounts = maxUpgradableAmounts;
		this.upgradeCategory = upgradeCategory;
	}
	public MaxUpgradesPerCategory(RangedAmountInt maxUpgradableAmounts) {
		this.maxUpgradableAmounts = maxUpgradableAmounts;
		this.upgradeCategory = null;
	}
	public MaxUpgradesPerCategory() {
		this(new RangedAmountInt(3)); // "3" as an example
	}
	
	//

	public int getMaxUpgradesAmount() {
		return this.maxUpgradableAmounts.getCurrent();
	}
	
	public int getLimitMaxUpgradesAmount() {
		return this.maxUpgradableAmounts.getMax();
	}

	public void upgradeMaxUpgradesAmountTo(int newMaximum) {
		this.maxUpgradableAmounts.setCurrent(newMaximum);
	}
	
	//

	public RangedAmountInt getMaxUpgradableAmounts() {
		return maxUpgradableAmounts;
	}
	public IEquipmentUpgradeCategory getUpgradeCategory() {
		return upgradeCategory;
	}
	public void setMaxUpgradableAmounts(RangedAmountInt maxUpgradableAmounts) {
		this.maxUpgradableAmounts = maxUpgradableAmounts;
	}
	public void setUpgradeCategory(IEquipmentUpgradeCategory upgradeCategory) {
		this.upgradeCategory = upgradeCategory;
	}
	
}
