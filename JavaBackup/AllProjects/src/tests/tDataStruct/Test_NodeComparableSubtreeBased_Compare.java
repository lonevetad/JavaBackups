package tests.tDataStruct;

import java.util.Comparator;

import dataStructures.NodeComparable;
import tools.Comparators;
import tools.DifferenceCalculator;

public class Test_NodeComparableSubtreeBased_Compare {
	static final Comparator<NodeComparable<Integer>> NC_I = NodeComparable
			.newNodeComparatorDefault(Comparators.INTEGER_COMPARATOR);
	static final Comparator<TreeCompInteger> COMP_TREE_INT = (t1, t2) -> {
		return NC_I.compare(t1.getRoot(), t2.getRoot());
	};
	static final DifferenceCalculator<TreeCompInteger> DIFF_TREE = DifferenceCalculator.from(COMP_TREE_INT);
//		 NodeComparable.newDifferenceCalculator(COMP_TREE_INT);

	public static final String[][] TREE_PAIRS_TO_TEST = { //
//			{ "2", "2" }, //
//			{ "2 {4}", "2 {5}" }, //
//			{ "2", "2 {5}" }, //
			//
			/*
			 * difference results in "4" because "5{4}" and "5{3}" are treated as
			 * "totally different" due to "getChildNCMostSimilarTo" exact match so .. their
			 * tree size is 2. and 2x2=4
			 */
			{ "2{ 5 {4} }", "2{ 5 {3}}" }, //
			{ "0 { 77 1 { 2 } }", "0 { 77 1 { 2 {3 {4} }} }" }, //
			{ "7 { 4 8 { 2 } }", "7 { 8 4 { 2 } }" }, //
			{ "2{1 4}", "2{1 5}" }, //
			{ "2{ 5 {4} 5 {6} }", "2{5 {4} 5 {3}}" }, //
			{ "2{ 5 {4} 5 {6} }", "2{5 {4} 5 {7}}" }, //
			{ "2{ 1 2 3}", "2{1 2 0}" }, //
			{ "2{ 1 2 3 4}", "2{1 2 0 4}" }, //
			{ "5{ 3 1 {2 -2 0{7 8} 9 {3} 11 {-2 -7 -9{-10}}} 4 7{ 1 2 3 4{4{4}}}}",
					"2{ 3 1{2 -2 0 {6 8} 9 {3} 11 {-2 -7 -9{-10}}} 5 7{ 1 2 54 4{4{4}}}} -666}" }, //
	};

	public static void main(String[] args) {
		TreeCompInteger t1, t2;
		System.out.println("AAAAAAAAAAAAAAAH");
		for (String[] pair : TREE_PAIRS_TO_TEST) {
			System.out.println("\n\n new test:");
			t1 = TreeCompInteger.fromString(pair[0]);
			t2 = TreeCompInteger.fromString(pair[1]);
			System.out.println(t1);
			System.out.println(t2);
			System.out.println("comparing:");
			System.out.println(COMP_TREE_INT.compare(t1, t2));
			System.out.println(COMP_TREE_INT.compare(t2, t1));
			System.out.println("now difference:");
			System.out.println(t1.computeDifference(t2));
		}
		System.out.println("AAAAAAAAAAAAAAAH\n");
	}
}