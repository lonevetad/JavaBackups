package geometry.implementations.shapeRunners;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Arrays;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.PointConsumer;
import geometry.pointTools.PolygonUtilities;
import tools.Comparators;
import tools.MathUtilities;

public class ShapeRunnerRectangleBorder extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = 3565885292245201000L;
	public static ShapeRunnerRectangleBorder SINGLETON;

	public static ShapeRunnerRectangleBorder getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ShapeRunnerRectangleBorder();
		return SINGLETON;
	}

	private ShapeRunnerRectangleBorder() {}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() { return ShapeRunnersImplemented.RectangleBorder; }

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action, boolean shouldPerformEarlyStops) {
		int[] xx, yy;
		double angDeg;
		ShapeRectangle sr;
		Point corner;
		Polygon poly;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		sr = (ShapeRectangle) shape;
		angDeg = sr.getAngleRotation();
		poly = sr.toPolygon();
		xx = poly.xpoints;
		yy = poly.ypoints;
		corner = new Point();
		if (angDeg == 0.0 || angDeg == 180.0) {
//			corner.x = xx[0];
//			corner.y = yy[0];
			corner.x = sr.getXTopLeft();
			corner.y = sr.getYTopLeft();
			ShapeRunnerLine.runHorizontalSpan(action, corner, sr.getWidth(), shouldPerformEarlyStops);
//			corner.x = xx[1]; // the same as 0
			corner.x = sr.getXTopLeft() + sr.getWidth(); // the same as 0
//			corner.y = yy[1]; // same as 0
//			corner.y++;
			ShapeRunnerLine.runVerticalSpan(action, corner, sr.getHeight(), shouldPerformEarlyStops);
			// the 3 is needed instead of 2 because of it runs from left to right
//			corner.x = xx[3]; // same as 0
//			corner.y = yy[3];
			corner.x = sr.getXTopLeft();
			corner.y = sr.getYTopLeft() + sr.getHeight();
			ShapeRunnerLine.runHorizontalSpan(action, corner, sr.getWidth(), shouldPerformEarlyStops);
			// same as 3: runs from lower y to higher y
//			corner.x = xx[0];
//			corner.y = yy[0];
			corner.x = sr.getXTopLeft();
			corner.y = sr.getYTopLeft();
//			corner.y = yy[3]; //same as 2
			ShapeRunnerLine.runVerticalSpan(action, corner, sr.getHeight(), shouldPerformEarlyStops);
		} else if (angDeg == 90.0 || angDeg == 270.0) {
			// specular to upper one
//			corner.x = xx[0];
//			corner.y = yy[0];
			corner.x = sr.getXTopLeft();
			corner.y = sr.getYTopLeft();
			ShapeRunnerLine.runHorizontalSpan(action, corner, sr.getHeight(), shouldPerformEarlyStops);
//			corner.x = xx[1];
			corner.x = sr.getXTopLeft() + sr.getHeight(); // the same as 0
			ShapeRunnerLine.runVerticalSpan(action, corner, sr.getWidth(), shouldPerformEarlyStops);
//			corner.x = xx[3];
//			corner.y = yy[3];
			corner.x = sr.getXTopLeft();
			corner.y = sr.getYTopLeft() + sr.getWidth();
			ShapeRunnerLine.runHorizontalSpan(action, corner, sr.getHeight(), shouldPerformEarlyStops);
//			corner.x = xx[0];
//			corner.y = yy[0];
			corner.x = sr.getXTopLeft() + sr.getHeight(); // the same as 0
			corner.y = sr.getYTopLeft();
			ShapeRunnerLine.runVerticalSpan(action, corner, sr.getWidth(), shouldPerformEarlyStops);
		} else {
			System.out.println("POLYGON: " + PolygonUtilities.polygonToString(poly));
			ShapeRunnerPolygonBorder.runShapePolygon(poly, action, shouldPerformEarlyStops);
//			runRectangleBorderRotated(action, new Point2D[] { //
//					new Point2D.Double(xx[0], yy[0]), //
//					new Point2D.Double(xx[1], yy[1]), //
//					new Point2D.Double(xx[2], yy[2]), //
//					new Point2D.Double(xx[3], yy[3]) }, shouldPerformEarlyStops);
		}
		return true;
	}

	public static void runRectangleBorderRotated(PointConsumer action, Point2D[] corners,
			boolean shouldPerformEarlyStops) {
//		if (PointConsumer.FORCE_EARLY_STOP_CHECKS)
//			runRectangleBorderRotated_EarlyStopped(action, corners);
//		else
//			runRectangleBorderRotated_NonStopping(action, corners);
//	}
//
//	/* Copy-pasted from ShapeRunnerRectangleFilled */
//	public static void runRectangleBorderRotated_NonStopping(PointConsumer action, Point2D[] corners,
//			boolean shouldPerformEarlyStops) {
		boolean isLeftRegressing, isRightProgressing, changedLeftTimeAgo, changedRightTimeAgo;
		int x, y, lasty, y2, y3, prevxLeft, prevxRight;
		double slopeLeft, slopeRight, ql, qr; /* slope12, slope13, slope24, slope34, */
		Point2D p1, p2, p3, p4; // , c[]; // == top, left, right, bottom
		Point p;
		if (action == null || corners == null || corners.length < 4)
			return;
		Arrays.sort(corners, Comparators.POINT_2D_COMPARATOR_LOWEST_FIRST);
		p1 = corners[0];
		if (p1 == null)
			return;
		p2 = corners[1];
		if (p2 == null)
			return;
		p3 = corners[2];
		if (p3 == null)
			return;

		if (p3.getX() < p2.getX()) { // swap
			p4 = p3;
			p3 = p2;
			p2 = p4;
		}
		p4 = corners[3];
		if (p4 == null)
			return;

		y2 = (int) Math.round(p2.getY());
		{
			double p1y, p1x;
			p = new Point(x = (int) Math.round(p1.getX()), y = (int) Math.round(p1y = p1.getY()));
			// optimize: check the quasi-perpendicular rectangle:

			action.accept(p);
			slopeLeft = MathUtilities.slope(p1, p2);
			slopeRight = MathUtilities.slope(p1, p3);
			ql = p1y - slopeLeft * (p1x = p1.getX());
			qr = p1y - slopeRight * p1x;
		}
		y3 = (int) Math.round(p3.getY());
		lasty = (int) Math.round(p4.getY());
		prevxRight = prevxLeft = x = p.x;
		isLeftRegressing = isRightProgressing = true;
		changedLeftTimeAgo = changedRightTimeAgo = true;

//		if (shouldPerformEarlyStops) {
//
//			// TODO VERIFICARE LE RIPETIZIONI DI CODICE NELL' IF-ELSE
//
//			while (++y < lasty) {
//				if (y == y2 && action.canContinue()) {
//					slopeLeft = MathUtilities.slope(p2, p4);
//					ql = p2.getY() - slopeLeft * p2.getX();
//					isLeftRegressing = false;
//					prevxLeft = (int) Math.round(p2.getX());
//				}
//				if (y == y3) {
//					slopeRight = MathUtilities.slope(p3, p4);
//					qr = p3.getY() - slopeRight * p3.getX();
//					isRightProgressing = false;
//					prevxRight = (int) Math.round(p3.getX());
//				}
//				p.y = y;
//				x = (int) Math.round((y - ql) / slopeLeft);
//				if (isLeftRegressing) {
//					p.x = x;
//					if (action.canContinue())
//						ShapeRunnerLine.runHorizontalSpan(action, p, prevxRight - x, shouldPerformEarlyStops);
//				} else {
//					p.x = prevxRight;
//					if (action.canContinue())
//						ShapeRunnerLine.runHorizontalSpan(action, p, x - prevxRight, shouldPerformEarlyStops);
//				}
//				prevxRight = x;
//				x = ((int) Math.round((y - qr) / slopeRight));
//				if (isRightProgressing) {
//					p.x = prevxLeft;
//					if (action.canContinue())
//						ShapeRunnerLine.runHorizontalSpan(action, p, x - prevxLeft, shouldPerformEarlyStops);
//				} else {
//					p.x = x;
//					if (action.canContinue())
//						ShapeRunnerLine.runHorizontalSpan(action, p, prevxLeft - x, shouldPerformEarlyStops);
//				}
//				prevxLeft = x;
//			}
//			if (action.canContinue()) {
//				p.setLocation((int) Math.round(p4.getX()), lasty);
//				action.accept(p);
//			}
//		} else {

		while (++y < lasty) {
			if (y == y2 && action.canContinue()) {
				slopeLeft = MathUtilities.slope(p2, p4);
				ql = p2.getY() - slopeLeft * p2.getX();
				isLeftRegressing = false;
				p.x = x = (int) Math.round(p2.getX());
//				p.y--;
				p.y = y;
				ShapeRunnerLine.runHorizontalSpan(action, p, prevxLeft - x, shouldPerformEarlyStops);
//				p.y++;
//				p.x = x;
//				action.accept(p);
//				p.y--;
				prevxLeft = x;
				changedLeftTimeAgo = false;
			}
			if (y == y3) {
				slopeRight = MathUtilities.slope(p3, p4);
				qr = p3.getY() - slopeRight * p3.getX();
				isRightProgressing = false;
//				p.x =
				x = (int) Math.round(p3.getX());
				p.y = y;
				p.x = prevxRight;
				ShapeRunnerLine.runHorizontalSpan(action, p, x - prevxRight, shouldPerformEarlyStops);
				p.x = x;
//				p.y++;
//				action.accept(p);
//				p.y--;
				prevxRight = x;
				changedRightTimeAgo = false;
			}
			p.y = y;
			if (changedLeftTimeAgo)
				x = (int) Math.round((y - ql) / slopeLeft);
			if (isLeftRegressing) {
				p.x = x;
				ShapeRunnerLine.runHorizontalSpan(action, p, prevxLeft - x, shouldPerformEarlyStops);
				prevxLeft = x;
			} else {
				if (changedLeftTimeAgo) {
					// skip one run: already done
					p.x = prevxLeft;
					ShapeRunnerLine.runHorizontalSpan(action, p, x - prevxLeft, shouldPerformEarlyStops);
					prevxLeft = x;
				} else
					changedLeftTimeAgo = true;
			}
			if (changedRightTimeAgo)
				x = ((int) Math.round((y - qr) / slopeRight));
			if (isRightProgressing) {
				p.x = prevxRight;
				ShapeRunnerLine.runHorizontalSpan(action, p, x - prevxRight, shouldPerformEarlyStops);
				prevxRight = x;
			} else {
				if (changedRightTimeAgo) {
					p.x = x;
					ShapeRunnerLine.runHorizontalSpan(action, p, prevxRight - x, shouldPerformEarlyStops);
					prevxRight = x;
				} else
					// skip one run: already done
					changedRightTimeAgo = true;
			}
		}
		p.y = lasty;
		x = (int) Math.round(p4.getX());
		p.x = prevxLeft;
		ShapeRunnerLine.runHorizontalSpan(action, p, x - prevxLeft, shouldPerformEarlyStops);
		p.x = x;
		ShapeRunnerLine.runHorizontalSpan(action, p, prevxRight - x, shouldPerformEarlyStops);
//		p.setLocation((int) Math.round(p4.getX()), lasty);
//		action.accept(p);
//		}
	}

//	public static void runRectangleBorderRotated_EarlyStopped(PointConsumer action, Point2D[] corners) {
//		boolean isLeftRegressing, isRightProgressing;
//		int x, y, lasty, y2, y3, prevxLeft, prevxRight;
//		double slopeLeft, slopeRight, ql, qr; /* slope12, slope13, slope24, slope34, */
//		Point2D p1, p2, p3, p4; // , c[]; // == top, left, right, bottom
//		Point p;
//		if (action == null || corners == null || corners.length < 4)
//			return;
//		Arrays.sort(corners, Comparators.POINT_2D_COMPARATOR_LOWEST_FIRST);
//		p1 = corners[0];
//		if (p1 == null)
//			return;
//		p2 = corners[1];
//		if (p2 == null)
//			return;
//		p3 = corners[2];
//		if (p3 == null)
//			return;
//
//	}
}