package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import tools.predicatesExpressions.expressionEvaluator.EqualNumbersES;
import tools.predicatesExpressions.expressionEvaluator.OrES;

public abstract class ComparerEqualNumberEvaluatorSegment<E extends Number> extends TwoNumbersEvaluator<E> {
	// implements TwoNumberHolder<E>, AbstractEvaluator
	private static final long serialVersionUID = 282399217154645L;

	public ComparerEqualNumberEvaluatorSegment(E v1, E v2) {
		super(v1, v2);
		TwoNumberHolder<E> del;
		del = this.getNumberHolderDelegate();
		this.comparativeEvaluator = getComparativeEvaluator(del);
		// , new EqualNumbersES(v1, v2));
		this.equalEvaluator = new EqualNumbersES<E>(del);
		this.orToBeEvaluated = new OrES(true, equalEvaluator, comparativeEvaluator);
	}

	protected final TwoNumbersEvaluator<E> comparativeEvaluator;
	protected final EqualNumbersES<E> equalEvaluator;
	protected final OrExpressionSegment orToBeEvaluated;

	//

	protected abstract TwoNumbersEvaluator<E> getComparativeEvaluator(TwoNumberHolder<E> del);

	@Override
	public boolean evaluateTwoNumbers(E v1, E v2) {
		this.getNumberHolderDelegate().setNumbers(v1, v2);
		return orToBeEvaluated.evaluate();
	}
}
