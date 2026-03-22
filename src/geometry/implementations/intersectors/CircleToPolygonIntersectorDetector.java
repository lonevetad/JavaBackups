package geometry.implementations.intersectors;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeCircle;
import geometry.implementations.shapes.ShapePolygon;
import tools.MathUtilities;

public class CircleToPolygonIntersectorDetector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 11111112316516501L;
	private static CircleToPolygonIntersectorDetector SINGLETON;

	public static CircleToPolygonIntersectorDetector newInstance() {
		if (SINGLETON == null) {
			SINGLETON = new CircleToPolygonIntersectorDetector();
		}
		return SINGLETON;
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		ShapePolygon sp;
		ShapeCircle sc;
		Polygon polygon; //
		sp = (ShapePolygon) s2;
		sc = (ShapeCircle) s1;
		polygon = sp.toPolygon();
		return MathUtilities.getCirclePolygonIntersection(sc.getCenter(), sc.getDiameter(), polygon);
	}
}