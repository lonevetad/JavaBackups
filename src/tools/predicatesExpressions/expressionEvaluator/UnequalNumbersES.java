package tools.predicatesExpressions.expressionEvaluator;

import tools.predicatesExpressions.expressionEvaluator.lessUsed.UnequalNumbersEvaluator_SimpleButUnrecycling;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.NotExpressionSegment;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumberHolder;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumbersEvaluator;

/**
 * Implementazione resa complicata in quanto e' la composizione di classi ed
 * interfacce gia' costruite.<br>
 * La sua versione piu' semplice e performante e'
 * {@link UnequalNumbersEvaluator_SimpleButUnrecycling}, ma l'ideale del riciclo
 * e il "pattern" secondo il quale si svolge una funzione semplicemente
 * componendo il risultato di piu' funzioni gia' esistenti e implementate (in
 * classi ed interfacce).
 */
public class UnequalNumbersES<E extends Number> extends UnequalNumbersEvaluator_SimpleButUnrecycling<E> {
	private static final long serialVersionUID = 2374468580201296L;

	public UnequalNumbersES(E v1, E v2) {
//		this(new NumberHolder<>(v1), new NumberHolder<>(v2)); }
//	public UnequalNumbersES(AbstractNumberHolder v1, AbstractNumberHolder v2) {
		super(v1, v2);
		equalNumberEvaluator = new EqualNumbersES<E>(this.getNumberHolderDelegate());
		notEvaluator = new NotES(equalNumberEvaluator);

	}

	protected final TwoNumbersEvaluator<E> equalNumberEvaluator;
	protected final NotExpressionSegment notEvaluator;

	// DELEGATORS

	//

	// TODO GETTER

	@Override
	public E getFirstNumber() {
		return equalNumberEvaluator.getFirstNumber();
	}

	@Override
	public E getSecondNumber() {
		return equalNumberEvaluator.getSecondNumber();
	}

	//

	// TODO SETTER

	@Override
	public TwoNumberHolder<E> setFirstNumber(E firstNumber) {
		return equalNumberEvaluator.setFirstNumber(firstNumber);
	}

	@Override
	public TwoNumberHolder<E> setSecondNumber(E secondNumber) {
		return equalNumberEvaluator.setSecondNumber(secondNumber);
	}

	@Override
	public TwoNumberHolder<E> setNumbers(E firstNumber, E secondNumber) {
		return equalNumberEvaluator.setNumbers(firstNumber, secondNumber);
	}

	//

	// TODO IMPLEMENTATION

	@Override
	public boolean evaluateTwoNumbers(E v1, E v2) {
		this.equalNumberEvaluator.setFirstNumber(v1);
		this.equalNumberEvaluator.setSecondNumber(v2);
		return notEvaluator.evaluate(this.equalNumberEvaluator);
	}

	@Override
	public boolean evaluate() {
		return notEvaluator.evaluate();
	}

	public static <T extends Number> UnequalNumbersES<T> newInstance(T v1, T v2) {
		return new UnequalNumbersES<T>(v1, v2);
	}
}