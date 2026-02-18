package geometry.pointTools;

import java.awt.Point;
import java.io.Serializable;

public interface ClosestPairPointsFinder {

	public ClosestPairPoints getClosestPairPoints(Point[] points);

	//

	public static class ClosestPairPoints implements Serializable {
		private static final long serialVersionUID = 84414156394248125L;
		protected double distance;
		protected Point firstPoint, secondPoint;

		public ClosestPairPoints(Point p1, Point p2, double distance) {
			super();
			this.firstPoint = p1;
			this.secondPoint = p2;
			this.distance = distance;
		}

		public double getDistance() {
			return distance;
		}

		public Point getFirstPoint() {
			return firstPoint;
		}

		public Point getSecondPoint() {
			return secondPoint;
		}

//

		/**
		 * Beware of using this: should be used only in the
		 * {@link ClosestPairPointsFinder}'s context or similar ones, because its value
		 * is trusted. It means: no checks are performed.
		 */
		public ClosestPairPoints setDistance(double distance) {
			this.distance = distance;
			return this;
		}

		public ClosestPairPoints setFirstPoint(Point firstPoint) {
			this.firstPoint = firstPoint;
			return this;
		}

		public ClosestPairPoints setSecondPoint(Point secondPoint) {
			this.secondPoint = secondPoint;
			return this;
		}
	}
}
