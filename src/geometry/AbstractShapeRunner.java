package geometry;

import java.io.Serializable;

import geometry.pointTools.PointConsumer;

public interface AbstractShapeRunner extends Serializable {
	public static boolean FORCE_EARLY_STOP_CHECKS = false;

	public ShapeRunnersImplemented getShapeRunnersImplemented();

//	public AbstractShapeRunner setShapeRunnersImplemented(ShapeRunnersImplemented shapeRunnersImplemented);

	/**
	 * If the given shape is compatible with this runner (for instance, a circle
	 * runner is not adequate to a rectangular shape), then performs an action on a
	 * area described by the implementation on a specific shape. The permission to
	 * cycle is performed.<br>
	 * To do so, check the equality of the instance of
	 * {@link ShapeRunnersImplemented} returned by
	 * {@link #getShapeRunnersImplemented()} and the instance returned by
	 * {@link AbstractShape2D#getShapeImplementing()}.<br>
	 * Calls {@link #runShape(AbstractShape2D, PointConsumer, boolean)} passing
	 * {@link #FORCE_EARLY_STOP_CHECKS}.
	 * <p>
	 * NOTE: this should be a final
	 */
	public default boolean runShape(AbstractShape2D shape, PointConsumer action) {
		return runShape(shape, action, FORCE_EARLY_STOP_CHECKS);
	}

	/**
	 * Extension of {@link #runShape(AbstractShape2D, PointConsumer)} with the
	 * specification of the needs to interrupt the runner as soon as
	 * {@link PointConsumer#canContinue()} returns <code>false</code>.
	 */
	public boolean runShape(AbstractShape2D shape, PointConsumer action, boolean shouldPerformEarlyStops);
}