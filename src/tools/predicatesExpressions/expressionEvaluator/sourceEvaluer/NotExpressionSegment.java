package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;

public interface NotExpressionSegment extends AbstractEvaluator {

	public AbstractEvaluator getExpressionSegment();

	public NotExpressionSegment setExpressionSegment(AbstractEvaluator expressionSegment);

	@Override
	public default boolean evaluate() {
		return this.evaluate(this.getExpressionSegment());
	}

	public default boolean evaluate(AbstractEvaluator expr) {
		if (expr == null)
			throw new NullPointerException("Expression to be negated is null");
		return !expr.evaluate();
	}
}