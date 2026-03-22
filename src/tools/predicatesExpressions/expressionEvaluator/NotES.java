package tools.predicatesExpressions.expressionEvaluator;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.NotExpressionSegment;

public class NotES implements NotExpressionSegment {
	private static final long serialVersionUID = 3200198072392505L;

	public NotES(AbstractEvaluator expressionSegment) {
		setExpressionSegment(expressionSegment);
	}

	protected AbstractEvaluator expressionSegment;

	@Override
	public AbstractEvaluator getExpressionSegment() {
		return expressionSegment;
	}

	@Override
	public NotExpressionSegment setExpressionSegment(AbstractEvaluator expressionSegment) {
		this.expressionSegment = expressionSegment;
		return this;
	}
}