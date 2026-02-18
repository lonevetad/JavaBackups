package tests.tDataStruct;

import java.util.Map.Entry;

import dataStructures.MapTreeAVL;
import tools.ClosestMatch;
import tools.Comparators;

public class TestClosestMatch_OnMapTreeAVL {
	public static final int[] TO_ADD = { 100, 50, 150, 20, 70, 130, 200, 10, 15, 188 };

//	size: 10, collection-type: Replace
//	Min: (k: 10, v: 10), Max: (k:200, v: 200)
//				10-10,h:0,f:15
//			15-15,h:1,f:50
//				20-20,h:0,f:15
//		50-50,h:2,f:100
//			70-70,h:0,f:50
//	100-100,h:3,f:null
//			130-130,h:0,f:150
//		150-150,h:2,f:100
//				188-188,h:0,f:200
//			200-200,h:1,f:150

	static enum ToSearch {
		Minimum(10), NoLowerBound(5), LowIsFatherUpIsSucc(13), LowIsPredUpIsFather(66), Maximum(200), NoUpperBound(222),
		Root(100), InBetweenFromRight(128), SomeWeird(194);

		public final Integer val;

		ToSearch(int x) { this.val = x; }
	}

	public static void main(String[] args) {
		MapTreeAVL<Integer, Integer> t;
		ClosestMatch<Entry<Integer, Integer>> cm;
		Integer x;
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		for (int a : TO_ADD) {
			x = a;
			t.put(x, x);
		}
		System.out.println("the tree:");
		System.out.println(t);

		System.out.println("\n\n\n so ..");
		for (ToSearch ts : ToSearch.values()) {
			System.out.println("\n searching: " + ts.name() + " -> " + ts.val);
			cm = t.closestMatchOf(ts.val);
			System.out.print("found: ");
			if (cm == null)
				System.out.println("null");
			else {
				System.out.println(
						toStr(cm.nearestLowerOrExact) + " <-> " + toStr(cm.nearestUpper) + " is: " + cm.isExactMatch());
				System.out.println("lowest: " + cm.getAvailableMatchLowerFirst() + " <----> upper: "
						+ cm.getAvailableMatchUpperFirst());
			}
		}
	}

	static String toStr(Entry<Integer, Integer> e) { return e == null ? "null" : e.getKey().toString(); }
}