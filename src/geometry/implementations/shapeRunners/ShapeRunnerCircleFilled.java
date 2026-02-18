package geometry.implementations.shapeRunners;

import java.awt.Point;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.implementations.shapes.ShapeCircle;
import geometry.pointTools.PointConsumer;

// TODO UNIFICARE RUN_SHAPE COME IN CIRCLE_BORDER E DISCRIMINARE SE SI FORZA L'EARLY_STOPPING

public class ShapeRunnerCircleFilled extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = 72014502659888L;
	private static ShapeRunnerCircleFilled SINGLETON = null;

	public static ShapeRunnerCircleFilled getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ShapeRunnerCircleFilled();
		return SINGLETON;
	}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return ShapeRunnersImplemented.Disk;
	}

	@Override
	public boolean runShape(AbstractShape2D shape, PointConsumer action) {
		return super.runShape(shape, action);
	}

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action, boolean shouldPerformEarlyStops) {
		int xcentre, ycentre, r, r2, dx, dy, diameter, xc, xc_1, yc, yc_1;
//		double r2;
		ShapeCircle sc;
		Point point;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		sc = (ShapeCircle) shape;
		diameter = sc.getDiameter();
		r = diameter >> 1;
		xc = xcentre = sc.getXCenter();
		ycentre = sc.getYCenter();
		point = new Point(xcentre, ycentre);
		if (r < 1)
			return false;
		switch (diameter) {
		case 1: {
			if ((!shouldPerformEarlyStops) || (action.canContinue()))
				action.accept(point);
			return true;
		}
		case 2: {
			if (shouldPerformEarlyStops) {
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x++;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.y++;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				return true;
			} else {
				action.accept(point);
				point.x++;
				action.accept(point);
				point.y++;
				action.accept(point);
				point.x--;
				action.accept(point);

			}
			return true;
		}
		case 3: {
			if (shouldPerformEarlyStops) {
				point.y--;
				point.x--;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				point.y++;
				if (!action.canContinue())
					return true;
				point.x = --xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				point.y++;
				if (!action.canContinue())
					return true;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				return true;
			} else {
				point.y--;
				point.x--;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				point.y++;
				point.x = --xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				point.y++;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				return true;
			}
		}
		case 4: {
			if (shouldPerformEarlyStops) {
				point.y--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x++;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.y += 3;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x--;
				xc = point.x;
				point.y--;
				if (!action.canContinue())
					return true;
				ShapeRunnerLine.runHorizontalSpan(action, point, 4, shouldPerformEarlyStops);
				point.y--;
				if (!action.canContinue())
					return true;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 4, shouldPerformEarlyStops);
			} else {
				point.y--;
				action.accept(point);
				point.x++;
				action.accept(point);
				point.y += 3;
				action.accept(point);
				point.x--;
				action.accept(point);
				point.x--;
				xc = point.x;
				point.y--;
				ShapeRunnerLine.runHorizontalSpan(action, point, 4, shouldPerformEarlyStops);
				point.y--;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 4, shouldPerformEarlyStops);
			}
			return true;
		}
		case 5: {
			if (shouldPerformEarlyStops) {
				point.y -= 2;
				point.x--;
				xc = point.x;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				point.y += 5;
				if (!action.canContinue())
					return true;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				point.x = --xc;
//				point.x--;
				point.y--;
				if (!action.canContinue())
					return true;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 5, shouldPerformEarlyStops);
				point.y--;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 5, shouldPerformEarlyStops);
				point.y--;
				if (!action.canContinue())
					return true;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 5, shouldPerformEarlyStops);
			} else {
				point.y -= 2;
				point.x--;
				xc = point.x;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				point.y += 4;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
				point.x = --xc;
//			point.x--;
				point.y--;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 5, shouldPerformEarlyStops);
				point.y--;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 5, shouldPerformEarlyStops);
				point.y--;
				point.x = xc;
				ShapeRunnerLine.runHorizontalSpan(action, point, 5, shouldPerformEarlyStops);
			}
			return true;
		}
		case 6: {
			if (shouldPerformEarlyStops) {
				point.y -= 2;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x++;
				if (!action.canContinue())
					return true;
				action.accept(point);//
				point.y++;
				point.x -= 2;
				if (!action.canContinue())
					return true;
				ShapeRunnerLine.runHorizontalSpan(action, point, 4, shouldPerformEarlyStops);
				point.x = xc - 1;
				point.y++;
				if (!action.canContinue())
					return true;
				ShapeRunnerLine.runHorizontalSpan(action, point, 6, shouldPerformEarlyStops);
				point.x = xc; /// -6;
				point.y++;
				if (!action.canContinue())
					return true;
				ShapeRunnerLine.runHorizontalSpan(action, point, 6, shouldPerformEarlyStops);
				point.y++;
//				point.x++;
				point.x = xc + 1;
				if (!action.canContinue())
					return true;
				ShapeRunnerLine.runHorizontalSpan(action, point, 4, shouldPerformEarlyStops);
				point.y++;
//				point.x++;
				point.x = xc + 1;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x++;
				if (!action.canContinue())
					return true;
				action.accept(point);
			} else {
				point.y -= 2;
				action.accept(point);
				point.x++;
				action.accept(point);//
				point.y++;
				point.x -= 2;
				xc = point.x - 1;
				ShapeRunnerLine.runHorizontalSpan(action, point, 4, shouldPerformEarlyStops);
				point.x = xc;
				point.y++;
				ShapeRunnerLine.runHorizontalSpan(action, point, 6, shouldPerformEarlyStops);
				point.x = xc; /// -6;
				point.y++;
				ShapeRunnerLine.runHorizontalSpan(action, point, 6, shouldPerformEarlyStops);
				point.y++;
//			point.x++;
				point.x = xc + 1;
				ShapeRunnerLine.runHorizontalSpan(action, point, 4, shouldPerformEarlyStops);
				point.y++;
//			point.x++;
				point.x = xc + 2;
				action.accept(point);
				point.x++;
				action.accept(point);
				return true;
			}
		}
//		case (8): {
//			int[] deltaFirstQuadrant;
//			deltaFirstQuadrant = new int[] { 0, 2, 2, 3 };
//			runQuadrant_EvenDiameter(point, action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
//			return true;
//		}
		case (11): {
			int[] deltaFirstQuadrant;
			deltaFirstQuadrant = new int[] { 1, 3, 4, 4, 5, 5 };
			runQuadrant_OddDiameter(point, action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
			return true;
		}
		case (12): {
			int[] deltaFirstQuadrant;
			deltaFirstQuadrant = new int[] { 1, 3, 4, 4, 5, 5 };
			runQuadrant_EvenDiameter(point, action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
			return true;
		}
		case (13): {
			int[] deltaFirstQuadrant;
			deltaFirstQuadrant = new int[] { 1, 3, 4, 5, 5, 6, 6 };
			runQuadrant_OddDiameter(point, action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
			return true;
		}
		case (14): {
			int[] deltaFirstQuadrant;
			deltaFirstQuadrant = new int[] { 1, 3, 4, 5, 5, 6, 6 };
			runQuadrant_EvenDiameter(point, action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
			return true;
		}
		default: {
		}
		}
		/*
		 * perform eight circle's slice, thanks to its regularity. Start from lowest y
		 * (the south) and go up.
		 */
		if (shouldPerformEarlyStops) {

			// TODO VERIFICARE LE RIPETIZIONI DI CODICE NELL' IF-ELSE

			if ((diameter & 0x1) == 0) {
				xc = xcentre;
				yc = ycentre;
				xc_1 = xcentre + 1;
				yc_1 = ycentre + 1;
				r--;
			} else {
				xc = xc_1 = xcentre;
				yc_1 = yc = ycentre;
			}
			r2 = r * r;
//			r2 = diameter / 2.0;
//			r2 *= r2;
			dy = 0;
			dx = r;
			while (dy <= dx) {
				// recycle diameter as the length of the span
				point.x = xc - dx;
				point.y = yc_1 + dy;
				diameter = ((xc_1 + dx) + 1) - point.x;
				if (!action.canContinue())
					return true;
				ShapeRunnerLine.runHorizontalSpan(action, point, diameter, shouldPerformEarlyStops);
//				point.x = xc;
				point.x = xc - dx;
				point.y = yc - dy;
				if (!action.canContinue())
					return true;
				ShapeRunnerLine.runHorizontalSpan(action, point, diameter, shouldPerformEarlyStops);
				point.x = xc - dy;
				point.y = yc_1 + dx;
				diameter = ((xc_1 + dy) + 1) - point.x;
				if (!action.canContinue())
					return true;
				ShapeRunnerLine.runHorizontalSpan(action, point, diameter, shouldPerformEarlyStops);
				point.y = yc - dx;
				point.x = xc - dy;
				if (!action.canContinue())
					return true;
				ShapeRunnerLine.runHorizontalSpan(action, point, diameter, shouldPerformEarlyStops);
				// --
				dy++;
				dx = (int) Math.round(Math.sqrt(r2 - dy * dy));
			}
		} else {
			if ((diameter & 0x1) == 0) {
				xc = xcentre;
				yc = ycentre;
				xc_1 = xcentre + 1;
				yc_1 = ycentre + 1;
				r--;
			} else {
				xc = xc_1 = xcentre;
				yc_1 = yc = ycentre;
			}
			r2 = r * r;
//		r2 = diameter / 2.0;
//		r2 *= r2;
			dy = 0;
			dx = r;
			while (dy <= dx) {
				// recycle diameter as the length of the span
				point.x = xc - dx;
				point.y = yc_1 + dy;
				diameter = ((xc_1 + dx) + 1) - point.x;
				ShapeRunnerLine.runHorizontalSpan(action, point, diameter, shouldPerformEarlyStops);
				point.y = yc - dy;
				point.x = xc - dx;
				ShapeRunnerLine.runHorizontalSpan(action, point, diameter, shouldPerformEarlyStops);
//--
				point.x = xc - dy;
				point.y = yc_1 + dx;
				diameter = ((xc_1 + dy) + 1) - point.x;
				ShapeRunnerLine.runHorizontalSpan(action, point, diameter, shouldPerformEarlyStops);
				point.y = yc - dx;
				point.x = xc - dy;
				ShapeRunnerLine.runHorizontalSpan(action, point, diameter, shouldPerformEarlyStops);
				// --
				dy++;
				dx = (int) Math.round(Math.sqrt(r2 - dy * dy));
			}
		}
		return true;
//	? runShapeImpl_WithEarlyStop(shape, action)
//				: runShapeImpl_NoEarlyStop(shape, action);
	}

	protected static void runQuadrant_EvenDiameter(PointConsumer action, int xcentre, int ycentre,
			int[] deltaFirstQuadrant, boolean shouldPerformEarlyStops) {
		runQuadrant_EvenDiameter(new Point(), action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
	}

	/** Pass deltas only for non-cardinal points */
	protected static void runQuadrant_OddDiameter(PointConsumer action, int xcentre, int ycentre,
			int[] deltaFirstQuadrant, boolean shouldPerformEarlyStops) {
		runQuadrant_OddDiameter(new Point(), action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
	}

	protected static void runQuadrant_EvenDiameter(Point p, PointConsumer action, int xcentre, int ycentre,
			int[] deltaFirstQuadrant, boolean shouldPerformEarlyStops) {
		int len, dy, dx, yc_1, spanLen;
		yc_1 = ycentre + 1;
		len = deltaFirstQuadrant.length;
		dy = 0;
//		xcentre++; // knowing where the centre lies, move it to adjust it
		if (shouldPerformEarlyStops)
			while (--len >= 0 && action.canContinue()) {
				spanLen = (dx = deltaFirstQuadrant[len]) << 1;
//				dy = deltaFirstQuadrant[++i];
				p.x = xcentre - dx;
				p.y = yc_1 + dy;
				ShapeRunnerLine.runHorizontalSpan(action, p, spanLen, shouldPerformEarlyStops);
				if (action.canContinue()) {
					p.x = xcentre - dx;
					p.y = ycentre - dy;
					ShapeRunnerLine.runHorizontalSpan(action, p, spanLen, shouldPerformEarlyStops);
				}
				dy++;
			}
		else
			while (--len >= 0) {
				spanLen = ((dx = deltaFirstQuadrant[len]) + 1) << 1;
//				dy = deltaFirstQuadrant[++i];
				p.x = xcentre - dx;
				p.y = yc_1 + dy;
				ShapeRunnerLine.runHorizontalSpan(action, p, spanLen, shouldPerformEarlyStops);
				p.x = xcentre - dx;
				p.y = ycentre - dy;
				ShapeRunnerLine.runHorizontalSpan(action, p, spanLen, shouldPerformEarlyStops);
				dy++;
			}
	}

	/**
	 * Pass as array all delta except the one with maximum span (equal to the
	 * diameter, that is the array's length plus one).
	 */
	protected static void runQuadrant_OddDiameter(Point p, PointConsumer action, int xcentre, int ycentre,
			int[] deltaFirstQuadrant, boolean shouldPerformEarlyStops) {
		int len, dy, dx, spanLen;
		len = deltaFirstQuadrant.length;
//		cardinal points
//		dy = deltaFirstQuadrant[len - 1];
		dy = 0;
		if (shouldPerformEarlyStops) {
			p.x = xcentre;
			p.y = ycentre + dy;
			if (!action.canContinue())
				return;
			action.accept(p);
			p.y = ycentre - dy;
			if (!action.canContinue())
				return;
			action.accept(p);
			p.y = ycentre;
			p.x = xcentre + dy;
			if (!action.canContinue())
				return;
			action.accept(p);
			p.x = xcentre - dy;
			if (!action.canContinue())
				return;
			action.accept(p);

			while (--len >= 0 && action.canContinue()) {
				spanLen = ((dx = deltaFirstQuadrant[len]) << 1) + 1;
//				dy = deltaFirstQuadrant[++i];
				p.x = xcentre - dx;
				p.y = ycentre + dy;
				ShapeRunnerLine.runHorizontalSpan(action, p, spanLen, shouldPerformEarlyStops);
				if (action.canContinue()) {
					p.x = xcentre - dx;
					p.y = ycentre - dy;
					ShapeRunnerLine.runHorizontalSpan(action, p, spanLen, shouldPerformEarlyStops);
				}
				dy++;
			}

		} else {
			p.x = xcentre - ((spanLen = deltaFirstQuadrant.length) >> 1);
			p.y = ycentre;
			ShapeRunnerLine.runHorizontalSpan(action, p, spanLen, shouldPerformEarlyStops);
			while (--len >= 0) {
				spanLen = ((dx = deltaFirstQuadrant[len]) << 1) + 1;
//				dy = deltaFirstQuadrant[++i];
				p.x = xcentre - dx;
				p.y = ycentre + dy;
				ShapeRunnerLine.runHorizontalSpan(action, p, spanLen, shouldPerformEarlyStops);
				p.x = xcentre - dx;
				p.y = ycentre - dy;
				ShapeRunnerLine.runHorizontalSpan(action, p, spanLen, shouldPerformEarlyStops);
				dy++;
			}
		}
	}
}