package tools.predicatesExpressions.expressionEvaluator;

import java.util.Objects;

import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumberHolder;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.TwoNumbersEvaluator;

public class EqualNumbersES<E extends Number> extends TwoNumbersEvaluator<E> {
	private static final long serialVersionUID = 7068413776650L;

	public EqualNumbersES(E v1, E v2) {
		super(v1, v2);
	}

	public EqualNumbersES(TwoNumberHolder<E> del) {
		super(del);
	}

//	public EqualNumbersES(AbstractNumberHolder<E> v1, AbstractNumberHolder<E> v2) {
//		super(v1, v2); }

	@Override
	public boolean evaluateTwoNumbers(E v1, E v2) {
		if (Objects.equals(v1, v2))
			return true;
		if (v1 == null || v2 == null)
			return false;
		return v1.longValue() == v2.longValue() || v1.doubleValue() == v2.doubleValue();
	}
}