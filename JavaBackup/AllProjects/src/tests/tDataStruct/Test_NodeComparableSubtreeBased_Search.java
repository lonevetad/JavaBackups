package tests.tDataStruct;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import dataStructures.MapTreeAVL;
import dataStructures.NodeComparable;
import tools.CloserGetter;
import tools.ClosestMatch;
import tools.Comparators;
import tools.DifferenceCalculator;

public class Test_NodeComparableSubtreeBased_Search {

	//
	static final Comparator<NodeComparable<Integer>> NC_I = NodeComparable
			.newNodeComparatorDefault(Comparators.INTEGER_COMPARATOR);
	static final Comparator<TreeCompInteger> COMP_TREE_INT = (t1, t2) -> {
		return NC_I.compare(t1.getRoot(), t2.getRoot());
	};
	static final DifferenceCalculator<TreeCompInteger> DIFF_TREE = COMP_TREE_INT::compare;

	public static final CloserGetter<Map.Entry<TreeCompInteger, TreeCompInteger>> CLOSE_GETTER_TREE = (eo, e1,
			e2) -> CloserGetter.getCloserTo(eo, (e11, e22) -> DIFF_TREE.getDifference(e11.getKey(), e22.getKey()), e1,
					e2);

	static {
		TreeCompInteger t1, t2;
		System.out.println("AAAAAAAAAAAAAAAH");
		t1 = TreeCompInteger.fromString("0 { 77 1 { 2 } }");
		t2 = TreeCompInteger.fromString("0 { 77 1 { 2 {3 {4} }} }");
		System.out.println(t1);
		System.out.println(t2);
		System.out.println(COMP_TREE_INT.compare(t1, t2));
		System.out.println(COMP_TREE_INT.compare(t2, t1));
		System.out.println("AAAAAAAAAAAAAAAH");
	}

	//

	//

	public Test_NodeComparableSubtreeBased_Search() {
		super();
		holder = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, COMP_TREE_INT);
	}

	protected MapTreeAVL<TreeCompInteger, TreeCompInteger> holder;

	//

	//

	protected void add(TreeCompInteger t) { this.holder.put(t, t); }

	protected TreeCompInteger getBest(TreeCompInteger subtreeToTransfer) {
//		NodeParsedSentence maybeALhs;
		ClosestMatch<Entry<TreeCompInteger, TreeCompInteger>> ruleMatched = this.holder
				.closestMatchOf(subtreeToTransfer);
		if (ruleMatched == null)
			return null;
		// a "ClosestMatch" could have an exact match or just approximation
		return ruleMatched.getClosetsMatchToOriginal(CLOSE_GETTER_TREE).getValue();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		holder.forEach(ForEachMode.BreadthGrowing, e-> );
		sb.append("Test_NodeComparableSubtreeBased_Search: ");
		sb.append(holder.size());
		sb.append("\n");
		holder.forEachAndDepth((e, d) -> { e.getKey().toString(sb, d); });
		return sb.toString();
	}

	//

	//

	//

	//

	static final String[] TREES = { //
			"0 { 4 2 }", //
			"0 { 4 2 77 }", //
			"0 { -1  -8 { 2 8 0 } }", //
			"0 { 5}", //
			"0 { 1 { 2 {3 {4} }} }", //
			"0 { 1 { 2 } }", //
			"0 { 4 2 88}", //
			"0 { 45}", //
			"0 { 1 { 2 {3 {4}  ", //
			"0 { 1  8 { 2 } }", //
			"0 { 25 13 {8 9} 6 {7} 5 1 {44 {3} -2} 0 4}", //
			"0 { -1  -8 { 2 } }", //
			"0 { 1 {-1} 8 { 2 } 15 }", //
//			"0 { 5}", //
//			"0 { 45}", //
//			"0 { 4 2 }", //
//			"0 { 4 2 77 }", //
//			"0 { 1 { 2 } }", //
//			"0 { 4 2 88}", //
//			"0 { 1 { 2 {3 {4} }} }", //
//			"0 { 1 { 2 {3 {4}  ", //
//			"0 { 1  8 { 2 } }", //
//			"0 { -1  -8 { 2 } }", //
//			"0 { -1  -8 { 2 8 0 } }", //
//			"0 { 1 {-1} 8 { 2 } 15 }", //
//			"0 { 25 13 {8 9} 6 {7} 5 1 {44 {3} -2} 0 4}", //
	};
	static final String[] TESTERS = { //
			"0", //
			"0 { 2}", //
			"0 { 8 2}", //
			"0 { 1{ 2}", // i'm strong against non-ending brackets
	};

	public static void main(String[] args) {
		TreeCompInteger t;
		Test_NodeComparableSubtreeBased_Search tt;
		tt = new Test_NodeComparableSubtreeBased_Search();
		System.out.println("START");
		for (String s : TREES) {
			t = TreeCompInteger.fromString(s);
			System.out.println(
					"\n\n\n ___________________________________________________________________________ tree:\n\t :"
							+ s);
			System.out.println(t);
			tt.add(t);
			System.out.println(tt);
			if (tt.holder.get(t) == null) { throw new IllegalStateException("WTF"); }
		}

		System.out.println("\n\n THEN PRINT THEM UP :D :D :D");
//		tt.holder.forEach((k, v) -> System.out.println(k));
		System.out.println(tt);
		System.out.println("\n\n search for ######################");

		for (String toTest : TESTERS) {
			System.out.println("\n\n testing :D");
			t = TreeCompInteger.fromString(toTest);
			System.out.println(t);
			System.out.println("..... got:");
			t = tt.getBest(t);
			System.out.println(t);
		}
		/*
		 * TODO: 1) fare un raccoglitore di questi nodi (che emuli il Transferer) 2)
		 * alberi di test 3) cercare il best match
		 */
		System.out.println("\n\n\n END");
	}
}