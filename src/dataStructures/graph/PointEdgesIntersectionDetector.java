package dataStructures.graph;

import java.awt.geom.Point2D;

import tools.MathUtilities;

public class PointEdgesIntersectionDetector implements EdgesIntersectionDetector<Point2D, Double> {

//	@Override
//	public IntersectionResult<Point2D> computeIntersection(GraphSimple<Point2D, Double> graphSource, Point2D start1,
//			Point2D end1, Point2D start2, Point2D end2, IntersectionInstantiator<Point2D, Double> ii) {
//		Point2D pInters;
//		pInters = MathUtilities.areLinesIntersecting(start1, end1, start2, end2);
//		return new IntersectionResult<>(pInters, pInters != null);
//	}

	public static class PointIntersectionInstantiator implements IntersectionInstantiator<Point2D, Double> {

		@Override
		public Point2D computeValueOnIntersection(GraphSimple<Point2D, Double> graphSource, Point2D start1,
				Point2D end1, Point2D start2, Point2D end2) {
			return MathUtilities.areLinesIntersecting(start1, end1, start2, end2);
		}

	}
}