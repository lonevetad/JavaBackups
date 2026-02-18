package geometry;

import java.io.Serializable;

/** More precise version of {@link ShapesImplemented} */
public enum ShapeRunnersImplemented implements Serializable {
	PolygonRegularBorder(ShapesImplemented.Polygon), PolygonBorder(ShapesImplemented.Polygon),
	Polygon(ShapesImplemented.Polygon), Point(ShapesImplemented.Point), Line(ShapesImplemented.Line),
	Rectangle(ShapesImplemented.Rectangle), RectangleBorder(ShapesImplemented.Rectangle),
	Circumference(ShapesImplemented.Circle), Disk(ShapesImplemented.Circle), Triangle(ShapesImplemented.Triangle),
	TriangleBorder(ShapesImplemented.Triangle), Arrow, ArrowBorderBodySameLength, Cone, // Cono_Border,
	EllipseNoRotation, EllipseNoRotationBorder;

	ShapeRunnersImplemented() {
		this(ShapesImplemented.Polygon);
	}

	ShapeRunnersImplemented(ShapesImplemented shapeAbstract) {
		this.shapeAbstract = shapeAbstract;
	}

	public final ShapesImplemented shapeAbstract;

	public ShapesImplemented getShapeAbstract() {
		return shapeAbstract;
	}
}