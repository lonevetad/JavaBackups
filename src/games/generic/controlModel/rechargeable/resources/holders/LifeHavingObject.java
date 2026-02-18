package games.generic.controlModel.rechargeable.resources.holders;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.events.GEvent;

public interface LifeHavingObject extends ObjectNamed {

	public int getLife();

	public int getLifeMax();

	/**
	 * Raw setting, like assigning a variable's value.
	 */
	public void setLife(int life);

	/**
	 * Raw setting, like assigning a variable's value, like {@link #setLife(int).
	 * Could invoke this last method if the actual life (returned by
	 * {@link #getLife()} is greater than the given parameter).
	 */
	public void setLifeMax(int lifeMax);

	/** BEWARE: by default, this method does NOT fire {@link GEvent}. */
	public default boolean receiveLifeHealing(int amount) {
		if (amount <= 0)
			return false;
		setLife(getLife() + amount);
		return true;
	}
}