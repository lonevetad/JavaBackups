package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import java.io.Serializable;

public interface ShortCircuitExpressionSegment extends Serializable {
	public boolean isShortCircuit();

	public ShortCircuitExpressionSegment setShortCircuit(boolean isShortCircuit);
}