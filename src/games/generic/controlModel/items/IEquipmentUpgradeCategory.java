package games.generic.controlModel.items;

import games.generic.controlModel.misc.IEnumAlike;
import tools.Comparators;
import tools.Comparators.MyComparator;
import tools.json.JSONValue;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * A marker-interface for {@link IEquipmentUpgrade} to define the category that
 * Upgrade belongs to.
 * This interface might be incorporated in an {@link Enum}-
 */
public interface IEquipmentUpgradeCategory extends IEnumAlike {
	public static final MyComparator<IEquipmentUpgradeCategory> COMPARATOR_IEQUIPMENT_UPGRADE_CATEGORY = (ec1, ec2) -> {
		if (ec1 == ec2) {
			return 0;
		}
		if (ec1 == null) {
			return -1;
		}
		if (ec2 == null) {
			return 1;
		}
		return Comparators.STRING_COMPARATOR.compare(ec1.getName(), ec2.getName());
	};

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		this.raiseUnsupportedOperationException("RaritiesTRAn should be transformed into a JSON String");
	}

	@Override
	public default JSONValue toJSONValue() {
		return new JSONString(this.getName());
	}
}
