package games.generic.controlModel.subimpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.objects.TimedObject;

/**
 * TimedObjectSimpleImpl is an object performing a defined action (which is
 * {@link #executeAction(GModality)}) as times goes on (inside the
 * implementation of {@link #act(GModality, int)}) after an amount of time has
 * elapsed ( defined by {@link #getTimeThreshold()}).
 */
public interface TimedObjectPeriodic extends TimedObject {

	public long getAccumulatedTimeElapsed();

	public void setAccumulatedTimeElapsed(long newAccumulated);

	/**
	 * Get the time threshold at which, every time this amount of time is elapsed,
	 * perform the action defined as {@link #executeAction(GModality)}. <br>
	 * Could modify the status determining {@link #getTimeThreshold()}.
	 */
	public long getTimeThreshold();

	/**
	 * Execute the given action. Could modify the status determining
	 * {@link #getTimeThreshold()} during the execution.<br>
	 * Define this method instead of {@link #executeAction(GModality)}.
	 */
	public void executeAction(GModality modality);

	/**
	 * A default implementation is provided, redefine
	 * {@link #executeAction(GModality)} instead.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public default void act(GModality modality, int timeUnits) {
		if (timeUnits > 0) {
			setAccumulatedTimeElapsed(timeUnits + getAccumulatedTimeElapsed());
			while (getAccumulatedTimeElapsed() > getTimeThreshold()) {
				// decrement the counter and execute the action
				setAccumulatedTimeElapsed(getAccumulatedTimeElapsed() - getTimeThreshold());
				executeAction(modality);
			}
		}
	}
}