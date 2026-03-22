package tests.tDataStruct;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Iterator;

import dataStructures.MapTreeAVL;

public class Test_MTAvl_onIteratorGettingNull {

	public Test_MTAvl_onIteratorGettingNull() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		MapTreeAVL<Integer, Integer> m;
		MapTreeAVL.Optimizations[] opts;
		Comparator<Integer> intComp;
		Integer key;
		int[] values;
		int i, len, counter;
		Iterator<java.util.Map.Entry<Integer, Integer>> iter;
		PrintStream out;

		System.out.println("start");

		try {
			out = // System.out;
					new PrintStream("./res.txt");

			intComp = Integer::compare;
			/*
			 * len = 14; values = (new Random()).ints().limit(len).toArray();
			 */
			values = new int[] { 7, 2, 0, -11, 4, 3, 15, 8, -8, 9, 6, -4, 33, 1, 100 };
			len = values.length;
			opts = MapTreeAVL.Optimizations.values();

			out.print("adding the following values:\n\t");
			i = -1;
			while (++i < len) {
				out.print(values[i]);
				out.print(", ");
			}
			out.println();
			out.flush();

			for (MapTreeAVL.Optimizations opt : opts) {
				out.println("Starting test with optimization: " + opt.name());
				System.out.println("Starting test with optimization: " + opt.name());
				m = MapTreeAVL.newMap(opt, intComp);
				out.println("adding values:");
				out.flush();
				i = -1;
				while (++i < len) {
					key = values[i];
					out.println("-- at " + i + " add: " + key + " now has values:");
					out.flush();
					m.put(key, key);
					iter = m.iterator();
					counter = 0;
					while (iter.hasNext() && counter++ < len) {
						out.print(iter.next().getKey() + ", ");
						out.flush();
					}
					out.println("\n\n______");
					out.flush();
				}
				m.clear();
				out.println("\n\n\n ++++++++++++++");
				out.flush();
			}

			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("end");
	}

}
