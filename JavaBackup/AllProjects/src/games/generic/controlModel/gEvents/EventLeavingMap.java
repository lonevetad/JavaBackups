package games.generic.controlModel.gEvents;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.gObj.ObjectInSpace;

public class EventLeavingMap extends EventOnMap {
	private static final long serialVersionUID = -562151896502L;

	public EventLeavingMap(ObjectInSpace objectInvolved, ObjectNamed eventType) {
		super(objectInvolved, eventType != null ? eventType : ExampleGameEvents.ObjectRemoved);
		this.objectInvolved = objectInvolved;
	}

	@Override
	public boolean isOnMapEventType(ObjectNamed eventType) {
		return eventType.getName() == ExampleGameEvents.ObjectRemoved.getName();
	}
}