package tools.predicatesExpressions.expressionEvaluator;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.ComparerEqualNumberEvaluatorSegment;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumberHolder;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumbersEvaluator;

public class GreaterEqualNumbersES<E extends Number> extends ComparerEqualNumberEvaluatorSegment<E> {
//implements TwoNumberHolder<E>, AbstractEvaluator
	private static final long serialVersionUID = 282399217154645L;

	public GreaterEqualNumbersES(E v1, E v2) {
		super(v1, v2);
	}

	@Override
	protected TwoNumbersEvaluator<E> getComparativeEvaluator(TwoNumberHolder<E> del) {
		return new GreaterNumbersES<>(del);
	}
}