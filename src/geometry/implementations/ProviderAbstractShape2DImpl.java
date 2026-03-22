package geometry.implementations;

import java.awt.geom.Point2D;

import geometry.AbstractShape2D;
import geometry.AbstractShape2DFactory;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapes.ShapeCircle;
import geometry.implementations.shapes.ShapeLine;
import geometry.implementations.shapes.ShapePoint;
import geometry.implementations.shapes.ShapePolygon;
import geometry.implementations.shapes.ShapePolygonRegular;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.implementations.shapes.ShapeTriangle;

public class ProviderAbstractShape2DImpl implements AbstractShape2DFactory {
	private static ProviderAbstractShape2DImpl SINGLETON;

	public static ProviderAbstractShape2DImpl getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ProviderAbstractShape2DImpl();
		return SINGLETON;
	}

	private ProviderAbstractShape2DImpl() {
	}

	@Override
	public AbstractShape2D newEmptyShape(ShapeRunnersImplemented sri) {
		switch (sri) {
		case Arrow:
			break;
		case ArrowBorderBodySameLength:
			break;
		case Circumference:
			return new ShapeCircle(false);
		case Cone:
			break;
		case Disk:
			return new ShapeCircle(true);
		case EllipseNoRotation:
			break;
		case EllipseNoRotationBorder:
			break;
		case Line:
			return new ShapeLine();
		case Point:
			return new ShapePoint();
		case Polygon:
			return new ShapePolygon(true);
		case PolygonRegularBorder:
			return (new ShapePolygonRegular()).setFilled(false).setCornersAmounts(5);
		case PolygonBorder:
			return new ShapePolygon(false);
		case Rectangle:
			return new ShapeRectangle(true);
		case RectangleBorder:
			return new ShapeRectangle(false);
		case Triangle:
			return (new ShapeTriangle(true)).setVertexes(new Point2D.Double[] { //
					new Point2D.Double(5, 5), new Point2D.Double(2, 8), new Point2D.Double(9, 13) });
		case TriangleBorder:
			return (new ShapeTriangle(false)).setVertexes(new Point2D.Double[] { //
					new Point2D.Double(5, 5), new Point2D.Double(2, 8), new Point2D.Double(9, 13) });
		default:
			break;

		}
		return null;
	}

}
