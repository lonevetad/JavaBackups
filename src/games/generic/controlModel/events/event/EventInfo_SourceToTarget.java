package games.generic.controlModel.events.event;

import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.events.IGEvent;
import tools.ObjectWithID;

/**
 * Event putting in relations two objects: a "source doing something to a
 * target". It a very general class.
 * <p>
 * In future, the Source and the Target will be replace by a specific class and
 * the damage will be implemented to something more flexible.
 */
public abstract class EventInfo_SourceToTarget<Source, Target extends ObjectWithID> extends GEvent {
	private static final long serialVersionUID = 1L;
	protected Source source;
	protected Target target;
	protected final IGEvent eventIdentifier;

	public EventInfo_SourceToTarget(IGEvent eventIdentifier, Source source, Target target) {
		super();
		this.eventIdentifier = eventIdentifier;
		this.source = source;
		this.target = target;
	}

	@Override
	public String getName() { return eventIdentifier.getName(); }

	public IGEvent getEventIdentifier() { return eventIdentifier; }

	public Source getSource() { return source; }

	public Target getTarget() { return target; }

	public void setSource(Source source) { this.source = source; }

	public void setTarget(Target target) { this.target = target; }

}