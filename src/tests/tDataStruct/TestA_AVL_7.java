package tests.tDataStruct;

import dataStructures.MapTreeAVL;
import tools.Comparators;

public class TestA_AVL_7 {

	public static void main(String[] args) {
		int max;
		int[] vals;
		MapTreeAVL<Integer, Integer> t;
		Integer x;
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		vals = new int[] { 70, 10, 100, 5, 21, 3, 155 };
		System.out.println("start");
		for (int v : vals) {
			x = v;
			t.put(x, x);
			System.out.println("\n\n");
			System.out.println("adding " + v + " to the tree:");
			System.out.println(t);
		}
		max = 1 << 4;
		System.out.println("finished\n\nnow add all numbers from 0 to " + max);

		buildAndPrint(t, new RangeIntSequence(0, max));

		vals = new int[] { 10, 4, 20, 2, 6, 15, 22, 1, 3, 5, 11, 0 };
		System.out.println("weird sequence");
		buildAndPrint(t, new ArrayIntSequence(vals));
		System.out.println("now balance t:");
		t.compact();
		System.out.println(t);

		vals = new int[] { 99, 10, 140, 4, 20, 120, 160, 2, 6, 15, 22, 110, 103, 150, 1, 3, 5, 11, 100, 0 };
		System.out.println("\n\n bigger weird sequence");
		buildAndPrint(t, new ArrayIntSequence(vals));
		System.out.println("now balance t:");
		t.compact();
		System.out.println(t);

		System.out.println("\n\n\n\n_______________\nTEST BANALCE OVER RANGE");
		buildAndPrint(t, new RangeIntSequence(0, max));
		t.compact();
		System.out.println(t);
		max = 1 << 10;
		System.out.println("\n huge range: " + max);
		buildAndPrint(t, new RangeIntSequence(0, max));
		t.compact();
		System.out.println("\n\nCOMPATC the huge tree");
		System.out.println(t);

		System.out.println("\n\n\n\n\n LITTLE TREES \n\n\n");
		vals = new int[] { 7, vals[0], vals[2], vals[3], vals[6], vals[10], 50 };

		boolean compactOnlyAtEnd;
		compactOnlyAtEnd = false;
		do {
			t.clear();
			System.out.println("compactOnlyAtEnd: " + compactOnlyAtEnd);
			for (int xxx : vals) {
				x = xxx;
				System.out.println("\n adding " + xxx + " to the little tree");
				t.put(x, x);
				if (!compactOnlyAtEnd) {
					System.out.println("before compact:");
					System.out.println(t);
					t.compact();
					System.out.println("then");
					System.out.println(t);
				}
			}
			compactOnlyAtEnd = !compactOnlyAtEnd;
		} while (compactOnlyAtEnd);
		System.out.println("before compact:");
		System.out.println(t);
		t.compact();
		System.out.println("then");
		System.out.println(t);

		System.out.println("\n\n\n\n\n MORE HUGE TREES");
		vals = new int[] { 1000, //
				99, 1500, //
				10, 140, 1200, 2000, //
				4, 20, 110, 160, 1100, 1250, 1700, 2200, //
				2, 6, 15, 22, 103, 120, 150, 1050, 1150, 1225, 1600, //
				1, 3, 5, 11, 100, 1025, //
				0 };
		buildAndPrint(t, new ArrayIntSequence(vals));
		System.out.println("now balance t:");
		t.compact();
		System.out.println(t);

		System.out.println("\n\n\n add -1 as a test \n\n");
		x = -1;
		t.put(x, x);
		System.out.println(t);

		System.out.println("\n\n\n now sequential add \n\n");
		for (int i = 0; i < 20; i++) {
			x = -i;
			System.out.println("adding " + -i);
			t.put(x, x);
			System.out.println(t);
			System.out.println("added");
		}
	}

	static void buildAndPrint(MapTreeAVL<Integer, Integer> t, IntSequence is) {
		buildAndPrint(t, is, true);
	}

	static void buildAndPrint(MapTreeAVL<Integer, Integer> t, IntSequence is, boolean shouldPrint) {
		Integer x;
		t.clear();
		while (is.hasNext()) {
			x = is.next();
			t.put(x, x);
		}
		if (shouldPrint) {
			System.out.println("\n\n");
			System.out.println("Now the tree is:");
			System.out.println(t);
		}
	}

	//

	static interface IntSequence {
		int next();

		boolean hasNext();
	}

	static class ArrayIntSequence implements IntSequence {
		int i;
		final int[] a;

		ArrayIntSequence(int[] a) {
			this.a = a;
			this.i = 0;
		}

		@Override
		public int next() {
			return a[i++];
		}

		@Override
		public boolean hasNext() {
			return i < a.length;
		}
	}

	/** Minimum value is inclusive, maximum is exclusive */
	static class RangeIntSequence implements IntSequence {
		int i;
		final int min, max;

		/** See {@link RangeIntSequence}. */
		RangeIntSequence(int min, int max) {
			this.min = min;
			this.max = max;
			this.i = min;
		}

		@Override
		public int next() {
			return i++;
		}

		@Override
		public boolean hasNext() {
			return i < this.max;
		}
	}
}