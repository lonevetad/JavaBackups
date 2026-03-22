package geometry.implementations.intersectors;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeLine;
import geometry.pointTools.PolygonUtilities;
import tools.MathUtilities;

public class PolygonToLineIntersection implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 651246543015L;
	private static PolygonToLineIntersection SINGLETON;

	public static PolygonToLineIntersection getInstance() {
		if (SINGLETON == null)
			SINGLETON = new PolygonToLineIntersection();
		return SINGLETON;
	}

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		int len, otherIndex, thisx, thisy, lastx, lasty;
		int[] xx, yy;
//		Point2D p1, p2;
		Line2D line, linePolygon;
		Polygon polygon;
		ShapeLine l1;

//		polygon = s1.toPolygon();
//		xx = polygon.xpoints;
//		yy = polygon.ypoints;
//		p1 = new Point2D.Double(xx[0], yy[0]);
//		p2 = new Point2D.Double(xx[1], yy[1]);
		l1 = (ShapeLine) s1;
		line = l1.toLine();
		polygon = s2.toPolygon();
		if (l1 == null || polygon == null)
			return false;

		if (PolygonUtilities.isInside(line.getP1(), polygon) || PolygonUtilities.isInside(line.getP2(), polygon)
				|| PolygonUtilities.isInside(s1.getCenter(), polygon))
			return true;
		// perform check on the tinier polygon and put in secondPoint
		len = polygon.npoints;
		xx = polygon.xpoints;
		yy = polygon.ypoints;
		otherIndex = 0;
		lastx = xx[otherIndex];
		lasty = yy[otherIndex];
		while(--len >= 0) {
			thisx = xx[len];
			thisy = yy[len];
			linePolygon = new Line2D.Double(thisx, thisy, lastx, lasty);
			if (MathUtilities.getLineIntersection(line, linePolygon) != null)
				return true;
			lastx = thisx;
			lasty = thisy;
		}
		return false;
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		int len, otherIndex, thisx, thisy, lastx, lasty;
		int[] xx, yy;
		Line2D line, linePolygon;
		Polygon polygon;
		Point2D pointIntersection;
		ShapeLine l1;
		List<Point2D> list;
		l1 = (ShapeLine) s1;
		line = l1.toLine();
		polygon = s2.toPolygon();
		if (l1 == null || polygon == null)
			return null;
		// perform check on the tinier polygon and put in secondPoint
		len = polygon.npoints;
		xx = polygon.xpoints;
		yy = polygon.ypoints;
		otherIndex = 0;
		lastx = xx[otherIndex];
		lasty = yy[otherIndex];
		list = new LinkedList<>();
		while(--len >= 0) {
			pointIntersection = null;
			thisx = xx[len];
			thisy = yy[len];
			linePolygon = new Line2D.Double(thisx, thisy, lastx, lasty);
			pointIntersection = MathUtilities.getLineIntersection(line, linePolygon);
			if (pointIntersection != null)
				list.add(pointIntersection);
			lastx = thisx;
			lasty = thisy;
		}
		return list.isEmpty() ? null : list;
	}
}