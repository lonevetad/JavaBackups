package games.generic.controlModel.items;

import java.util.Map;
import java.util.function.Function;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.AttributesUpgrade;
import games.generic.controlModel.holders.PriceHolder;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONBoolean;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * Simple extension to mark an {@link AttributeUpgrade} designed for
 * {@link EquipmentItem}s.
 */
public interface IEquipmentUpgrade extends AttributesUpgrade, PriceHolder {
	public static final Function<IEquipmentUpgrade, String> KEY_EXTRACTOR = IEquipmentUpgrade::getName;
	public static final String FIELD_IS_PREFIX = "isPrefix";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_UPGRADE_CATEGORY = "category"; // upgradeCategory

	public boolean isPrefix();

	public String getDescription();

	/**
	 * Instance-related : do not serialize
	 */
	public EquipmentItem getEquipmentAssigned();

	public IEquipmentUpgradeCategory getUpgradeCategory();

	//

	public void setIsPrefix(boolean flag);

	public void setDescription(String description);

	public void setEquipmentAssigned(EquipmentItem equipmentAssigned);

	public void setUpgradeCategory(IEquipmentUpgradeCategory upgradeCategory);

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		AttributesUpgrade.super.toJSONValue(wrapper);
		PriceHolder.super.toJSONValue(wrapper);
		wrapper.addField(FIELD_IS_PREFIX, new JSONBoolean(this.isPrefix()));
		wrapper.addField(FIELD_DESCRIPTION, new JSONString(this.getDescription()));
		wrapper.addField(FIELD_UPGRADE_CATEGORY, this.getUpgradeCategory().toJSONValue());
	}

	/**
	 * The UpgradeCategory are game-specific, so they cannot be loaded in the
	 * generic package.
	 */
	public IEquipmentUpgradeCategory loadIEquipmentUpgradeCategory(GModality gm, String categoryName);

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		AttributesUpgrade.super.loadFromJSONObject(gm, wrapper);
		PriceHolder.super.loadFromJSONObject(gm, wrapper);
		// isPrefix
		if (!wrapper.hasField(FIELD_IS_PREFIX)) {
			this.setIsPrefix(false);
		} else {
			this.setIsPrefix(wrapper.getFieldValue(FIELD_IS_PREFIX).asBoolean());
		}
		// description (optional)
		if (wrapper.hasField(FIELD_DESCRIPTION)) {
			this.setDescription(wrapper.getFieldValue(FIELD_DESCRIPTION).asString());
		} else {
			this.setDescription(null);
		}
		// UpgradeCategory's name
		// equip upgrade category
		if (!wrapper.hasField(FIELD_UPGRADE_CATEGORY)) {
			this.raiseExceptionMissingField(FIELD_UPGRADE_CATEGORY, JSONTypes.String);
		}
		JSONValue upgradeCategoryJSONed_value = wrapper.getFieldValue(FIELD_UPGRADE_CATEGORY);
		if (!upgradeCategoryJSONed_value.isType(JSONTypes.String)) {
			this.raiseExceptionIllegalTypeField(FIELD_UPGRADE_CATEGORY, JSONTypes.String, upgradeCategoryJSONed_value);
		}
		this.setUpgradeCategory(this.loadIEquipmentUpgradeCategory(gm, upgradeCategoryJSONed_value.asString()));
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		AttributesUpgrade.super.loadFromJSONMap(gm, jsonMap);
		PriceHolder.super.loadFromJSONMap(gm, jsonMap);
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
		// equip upgrade category
		if (!jsonMap.containsKey(FIELD_UPGRADE_CATEGORY)) {
			this.raiseExceptionMissingField(FIELD_UPGRADE_CATEGORY, JSONTypes.String);
		}
		Object upgradeCategoryMapped_value = jsonMap.get(FIELD_UPGRADE_CATEGORY);
		if (!(upgradeCategoryMapped_value instanceof String)) {
			this.raiseExceptionIllegalTypeField(FIELD_UPGRADE_CATEGORY, JSONTypes.String, upgradeCategoryMapped_value);
		}
		String upgradeCategoryName = (String) upgradeCategoryMapped_value;
		this.setUpgradeCategory(this.loadIEquipmentUpgradeCategory(gm, upgradeCategoryName));
	}
}