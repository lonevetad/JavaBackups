package tools.predicatesExpressions.expressionEvaluator.lessUsed;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;

public class BooleanHolderEvaluator implements AbstractEvaluator {
	private static final long serialVersionUID = -65105180018804L;
	private boolean value;

	public BooleanHolderEvaluator() {
		this(false);
	}

	public BooleanHolderEvaluator(boolean f) {
		value = f;
	}

	public boolean isValue() {
		return value;
	}

	public BooleanHolderEvaluator setValue(boolean value) {
		this.value = value;
		return this;
	}

	@Override
	public boolean evaluate() {
		return value;
	}
}