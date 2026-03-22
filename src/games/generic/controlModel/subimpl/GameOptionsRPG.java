package games.generic.controlModel.subimpl;

import dataStructures.isom.InSpaceObjectsManagerImpl;
import games.generic.GameOptions;
import games.generic.controlModel.GController;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.objects.MovingObject;
import games.generic.controlModel.objects.TimedObject;

public class GameOptionsRPG extends GameOptions {

	public GameOptionsRPG(GController gc) { super(gc); }

	protected int spaceSubunitsEachUnit, timeSubunitsEachUnit;

	/**
	 * Concept useful for {@link GObjectsInSpaceManager} and
	 * {@link MovingObject}.<br>
	 * Each "space unit" (like <i>meters</i>) are composed by some "sub-units" (like
	 * <i>centimeters</i>) which could be used to build up
	 * {@link InSpaceObjectsManagerImpl} instances and to calculate object
	 * movements.
	 *
	 * <p>
	 */
	public int getSpaceSubunitsEachUnit() { return spaceSubunitsEachUnit; }

	/**
	 * See also {@link TimedObject#getTimeSubUnitsEachUnit()}.
	 */
	public int getTimeSubunitsEachUnit() { return timeSubunitsEachUnit; }

	//

	/**
	 * BEWARE OF CHANGING THE VALUE!!
	 *
	 * @param spaceSubunitsEachUnit
	 */
	public void setSpaceSubunitsEachUnit(int spaceSubunitsEachUnit) {
		this.spaceSubunitsEachUnit = spaceSubunitsEachUnit;
	}

	/**
	 * BEWARE OF CHANGING THE VALUE!!
	 *
	 * @param timeSubunitsEachUnit
	 */
	public void setTimeSubunitsEachUnit(int timeSubunitsEachUnit) {
		this.timeSubunitsEachUnit = timeSubunitsEachUnit;
	}
}