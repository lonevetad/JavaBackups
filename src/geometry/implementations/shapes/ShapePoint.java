package geometry.implementations.shapes;

import java.awt.Polygon;

import geometry.AbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.subHierarchy.AbstractShapeImpl;

public class ShapePoint extends AbstractShapeImpl {
	private static final long serialVersionUID = -1545584050L;

	public ShapePoint() { super(ShapeRunnersImplemented.Point); }

	public ShapePoint(ShapePoint s) { super(s); }

	public ShapePoint(int x, int y) { super(ShapeRunnersImplemented.Point, 0.0, x, y); }

	//

	@Override
	public boolean isRegular() { return true; }

	@Override
	public int getRadius() { return 0; }

	@Override
	public int getDiameter() { return 0; }

	@Override
	public AbstractShape2D setRadius(int radius) { return this; }

	@Override
	public AbstractShape2D setDiameter(int radius) { return this; }

	@Override
	public int getCornersAmounts() { return 0; }

	@Override
	public AbstractShape2D toBorder() { return this; }

	@Override
	public boolean contains(int x, int y) { return this.center.x == x && this.center.y == y; }

	@Override
	public Polygon toPolygon() {
		if (polygonCache != null)
			return polygonCache;
		return polygonCache = new Polygon(new int[] { center.x }, new int[] { center.y }, 1);
	}

	@Override
	public boolean equals(Object o) {
		ShapePoint sp;
		if (o == null || (!(o instanceof ShapePoint)))
			return false;
		sp = (ShapePoint) o;
		return center.x == sp.center.x && center.y == sp.center.y;
	}

	@Override
	public AbstractShape2D clone() { return new ShapePoint(this); }

	@Override
	public AbstractShape2D setCornersAmounts(int cornersAmounts) { return this; }

}