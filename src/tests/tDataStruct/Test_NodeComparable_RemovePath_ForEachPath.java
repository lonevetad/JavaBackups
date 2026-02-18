package tests.tDataStruct;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import dataStructures.NodeComparable;
import dataStructures.SortedSetEnhanced;
import tools.Comparators;

public class Test_NodeComparable_RemovePath_ForEachPath {

	public static void main(String[] args) {
		TreeCompInteger t;
		SortedSetEnhanced<NodeComparable<Integer>> forest;
		LinkedList<NodeComparable<Integer>> path;
		Consumer<NodeComparable<Integer>> printer;
		Consumer<List<Integer>> printerList;
//
		printer = n -> System.out.println(n);
		printerList = l -> { System.out.println(Arrays.toString(l.toArray())); };
		t = TreeCompInteger.fromString(
				Test_NodeComparableSubtreeBased_Compare.TREE_PAIRS_TO_TEST[Test_NodeComparableSubtreeBased_Compare.TREE_PAIRS_TO_TEST.length
						- 1][1]);
		t.getRoot().forEachPathKey(printerList);
		System.out.println("test tree:");
		printer.accept(t.getRoot());
		path = new LinkedList<>();

		System.out.println("\n\nA simple 7 gives ...");
		path.add(NodeComparable.newDefaultNodeComparable(7, Comparators.INTEGER_COMPARATOR));
		forest = NodeComparable.removePath(t.getRoot(), path);
		forest.forEach(printer);
		//
		path.clear();
		for (int x : new int[] { 2, 1, 0, 8 }) {
			path.add(NodeComparable.newDefaultNodeComparable(x, Comparators.INTEGER_COMPARATOR));
		}
		System.out.println("\n\n with new path:");
		forest = NodeComparable.removePath(t.getRoot(), path);
		forest.forEach(printer);
		//

		System.out.println("\n\n\n\n now iterates for each .. starting from t");
		t.getRoot().forEachPathKey(printerList);
		System.out.println("\n\n\n now iterating over each path of the last forest's trees");
		forest.forEach(n -> n.forEachPathKey(printerList));
		System.out.println("\n\n\n AAAAAAAAAH\niterating with a for-each");
		printPathsThroughIterator(t.getRoot(), printerList);
		System.out.println("and into the forest");
		forest.forEach(n -> Test_NodeComparable_RemovePath_ForEachPath.printPathsThroughIterator(n, printerList));
		System.out.println("\n\n\n end");
		System.out.println("..");
		printPathsThroughIterator(NodeComparable.newDefaultNodeComparable(8, Comparators.INTEGER_COMPARATOR),
				printerList);
		System.out.println("...");

	}

	static void printPathsThroughIterator(NodeComparable<Integer> t, Consumer<List<Integer>> printerList) {
		var iter = t.iteratorPathKeys();
		while (iter.hasNext()) {
			printerList.accept(iter.next());
		}
	}
}