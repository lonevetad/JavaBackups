package geometry.implementations;

import geometry.AbstractShape2D;
import geometry.AbstractShapeRunner;
import geometry.pointTools.PointConsumer;

public interface AbstractShapeRunnerDelegating extends AbstractShapeRunner {

	public AbstractShapeRunner getShapeRunnerDelegate();

	public void setShapeRunnerDelegate(AbstractShapeRunner shapeRunnerDelegate);

	@Override
	public default boolean runShape(AbstractShape2D shape, PointConsumer action) {
		AbstractShapeRunner delegated;
		delegated = this.getShapeRunnerDelegate();
		if (delegated == null || delegated == this)
			return false;
		return delegated.runShape(shape, action);
	}
}