package games.theRisingAngel.events;

import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageReceiverGeneric;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventDamage;

public class EventDamageTRAn extends EventDamage {
	private static final long serialVersionUID = 2222178786L;

	public EventDamageTRAn(IGEvent eventIdentifier, DamageDealerGeneric source, DamageReceiverGeneric target,
			DamageGeneric damage) {
		super(eventIdentifier, source, target, damage);
	}

	public EventDamageTRAn(IGEvent eventIdentifier, DamageDealerGeneric source, DamageReceiverGeneric target,
			DamageGeneric damage, int damageAmountToBeApplied) {
		super(eventIdentifier, source, target, damage, damageAmountToBeApplied);
	}
}