package geometry.implementations.intersectors;

import java.awt.geom.Point2D;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeCircle;
import tools.MathUtilities;

public class CircleToCircleIntersectionDetector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = -7459854103L;
	private static CircleToCircleIntersectionDetector SINGLETON;

	public static CircleToCircleIntersectionDetector getInstance() {
		if (SINGLETON == null)
			SINGLETON = new CircleToCircleIntersectionDetector();
		return SINGLETON;
	}

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		int r1, r2;
		double distanceCenters;
		ShapeCircle c1, c2;
		c1 = (ShapeCircle) s1;
		c2 = (ShapeCircle) s2;
		r1 = c1.getRadius();
		r2 = c2.getRadius();
		distanceCenters = Math.hypot(c1.getXCenter() - c2.getXCenter(), c1.getYCenter() - c2.getYCenter());
		// just check ther radius and distance
		return (r1 + r2) <= distanceCenters && // and stop it or ..
				((c1.isFilled() || c2.isFilled()) ||
				// or they are 2 circumferences, they must not be too far or too close
						(Math.abs(r1 - r2) >= distanceCenters));
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		if (!areIntersecting(s1, s2))
			return null;
		ShapeCircle c1, c2;
		c1 = (ShapeCircle) s1;
		c2 = (ShapeCircle) s2;
		// c1 :
		return MathUtilities.getCircleToCircleIntersection(c1.getCenter(), c1.getRadius(), c2.getCenter(),
				c2.getRadius());
	}
}