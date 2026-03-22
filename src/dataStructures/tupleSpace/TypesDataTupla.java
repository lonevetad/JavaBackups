package dataStructures.tupleSpace;

import java.io.Serializable;

/**
 * No arrays allowed: use tuples instead, because the arrays' types should be
 * specified and it could make it confusing
 */
public enum TypesDataTupla implements Serializable {
	IntNumber(Integer.valueOf(0)), Float(Double.valueOf(0.0)), String(""), Char(Character.valueOf(' ')),
	Bool(Boolean.valueOf(false)), Object(null);
	private TypesDataTupla(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	private final Object defaultValue;

	public Object getDefaultValue() {
		return defaultValue;
	}

	//
	public static final TypesDataTupla[] VALS = TypesDataTupla.values();
	public static final int TYPES_COUNT = VALS.length;
}