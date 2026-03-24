package games.generic.controlModel.events.event;

import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.holders.ResourceRechargeableHolder;
import tools.ObjectWithID;

/**
 * A {@link GEvent} that holds the information about who (the "Source", returned
 * by {@link #getSource()}) has recharged a resource (returned by
 * {@link #getResourceAmountRecharged()}), which is held by a resource holder
 * (the {@link ResourceRechargeableHolder} "Target", returned by
 * {@link #getTarget()}).
 */
public class EventResourceLeeched<Source extends ObjectWithID>
		extends EventInfo_SourceToTarget<Source, ResourceRechargeableHolder> {
	private static final long serialVersionUID = -2222178787L;

	public EventResourceLeeched(IGEvent eventIdentifier, Source source, EventResourceRecharge<Source> eventRecharge) {
		super(eventIdentifier, source, eventRecharge.getTarget());
		this.eventRecharge = eventRecharge;
	}

	protected EventResourceRecharge<Source> eventRecharge;

	public EventResourceRecharge<Source> getEventRecharge() {
		return eventRecharge;
	}

	public void setEventRecharge(EventResourceRecharge<Source> eventRecharge) {
		this.eventRecharge = eventRecharge;
	}

}