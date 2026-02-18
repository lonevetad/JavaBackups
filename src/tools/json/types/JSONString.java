package tools.json.types;

import tools.json.JSONTypes;
import tools.json.JSONValue;

public class JSONString extends JSONValue {
	private static final long serialVersionUID = -5641554230425402L;
	protected String value;

	public JSONString(String value) {
		super();
		this.value = value;
	}

	public boolean isPrimitive() {
		return true;
	}

	@Override
	public String asString() {
		return this.value;
	}

	@Override
	public JSONTypes getType() {
		return JSONTypes.String;
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append('\"').append(
				value
						.replace("\\", "\\\\")
						.replace("\"", "\\\"")
		// this resuls from "hello" to \"hello\"
		// and \"hi\" to \\\"hi\\\"
		).append('\"');
	}

	@Override
	public Object asObject() {
		return this.value;
	}
}
