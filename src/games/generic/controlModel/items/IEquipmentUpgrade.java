package games.generic.controlModel.items;

import java.util.Map;
import java.util.function.Function;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.AttributesUpgrade;
import games.generic.controlModel.currency.CurrencySet;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * Simple extension to mark an {@link AttributeUpgrade} designed for
 * {@link EquipmentItem}s.
 */
public interface IEquipmentUpgrade extends AttributesUpgrade {
	public static final Function<IEquipmentUpgrade, String> KEY_EXTRACTOR = eu -> eu.getName();
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_UPGRADE_CATEGORY = "upgradeCategory";
	public static final String FIELD_PRICES_MODIFICATIONS = "pricesModifications";

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
		loadIEquipmentUpgradeCategory(gm, wrapper);
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		AttributesUpgrade.super.loadFromJSONMap(gm, jsonMap);
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
		loadIEquipmentUpgradeCategory(gm, jsonMap);
	}
}