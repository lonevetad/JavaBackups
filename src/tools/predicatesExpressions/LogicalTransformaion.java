package tools.predicatesExpressions;

import java.util.List;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;
import tools.predicatesExpressions.expressionEvaluator.AtomEvaluer;
import tools.predicatesExpressions.expressionEvaluator.MultipleAndES;
import tools.predicatesExpressions.expressionEvaluator.XnorES;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.AbstractTwoPartExpressionSegment;

public class LogicalTransformaion {

	private LogicalTransformaion() {
	}

	@Deprecated
	public static AbstractEvaluator tseitinTransformaion(AbstractTwoPartExpressionSegment originalFormula) {
		MultipleAndES ret;
		AtomEvaluer atom;
		List<XnorES> newParts;
		XnorES newPart;
		ret = null; // TODO
		return ret;
	}
}