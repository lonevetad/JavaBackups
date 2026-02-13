package stuffs.shitter;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;

public class IntegerStream {
	public static final Comparator<Integer> INT_COMPARATOR = (e1, e2) -> {
		if (e1 == e2)
			return 0;
		if (e1 == null)
			return -1;
		if (e2 == null)
			return 1;
		return Integer.compare(e1, e2);
	};

	protected static enum UsedStatus {
		NotReady, ReadyToStart, Working, WorkFinished
	}

//	protected boolean canRun;
	protected int upperBound, index;
	protected UsedStatus usedStatus;
	protected MapTreeAVL<Integer, Integer> helper;
	protected long seed;
	protected Random random;

	/**
	 * Requires the (EXCLUDED) upper bound of indexes (the lower is
	 * <code>0</code>).<br>
	 * Calls {@link #IntegerStream(int, long)} providing
	 * <code>System.currentTimeMillis()</code> as long value.
	 */
	public IntegerStream(int upperBound) {
		this(upperBound, System.currentTimeMillis());
	}

	/**
	 * The first parameter is the (EXCLUDED) upper bound of indexes, as for
	 * {@link #IntegerStream(int)}, the second is the {@link Random}'s seed.
	 */
	public IntegerStream(int upperBound, long seed) {
		if (upperBound < 1)
			throw new IllegalArgumentException("The upper bound must be greater than zero");
		this.upperBound = upperBound;
//this.canRun=false;
		this.helper = null;
		this.seed = seed;
		this.usedStatus = UsedStatus.NotReady;
	}

	//

	public boolean canRun() {
		return this.helper == null;
	}

	/**
	 * Returns the (EXCLUDED) upper bound of indexes (the lower is <code>0</code>).
	 */
	public int getUpperBound() {
		return upperBound;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
		this.restart();
	}

	public void restart() {
		int n;
		Integer x;
		MapTreeAVL<Integer, Integer> h;
		if (this.usedStatus != UsedStatus.NotReady)
			return;
		this.index = 0;
		this.random = new Random(this.seed);
		h = this.helper = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, INT_COMPARATOR);
		n = this.upperBound;
		while (--n >= 0)
			h.put(x = n, x);
		this.usedStatus = UsedStatus.ReadyToStart;
	}

	protected void afterWork() {
		if (this.usedStatus != UsedStatus.WorkFinished)
			return;
		this.helper = null;
		this.random = null;
		this.index = 0;
		this.usedStatus = UsedStatus.NotReady;
	}

	/**
	 * Consume the randomized sequence of indexes, providing as first parameter the
	 * random index, as second parameter the sequential index, ranging from
	 * <code>0</code> to the upper bound (EXCLUDED) provided by
	 * {@link #getUpperBound()}.
	 *
	 * @param pairRandomAndSequentialIndexConsumer a consumer that consume a pair of
	 *                                             indexes: the first parameter is
	 *                                             the random index, the second
	 *                                             parameter is the sequential
	 *                                             index, ranging from
	 *                                             <code>0</code> to the upper bound
	 *                                             provided by
	 *                                             {@link #getUpperBound()}.
	 */
	public void forEach(BiConsumer<Integer, Integer> pairRandomAndSequentialIndexConsumer) {
		int size;
		MapTreeAVL<Integer, Integer> h;
		Random r;
		if (pairRandomAndSequentialIndexConsumer == null || this.usedStatus != UsedStatus.ReadyToStart)
			return;
		this.usedStatus = UsedStatus.Working;
		h = this.helper;
		r = this.random;

//		int count = h.size();
//		while ((size = h.size()) > 1) {
//			if (--count == 0)
//				break;
//			int i;
//			Integer x;
//			i = r.nextInt(size);
//			x = h.getAt(i).getKey();
////			System.out.println("size: " + size + ", i: " + i + ", x: " + x);
//			if (x == null) {
//				System.out.println("\n\n BUG\n");
//				System.out.println(h.toString());
//				this.usedStatus = UsedStatus.WorkFinished;
//				this.afterWork();
//				return;
//			}
//			pairRandomAndSequentialIndexConsumer.accept(h.remove(x), this.index++);
//			// if (i == 0)System.out.println("new min: " + h.peekMinimum());
//		}

		while ((size = h.size()) > 1)
			pairRandomAndSequentialIndexConsumer.accept(//
					h.remove(//
							h.getAt(r.nextInt(size)).getKey())//
					, this.index++);

//		System.out.println("now is left:\n" + h + "\n having minimum: " + h.peekMinimum().getKey());
		pairRandomAndSequentialIndexConsumer.accept(h.peekMinimum().getKey(), this.index++);
		this.usedStatus = UsedStatus.WorkFinished;
		this.afterWork();
	}

	public void forEach(Consumer<Integer> randomIndexConsumer) {
		if (randomIndexConsumer == null)
			return;
		this.forEach((ri, si) -> randomIndexConsumer.accept(ri));
	}

	public Iterator<Integer> iterator() {
		if (this.usedStatus != UsedStatus.ReadyToStart)
			return null;
		return new IteratorInteger();
	}

	//

	class IteratorInteger implements Iterator<Integer> {
		IteratorInteger() {
			usedStatus = UsedStatus.Working;
		}

		@Override
		public boolean hasNext() {
			return (usedStatus == UsedStatus.Working) && (!helper.isEmpty());
		}

		@Override
		public Integer next() {
			Integer x;
			if (helper.isEmpty())
				return null;
			x = helper.getAt(random.nextInt(helper.size())).getKey();
			helper.remove(x);
			if (helper.isEmpty())
				usedStatus = UsedStatus.WorkFinished;
			return x;
		}
	}
}