package tools.predicatesExpressions.expressionEvaluator;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.AndExpressionSegment;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.MultipleAndOrES;

public class MultipleAndES extends MultipleAndOrES implements AndExpressionSegment {
	private static final long serialVersionUID = 81603777L;

	public MultipleAndES() {
		super();
	}

	public MultipleAndES(boolean isShortCircuit) {
		super(isShortCircuit);
	}

	//

	@Override
	public boolean evaluate() {
		boolean res, isShortCirc;
		if (segments == null || segments.isEmpty())
			return false;
		res = true;
		isShortCirc = this.isShortCircuit;
		for (AbstractEvaluator e : segments) {
			res &= e.evaluate();
			if (isShortCirc && (!res))
				return true;
		}
		return res;
	}
}