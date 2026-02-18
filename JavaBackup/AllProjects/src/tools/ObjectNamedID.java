package tools;

import java.util.Comparator;
import java.util.Map;

import games.generic.controlModel.ObjectNamed;
import tools.json.JSONValue;
import tools.json.types.JSONObject;

/**
 * Mark an object as identifiable through both its numerical identifier (i.e.
 * {@link #getID()}) and its textual identifier (i.e. {@link #getName()}).
 */
public interface ObjectNamedID extends ObjectWithID, ObjectNamed {
	public static final Comparator<ObjectNamedID> COMPARATOR_OBJECT_NAMED_ID = (o1, o2) -> {
		int res;
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		res = Comparators.STRING_COMPARATOR.compare(o1.getName(), o2.getName());
		if (res != 0) {
			return res;
		}
		return Comparators.LONG_COMPARATOR.compare(o1.getID(), o2.getID());
	};

	//

	// JSON-related

	//

	@Override
	public default void loadFromJSONObject(JSONObject wrapper) {
		ObjectWithID.super.loadFromJSONObject(wrapper);
		ObjectNamed.super.loadFromJSONObject(wrapper);
	}

	/**
	 * Since this instance is NOT a primitive value, then it has some instance
	 * fields -> convert
	 * this instance into a "json-map" or "json-object" by filling the already
	 * provided instance (via {@link JSONObject#addField(String, JSONValue)}).
	 * 
	 * @param wrapper the {@link JSONObject} representing this instance and that
	 *                needs to be filled via
	 *                {@link JSONObject#addField(String, JSONValue)}.
	 */
	public default void toJSONValue(JSONObject wrapper) {
		ObjectWithID.super.toJSONValue(wrapper);
		ObjectNamed.super.toJSONValue(wrapper);
	}

	public default void loadFromJSONMap(Map<String, Object> jsonMap) {
		ObjectWithID.super.loadFromJSONMap(jsonMap);
		ObjectNamed.super.loadFromJSONMap(jsonMap);
	}

}