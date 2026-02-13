package aaut.nn.old;

import java.awt.Polygon;
import java.util.Arrays;
import java.util.function.Function;

import aaut.nn.PerceptronArray;
import aaut.tools.ActivationFunction;
import aaut.tools.MatrixInput;
import aaut.tools.impl.MatrixInput2D;
import geometry.pointTools.PolygonUtilities;
import tools.MathUtilities;

/** Would be made abstract to forbid to instantiate */
public class PolygonAttentionNN<NNMI_Input extends MatrixInput, E> extends PerceptronArray<NNMI_Input, E> {

	//

	protected ActivationFunction activationFunction;
	protected Function<MatrixInput, E> layerOutputCaster;
	protected MatrixInput2D polygonWeightsTrasposed, secondLayerDecider; // secondLayerDecider size: {h: P; w: 1}
//	protected final double[] rowDecider;

	public PolygonAttentionNN(Polygon poly) {
		super();
		int i;
		double[] row, mat[];
		MatrixInput2D secondLayerTemp;
		this.activationFunction = ActivationFunction.DefaultActivationFunctions.POSITIVE_STEP;
		polygonWeightsTrasposed = build(poly); // yet trasposed

//		System.out.println("\n<<<PRINTING WEIGHTS:");
//		System.out.println(polygonWeightsTrasposed);

		row = new double[poly.npoints + 1];
		mat = new double[][] { row };
		secondLayerTemp = (new MatrixInput2D(mat));
		row[0] = -((i = row.length - 1) - 0.5);
		while (i > 0)
			row[i--] = 1;
		secondLayerDecider = secondLayerTemp.traspose();
//		System.out.println("second layer decider: " + Arrays.toString(row));
	}

	@Override
	public Function<MatrixInput, E> getLayerOutputCaster() { return layerOutputCaster; }

	public ActivationFunction getActivationFunction() { return activationFunction; }

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	public void setLayerOutputCaster(Function<MatrixInput, E> layerOutputCaster) {
		this.layerOutputCaster = layerOutputCaster;
	}

	/***
	 * {@inheritDoc}
	 * <p>
	 * Compute the attention task over a matrix of size <code>Nx2</code>
	 * (<code>N</code> is the height, or the rows, and <code>2</code> is the width,
	 * or the columns), that is an array of <code>N</code> 2D-points.<br>
	 * This produces a vertical array of <code>double</code> with the same size
	 * (height) of <code>N</code>, that is provided to the {@link Function} returned
	 * by {@link #getLayerOutputCaster()}.
	 *
	 * @returns the computation of the application of the {@link Function} returned
	 *          by {@link #getLayerOutputCaster()} to a vertical array of
	 *          <code>double</code> with the same height of this funciont's input,
	 *          that is <code>N</code>,
	 **/
	@Override
	public E apply(NNMI_Input t) { // input's size is {h: N; w: 2}
		int i, numPointToClassify, numPolygonSide;
		MatrixInput2D pw, input;
		double[] rowInput, newRow; // neuronsInputSecondLayer;
		double[][] pointWithBiasMatrix, inputMatrix;
		pointWithBiasMatrix = new double[i = numPointToClassify = t.getDimensionAriety(0)][3];
		if (t instanceof MatrixInput2D) {
			inputMatrix = ((MatrixInput2D) t).getMatrix();
			while (--i >= 0) {
				rowInput = pointWithBiasMatrix[i];
				newRow = inputMatrix[i];
				rowInput[0] = 1;
				rowInput[1] = newRow[0];
				rowInput[2] = newRow[1];
			}
		} else {
			int[] at;
			at = new int[2];
			while (--i >= 0) {
				rowInput = pointWithBiasMatrix[i];
				rowInput[0] = 1;
				at[0] = i;
				at[1] = 0;
				rowInput[1] = t.getValueAt(at);
				at[1] = 1;
				rowInput[2] = t.getValueAt(at);
			}
		}
		input = (new MatrixInput2D(pointWithBiasMatrix)); // .traspose();
//		System.out.println("\n\n BIASED INPUT:");
//		System.out.println(input);
		// input's size is now {h: N; w: 3}
		pw = this.polygonWeightsTrasposed; // pw's size is {h: 3; w: P} , P = polygon's sides count
		numPolygonSide = pw.getDimensionAriety(1);
		// input will now feed the decider level
//		System.out.println("\nINPUT size: (h: " + input.getDimensionAriety(0)//
//				+ "; w: " + input.getDimensionAriety(1));
//		System.out.println("PW size: (h: " + pw.getDimensionAriety(0) //
//				+ "; w: " + pw.getDimensionAriety(1));
//		input = (MatrixInput2D) pw.multiply(input); // due to pw's and input's dimension, revert the operation
		input = (MatrixInput2D) input.multiply(pw);
//		System.out.println("is null? " + (input == null));
//		System.out.println("input IS:");
//		System.out.println(input);
		input.apply(//
				this.getActivationFunction());
//		System.out.println("input IS became:");
//		System.out.println(input);
		// input's size is {h: N; w: P}, with P classifier's judments
		// to sum them up, must add biases to all row -> increase "w" by one
		pointWithBiasMatrix = new double[numPointToClassify][++numPolygonSide];
		inputMatrix = input.getMatrix();
		for (int r = 0; r < numPointToClassify; r++) {
			rowInput = inputMatrix[r];
			newRow = pointWithBiasMatrix[r];
			newRow[0] = 1;
			i = numPolygonSide;
			while (--i > 0) {
				newRow[i] = rowInput[i - 1];// input.getValueAt(at)
			}
		}
		input = (new MatrixInput2D(pointWithBiasMatrix));

//		System.out.println("\n NEW input to feed the second layer");
//		System.out.println(input);
//		System.out.println("-----------");
		// input's size is now {h: N; w: P+1}
		pw = (MatrixInput2D) input.multiply(secondLayerDecider);
//		System.out.println("\n OUTPUT OF SECOND LAYER IS:");
//		System.out.println(pw.toString());
		// secondLayerDecider's size: {h: P; w: 1}
		// -> pw's size: {h: N; w: 1}
		pw.apply(this.getActivationFunction());
		return this.getLayerOutputCaster().apply(pw);
	}

	//

	// TODO BUILDING

	protected MatrixInput2D build(Polygon poly) {
		int i, np;
		int[] xx, yy;
		MatrixInput2D pwm;
		double[][] polyWeigths;
		SideInfo firstFound, allPointsInfo[], tempSI, toBeClassifiedSI;
		if (poly == null || (np = poly.npoints) < 2)
			throw new IllegalArgumentException("polygon null or with too less points");
		xx = poly.xpoints;
		yy = poly.ypoints;
//		if(np==3) {
//
//		}
		polyWeigths = new double[np][3]; // 3 = {bias, x, y}
		this.polygonWeightsTrasposed = pwm = new MatrixInput2D(polyWeigths);
		allPointsInfo = new SideInfo[np];
		// look for the first useful corner
		firstFound = tempSI = null;
		i = -1;
		while (firstFound == null && ++i < np) {
			allPointsInfo[i] = tempSI = createInfo(i, polyWeigths[i], allPointsInfo, xx, yy, poly, false);
			if (tempSI.classification == SideClassification.Positive
					|| tempSI.classification == SideClassification.Negative)
				firstFound = tempSI;
		}
		if (i >= np) {
//			System.out.println("NOTHING FOUND");
			return pwm.traspose();// no one found _> keep matrixas is
		}
//		System.out.println("\n\n before do-while, the allInfo is: ");
//		System.out.println(Arrays.toString(allPointsInfo));
//		System.out.println("entering the do-while");
		// tempSI is the first usefully classified side, nextSI is the side to be
		// classified
		do {
			if (++i >= np)
				i = 0;
			toBeClassifiedSI = allPointsInfo[i]; // get the line to be classified
			if (toBeClassifiedSI == null)
				allPointsInfo[i] = toBeClassifiedSI = createInfo(i, polyWeigths[i], allPointsInfo, xx, yy, poly, true);
//			System.out.println("\n\n toBeClassifiedSI:" + toBeClassifiedSI);
			if (toBeClassifiedSI.classification == null
					|| toBeClassifiedSI.classification == SideClassification.Uncertain) {
				propagateClassification(tempSI, toBeClassifiedSI, xx, yy);
			}
			// move
			tempSI = toBeClassifiedSI;
		} while (firstFound != tempSI);
//		System.out.println("\n\n AFTER do-while, the allInfo is: ");
//		System.out.println(Arrays.toString(allPointsInfo));
		return pwm.traspose();
	}

	protected static void propagateClassification(SideInfo alreadyClassifiedSI, SideInfo toBeClassifiedSI, int[] xx,
			int[] yy) {
		boolean classify1By2, classify2By1;
		int indexPointLineTBC;
		double row[]; // pc,
//		classify1By2 = evaluatePoint(alreadyClassifiedSI.x, alreadyClassifiedSI.y, row = toBeClassifiedSI.row) >= 0;
		classify1By2 = evaluatePoint(xx[alreadyClassifiedSI.index], yy[alreadyClassifiedSI.index],
				row = toBeClassifiedSI.row) >= 0;
//		System.out.println("1by2 .. alreadyCl-p: (x: " + alreadyClassifiedSI.x + ", y: " + alreadyClassifiedSI.y
//				+ "), .." + "toBeClassifiedSI.row: " + Arrays.toString(row));
//		classify2By1 = evaluatePoint(toBeClassifiedSI.x, toBeClassifiedSI.y, alreadyClassifiedSI.row) >= 0;
		indexPointLineTBC = toBeClassifiedSI.index + 1;
		if (indexPointLineTBC >= xx.length)// it's circular
			indexPointLineTBC -= xx.length;
		classify2By1 = evaluatePoint(xx[indexPointLineTBC], yy[indexPointLineTBC], alreadyClassifiedSI.row) >= 0;
//		System.out.println("2by1 .. toBeClassifiedSI-p: (x: " + xx[indexPointLineTBC] // toBeClassifiedSI.x
//				+ ", y: " + yy[indexPointLineTBC] // toBeClassifiedSI.y
//				+ "), .." + "alreadyClassifiedSI.row: " + Arrays.toString(alreadyClassifiedSI.row));

		if (classify1By2 ^ classify2By1) { // classify1By2 != classify2By1
			// all weights are built as "positive", so as for nextSI
			// all cases collapse to this code
//			System.out.println("......toBeClassifiedSI in propag is XORing: .. classify1By2: " + classify1By2
//					+ ", classify2By1: " + classify2By1);
			// successive sides must have
//			toBeClassifiedSI.positivenessCoefficient = pc = -alreadyClassifiedSI.positivenessCoefficient;
//			toBeClassifiedSI.classification = alreadyClassifiedSI.classification == SideClassification.Positive
//					? SideClassification.Negative
//					: SideClassification.Positive;
			toBeClassifiedSI.positivenessCoefficient = -1;
			toBeClassifiedSI.classification = SideClassification.Negative;
//			if (pc < 0) {
			row[0] = -row[0];
			row[1] = -row[1];
			row[2] = -row[2];
//			}
		} else {
			boolean oracleJudgement;
//			toBeClassifiedSI.classification = SideClassification.Positive;
//			toBeClassifiedSI.coefficAngolare = 1;

			toBeClassifiedSI.classification = classify1By2 ? SideClassification.Positive : SideClassification.Negative;
			toBeClassifiedSI.coefficAngolare = classify1By2 ? 1 : -1;
		}
//		System.out.println("--toBeClassifiedSI result, after propagation:");
//		System.out.println("--\t" + toBeClassifiedSI);
	}

	protected static SideInfo createInfo(int index, double[] row, SideInfo[] allPoints, int[] xx, int[] yy,
			Polygon poly, boolean onlyCreateWeight) {
		boolean isP1Inside, isP2Inside;
		int thisx, thisy, nextx, nexty;
		double y_coeff, m_negated, q_negated, radBisector, p1x, p1y, p2x, p2y, sin2, cos2; // y = m*x + q
		SideInfo si, siNext;

		si = allPoints[index] != null ? allPoints[index] : new SideInfo(index, row);
		si.x = //
				thisx = xx[index];
		si.y = //
				thisy = yy[index];
		if (++index >= xx.length)
			index = 0;
		nextx = xx[index];
		nexty = yy[index];

		if (thisx == nextx && thisy == nexty)
			throw new IllegalArgumentException("Cannot build the Net: degenerate point at: " + index);
		if (thisx == nextx) {
			row[0] = q_negated = -thisx;
			row[1] = m_negated = 1;
			row[2] = y_coeff = 0;
			m_negated = Double.POSITIVE_INFINITY;
		} else if (thisy == nexty) {
			row[0] = q_negated = -thisy;
			row[1] = m_negated = 0;
			row[2] = y_coeff = 1;
		} else {
			int divisor, dx, dy;
			dx = thisx - nextx;
			dy = thisy - nexty;
			if (dx < 0) {
				dx = -dx;
				dy = -dy;
			}
			divisor = MathUtilities.mcd(dx, dy < 0 ? -dy : dy);
			if (divisor != 1) {
				dx /= divisor;
				dy /= divisor;
			}
//			//  y - mx - c = 0 -> c = y-mx
//			m = -MathUtilities.slope(thisx, thisy, nextx, nexty); // beware of the minus on "m"
//			row[0] = q = thisy + m * thisx;
//			row[1] = m;
//			row[2] = y_coeff = 1;
			m_negated = -dy;
			y_coeff = dx;
			row[0] = q_negated = (thisy * y_coeff + m_negated * thisx); // m is yet negated
			row[1] = m_negated;
			row[2] = y_coeff;
		}
		si.coefficAngolare = m_negated;
		if (onlyCreateWeight)
			return si;

		if (++index >= xx.length)
			index = 0;
		if ((siNext = allPoints[index]) == null)
			radBisector = ((Math.atan(m_negated) + Math.atan(MathUtilities.slope(nextx, nexty, xx[index], yy[index])))
					/ 2.0);
		else
			radBisector = ((Math.atan(m_negated) + Math.atan(siNext.coefficAngolare)) / 2.0);

		sin2 = Math.sin(radBisector) * 2.0;
		cos2 = Math.cos(radBisector) * 2.0;
		si.p1x = p1x = nextx + cos2;
		si.p1y = p1y = nexty - sin2;
		si.p2x = p2x = nextx - cos2;
		si.p2y = p2y = nexty + sin2;
		si.isP1Inside = isP1Inside = PolygonUtilities.isInside(p1x, p1y, poly);
		si.isP2Inside = isP2Inside = PolygonUtilities.isInside(p2x, p2y, poly);

		if (isP1Inside == isP2Inside) {
			si.classification = SideClassification.Uncertain;
		} else {
			// classify the points
			// [wbias, wc, wy]T*[1,px,py] < 0 -> positivenessCoefficient = "-1"
			// recycle radBisector as a temp variable for evaluatePoint call
			if (isP1Inside) {
				radBisector = q_negated + m_negated * p1x + y_coeff * p1y;
			} else {
				radBisector = q_negated + m_negated * p2x + y_coeff * p2y;
			}
			if (radBisector >= 0) {
				si.positivenessCoefficient = 1;
				si.classification = SideClassification.Positive;
			} else {
				si.positivenessCoefficient = -1;
				si.classification = SideClassification.Negative;
				// swap the sign
				row[0] = -row[0];
				row[1] = -row[1];
				row[2] = -row[2];
			}
		}
		return si;
	}

	protected static double evaluatePoint(double px, double py, double[] weights) {
		return weights[0] + weights[1] * px + weights[2] * py;
	}

	//

	// TODO CLASSES

	static enum SideClassification {
		Positive, Negative, Uncertain
	}

	/** Represent a line starting from this point and ending to the next ones */
	static class SideInfo {
		boolean isP1Inside, isP2Inside;
//		boolean isFirstPointClassifiedWithNextSide = false;
		int index, x, y; // x,y are the starting point of this side ; //
		// ,
		double coefficAngolare, positivenessCoefficient, // = {1.0, -1.0};
				p1x, p1y, p2x, p2y;
		double[] row;
		SideClassification classification;
//		Point2D.Double p_1, p_2;

		/*
		 * valore che ha senso essere considerato se e solo se positivenessCoefficient
		 * è "Positive" o "Negative"
		 */
		SideInfo(int i, double[] row) {
			this.index = i;
			this.row = row;
			classification = null;
		}

		@Override
		public String toString() {
			return "\nSideInfo [x=" + x + ", y=" + y + //
					",isP1Inside=" + isP1Inside + ", isP2Inside=" + isP2Inside + ", index=" + index + ", C_A="
					+ coefficAngolare + ", P_C=" + positivenessCoefficient + ", p1x=" + p1x + ", p1y=" + p1y + ", p2x="
					+ p2x + ", p2y=" + p2y + ", row=" + Arrays.toString(row) + ", class_f=" + classification + "]";
		}
	}
}