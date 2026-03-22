package tools.predicatesExpressions.expressionEvaluator.sourceEvaluer;

import java.io.Serializable;

public interface TwoNumberHolder<E extends Number> extends Serializable {

	public E getFirstNumber();

	public E getSecondNumber();

	//

	public TwoNumberHolder<E> setFirstNumber(E firstNumber);

	public TwoNumberHolder<E> setSecondNumber(E secondNumber);

	public TwoNumberHolder<E> setNumbers(E firstNumber, E secondNumber);

//

	public static <T extends Number> TwoNumberHolder<T> newInstance(T v1, T v2) {
		return new TwoNumberHolderImpl<T>(v1, v2);
	}
}