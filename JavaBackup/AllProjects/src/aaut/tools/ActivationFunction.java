package aaut.tools;

import java.util.Objects;

public interface ActivationFunction {
	static final ActivationFunction _SIGMOID = x -> (1.0 / (1.0 + Math.exp(-x)));

	public double apply(double x);

	/** That's the same of {@link #inverseComposition(ActivationFunction)}. */
	public default ActivationFunction andThen(ActivationFunction af) {
		return inverseComposition(af);
	}

	/**
	 * Compose the given function with <code>this</code>, so the given function is
	 * the last to be called.<br>
	 * It's equal to <code>parameter(this(x))</code>
	 */
	public default ActivationFunction inverseComposition(ActivationFunction af) {
		Objects.requireNonNull(af);
		return x -> {
			return af.apply(this.apply(x));
		};
	}

	/**
	 * Compose <code>this</code> with the given function, that is the first to be
	 * called.<br>
	 * It's equal to <code>this(af(x))</code>
	 */
	public default ActivationFunction composition(ActivationFunction af) {
		Objects.requireNonNull(af);
		return x -> {
			return this.apply(af.apply(x));
		};
	}

	public default double power(int amoutOfTimes, double x) {
		while (amoutOfTimes-- > 0)
			x = this.apply(x);
		return x;
	}

//

	public static enum DefaultActivationFunctions implements ActivationFunction {
		IDENTITY(x -> x), //
		STEP(x -> (x > 0 ? 1 : -1)), POSITIVE_STEP(x -> (x >= 0 ? 1 : -1)), //
		RELU(x -> (x > 0 ? x : 0)), //
		GAUSSIAN(x -> Math.exp(-(x * x))), //
		GAUSSIAN_UNARY_VARIANCE(x -> Math.exp(-((x * x) / 2.0))), //
		SOFT_PLUS(x -> Math.log(1 + Math.exp(x))), //
		TANH(x -> ((2.0 * _SIGMOID.apply(2.0 * x)) - 1.0)), ARC_TAN(x -> Math.atan(x)), //
		SIGMOID(_SIGMOID), SIGMOID_DERIVATIVE(x -> (x * (1.0 - x))), //
		SINUSOID(x -> Math.sin(x)), COSINUSOID(x -> Math.cos(x)), //
		AR_SIN_H(x -> Math.log(x + Math.hypot(x, 1.0))), //
		SINC(x -> x == 0.0 ? 1.0 : (Math.sin(x) / x)), COSC(x -> x == 0.0 ? -1.0 : (Math.cos(x) / x)), //
		SQ_RBFKernel(x -> {
			if (x > 2.0 || x < -2.0)
				return 0.0;
			if (x <= 1.0 && x >= -1.0)
				return 1.0 - ((x * x) / 2.0);
			return 2.0 - ((2.0 - x * x) / 2.0);
		}), //
		ELLIOT_SIGN(x -> x / (1 + Math.abs(x))), //
		BENT_IDENTITY(x -> x + ((Math.hypot(x, 1.0) - 1.0) / 2.0)) //
		;

		DefaultActivationFunctions(ActivationFunction af) {
			ActivationFunction.af = af;
		}

		final ActivationFunction af;

		@Override
		public double apply(double x) {
			return af.apply(x);
		}
	}
}