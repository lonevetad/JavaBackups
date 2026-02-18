package tests.tDataStruct;

import java.util.Comparator;
import java.util.function.Function;

import dataStructures.PriorityQueueKey;
import tools.Comparators;

public class TestPriorityQueue2 {
	static final int[] KEYS = { //
			5, 4, 7, 8, 6, //
			13, 2, 0, 20, 31, //
			1, 15 };

	static final String[] STR = { //
			"ci", "ao", "mon", "do", ":D", //
			"AVLTree", "based", "min", "priority", "queue", //
			"test", "due" //
	};

	public static void main(String[] args) {
		int i, len;
		Integer x;
		PriorityQueueKey<VHK, Integer> pq;
		pq = new PriorityQueueKey<>(VHK.VHK_COMPARATOR, Comparators.INTEGER_COMPARATOR, VHK.KEY_EXTRACTOR);
		i = -1;
		len = KEYS.length;
		System.out.println("now add");
		while (++i < len) {
			pq.put(new VHK(KEYS[i], STR[i]));
		}
		System.out.println(pq.toString());
//		x = 13;
		x = 8;
		System.out.println("\n\nremoving " + x);
		pq.remove(new VHK(x));
		System.out.println(pq.toString());
		System.out.println("\n\n\n fine");
	}

	static class VHK {
		static final Function<VHK, Integer> KEY_EXTRACTOR = k -> k.i;
		static final Comparator<VHK> VHK_COMPARATOR = (k1, k2) -> Comparators.INTEGER_COMPARATOR.compare(k1.i, k2.i);
		public Integer i;
		public String s;

		public VHK(Integer i) { this(i, null); }

		public VHK(Integer i, String s) {
			super();
			this.i = i;
			this.s = s;
		}

		@Override
		public String toString() { return "K [" + i + " <=> " + s + "]"; }

	}
}