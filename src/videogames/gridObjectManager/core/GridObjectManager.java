package gridObjectManager.core;

import java.awt.Point;
import java.io.Serializable;

import tools.LoggerMessages;
import tools.geometry.AbstractShape;
import tools.geometry.AbstractShapeRunner;

public interface GridObjectManager extends Serializable {

	public int getWidthMicropixel();

	public int getHeightMicropixel();

	public LoggerMessages getLog();

//	public AbstractShapeRunners/* _WithCoordinates */ getShapeRunners();

//	public AbstractPathFinder getPathFinder();

	// TODO SETTER

	public GridObjectManager setLog(LoggerMessages log);

//	public GridObjectManager setShapeRunners(AbstractShapeRunners/* _WithCoordinates */ shapeRunners);
//
//	public GridObjectManager setPathFinder(AbstractPathFinder pathFinder);

	//

	public default boolean isInside(Point p) {
		return isInside(p.x, p.y);
	}

	public boolean isInside(int x, int y);

	public default void runOnShape(AbstractShape2D shape, AbstractShapeRunner runner, GomConsumer action) {
		if (shape != null && runner != null) {
			runner.runShape(shape, action);
		}
	}

	//

	// imported

}
