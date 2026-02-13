package aaut.test;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Arrays;

import aaut.nn.PolygonConvexAttentionNN;
import aaut.nn.impl.OutputToBoolean;
import aaut.nn.old.PolygonAttentionNN;
import aaut.tools.impl.MatrixInput2D;
import geometry.pointTools.PolygonUtilities;
import tools.MathUtilities;

public class TestPolygonNNAttention {

	static final int[][][] examples = new int[][][] {
			// esempio 1
			new int[][] { // in senso orario
					// xx
					new int[] { 50, 150, 150, 50 },
					// yy
					new int[] { 40, 40, 120, 120 }
			//
			}
			// esempio 2
			, new int[][] { // in senso orario
					// xx
					new int[] { 3, 8, 2, 5, 5, 7 },
					// yy
					new int[] { 3, 3, 8, 1, 6, 5 }
			//
			}

			// esempio 3
			, new int[][] { // in senso orario
					// xx
					new int[] { 10, 20, 25, 22, 18 },
					// yy
					new int[] { 10, 10, 15, 17, 16 }
			//
			}
			// esempio 4
			, new int[][] { // in senso orario
					// xx
					new int[] { 0, 8, 5, 3 },
					// yy
					new int[] { 0, 5, 5, 2 }
			//
			}
			// esempio 5
			, new int[][] { // in senso orario
					// xx
					new int[] { 1, 2, 2, 10 },
					// yy
					new int[] { 1, 2, 2, 2 }
			//
			}
			// esempio 6
			, new int[][] { // in senso orario
					// xx
					new int[] { 0, 10, 4, 8, 2 },
					// yy
					new int[] { 0, 0, 4, 6, 8 }
			//
			}
			// esempio 7
			, new int[][] { // in senso orario
					// xx
					new int[] { 1, 1, 2, 2 },
					// yy
					new int[] { 1, 2, 2, 1 }
			//
			} };
	static final double[][] pointsToTest = new double[][] {
			// x of points
			new double[] { 50, 55, 45, 0, 75, 444, -12, 99, -5000, 8888, 70, 90, 1, 3, 1.5, 2.7 }
			// y of points
			, new double[] { 50, 55, 45, 0, 60, 333, 100, -5, -5000, 8888, 90, 70, 2, 5, 0.5, 2 } //
	};
//	static final boolean[][] expectedResult = new boolean[][] {
//			// esempio 1
//			new boolean[] { true, true, false, false, true, false, false, false, false, false, true, true }//
//	};
	static final boolean[] EXPECTED_CONVEXITIES = { //
			true, false, true, true, true, false, true };

	static {
		boolean apiPoly, myPoly;
		double[] x_pointsToTest, y_pointsToTest;
		int i, numEx, numPoints;
		int[][] ex;
		Polygon poly;

		numEx = examples.length;
		x_pointsToTest = pointsToTest[0];
		y_pointsToTest = pointsToTest[1];
		numPoints = y_pointsToTest.length;
		i = -1;
		System.out.println("FIRST test my and api poly-contains algoritm");
		while (++i < numEx) {
			System.out.println("\n esempio n°: " + i);
			ex = examples[i];
//
			poly = new Polygon(ex[0], ex[1], ex[0].length);

			for (int k = 0; k < numPoints; k++) {
				apiPoly = poly.contains(x_pointsToTest[k], y_pointsToTest[k]);
				myPoly = PolygonUtilities.isPointInsidePolygon(x_pointsToTest[k], y_pointsToTest[k], poly);
				if (apiPoly != myPoly) {
					System.out.println("at " + k + ", differences: api: " + apiPoly + ", my: " + myPoly);
				}
			}
		}
	}

	public static void main(String[] args) {
		boolean expected;
		int i, numEx;
		boolean[] ret; // ,
		double[] x_pointsToTest, y_pointsToTest;
		int[][] ex;
		PolygonAttentionNN<MatrixInput2D, boolean[]> pann;
		PolygonConvexAttentionNN<MatrixInput2D, boolean[]> pannConvex;
		MatrixInput2D points;
		Polygon poly;
		OutputToBoolean outputCaster;
		System.out.println("START");
		i = -1;
		numEx = examples.length;

		System.out.println("POINTS:");
		System.out.println(Arrays.deepToString(pointsToTest));
		x_pointsToTest = pointsToTest[0];
		y_pointsToTest = pointsToTest[1];
		points = new MatrixInput2D(pointsToTest);
		points = points.traspose();
		System.out.println("POINTS AS INPUT ARE:");
		System.out.println(points);
		outputCaster = new OutputToBoolean();
		while (++i < numEx) {
			System.out.println("\n\n\n esempio n°: " + i);
			ex = examples[i];
//			expected = expectedResult[i];
			poly = new Polygon(ex[0], ex[1], ex[0].length);
			System.out.println("myPolygon:");
			System.out.println(PolygonUtilities.polygonToString(poly));
			System.out.println(
					"is it convex? " + PolygonUtilities.isConvex(poly) + ", expected: " + EXPECTED_CONVEXITIES[i]);
			System.out.println("\n\n");
			try {
				pann = new PolygonAttentionNN<>(poly);
				pann.setLayerOutputCaster(outputCaster);
//			for (double[][] pointsExample : pointsToTest) {}

				ret = pann.apply(points);
				System.out.println("resulting in: ");
				for (int k = 0; k < ret.length; k++) {
					expected = PolygonUtilities.isPointInsidePolygon(x_pointsToTest[k], y_pointsToTest[k], poly);
					System.out.println("\t - " + k + " : " + ret[k] + ", expected: " + expected + ",\t____ correct? "
							+ (ret[k] == expected));
				}
				System.out.println("------------------\n trying convex test");
				try {
					pannConvex = new PolygonConvexAttentionNN<>(poly);
					pannConvex.setLayerOutputCaster(outputCaster);
					ret = pannConvex.apply(points);
					System.out.println("resulting in: ");
					for (int k = 0; k < ret.length; k++) {
						expected = PolygonUtilities.isPointInsidePolygon(x_pointsToTest[k], y_pointsToTest[k], poly);
						System.out.println("\t - " + k + " : " + ret[k] + ", expected: " + expected
								+ ",\t____ correct? " + (ret[k] == expected) + ", over the point (x:  "
								+ x_pointsToTest[k] + ", y: " + y_pointsToTest[k] + ")");
					}
				} catch (IllegalArgumentException iaexc) {
					System.out.println("nope, not convex");
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		// nnr.setWeights(50, 40, 100, 80);

//		System.out.println("contains (120,105)? ");
//		System.out.println(nnr.contains(110, 105));
		System.out.println("END");
		Rectangle rect;
		rect = new Rectangle(1, 1, 1, 1);
		System.out.println(MathUtilities.isInside(rect, new Point(1, 2)));
		System.out.println(MathUtilities.isInside(rect, new Point(2, 1)));
	}
}