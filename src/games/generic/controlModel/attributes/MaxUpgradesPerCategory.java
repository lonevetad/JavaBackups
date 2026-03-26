package games.generic.controlModel.attributes;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.IEquipmentUpgrade;
import games.generic.controlModel.items.IEquipmentUpgradeCategory;
import games.generic.controlModel.misc.RangedAmountInt;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * An {@link EquipmentItem} might have some restrictions about the amount of
 * {@link IEquipmentUpgrade} it could hold at the same time. That amount could
 * be either a general one, or a specific one, i.e. a
 * per-{@link IEquipmentUpgradeCategory}
 */
public abstract class MaxUpgradesPerCategory implements JSONable {
	private static final long serialVersionUID = -4000000102054402248L;
	public static final String FIELD_UPGRADE_CATEGORY = "upgradeCategory";

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

	public MaxUpgradesPerCategory(IEquipmentUpgradeCategory upgradeCategory) {
		this();
		this.upgradeCategory = upgradeCategory;
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

	//

	// JSON-related

	//

	public abstract void loadEquipmentUpgradeCategory(GModality gm, String name);

	@Override
	public void toJSONValue(JSONObject wrapper) {
		this.maxUpgradableAmounts.toJSONValue(wrapper);
		// wrapper.addField(FIELD_UPGRADE_CATEGORY, this.upgradeCategory.toJSONValue());
	}

	@Override
	public JSONValue toJSONValue() {
		return this.maxUpgradableAmounts.toJSONValue();
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		if (wrapper == null) {
			throw new IllegalArgumentException("Provided JSON wrapper cannot be null");
		}
		if (this.getMaxUpgradableAmounts() == null) {
			this.setMaxUpgradableAmounts(new RangedAmountInt(1)); // dummy default
		}
		this.getMaxUpgradableAmounts().loadFromJSONObject(gm, wrapper);
		// upgradeCategory
		if (wrapper.hasField(FIELD_UPGRADE_CATEGORY)) { // optional
			// this.raiseExceptionMissingField(FIELD_UPGRADE_CATEGORY, JSONTypes.Object);
			JSONValue upgradeCategoryJSONed_value = wrapper.getFieldValue(FIELD_UPGRADE_CATEGORY);
			JSONString upgradeCategory_Name_JSONed = null;
			if (upgradeCategoryJSONed_value.isType(JSONTypes.String)) {
				upgradeCategory_Name_JSONed = (JSONString) upgradeCategoryJSONed_value;
			} else {
				if (!upgradeCategoryJSONed_value.isType(JSONTypes.Object)) {
					this.raiseExceptionIllegalTypeField(FIELD_UPGRADE_CATEGORY, JSONTypes.Object,
							upgradeCategoryJSONed_value);
				}
				JSONObject upgradeCategoryJSONed = (JSONObject) upgradeCategoryJSONed_value;
				if (!upgradeCategoryJSONed.hasField(ObjectNamed.FIELD_NAME)) {
					this.raiseExceptionMissingField(
							FIELD_UPGRADE_CATEGORY + JSONable.SEPARATOR_FIELD + ObjectNamed.FIELD_NAME,
							JSONTypes.String);
				}
				JSONValue upgradeCategory_Name_JSONed_value = upgradeCategoryJSONed
						.getFieldValue(ObjectNamed.FIELD_NAME);
				if (!upgradeCategory_Name_JSONed_value.isType(JSONTypes.String)) {
					this.raiseExceptionIllegalTypeField(
							FIELD_UPGRADE_CATEGORY + JSONable.SEPARATOR_FIELD + ObjectNamed.FIELD_NAME,
							JSONTypes.String, upgradeCategory_Name_JSONed_value);
				}
				upgradeCategory_Name_JSONed = (JSONString) upgradeCategory_Name_JSONed_value;
			}
			this.loadEquipmentUpgradeCategory(gm, upgradeCategory_Name_JSONed.asString());
		}
	}

	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}
		if (this.getMaxUpgradableAmounts() == null) {
			this.setMaxUpgradableAmounts(new RangedAmountInt(1)); // dummy default
		}
		this.getMaxUpgradableAmounts().loadFromJSONMap(gm, jsonMap);
		// upgradeCategory
		if (jsonMap.containsKey(FIELD_UPGRADE_CATEGORY)) { // optional
			// this.raiseExceptionMissingField(FIELD_UPGRADE_CATEGORY, JSONTypes.Object);
			Object upgradeCategoryJSONed_value = jsonMap.get(FIELD_UPGRADE_CATEGORY);
			String upgradeCategory_Name = null;
			if (upgradeCategoryJSONed_value instanceof String) {
				upgradeCategory_Name = (String) upgradeCategoryJSONed_value;
			} else {
				if (!(upgradeCategoryJSONed_value instanceof Map<?, ?>)) {
					this.raiseExceptionIllegalTypeField(FIELD_UPGRADE_CATEGORY, JSONTypes.Object,
							upgradeCategoryJSONed_value);
				}
				Map<String, Object> upgradeCategoryJSONed = (Map<String, Object>) upgradeCategoryJSONed_value;
				if (!upgradeCategoryJSONed.containsKey(ObjectNamed.FIELD_NAME)) {
					this.raiseExceptionMissingField(
							FIELD_UPGRADE_CATEGORY + JSONable.SEPARATOR_FIELD + ObjectNamed.FIELD_NAME,
							JSONTypes.String);
				}
				Object upgradeCategory_Name_value = upgradeCategoryJSONed.get(ObjectNamed.FIELD_NAME);
				if (!(upgradeCategory_Name_value instanceof Map<?, ?>)) {
					this.raiseExceptionIllegalTypeField(
							FIELD_UPGRADE_CATEGORY + JSONable.SEPARATOR_FIELD + ObjectNamed.FIELD_NAME,
							JSONTypes.String, upgradeCategory_Name_value);
				}
				upgradeCategory_Name = (String) upgradeCategory_Name_value;
			}
			this.loadEquipmentUpgradeCategory(gm, upgradeCategory_Name);
		}
	}

	@Override
	public abstract MaxUpgradesPerCategory clone();
}
