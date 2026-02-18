package tools.predicatesExpressions.expressionEvaluator;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoPartExpressionSegment;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.XnorExpressionSegment;

public class XnorES extends TwoPartExpressionSegment implements XnorExpressionSegment {
	private static final long serialVersionUID = 984016406433897858L;

	protected XnorES() {
		super();
	}

	public XnorES(AbstractEvaluator left, AbstractEvaluator right) {
		super(left, right);
	}

	public static XnorES emptyXorEs() {
		return new XnorES();
	}
}