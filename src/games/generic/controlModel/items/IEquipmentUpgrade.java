package games.generic.controlModel.items;

import java.util.Map;
import java.util.function.Function;

import games.generic.controlModel.attributes.AttributesUpgrade;
import games.generic.controlModel.currency.CurrencySet;
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

	@Override
	public default void loadFromJSONMap(Map<String, Object> jsonMap);

	@Override
	public default void loadFromJSONObject(JSONObject wrapper);
}