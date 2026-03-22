package geometry.implementations.shapeRunners;

import java.awt.Point;
import java.awt.geom.Point2D;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.implementations.shapes.ShapeLine;
import geometry.pointTools.PointConsumer;
import tools.MathUtilities;

public class ShapeRunnerLine extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = 455240963888849770L;
	private static ShapeRunnerLine SINGLETON;

	public static ShapeRunnerLine getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ShapeRunnerLine();
		return SINGLETON;
	}

	private ShapeRunnerLine() {
	}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return ShapeRunnersImplemented.Line;
	}

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action, boolean shouldPerformEarlyStops) {
//		int length;
//		double angDeg;
//		Point point;
		ShapeLine sp;
//		Polygon p;
		sp = (ShapeLine) shape;
//		p = sp.toPolygon();
//		angDeg = MathUtilities.adjustDegAngle(shape.getAngleRotation());
//		length = sp.getLength();
//		point = new Point(p.xpoints[0], p.ypoints[0]);
//		System.out.println(
//				"POINT: " + point + ", centre: " + sp.getCenter() + ", p2: " + new Point(p.xpoints[1], p.ypoints[1]));
//		runSpan_ControlledEnvironment(action, point, length, angDeg, p.xpoints[1], p.ypoints[1]);
//		runSpan(action, new Point(p.xpoints[0], p.ypoints[0]), new Point(p.xpoints[1], p.ypoints[1]),
//				shouldPerformEarlyStops);
		runSpan(action, sp.getP1(), sp.getP2(), shouldPerformEarlyStops);
		return true;
	}

	public static void runSpan(PointConsumer action, Point2D point1, Point2D point2, boolean shouldPerformEarlyStops) {
		double d, a, x1, y1, x2, y2;
		x1 = point1.getX();
		y1 = point1.getY();
		x2 = point2.getX();
		y2 = point2.getY();
		if (x1 == x2) {
			if (y1 <= y2) {
				a = 90.0;
				d = y2 - y1;
			} else {
//				a = 270;
				a = 90.0;
				d = y1 - y2;
				point1.setLocation(x1, y2);
			}
		} else if (y1 == y2) {
			if (x1 <= x2) {
				a = 0.0;
				d = x2 - x1;
			} else {
//				a = 180;
				a = 0.0;
				d = x1 - x2;
				point1.setLocation(x2, y1);
			}
		} else {
			a =
//			Math.toDegrees(Math.atan(MathUtilities.slope(x1, y1, x2, y2)));
					MathUtilities.angleDegrees(x1, y1, x2, y2);
			if (a < 0.0)
				a += 360.0;
			d = Math.hypot(x1 - x2, //
					y1 - y2);
//			System.out.println("--before run span from " + point1 + " to " + point2);
			if (a > 90.0 && a < 270.0) {
				point1 = point2;
				a -= 180.0;
//				System.out.println("--CHANGED .. angle: " + a);
			}
		}
		runSpanFrom(action, point1, (int) Math.floor(1 + d), a, shouldPerformEarlyStops);
	}

	public static void runSpanFrom(PointConsumer action, Point2D point, int length, double angDeg,
			boolean shouldPerformEarlyStops) {
		if (action == null || point == null | length <= 0)
			return;
		if (angDeg == 0.0)
			runHorizontalSpan(action, point, length, shouldPerformEarlyStops);
		else if (angDeg == 180.0) {
			point.setLocation(point.getX() - length, point.getY());
			runHorizontalSpan(action, point, length, shouldPerformEarlyStops);
		} else if (angDeg == 90.0)
			runVerticalSpan(action, point, length, shouldPerformEarlyStops);
		else if (angDeg == 270.0) {
			point.setLocation(point.getX(), point.getY() - length);
			runVerticalSpan(action, point, length, shouldPerformEarlyStops);
		} else
			runRotatedSpan(action, point, length, angDeg, shouldPerformEarlyStops);
	}

//	protected static void runSpan_ControlledEnvironment(PointConsumer action, Point2D point, int length, double angDeg,
//			boolean shouldPerformEarlyStops) {
//		if (angDeg == 0.0)
//			runHorizontalSpan(action, point, length, shouldPerformEarlyStops);
//		else if (angDeg == 180.0) {
//			point.setLocation(point.getX() - length, point.getY());
//			runHorizontalSpan(action, point, length, shouldPerformEarlyStops);
//		} else if (angDeg == 90.0)
//			runVerticalSpan(action, point, length, shouldPerformEarlyStops);
//		else if (angDeg == 270.0) {
//			point.setLocation(point.getX(), point.getY() - length);
//			runVerticalSpan(action, point, length, shouldPerformEarlyStops);
//		} else
//			runRotatedSpan(action, point, length, angDeg, shouldPerformEarlyStops);
//	}

	protected static void runHorizontalSpan(PointConsumer action, Point2D pp, int length,
			boolean shouldPerformEarlyStops) {
		Point point;
		point = new Point((int) pp.getX(), (int) pp.getY());
		if (length == 0)
			length = 1;
		if (shouldPerformEarlyStops)
			while (length-- > 0 && action.canContinue()) {
				action.accept(point);
				point.x++;
			}
		else
			while (length-- > 0) {
				action.accept(point);
				point.x++;
			}
	}

	protected static void runHorizontalSpan(PointConsumer action, Point point, int length,
			boolean shouldPerformEarlyStops) {
		if (length == 0)
			length = 1;
		if (shouldPerformEarlyStops)
			while (length-- > 0 && action.canContinue()) {
				action.accept(point);
				point.x++;
			}
		else
			while (length-- > 0) {
				action.accept(point);
				point.x++;
			}
	}

	protected static void runVerticalSpan(PointConsumer action, Point2D pp, int length,
			boolean shouldPerformEarlyStops) {
		Point point;
		point = new Point((int) pp.getX(), (int) pp.getY());
		if (length == 0)
			length = 1;
		if (shouldPerformEarlyStops)
			while (length-- > 0 && action.canContinue()) {
				action.accept(point);
				point.y++;
			}
		else
			while (length-- > 0) {
				action.accept(point);
				point.y++;
			}
	}

	protected static void runVerticalSpan(PointConsumer action, Point point, int length,
			boolean shouldPerformEarlyStops) {
		if (length == 0)
			length = 1;
		if (shouldPerformEarlyStops)
			while (length-- > 0 && action.canContinue()) {
				action.accept(point);
				point.y++;
			}
		else
			while (length-- > 0) {
				action.accept(point);
				point.y++;
			}
	}

	protected static void runRotatedSpan(PointConsumer action, Point2D pp, int length, double angDeg,
			boolean shouldPerformEarlyStops) {
		int x, y, i, lastx, lasty;
		double rad, sin, cos;
		Point point;
		point = new Point((int) pp.getX(), (int) pp.getY());
		x = point.x;
		y = point.y;
		rad = Math.toRadians(angDeg);
		cos = Math.cos(rad);
		sin = Math.sin(rad);
		lastx = (int) Math.round(x + (length * cos));
		lasty = (int) Math.round(y + (length * sin));
		i = 0;
		action.accept(point);
		if (shouldPerformEarlyStops) {
			if (!action.canContinue())
				return;
			action.accept(point);
			while (++i < length && action.canContinue()) {
				point.x = (int) Math.round(x + cos * i);
				point.y = (int) Math.round(y + sin * i);
				action.accept(point);
			}
			point.x = lastx;
			point.y = lasty;
			if (!action.canContinue())
				return;
			action.accept(point);
		} else {
			action.accept(point);
			while (++i < length) {
				point.x = (int) Math.round(x + cos * i);
				point.y = (int) Math.round(y + sin * i);
				action.accept(point);
			}
			point.x = lastx;
			point.y = lasty;
			action.accept(point);
		}
	}

	/*
	 * protected static void runRotatedSpan_FatLine(PointConsumer action, Point
	 * point, int length, double angDeg, int lastx, int lasty) {boolean
	 * yfloorDifferent,yCeilDifferent // boolean isRoundx,isRoundy; int x, y, i; int
	 * xfloor, yfloor, xceil, yceil, x1, y1, x2, y2, x3, y3, x4, y4;// act as a
	 * cache, to not repeat points double rad, sin, cos, xx, yy; rad =
	 * Math.toRadians(angDeg); sin = Math.sin(rad); cos = Math.cos(rad); i = 0;
	 * action.accept(point); x1 = x2 = x3 = x4 = x = point.x; y1 = y2 = y3 = y4 = y
	 * = point.y; while (++i < length) { xx = x + cos * i; yy = y + sin * i; //
	 * check cache xfloor = (int) Math.floor(xx); yfloor = (int) Math.floor(yy);
	 * xceil = (int) Math.ceil(xx); yceil = (int) Math.ceil(yy);
	 *
	 * if (xfloor != x1) { point.x = xfloor; if (yfloor != y1) { y1 = yfloor;
	 * point.y = yfloor; action.accept(point); } if (yfloor != yceil) { point.y =
	 * yfloor; action.accept(point); } y2 = yceil;
	 *
	 * x1 = xfloor; } if (xfloor != xceil) {
	 *
	 * }
	 *
	 * if (xt != x1) point.x = (int) Math.round(x + cos * i); point.y = (int)
	 * Math.round(y + sin * i); action.accept(point); } point.x = lastx; point.y =
	 * lasty; action.accept(point); }
	 */
}