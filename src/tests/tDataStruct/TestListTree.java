package tests.tDataStruct;

import java.util.function.Consumer;

import dataStructures.ListTree;

public class TestListTree {
	static final Consumer<Integer> PRINTER = e -> System.out.print(", " + e);

	public TestListTree() {
	}

	public static void main(String[] args) {
		ListTree<Integer> l;
		int[] vals = { 7, 5, -3, 16, -222, 0, 55 };

		l = new ListTree<>();
		System.out.println(l);
		System.out.println("now sstar insert");
		l.size();
		l.add(3);
		p(l);

		for (int x : vals) {
			System.out.println("adding: " + x);
			l.add(x);
			p(l);
		}
		System.out.println("\n\n\n HUGE INSERTION \n\n\n");
		for (int i = 0; i < 20; i++) {
			l.add(i);
			System.out.println("\n\nadding " + i);
			p(l);
		}
		System.out.println("\n\n after huge insertions:");
		p(l);
		System.out.println("\n\n\n\n\n now start adding at 0 \n\n\n\n");
		l.clear();
		for (int x : vals) {
			System.out.println("adding: " + x);
			l.add(0, x);
			p(l);
		}
		for (int i = 0; i < 20; i++) {
			l.add(0, i);
		}
		System.out.println("\n\n after huge insertions:");
		p(l);

		System.out.println("\n\n\n\n\n now start adding  at index\n\n\n\n");
		l.clear();

		for (int i = 0; i < 20; i++) {
			l.add(0, -(i + 1000));
		}
		System.out.println("\n\n after huge insertions:");
		p(l);
		System.out.println("\n now, add vals at indexes");
		int[] indexes = { 18, 2, 4, 6, 15, 25, 25 };
		for (int x = 0, i = 0; i < vals.length; i++) {
			x = vals[i];
			System.out.println("adding: " + x + " at index " + indexes[i] + ", where it was: " + //
					(indexes[i] < l.size() ? l.get(indexes[i]) : null));
			l.add(indexes[i], x);
			p(l);
		}
		System.out.println("adding: " + 666 + " at index 16, where it was: " + l.get(16));
		l.add(16, 666);
		p(l);

		System.out.println("\n\n\n now removing \n\n\n");
		vals = new int[] { -1019, 0, -1010, -1000, -1002, -1011, -1007 };
		for (int x : vals) {
			System.out.println("@@@ removing " + x);
			l.delete(Integer.valueOf(x));
			p(l);
		}

		System.out.println("\n\n now set a bit of indexes to 77 \n\n ");
		vals = new int[] { 8, 9, 10, 4, 13 };
		for (int x : vals) {
			l.set(x, 77);
		}
		System.out.println("\n\n finished setting:");
		p(l);
		System.out.println("\n\n FINE");
	}

	static void p(ListTree<Integer> l) {
		System.out.println(l);
		System.out.println("using for-each");
		l.forEach(PRINTER);
		System.out.println("\nreversed for each");
		l.forEachReversed(PRINTER);
		System.out.println("\n\n");
	}
}