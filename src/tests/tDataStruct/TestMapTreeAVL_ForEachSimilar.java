package tests.tDataStruct;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import tools.Comparators;

public class TestMapTreeAVL_ForEachSimilar {

	public static void main(String[] args) {
		MapTreeAVL<String, String> t;
		Comparator<String> similarComparator;
//		Comparator<Entry<String, String>> sc;
		Consumer<Entry<String, String>> cons;
//		String toTest;
		String[] dataset = { "abba", "diana", "damocle", "banana", "bonobo", "dado", "dodo", "elettroni", "capperi",
				"elektra", "ambarabacciccicocco", "computer", "cpu", "algorithm", "pc", "existence", "life", "abstract",
				"data", "class", "method", "variable"//
				, "allocco"//
		};
		similarComparator = (s1, s2) -> Character.compare(s1.charAt(0), s2.charAt(0));
		cons = e -> System.out.println(e.getKey());
//		sc = (e1, e2) -> { return similarComparator.compare(e1.getKey(), e2.getKey()); };
		t = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.STRING_COMPARATOR);
		for (String s : dataset) {
			t.put(s, s);
		}
		System.out.println(t);
//		System.out.println("foreaching");
//		t.forEach(cons);

		System.out.println("START TESTING ...\n\n\n");
		for (String s : new String[] { "c", "d", "p", "a", "v", "e" }) {
			System.out.println("\n\nSearching for: --" + s + "--");
			t.forEachSimilar(s, similarComparator, cons);
		}
	}

}
