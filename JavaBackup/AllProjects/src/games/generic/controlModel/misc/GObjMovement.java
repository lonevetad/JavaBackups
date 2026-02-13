package games.generic.controlModel.misc;

import games.generic.controlModel.objects.MovingObject;
import games.generic.controlModel.objects.TimedObject;
import tools.impl.OWIDLongImpl;

/**
 * Generic definition of a "movement" and its implementation.<br>
 * Some implementations could be:
 * <ul>
 * <li>Linear (from a starting point to a destination)</li>
 * <li>Circular (orbital around a center) of part of it</li>
 * <li>Parabolic / ellipsoid</li>
 * <li>Sinusoidal (snake-like)</li>
 * <li>Just a teleport</li>
 * <li>Some combination of the above ones (i.e.: "zig-zag" is a sequence of
 * linear movements)</li>
 * </ul>
 */
public abstract class GObjMovement extends OWIDLongImpl implements TimedObject {
	private static final long serialVersionUID = 1L;

	public GObjMovement() { super(); }

	protected MovingObject objectToMove;

	//

	public MovingObject getObjectToMove() { return objectToMove; }

	public void setObjectToMove(MovingObject objectToMove) { this.objectToMove = objectToMove; }

	/**
	 * Returns the amount of "space" traveled in a fixed amount of time. (The time's
	 * unit and granularity depends on the context, as explained in
	 * {@link TimedObject}).
	 */
	public abstract int getVelocity();

	/**
	 * Returns the amount of distance traveled since the starting point.<br>
	 * Be careful when implementing some manager with acceleration in it.
	 */
	public int getDistanceTraveled() { return 0; }
}