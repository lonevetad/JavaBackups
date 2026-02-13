package tools.json.types;

import java.util.function.BiConsumer;

import tools.json.JSONTypes;
import tools.json.JSONValue;

public class JSONArray extends JSONValue {
	private static final long serialVersionUID = -5641554230425407L;
	protected boolean isHomogeneous = true;
	protected JSONValue[] array;
	protected JSONTypes elementsTypes;

	protected JSONArray() {
		this.isHomogeneous = true;
		this.array = null;
		this.elementsTypes = JSONTypes.ArrayMiscTypes;
	}

	public JSONArray(JSONTypes arrayType, JSONValue[] values, JSONTypes elementsTypes) {
		this();
		this.elementsTypes = elementsTypes;
		this.isHomogeneous = JSONTypes.ArrayHomogeneousType == arrayType;
		this.array = values;
	}

	public void forEach(BiConsumer<? super Integer, ? super JSONValue> action) {
		int i;
		if (this.array == null || this.array.length == 0) { return; }
		i = 0;
		for (JSONValue val : this.array) {
			action.accept(i++, val);
		}
	}

	@Override
	public void toString(StringBuilder sb) {
		boolean isNotFirst = false;
		sb.append('[');
		for (JSONValue val : this.array) {
			if (isNotFirst) {
				sb.append(',');
			} else {
				isNotFirst = true;
			}
			val.toString(sb);
		}
		sb.append(']');
	}

	@Override
	public JSONTypes getType() { return isHomogeneous ? JSONTypes.ArrayHomogeneousType : JSONTypes.ArrayMiscTypes; }

	@Override
	public Object asObject() { return this.asArrayObject(); }

	// TODO: scoprire se l'array è omogeneo, se il tipo del primo elemento
	// corrisponde, sennò ECCEZIONE

	public boolean isHomogeneous() { return isHomogeneous; }

	public JSONTypes getElementsTypes() { return elementsTypes; }

	public JSONValue getAt(int i) { return array[i]; }

	public int getElementsAmount() { return this.length(); }

	public int length() { return this.array.length; }

	public void setElementsTypes(JSONTypes elementsTypes) { this.elementsTypes = elementsTypes; }

	//

	@Override
	public int[] asArrayInt() {
		int i = 0;
		int[] arr = new int[this.array.length];
		for (JSONValue e : this.array) {
			arr[i++] = e.asInt();
		}
		return arr;
	}

	@Override
	public String[] asArrayString() {
		int i = 0;
		String[] arr = new String[this.array.length];
		for (JSONValue e : this.array) {
			arr[i++] = e.asString();
		}
		return arr;
	}

	@Override
	public long[] asArrayLong() {
		int i = 0;
		long[] arr = new long[this.array.length];
		for (JSONValue e : this.array) {
			arr[i++] = e.asLong();
		}
		return arr;
	}

	@Override
	public double[] asArrayDouble() {
		int i = 0;
		double[] arr = new double[this.array.length];
		for (JSONValue e : this.array) {
			arr[i++] = e.asDouble();
		}
		return arr;
	}

	@Override
	public boolean[] asArrayBoolean() {
		int i = 0;
		boolean[] arr = new boolean[this.array.length];
		for (JSONValue e : this.array) {
			arr[i++] = e.asBoolean();
		}
		return arr;
	}

	@Override
	public Object[] asArrayObject() {
		int i = 0;
		Object[] arr = new Object[this.array.length];
		for (JSONValue e : this.array) {
			arr[i++] = e.asObject();
		}
		return arr;
	}
}