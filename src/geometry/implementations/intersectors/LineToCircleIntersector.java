package geometry.implementations.intersectors;

import java.awt.geom.Point2D;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeCircle;
import geometry.implementations.shapes.ShapeLine;
import tools.MathUtilities;

public class LineToCircleIntersector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 1111L;
	private static LineToCircleIntersector SINGLETON;

	public static LineToCircleIntersector getInstance() {
		if (SINGLETON == null)
			SINGLETON = new LineToCircleIntersector();
		return SINGLETON;
	}

	private LineToCircleIntersector() {
	}

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		return computeIntersectionPoints(s1, s2) != null;
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		int d;
		List<Point2D> l;
		ShapeCircle sc;
		ShapeLine sl;
		Point2D p1, p2, pl1, pl2; // pcl,
		if (s1 == null || s2 == null)
			return null;
		sl = (ShapeLine) s1;
		sc = (ShapeCircle) s2;
		d = sc.getDiameter();
		l = MathUtilities.getCircleLineIntersections(/* pcl = */ sc.getCenter(), d, sl.getCenter(),
				sl.getAngleRotation(), false);
//		l = MathUtilities.getCircleLineIntersections(sc.getCenter(), d, sl.getP1(), sl.getP2());
		if (l == null)
			return null;
		pl1 = sl.getP1();
		pl2 = sl.getP2();
		p1 = l.remove(0);
		p2 = l.remove(0);
		p1.setLocation(Math.round(p1.getX()), Math.round(p1.getY())); // round locations
		p2.setLocation(Math.round(p2.getX()), Math.round(p2.getY())); // round locations
//		if (Point.distance(pcl.getX(), pcl.getY(), p1.getX(), p1.getY()) <= d)
//		if (Math.hypot(pcl.getX() - p1.getX(), p1.getY() - pcl.getY()) <= d)
		if (MathUtilities.isBetween(p1, pl1, pl2))
			l.add(p1);
//			if (Point.distance(pcl.getX(), pcl.getY(), p2.getX(), p2.getY()) <= d)
//		if (Math.hypot(pcl.getX() - p2.getX(), p2.getY() - pcl.getY()) <= d)
		if (MathUtilities.isBetween(p2, pl1, pl2))
			l.add(p2);
		return l.isEmpty() ? null : l;
	}
}