package geometry.implementations.shapeRunners;

import geometry.ShapeRunnersImplemented;

public class ShapeRunnerTriangleBorder extends ShapeRunnerPolygonBorder {
	private static final long serialVersionUID = -6501855552021048L;
	private static ShapeRunnerTriangleBorder SINGLETON;

	public static ShapeRunnerTriangleBorder getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ShapeRunnerTriangleBorder();
		return SINGLETON;
	}

	protected ShapeRunnerTriangleBorder() {
		super();
	}

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() {
		return ShapeRunnersImplemented.TriangleBorder;
	}
}