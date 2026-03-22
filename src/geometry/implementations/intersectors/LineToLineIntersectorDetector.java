package geometry.implementations.intersectors;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import geometry.AbstractShape2D;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeLine;
import tools.MathUtilities;

public class LineToLineIntersectorDetector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = 1111L;
	private static LineToLineIntersectorDetector SINGLETON;

	public static LineToLineIntersectorDetector getInstance() {
		if (SINGLETON == null)
			SINGLETON = new LineToLineIntersectorDetector();
		return SINGLETON;
	}

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		Point2D intersection;
		ShapeLine l1, l2;
		l1 = (ShapeLine) s1;
		l2 = (ShapeLine) s2;
		intersection = MathUtilities.getLineIntersection(l1.toLine(), l2.toLine());
		return intersection != null;
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		Point2D intersection;
		List<Point2D> l;
		ShapeLine l1, l2;
		l1 = (ShapeLine) s1;
		l2 = (ShapeLine) s2;
		intersection = MathUtilities.getLineIntersection(l1.toLine(), l2.toLine());
		l = null;
		if (intersection != null) {
			l = new LinkedList<>();
			l.add(intersection);
		}
		return l;
	}
}