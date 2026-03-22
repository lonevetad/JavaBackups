package geometry.implementations.intersectors;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.implementations.shapes.ShapeCircle;
import geometry.implementations.shapes.ShapeRectangle;
import tools.MathUtilities;

public class RectangleToCircleIntersectionDetector extends CircleToPolygonIntersectorDetector {
	private static final long serialVersionUID = 9869654225753000L;
	private static RectangleToCircleIntersectionDetector SINGLETON;

	public static RectangleToCircleIntersectionDetector newInstance() {
		if (SINGLETON == null) {
			SINGLETON = new RectangleToCircleIntersectionDetector();
		}
		return SINGLETON;
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		ShapeRectangle sp;
		ShapeCircle sc;
		Polygon polygon; //
		sp = (ShapeRectangle) s1;
		sc = (ShapeCircle) s2;
		polygon = sp.toPolygon();
		return MathUtilities.getCirclePolygonIntersection(sc.getCenter(), sc.getDiameter(), polygon);
	}
}