package games.generic.controlModel.misc;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import tools.ObjectNamedID;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * An {@code int} amount ({@code value}, returned by {@link #getValue()} with a
 * {@code name} associated. That {@code name} is provided by {@link #getName()},
 * which delegates the call to the delegated value {@link ObjectNamedID}
 * (returned by {@link #getType()}) .
 *
 * @author ottin
 *
 */
public abstract class AmountNamed implements ObjectNamed {
	private static final long serialVersionUID = -54562455221147L;
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_VALUE = "value";

	protected int value;
	protected ObjectNamedID type;

	public AmountNamed() {
		super();
	}

	public AmountNamed(ObjectNamedID type, int value) {
		this();
		this.type = type;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public ObjectNamedID getType() {
		return type;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setType(ObjectNamedID type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return type.getName();
	}

	@Override
	public String toString() {
		return "AmountNamed [value=" + value + ", type=" + type + "]";
	}

	//

	// JSON-related

	//

	protected void loadNameValue(GModality gm, String name, int value) {
		this.setName(name);
		this.setValue(value);
	}

	/**
	 * Customize the loading of its inner {@link ObjectNamedID} "type" because it
	 * could be an instance of an {@link Enum}, therefore it can't be instantiated
	 * with a plain old "new" (otherwise, memory address equality checks would
	 * fail).
	 * 
	 * @param gm
	 * @param name
	 */
	protected abstract void loadInnerObjectNamedID(GModality gm, String name);

	@Override
	public void toJSONValue(JSONObject wrapper) {
		// no super call due to delegation to the ObjectNamedID "value" field
		JSONInt jsonedValue = new JSONInt(this.getValue());
		wrapper.addField(FIELD_VALUE, jsonedValue);
		JSONObject jsonedType = new JSONObject();
		this.getType().toJSONValue(jsonedType);
		wrapper.addField(FIELD_TYPE, jsonedType);
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		if (wrapper == null) {
			throw new IllegalArgumentException("Provided JSON wrapper cannot be null");
		}
		if (!wrapper.hasField(FIELD_TYPE)) {
			this.raiseExceptionMissingField(FIELD_TYPE, JSONTypes.Object);
		}
		JSONValue typeJSONed_value = wrapper.getFieldValue(FIELD_TYPE);
		if (!typeJSONed_value.isType(JSONTypes.Object)) {
			this.raiseExceptionIllegalTypeField(FIELD_TYPE, JSONTypes.Object, typeJSONed_value);
		}
		JSONObject typeJSONed = (JSONObject) typeJSONed_value;
		if (!typeJSONed.hasField(FIELD_NAME)) {
			this.raiseExceptionMissingField(FIELD_TYPE + SEPARATOR_FIELD + FIELD_NAME, JSONTypes.String);
		}
		JSONValue typeNameJSONed_value = typeJSONed.getFieldValue(FIELD_NAME);
		if (!typeNameJSONed_value.isType(JSONTypes.String)) {
			this.raiseExceptionIllegalTypeField(FIELD_TYPE + SEPARATOR_FIELD + FIELD_NAME, JSONTypes.String,
					typeNameJSONed_value);
		}
		JSONString typeNameJSONed = (JSONString) typeNameJSONed_value;
		String typeName = typeNameJSONed.asString();
		loadInnerObjectNamedID(gm, typeName); // delegate the loading -> see its documentation
		this.getType().loadFromJSONObject(gm, typeJSONed);
		if (!wrapper.hasField(FIELD_VALUE)) {
			this.raiseExceptionMissingField(FIELD_VALUE, JSONTypes.Int);
		}
		JSONValue valueJSONed_value = wrapper.getFieldValue(FIELD_VALUE);
		if (!valueJSONed_value.isType(JSONTypes.Int)) {
			this.raiseExceptionIllegalTypeField(FIELD_TYPE + SEPARATOR_FIELD + FIELD_VALUE, JSONTypes.Int,
					valueJSONed_value);
		}
		JSONInt valueJSONed = (JSONInt) valueJSONed_value;
		int typeValue = valueJSONed.asInt();
		loadNameValue(gm, typeName, typeValue);
	}

	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}
		// TODO
	}
}