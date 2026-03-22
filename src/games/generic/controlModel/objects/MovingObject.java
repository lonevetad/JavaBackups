package games.generic.controlModel.objects;

import games.generic.controlModel.GModality;

public interface MovingObject extends TimedObject, ObjectInSpace {

	/**
	 * Progress the object's movement by the some time's units elapsed, like
	 * milliseconds.
	 */
	public void move(GModality modality, int timeUnits);

	@Override
	public default void act(GModality modality, int timeUnits) {
		move(modality, timeUnits);
	}
}