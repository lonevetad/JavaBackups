package geometry;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.List;

import geometry.implementations.intersectors.CircleToCircleIntersectionDetector;
import geometry.implementations.intersectors.PointToCircleIntersector;
import geometry.implementations.intersectors.PointToLineIntersector;
import geometry.implementations.intersectors.PointToPointIntersectionDetector;
import geometry.implementations.intersectors.PolygonToPolygonIntersection;
import geometry.implementations.intersectors.PolygonToRectangleIntersection;
import geometry.implementations.intersectors.RectangleToCircleIntersectionDetector;
import geometry.implementations.intersectors.RectangleToRectangleIntersection;

/**
 * Provides instances of {@link ShapesIntersectionDetector} to manage pair of
 * {@link AbstractShape2D} are intersecting. It's also a
 * {@link ShapesIntersectionDetector}, so it can behave as a multi-shape
 * detector.
 */
public class ProviderShapesIntersectionDetector implements ShapesIntersectionDetector {
	private static final long serialVersionUID = -40472961872864200L;
	private static ProviderShapesIntersectionDetector singleton;

	public static final ProviderShapesIntersectionDetector getInstance() {
		if (singleton == null) {
			synchronized (ProviderShapesIntersectionDetector.class) {
				singleton = new ProviderShapesIntersectionDetector();
			}
		}
		return singleton;
	}

	private ProviderShapesIntersectionDetector() {
		int len;
		len = ShapesImplemented.values().length;
		System.out.println("ShapesImplemented.values().length " + len);
		this.intersectionDetectorsImplemented =
//				MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
//				PairShape.COMPARATOR_PAIR_SHAPE);
				new ShapesIntersectionDetector[len][len];

		registerIntersectionDetectors();
	}

	/** used as multi-purpose and for cache */
	protected ShapesIntersectionDetector polygonsIntersectionManager;
	protected // Map<PairShape, ShapesIntersectionDetector>
	ShapesIntersectionDetector[][] intersectionDetectorsImplemented;

	protected void registerIntersectionDetectors() {
//		int i;
		ShapesImplemented[] vals;
		ShapesIntersectionDetector[] row;
//TODO
//		intersectionDetectorsImplemented.put(new PairShape(ShapesImplemented.Point, ShapesImplemented.Point),
//				new PointToPointIntersectionDetector());

		// TODO use a matrix of ShapesIntersectionDetector indexed by
		// ShapesImplemented's ordinal so the Polygon and Point could be well managed
		// also, PairShape orders its arguments, so the matrix would be a triangle (the
		// last row will be a 1-element: ItselfToItself) (reminding to PolygonToPolygon
		// example)
		this.polygonsIntersectionManager = new PolygonToPolygonIntersection();
//		i = 0;
		vals = ShapesImplemented.values();
		for (ShapesImplemented si : vals) {
			if (si == ShapesImplemented.Polygon) {
				this.intersectionDetectorsImplemented[ShapesImplemented.Polygon
						.ordinal()] = new ShapesIntersectionDetector[] { this.polygonsIntersectionManager };
//				i++;
			} /*
				 * else { this.intersectionDetectorsImplemented[si.ordinal()] = new
				 * ShapesIntersectionDetector[vals.length - i++]; }
				 */
		}
		// point
		row = this.intersectionDetectorsImplemented[ShapesImplemented.Point.ordinal()];
		row[ShapesImplemented.Point.ordinal()] = new PointToPointIntersectionDetector();
		row[ShapesImplemented.Line.ordinal()] = new PointToLineIntersector();
		row[ShapesImplemented.Circle.ordinal()] = new PointToCircleIntersector();

		// rectangle
		row = this.intersectionDetectorsImplemented[ShapesImplemented.Rectangle.ordinal()];
		row[ShapesImplemented.Rectangle.ordinal()] = new RectangleToRectangleIntersection();
		row[ShapesImplemented.Polygon.ordinal()] = new PolygonToRectangleIntersection();
		row[ShapesImplemented.Circle.ordinal()] = new RectangleToCircleIntersectionDetector();

		// Line
		row = this.intersectionDetectorsImplemented[ShapesImplemented.Line.ordinal()];
		row[ShapesImplemented.Point.ordinal()] = new PointToLineIntersector();
		row[ShapesImplemented.Rectangle.ordinal()] = new RectangleToRectangleIntersection();
		row[ShapesImplemented.Polygon.ordinal()] = new PolygonToRectangleIntersection();
		row[ShapesImplemented.Circle.ordinal()] = new RectangleToCircleIntersectionDetector();

		// Circle
		row = this.intersectionDetectorsImplemented[ShapesImplemented.Circle.ordinal()];
		row[ShapesImplemented.Point.ordinal()] = new PointToLineIntersector();
//		row[ShapesImplemented.Rectangle.ordinal()] = new RectangleToRectangleIntersection();
//		row[ShapesImplemented.Polygon.ordinal()] = new PolygonToRectangleIntersection();
		row[ShapesImplemented.Circle.ordinal()] = new CircleToCircleIntersectionDetector();

	}

	public ShapesIntersectionDetector getShapesIntersectionDetector(AbstractShape2D si1, AbstractShape2D si2) {
		return getShapesIntersectionDetector(si1.getShapeImplementing().getShapeAbstract(),
				si2.getShapeImplementing().getShapeAbstract());
	}

	public ShapesIntersectionDetector getShapesIntersectionDetector(ShapesImplemented si1, ShapesImplemented si2) {
		PairShape ps;
		ShapesIntersectionDetector sim; // ..menthal .. ah ah
		ShapesIntersectionDetector[] lowerClass;
//		if (si1 == ShapesImplemented.Polygon || si2 == ShapesImplemented.Polygon)
//			sim = this.polygonsIntersectionManager;
		ps = new PairShape(si1, si2);
		lowerClass = this.intersectionDetectorsImplemented[ps.si1.ordinal()];
		if (lowerClass == null)
			// the first doesn't even exists !
			return this.polygonsIntersectionManager;

//		sim = this.intersectionDetectorsImplemented.get(ps);
		sim = lowerClass[ps.si2.ordinal()];
		if (sim == null)
			// no specific pair, give the "si1-to-Polygon" 's ones
			sim = lowerClass[ShapesImplemented.Polygon.ordinal()];

		if (sim == null)
			// no way, no more specific method found: just use Polygon-to-Polygon one
			sim = this.polygonsIntersectionManager;
		return sim;
	}

	//

	//

	@Override
	public boolean areIntersecting(AbstractShape2D s1, AbstractShape2D s2) {
		ShapesIntersectionDetector sim;
		final ShapesImplemented si1, si2;
		if (s1 == s2)
			return true;
		if (s1 == null || s2 == null)
			return false;
		si1 = s1.getShapeImplementing().getShapeAbstract();
		si2 = s2.getShapeImplementing().getShapeAbstract();
		sim = this.getShapesIntersectionDetector(si1, si2);
		if (sim == null)
			throw new UnsupportedOperationException(
					"Not yet implemented the intersection manager for this pair of shapes: " + si1.name() + " - "
							+ si2.name());
		return sim.areIntersecting(s1, s2);
	}

	//

	//

	protected static class PairShape {
		protected static final Comparator<PairShape> COMPARATOR_PAIR_SHAPE = (p1, p2) -> {
			int c;
			if (p1 == p2)
				return 0;
			if (p1 == null)
				return -1;
			if (p2 == null)
				return 1;
			c = Integer.compare(p1.si1.ordinal(), p2.si1.ordinal());
			if (c != 0)
				return c;
			return Integer.compare(p1.si2.ordinal(), p2.si2.ordinal());
		};

		protected PairShape(ShapesImplemented si1, ShapesImplemented si2) {
//			si1 = s1.getShapeImplementing().getShapeAbstract();
//			si2 = s2.getShapeImplementing().getShapeAbstract();
			if (si1.ordinal() > si2.ordinal()) {
				this.si1 = si1;
				this.si2 = si2;
			} else {
				// the opposite
				this.si1 = si2;
				this.si2 = si1;
			}
		}

		protected final ShapesImplemented si1, si2;
	}

	@Override
	public List<Point2D> computeIntersectionPoints(AbstractShape2D s1, AbstractShape2D s2) {
		// TODO Auto-generated method stub
		return null;
	}
}