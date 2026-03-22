package games.generic.controlModel.objects;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.GObjMovement;

/**
 * Defines an object that moves BUT its movement-over-time is defined as using
 * an instance of {@link GObjMovement}.<br>
 * NOTE: remember to call {@link GObjMovement#setObjectToMove(MovingObject)}
 * giving <code>this</code>!.
 */
public interface MovingObjectDelegatingMovement extends MovingObject {
	public GObjMovement getMovementImplementation();

	/**
	 * NOTE: remember to call {@link GObjMovement#setObjectToMove(MovingObject)}
	 * giving <code>this</code> inside this implementation!.
	 */
	public void setMovementImplementation(GObjMovement movementImplementation);

	@Override
	public default void move(GModality modality, int timeUnits) {
		GObjMovement gm;
		gm = getMovementImplementation();
		gm.setObjectToMove(this);
		gm.act(modality, timeUnits);
	}
}