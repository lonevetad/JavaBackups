package games.generic.controlModel.items;

import java.util.Map;
import java.util.function.Function;

import games.generic.controlModel.attributes.AttributesUpgrade;
import tools.json.types.JSONObject;

/**
 * Simple extension to mark an {@link AttributeUpgrade} designed for
 * {@link EquipmentItem}s.
 */
public interface IEquipmentUpgrade extends AttributesUpgrade {
	public static final Function<IEquipmentUpgrade, String> KEY_EXTRACTOR = eu -> eu.getName();
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_UPGRADE_CATEGORY = "upgradeCategory";

	public String getDescription();

	/**
	 * Instance-related : do not serialize
	 */
	public EquipmentItem getEquipmentAssigned();

	public IEquipmentUpgradeCategory getUpgradeCategory();

	//

	public void setDescription(String description);

	public void setEquipmentAssigned(EquipmentItem equipmentAssigned);

	public void setUpgradeCategory(IEquipmentUpgradeCategory upgradeCategory);

	//

	// JSON-related

	//

	@Override
	public void toJSONValue(JSONObject wrapper);

	@Override
	public void loadFromJSONMap(Map<String, Object> jsonMap);

	@Override
	public void loadFromJSONObject(JSONObject wrapper);
}