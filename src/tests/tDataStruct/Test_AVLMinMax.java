package tests.tDataStruct;

import dataStructures.MapTreeAVL;
import tools.Comparators;

public class Test_AVLMinMax {

	public static void main(String[] args) {
		MapTreeAVL<Integer, Integer> t;
		Integer x;
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		for (int i = 0; i < 10; i++) {
			x = i * 10;
			t.put(x, x);
		}
		pr(t);
		x = 5;
		System.out.println("adding " + x);
		t.put(x, x);
		pr(t);
		x = 85;
		System.out.println("adding " + x);
		t.put(x, x);
		pr(t);
	}

	static void pr(MapTreeAVL<Integer, Integer> t) {
		System.out.println("\n--------");
		System.out.println(t);
		t.forEach((k, v) -> System.out.println("k: " + k));
	}
}