package geometry.implementations.intersectors;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapePoint;

public class PointToPointIntersectionDetector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 1111L;
	private static PointToPointIntersectionDetector SINGLETON;

	public static PointToPointIntersectionDetector getInstance() {
		if (SINGLETON == null)
			SINGLETON = new PointToPointIntersectionDetector();
		return SINGLETON;
	}

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		return ((ShapePoint) s1).equals(s2);
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		if (!this.areIntersecting(s1, s2))
			return null;
		List<Point2D> l;
		l = new LinkedList<>();
		l.add(s1.getCenter());
		return l;
	}
}