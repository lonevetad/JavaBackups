package tests.tDataStruct;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

import dataStructures.PriorityQueueKey;
import tools.LoggerMessages;
import tools.impl.LoggerOnFile;

public class TestPriorityQueueKey {

	static final Comparator<JustAComplexType> COMPARATOR_JustAComplexType = (c1, c2) -> {
		if (c1 == c2)
			return 0;
		return c1.id > c2.id ? 1 : (c1.id < c2.id ? -1 : 0);
	};
	static final Comparator<Integer> COMPARATOR_INTEGER = // Integer::compare;
			(i1, i2) -> {
				if (i1 == null && i2 == null)
					return 0;
				if (i1 == null)
					return -1;
				if (i2 == null)
					return 1;
				return Integer.compare(i1, i2);
			};
	static final Function<JustAComplexType, Integer> KEY_EXTRACTOR = jact -> jact.val;

	//

	public TestPriorityQueueKey() throws IOException {
		try {
			printer = new LoggerOnFile("output_TestPriorityQueueKey.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
			printer = LoggerMessages.LOGGER_DEFAULT;
		}
		rand = new Random();
		p = new PriorityQueueKey<>(COMPARATOR_JustAComplexType, COMPARATOR_INTEGER, KEY_EXTRACTOR);

		printerOnlyKey = e -> printer.log(e.getKey() + ", ");
		printerPQK = e -> {
			JustAComplexType c;
			String text;
			if (e == null) {
				System.out.println("STIGHEZZI");
			}
			c = e.getKey();
			text = "id: " + c.id + "\t-- val: " + c.val + '\n';
			printer.log(text);
		};

		printAllPQK = pq -> {
			printer.log("Printing pq having " + pq.size() + " elements:\n");
			printer.log("id -> keys\n--------------\n");
			pq.forEach(printerPQK);
			pq.forEach(printerOnlyKey);
			printer.log('\n' + pq.toString() + '\n');
		};

		testerIntegrity = pqk -> {
			boolean testKey;
			printer.log("\n\nSTART TEST INTEGRITY");
			testKey = false;
			do {
				// do test
				printer.log(testKey ? "-key" : "-value\n");
//				printer.log("--is sorted? " + pqk.assertSorted(testKey) + '\n');
//				printer.log("--is balanced? " + pqk.assertBalanced(testKey) + '\n');

				// swap test
				testKey = !testKey;
			} while (testKey);
			printer.log("\n\nEND TEST INTEGRITY");
		};

	}

	// for printing
	private LoggerMessages printer;
	// other
	private final PriorityQueueKey<JustAComplexType, Integer> p;
	private final Random rand;
	private final Consumer<Entry<JustAComplexType, Integer>> printerPQK;
	private final Consumer<Entry<JustAComplexType, Integer>> printerOnlyKey;
//	private final Consumer<String> printer;
	private final Consumer<PriorityQueueKey<JustAComplexType, Integer>> testerIntegrity, printAllPQK;

//

	//

	public void testAddRandom() {
		long time, delta;
		JustAComplexType temp;
		Integer t;

//

		time = System.currentTimeMillis();

		testerIntegrity.accept(p);

		printer.log("\n\n START TEST ADD-PUT");
		// test add

		printAllPQK.accept(p);
		temp = new JustAComplexType(16180);
		p.put(temp);

		printAllPQK.accept(p);
		testerIntegrity.accept(p);

		// a bit of add
		printer.log(".......... a bit of add");

		for (int i = 0; i < 10; i++) {
			t = Integer.valueOf(i);
			temp = new JustAComplexType(t);
			p.put(temp);
		}
		printAllPQK.accept(p);

		testerIntegrity.accept(p);
//
		// a bit more of random add
		printer.log(".......... a bit more of random add");

		for (int i = 0; i < 35; i++) {
			t = Integer.valueOf(rand.nextInt());
			temp = new JustAComplexType(t);
			p.put(temp);
		}
		printAllPQK.accept(p);
		testerIntegrity.accept(p);

		// TONS of add
		printer.log(".......... TONS of add");

		for (int i = 0; i < 1000000; i++) {
			t = Integer.valueOf(rand.nextInt());
			temp = new JustAComplexType(t);
			p.put(temp);
		}
//		printAllPQK.accept(p);
		testerIntegrity.accept(p);

//
		printer.log("\n\n END TEST ADD-PUT");
		delta = System.currentTimeMillis() - time;
		printer.log("took " + delta + " milliseconds\n\n");

		//

		printer.log("\n\n START TEST REMOVE");
		time = System.currentTimeMillis();
		//
		printer.log("\n\n END TEST REMOVE");
		delta = System.currentTimeMillis() - time;
		printer.log("took " + delta + " milliseconds\n\n");

		//

		printer.log("\n\n START TEST MODIFY");
		time = System.currentTimeMillis();
		//
		printer.log("\n\n END TEST MODIFY");
		delta = System.currentTimeMillis() - time;
		printer.log("took " + delta + " milliseconds\n\n");
	}

	public void testAddHuge() {
		int amount, tenFactor;
		long time, delta;
		JustAComplexType temp;
		Integer t;

		amount = 10000;
		tenFactor = 5;
		for (int x = 0; x < tenFactor; x++) {

			p.clear();
			System.gc();
			printer.log("\n\n START TEST ADD HUGE " + x + ": " + amount);
			time = System.currentTimeMillis();

			for (int i = 0; i < amount; i++) {
				t = Integer.valueOf(rand.nextInt());
				temp = new JustAComplexType(t);
				p.put(temp);
			}
			//
			delta = System.currentTimeMillis() - time;
			testerIntegrity.accept(p);
			printer.log("\n\n END TEST ADD HUGE");
			printer.log("took " + delta + " milliseconds\n\n");

			amount *= 10;
		}
	}

	// studio particolare di esempi pensati apposta per far risaltare alcuni punti
	// importanti
	public void testParticularAddRemoveAlter() {
		int i, x;
//		long time, delta;
		int[] vals;
		JustAComplexType temp;
		Integer t;
		Entry<JustAComplexType, Integer> entry;

		vals = new int[] { 10, 22, 15, 6, 3, 4, 8, //
//				-1, //
				7, 22, 2, 11, 44 };

		printer.log("\n\n START TEST ADD particular");
		i = -1;
		while (++i < vals.length) {
			t = Integer.valueOf(vals[i]);
			temp = new JustAComplexType(t);
			p.put(temp);
			printer.log("\n\n\n populating the queue with: " + vals[i] + "\n");
			printAllPQK.accept(p);
		}
		printer.log("\n\n NOW min is: : " + p.peekMinimum() + " , and max is: " + p.peekMaximum());

		printer.log("\n\n\n AAAAAAAAAAAAAHHHHH priority queue filled, now peek, remove, alter, etc ...\n");

		// TODO fare rimozioni a caso, aggiunte e poi modify ignoranti

		printer.log("Picking value with id=2 : " + p.get(new JustAComplexType(2, null)) + "\n");
		// remove
		vals = new int[] { 8, 4, 6, 9, 0 };
		temp = null;
		i = -1;
		while (++i < vals.length) {
			t = Integer.valueOf(x = vals[i]);
			temp = new JustAComplexType(x, t);
			entry = p.remove(temp);
			printer.log("\n\n\n removing: " + temp + " results as: " + entry + "\n");
			printAllPQK.accept(p);
			printer.log(
					"now min is: " + p.peekMinimum().getValue() + ", and max is:" + p.peekMaximum().getValue() + "\n");
		}

		printer.log("\n\n\n END removing\n");
		printAllPQK.accept(p);

		printer.log("\n\n\n has still the last removed one, that is: " + temp + "?: " + p.get(temp) + "\n");
		printer.log("Picking value with id=2 : " + p.get(new JustAComplexType(2, null)) + "\n");
		printer.log("\n\n\n removing again the last removed one: " + p.remove(temp) + "\n");
		printAllPQK.accept(p);
		printer.log("NOW min is: : " + p.peekMinimum() + " , and max is: " + p.peekMaximum() + "\n\n");

		vals = new int[] { -2, 22, 10, 17, 3, 8, 22, 2, -3, 0, 52, 33, 100, -31415, 16180, 10, -51, -52, -53 };
		printer.log("\n\n\n refill the queue with " + vals.length + " new elements\n");
		i = -1;
		while (++i < vals.length) {
			t = Integer.valueOf(vals[i]);
			temp = new JustAComplexType(t);
			printer.log("refilling with: " + temp + "\n");
			printer.log("..before min is: : " + p.peekMinimum() + " , and max is: " + p.peekMaximum() + "\n");
			p.put(temp);
			printer.log("..NOW min is: : " + p.peekMinimum());
			printer.log(" , and max is: " + p.peekMaximum() + "\n\n");

			if (COMPARATOR_INTEGER.compare(t, p.peekMaximum().getValue()) > 0) {
				printer.log("\n\n inserted's key; " + KEY_EXTRACTOR.apply(temp) + ", max's key: "
						+ KEY_EXTRACTOR.apply(p.peekMaximum().getKey()) + "\n\n");
				throw new RuntimeException("inserted " + t + " > " + p.peekMaximum().getValue() + " that is maximum");
			}

		}

		printAllPQK.accept(p);
		printer.log("now min is: " + p.peekMinimum().getValue() + "\n");

		printer.log("\n\n\n START altering\n");

		temp = new JustAComplexType(14, 10);
		printer.log("getting entry with id 14:  " + p.get(temp) + "\n");
		entry = p.alterKey(temp, j -> j.setVal(12));
		printer.log("entry returned on alter: " + String.valueOf(entry) + "\n\n");
		printAllPQK.accept(p);

		printer.log("\n\n now change root \n");
		temp = new JustAComplexType(10, 11);
		entry = p.alterKey(temp, j -> j.setVal(5));
		printer.log("entry returned on alter: " + String.valueOf(entry) + "\n\n");
		printAllPQK.accept(p);

		printer.log("\n\n now change min \n");
		temp = new JustAComplexType(25, -31415);
		entry = p.alterKey(temp, j -> j.setVal(-314159265));
		printer.log("entry returned on alter: " + String.valueOf(entry) + "\n\n");
		printAllPQK.accept(p);

		printer.log("\n\n now change min again, reverting its sign \n");
		printer.log("recycling the previous \"temp\" value: " + temp + "\n");
//		temp = new JustAComplexType(25, -314159265);
		entry = p.alterKey(temp, j -> j.setVal(-j.val / 100));
		printer.log("entry returned on alter: " + String.valueOf(entry) + "\n\n");
		printAllPQK.accept(p);
		printer.log("\n\n\n now min is: " + p.peekMinimum().getValue() + ", and max is:" + p.peekMaximum().getValue()
				+ "\n\n");

		printer.log("\n\n now change max \n");
		temp = new JustAComplexType(26, 16180);
		entry = p.alterKey(temp, j -> j.setVal(161800898));
		printer.log("entry returned on alter: " + String.valueOf(entry) + "\n\n");
		printAllPQK.accept(p);
		printer.log("\n\n\n now min is: " + p.peekMinimum().getValue() + ", and max is:" + p.peekMaximum().getValue()
				+ "\n\n");

		printer.log("\n\n now change something with 0 child \n");
		printer.log("\n\n now change something with 1 child \n");
		printer.log("\n\n now change something with 2 children \n");

		printer.log("\n\n\n END altering\n");

		printer.log("\n\n END TEST END particular");
	}

	//

	// TODO MAIN

	//

	public static void main(String[] args) {
		int i;
		TestPriorityQueueKey tester;
		Scanner scan;
		String s, toQuit[];
		RunnerTestNamed[] tests;
		RunnerTestNamed runnerTestNamed;

		try {

			toQuit = new String[] { "e", "exit", "quit", "q", "close", "c", "no", "n" };

			scan = new Scanner(System.in);
			s = null;

			tester = new TestPriorityQueueKey();
			tests = new RunnerTestNamed[] { //
					new RunnerTestNamed("Add random", tester::testAddRandom), //
					new RunnerTestNamed("Add huge", tester::testAddHuge), //
					new RunnerTestNamed("Particular study", tester::testParticularAddRemoveAlter) //
			};

			do {

				System.out.println("Choose your test:");
				for (i = 0; i < tests.length; i++) {
					runnerTestNamed = tests[i];
					System.out.println(i + " -- " + runnerTestNamed.name);
				}
				System.out
						.println("What you choose? Write the index, or to exit write one of the following outputs:\n\t"
								+ Arrays.toString(toQuit));

				s = scan.nextLine();

				i = -1;
				while (s != null & ++i < toQuit.length) {
					if (toQuit[i].equalsIgnoreCase(s))
						s = null;
				}

				if (s == null) {
					System.out.println("bye");
				} else {
					if (hasOnlyDigits(s) && //
							(i = Integer.parseInt(s)) >= 0 && i < tests.length) {

						runnerTestNamed = tests[i];
						tester.printer.log("\n\n\n ---------------------------\n\n");
						tester.printer.log("Performing the " + i + "-th test: " + runnerTestNamed.name);
						JustAComplexType.idProgressive = 0;
						runnerTestNamed.test.run();
						tester.p.clear();
					} else {
						System.out.println("Wrong input, cannot understand.");
					}
				}

			} while (s != null);

			scan.close();
			System.out.println("end");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	//

	// TODO MAIN END

	private static boolean hasOnlyDigits(String s) {
		int i, len;
		char c;
		len = s.length();
		i = -1;
		while (++i < len && (c = s.charAt(i)) >= '0' && c <= '9')
			;
		return i >= len;
	}

	//

	static class JustAComplexType {
		static int idProgressive = 0;

		JustAComplexType(Integer v) {
			this(idProgressive++, v);
		}

		private JustAComplexType(int id, Integer v) {
			this.id = id;
			this.val = v;
		}

		private final int id;
		Integer val;

		public void setVal(Integer v) {
			this.val = v;
		}

		@Override
		public String toString() {
			return "[id=" + id + ", val=" + val + "]";
		}
	}

	static class RunnerTestNamed {

		protected RunnerTestNamed(String name, Runnable test) {
			super();
			this.name = name;
			this.test = test;
		}

		String name;
		Runnable test;
	}
}