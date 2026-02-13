package aaut.tools.impl;

import java.io.Serializable;
import java.util.Arrays;

import aaut.tools.MatrixInput;

/**
 * Represents a N-dimensional matrix input implemented as a tree having each
 * node within <i>N-1</i> levels pointing to the next dimension and the
 * <i>N-th</I> level pointing to an array od <code>double</>.
 */
public class MatrixInputTree extends MatrixInput {
	private static final long serialVersionUID = 1L;

	public MatrixInputTree(int[] dimensions) {
		super(dimensions);
		build(dimensions);
	}

	protected NodeMatrixInput root;

	// building stuffs

	protected void build(int[] dimensions) {
		this.dimensionsSizes = dimensions;
		root = build(0, dimensions, dimensions.length - 1);
	}

	protected NodeMatrixInput build(int level, int[] d, int len_1) {
		boolean isPreLast;
		int i, dimensionLength;
		NodeMatrixInput n, children[];
		n = newNodeMatrixInput(isPreLast = (level == len_1), dimensionLength = d[level]);
		if (!isPreLast) {
			i = -1;
			children = n.subdimensions;
			level++;
			while (++i < dimensionLength)
				children[i] = build(level, d, len_1);
		}
		return n;
	}

	protected NodeMatrixInput newNodeMatrixInput(boolean isPreLastLevel, int dimensionLength) {
		return new NodeMatrixInput(isPreLastLevel, dimensionLength);
	}

	//

	//

	@Override
	public double getValueAt(int[] at) {
		GetterAtLastLevel gall;
		gall = new GetterAtLastLevel();
		doAtPreLastLevel(at, gall);
		return gall.val;
	}

	/**
	 * public double getValueAt(int[] at) {//<br>
	 * boolean iobe;//<br>
	 * int atIndex;//<br>
	 * Object o;//<br>
	 * double[] rSubset;//<br>
	 * NodeMatrixInput preLastLevel;//<br>
	 * o = getPreLastLevel(at);//<br>
	 * iobe = false;//<br>
	 * if (o instanceof Integer) {//<br>
	 * iobe = true;//<br>
	 * atIndex = ((Integer) o);//<br>
	 * }//<br>
	 * preLastLevel = (NodeMatrixInput) o; //<br>
	 * rSubset = preLastLevel.valuesLastDimension;//<br>
	 * atIndex = at[at.length - 1];//<br>
	 * if (rSubset.length >= atIndex) {//<br>
	 * iobe = true;//<br>
	 * atIndex = at.length - 1;//<br>
	 * }//<br>
	 * if (iobe)//<br>
	 * throw new IndexOutOfBoundsException("Out of bounds at dimension's index: " +
	 * atIndex);//<br>
	 * return rSubset[atIndex];//<br>
	 * }
	 */
	/***/

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void setValueAt(final int[] at, final double value) {
		doAtPreLastLevel(at, (vals, atIndex) -> vals[atIndex] = value);
	}

	@Override
	protected void manageSetValuesOfLastDimensions(final int[] at2, final double[] values) {
		NodeMatrixInput preLastLevel;
		Object o;
		o = getPreLastLevel(at2);
		if (o instanceof Integer)
			return;
		preLastLevel = (NodeMatrixInput) o;
		preLastLevel.setValuesLastDimension(values);
	}

	//

	/**
	 * Returns the {@link NodeMatrixInput} holding the last level specified by the
	 * path (so, ignoring the last array's value) or an {@link Integer} indicating
	 * the dimension's index that exceeded the maximum size.
	 */
	protected Object getPreLastLevel(int[] at) {
		int i, len_1, atIndex;
		NodeMatrixInput n, children[];
		i = -1;
		len_1 = this.dimensionsSizes.length - 1;
		n = root;
		while (++i < len_1) {
			// todo
			// n and i are at jet synchronized
			atIndex = at[i];
			children = n.subdimensions;
//			dimLen =
			if (atIndex >= children.length)
				return Integer.valueOf(i);
			n = children[atIndex]; // to next dimension
		}
		return n;
	}

	/** Similar to {@link #getPreLastLevel(int[])} about implementation. */
	protected void doAtPreLastLevel(int[] at, ConsumerLastLevel consumer) {
		boolean noError;
		int i, len_1, atIndex;
		NodeMatrixInput n, children[];
		double[] rSubset;
		len_1 = this.dimensionsSizes.length - 1;
		rSubset = null;
		atIndex = -1;
		if (len_1 >= 0) {
			i = -1;
			noError = true;
			n = root;
			while (noError && ++i < len_1) {
				// todo
				// n and i are at jet synchronized
				atIndex = at[i];
				children = n.subdimensions;
//			dimLen =
				if (atIndex >= children.length)
					noError = false;
				else
					n = children[atIndex]; // to next dimension
			}
			if (noError && ((atIndex = at[len_1]) >= (rSubset = n.valuesLastDimension).length))
				noError = false;
			if (!noError)
				throw new IndexOutOfBoundsException(
						"Out of bounds at dimension's index: " + i + ", path: " + Arrays.toString(at));
		} // else return;
		consumer.apply(rSubset, atIndex); // done
	}

	@Override
	public void forEach(ValuePathConsumer dc) {
		int[] at;
		at = new int[this.dimensionsSizes.length];
		for (int i = 0; i < at.length; i++)
			at[i] = 0;
		this.forEach(dc, root, at, 0);
	}

	protected void forEach(ValuePathConsumer dc, NodeMatrixInput n, int[] at, int level) {
		int i, len, nextLevel;
		if (n.subdimensions != null) {
			NodeMatrixInput[] children;
			nextLevel = 1 + level;
			at[level] = 0;
			children = n.subdimensions;
			len = children.length;
			i = -1;
			while (++i < len) {
				forEach(dc, children[i], at, nextLevel);
				at[level]++;
			}
		} else {
			int lastIndex;
			double[] values;
			values = n.valuesLastDimension;
			len = values.length;
			i = -1;
			at[lastIndex = at.length - 1] = 0;
			while (++i < len) {
				dc.apply(values[i], at);
				at[lastIndex]++;
			}
		}
	}

	//

	//

	protected static interface ConsumerLastLevel { // ConsumerPreLastLevel
//		public void apply(NodeMatrixInput preLastLevel, int indexOnLastLevel);
		public void apply(double[] lastLevel, int indexOnLastLevel);
	}

	protected static class GetterAtLastLevel implements ConsumerLastLevel {
		double val;

		@Override
		public void apply(double[] lastLevel, int indexOnLastLevel) {
			val = lastLevel[indexOnLastLevel];
		}
	}

	protected class NodeMatrixInput implements Serializable {
		private static final long serialVersionUID = 504254308631534L;
		// informations not-needed: save some space
//		int level, indexOnFatherDimension;
//		NodeMatrixInput father;
		private NodeMatrixInput[] subdimensions;
		protected double[] valuesLastDimension;

		protected NodeMatrixInput(boolean isPreLastLevel, int dimensionLength) {
			if (isPreLastLevel) {
				subdimensions = null;
				valuesLastDimension = new double[dimensionLength];
			} else {
				subdimensions = new NodeMatrixInput[dimensionLength];
				valuesLastDimension = null;
			}
		}

		protected NodeMatrixInput[] getSubdimensions() {
			return subdimensions;
		}

		public NodeMatrixInput getSubdimensionAt(int index) {
			return subdimensions[index];
		}

		public double[] getValuesLastDimension() {
			return valuesLastDimension;
		}

		public void setValuesLastDimension(double[] valuesLastDimension) {
			if (valuesLastDimension == null)
				return;
			this.valuesLastDimension = Arrays.copyOf(valuesLastDimension, valuesLastDimension.length);
		}

	}

	@Override
	public MatrixInput multiply(MatrixInput nnmi) {
		throw new UnsupportedOperationException("Cannot mutiply. For 2D matrix, use the MatrixInput2D_Tree class.");
	}
}