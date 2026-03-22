package geometry.implementations.intersectors;

public class PolygonToRectangleIntersection extends PolygonToPolygonIntersection {
	private static final long serialVersionUID = 2222L;
	private static PolygonToRectangleIntersection SINGLETON;

	public static PolygonToRectangleIntersection getInstance() {
		if (SINGLETON == null)
			SINGLETON = new PolygonToRectangleIntersection();
		return SINGLETON;
	}
}