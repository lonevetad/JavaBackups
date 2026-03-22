package tools.predicatesExpressions;

import tools.ValueHolder;
import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;

public class EvaluationPredicateWrapper<E> implements AbstractEvaluator {
	private static final long serialVersionUID = 1L;

	public EvaluationPredicateWrapper(MyPredicate<E> predicate) {
		this(ValueHolder.newInstance(), predicate);
	}

	public EvaluationPredicateWrapper(ValueHolder<E> holder, MyPredicate<E> predicate) {
		setHolder(holder);
		setPredicate(predicate);
	}

	protected ValueHolder<E> holder;
	protected MyPredicate<E> predicate;

	//

	public ValueHolder<E> getHolder() {
		return holder;
	}

	public MyPredicate<E> getPredicate() {
		return predicate;
	}

	public EvaluationPredicateWrapper<E> setHolder(ValueHolder<E> holder) {
		if (holder == null)
			throw new IllegalArgumentException("Holder cannot be null");
		this.holder = holder;
		return this;
	}

	public EvaluationPredicateWrapper<E> setPredicate(MyPredicate<E> predicate) {
		if (predicate == null)
			throw new IllegalArgumentException("Predicate cannot be null");
		this.predicate = predicate;
		return this;
	}

	//

	@Override
	public boolean evaluate() {
		return getPredicate().test(getHolder().getVal());
	}
}