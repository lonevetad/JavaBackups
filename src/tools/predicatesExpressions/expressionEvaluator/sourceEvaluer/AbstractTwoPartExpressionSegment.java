package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;

public interface AbstractTwoPartExpressionSegment extends AbstractEvaluator {

	public AbstractEvaluator getFirstPart();

	public AbstractEvaluator getSecondPart();

	public AbstractTwoPartExpressionSegment setFirstPart(AbstractEvaluator evaluator);

	public AbstractTwoPartExpressionSegment setSecondPart(AbstractEvaluator evaluator);
}