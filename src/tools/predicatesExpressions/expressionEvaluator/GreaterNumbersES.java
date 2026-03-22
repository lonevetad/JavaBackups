package tools.predicatesExpressions.expressionEvaluator;

import java.math.BigDecimal;
import java.math.BigInteger;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumberHolder;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumbersEvaluator;

public class GreaterNumbersES<E extends Number> extends TwoNumbersEvaluator<E> {
	private static final long serialVersionUID = 13983221214664102L;

	public GreaterNumbersES(E v1, E v2) {
		super(v1, v2);
	}

	protected GreaterNumbersES(TwoNumberHolder<E> del) {
		super(del);
	}

	@Override
	public boolean evaluateTwoNumbers(E v1, E v2) {
		if (v1 == v2)
			return true;
		if (v1 == null || v2 == null)
			return false;
		if (v1.getClass() == Integer.class)
			return ((Integer) v1) > ((Integer) v2);
		if (v1.getClass() == Double.class)
			return ((Double) v1) > ((Double) v2);
		if (v1.getClass() == Long.class)
			return ((Long) v1) > ((Long) v2);

		if (v1.getClass() == BigInteger.class)
			return ((BigInteger) v1).compareTo((BigInteger) v2) > 0;
		if (v1.getClass() == BigDecimal.class)
			return ((BigDecimal) v1).compareTo((BigDecimal) v2) > 0;

		if (v1.getClass() == Float.class)
			return ((Float) v1) > ((Float) v2);
		if (v1.getClass() == Byte.class)
			return ((Byte) v1) > ((Byte) v2);
		if (v1.getClass() == Short.class)
			return ((Short) v1) > ((Short) v2);

		throw new IllegalArgumentException("unacceptable arguments:\n" + v1 + "\n" + v2);
	}
}