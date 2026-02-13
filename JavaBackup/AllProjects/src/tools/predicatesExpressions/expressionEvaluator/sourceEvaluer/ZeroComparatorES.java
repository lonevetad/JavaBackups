package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import java.math.BigDecimal;
import java.math.BigInteger;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;

@SuppressWarnings("unchecked")
public abstract class ZeroComparatorES<E extends Number> extends TwoNumberHolderImpl<E> implements AbstractEvaluator {
	private static final long serialVersionUID = -1414515142523L;

	public ZeroComparatorES(Integer v1) {
		this(v1, Integer.valueOf(0));
	}

	public ZeroComparatorES(Double v1) {
		this(v1, Double.valueOf(0.0));
	}

	public ZeroComparatorES(Long v1) {
		this(v1, Long.valueOf(0));
	}

	public ZeroComparatorES(Float v1) {
		this(v1, Float.valueOf(0));
	}

	public ZeroComparatorES(Byte v1) {
		this(v1, Byte.valueOf((byte) 0));
	}

	public ZeroComparatorES(Short v1) {
		this(v1, Short.valueOf((short) 0));
	}

	public ZeroComparatorES(BigInteger v1) {
		this(v1, BigInteger.ZERO);
	}

	public ZeroComparatorES(BigDecimal v1) {
		this(v1, BigDecimal.ZERO);
	}

	protected ZeroComparatorES(E v1, E v2) {
		super(v1, v2);
		this.une = getNewZeroComparator(v1, v2);
	}

	protected final TwoNumbersEvaluator<E> une;

	//

	protected abstract TwoNumbersEvaluator<E> getNewZeroComparator(E v1, E v2);

	//

	// TODO GETTER

	@Override
	public E getFirstNumber() {
		return une.getFirstNumber();
	}

	@Override
	public E getSecondNumber() {
		return une.getSecondNumber();
	}

	@Override
	public TwoNumberHolderImpl<E> setFirstNumber(E firstNumber) {
		une.setFirstNumber(firstNumber);
		return this;
	}

	@Override
	public TwoNumberHolderImpl<E> setSecondNumber(E secondNumber) {
		une.setSecondNumber(secondNumber);
		return this;
	}

	public boolean evaluateTwoNumbers(E v1, E v2) {
		une.setNumbers(v1, v2);
		return une.evaluate();
	}

	@Override
	public boolean evaluate() {
		return une.evaluate();
	}
}