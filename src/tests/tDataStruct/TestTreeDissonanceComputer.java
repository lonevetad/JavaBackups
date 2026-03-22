package tests.tDataStruct;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import dataStructures.NodeComparable;
import dataStructures.TreeComparable;
import tools.Comparators;

public class TestTreeDissonanceComputer extends TreeComparable<Integer> {
	private static final long serialVersionUID = 1L;

	public TestTreeDissonanceComputer() { super(Comparators.INTEGER_COMPARATOR); }

	/** See {@link NodeComparable#computeDissonanceAsLong(NodeComparable)}. */
	public long computeDiff(TestTreeDissonanceComputer theBase) {
		if (this.root == theBase.root)
			return 0;
		if (this.root == null)
			return theBase.computeDiff(this);
		return this.root.computeDissonanceAsLong(theBase.root);
	}

	//

	//

	//

	public static void main(String[] args) {
		Integer x;
		TestTreeDissonanceComputer t, altro;
		LinkedList<Integer> l;
		l = new LinkedList<>();
		t = new TestTreeDissonanceComputer();
		altro = new TestTreeDissonanceComputer();
		System.out.println("START");
		d(altro, t);
		x = 7;
		ap(t, x, null);
		System.out.println("added");
		l.add(x);

		for (int i = 0; i < 4; i++)
			ap(t, i * 10, l);

		pp(t, altro);
		d(altro, t);
		l.add(20);
		ap(t, 25, l);
		ap(t, 13, l);
		l.add(25);
		System.out.println("\n\n\n\n eh eh");
		ap(t, 22, l);

		System.out.println("#### adding " + x + " to altro");
		altro.addNode(x, null);
		System.out.println(altro);
		d(altro, t);

		System.out.println("\n\nyay");
		l.clear();
		System.out.println("---------------------------");
		System.out.println("adding stuff to altro");
		l.add(x);
		altro.addNode(10, l);
		altro.addNode(20, l);
		System.out.println("AND now 10 and 20");
		System.out.println(altro);
		System.out.println("now add some other blabla");
//		altro.addNode(x, null);
		l.add(20);
		ap(altro, 25, l);
		l.add(25);
		ap(altro, 22, l);
		System.out.println("\n\n now do tests between");
		System.out.println(t);
		System.out.println(altro);
		System.out.println(":D");
		d(altro, t);

		System.out.println("now add -8: " + Arrays.toString(l.toArray()));
		ap(altro, -8, l);
		System.out.println("\n\n do it again, between");
		System.out.println(t);
		System.out.println(altro);
		System.out.println(":D");
		d(altro, t);
		System.out.println("what if I add 7->50 and 7->50->55 to the altro?");
		altro.addNode(50, Arrays.asList(7));
		altro.addNode(55, Arrays.asList(7, 50));
		pp(t, altro);
		d(altro, t);

		altro.getRoot().getChildrenNC().remove(altro.nodeSupplier.apply(50, altro.keyComparator).addChildNC(//
				altro.nodeSupplier.apply(55, altro.keyComparator)));
		System.out.println("new altro :D");
		System.out.println(altro);

		//

		System.out.println("\n\n\n[[[[[[[[[[[[[[[[ now complete altro");
		l.clear();
		l.add(x);
		altro.addNode(30, l);
		System.out.println("after added 30:");
		System.out.println(altro);
		altro.addNode(0, l);
		l.add(20);
		System.out.println("altro .... l: " + Arrays.toString(l.toArray()));
		for (int i = 0; i < 20; i++) {
			System.out.println(" force adding " + i + " times");
			altro.addNode(13, l);
		}
		System.out.println(altro);
		System.out.println("t was");
		System.out.println(t);
		d(altro, t);

		System.out.println("fixing t by adding -8");
		l.add(25);
		ap(t, -8, l);
		System.out.println("diffff");
		pp(t, altro);
		d(altro, t);

		for (int i = 0; i < 5; i++)
			System.out.println("---------------------------");
		System.out.println("\n\n now grow them up");
		t.root = null;
		altro.root = null;
		t.addNode(x, null);
		altro.addNode(-3, null);
		pp(t, altro);
		d(altro, t);
		altro.addNode(x, null);
		d(altro, t);
		System.out.println("+++++++++++++ now make it more complex: 2 levels");
		pp(t, altro);
		l.clear();
		l.add(x);
		ap(t, 10, l);
		System.out.println(altro);
		d(altro, t);
		System.out.println("adding -3 and stuffs");
		altro.addNode(-3, l);
		d(altro, t);
		ap(altro, 10, l);
		pp(t, altro);
		d(altro, t);
		System.out.println("]]]]][[[[[level 3");
		l.add(10);
		ap(altro, 55, l);
		d(altro, t);
		l.removeLast();
		ap(t, 16, l);
		d(altro, t);
		pp(t, altro);
		System.out.println("now en-fat");
		ap(altro, 16, l);
		t.addNode(-3, l);
		l.add(10);
		t.addNode(55, l);
		l.removeLast();
		l.add(-3);
		t.addNode(-33, l);
		t.addNode(-74, l);
		altro.addNode(2, l);
		ap(altro, 44, Arrays.asList(7, 10));
		pp(t, altro);
		d(altro, t);
		System.out.println("now fix");
		t.addNode(2, l);
		altro.addNode(-33, l);
		altro.addNode(-74, l);
		l.add(2);
		t.addNode(222, l);
		altro.addNode(222, l);
		l.clear();
		l.add(7);
		l.add(10);
		t.addNode(44, l);
		pp(t, altro);
		d(altro, t);
	}

	static void pp(TestTreeDissonanceComputer t, TestTreeDissonanceComputer altro) {
		System.out.println("print em all");
		System.out.println(t);
		System.out.println(altro);
	}

	static void d(TestTreeDissonanceComputer toBeTested, TestTreeDissonanceComputer theBase) {
		System.out.print("\n\n testing differences:    ->");
		System.out.println(theBase.computeDiff(toBeTested));
	}

	static void ap(TestTreeDissonanceComputer t, Integer v, List<Integer> path) {
		System.out.println("\n adding: " + v);
		t.addNode(v, path);
		System.out.println("tree:");
		System.out.println(t);
		System.out.println();
	}

}