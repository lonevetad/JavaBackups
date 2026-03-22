package tools.predicatesExpressions.expressionEvaluator;

import java.math.BigDecimal;
import java.math.BigInteger;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumbersEvaluator;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.ZeroComparatorES;

public class IsZeroES<E extends Number> extends ZeroComparatorES<E> {
	private static final long serialVersionUID = 504680780807L;

	public IsZeroES(BigDecimal v1) {
		super(v1);
	}

	public IsZeroES(BigInteger v1) {
		super(v1);
	}

	public IsZeroES(Byte v1) {
		super(v1);
	}

	public IsZeroES(Double v1) {
		super(v1);
	}

	public IsZeroES(E v1, E v2) {
		super(v1, v2);
	}

	public IsZeroES(Float v1) {
		super(v1);
	}

	public IsZeroES(Integer v1) {
		super(v1);
	}

	public IsZeroES(Long v1) {
		super(v1);
	}

	public IsZeroES(Short v1) {
		super(v1);
	}

	//

	@Override
	protected TwoNumbersEvaluator<E> getNewZeroComparator(E v1, E v2) {
		return new EqualNumbersES<E>(v1, v2);
	}
}