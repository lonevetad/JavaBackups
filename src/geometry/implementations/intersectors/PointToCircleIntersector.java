package geometry.implementations.intersectors;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeCircle;
import geometry.implementations.shapes.ShapePoint;

public class PointToCircleIntersector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 1111L;
	private static PointToCircleIntersector SINGLETON;

	public static PointToCircleIntersector getInstance() {
		if (SINGLETON == null)
			SINGLETON = new PointToCircleIntersector();
		return SINGLETON;
	}

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		int r2;
		double distanceCenters;
		ShapePoint p1;
		ShapeCircle c2;
		p1 = (ShapePoint) s1;
		c2 = (ShapeCircle) s2;
		r2 = c2.getRadius();
		distanceCenters = Math.hypot(p1.getXCenter() - c2.getXCenter(), p1.getYCenter() - c2.getYCenter());
		// just check ther radius and distance
		return c2.isFilled() ? (distanceCenters <= r2)
				: (p1.getCenter().equals(c2.getCenter())
						|| (((int) Math.floor(distanceCenters)) <= r2 && (r2 <= ((int) Math.ceil(distanceCenters)))));
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		List<Point2D> l;
		ShapePoint p1;
		p1 = (ShapePoint) s1;
		l = null;
		if (this.areIntersecting(s1, s2)) {
			l = new ArrayList<>(1);
			l.add(p1.getCenter());
		}
		return l;
	}
}