package geometry;

import geometry.implementations.AbstractShapeRunnerImpl;
import geometry.pointTools.PointConsumer;

public abstract class ProviderShapeRunner extends AbstractShapeRunnerImpl {
	private static final long serialVersionUID = -9325211741502878L;

	public abstract AbstractShapeRunner getShapeRunner(ShapeRunnersImplemented sri);

	public AbstractShapeRunner getShapeRunner(AbstractShape2D shape) {
		return getShapeRunner(shape.getShapeImplementing());
	}

	@Override
	protected boolean runShapeImpl(AbstractShape2D shape, PointConsumer action, boolean shouldPerformEarlyStops) {
		AbstractShapeRunner r;
		if (shape == null || action == null)
			return false;
		r = this.getShapeRunner(shape.getShapeImplementing());
		return r == null ? false : r.runShape(shape, action, shouldPerformEarlyStops);
	}
}