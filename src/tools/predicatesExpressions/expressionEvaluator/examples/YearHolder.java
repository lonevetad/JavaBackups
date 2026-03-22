package tools.predicatesExpressions.expressionEvaluator.examples;

import tools.ValueHolder;
import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;
import tools.predicatesExpressions.expressionEvaluator.AndES;
import tools.predicatesExpressions.expressionEvaluator.EqualNumbersES;
import tools.predicatesExpressions.expressionEvaluator.OrES;
import tools.predicatesExpressions.expressionEvaluator.UnequalNumbersES;
import tools.predicatesExpressions.expressionEvaluator.lessUsed.ModuleGetter;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.AbstractTwoPartExpressionSegment;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.NumberDelegating;

public class YearHolder implements ValueHolder<Integer>, AbstractEvaluator {
	private static final long serialVersionUID = -52601531523205L;

	public YearHolder() {
		this(2017);
	}

	public YearHolder(Integer n) {
		NumberDelegating<Integer> zero;
		AbstractTwoPartExpressionSegment or, and;
		if (n == null)
			throw new IllegalArgumentException("Value is null");
		mod4 = new ModuleGetter<Integer>(n, 4);
		mod400 = new ModuleGetter<Integer>(n, 400);
		mod100 = new ModuleGetter<Integer>(n, 100);

		leapEval = or = OrES.emptyOrEs().setShortCircuit(true);
		zero = NumberDelegating.newInstance(Integer.valueOf(0));
		or.setFirstPart(new EqualNumbersES<NumberDelegating<Integer>>(mod400, zero));

		and = AndES.emptyAndES().setShortCircuit(true);
		or.setSecondPart(and);
		and.setFirstPart(new EqualNumbersES<NumberDelegating<Integer>>(mod4, zero));
		and.setSecondPart(new UnequalNumbersES<NumberDelegating<Integer>>(mod100, zero));

//		setVal(n);
		this.val = n;
		this.isLeap = leapEval.evaluate();
	}

	private boolean isLeap; // works as a cache
	private Integer val;
	private AbstractEvaluator leapEval;
	private final ModuleGetter<Integer> mod4, mod400, mod100;

	@Override
	public Integer getVal() {
		return val;
	}

	@Override
	public ValueHolder<Integer> setVal(Integer val) {
		if (this.val != val && //
				this.val.compareTo(val) != 0) {
			this.val = val;
			mod4.setNumber(val);
			mod400.setNumber(val);
			mod100.setNumber(val);
			this.isLeap = leapEval.evaluate();
		}
		return this;
	}

	/** Italian: "bisestile" */
	public boolean isLeapYear() {
		return isLeap;
	}

	@Override
	public boolean evaluate() {
		return isLeap;
	}
}