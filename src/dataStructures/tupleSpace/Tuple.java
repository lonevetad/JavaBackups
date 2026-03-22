package dataStructures.tupleSpace;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Tuple implements Serializable {
	private static final long serialVersionUID = -659864578327848726L;
	public static final Comparator<Tuple> COMPARATOR_TUPLE = (t1, t2) -> {
		int i, l1, l2, lenMin, c;
		PairTypeValue[] v1, v2;
		if (t1 == t2)
			return 0;
		if (t1 == null)
			return -1;
		if (t2 == null)
			return 1;
		v1 = t1.getValues();
		v2 = t2.getValues();
		i = -1;
		lenMin = (l1 = v1.length) > (l2 = v2.length) ? l2 : l1; // min
		while (++i < lenMin)
			if ((c = v1[i].type.ordinal() - v2[i].type.ordinal()) > 0)
				return 1;
			else if (c < 0)
				return -1;
		// no more items, just compare length: shorter is tinier
		return l1 > l2 ? 1 : -1;
	};

	public static Tuple newEmptyFromTypes(TypesDataTupla[] types) {
		int i;
		PairTypeValue[] values;
		values = new PairTypeValue[types.length];
		i = 0;
		for (TypesDataTupla t : types) {
			values[i++] = new PairTypeValue(t, t.getDefaultValue());
		}
		return new Tuple(values);
	}

	//

	protected Tuple() {
		super();
	}

	public Tuple(PairTypeValue value) {
		this(new PairTypeValue[] { value });
	}

	public Tuple(PairTypeValue[] values) {
		this();
		setValues(values);
	}

	/** BEWARE: no cache !! */
	public TypesDataTupla[] getTypes() {
		int i, len;
		TypesDataTupla[] tt;
		tt = new TypesDataTupla[len = values.length];
		i = -1;
		while (++i < len)
			tt[i] = values[i].type;
		return tt;
	}

	//

	protected PairTypeValue[] values;

	//

	public PairTypeValue[] getValues() {
		return values;
	}

	public Tuple setValues(PairTypeValue[] values) {
		int i, len;
		Objects.requireNonNull(values);
		if ((len = values.length) == 0)
			throw new IllegalArgumentException("A tupla cannot be empty");
		i = -1;
		while (++i < len)
			if (values[i] == null)
				throw new IllegalArgumentException("Pairs of type-value cannot be null. Index: " + i);
		this.values = values;
		return this;
	}

	//

	// TODO OTHER METHODS

	public int length() {
		return values.length;
	}

	public PairTypeValue getPairAt(int index) {
		return values[index];
	}

	public Object getValueAt(int index) {
		return values[index].value;
	}

	public TypesDataTupla getTypesDataTuplaAt(int index) {
		return values[index].type;
	}

	//

	// TODO CLASSES

	public static final class PairTypeValue implements Serializable {
		private static final long serialVersionUID = 578683529234L;
		private TypesDataTupla type;
		private Object value;

		protected PairTypeValue(TypesDataTupla type, Object value) {
			super();
			this.setType(type);
			this.setValue(value);
		}

		public TypesDataTupla getType() {
			return type;
		}

		public Object getValue() {
			return value;
		}

		public PairTypeValue setType(TypesDataTupla type) {
			Objects.requireNonNull(type);
			this.type = type;
			return this;
		}

		public PairTypeValue setValue(Object value) {
			if (value == null && type != TypesDataTupla.Object)
				throw new IllegalArgumentException("Non-Object values cannot be null.");
			this.value = value;
			return this;
		}
	}

	@Override
	public String toString() {
		int i, len;
		PairTypeValue p;
		StringBuilder sb;
		sb = new StringBuilder(length() << 4);
		sb.append("Tuple { ");
		i = -1;
		len = values.length;
		while (++i < len) {
			p = values[i];
			if (i > 0)
				sb.append(", ");
			sb.append(i).append(": ").append(p.type.name()).append('-').append(p.value);
		}
		return sb.append(" }").toString();
	}
}