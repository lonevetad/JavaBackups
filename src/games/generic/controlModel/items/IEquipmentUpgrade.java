package games.generic.controlModel.items;

import java.util.Map;
import java.util.function.Function;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.AttributesUpgrade;
import games.generic.controlModel.currency.CurrencySet;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONBoolean;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * Simple extension to mark an {@link AttributeUpgrade} designed for
 * {@link EquipmentItem}s.
 */
public interface IEquipmentUpgrade extends AttributesUpgrade {
	public static final Function<IEquipmentUpgrade, String> KEY_EXTRACTOR = eu -> eu.getName();
	public static final String FIELD_IS_PREFIX = "isPrefix";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_UPGRADE_CATEGORY = "upgradeCategory";
	public static final String FIELD_PRICES_MODIFICATIONS = "pricesModifications";

	public boolean isPrefix();

	public String getDescription();

	/**
	 * Instance-related : do not serialize
	 */
	public EquipmentItem getEquipmentAssigned();

	public IEquipmentUpgradeCategory getUpgradeCategory();

	/**
	 * Any attributes could apply a bonus or a malus to the price of everything it's
	 * applied on.
	 */
	public CurrencySet getPricesModifications();

	//

	public void setIsPrefix(boolean flag);

	public void setDescription(String description);

	public void setEquipmentAssigned(EquipmentItem equipmentAssigned);

	public void setUpgradeCategory(IEquipmentUpgradeCategory upgradeCategory);

	public void setPricesModifications(CurrencySet priceModifications);

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		AttributesUpgrade.super.toJSONValue(wrapper);
		JSONObject priceModsJsoned = new JSONObject();
		this.getPricesModifications().toJSONValue(priceModsJsoned);
		wrapper.addField(FIELD_PRICES_MODIFICATIONS, priceModsJsoned);
		wrapper.addField(FIELD_IS_PREFIX, new JSONBoolean(this.isPrefix()));
		wrapper.addField(FIELD_DESCRIPTION, new JSONString(this.getDescription()));
		JSONObject upgradeCategoryJsoned = new JSONObject();
		this.getUpgradeCategory().toJSONValue(upgradeCategoryJsoned);
		wrapper.addField(FIELD_UPGRADE_CATEGORY, upgradeCategoryJsoned);
	}

	/**
	 * The UpgradeCategory are game-specific, so they cannot be loaded in the
	 * generic package.
	 */
	public void loadIEquipmentUpgradeCategory(GModality gm, String categoryName);

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		AttributesUpgrade.super.loadFromJSONObject(gm, wrapper);
		// isPrefix
		if (!wrapper.hasField(FIELD_IS_PREFIX)) {
			this.setIsPrefix(false);
		} else {
			this.setIsPrefix(wrapper.getFieldValue(FIELD_IS_PREFIX).asBoolean());
		}
		// description
		if (!wrapper.hasField(FIELD_DESCRIPTION)) {
			this.raiseExceptionMissingField(FIELD_DESCRIPTION, JSONTypes.String);
		}
		this.setDescription(wrapper.getFieldValue(FIELD_DESCRIPTION).asString());
		// prices
		if (!wrapper.hasField(FIELD_PRICES_MODIFICATIONS)) {
			this.raiseExceptionMissingField(FIELD_PRICES_MODIFICATIONS, JSONTypes.Object);
		}
		JSONValue pricesModsJSONed = wrapper.getFieldValue(FIELD_PRICES_MODIFICATIONS);
		if (!pricesModsJSONed.isType(JSONTypes.Object)) {
			this.raiseExceptionIllegalTypeField(FIELD_PRICES_MODIFICATIONS, JSONTypes.Object, pricesModsJSONed);
		}
		CurrencySet cs = gm.getGameObjectsProvider().newCurrencyHolder();
		cs.loadFromJSONObject(gm, (JSONObject) pricesModsJSONed);
		// UpgradeCategory's name
		// equip upgrade category
		if (!wrapper.hasField(FIELD_UPGRADE_CATEGORY)) {
			this.raiseExceptionMissingField(FIELD_UPGRADE_CATEGORY, JSONTypes.Object);
		}
		JSONValue upgradeCategoryJSONed_value = wrapper.getFieldValue(FIELD_UPGRADE_CATEGORY);
		if (!upgradeCategoryJSONed_value.isType(JSONTypes.Object)) {
			this.raiseExceptionIllegalTypeField(FIELD_UPGRADE_CATEGORY, JSONTypes.Object, upgradeCategoryJSONed_value);
		}
		JSONObject upgradeCategoryJSONed = (JSONObject) upgradeCategoryJSONed_value;
		// the instances of the UpgradeCategory might be an enum, so a custom loading
		// process is needed -> get just the name
		if (!upgradeCategoryJSONed.hasField(FIELD_NAME)) {
			this.raiseExceptionMissingField(FIELD_UPGRADE_CATEGORY + JSONable.SEPARATOR_FIELD + FIELD_NAME,
					JSONTypes.String);
		}
		JSONValue upgradeCategoryJSONed_name_value = upgradeCategoryJSONed.getFieldValue(FIELD_NAME);
		if (!upgradeCategoryJSONed_name_value.isType(JSONTypes.String)) {
			this.raiseExceptionIllegalTypeField(FIELD_UPGRADE_CATEGORY + JSONable.SEPARATOR_FIELD + FIELD_NAME,
					JSONTypes.String,
					upgradeCategoryJSONed_name_value);
		}
		JSONString upgradeCategoryJSONed_name = (JSONString) upgradeCategoryJSONed_name_value;
		loadIEquipmentUpgradeCategory(gm, upgradeCategoryJSONed_name.asString());
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		AttributesUpgrade.super.loadFromJSONMap(gm, jsonMap);
		// isPrefix
		if (!jsonMap.containsKey(FIELD_IS_PREFIX)) {
			this.setIsPrefix(false);
		} else {
			this.setIsPrefix((Boolean) jsonMap.get(FIELD_IS_PREFIX));
		}
		// description
		if (!jsonMap.containsKey(FIELD_DESCRIPTION)) {
			this.raiseExceptionMissingField(FIELD_DESCRIPTION, JSONTypes.String);
		}
		this.setDescription((String) jsonMap.get(FIELD_DESCRIPTION));
		// prices
		if (!jsonMap.containsKey(FIELD_PRICES_MODIFICATIONS)) {
			this.raiseExceptionMissingField(FIELD_PRICES_MODIFICATIONS, JSONTypes.Object);
		}
		Object pricesModsJSONed = jsonMap.get(FIELD_PRICES_MODIFICATIONS);
		if (!(pricesModsJSONed instanceof Map<?, ?>)) {
			this.raiseExceptionIllegalTypeField(FIELD_PRICES_MODIFICATIONS, JSONTypes.Object, pricesModsJSONed);
		}
		CurrencySet cs = gm.getGameObjectsProvider().newCurrencyHolder();
		cs.loadFromJSONMap(gm, (Map<String, Object>) pricesModsJSONed);
		// equip upgrade category
		if (!jsonMap.containsKey(FIELD_UPGRADE_CATEGORY)) {
			this.raiseExceptionMissingField(FIELD_UPGRADE_CATEGORY, JSONTypes.Object);
		}
		Object upgradeCategoryMapped_value = jsonMap.get(FIELD_UPGRADE_CATEGORY);
		if (!(upgradeCategoryMapped_value instanceof Map<?, ?>)) {
			this.raiseExceptionIllegalTypeField(FIELD_UPGRADE_CATEGORY, JSONTypes.Object, upgradeCategoryMapped_value);
		}
		Map<String, Object> upgradeCategoryMapped = (Map<String, Object>) upgradeCategoryMapped_value;
		// the instances of the UpgradeCategory might be an enum, so a custom loading
		// process is needed -> get just the name
		if (!upgradeCategoryMapped.containsKey(FIELD_NAME)) {
			this.raiseExceptionMissingField(FIELD_UPGRADE_CATEGORY + JSONable.SEPARATOR_FIELD + FIELD_NAME,
					JSONTypes.String);
		}
		Object upgradeCategoryMapped_name_value = upgradeCategoryMapped.get(FIELD_NAME);
		if (!(upgradeCategoryMapped_name_value instanceof String)) {
			this.raiseExceptionIllegalTypeField(FIELD_UPGRADE_CATEGORY + JSONable.SEPARATOR_FIELD + FIELD_NAME,
					JSONTypes.String,
					upgradeCategoryMapped_name_value);
		}
		String upgradeCategoryName = (String) upgradeCategoryMapped_name_value;
		loadIEquipmentUpgradeCategory(gm, upgradeCategoryName);
	}
}