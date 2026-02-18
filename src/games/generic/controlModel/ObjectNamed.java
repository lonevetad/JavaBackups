package games.generic.controlModel;

import java.util.Comparator;
import java.util.Map;

import tools.Comparators;
import tools.json.JSONTypes;
import tools.json.JSONable;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

public interface ObjectNamed extends JSONable {

	public static final String FIELD_NAME = "name";

	public static final Comparator<ObjectNamed> COMPARATOR_OBJECT_NAMED = (o1, o2) -> {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		return Comparators.STRING_COMPARATOR.compare(o1.getName(), o2.getName());
	};

	/**
	 * The "name" of this instance, that is a String identifier that should be
	 * unique.
	 */
	public String getName();

	public default ObjectNamed setName(String name) {
		return this; // empty implementation
	}

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		JSONString jsonedName = new JSONString(this.getName());
		wrapper.addField(FIELD_NAME, jsonedName);
	}

	@Override
	public default void loadFromJSONMap(Map<String, Object> jsonMap) {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}
		if (!jsonMap.containsKey(FIELD_NAME) || !(jsonMap.get(FIELD_NAME) instanceof String)) {
			this.raiseExceptionIllegalTypeField(FIELD_NAME);
		}
		this.setName((String)jsonMap.get(FIELD_NAME));
	}

	@Override
	public default void loadFromJSONObject(JSONObject wrapper) {
		if (!wrapper.hasField(FIELD_NAME)) {
			this.raiseExceptionMissingField(FIELD_NAME, JSONTypes.String);
		}
		this.setName(wrapper.getFieldValue(FIELD_NAME).asString());
	}
}