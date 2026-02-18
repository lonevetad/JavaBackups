package tools.predicatesExpressions.expressionEvaluator;

import java.io.Serializable;

public interface AbstractEvaluator extends Serializable {

	/** Evaluate depending on its implementor's fields. */
	public boolean evaluate();
}