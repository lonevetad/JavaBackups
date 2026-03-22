package dataStructures.isom.pathFinders;

import java.awt.Point;

import geometry.IHeuristic;
import tools.NumberManager;

public class Heuristic8GridMovement<Distance extends Number> implements IHeuristic<Distance> {
	public static final double SQRT_TWO = Math.sqrt(2.0);

	public Heuristic8GridMovement(NumberManager<Distance> distanceManager) {
		super();
		this.distanceManager = distanceManager;
	}

	protected NumberManager<Distance> distanceManager;

	@Override
	public NumberManager<Distance> getDistanceManager() { return distanceManager; }

	@Override
	public void setDistanceManager(NumberManager<Distance> distanceManager) { this.distanceManager = distanceManager; }

	@Override
	public Distance apply(Point t, Point u) {
		int dx, dy; // recycled as linear and oblique difference
		dx = t.x - u.x;
		dy = t.y - u.y;
		if (dx < 0)
			dx = -dx;
		if (dy < 0)
			dy = -dy;
		return distanceManager.fromDouble(//
				(dx > dy) ? //
						((dx - dy) + SQRT_TWO * dy) : //
						((dy - dx) + SQRT_TWO * dx) //
		);
	}
}