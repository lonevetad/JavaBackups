package games.generic.controlModel.misc;

import java.util.Map;

import tools.ObjectNamedID;
import tools.json.JSONable;
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
public class AmountNamed implements JSONable {
	private static final long serialVersionUID = -54562455221147L;
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
	public void toJSONValue(JSONObject wrapper);

	@Override
	public void loadFromJSONMap(Map<String, Object> jsonMap);

	@Override
	public void loadFromJSONObject(JSONObject wrapper);
}