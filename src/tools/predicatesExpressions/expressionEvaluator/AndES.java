package tools.predicatesExpressions.expressionEvaluator;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.AndExpressionSegment;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoPartExpressionSegment;

public class AndES extends TwoPartExpressionSegment implements AndExpressionSegment {
	private static final long serialVersionUID = 914012333882277282L;

	protected AndES() {
		this(null, null);
	}

	public AndES(AbstractEvaluator left, AbstractEvaluator right) {
		this(true, left, right);
	}

	public AndES(boolean isShortCircuit, AbstractEvaluator left, AbstractEvaluator right) {
		super(left, right);
		this.isShortCircuit = isShortCircuit;
	}

	//

	private boolean isShortCircuit;

	//

	// TODO GETTER

	@Override
	public boolean isShortCircuit() {
		return isShortCircuit;
	}

	@Override
	public AndES setShortCircuit(boolean isShortCircuit) {
		this.isShortCircuit = isShortCircuit;
		return this;
	}

	public static AndES emptyAndES() {
		return new AndES();
	}

	//

	@Override
	public boolean evaluate() {
		boolean leftResult;
		AbstractEvaluator left, right;

		left = this.getFirstPart();
		if (left == null)
			throw new NullPointerException("Left part of expression is null");
		leftResult = left.evaluate();
		if (leftResult || (!this.isShortCircuit())) {
			right = this.getSecondPart();
			if (right == null)
				throw new NullPointerException("Right part of expression is null even if required");
			leftResult &= right.evaluate();
		}
		return leftResult;
	}
}