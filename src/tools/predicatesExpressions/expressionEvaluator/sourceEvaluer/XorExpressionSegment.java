package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;

public interface XorExpressionSegment extends AbstractTwoPartExpressionSegment {
	@Override
	public default boolean evaluate() {
		AbstractEvaluator left, right;
		left = this.getFirstPart();
		if (left == null)
			throw new NullPointerException("Left part of expression is null");
		right = this.getSecondPart();
		if (right == null)
			throw new NullPointerException("Right part of expression is null even if required");
		return left.evaluate() ^ right.evaluate();
	}
}