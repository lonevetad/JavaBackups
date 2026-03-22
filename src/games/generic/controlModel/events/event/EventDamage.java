package games.generic.controlModel.events.event;

import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.abilities.impl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.abilities.impl.AbilityVanishingOverTime;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageTypeGeneric;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.LivingObject;

public class EventDamage extends EventInfo_SourceToTarget<DamageDealerGeneric, LivingObject> {
	private static final long serialVersionUID = 1L;

	public EventDamage(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target, DamageGeneric damage) {
		this(eventIdentifier, source, target, damage, damage.getDamageAmount());
	}

	public EventDamage(IGEvent eventIdentifier, DamageDealerGeneric source, LivingObject target, DamageGeneric damage,
			int damageReductedByArmour) {
		super(eventIdentifier, source, target);
		this.damageOriginal = damage;
		this.damageAmountToBeApplied = this.damageReducedByTargetArmors = damageReductedByArmour;
	}

	protected DamageGeneric damageOriginal;
	protected int damageAmountToBeApplied, damageReducedByTargetArmors;

	/**
	 * DO NOT USE IT, LEFT FOR SHORTCUTS.
	 * <p>
	 * Returns the original damage with related informations. See
	 * {@link DamageGeneric}.
	 */
	public DamageGeneric getDamageOriginal() { return damageOriginal; }

	/**
	 * Proxy for calling <code>{@link #getDamageOriginal()}.getDamageType()</code>.
	 */
	public DamageTypeGeneric getDamageType() { return damageOriginal.getDamageType(); }

	/**
	 * Proxy for calling
	 * <code>{@link #getDamageOriginal()}.getDamageAmount()</code>.
	 */
	public int getDamageAmountOriginal() { return damageOriginal.getDamageAmount(); }

	/**
	 * The amount of damage that will be dealt to the target, computed considering
	 * as a starting valuethe value returned by
	 * {@link #getDamageReducedByTargetArmors()} (which is calculated applying some
	 * kind of "armor/reduction" to {@link #getDamageAmountOriginal()}, see that
	 * first method's documentation for further informations ) and also considering
	 * the effects of attributes ({@link CreatureAttributes}), equipments
	 * ({@link EquipmentItem}) or abilities' effects ({@link AbilityGeneric}, like
	 * {@link AbilityModifyingAttributesRealTime} and/or
	 * {@link AbilityVanishingOverTime}).
	 * <p>
	 * Each ability (in general, an {@link GEventObserver} able to react to damage,
	 * i.e.: this event) should modify this value.
	 */
	public int getDamageAmountToBeApplied() { return damageAmountToBeApplied; }

	/**
	 * Upon receiving some damage, a {@link LivingObject} may apply some reductions
	 * or some malus, maybe based on some kind of "armor/reduction", which could be
	 * calculated considering that object's attributes ({@link CreatureAttributes}).
	 */
	public int getDamageReducedByTargetArmors() { return damageReducedByTargetArmors; }

	public void setDamageOriginal(DamageGeneric damage) { this.damageOriginal = damage; }

	/** See {@link #getDamageAmountToBeApplied()}. */
	public void setDamageAmountToBeApplied(int damageAmountToBeApplied) {
		this.damageAmountToBeApplied = damageAmountToBeApplied;
	}

	@Override
	public boolean isRequirigImmediateProcessing() { return true; }

	/**
	 * Check if the provided object is the source of the {@link Damage AAAAAA TODO
	 * }, which is the one who is dealing damaged. .</br>
	 * Compares the references only.
	 *
	 * @param ddg the object that may be the source of this damage event.
	 * @return the check result
	 */
	public boolean isSource(DamageDealerGeneric ddg) { return super.source == ddg; }

	/**
	 * Check if the provided object is the target of the {@link Damage AAAAAA TODO
	 * }, which is the one who is being damaged. .</br>
	 * Compares the references only.
	 *
	 * @param ddg the object that may be the target of this damage event.
	 * @return the check result
	 */
	public boolean isTarget(LivingObject ddg) { return super.target == ddg; }

	@Override
	public String toString() {
		return "EventDamage [damage=" + damageOriginal + ", damageAmountToBeApplied=" + damageAmountToBeApplied + "]";
	}
}