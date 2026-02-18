package tools.predicatesExpressions.expressionEvaluator.tests;

import tools.predicatesExpressions.expressionEvaluator.AndES;
import tools.predicatesExpressions.expressionEvaluator.GreaterEqualNumbersES;
import tools.predicatesExpressions.expressionEvaluator.OrES;
import tools.predicatesExpressions.expressionEvaluator.UnequalNumbersES;
import tools.predicatesExpressions.expressionEvaluator.XorES;
import tools.predicatesExpressions.expressionEvaluator.examples.YearHolder;
import tools.predicatesExpressions.expressionEvaluator.lessUsed.BooleanHolderEvaluator;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.AbstractTwoPartExpressionSegment;
import tools.predicatesExpressions.expressionEvaluator.sourceEvaluer.ShortCircuitExpressionSegment;

public class TestEvaluator {

	public TestEvaluator() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		int[] years;
		GreaterEqualNumbersES<Integer> g;
		YearHolder y;

		g = new GreaterEqualNumbersES<Integer>(99, 100);
		System.out.println(g + " - evals: " + g.evaluate());

		g.setFirstNumber(666);
		System.out.println(g + " - evals: " + g.evaluate());

		System.out.println("4 != 0 ?" + (new UnequalNumbersES<Integer>(4, 0)).evaluate());

		testAnd();
		testOr();
		testXor();

		System.out.println("testYear");

		years = new int[] { 2004, 2000, 1999, 2015, 2010, 2100, 2016, 2400, 2016, 666, 400, 2200, 900, 1200, 1300, 1100,
				44444, 40000, 870 };
		y = new YearHolder();
		for (int n : years) {
			y.setVal(n);
			ty(y);
		}

	}

	public static void testAnd() {
		fullTest_TwoPartExpressionSegment(AndES.emptyAndES());
	}

	public static void testOr() {
		fullTest_TwoPartExpressionSegment(OrES.emptyOrEs());
	}

	public static void testXor() {
		fullTest_TwoPartExpressionSegment(XorES.emptyXorEs());
	}

	public static void fullTest_TwoPartExpressionSegment(AbstractTwoPartExpressionSegment tpes) {
		boolean p1, p2, isShort;
		String classTpes;
		ShortCircuitExpressionSegment sses;
		BooleanHolderEvaluator ph1, ph2;

		if (tpes == null) {
			System.out.println("NULL");
			return;
		}
		sses = (tpes instanceof ShortCircuitExpressionSegment) ? (ShortCircuitExpressionSegment) tpes : null;
		classTpes = tpes.getClass().getSimpleName();
		System.out.println("\n start test + " + classTpes);
		p1 = p2 = isShort = false;
		ph1 = new BooleanHolderEvaluator();
		ph2 = new BooleanHolderEvaluator();
		tpes.setFirstPart(ph1);
		tpes.setSecondPart(ph2);
		do {
			do {
				do {
					if (sses != null) {
						sses.setShortCircuit(isShort);
					}
					ph1.setValue(p1);
					ph2.setValue(p2);

					System.out.println(classTpes + "-short:" + isShort + " -firstPoint:" + p1 + " -secondPoint:" + p2 + " -- eval: "
							+ tpes.evaluate());
				} while (p1 = !p1);
			} while (p2 = !p2);
		} while (isShort = !isShort);
	}

	public static void ty(YearHolder y) {
		System.out.println("year " + y.getVal() + " is leap? " + y.isLeapYear());
	}

}