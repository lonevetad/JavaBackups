package games.generic.controlModel.subimpl;

import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.gObj.DestructibleObject;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.misc.HealGeneric;
import tools.ObjectNamedID;
import tools.ObjectWithID;

public interface GEventInterfaceRPG extends GEventInterface {

	// object living, moving and creature related

	public void fireDestructionObjectEvent(GModalityET gaModality, DestructibleObject desObj);

	/**
	 * Fire a damage event and returns it, so informations like reductions and malus
	 * can be applied by listeners and processed by the injured living object
	 * (usually, a creature).
	 * 
	 * @param damageAmountToBeApplied: damage original subtracted by the amount of
	 *        damage reduction provided by the target (usually, those two damages
	 *        amounts are the same)
	 */
	public EventDamage fireDamageReceivedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric originalDamage, int damageAmountToBeApplied);

	/**
	 * See
	 * {@link #fireDamageReceivedEvent(GModalityET, DamageDealerGeneric, LivingObject, DamageGeneric, int)}.
	 */
	public EventDamage fireDamageCriticalReceivedEvent(GModalityET gm, DamageDealerGeneric source, LivingObject target,
			DamageGeneric originalDamage, int damageAmountToBeApplied);

	/**
	 * After someone is being healed, fire this event.<br>
	 */
	public <SourceHealing extends ObjectWithID> void fireHealReceivedEvent(GModalityET gaModality, SourceHealing source,
			LivingObject receiver, HealGeneric heal);

	/**
	 * Similar to
	 * {@link #fireHealReceivedEvent(GModalityET, CreatureSimple, ObjectNamedID)},
	 * but this event is fired upon healing someone else (the source of the healing
	 * is the fourth parameter while the second parameter is the target; the target
	 * is the same of
	 * {@link #fireHealReceivedEvent(GModalityET, CreatureSimple, ObjectNamedID)}).
	 */
	public <SourceHealing extends ObjectWithID> void fireHealGivenEvent(GModalityET gaModality, LivingObject receiver,
			HealGeneric heal, SourceHealing source);

	//

// misc

	public void fireMoneyChangeEvent(GModalityET gm, int currencyType, int oldValue, int newValue);

	public abstract void fireExpGainedEvent(GModalityET gm, int expGained);

	public abstract void fireLevelGainedEvent(GModalityET gm, int levelGained);
}