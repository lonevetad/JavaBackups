package geometry.implementations.intersectors;

import java.awt.geom.Point2D;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ProviderShapeRunner;
import geometry.ShapeRunnersImplemented;
import geometry.ShapesIntersectionDetector;

/**
 * Should not exists thanks to the ordering of {@link ShapeRunnersImplemented}
 * on {@link ProviderShapeRunner} and then replaced by
 * {@link PolygonToLineIntersection}. In fact, it delegates there the work.
 */
public class LineToPolygonIntersector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 1111L;

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		return PolygonToLineIntersection.getInstance().areIntersecting(s2, s1);
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		return PolygonToLineIntersection.getInstance().computeIntersectionPoints(s2, s1);
	}
}