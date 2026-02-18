package tests.tDataStruct;

import java.util.Arrays;
import java.util.LinkedList;

import dataStructures.TreeComparable;
import tools.Comparators;

public class Test_NodeCompGetChild {

	public static void main(String[] args) {
		TreeComparable<Integer> t;
		LinkedList<Integer> l;
		t = new TreeComparable<>(Comparators.INTEGER_COMPARATOR);
		System.out.println("start");
		t.addNode(0, null);
		a(t, 7, 0, 10);
		p(t);
		l = new LinkedList<>();
		l.add(0);
		l.add(-5);
//		l.add(-88);
		t.addNode(-88, l);
		t.addNode(-77, l);
		l.clear();
		l.add(0);
		l.add(10);
		t.addNode(13, l);
		p(t);
		a(t, 2, 0);
		p(t);
		System.out.println("\n\n\n end");
	}

	static void a(TreeComparable<Integer> t, Integer x, Integer... c) { t.addNode(x, Arrays.asList(c)); }

	static void p(TreeComparable<Integer> t) {
		System.out.println("\ntree:");
		System.out.println(t);
	}
}