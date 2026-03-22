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
import tools.Comparators;
import tools.MathUtilities;

public class ShapeRunnerRectangleFilled extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = 3565885292245201000L;
	public static ShapeRunnerRectangleFilled SINGLETON;

	public static ShapeRunnerRectangleFilled getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ShapeRunnerRectangleFilled();
		return SINGLETON;
	}

	private ShapeRunnerRectangleFilled() {}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() { return ShapeRunnersImplemented.Rectangle; }

	//

	//

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action, boolean shouldPerformEarlyStops) {
		int[] xx, yy;
		double angDeg;
		ShapeRectangle sr;
//		Point corner;
		Polygon poly;
		if (shape == null || action == null || shape.getShapeImplementing() != this.getShapeRunnersImplemented())
			return false;
		sr = (ShapeRectangle) shape;
		angDeg = sr.getAngleRotation();
//		corner = new Point();
		if (angDeg == 0.0 || angDeg == 180.0) {
//			runRectangleNonRotated(shape, action);
			runRectangleNonRotated(action, shape.getXCenter(), shape.getYCenter(), shape.getWidth(), shape.getHeight(),
					shouldPerformEarlyStops);
		} else if (angDeg == 90.0 || angDeg == 270.0) {
			runRectangleNonRotated(action, shape.getXCenter(), shape.getYCenter(), shape.getHeight(), shape.getWidth(),
					shouldPerformEarlyStops);
		} else {
			Point2D[] c;
			poly = sr.toPolygon();
			xx = poly.xpoints;
			yy = poly.ypoints;
			c = new Point2D[] { new Point2D.Double(xx[0], yy[0]), new Point2D.Double(xx[1], yy[1]),
					new Point2D.Double(xx[2], yy[2]), new Point2D.Double(xx[3], yy[3]), };
			runRectangleRotated(action, c, shouldPerformEarlyStops);
		}
		return true;
	}

	public static void runRectangleNonRotated(AbstractShape2D shape, PointConsumer action,
			boolean shouldPerformEarlyStops) {
		if (shape == null || action == null)
			return;
		runRectangleNonRotated(action, shape.getXCenter(), shape.getYCenter(), shape.getWidth(), shape.getHeight(),
				shouldPerformEarlyStops);
	}

	public static void runRectangleNonRotated(PointConsumer action, int xCentre, int yCentre, int width, int height,
			boolean shouldPerformEarlyStops) {
		int x;
		Point p;
		if (width <= 0 || height <= 0 || action == null)
			return;
		p = new Point(x = xCentre - (width >> 1), yCentre - (height >> 1));
		while (height-- > 0 && action.canContinue()) {
			ShapeRunnerLine.runHorizontalSpan(action, p, width, shouldPerformEarlyStops);
			p.x = x;
			p.y++;
		}
	}

	/**
	 * Run the action over an area described by the given array of corners, assuming
	 * that this array has at least four (4) elements describing a rectangular area.
	 * <br>
	 * To be honest, it's able to run any four-sided shape having no vertical
	 * side(s). <br>
	 * BEWARE: it modify the original array !
	 */
	public static void runRectangleRotated(PointConsumer action, Point2D[] corners, boolean shouldPerformEarlyStops) {
		boolean canDoLine;
		int x, y, lasty, y2, y3, widthCache;
		double slopeLeft, slopeRight, ql, qr; /* slope12, slope13, slope24, slope34, */
		Point2D p1, p2, p3, p4; // , c[]; // == top, left, right, bottom
		Point p;
		if (action == null || corners == null || corners.length < 4)
			return;
		// 1) ordina i vertici per y decrescente
		// 2) ottieni il primo, che sarà il punto di partenza, e l'ultimo, cioè quello
		// di arrivo
		// 3) dei due rimanenti, capisco qual e' quello a sinistra e quello a destra
		// l'algoritmo si divide poi in 3 parti: dal primo al secondo, dal secondo al
		// terzo e dal terzo all'ultimo
		// 4) quindi si discendono i segmenti "a sinstra", calcolando6 i coefficienti
		// angolari
//		c = new Point2D[] { corners[0], corners[1], corners[2], corners[3] }
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
		{
			double p1y, p1x;
			p = new Point((int) Math.round(p1.getX()), y = (int) Math.round(p1y = p1.getY()));
			action.accept(p);
			slopeLeft = MathUtilities.slope(p1, p2);
			slopeRight = MathUtilities.slope(p1, p3);
			ql = p1y - slopeLeft * (p1x = p1.getX());
			qr = p1y - slopeRight * p1x;
		}
		lasty = (int) Math.round(p4.getY());
		y2 = (int) Math.round(p2.getY());
		y3 = (int) Math.round(p3.getY());
		widthCache = -1;
		x = (int) Math.round(p1.getX());
		if (shouldPerformEarlyStops) {
			canDoLine = true;
			while (++y < lasty && action.canContinue()) {
				if (y == y2) {
					slopeLeft = MathUtilities.slope(p2, p4);
					ql = p2.getY() - slopeLeft * p2.getX();
					p.x = x;
					p.y = y;
					widthCache = (int) Math.round(p3.getX()) - x;
					ShapeRunnerLine.runHorizontalSpan(action, p, //
							widthCache, shouldPerformEarlyStops);
					canDoLine = false;
				}
				if (y == y3) {
					slopeRight = MathUtilities.slope(p3, p4);
					qr = p3.getY() - slopeRight * p3.getX();
//					widthCache = -1;
					x = (int) Math.round(p2.getX());
					p.x = x;
					p.y = y;
					widthCache = (int) Math.round(p1.getX()) - x;
					ShapeRunnerLine.runHorizontalSpan(action, p, //
							widthCache, shouldPerformEarlyStops);
					canDoLine = false;
				}
				if (canDoLine) {
					x = (int) Math.round((y - ql) / slopeLeft);
					p.y = y;
					p.x = x;
					if (slopeRight == Double.POSITIVE_INFINITY || slopeRight == Double.NEGATIVE_INFINITY) {
						if (widthCache < 0) { widthCache = (int) Math.round(p3.getX() - x); }
						ShapeRunnerLine.runHorizontalSpan(action, p, //
								widthCache, shouldPerformEarlyStops);
					} else
						ShapeRunnerLine.runHorizontalSpan(action, p, //
								((int) Math.round((y - qr) / slopeRight)) - x, shouldPerformEarlyStops);
				} else
					canDoLine = true;
			}
			if (action.canContinue()) {
				p.setLocation((int) Math.round(p4.getX()), lasty);
				action.accept(p);
			}
		} else {
			while (++y < lasty) {
				if (y == y2) {
					slopeLeft = MathUtilities.slope(p2, p4);
					ql = p2.getY() - slopeLeft * p2.getX();
				}
				if (y == y3) {
					slopeRight = MathUtilities.slope(p3, p4);
					qr = p3.getY() - slopeRight * p3.getX();
				}
				p.y = y;
				p.x = x = (int) Math.round((y - ql) / slopeLeft);
				ShapeRunnerLine.runHorizontalSpan(action, p, //
						((int) Math.round((y - qr) / slopeRight)) - x, false);
			}
			p.setLocation((int) Math.round(p4.getX()), lasty);
			action.accept(p);
		}
	}

	/**
	 * Run the action over an area described by the given array of corners, assuming
	 * that this array has at least four (4) elements describing a rectangular area.
	 * <br>
	 * To be honest, it's able to run any four-sided shape having no vertical
	 * side(s). <br>
	 * BEWARE: it modify the original array !
	 */

	/**
	 * Run a rotated rectangular area, with angle expressed in degrees.
	 */
	public static void runRectangleRotated(PointConsumer action, int xCentre, int yCentre, int width, int height,
			double angDeg) {
		ShapeRectangle sr;
		if (action == null)
			return;
		sr = new ShapeRectangle(angDeg, xCentre, yCentre, true, width, height);
		getInstance().runShape(sr, action);
	}
}