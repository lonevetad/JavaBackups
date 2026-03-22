package geometry.implementations.intersectors;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeLine;
import geometry.implementations.shapes.ShapePoint;
import tools.MathUtilities;

public class PointToLineIntersector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 1111L;
	private static PointToLineIntersector SINGLETON;

	public static PointToLineIntersector getInstance() {
		if (SINGLETON == null)
			SINGLETON = new PointToLineIntersector();
		return SINGLETON;
	}

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		int x1, y1, x2, y2, xp, yp;
		ShapePoint p;
		ShapeLine l;
		Polygon polyLine;
		p = (ShapePoint) s1;
		l = (ShapeLine) s2;
		polyLine = l.toPolygon();
		x1 = polyLine.xpoints[0];
		y1 = polyLine.ypoints[0];
		xp = p.getXCenter();
		yp = p.getYCenter();
		x2 = polyLine.xpoints[1];
		y2 = polyLine.ypoints[1];
		return MathUtilities.areCollinear(x1, y1, xp, yp, x2, y2)//
				&& ((x1 <= xp && xp <= x2) && (y1 <= yp && yp <= y2));
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		if (this.areIntersecting(s1, s2)) {
			List<Point2D> l;
			l = new LinkedList<>();
			l.add(s1.getCenter());
			return l;
		}
		return null;
	}
}