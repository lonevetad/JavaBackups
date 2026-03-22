package tools.predicatesExpressions.expressionEvaluator;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.OrExpressionSegment;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoPartExpressionSegment;

public class OrES extends TwoPartExpressionSegment implements OrExpressionSegment {
	private static final long serialVersionUID = 81603777L;

	protected OrES() {
		this(null, null);
	}

	public OrES(AbstractEvaluator left, AbstractEvaluator right) {
		this(true, left, right);
	}

	public OrES(boolean isShortCircuit, AbstractEvaluator left, AbstractEvaluator right) {
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

	public static OrES emptyOrEs() {
		return new OrES();
	}

	@Override
	public OrES setShortCircuit(boolean isShortCircuit) {
		this.isShortCircuit = isShortCircuit;
		return this;
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
		// if ((!leftResult) || (!this.isShortCircuit())) {
		if (!(leftResult && this.isShortCircuit())) {
			right = this.getSecondPart();
			if (right == null)
				throw new NullPointerException("Right part of expression is null even if required");
			leftResult |= right.evaluate();
		}
		return leftResult;
	}
}