package games.theRisingAngel.events;

import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventResourceRecharge;
import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import tools.ObjectWithID;

public class EventResourceRechargeTRAr<Source extends ObjectWithID> extends EventResourceRecharge<Source> {
	private static final long serialVersionUID = 2222178787L;

	public EventResourceRechargeTRAr(IGEvent eventIdentifier, Source source, ResourceRechargeableHolder target,
			ResourceAmountRecharged heal) {
		super(eventIdentifier, source, target, heal);
	}

}