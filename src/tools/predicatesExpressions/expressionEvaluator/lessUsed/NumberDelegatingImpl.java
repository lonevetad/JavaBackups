package tools.predicatesExpressions.expressionEvaluator.lessUsed;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.NumberDelegating;

public class NumberDelegatingImpl<E extends Number> extends NumberDelegating<E> {
	private static final long serialVersionUID = 336845610451510L;

	public NumberDelegatingImpl() {
		this(null);
	}

	public NumberDelegatingImpl(E delegator) {
		super();
		this.delegator = delegator;
	}

	protected E delegator;

	@Override
	public E getDelegator() {
		return delegator;
	}

	@Override
	public NumberDelegating<E> setDelegator(E delegator) {
		this.delegator = delegator;
		return this;
	}
}