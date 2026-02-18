package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;

public abstract class TwoNumbersEvaluator<E extends Number> implements TwoNumberHolder<E>, AbstractEvaluator {
	private static final long serialVersionUID = 651028563380L;

	public TwoNumbersEvaluator(E v1, E v2) {
		this(TwoNumberHolder.newInstance(v1, v2));
	}

	public TwoNumbersEvaluator(TwoNumberHolder<E> del) {
		this.numberHolderDelegate = del;
	}

	protected final TwoNumberHolder<E> numberHolderDelegate;

	//

	@Override
	public E getFirstNumber() {
		return numberHolderDelegate.getFirstNumber();
	}

	@Override
	public E getSecondNumber() {
		return numberHolderDelegate.getSecondNumber();
	}

	@Override
	public TwoNumberHolder<E> setFirstNumber(E firstNumber) {
		return numberHolderDelegate.setFirstNumber(firstNumber);
	}

	@Override
	public TwoNumberHolder<E> setSecondNumber(E secondNumber) {
		return numberHolderDelegate.setSecondNumber(secondNumber);
	}

	@Override
	public TwoNumberHolder<E> setNumbers(E firstNumber, E secondNumber) {
		return numberHolderDelegate.setNumbers(firstNumber, secondNumber);
	}

	//

	public abstract boolean evaluateTwoNumbers(E v1, E v2);

	@Override
	public boolean evaluate() {
		return evaluateTwoNumbers(this.getFirstNumber(), this.getSecondNumber());
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " elvals: (" + this.getFirstNumber() + ',' + this.getSecondNumber() + ')';
	}

	protected TwoNumberHolder<E> getNumberHolderDelegate() {
		return numberHolderDelegate;
	}
}
