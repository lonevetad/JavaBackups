package tools.predicatesExpressions.expressionEvaluator.lessUsed;

import tools.predicatesExpressions.expressionEvaluator.UnequalNumbersES;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumbersEvaluator;

/**
 * Versione piu' semplice e performante di {@link UnequalNumbersES} in quanto
 * l'operazione che deve svolgere viene creata appositamente, anziche' venire
 * generata dalla composizione di classi ed inerfacce gia' esistenti sfruttando
 * l'ideale del riciclo.
 */
public class UnequalNumbersEvaluator_SimpleButUnrecycling<E extends Number> extends TwoNumbersEvaluator<E> {
	private static final long serialVersionUID = 981084442L;

	public UnequalNumbersEvaluator_SimpleButUnrecycling(E v1, E v2) {
		super(v1, v2);
	}

	@Override
	public boolean evaluateTwoNumbers(E v1, E v2) {
		return v1 != v2;
	}

	public static <T extends Number> UnequalNumbersEvaluator_SimpleButUnrecycling<T> newInstance(T v1, T v2) {
		return new UnequalNumbersEvaluator_SimpleButUnrecycling<T>(v1, v2);
	}
}