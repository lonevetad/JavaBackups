package tools.predicatesExpressions.expressionEvaluator;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoPartExpressionSegment;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.XorExpressionSegment;

public class XorES extends TwoPartExpressionSegment implements XorExpressionSegment {
	private static final long serialVersionUID = 984016406433897858L;

	protected XorES() {
		this(null, null);
	}

	public XorES(AbstractEvaluator left, AbstractEvaluator right) {
		super(left, right);
	}

	public static XorES emptyXorEs() {
		return new XorES();
	}
}