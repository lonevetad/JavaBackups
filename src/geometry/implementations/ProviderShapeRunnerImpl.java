package geometry.implementations;

import geometry.AbstractShapeRunner;
import geometry.ProviderShapeRunner;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.shapeRunners.ShapeRunnerCircleBorder;
import geometry.implementations.shapeRunners.ShapeRunnerCircleFilled;
import geometry.implementations.shapeRunners.ShapeRunnerLine;
import geometry.implementations.shapeRunners.ShapeRunnerPoint;
import geometry.implementations.shapeRunners.ShapeRunnerPolygonBorder;
import geometry.implementations.shapeRunners.ShapeRunnerRectangleBorder;
import geometry.implementations.shapeRunners.ShapeRunnerRectangleFilled;
import geometry.implementations.shapeRunners.ShapeRunnerTriangleBorder;
import geometry.implementations.shapeRunners.ShapeRunnerTriangleFilled;

public class ProviderShapeRunnerImpl extends ProviderShapeRunner {
	private static final long serialVersionUID = 342405452L;
	private static final AbstractShapeRunner[] RUNNERS;
	private static ProviderShapeRunnerImpl SINGLETON;
	static {
		RUNNERS = new AbstractShapeRunner[ShapeRunnersImplemented.values().length];
		RUNNERS[ShapeRunnersImplemented.Circumference.ordinal()] = ShapeRunnerCircleBorder.getInstance();
		RUNNERS[ShapeRunnersImplemented.Disk.ordinal()] = ShapeRunnerCircleFilled.getInstance();
		RUNNERS[ShapeRunnersImplemented.Line.ordinal()] = ShapeRunnerLine.getInstance();
		RUNNERS[ShapeRunnersImplemented.Point.ordinal()] = ShapeRunnerPoint.getInstance();
		RUNNERS[ShapeRunnersImplemented.PolygonBorder.ordinal()] =
//				ShapeRunnerPolygonBorder.getInstance();
				RUNNERS[ShapeRunnersImplemented.PolygonRegularBorder.ordinal()] = ShapeRunnerPolygonBorder
						.getInstance();
		RUNNERS[ShapeRunnersImplemented.RectangleBorder.ordinal()] = ShapeRunnerRectangleBorder.getInstance();
		RUNNERS[ShapeRunnersImplemented.Rectangle.ordinal()] = ShapeRunnerRectangleFilled.getInstance();
		RUNNERS[ShapeRunnersImplemented.Triangle.ordinal()] = ShapeRunnerTriangleFilled.getInstance();
		RUNNERS[ShapeRunnersImplemented.TriangleBorder.ordinal()] = ShapeRunnerTriangleBorder.getInstance();
	}

	public static ProviderShapeRunnerImpl getInstance() {
		if (SINGLETON == null)
			SINGLETON = new ProviderShapeRunnerImpl();
		return SINGLETON;
	}

	private ProviderShapeRunnerImpl() {}

	@Override
	public AbstractShapeRunner getShapeRunner(ShapeRunnersImplemented sri) { return RUNNERS[sri.ordinal()]; }

	@Override
	public ShapeRunnersImplemented getShapeRunnersImplemented() { return null; }
}