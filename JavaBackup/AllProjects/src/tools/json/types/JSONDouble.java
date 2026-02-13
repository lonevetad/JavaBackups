package tools.json.types;

import tools.json.JSONTypes;
import tools.json.JSONValue;

public class JSONDouble extends JSONValue {
	private static final long serialVersionUID = -5641554230425404L;
	protected double value;

	public JSONDouble(double value) {
		super();
		this.value = value;
	}

	@Override
	public double asDouble() { return this.value; }

	@Override
	public JSONTypes getType() { return JSONTypes.Double; }

	@Override
	public void toString(StringBuilder sb) { sb.append(Double.toString(value)); }

	@Override
	public Object asObject() { return Double.valueOf(this.value); }
}
