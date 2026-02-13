package tools.json.types;

import tools.json.JSONTypes;
import tools.json.JSONValue;

public class JSONInt extends JSONValue {
	private static final long serialVersionUID = -5641554230425401L;
	protected int value;

	public JSONInt(int value) {
		super();
		this.value = value;
	}

	@Override
	public int asInt() { return this.value; }

	@Override
	public long asLong() { return this.value; }

	@Override
	public JSONTypes getType() { return JSONTypes.Int; }

	@Override
	public void toString(StringBuilder sb) { sb.append(Integer.toString(value)); }

	@Override
	public Object asObject() { return Integer.valueOf(this.value); }
}