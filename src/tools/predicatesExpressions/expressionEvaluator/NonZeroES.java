package tools.predicatesExpressions.expressionEvaluator;

import java.math.BigDecimal;
import java.math.BigInteger;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumbersEvaluator;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.ZeroComparatorES;

public class NonZeroES<E extends Number> extends ZeroComparatorES<E> {
	private static final long serialVersionUID = 504680780807L;

	public NonZeroES(BigDecimal v1) {
		super(v1);
	}

	public NonZeroES(BigInteger v1) {
		super(v1);
	}

	public NonZeroES(Byte v1) {
		super(v1);
	}

	public NonZeroES(Double v1) {
		super(v1);
	}

	public NonZeroES(E v1, E v2) {
		super(v1, v2);
	}

	public NonZeroES(Float v1) {
		super(v1);
	}

	public NonZeroES(Integer v1) {
		super(v1);
	}

	public NonZeroES(Long v1) {
		super(v1);
	}

	public NonZeroES(Short v1) {
		super(v1);
	}

	//

	@Override
	protected TwoNumbersEvaluator<E> getNewZeroComparator(E v1, E v2) {
		return new UnequalNumbersES<E>(v1, v2);
	}
}