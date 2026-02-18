package tools.predicatesExpressions.expressionEvaluator;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.MultipleAndOrES;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.OrExpressionSegment;

public class MultipleOrES extends MultipleAndOrES implements OrExpressionSegment {
	private static final long serialVersionUID = 81603777L;

	public MultipleOrES() {
		super();
	}

	public MultipleOrES(boolean isShortCircuit) {
		super(isShortCircuit);
	}

	//

	@Override
	public boolean evaluate() {
		boolean res, isShortCirc;
		if (segments == null || segments.isEmpty())
			return false;
		res = false;
		isShortCirc = this.isShortCircuit;
		for (AbstractEvaluator e : segments) {
			res |= e.evaluate();
			if (isShortCirc && res)
				return true;
		}
		return res;
	}
}