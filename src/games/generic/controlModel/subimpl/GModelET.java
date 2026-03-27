package games.generic.controlModel.subimpl;

import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.events.GEventManager;
import games.generic.controlModel.holders.GObjectsHolder;
import games.generic.controlModel.objects.TimedObject;
import tools.ObjectWithID;

/**
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link }></li>
 * </ul>
 */
public class GModelET extends GModelTimeBased {
	public static final String EVENT_MANAGER_OBSERVERS_HOLDER_NAME = "emonGModelET"; // event manager observers

	public GModelET() {
		super();
	}

	/** used as fast cache AND to put it to the list of {@link GObjectsHolder}. */
	protected GEventManager eventManager;

	@Override
	public boolean canHold(ObjectWithID o) {
		return (o instanceof TimedObject) || (o instanceof GEvent);
	}

	@Override
	public void onCreate() {
		// nothing to do
	}

	public GEventManager getEventManager() {
		return eventManager;
	}

	public void setEventManager(GEventManager eventManager) {
		this.eventManager = eventManager;
		super.addObjHolder(EVENT_MANAGER_OBSERVERS_HOLDER_NAME, eventManager);
	}

	@Override
	public int objectsHeldCount() {
		return this.eventManager.objectsHeldCount();
	}

}