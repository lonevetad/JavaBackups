package tools.json;

import java.io.Serializable;

public abstract class JSONValue implements Serializable {
	private static final long serialVersionUID = -5641554230425400L;

	public abstract JSONTypes getType();

	public boolean isType(JSONTypes t) { return t == this.getType(); }

	public int asInt() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public String asString() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public long asLong() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public double asDouble() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public boolean asBoolean() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public Object asObject() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public int[] asArrayInt() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public String[] asArrayString() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public long[] asArrayLong() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public double[] asArrayDouble() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public boolean[] asArrayBoolean() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	public Object[] asArrayObject() {
		throw new UnsupportedOperationException("Unsupported operation for type: " + this.getType().name());
	}

	@Override
	public final String toString() {
		StringBuilder sb;
		sb = new StringBuilder();
		this.toString(sb);
		return sb.toString();
	}

	public abstract void toString(StringBuilder sb);
}
