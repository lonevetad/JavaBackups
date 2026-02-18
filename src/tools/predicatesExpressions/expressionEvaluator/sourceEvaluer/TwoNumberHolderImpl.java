package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

public class TwoNumberHolderImpl<E extends Number> implements TwoNumberHolder<E> {
	private static final long serialVersionUID = 506254075224500L;

	protected TwoNumberHolderImpl() {
		this(null, null);
	}

	public TwoNumberHolderImpl(E firstNumber, E secondNumber) {
//		this.firstNumber = new NumberHolder<E>();
//		this.secondNumber = new NumberHolder<E>();
		setFirstNumber(firstNumber);
		setSecondNumber(secondNumber);
		// setNumbers(firstNumber, secondNumber);
	}

	protected E firstNumber, secondNumber;

	//

	@Override
	public E getFirstNumber() {
		return this.firstNumber;
	}

	@Override
	public E getSecondNumber() {
		return this.secondNumber;
	}

	//

	@Override
	public TwoNumberHolderImpl<E> setFirstNumber(E firstNumber) {
		this.firstNumber = firstNumber;
		return this;
	}

	@Override
	public TwoNumberHolderImpl<E> setSecondNumber(E secondNumber) {
		this.secondNumber = secondNumber;
		return this;
	}

	@Override
	public TwoNumberHolderImpl<E> setNumbers(E firstNumber, E secondNumber) {
		this.firstNumber = firstNumber;
		this.secondNumber = secondNumber;
		return this;
	}

	//

	/*
	 * public TwoNumberHolder setNumbers(Number firstNumber, Number secondNumber) {
	 * Class<? extends Number> cn; Number oldModule; if (firstNumber == null) throw
	 * new IllegalArgumentException("Number must be not null"); if (secondNumber ==
	 * null) throw new IllegalArgumentException("Modulemust be not null");
	 *
	 * if ((cn = firstNumber.getClass()) != secondNumber.getClass()) throw new
	 * IllegalArgumentException("Number and secondNumber must have the same class");
	 * if (this.firstNumber != firstNumber) { this.firstNumber = firstNumber; }
	 * oldModule = this.secondNumber; if (this.secondNumber != secondNumber) {
	 * this.secondNumber = secondNumber; } if (notChanged) return this; if
	 * (cn.getName() == Integer.class.getName()) { if (((Integer) secondNumber) ==
	 * 0) { this.secondNumber = oldModule; throw new
	 * IllegalArgumentException("The secondNumber cannot be 0"); } this.cacheModule
	 * = ((Integer) firstNumber) % ((Integer) secondNumber); } else if (cn.getName()
	 * == Double.class.getName()) { if (((Double) secondNumber) == 0) {
	 * this.secondNumber = oldModule; throw new
	 * IllegalArgumentException("The secondNumber cannot be 0"); } this.cacheModule
	 * = ((Double) firstNumber) % ((Double) secondNumber); } else if (cn.getName()
	 * == Long.class.getName()) { if (((Long) secondNumber) == 0) {
	 * this.secondNumber = oldModule; throw new
	 * IllegalArgumentException("The secondNumber cannot be 0"); } this.cacheModule
	 * = ((Long) firstNumber) % ((Long) secondNumber); } else if (cn.getName() ==
	 * Byte.class.getName()) { if (((Byte) secondNumber) == 0) { this.secondNumber =
	 * oldModule; throw new
	 * IllegalArgumentException("The secondNumber cannot be 0"); } this.cacheModule
	 * = ((Byte) firstNumber) % ((Byte) secondNumber); } else if (cn.getName() ==
	 * Float.class.getName()) { if (((Float) secondNumber) == 0) { this.secondNumber
	 * = oldModule; throw new
	 * IllegalArgumentException("The secondNumber cannot be 0"); } this.cacheModule
	 * = ((Float) firstNumber) % ((Float) secondNumber); } else if (cn.getName() ==
	 * Short.class.getName()) { if (((Short) secondNumber) == 0) { this.secondNumber
	 * = oldModule; throw new
	 * IllegalArgumentException("The secondNumber cannot be 0"); } this.cacheModule
	 * = ((Short) firstNumber) % ((Short) secondNumber); } else throw new
	 * IllegalArgumentException("Unexpected value: " + cn); return this; }
	 */
}