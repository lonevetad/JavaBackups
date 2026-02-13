package common.tests.testsLittle;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import common.mainTools.MyComparator;
import common.mainTools.MapTreeAVL;
import common.mainTools.MapTreeAVL.ForEachMode;

public class TestTreeAVLToCollections {

	public TestTreeAVLToCollections() {
	}

	private static final Consumer<Entry<Integer, ? extends Object>> PRINTER = (e) -> {
		Integer k;
		Object v;
		k = e.getKey();
		v = e.getValue();
		System.out.print(" (k:" + k + ",v:" + v + ")");
	};
	private static final Consumer<? extends Object> PRINTER_K = (k) -> System.out.print(" (k:" + k + ")");

	public static void main(String[] args) {
		mainTest();
		testKeyExtractor();
	}

	@SuppressWarnings("unchecked")
	private static void testKeyExtractor() {
		String xx;
		String[] ar;
		Set<String> wrapperSet;
		MapTreeAVL<Integer, String> t;
		MyComparator<Integer> c;
		Function<String, Integer> ke;

		System.out.println("\n\n\n\n TEST KEY ETRACTOR AND SET WRAPPER\n\n\n");
		c = common.mainTools.Comparators.INTEGER_COMPARATOR;
		t = new MapTreeAVL<Integer, String>(c);

		ke = (Function<String, Integer>) val -> Integer.valueOf(val.hashCode());
		wrapperSet = t.toSetValue(ke);

		//
		p(t);

		wrapperSet.add("LAL");
		System.out.println("adding 'LAL' string");
		p(t);
		System.out.println("current size: " + t.size() + "..now add a bulk of strings ..");

		ar = new String[] { "Trentatre", "trentini", "entrarono", "in", "Trento", "continua tu.." };
		for (String x : ar)
			t.put(ke.apply(x), x);
		p(t);
		System.out.println("now do foreach on list:\n");
		wrapperSet.forEach(PRINTER_K);
		System.out.println("\n and iterate over it:\n\t");
		for (String x : wrapperSet)
			System.out.print(" " + x);

		System.out.println("\nsize after iteration: " + t.size());
		System.out.println("\n\n now unbalance adding 'lal' to wrapper set");
		xx = "lal";
		wrapperSet.add(xx);
		p(t);

		for (int i = 0; i <= 28; i++) {
			wrapperSet.add(Integer.toString(i));
//			t.put(ke.apply(xx), xx);
		}

		System.out.println("...\n\n..++" + "its size after bulk of integers: " + t.size());
		p(t);
		System.out.println("\n.....now do foreach on list:\n");
		wrapperSet.forEach(PRINTER_K);
		System.out.println("\n and iterate over it:\n\t");
		for (String x : wrapperSet)
			System.out.print(" " + x);

	}

	@SuppressWarnings("unchecked")
	private static void mainTest() {
		Integer xx;
		Integer[] ar;
		List<Integer> wrapperList;
		MapTreeAVL<Integer, Integer> t;

		t = new MapTreeAVL<>(common.mainTools.Comparators.INTEGER_COMPARATOR);
		wrapperList = t.toListValue(val -> val); // identity

		//
		p(t);

		wrapperList.add(7);
		p(t);

		ar = new Integer[] { 7, 17, 2, 9 };
		for (Integer x : ar)
			t.put(x, x);
		p(t);
		System.out.println("now do foreach on list:\n");
		wrapperList.forEach(PRINTER_K);
		System.out.println("\n and iterate over it:\n\t");
		for (Integer x : wrapperList)
			System.out.print(" " + x);

		System.out.println("\n\n now unbalance");
		xx = 8;
		t.put(xx, xx);
		p(t);

		for (int i = 0; i <= 28; i++) {
			xx = i;
			t.put(xx, xx);
		}

		System.out.println("...\n\n..++" + "");
		p(t);
		System.out.println("\n.....now do foreach on list:\n");
		wrapperList.forEach(PRINTER_K);
		System.out.println("\n and iterate over it:\n\t");
		for (Integer x : wrapperList)
			System.out.print(" " + x);

	}

	static void p(MapTreeAVL<Integer, ?> t) {
		System.out.println();
		System.out.println(t);
		System.out.println("tree with size: " + t.size() + "\n\t");
		t.forEachEntry(PRINTER);
		System.out.println();
		t.forEachEntry(ForEachMode.Queue, PRINTER);
		System.out.println("\n...\n");
	}
}