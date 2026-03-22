package dataStructures.isom;

import java.awt.Point;

import geometry.PathOptimizer;
import tools.LoggerMessages;
import tools.NumberManager;
import tools.impl.OWIDLongImpl;

public abstract class InSpaceObjectsManagerImpl<Distance extends Number> extends OWIDLongImpl
		implements InSpaceObjectsManager<Distance> {
	private static final long serialVersionUID = 1L;

	public InSpaceObjectsManagerImpl() { super(); }

	protected LoggerMessages log;
//	protected PathFinderIsomAdapter<NodeIsom, D> pathFinder;
	protected PathFinderIsom<Distance> pathFinder;
	protected NumberManager<Distance> weightManager;
	protected PathOptimizer<Point> pathOptimizer;

	//

	// TODO GETTER

	@Override
	public LoggerMessages getLog() { return log; }

	@Override
	public NumberManager<Distance> getWeightManager() { return weightManager; }

	@Override
	public PathOptimizer<Point> getPathOptimizer() { return pathOptimizer; }

	@Override
	public PathFinderIsom<Distance> getPathFinder() { return pathFinder; }

	//

	// TODO SETTER

	@Override
	public void setPathOptimizer(PathOptimizer<Point> pathOptimizer) { this.pathOptimizer = pathOptimizer; }

	@Override
	public void setLog(LoggerMessages log) { this.log = log; }

	@Override
	public void setPathFinder(PathFinderIsom<Distance> pathFinder) { this.pathFinder = pathFinder; }

	@Override
	public void setWeightManager(NumberManager<Distance> numberManager) { this.weightManager = numberManager; }

	//

	// TODO OTHER

//	public AbstractShapeRunners/* _WithCoordinates */ getShapeRunners();

//	public AbstractPathFinder getPathFinder();

	//

//	public default boolean isInside(Point p) { return isInside(p.getX(), p.getY());}
//	public default boolean isInside(double x, double y) {
//		AbstractShape2D s;
//		s = getSpaceShape();
//		return s == null ? false : s.contains((int) x, (int) y);
//	}

	// TODO to-do path find

}