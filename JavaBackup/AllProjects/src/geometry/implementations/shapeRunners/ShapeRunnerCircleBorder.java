
package geometry.implementations.shapeRunners;

import java.awt.Point;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.implementations.shapes.ShapeCircle;
import geometry.pointTools.PointConsumer;

public class ShapeRunnerCircleBorder extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = 72014502659887L;
	private static ShapeRunnerCircleBorder SINGLETON;

	public static ShapeRunnerCircleBorder getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ShapeRunnerCircleBorder();
		return SINGLETON;
	}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return ShapeRunnersImplemented.Circumference;
	}

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action, boolean shouldPerformEarlyStops) {
//		return shouldPerformEarlyStops ? runShapeImpl_WithEarlyStop(shape, action)
//				: runShapeImpl_NoEarlyStop(shape, action);
//	}
//
//	protected boolean runShapeImpl_NoEarlyStop(AbstractShape2D shape, PointConsumer action) {
		int xcentre, ycentre, r, r2, dx, dy, diameter, xc1, xc2, xc3, xc4, yc1, /* yc2, yc3, */ yc4/* , dy2 */;
//		double r2;
		ShapeCircle sc;
		Point point;
		sc = (ShapeCircle) shape;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		diameter = sc.getDiameter();
		r = diameter >> 1;
		xcentre = sc.getXCenter();
		ycentre = sc.getYCenter();
		point = new Point(xcentre, ycentre);
		if (r < 1)
			return false;
		switch (diameter) {
		case 1: {
			if (!action.canContinue())
				return true;
			action.accept(point);
			return true;
		}
		case 2: {
			if (shouldPerformEarlyStops) {
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
			point.y--;
			point.x--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
			point.y += 2;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
			point.y--;
			if (shouldPerformEarlyStops) {
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x += 2;
				if (!action.canContinue())
					return true;
				action.accept(point);
			} else {
				action.accept(point);
				point.x += 2;
				action.accept(point);
			}
			return true;
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
				point.y--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x += 3;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.y--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x -= 3;
				if (!action.canContinue())
					return true;
				action.accept(point);
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
				point.y--;
				action.accept(point);
				point.x += 3;
				action.accept(point);
				point.y--;
				action.accept(point);
				point.x -= 3;
				action.accept(point);
			}
			return true;
		}
		case 5: {
			point.y -= 2;
			point.x--;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
			point.y += 4;
			ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
			point.x--;
			point.y--;
			if (shouldPerformEarlyStops) {
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x += 4;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.y--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x -= 4;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.y--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x += 4;
				if (!action.canContinue())
					return true;
				action.accept(point);
			} else {
				action.accept(point);
				point.x += 4;
				action.accept(point);
				point.y--;
				action.accept(point);
				point.x -= 4;
				action.accept(point);
				point.y--;
				action.accept(point);
				point.x += 4;
				action.accept(point);
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
				point.x++;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x -= 3;
				if (!action.canContinue())
					return true;
				action.accept(point);//
				point.y++;
				point.x--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x += 5;
				if (!action.canContinue())
					return true;
				action.accept(point);//
				point.y++;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x -= 5;
				if (!action.canContinue())
					return true;
				action.accept(point);//
				point.y++;
				point.x++;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x += 3;
				if (!action.canContinue())
					return true;
				action.accept(point);//
				point.y++;
				point.x--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x--;
				if (!action.canContinue())
					return true;
				action.accept(point);
				return true;
			} else {
				point.y -= 2;
				action.accept(point);
				point.x++;
				action.accept(point);//
				point.y++;
				point.x++;
				action.accept(point);
				point.x -= 3;
				action.accept(point);//
				point.y++;
				point.x--;
				action.accept(point);
				point.x += 5;
				action.accept(point);//
				point.y++;
				action.accept(point);
				point.x -= 5;
				action.accept(point);//
				point.y++;
				point.x++;
				action.accept(point);
				point.x += 3;
				action.accept(point);//
				point.y++;
				point.x--;
				action.accept(point);
				point.x--;
				action.accept(point);
			}
			return true;
		}
		case (11): {
			int[] deltaFirstQuadrant;
			deltaFirstQuadrant = new int[] { 1, 2, 3, 4, 4, 5 };
			runQuadrant_OddDiameter(point, action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
			return true;
		}
		case (12): {
			int[] deltaFirstQuadrant;
			deltaFirstQuadrant = new int[] { 0, 1, 2, 3, 4, 4, 5, 5 };
			runQuadrant_EvenDiameter(point, action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
			return true;
		}
		case (13): {
			int[] deltaFirstQuadrant;
			deltaFirstQuadrant = new int[] { 1, 2, 3, 4, 5, 5, 6 };
			runQuadrant_OddDiameter(point, action, xcentre, ycentre, deltaFirstQuadrant, shouldPerformEarlyStops);
			return true;
		}
		case (14): {
			int[] deltaFirstQuadrant;
			deltaFirstQuadrant = new int[] { 0, 1, 2, 3, 4, 5, 5, 6, 6 };
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

		if ((diameter & 0x1) == 0) {
			xc1 = xc4 = xcentre;
			xc2 = xc3 = xcentre + 1;
			yc1 = /* yc2 = */ ycentre + 1;
			/* yc3 = */ yc4 = ycentre;
			r--;
		} else {
			xc1 = xc2 = xc3 = xc4 = xcentre;
			yc1 = /* yc2 = yc3 = */ yc4 = ycentre;
		}
		r2 = r * r;
//		r2 = diameter / 2.0;
//		r2 *= r2;
		dy = 0;
		dx = r;
		if (shouldPerformEarlyStops) {
			while (dy <= dx) {
				point.x = xc1 - dx;
				point.y = yc1 + dy;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x = xc2 + dx;
				if (!action.canContinue())
					return true;
				action.accept(point);
				//
				point.y = yc4 - dy;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x = xc1 - dx;
				if (!action.canContinue())
					return true;
				action.accept(point);
				// --
				point.x = xc1 - dy;
				point.y = yc1 + dx;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x = xc2 + dy;
				if (!action.canContinue())
					return true;
				action.accept(point);
				//
				point.x = xc4 - dy;
				point.y = yc4 - dx;
				if (!action.canContinue())
					return true;
				action.accept(point);
				point.x = xc3 + dy;
				if (!action.canContinue())
					return true;
				action.accept(point);
				dy++;
				dx = (int) Math.round(Math.sqrt(r2 - dy * dy));
			}
		} else {
			while (dy <= dx) {
				point.x = xc1 - dx;
				point.y = yc1 + dy;
				action.accept(point);
				point.x = xc2 + dx;
				action.accept(point);
				//
				point.y = yc4 - dy;
				action.accept(point);
				point.x = xc1 - dx;
				action.accept(point);
//--
				point.x = xc1 - dy;
				point.y = yc1 + dx;
				action.accept(point);
				point.x = xc2 + dy;
				action.accept(point);
//
				point.x = xc4 - dy;
				point.y = yc4 - dx;
				action.accept(point);
				point.x = xc3 + dy;
				action.accept(point);
				dy++;
				dx = (int) Math.round(Math.sqrt(r2 - dy * dy));
			}
		}
		return true;
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
		int i, len, dy, dx, yc_1;
		i = -1;
		yc_1 = ycentre + 1;
		len = deltaFirstQuadrant.length;
		if (shouldPerformEarlyStops)
			while (--len >= 0) {
				dx = deltaFirstQuadrant[len];
				dy = deltaFirstQuadrant[++i];
				p.x = xcentre + dx + 1;
				p.y = yc_1 + dy;
				if (!action.canContinue())
					return;
				action.accept(p);
				p.y = ycentre - dy;
				if (!action.canContinue())
					return;
				action.accept(p);
				p.x = xcentre - dx;
				p.y = yc_1 + dy;
				if (!action.canContinue())
					return;
				action.accept(p);
				p.y = ycentre - dy;
				if (!action.canContinue())
					return;
				action.accept(p);
			}
		else
			while (--len >= 0) {
				dx = deltaFirstQuadrant[len];
				dy = deltaFirstQuadrant[++i];
				p.x = xcentre + dx + 1;
				p.y = yc_1 + dy;
				action.accept(p);
				p.y = ycentre - dy;
				action.accept(p);
				p.x = xcentre - dx;
				p.y = yc_1 + dy;
				action.accept(p);
				p.y = ycentre - dy;
				action.accept(p);
			}
	}

	protected static void runQuadrant_OddDiameter(Point p, PointConsumer action, int xcentre, int ycentre,
			int[] deltaFirstQuadrant, boolean shouldPerformEarlyStops) {
		int i, len, dy, dx;
		i = -1;
		len = deltaFirstQuadrant.length;
//		cardinal points
		dy = deltaFirstQuadrant[len - 1];
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
			while (--len >= 0) {
				dx = deltaFirstQuadrant[len];
				dy = deltaFirstQuadrant[++i];
				p.x = xcentre + dx;
				p.y = ycentre + dy;
				if (!action.canContinue())
					return;
				action.accept(p);
				p.y = ycentre - dy;
				if (!action.canContinue())
					return;
				action.accept(p);
				p.x = xcentre - dx;
				p.y = ycentre + dy;
				if (!action.canContinue())
					return;
				action.accept(p);
				p.y = ycentre - dy;
				if (!action.canContinue())
					return;
				action.accept(p);
			}
		} else {
			p.x = xcentre;
			p.y = ycentre + dy;
			action.accept(p);
			p.y = ycentre - dy;
			action.accept(p);
			p.y = ycentre;
			p.x = xcentre + dy;
			action.accept(p);
			p.x = xcentre - dy;
			action.accept(p);
			while (--len >= 0) {
				dx = deltaFirstQuadrant[len];
				dy = deltaFirstQuadrant[++i];
				p.x = xcentre + dx;
				p.y = ycentre + dy;
				action.accept(p);
				p.y = ycentre - dy;
				action.accept(p);
				p.x = xcentre - dx;
				p.y = ycentre + dy;
				action.accept(p);
				p.y = ycentre - dy;
				action.accept(p);
			}
		}
	}
//
//	protected boolean runShapeImpl_WithEarlyStop(AbstractShape2D shape, PointConsumer action,
//			boolean shouldPerformEarlyStops) {
//		int xcentre, ycentre, r, r2, dx, dy, diameter, xc1, xc2, xc3, xc4, yc1, /* yc2, yc3, */ yc4/* , dy2 */;
////	double r2;
//		ShapeCircle sc;
//		Point point;
//		sc = (ShapeCircle) shape;
//		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
//			return false;
//		diameter = sc.getDiameter();
//		r = diameter >> 1;
//		xcentre = sc.getXCenter();
//		ycentre = sc.getYCenter();
//		point = new Point(xcentre, ycentre);
//		if (r < 1)
//			return false;
//		switch (diameter) {
//		case 1: {
//			if (action.canContinue())
//				return true;
//			action.accept(point);
//			return true;
//		}
//		case 2: {
//			action.accept(point);
//			point.x++;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.y++;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			return true;
//		}
//		case 3: {
//			point.y--;
//			point.x--;
//			ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
//			point.y += 2;
//			ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
//			point.y--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x += 2;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			return true;
//		}
//		case 4: {
//			point.y--;
//			action.accept(point);
//			point.x++;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.y += 3;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x--;
//			point.y--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x += 3;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.y--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x -= 3;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			return true;
//		}
//		case 5: {
//			point.y -= 2;
//			point.x--;
//			ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
//			point.y += 4;
//			ShapeRunnerLine.runHorizontalSpan(action, point, 3, shouldPerformEarlyStops);
//			point.x--;
//			point.y--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x += 4;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.y--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x -= 4;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.y--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x += 4;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			return true;
//		}
//		case 6: {
//			point.y -= 2;
//			action.accept(point);
//			point.x++;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);//
//			point.y++;
//			point.x++;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x -= 3;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);//
//			point.y++;
//			point.x--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x += 5;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);//
//			point.y++;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x -= 5;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);//
//			point.y++;
//			point.x++;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x += 3;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);//
//			point.y++;
//			point.x--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x--;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			return true;
//		}
//		default: {
//		}
//		}
//		/*
//		 * perform eight circle's slice, thanks to its regularity. Start from lowest y
//		 * (the south) and go up.
//		 */
//		if ((diameter & 0x1) == 0) {
//			xc1 = xc4 = xcentre;
//			xc2 = xc3 = xcentre + 1;
//			yc1 = /* yc2 = */ ycentre + 1;
//			/* yc3 = */ yc4 = ycentre;
//			r--;
//		} else {
//			xc1 = xc2 = xc3 = xc4 = xcentre;
//			yc1 = /* yc2 = yc3 = */ yc4 = ycentre;
//		}
////		r2 = diameter / 2.0;
////		r2 *= r2;
//		r2 = r * r;
//		dy = 0;
//		dx = r;
//		while (dy <= dx) {
//			point.x = xc1 - dx;
//			point.y = yc1 + dy;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x = xc2 + dx;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			//
//			point.y = yc4 - dy;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x = xc1 - dx;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
////--
//			point.x = xc1 - dy;
//			point.y = yc1 + dx;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x = xc2 + dy;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
////
//			point.x = xc4 - dy;
//			point.y = yc4 - dx;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			point.x = xc3 + dy;
//			if (!action.canContinue())
//				return true;
//			action.accept(point);
//			dy++;
//			dx = (int) Math.round(Math.sqrt(r2 - dy * dy));
//		}
//		return true;
//	}

//	protected boolean runShapeImpl_V2_NOT_WORKING(AbstractShape2D shape, PointConsumer action) {
//		int xcentre, ycentre, r, ///
//				x, y, x1, y1, //
//				dx, diameter, quarterDiameter;
//		double radius; // , ydregressive, ; // rs = radius square
//		ShapeCircle sc;
//		Point point;
//		sc = (ShapeCircle) shape;
//		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
//			return false;
//		quarterDiameter = ((diameter = sc.getDiameter()) >> 2); // divided by four: the amount of pixel to repeat
////		rs = (ydregressive = radius = diameter / 2.0) * radius;
//		r = diameter >> 1;
////		toCompensateEvenDiameter = ((diameter & 0x1) == 0) ? 0.5 : 0.0;
//		xcentre = sc.getXCenter();
//		ycentre = sc.getYCenter();
//		point = new Point(xcentre, ycentre);
//		action.accept(point);
//		if (r < 1)
//			return false;
//		switch (diameter) {
//		default: {
	// LOL ELIMINATED
//		}
//		}
//		/*
//		 * perform eight circle's slice, thanks to its regularity. Start from lowest y
//		 * (the south) and go up.
//		 */
//		if ((diameter & 0x1) == 0) {
////			int dxprev_i;
//			int dxnext, dxprev, d2; // rs,ys,ydecreasing
//			double rs, ydecreasing;
//			ydecreasing = radius = r - 0.5;
////			rs= (ydregressive = radius = diameter / 2.0) * radius;
////			quarterDiameter--;
////			rs -= (2.0 * (--radius) + 1);
//
////			r--;
//			dxprev = 0;
////			ys = rs = r * r;
//			rs = radius * radius;
////			dxprev_i = 0;
//			System.out.println("rs: " + rs + ", radius: " + radius);
//			while (quarterDiameter-- > 0 && action.canContinue()) {
////				ys -= 1 + ((--ydecreasing) << 1);
//				dxnext = (int) Math.round(Math.sqrt(rs - ((--ydecreasing) * ydecreasing)));
//				System.out.println("dxnext: " + dxnext + ", yd: " + ydecreasing + ", s: "
//						+ Math.sqrt(rs - (ydecreasing * ydecreasing)));
//// dxprev = is considered for the bottom-right corner
//				dx = // Math.round
//						(dxnext - (dxprev));
//
//				y1 = ycentre + r;
//				y = 1 + (ycentre - r);
//				x1 = xcentre + dxprev;
//				x = (xcentre - (dxnext - 1)) + 1;
//				if (dx <= 1) {
////					x--;
//					dx = 1;
//				}
//				point.x = x1;
//				point.y = y1;
//				// if (action.canContinue())
//				ShapeRunnerLine.runHorizontalSpan(action, point, dx);
//				point.x = x;
//				if (action.canContinue())
//					ShapeRunnerLine.runHorizontalSpan(action, point, dx);
//				point.y = y;
//				if (action.canContinue())
//					ShapeRunnerLine.runHorizontalSpan(action, point, dx);
//				point.x = x1;
//				if (action.canContinue())
//					ShapeRunnerLine.runHorizontalSpan(action, point, dx);
//// specular
//				x1 = xcentre + r;
//				x = 1 + (xcentre - r);
//				y1 = ycentre + dxprev;
////				y = ycentre - (dxnext - 2);
//				y = (ycentre - (dxnext - 1)) + 1;
//
//				point.x = x1;
//				point.y = y1;
//				if (action.canContinue())
//					ShapeRunnerLine.runVerticalSpan(action, point, dx);
//				point.x = x;
//				if (action.canContinue())
//					ShapeRunnerLine.runVerticalSpan(action, point, dx);
//				point.y = y;
//				if (action.canContinue())
//					ShapeRunnerLine.runVerticalSpan(action, point, dx);
//				point.x = x1;
//				if (action.canContinue())
//					ShapeRunnerLine.runVerticalSpan(action, point, dx);
//				//
//				dxprev = dxnext;
////				dxprev_i = Math.round(dxprev);
//				r--;// = ydecreasing;
//			}
//		} else {
//			int dxnext, dxprev;
//			double xnew, rs;
//			rs = (radius = diameter / 2.0) * radius;
//			// odd diametre
//			point.x = xcentre;
//			point.y = ycentre - r;
//			action.accept(point);
//			point.y = ycentre + r;
//			action.accept(point);
//			point.x = xcentre + r;
//			point.y = ycentre;
//			action.accept(point);
//			point.x = xcentre + r;
//			action.accept(point);
//			dxprev = 0;
//			while (quarterDiameter-- > 0 && action.canContinue()) {
//				xnew = Math.sqrt(rs - (r * r));
//				rs -= 1 + (r << 1);
//				dxnext = (int) Math.round(xnew);
//				dx = dxnext - dxprev;
//				if (dx < 1)
//					dx = 1;
//
//				y1 = ycentre + r;
//				y = 1 + (ycentre - r);
//				x1 = xcentre + dxprev;
//				x = (xcentre - (dxnext - 1)) + 1;
////				x = xcentre - dxnext;
//
//				point.x = x1;
//				point.y = y1;
//				// if (action.canContinue())
//				ShapeRunnerLine.runHorizontalSpan(action, point, dx);
//				point.x = x;
//				if (action.canContinue())
//					ShapeRunnerLine.runHorizontalSpan(action, point, dx);
//				point.y = y;
//				if (action.canContinue())
//					ShapeRunnerLine.runHorizontalSpan(action, point, dx);
//				point.x = x1;
//				if (action.canContinue())
//					ShapeRunnerLine.runHorizontalSpan(action, point, dx);
//				x1 = xcentre + r;
//				x = 1 + (xcentre - r);
//				y1 = ycentre + dxprev;
//				y = (ycentre - (dxnext - 1)) + 1;
////				y = ycentre - dxnext;
//				point.x = x1;
//				point.y = y1;
//				if (action.canContinue())
//					ShapeRunnerLine.runVerticalSpan(action, point, dx);
//				point.x = x;
//				if (action.canContinue())
//					ShapeRunnerLine.runVerticalSpan(action, point, dx);
//				point.y = y;
//				if (action.canContinue())
//					ShapeRunnerLine.runVerticalSpan(action, point, dx);
//				point.x = x;
//				if (action.canContinue())
//					ShapeRunnerLine.runVerticalSpan(action, point, dx);
//				//
//				dxprev = dxnext;
//				r--;
//			}
//		}
//		return true;
//	}
}