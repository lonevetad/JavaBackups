package geometry.pointTools;

import java.awt.Point;
import java.io.Serializable;
import java.util.function.Consumer;

public interface PointConsumer extends Consumer<Point>, Serializable {
	public static final PointConsumer POINT_PRINTER = p -> System.out.println(p);

	/**
	 * Specify if the consumer can continue consuming points (allowing the cycle to
	 * be stopped earlier).)
	 */
	public default boolean canContinue() {
		return true;
	}
}