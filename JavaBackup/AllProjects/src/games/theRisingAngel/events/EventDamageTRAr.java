package games.theRisingAngel.events;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.misc.DamageGeneric;

public class EventDamageTRAr<Source> extends EventDamage<Source> {
	private static final long serialVersionUID = 1L;

	public EventDamageTRAr(IGEvent eventIdentifier, Source source, CreatureSimple target, DamageGeneric damage) {
		super(eventIdentifier, source, target, damage);
	}
}