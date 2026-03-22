package geometry;

import java.awt.Point;
import java.util.function.BiFunction;

import tools.NumberManager;

public interface IHeuristic<Distance extends Number> extends BiFunction<Point, Point, Distance> {
	public NumberManager<Distance> getDistanceManager();

	public void setDistanceManager(NumberManager<Distance> distanceManager);

}