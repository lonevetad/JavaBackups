package games.theRisingAngel.events;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.events.event.EventEnteringOnMap;
import games.theRisingAngel.enums.EventsTRAn;
import geometry.ObjectLocated;

public class EventEnteringTRAn extends EventEnteringOnMap {
	private static final long serialVersionUID = 55845824591500L;

	public EventEnteringTRAn(ObjectLocated objectInvolved) { super(objectInvolved, EventsTRAn.ObjectAdded); }

	@Override
	public boolean isOnMapEventType(ObjectNamed eventType) {
		return eventType.getName() == EventsTRAn.ObjectAdded.getName();
	}
}