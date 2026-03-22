package geometry.implementations.intersectors;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import geometry.pointTools.PolygonUtilities;

public class PointToPolygonIntersector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 1111L;
	private static PointToPolygonIntersector SINGLETON;

	public static PointToPolygonIntersector getInstance() {
		if (SINGLETON == null)
			SINGLETON = new PointToPolygonIntersector();
		return SINGLETON;
	}

	/** get the centre. */
	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		Polygon polygon;
		polygon = s2.toPolygon();
		return PolygonUtilities.isInside(s2.getCenter(), polygon);
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		List<Point2D> l;
		if (this.areIntersecting(s1, s2)) {
			l = new LinkedList<>();
			l.add(s2.getCenter());
			return l;
		}
		return null;
	}
}