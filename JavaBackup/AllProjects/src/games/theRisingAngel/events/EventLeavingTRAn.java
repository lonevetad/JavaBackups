package games.theRisingAngel.events;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.events.event.EventLeavingMap;
import games.theRisingAngel.enums.EventsTRAn;
import geometry.ObjectLocated;

public class EventLeavingTRAn extends EventLeavingMap {
	private static final long serialVersionUID = -55845824591500L;

	public EventLeavingTRAn(ObjectLocated objectInvolved) { super(objectInvolved, EventsTRAn.ObjectRemoved); }

	@Override
	public boolean isOnMapEventType(ObjectNamed eventType) {
		return eventType.getName() == EventsTRAn.ObjectRemoved.getName();
	}
}