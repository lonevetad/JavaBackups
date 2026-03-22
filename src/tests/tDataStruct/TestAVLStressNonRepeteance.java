package tests.tDataStruct;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import tools.Comparators;
import tools.LoggerMessages;

public class TestAVLStressNonRepeteance {
	static final int MAX_CARDINALITY = 100000, RANDOM_BOUND = MAX_CARDINALITY << 2,
			REMOVE_AMOUNT = MAX_CARDINALITY >> 2;
	static final LoggerMessages LOG_GER = LoggerMessages.LOGGER_DEFAULT;

	public static void main(String[] args) {
		Comparator<Integer> c;
		Random r;
//		LoggerMessages logger;
		c = Comparators.INTEGER_COMPARATOR;
		Consumer<Entry<Integer, Integer>> printer;
		printer = e -> {
			if (e == null || e.getKey() == null)
				throw new RuntimeException("NULL KEY");
			System.out.println(e);
		};
		r = new Random();
//		for (MapTreeAVL.Optimizations o : MapTreeAVL.Optimizations.values()) {
//			testMapWithOptimization(r, o, printer, c);
//		}
		ExecutorService executor = Executors.newFixedThreadPool(MapTreeAVL.Optimizations.values().length);
		for (MapTreeAVL.Optimizations o : MapTreeAVL.Optimizations.values()) {
			executor.execute(() -> testMapWithOptimization(LOG_GER, r, o, printer, c));
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("END");
	}

	public static void testMapWithOptimization(LoggerMessages logger, Random r, MapTreeAVL.Optimizations optimization,
			Consumer<Entry<Integer, Integer>> printer, Comparator<Integer> c) {
		MapTreeAVL<Integer, Integer> t;
		Integer x;
		Set<Integer> duplicatesChecker;
		duplicatesChecker = new HashSet<>(MAX_CARDINALITY >> 2);
		//
		logger.log("#\n#\n# ------------------------------------------------------------------" + optimization.name()
				+ "\n#\n#\n#\n\n");
		t = MapTreeAVL.newMap(optimization, MapTreeAVL.BehaviourOnKeyCollision.Replace, c);
		for (int repetition = 0; repetition < 100; repetition++) {
			duplicatesChecker.clear();
			logger.logAndPrint("\n\nrepetition: " + repetition);
			for (int i = 0; i < MAX_CARDINALITY; i++) {
				x = r.nextInt(RANDOM_BOUND);
				t.put(x, x);
			}
			for (int i = 0; i < REMOVE_AMOUNT; i++) {
				x = r.nextInt(RANDOM_BOUND);
				t.remove(x);
			}
			// check for duplicates
			t.forEach((k, v) -> {
				if (duplicatesChecker.contains(k)) {
//					logger.log(t.toString());
					t.forEachAndDepth((e, depth) -> {
						while (depth-- > 0) {
							logger.logNoNewLine("\t");
						}
						logger.log(String.valueOf(e.getKey()), true);
					});
					logger.log("duplicate: " + k);
					throw new RuntimeException("duplicate: " + k);
				} else {
					duplicatesChecker.add(k);
				}
			});
			duplicatesChecker.clear();
			System.gc();
		}
	}

}