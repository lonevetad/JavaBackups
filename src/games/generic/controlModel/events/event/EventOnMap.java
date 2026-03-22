package games.generic.controlModel.events.event;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.events.GEvent;
import geometry.ObjectLocated;

public abstract class EventOnMap extends GEvent {
	private static final long serialVersionUID = 562151896502L;

	public EventOnMap(ObjectLocated objectInvolved, ObjectNamed eventType) {
		super();
		this.objectInvolved = objectInvolved;
		if (!isOnMapEventType(eventType))
			throw new IllegalArgumentException(
					"The given event type is not compatible for \"in map\" events: " + eventType);
		this.eventType = eventType;
	}

	protected ObjectNamed eventType;
	protected ObjectLocated objectInvolved;

	public ObjectLocated getObjectInvolved() { return objectInvolved; }

	public abstract boolean isOnMapEventType(ObjectNamed eventType);

	@Override
	public String getName() { return eventType.getName(); }
}