package games.generic.controlModel.misc.uidp;

import java.util.Map;
import java.util.function.Function;

import games.generic.controlModel.GModality;
import tools.UniqueIDProvider;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONLong;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

public class UIDPState implements JSONable {
	public static final String FIELD_STATE = "state";
	public static final String FIELD_CLASS_IDENTIFIER = "classIdentifier";
	public static final Function<Class<?>, String> CLASS_TO_NAME = Class::getName;
	private static final long serialVersionUID = -401587584588922221L;

	public static UIDPState newEmpty(Class<?> clazz) {
		return new UIDPState(clazz, 0);
	}

	public static UIDPState newEmpty(String classIdentifier) {
		return new UIDPState(classIdentifier, 0);
	}

	//

	public UIDPState(Class<?> clazz, long state) {
		this(CLASS_TO_NAME.apply(clazz), state);
	}

	public UIDPState(String classIdentifier, long state) {
		super();
		this.state = state;
		this.classIdentifier = classIdentifier;
	}

	protected long state;
	/**
	 * Some Classes may use a private {@link UniqueIDProvider}. This variable is
	 * used to identify that class.
	 */
	protected String classIdentifier;

	public long getState() {
		return state;
	}

	public String getClassIdentifier() {
		return classIdentifier;
	}

	//

	// JSON-related

	//

	@Override
	public void toJSONValue(JSONObject wrapper) {
		wrapper.addField(FIELD_STATE, new JSONLong(state));
		wrapper.addField(FIELD_CLASS_IDENTIFIER, new JSONString(classIdentifier));
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		// state
		if (!wrapper.hasField(FIELD_STATE)) {
			this.raiseExceptionMissingField(FIELD_STATE, JSONTypes.Long);
		}
		JSONValue stateJSONed_value = wrapper.getFieldValue(FIELD_STATE);
		if (stateJSONed_value.isType(JSONTypes.Int)) {
			this.state = stateJSONed_value.asInt();
		} else if (stateJSONed_value.isType(JSONTypes.Long)) {
			this.state = stateJSONed_value.asLong();
		} else {
			this.raiseExceptionIllegalTypeField(FIELD_STATE, JSONTypes.Int, stateJSONed_value);
		}
		// classIdentifier
		if (!wrapper.hasField(FIELD_CLASS_IDENTIFIER)) {
			this.raiseExceptionMissingField(FIELD_CLASS_IDENTIFIER, JSONTypes.String);
		}
		JSONValue classIdentifierJSONed_value = wrapper.getFieldValue(FIELD_CLASS_IDENTIFIER);
		if (!classIdentifierJSONed_value.isType(JSONTypes.String)) {
			this.raiseExceptionIllegalTypeField(FIELD_CLASS_IDENTIFIER, JSONTypes.String, classIdentifierJSONed_value);
		}
		this.classIdentifier = classIdentifierJSONed_value.asString();
	}

	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		// state
		if (!jsonMap.containsKey(FIELD_STATE)) {
			this.raiseExceptionMissingField(FIELD_STATE, JSONTypes.Long);
		}
		Object stateJSONed_value = jsonMap.get(FIELD_STATE);
		if (stateJSONed_value instanceof Integer) {
			this.state = ((Integer) stateJSONed_value).longValue();
		} else if (stateJSONed_value instanceof Long) {
			this.state = ((Long) stateJSONed_value).longValue();
		} else {
			this.raiseExceptionIllegalTypeField(FIELD_STATE, JSONTypes.Int, stateJSONed_value);
		}
		// classIdentifier
		if (!jsonMap.containsKey(FIELD_CLASS_IDENTIFIER)) {
			this.raiseExceptionMissingField(FIELD_CLASS_IDENTIFIER, JSONTypes.String);
		}
		Object classIdentifierJSONed_value = jsonMap.get(FIELD_CLASS_IDENTIFIER);
		if (!(classIdentifierJSONed_value instanceof String)) {
			this.raiseExceptionIllegalTypeField(FIELD_CLASS_IDENTIFIER, JSONTypes.String, classIdentifierJSONed_value);
		}
		this.classIdentifier = (String) classIdentifierJSONed_value;
	}

}