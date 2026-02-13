package tools.json.types;

import tools.json.JSONTypes;
import tools.json.JSONValue;

public class JSONLong extends JSONValue {
	private static final long serialVersionUID = -5641554230425403L;
	protected long value;

	public JSONLong(long value) {
		super();
		this.value = value;
	}

	@Override
	public long asLong() { return this.value; }

	@Override
	public int asInt() {
		System.out.println("LONG TO int: " + this.value);
		return (int) this.value;
	}

	@Override
	public double asDouble() {
		System.out.println("LONG TO double: " + this.value);
		return this.value;
	}

	@Override
	public JSONTypes getType() { return JSONTypes.Long; }

	@Override
	public void toString(StringBuilder sb) { sb.append(Long.toString(value)); }

	@Override
	public Object asObject() { return Long.valueOf(this.value); }
}