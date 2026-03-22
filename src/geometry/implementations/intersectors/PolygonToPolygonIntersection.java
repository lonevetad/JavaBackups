package geometry.implementations.intersectors;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import tools.MathUtilities;

public class PolygonToPolygonIntersection implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 2222L;
	private static PolygonToPolygonIntersection SINGLETON = null;

	public static PolygonToPolygonIntersection getInstance() {
		if (SINGLETON == null)
			SINGLETON = new PolygonToPolygonIntersection();
		return SINGLETON;
	}

	/*
	 * @Override public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D
	 * s2) { int len; int[] xx, yy; Polygon p1, p2; p1 = s1.toPolygon(); p2 =
	 * s2.toPolygon(); if (p1 == null || p2 == null) return false; // perform check
	 * on the tinier polygon and put in secondPoint if (p1.npoints > p2.npoints) {
	 * Polygon p; p = p1; p1 = p2; p2 = p; } len = p2.npoints; xx = p2.xpoints; yy =
	 * p2.ypoints; while (--len >= 0) if
	 * (PolygonUtilities.isPointInsideThePolygon// p1.contains (xx[len],
	 * yy[len], p1)) return true; return false; }
	 */

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		Polygon polygon, otherPolygon;
		polygon = s1.toPolygon();
		otherPolygon = s2.toPolygon();
		if (otherPolygon == null || polygon == null)
			return null;
		return MathUtilities.getPolygonsIntersection(polygon, otherPolygon);
	}
}