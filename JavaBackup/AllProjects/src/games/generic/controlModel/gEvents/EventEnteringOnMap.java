package games.generic.controlModel.gEvents;

import games.generic.controlModel.ObjectNamed;
import geometry.ObjectLocated;

public class EventEnteringOnMap extends EventOnMap {
	private static final long serialVersionUID = 562151896502L;

	public EventEnteringOnMap(ObjectLocated objectInvolved, ObjectNamed eventType) {
		super(objectInvolved, eventType != null ? eventType : ExampleGameEvents.ObjectAdded);
		this.objectInvolved = objectInvolved;
	}

	@Override
	public boolean isOnMapEventType(ObjectNamed eventType) {
		return eventType.getName() == ExampleGameEvents.ObjectAdded.getName();
	}
}