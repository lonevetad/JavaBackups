package games.theRisingAngel.events;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.EventDamage;
import games.generic.controlModel.gObj.LivingObject;

public class EventDamageTRAn extends EventDamage {
	private static final long serialVersionUID = 2222178786L;

	public EventDamageTRAn(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage) {
		super(eventIdentifier, source, target, damage);
	}

	public EventDamageTRAn(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target,
			DamageGeneric damage, int damageAmountToBeApplied) {
		super(eventIdentifier, source, target, damage, damageAmountToBeApplied);
	}
}