package games.generic.controlModel.events.event;

import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import tools.ObjectWithID;

/**
 * A {@link GEvent} that holds the information about who (the "Source", returned
 * by {@link #getSource()}) has recharged a resource (returned by
 * {@link #getResourceAmountRecharged()}), which is held by a resource holder
 * (the {@link ResourceRechargeableHolder} "Target", returned by
 * {@link #getTarget()}).
 */
public class EventResourceRecharge<Source extends ObjectWithID>
		extends EventInfo_SourceToTarget<Source, ResourceRechargeableHolder> {
	private static final long serialVersionUID = -2222178787L;

	public EventResourceRecharge(IGEvent eventIdentifier, Source source, ResourceRechargeableHolder target,
			ResourceAmountRecharged resourceAmountRecharged) {
		super(eventIdentifier, source, target);
		this.resourceAmountRecharged = resourceAmountRecharged;
	}

	protected ResourceAmountRecharged resourceAmountRecharged;

	public ResourceAmountRecharged getResourceAmountRecharged() { return resourceAmountRecharged; }

	public void setResourceAmountRecharged(ResourceAmountRecharged heal) { this.resourceAmountRecharged = heal; }

	@Override
	public boolean isRequirigImmediateProcessing() { return true; }
}