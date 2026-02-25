package games.generic.controlModel.misc;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import tools.ObjectNamedID;
import tools.json.JSONTypes;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;

/**
 * An {@code int} amount ({@code value}, returned by {@link #getValue()} with a
 * {@code name} associated. That {@code name} is provided by {@link #getName()},
 * which delegates the call to the delegated value {@link ObjectNamedID}
 * (returned by {@link #getType()}) .
 *
 * @author ottin
 *
 */
public class AmountNamed implements ObjectNamed {
	private static final long serialVersionUID = -54562455221147L;
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_VALUE = "value";

	protected int value;
	protected ObjectNamedID type;

	public AmountNamed(ObjectNamedID type, int value) {
		super();
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
		if (!wrapper.hasField(FIELD_VALUE)) {
			this.raiseExceptionMissingField(FIELD_VALUE, JSONTypes.Int);
		}
		// TODO
	}

	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		// TODO
	}
}