package geometry.implementations.intersectors;

public class RectangleToRectangleIntersection extends PolygonToRectangleIntersection {
	private static final long serialVersionUID = 1L;
	private static RectangleToRectangleIntersection SINGLETON;

	public static RectangleToRectangleIntersection getInstance_() {
		if (SINGLETON == null)
			SINGLETON = new RectangleToRectangleIntersection();
		return SINGLETON;
	}
}