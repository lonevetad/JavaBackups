package tools.json;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import tools.Comparators;
import tools.Stringable;
import tools.json.types.JSONObject;

public interface JSONable extends Stringable {
	public static final String SEPARATOR_INDEX = "#";
	public static final String SEPARATOR_FIELD = "@";

	// Incremental methods: from and to (usually, those are the one to be
	// overridden)

	/**
	 * Since this instance is NOT a primitive value, then it has some instance
	 * fields -> convert this instance into a "json-map" or "json-object" by filling
	 * the already provided instance (via
	 * {@link JSONObject#addField(String, JSONValue)}).
	 * 
	 * @param wrapper the {@link JSONObject} representing this instance and that
	 *                needs to be filled via
	 *                {@link JSONObject#addField(String, JSONValue)}.
	 */
	public void toJSONValue(JSONObject wrapper);

	/**
	 * Load the values of this instance using the informations from the provided
	 * {@link JSONObject}; the given {@link GModality} may be used to obtain
	 * constant values (like {@link Enum}s.
	 * 
	 * @param gm
	 * @param wrapper
	 */
	public void loadFromJSONObject(GModality gm, JSONObject wrapper);

	/**
	 * Load the values of this instance using the informations from the provided
	 * {@link java.util.Map}<String, Object>; the given {@link GModality} may be
	 * used to obtain constant values (like {@link Enum}s.
	 * 
	 * @param gm
	 * @param wrapper
	 */
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap);

	// "starter" methods: those who start the chain of invocations and deepening
	// recursion

	public default String toJSON() {
		// return this.toJSON(0);
		return this.toJSONValue().toString();
	}

	/**
	 * Convert this instance into a {@link JSONObject}, which can be easily
	 * String-ed.
	 */
	public default JSONValue toJSONValue() {
		JSONObject wrapper = new JSONObject();
		toJSONValue(wrapper);
		return wrapper;
	}

	/**
	 * Should be effectively final.
	 * 
	 * @return a map name-value of the fields of this instance
	 */
	public default Map<String, Object> toJSONMap() {
		JSONValue jvThis_value = this.toJSONValue();
		if (!jvThis_value.isType(JSONTypes.Object)) {
			throw new RuntimeException("ERROR: " + this.getClass().getName()
					+ " should have returned a JSONObject while calling toJSONMap(), but a "
					+ jvThis_value.getType().name() + " has been returned instead");
		}
		JSONObject jvThis = (JSONObject) jvThis_value;
		return jvThis.toMapFields();
	}

	/**
	 * Should not be overridden, unless another subclass of {@link JSONParser} is
	 * desired.
	 * 
	 * @param jsonString a valid JSON string representing this object
	 * @throws IllegalArgumentException
	 */
	public default void loadFromJSON(GModality gm, String jsonString) throws IllegalArgumentException {
		JSONValue read = JSONParser.parse(jsonString);
		if (read == null) {
			throw new RuntimeException("The object read is null from te following JSON string:\n\t" + jsonString);
		}
		if (read instanceof JSONObject jsonObj) {
			this.loadFromJSONObject(gm, jsonObj);
		} else {
			this.raiseExceptionIllegalTypeField(read);
		}
	}

	// helpers

	public static Map<String, Object> newFieldValuesMap() {
		return MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
	}

	// exceptions

	public default void raiseUnsupportedOperationException(String msg) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(msg);
	}

	public default void raiseUnsupportedOperationExceptionForMethod(String methodName)
			throws UnsupportedOperationException {
		this.raiseUnsupportedOperationException("ERROR: unsupported execution of the method: " + methodName);
	}

	public default void raiseExceptionIllegalTypeField(String fieldName, JSONTypes expectedType, Object actualValue)
			throws IllegalArgumentException {
		throw new IllegalArgumentException("Invalid JSON value (of type: " + expectedType.getTypeName()
				+ ") for field '" + fieldName + "' in " + this.getClass().getName() + " : " + actualValue);
	}

	public default void raiseExceptionMissingField(String fieldName, JSONTypes expectedType) throws RuntimeException {
		throw new RuntimeException("Field \"" + fieldName + "\" of type " + expectedType.getTypeName()
				+ " not found in " + this.getClass().getName());
	}

	public default void raiseExceptionIllegalTypeField(Object actualValue) throws IllegalArgumentException {
		this.raiseExceptionIllegalTypeField("The actual source", JSONTypes.Object, actualValue);
	}
}