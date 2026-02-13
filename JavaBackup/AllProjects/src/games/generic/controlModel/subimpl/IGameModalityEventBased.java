package games.generic.controlModel.subimpl;

import java.util.List;

import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.events.IGEvent;

/**
 * Specify that this Game <br>
 * Needs to be an interface to allow multiple inheritance
 */
public interface IGameModalityEventBased {

	/** Override designed */
	public GEventInterface newEventInterface();

	public GEventInterface getEventInterface();

	/**
	 * At each time step (if any), for instance turns or milliseconds, a maximum
	 * amount of events to be processed may be defined, as default a million. If so,
	 * then this method should return a positive integer.
	 */
	public default int getMaxEventProcessedEachStep() { return 1000000; }

	public void setEventInterface(GEventInterface ei);

	/**
	 * See {@link #getMaxEventProcessedEachStep()}, by default is an empty method.
	 */
	public default void setMaxEventProcessedEachStep(int maxEventProcessedEachStep) {}

	/**
	 * Perform an action handling the case when the upper bound of events processed
	 * in the current time step (i.e., the value returned by
	 * {@link #getMaxEventProcessedEachStep()}) is exceeded. Then, return
	 * <code>true</code> to indicate that the task of processing events can be
	 * executed, <code>false</code> otherwise (i.e.: interrupt the current
	 * processing). <br>
	 * Usually, it's used to handle infinite loops
	 * <p>
	 * Optional method, could be empty, by default returns <code>false</code>.
	 */
	public default boolean handleExceedingEventsEnqueued(List<IGEvent> eventsLeft) {
		eventsLeft.clear();
		return false;
	}
}