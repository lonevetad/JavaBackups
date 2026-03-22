package tests.tDataStruct;

import dataStructures.MapTreeAVL;
import tools.Comparators;

public class TestRange {
	static final int[][] ToTest = { //
			{ 13, 50 }, //
			{ 1, 11 }, //
			{ 21, 121 }, //
			{ 199, 201 }, //
			{ 60, 189 }, //
			{ 99, 100 }, //
			{ 0, 333 }, //
			{ -7, -3 }, //
//			{ 1000, 35 }, //
			{ 1000, 3005 }, //
			{ -3, 10 }, //
			{ 200, 205 }, //
	};

	public static void main(String[] args) {
		MapTreeAVL<Integer, Integer> t;
		Integer x;
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		for (int a : TestClosestMatch_OnMapTreeAVL.TO_ADD) {
			x = a;
			t.put(x, x);
		}
		System.out.println("the tree:");
		System.out.println(t);

		System.out.println("\n\n\n so? \n\n");

		for (int[] aaaa : ToTest) {
			System.out.println("\n\n\n range: <" + aaaa[0] + "; " + aaaa[1] + ">");
			System.out.println(t.rangeQuery(aaaa[0], aaaa[1]));
		}
	}
}