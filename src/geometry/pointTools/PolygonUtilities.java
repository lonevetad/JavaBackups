package geometry.pointTools;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import tools.MathUtilities;
import tools.MathUtilities.PointRelativeToLine;

public final class PolygonUtilities {
	static final double PI_2 = Math.PI * 2.0;

	public static void forEachPoint(Polygon poly, Consumer<Point> action) {
		int i, len;
		int[] xx, yy;
		Point p;
		len = poly.npoints;
		i = -1;
		p = new Point();
		xx = poly.xpoints;
		yy = poly.xpoints;
		while (++i < len) {
			p.x = xx[i];
			p.y = yy[i];
			action.accept(p);
		}
	}

	public static void forEachPoint(Polygon poly, BiConsumer<Integer, Integer> action) {
		int i, len;
		int[] xx, yy;
		len = poly.npoints;
		i = -1;
		xx = poly.xpoints;
		yy = poly.xpoints;
		while (++i < len)
			action.accept(xx[i], yy[i]);
	}

	public static void forEachEdge(Polygon poly, BiConsumer<Point, Point> action) {
		int i, len, previ, px, py;
		int[] xx, yy;
		Point p1, p2;
		len = poly.npoints;
		i = -1;
		previ = len - 1;
		xx = poly.xpoints;
		yy = poly.xpoints;
		p1 = new Point(xx[previ], yy[previ]);
		p2 = new Point();
		while (++i < len) {
			p2.x = px = xx[i];
			p2.y = py = yy[i];
			previ = i;
			action.accept(p1, p2);
			p1.x = px; // xx[previ];
			p1.y = py; // yy[previ];
		}
	}

	public static void forEachEdge(Polygon poly, Consumer<Line2D> action) {
		int i, len, px, py, previ;
		int[] xx, yy;
		Line2D line;
		len = poly.npoints;
		i = -1;
		xx = poly.xpoints;
		yy = poly.xpoints;
		px = xx[previ = len - 1];
		py = yy[previ];
		line = new Line2D.Double();
		while (++i < len) {
//			line.setLine(xx[previ], yy[previ], xx[i], yy[i]);
			// knowing that parameters are left-computed, the above is an optimization
			line.setLine(px, py, px = xx[i], py = yy[i]);
//			previ = i;
			action.accept(line);
		}
	}

	public static String polygonToString(Polygon p) {
		final int len;
		final StringBuilder sb;

		len = p.npoints;
		sb = new StringBuilder(len << 4);
		sb.append("Polygon with ").append(len).append(" corners:");
		forEachPoint(p, (x, y) -> sb.append("\n- ")/* .append(i) */.append(": (x: ").append(x).append(", y: ").append(y)
				.append(')'));
		return sb.toString();
	}

	/** Copied from {@link java.awt.Polygon} because it's a beautyfull method. */
	protected static boolean contains_FromPolygonClass(Polygon p, double x, double y) {
		if (p.npoints <= 2 /* || !p.getBoundingBox().contains(x, y) */) { return false; }
		// use the ray-casting-algorithm
		int hits = 0;
		int lastx = p.xpoints[p.npoints - 1];
		int lasty = p.ypoints[p.npoints - 1];
		int curx, cury;

		// Walk the edges of the polygon
		for (int i = 0; i < p.npoints; lastx = curx, lasty = cury, i++) {
			curx = p.xpoints[i];
			cury = p.ypoints[i];

			if (cury == lasty) { continue; }

			int leftx;
			if (curx < lastx) {
				if (x >= lastx) { continue; }
				leftx = curx;
			} else {
				if (x >= curx) { continue; }
				leftx = lastx;
			}

			double test1, test2;
			if (cury < lasty) {
				if (y < cury || y >= lasty) { continue; }
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - curx;
				test2 = y - cury;
			} else {
				if (y < lasty || y >= cury) { continue; }
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - lastx;
				test2 = y - lasty;
			}
			if (test1 < (test2 / (lasty - cury) * (lastx - curx)))
				hits++;
		}
		return ((hits & 1) != 0);
	}

	// Copyright 2000 softSurfer, 2012 Dan Sunday
	// This code may be freely used and modified for any purpose
	// providing that this copyright notice is included with it.
	// SoftSurfer makes no warranty for this code, and cannot be held
	// liable for any real or imagined damage resulting from its use.
	// Users of this code must verify correctness for their application.

	// a Point is defined by its coordinates {int x, y;}
	// ===================================================================

	// ===================================================================

	// cn_PnPoly(): crossing number test for a point in a polygon
//	      Input:   P = a point,
//	               V[] = vertex points of a polygon V[n+1] with V[n]=V[0]
//	      Return:  0 = outside, 1 = inside
	// This code is patterned after [Franklin, 2000]
	static int cn_PnPoly(Point2D pointToBeTested, Point2D[] V, int n) {
		int cn = 0; // the crossing number counter

		// loop through all edges of the polygon
		for (int i = 0; i < n; i++) { // edge from V[i] to V[i+1]
			if (((V[i].getY() <= pointToBeTested.getY()) && (V[i + 1].getY() > pointToBeTested.getY())) // an upward
																										// crossing
					|| ((V[i].getY() > pointToBeTested.getY()) && (V[i + 1].getY() <= pointToBeTested.getY()))) { // a
																													// downward
																													// crossing
				// compute the actual edge-ray intersect x-coordinate
				double vt = (pointToBeTested.getY() - V[i].getY()) / (V[i + 1].getY() - V[i].getY());
				if (pointToBeTested.getX() < V[i].getX() + vt * (V[i + 1].getX() - V[i].getX())) // P.getX() < intersect
					++cn; // a valid crossing of y=P.getY() right of P.getX()
			}
		}
		return (cn & 1); // 0 if even (out), and 1 if odd (in)

	}
	// ===================================================================

	/**
	 * isPointInsideThePolygon(): winding number test for a point in a polygon <br>
	 *
	 * @param pointToBeTested            a point
	 * @param polygonAsConsecutivePoints array "V" of "n" vertex points of a
	 *                                   polygon, where <code>V[n] == V[0]</code>
	 *                                   implicitly
	 *
	 * @return: wn = the winding number's computation (=0 only when P is outside)
	 */
	public static boolean isInside(Point2D pointToBeTested, Point2D[] polygonAsConsecutivePoints) {
		int wn = 0, nextIndex, n; // the winding number counter
		n = polygonAsConsecutivePoints.length;
		// loop through all edges of the polygon
		for (int i = 0; i < n; i++) { // edge from V[i] to V[i+1]

			nextIndex = i + 1;
			if (nextIndex >= n)// no overflowing
				nextIndex = 0;
			if (polygonAsConsecutivePoints[i].getY() <= pointToBeTested.getY()) {
				// start y <= P.getY()
				if (polygonAsConsecutivePoints[nextIndex//
				].getY() > pointToBeTested.getY()) // an upward crossing
					if (MathUtilities.relationToLine(polygonAsConsecutivePoints[i],
							polygonAsConsecutivePoints[nextIndex], pointToBeTested) == PointRelativeToLine.Left)
						// P left of edge
						++wn; // have a valid up intersect
			} else { // start y > P.getY() (no test needed)
				if (polygonAsConsecutivePoints[nextIndex].getY() <= pointToBeTested.getY())
					// a downward crossing
					if (MathUtilities.relationToLine(polygonAsConsecutivePoints[i],
							polygonAsConsecutivePoints[nextIndex], pointToBeTested) == PointRelativeToLine.Right)
						// P right of edge
						--wn; // have a valid down intersect
			}
		}
		return wn == 0;
	}

	/**
	 * isPointInsideThePolygon(): winding number test for a point in a polygon <br>
	 *
	 * @param pointToBeTested a point
	 * @param polygon         array "V" of "n" vertex points of a polygon, where
	 *                        <code>V[n] == V[0]</code> implicitly
	 *
	 * @return: wn = the winding number's computation (=0 only when P is outside)
	 */
	public static boolean isInside(Point2D pointToBeTested, Polygon polygon) {
		int n;
		n = polygon.npoints;
		if (n < 3 || (!polygon.getBounds().contains(((pointToBeTested instanceof Point) ? ((Point) pointToBeTested)
				: new Point((int) pointToBeTested.getX(), (int) pointToBeTested.getY()))))//
		)
			return false;
		return isInside(pointToBeTested.getX(), pointToBeTested.getY(), polygon);
	}

	/** See {@link #isInside(Point2D,Polygon)}. */
	public static boolean isInside(double px, double py, Polygon polygon) {
		int wn = 0, n, x, y; // the winding number counter
		double prevx, prevy;
		int[] xx, yy;
		n = polygon.npoints;
		if (n < 3 || (!MathUtilities.isInside(polygon.getBounds(), (int) px, (int) py)))
			return false;
		xx = polygon.xpoints;
		yy = polygon.ypoints;
		// loop through all edges of the polygon
		// pointToBeTested used as previous
		prevx = xx[--n];
		prevy = yy[n];
		for (int i = 0; i < n; i++) { // edge from V[i] to V[i+1]
			x = xx[i];
			if ((y = yy[i]) == py && x == px)
				return true;
			if (y <= py) { // start y <= P.getY()
				if ((prevy >= py)// an upward crossing
//						&& MathUtilities.relationToLine(x, y, prevx, prevy, px, py) == PointRelativeToLine.Left)
						&& MathUtilities.relationToLine(x, y, prevx, prevy, px, py) != PointRelativeToLine.Right)
					// P left of edge
					++wn; // have a valid up intersect
			} else { // start y > P.getY() (no test needed)
				if ((prevy <= py)// a downward crossing
//						&& MathUtilities.relationToLine(x, y, prevx, prevy, px, py) == PointRelativeToLine.Right)
						&& MathUtilities.relationToLine(x, y, prevx, prevy, px, py) != PointRelativeToLine.Left)
					// P right of edge
					--wn; // have a valid down intersect
			}
			prevx = x;
			prevx = y;
		}
		return wn != 0;
	}

	public static boolean isConvex(Polygon polygon) {
		int i, pointsCalculated, n, oldx, oldy, newx, newy;
		int[] xx, yy;
		double angle, olddir, newdir, anglesum, orientation;
		n = polygon.npoints;
		if (n < 3)
			return false;
		if (n == 3)
			return true;
		xx = polygon.xpoints;
		yy = polygon.ypoints;
		oldx = xx[n - 3];
		oldy = yy[n - 3];
		newx = xx[n - 2];
		newy = yy[n - 2];
		newdir = Math.atan2(newy - oldy, newx - oldx);
		anglesum = 0.0;
		orientation = 1.0;
		i = -1;
		pointsCalculated = 0;
		while (++i < n) {
			oldx = newx;
			oldy = newy;
			olddir = newdir;
			newx = xx[i];
			newy = yy[i];
			if (oldx != newx || oldy != newy) { // ignore overlapping points
				newdir = Math.atan2(newy - oldy, newx - oldx);
				angle = newdir - olddir;
				if (angle <= -Math.PI)
					angle += PI_2;
				else if (angle > Math.PI)
					angle -= PI_2;
				if (pointsCalculated++ == 0) {
					if (angle == 0.0)
						return false;
					orientation = angle > 0.0 ? 1.0 : -1.0;
				} else {
					if (orientation * angle <= 0.0)
						return false;
				}
				anglesum += angle;
			}
		}
		return Math.abs(Math.round(anglesum / PI_2)) == 1.0;
	}

	@Deprecated
	public static double areaPolygon2D(Polygon p) {
		int n, i, j, k; // indices
		double area;
		int[] xx, yy;
		n = p.npoints;
		if (n < 3)
			return 0; // a degenerate polygon
		area = 0;
		n--;
		xx = p.xpoints;
		yy = p.ypoints;
		i = 1;
		j = 2;
		k = 0;
		while (i < n)
			area += xx[i++] * (yy[j++] - yy[k++]);
		area += xx[n] * (yy[1] - yy[n - 1]); // wrap-around term
		return area / 2.0;
	}
}