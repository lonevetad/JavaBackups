package tests.tDataStruct;

import java.util.Arrays;
import java.util.Map.Entry;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.mtAvl.MapTreeAVLQueuable;

public class TestMapTreeAVL_GetAt {

	public TestMapTreeAVL_GetAt() {
	}

	public static void main(String[] args) {
		int s;
		int[] indexes;
		MapTreeAVL<Integer, Integer> t;
		Consumer<Entry<Integer, Integer>> entryPrinter;
		entryPrinter = e -> System.out.println(e);
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Integer::compare);

		s = 31;
		fill(s, t);

		indexes = new int[] { 0, s - 2, //
				((s - 2) >> 1), //
				(((s - 2) >> 1) + 1), //
				(((s + 1) >> 1) + 2) // 18
				, 0//
		};

		System.out.println("BEFORE:");
		System.out.println(t);

		for (int i : indexes) {
			System.out.println("\nremove at " + i);
			System.out.println(t.remove(t.getAt(i).getKey()));
			System.out.println(t);
		}

		System.out.println("+++++++++++++++++ MIN: " + t.peekMinimum().getKey() + ", max: " + t.peekMaximum().getKey());

		//

		System.out.println("\n\n\n work with few elements:\n\n\n\n");

		int[][] indexesMore = new int[][] { //
				new int[] { 0, 4, 2 }//
				, new int[] { 3, 3, 0 }//
				, new int[] { 5, 4, 3 }//
				, new int[] { 0, 0, 0 }//
		};
		s = 6;
		for (int ind = 0; ind < indexesMore.length; ind++) {
			t.clear();
			fill(s, t);
			indexes = indexesMore[ind];
			System.out.println("removing in row: " + Arrays.toString(indexes));
			System.out.println(t);
			for (int i : indexes) {
				System.out.println("\nremove at " + i);
				System.out.println(t.remove(t.getAt(i).getKey()));
				System.out.println(t);
			}
			System.out.println("\n----------------------------------------------------\n");
		}

		System.out.println("put 3 stuffs, remove min, then peek");
		s = 3;
		t.clear();
		fill(s, t);
		t.remove(t.getAt(0).getKey());
		System.out.println(t);
		System.out.println("min: " + t.peekMinimum());
		System.out.println("max: " + t.peekMaximum());

		System.out.println("\n\n put 3 stuffs, remove MAX, then peek");
		t.clear();
		fill(s, t);
		t.remove(t.getAt(2).getKey());
		System.out.println(t);
		System.out.println("min: " + t.peekMinimum());
		System.out.println("max: " + t.peekMaximum());

		//

		System.out.println("\n\n################ now test if tree's queue version is working well\n\n\n");
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.QueueFIFOIteration, Integer::compare);
		s = 15;
		fill(s, t);
		System.out.println(t);
		System.out.println("the element at index 1: " + t.getAt(1));
		System.out.println("then remove 2, then 1");
		t.remove(t.getAt(2).getKey());
		System.out.println(t);
		System.out.println("as a Queue");
		System.out.println("(the last inserted: " + ((MapTreeAVLQueuable<?, ?>) t).getLastInserted());
		t.forEach(MapTreeAVL.ForEachMode.Queue, entryPrinter);
		System.out.println("at index 1 we have: " + t.getAt(1).getKey());
		t.remove(t.getAt(1).getKey());
		System.out.println(t);

		System.out.println("now for-each printing everybody");
		System.out.println("as a Queue");
		t.forEach(MapTreeAVL.ForEachMode.Queue, entryPrinter);
		System.out.println("as a Stack");
		t.forEach(MapTreeAVL.ForEachMode.Stack, entryPrinter);
		System.out.println(t);

		System.out.println("\n\nTHEEEEEN: the last inserted: " + ((MapTreeAVLQueuable<?, ?>) t).getLastInserted());

		System.out.println(
				"\n\n\n ----- in the end, create a huge tree, remove a leaf node at the extreme left and right of a subtree and see what happens");
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Integer::compare);
		fill(31, t);
		System.out.println(t);

		int[] toBeRemoved = { 8, 9, 10, 24, 25, 26 };
		System.out.println("Remove " + Arrays.toString(toBeRemoved) + ", then see");
		for (int r : toBeRemoved)
			t.remove(r);
		System.out.println(t);
		System.out.println("remove 30");
		t.remove(30);
		System.out.println(t);

	}

	static void fill(int s, MapTreeAVL<Integer, Integer> t) {
		Integer x;
		for (int i = 0; i < s; i++)
			t.put(x = i, x);
	}
}