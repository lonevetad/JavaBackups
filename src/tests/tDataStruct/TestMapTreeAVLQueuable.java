package tests.tDataStruct;

import java.util.Map.Entry;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import tools.Comparators;

public class TestMapTreeAVLQueuable {

	public static void main(String[] args) {
		Integer x;
		MapTreeAVL<Integer, Integer> t;
		Consumer<Entry<Integer, Integer>> printer;
		printer = e -> {
			if (e == null || e.getKey() == null)
				throw new RuntimeException("NULL KEY");
			System.out.println(e);
		};
		//
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.QueueFIFOIteration, MapTreeAVL.BehaviourOnKeyCollision.Replace,
				Comparators.INTEGER_COMPARATOR);
		System.out.println(t.getClass());
		System.out.println(t);
		for (int i = 0; i < 3; i++) {
			x = i;
			t.put(x, x);
		}
		System.out.println(t);
		System.out.println("\n\n NOW start to remove");
		t.forEach(printer);

		rem(t, printer, 0);
		rem(t, printer, 2);
		rem(t, printer, 1);

		System.out.println("\n\n\n\n\nremoving again stuffs");
		t.clear();
		for (int i = 0; i < 5; i++) {
			x = i;
			t.put(x, x);
		}
		System.out.println("refilled");
		System.out.println(t);
		rem(t, printer, 2);
		rem(t, printer, 0);
		rem(t, printer, 1);
		rem(t, printer, 4);
		x = 0;
		t.put(x, x);
		System.out.println("added 0 just to make noise");
		System.out.println(t);
		rem(t, printer, 3);

		System.out.println("\n\n\n\n\n\n\n\n removing last shit");
		for (int i = 0; i < 16; i++) {
			x = i;
			t.put(x, x);
			System.out.println("\n\n_____________________________________________________");
			System.out.println(t);
			t.forEach(printer);
		}
		System.out.println("\n\n\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n\n\nbefore removing:");
		System.out.println(t);
		t.forEach(printer);
		rem(t, printer, 15);
		rem(t, printer, 0);
	}

	static void rem(MapTreeAVL<Integer, Integer> t, Consumer<Entry<Integer, Integer>> printer, Integer x) {
		System.out.println("\n\n-----------------------------\n removing: " + x);
		t.remove(x);
		System.out.println(t);
		t.forEach(printer);
	}
}