package dataStructures.quadTree;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.function.IntFunction;

/***
 * Class made for double 2-D points (with the z-axes as additional data /
 * payload) designed for Matlab usage
 * 
 * @author marcoottina
 *
 */
public class QuadTreeDouble extends Quadtree<Point2D.Double, Double> {

	private static final long serialVersionUID = 1L;

	public static final IntFunction<Point2D.Double[]> PRODUCER_ARRAY_POINT_DOUBLE = i -> {
		return new Point2D.Double[i];
	};

	public static final IntFunction<Double[]> PRODUCER_ARRAY_DOUBLE = i -> {
		return new Double[i];
	};

	public static Rectangle2D rectangleFromBottomLeftAndSizes(double x, double y, double w, double h) {
		return new Rectangle2D.Double(x, y, w, h);
	}

	public QuadTreeDouble(double[][] initialPoints, double[] zAxes, int maxPointsPerSubsection) {
		super(//
				Arrays.stream(initialPoints).map(a -> new Point2D.Double(a[0], a[1]))
						.toArray(PRODUCER_ARRAY_POINT_DOUBLE), //
				Arrays.stream(zAxes).mapToObj(Double::valueOf).toArray(PRODUCER_ARRAY_DOUBLE), //
				maxPointsPerSubsection);
	}

	public QuadTreeDouble(double[][] initialPoints, double[] zAxes, int maxPointsPerSubsection, double xBottomLeft,
			double yBottomLeft, double height, double width) {
		super(//
				Arrays.stream(initialPoints).map(a -> new Point2D.Double(a[0], a[1]))
						.toArray(PRODUCER_ARRAY_POINT_DOUBLE), //
				Arrays.stream(zAxes).mapToObj(Double::valueOf).toArray(PRODUCER_ARRAY_DOUBLE), //
				maxPointsPerSubsection, xBottomLeft, yBottomLeft, height, width);
	}

	public QuadTreeDouble(Double[][] initialPoints, Double[] zAxes, int maxPointsPerSubsection) {
		super(//
				Arrays.stream(initialPoints).map(a -> new Point2D.Double(a[0], a[1]))
						.toArray(PRODUCER_ARRAY_POINT_DOUBLE), //
				zAxes, //
				maxPointsPerSubsection);
	}

	public QuadTreeDouble(Double[][] initialPoints, Double[] zAxes, int maxPointsPerSubsection, double xBottomLeft,
			double yBottomLeft, double height, double width) {
		super(//
				Arrays.stream(initialPoints).map(a -> new Point2D.Double(a[0], a[1]))
						.toArray(PRODUCER_ARRAY_POINT_DOUBLE), //
				zAxes, //
				maxPointsPerSubsection, xBottomLeft, yBottomLeft, height, width);
	}
}