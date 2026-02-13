package aaut.tools;

import java.io.Serializable;
import java.util.Arrays;

public abstract class MatrixInput implements Serializable {
	private static final long serialVersionUID = 54035406483L;

	public static interface ValuePathConsumer extends Serializable {
		public static final ValuePathConsumer PRINTER = (d, at) -> //
		System.out.println("- val: " + d + ", at: " + Arrays.toString(at));

		public void apply(double d, int[] path);
	}

	//

	public MatrixInput(int[] dimensions) {
		if (dimensions == null || dimensions.length == 0)
			throw new IllegalArgumentException("Not a 2D matrix. Dimensions given: " + Arrays.toString(dimensions));
		this.dimensionsSizes = dimensions;
	}

	/**
	 * For instance, a rectangle of <i>5x6</i> is a array like
	 * <code>int[] d = {5, 6};</code>
	 */
	protected int[] dimensionsSizes;

	//

	/** Returns the amount of dimensions that this matrix is holding. */
	public int getDimensionsCount() {
		return dimensionsSizes.length;
	}

	/**
	 * Supposing that each dimension's domain is a finite space (having at most
	 * {@link Integer#MAX_VALUE} elements), returns the dimension's domain's ariety
	 * of the dimension of the specified index (the index is the given parameter).
	 *
	 * @throws IndexOutOfBoundsException
	 */
	public int getDimensionAriety(int indexDimension) {
		return dimensionsSizes[indexDimension];
	}

	/**
	 * Read the cell's value located at the given path, described as an index. <br>
	 * Lower array's index, outer dimensions. It follows tha only the deepest/last
	 * dimension truly holds the values (as for usual matrices in programming).
	 */
	public abstract double getValueAt(int[] at);

	/**
	 * Set the value of a single cell. For the first parameter's documentation, see
	 * {@link #getValueAt(int[])}.
	 */
	public abstract void setValueAt(int[] at, double value);

	/**
	 * Similar to {@link #setValueAt(int[],double)}, but performs the settings of
	 * the last dimension hopefully in an optimised way. NOTE: the given path must
	 * have a length, in terms of array's length, less by one than
	 * {@link #getDimensionsCount()}. or it will throws an
	 * {@link #IllegalArgumentException}.
	 *
	 * @throws IllegalArgumentException
	 */
	public void setValuesOfLastDimensionsAt(int[] at, final double[] values) {
		int i, dc, atlen, lastDimensionAriety;
		int[] at2;
		dc = this.getDimensionsCount();
		atlen = at.length;
		if (atlen != dc - 1)
			throw new IllegalArgumentException("Illegal path: " + Arrays.toString(at));
		lastDimensionAriety = this.getDimensionAriety(atlen);
		if (lastDimensionAriety != values.length)
			throw new IllegalArgumentException("Illegal values:\n\t- required length: " + lastDimensionAriety
					+ "\n\t- given values: " + Arrays.toString(values));
		// do the copy
		at2 = new int[dc];
		at2[i = atlen] = 0;
		while (--i >= 0)
			at2[i] = at[i];
		at = null;
		// then, do it
		manageSetValuesOfLastDimensions(at2, values);
	}

	protected void manageSetValuesOfLastDimensions(final int[] at2, final double[] values) {
		int i, lastDimensionAriety, lastIndex;
		i = -1;
		lastIndex = at2.length - 1;
		lastDimensionAriety = values.length;
		while (++i < lastDimensionAriety) {
			at2[lastIndex] = i;
			setValueAt(at2, values[i]);
		}
	}

	public abstract MatrixInput multiply(MatrixInput nnmi);
//		public abstract MatrixInput multiply(MatrixInput nnmi) {
//				MatrixInput ret;
//	}

	public abstract void forEach(ValuePathConsumer dc);

	public abstract void apply(ActivationFunction af);
}