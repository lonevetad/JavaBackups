package geometry.implementations.intersectors;

public class LineToRectangleIntersector extends PolygonToLineIntersection {
	private static final long serialVersionUID = 1111L;
	private static LineToRectangleIntersector SINGLETON;

	public static LineToRectangleIntersector getInstance() {
		if (SINGLETON == null)
			SINGLETON = new LineToRectangleIntersector();
		return SINGLETON;
	}
}