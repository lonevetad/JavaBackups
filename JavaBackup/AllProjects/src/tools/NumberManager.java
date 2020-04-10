package tools;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.BiFunction;

import tools.Comparators;

public interface NumberManager<T> extends Serializable {
	public Comparator<T> getComparator();

	public BiFunction<T, T, T> getAdder();

	/** Returns a constant-like value representing the "zero" for accumulators. */
	public T getZeroValue();

	/**
	 * Provides a standardized way to convert, if possible, a value to a Double
	 * representation. In fact, it's a mapping.
	 */
	public default Double toDouble(T value) {
		return DoubleDistanceManager.ZERO;
	}

	public default T fromDouble(Double value) {
		return null;
	}

	//

	public static NumberManager<Integer> integerManager() {
		if (IntegerDistanceManager.singleton == null)
			IntegerDistanceManager.singleton = new IntegerDistanceManager();
		return IntegerDistanceManager.singleton;
	}

	public static NumberManager<Double> doubleManager() {
		if (DoubleDistanceManager.singleton == null)
			DoubleDistanceManager.singleton = new DoubleDistanceManager();
		return DoubleDistanceManager.singleton;
	}

	public static final class IntegerDistanceManager implements NumberManager<Integer> {
		private static final long serialVersionUID = 1L;
		static final Integer ZERO = 0;
		static final BiFunction<Integer, Integer, Integer> ADDER = (a, b) -> a + b;
		private static IntegerDistanceManager singleton = null;

		@Override
		public Comparator<Integer> getComparator() {
			return Comparators.INTEGER_COMPARATOR;
		}

		@Override
		public BiFunction<Integer, Integer, Integer> getAdder() {
			return ADDER;
		}

		@Override
		public Integer getZeroValue() {
			return ZERO;
		}

		@Override
		public Double toDouble(Integer value) {
			return value.doubleValue();
		}

		@Override
		public Integer fromDouble(Double value) {
			return value.intValue();
		}
	}

	public static final class DoubleDistanceManager implements NumberManager<Double> {
		private static final long serialVersionUID = 777L;
		static final Double ZERO = 0.0;
		static final BiFunction<Double, Double, Double> ADDER = (a, b) -> a + b;
		private static DoubleDistanceManager singleton = null;

		@Override
		public Comparator<Double> getComparator() {
			return Comparators.DOUBLE_COMPARATOR;
		}

		@Override
		public BiFunction<Double, Double, Double> getAdder() {
			return ADDER;
		}

		@Override
		public Double getZeroValue() {
			return ZERO;
		}

		@Override
		public Double toDouble(Double value) {
			return value;
		}

		@Override
		public Double fromDouble(Double value) {
			return value;
		}
	}
}