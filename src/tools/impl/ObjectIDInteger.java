package tools.impl;

import java.util.Comparator;
import java.util.Map;

import games.generic.controlModel.GModality;
import tools.Comparators;
import tools.ObjWithIDGeneric;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;

/** Not deprecated but should not be used. */
public class ObjectIDInteger implements ObjWithIDGeneric<Integer> {
	private static final long serialVersionUID = -70321584892380L;
	private static int progressiveID = 0;
	public static final Comparator<ObjectIDInteger> COMPARATOR_OWID = (o1, o2) -> {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return -1;
		}
		if (o2 == null) {
			return 1;
		}
		return Comparators.INTEGER_COMPARATOR.compare(o1.ID, o2.ID);
	};

	public static int getProgressiveID() {
		return progressiveID;
	}

	//

	//

	public ObjectIDInteger() {
		super();
		this.ID = ++progressiveID;
	}

	protected Integer ID;

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public boolean setID(Integer newID) {
		if (this.ID != null || newID == null) {
			return false;
		}
		this.ID = newID;
		return true;
	}

	//

	// JSON-related

	//

	@Override
	public JSONValue newJSONValueForID() {
		return new JSONInt(this.ID);
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		if (wrapper == null) {
			throw new IllegalArgumentException("Provided JSONObject wrapper cannot be null");
		}
		if (!wrapper.hasField(FIELD_ID)) {
			this.raiseExceptionMissingField(FIELD_ID, JSONTypes.Int);
		}
		JSONValue idJSONed_value = wrapper.getFieldValue(FIELD_ID);
		if (!idJSONed_value.isType(JSONTypes.Int)) {
			this.raiseExceptionIllegalTypeField(FIELD_ID, JSONTypes.Int, idJSONed_value);
		}
		this.setID(((JSONInt) idJSONed_value).asInt());
	}

	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSONObject map cannot be null");
		}
		if (!jsonMap.containsKey(FIELD_ID)) {
			this.raiseExceptionMissingField(FIELD_ID, JSONTypes.Int);
		}
		Object idJSONed_value = jsonMap.get(FIELD_ID);
		if (!(idJSONed_value instanceof Integer)) {
			this.raiseExceptionIllegalTypeField(FIELD_ID, JSONTypes.Int, idJSONed_value);
		}
		this.setID(((Integer) idJSONed_value));
	}
}