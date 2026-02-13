package tools.json.types;

import tools.json.JSONTypes;
import tools.json.JSONValue;

public class JSONBoolean extends JSONValue {
	private static final long serialVersionUID = -5641554230425405L;

	public JSONBoolean(boolean value) {
		super();
		this.value = value;
	}

	protected boolean value;

	@Override
	public boolean asBoolean() { return this.value; }

	@Override
	public JSONTypes getType() { return JSONTypes.Boolean; }

	@Override
	public void toString(StringBuilder sb) { sb.append(Boolean.toString(value)); }

	@Override
	public Object asObject() { return this.value ? Boolean.TRUE : Boolean.FALSE; }
}