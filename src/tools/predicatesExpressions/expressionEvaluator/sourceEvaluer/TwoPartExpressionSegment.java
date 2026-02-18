package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;

public abstract class TwoPartExpressionSegment implements AbstractTwoPartExpressionSegment {
	private static final long serialVersionUID = 298521095041809L;

	protected TwoPartExpressionSegment() {
		this(null, null);
	}

	public TwoPartExpressionSegment(AbstractEvaluator left, AbstractEvaluator right) {
		super();
		this.setFirstPart(left);
		this.setSecondPart(right);
	}

	protected AbstractEvaluator firstPart, secondPart;

	//

	@Override
	public AbstractEvaluator getFirstPart() {
		return firstPart;
	}

	@Override
	public AbstractEvaluator getSecondPart() {
		return secondPart;
	}

	@Override
	public TwoPartExpressionSegment setFirstPart(AbstractEvaluator firstPart) {
		this.firstPart = firstPart;
		return this;
	}

	@Override
	public TwoPartExpressionSegment setSecondPart(AbstractEvaluator secondPart) {
		this.secondPart = secondPart;
		return this;
	}
}