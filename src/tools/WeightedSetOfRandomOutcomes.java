package tools;

import java.util.Arrays;
import java.util.Random;
import java.util.function.BiConsumer;

/**
 * Define a set of values each having its own probability to randomly come out
 * and each of them is identified by a simple integer index.. <br>
 * In order to define that probability, each value has a <i>weight</i>. These
 * values can be defined as an array of positive integers {@code int[]} or as an
 * array of {@link ObjWithRarityWeight}. Each negative weight is reduced to 0
 * <p>
 * Extract randomly an index (from 0 to <code>length -1</code>) based on those
 * indexes' weights.
 * <p>
 * Uses {@link ObjWithRarityWeight}.
 */
public class WeightedSetOfRandomOutcomes {

	//

	public static interface ConsumerIndexWeight {
		/** The first parameter is the index, the second is the weight. */
		public void applyIndexWeight(int index, int weight);
	}

	//

	/**
	 *
	 * @param indexesWeights weights of each indexes
	 */
	public WeightedSetOfRandomOutcomes(int[] indexesWeights, Random r) {
		if (r == null)
			throw new IllegalArgumentException("Null Random instance");
		this.rng = r;
		this.objWRW = null;
		setValues(indexesWeights);
	}

	/**
	 *
	 * @param indexesWeights weights of each indexes
	 */
	public WeightedSetOfRandomOutcomes(int[] indexesWeights) { this(indexesWeights, new Random()); }

	/**
	 *
	 * @param indexesWeights weights of each indexes
	 */
	public WeightedSetOfRandomOutcomes(int[] indexesWeights, long seed) { this(indexesWeights, new Random(seed)); }

	//

	public WeightedSetOfRandomOutcomes(ObjWithRarityWeight[] values, Random r) {
		if (r == null)
			throw new IllegalArgumentException("Null Random instance");
		this.rng = r;
		setValues(values);
	}

	public WeightedSetOfRandomOutcomes(ObjWithRarityWeight[] values) { this(values, new Random()); }

	public WeightedSetOfRandomOutcomes(ObjWithRarityWeight[] values, long seed) { this(values, new Random(seed)); }

	//

	protected long sumAllWeights;
	protected int[] weights;
	/**
	 * Partial and sequential sum of the weights before and included the "current"
	 * one (the current index).
	 */
	protected long[] partialSequentialSumWeights;
	protected transient Random rng;
	protected ObjWithRarityWeight[] objWRW = null;

	//

	protected void setValues(ObjWithRarityWeight[] v) {
		int len;
		int[] vv;
		vv = new int[len = v.length];
		while (--len >= 0) {
			vv[len] = v[len].getRarityWeight();
		}
		this.setValues(vv);
	}

	protected void setValues(int[] v) {
		int i, n, val;
		long s;
		long[] sv;
		if (v == null || (n = v.length) < 2)
			throw new IllegalArgumentException("arrays of values is null or with few elemens");
		i = -1;
		s = 0;
		sv = new long[n];
		while (++i < n) {
			if ((val = v[i]) <= 0)
				throw new IllegalArgumentException("Values must be strictly positive, error at index: " + i);
			s += val;
			sv[i] = s;
		}
		if (s < 0) { throw new IllegalStateException("Can't have a total sum of weights lesser than zero: " + s); }
		this.weights = new int[n];
		System.arraycopy(v, 0, this.weights, 0, n);
		this.sumAllWeights = s;
		this.partialSequentialSumWeights = sv;
	}

	public int getWeight(int index) { return this.weights[index]; }

	/** Returns the amount of indexes managed by this instance. */
	public int getIndexesAmount() {
		return this.weights.length;
	}

	/** Returns <code>{@link #getIndexesAmount()} - 1</code>. */
	public int getMaxIndex() {
		return this.weights.length - 1;
	}

	/**
	 * WARNINGS: To modify ALL values, just create a new instance: this method is
	 * <code>O(n)</code>.
	 */
	public void setWeight(int index, int weight) {
		int oldval, n;
		long newSum;
		int[] vals;
		long[] sv;
		if (weight <= 0)
			throw new IllegalArgumentException("Values must be strictly positive: " + weight);
		vals = this.weights; // cache
		oldval = vals[index];
		this.sumAllWeights -= oldval;
		vals[index] = weight;
		newSum = index == 0 ? 0 : vals[--index]; // "--" to use the "++index"
		// update the summed values
		n = vals.length;
		sv = this.partialSequentialSumWeights; // cache
		while (++index < n) {
			sv[index] = (newSum += vals[index]);
		}
//		this.sumValues = --sv[n - 1]; // the max cap is excluded
	}

	/** Alias for the less clear {@link #next()}. */
	public int nextIndex() { return next(); }

	/**
	 * Returns a random index, which probability depends on the already given
	 * weights.
	 */
	public int next() {
		boolean isLess;
		int i, j, mid;
		long v;
		long[] vals;
		v = sumAllWeights;
		if (v < Integer.MAX_VALUE)
			v = rng.nextInt((int) v);
		else {
			v = rng.nextLong();
			if (v > 0)
				v %= sumAllWeights;
			else if (v < 0)
				v = (v % sumAllWeights) + sumAllWeights;
		}
//		System.out.print(" (v:" + v + ") ");
//		System.out.println("\t\t(v:" + v + ") ");
		j = (vals = this.partialSequentialSumWeights).length - 1;
		i = 0;
		while (i < j) {
			mid = (i + j) >> 1;
//			System.out.println("\t\t\t(i:" + i + ", mid:" + mid + ", j:" + j + ")");
			if (mid == 0)
				return 0;
//		else if(mid==(v.length-1)) // will be prevented by while's condition
			isLess = v < vals[mid];
			if (vals[mid - 1] <= v && isLess)
				return mid;
			if (isLess)
				j = mid;
			else {
				if (i == mid)
					i++;
				else
					i = mid;
			}
			/*
			 * else else if (vals[mid - 1] <= v && v < vals[mid]) { return mid; } else { j =
			 * mid; }
			 */

		}
		return i;
	}

	public void forEachIndexAndWeight(BiConsumer<Integer, Integer> consumer) {
		forEachIndexAndWeight(new ConsumerIndexWeight() {
			@Override
			public void applyIndexWeight(int index, int weight) { consumer.accept(index, weight); }
		});
	}

	public void forEachIndexAndWeight(ConsumerIndexWeight consumer) {
		int i, len;
		int ww[];
		i = -1;
		len = (ww = this.weights).length;
		while (++i < len) {
			consumer.applyIndexWeight(i, ww[i]);
		}
	}

	@Override
	public String toString() { return "RandomWeightedIndexes [weights=" + Arrays.toString(weights) + "]"; }

	//

	public static void main(String[] args) {
		int i;
		int[] indexes;
		int[][] testValyes;
		WeightedSetOfRandomOutcomes rw;
		testValyes = new int[][] { //
				new int[] { 12, 7, 20 }, // , 4, 8, 32, 3
				new int[] { 12, 7, 20, 4, 8, 32, 3 }, //
				new int[] { 224, 112, 56, 28, 14, 7 }//
		};

		for (int[] vals : testValyes) {
			rw = new WeightedSetOfRandomOutcomes(vals);
			indexes = new int[vals.length];
			System.out.println(Arrays.toString(vals));
			for (i = 0; i < vals.length; i++)
				indexes[i] = 0;
			for (int r = 1000; r > 0; r--) {
//			System.out.print(r + " -> ");
				i = rw.next();
//			System.out.println("\t" + i);
				indexes[i]++;
			}
			System.out.println(Arrays.toString(rw.partialSequentialSumWeights));
			System.out.println(Arrays.toString(indexes));
			System.out.println("\n\n");
		}
	}
}