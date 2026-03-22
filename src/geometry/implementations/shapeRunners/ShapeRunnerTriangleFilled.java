package geometry.implementations.shapeRunners;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.pointTools.PointConsumer;
import tools.MathUtilities;

public class ShapeRunnerTriangleFilled extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = -6501855552021048L;
	private static ShapeRunnerTriangleFilled SINGLETON;

	public static ShapeRunnerTriangleFilled getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ShapeRunnerTriangleFilled();
		return SINGLETON;
	}

	protected ShapeRunnerTriangleFilled() {
	}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return ShapeRunnersImplemented.Triangle;
	}

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action, boolean shouldPerformEarlyStops) {
		int[] xx, yy;
		Polygon poly;
		poly = shape.toPolygon();
		xx = poly.xpoints;
		yy = poly.ypoints;
		return runShapeImpl(//
				new Point2D[] { new Point2D.Double(xx[0], yy[0]), new Point2D.Double(xx[1], yy[1]),
						new Point2D.Double(xx[2], yy[2]), }, //
				action, shouldPerformEarlyStops);
	}

	protected boolean runShapeImpl(Point2D[] corners, PointConsumer action, boolean shouldPerformEarlyStops) {
		int x, y, lasty, y2, y3;
		double slopeLeft, slopeRight, lastSlope, ql, qr, qls; /* slope12, slope13, slope24, slope34, */
		Point2D p1, p2, p3, temp; // , c[]; // == top, left, right, bottom
		Point p;
		if (action == null || corners == null || corners.length < 3)
			return false;
		p1 = corners[0];
		if (p1 == null)
			return false;
		p2 = corners[1];
		if (p2 == null)
			return false;
		// sort
		if (p1.getY() > p2.getY()) {
			p3 = p2;
			p2 = p1;
			p1 = p3;
		}
		p3 = corners[2];
		if (p3 == null)
			return false;
		// sort
		if ((p3.getY() > p1.getY()) || ((p3.getY() == p1.getY()) && (p3.getX() < p1.getX()))) {
			temp = p3;
			p3 = p1;
			p1 = temp;
		}
		if (p3.getX() < p2.getX()) {
			temp = p3;
			p3 = p2;
			p2 = temp;
		}
		corners = null;
		y2 = (int) Math.round(p2.getY());
		y3 = (int) Math.round(p3.getY());
		{
			double p1y, p1x;
			p = new Point((int) Math.round(p1.getX()), y = (int) Math.round(p1y = p1.getY()));
			action.accept(p);
			slopeLeft = MathUtilities.slope(p1, p2);
			slopeRight = MathUtilities.slope(p1, p3);
			lastSlope = MathUtilities.slope(p2, p3);
			ql = p1y - slopeLeft * (p1x = p1.getX());
			qr = p1y - slopeRight * p1x;
			if (y3 < y2) {
				lasty = y3;
				qls = p2.getY() - p2.getX() * lastSlope;
			} else {
				lasty = y2;
				qls = p3.getY() - p3.getX() * lastSlope;
			}
		}
		if (shouldPerformEarlyStops) {
			while (++y < lasty && action.canContinue()) {
				if (y == y2) {
					slopeLeft = lastSlope;
					ql = qls;
				}
				if (y == y3) {
					slopeRight = lastSlope;
					qr = qls;
				}
				p.y = y;
				p.x = x = (int) Math.round((y - ql) / slopeLeft);
				ShapeRunnerLine.runHorizontalSpan(action, p, //
						((int) Math.round((y - qr) / slopeRight)) - x, shouldPerformEarlyStops);
			}
			if (y2 == y3 && action.canContinue()) {
				p.y = y2;
				p.x = (int) Math.round(p2.getX());
				ShapeRunnerLine.runHorizontalSpan(action, p, Math.max(1, (int) Math.round(p2.getX()) - p.x),
						shouldPerformEarlyStops);
			}
			if (action.canContinue()) {
				p.setLocation((int) Math.round((y2 < y3) ? p3.getX() : p2.getX()), lasty);
				action.accept(p);
			}
		} else {
			while (++y < lasty) {
				if (y == y2) {
					slopeLeft = lastSlope;
					ql = qls;
				}
				if (y == y3) {
					slopeRight = lastSlope;
					qr = qls;
				}
				p.y = y;
				p.x = x = (int) Math.round((y - ql) / slopeLeft);
				ShapeRunnerLine.runHorizontalSpan(action, p, //
						((int) Math.round((y - qr) / slopeRight)) - x, shouldPerformEarlyStops);
			}
			if (y2 == y3) {
				p.y = y2;
				p.x = (int) Math.round(p2.getX());
				ShapeRunnerLine.runHorizontalSpan(action, p, Math.max(1, (int) Math.round(p2.getX()) - p.x),
						shouldPerformEarlyStops);
			}
			p.setLocation((int) Math.round((y2 < y3) ? p3.getX() : p2.getX()), lasty);
			action.accept(p);
		}
		return true;
	}
}