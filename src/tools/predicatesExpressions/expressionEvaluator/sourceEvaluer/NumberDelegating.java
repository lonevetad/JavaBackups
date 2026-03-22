package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import tools.ValueHolder;
import tools.predicatesExpressions.expressionEvaluator.lessUsed.NumberDelegatingImpl;

public abstract class NumberDelegating<E extends Number> extends Number implements ValueHolder<E> {

	private static final long serialVersionUID = 5619006L;

	public abstract E getDelegator();

	public abstract NumberDelegating<E> setDelegator(E delegator);

//

	@Override
	public E getVal() {
		return getDelegator();
	}

	@Override
	public ValueHolder<E> setVal(E val) {
		return setDelegator(val);
	}

	//

	@Override
	public int intValue() {
		return getDelegator().intValue();
	}

	@Override
	public long longValue() {
		return getDelegator().longValue();
	}

	@Override
	public float floatValue() {
		return getDelegator().floatValue();
	}

	@Override
	public double doubleValue() {
		return getDelegator().doubleValue();
	}

	@Override
	public byte byteValue() {
		return getDelegator().byteValue();
	}

	@Override
	public short shortValue() {
		return getDelegator().shortValue();
	}

	//

	public static <T extends Number> NumberDelegating<T> newInstance(Class<T> typeClass) {
		return new NumberDelegatingImpl<T>();
	}

	public static <T extends Number> NumberDelegating<T> newInstance(T val) {
		return new NumberDelegatingImpl<T>(val);
	}
}