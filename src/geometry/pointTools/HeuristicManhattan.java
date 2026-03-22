package geometry.pointTools;

import java.awt.Point;
import java.util.function.BiFunction;

public class HeuristicManhattan implements BiFunction<Point, Point, Double> {
	public static final HeuristicManhattan SINGLETON = new HeuristicManhattan();

	@Override
	public Double apply(Point t, Point u) {
		int dx, dy;
//		NumberManager<Integer> inm;
//		inm = NumberManager.getIntegerManager();
		if (t == null || u == null) {
			Point p;
			p = t == null ? t : u;
			if (p == null)
				return 0.0;
			dx = p.x;
			dy = p.y;
		} else {
			dx = t.x - u.x;
			dy = t.y - u.y;
		}
		if (dx < 0)
			dx = -dx;
		if (dy < 0)
			dy = -dy;
		return (double) (dx + dy);
	}
}