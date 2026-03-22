package geometry.implementations;

import geometry.AbstractShape2D;
import geometry.AbstractShapeRunner;
import geometry.pointTools.PointConsumer;

public abstract class AbstractShapeRunnerImpl implements AbstractShapeRunner {
	private static final long serialVersionUID = 30215489521L;

	@Override
	public final boolean runShape(AbstractShape2D shape, PointConsumer action, boolean shouldPerformEarlyStops) {
		if (shape == null || action == null || (shape.getShapeImplementing() != this.getShapeRunnersImplemented()))
			return false;
		return runShapeImpl(shape, action, shouldPerformEarlyStops);
	}

	protected abstract boolean runShapeImpl(AbstractShape2D shape, PointConsumer action,
			boolean shouldPerformEarlyStops);
}