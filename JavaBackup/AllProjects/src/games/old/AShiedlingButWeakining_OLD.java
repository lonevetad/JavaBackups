package games.old;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.creature.BaseCreatureRPG;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.events.EventDamageTRAn;
import games.theRisingAngel.events.EventsTRAr;
import games.theRisingAngel.events.EventsTRAr;
import tools.ObjectWithID;

/**
 * Upon taking damage, reduce it by
 * <code>a Q percentage of life regeneration * N</code> by reducing it by H for
 * a certain amount of time T. Each damage taken reset the counter.<br>
 * It's like <i>enduring the skin as a stone: it's harder, but also harder to
 * heal</i>.
 */
// Q = 25%
// H = 50%
// N = 1
// T = 5000 // milliseonds
public class AShiedlingButWeakining_OLD extends AbilityModifyingAttributesRealTime implements GEventObserver {
	private static final long serialVersionUID = -5898625452208602145L;
	public static final String NAME = "Stonefying Skin";
	public static final int DURATION_EFFECT = 750; // 5000; //
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] { AttributesTRAn.RegenLife,
			AttributesTRAn.DamageReductionPhysical, AttributesTRAn.DamageReductionMagical };

	public AShiedlingButWeakining_OLD() {
		super(WHAT_TO_MODIFY, NAME);
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(
//				this.getAttributeToModify().getAttributeModified().getName()
				EventsTRAn.DamageReceived.getName());
		ticks = 0;
		timeThreshold = DURATION_EFFECT;
		isAbilityActive = false;
	}

	protected boolean isAbilityActive;
	protected long ticks, timeThreshold;
	protected List<String> eventsWatching;

	@Override
	public Integer getObserverID() {
		return getID();
	}

//	public CreatureSimple getCreatureReferred() {return creatureReferred;}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public ObjectWithID getOwner() {
		return this.getEquipItem().getCreatureWearingEquipments();
	}

	@Override
	public long getTimeThreshold() {
		return timeThreshold;
	}

	@Override
	public void resetAbility() {
		super.resetAbility();
		ticks = 0;
		isAbilityActive = false;
		this.setAccumulatedTimeElapsed(0);
	}

	@Override
	public void act(GModality modality, int timeUnits) {
		if (isAbilityActive) {
			// act only if the ability is active
			super.act(modality, timeUnits);
		} // if no, the do not waste the time
	}

	@Override
	public void performAbility(GModality modality) {
		super.performAbility(modality);
		isAbilityActive = false;
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int lifeRegenOriginal;
		EventDamageTRAn<?> dEvent;
		BaseCreatureRPG creatureWearing;
		CreatureAttributes cAttr;
<<<<<<< HEAD
		if (EventsTRAn.DamageReceived.getName() == ge.getName()) {
=======
<<<<<<< HEAD
		if (EventsTRAr.DamageReceived.getName() == ge.getName()) {
>>>>>>> master
=======
<<<<<<< HEAD
		if (EventsTRAn.DamageReceived.getName() == ge.getName()) {
=======
		if (EventsTRAr.DamageReceived.getName() == ge.getName()) {
>>>>>>> master
>>>>>>> develop
>>>>>>> develop
			dEvent = (EventDamageTRAn<?>) ge;
			if (dEvent.getTarget() ==
			// check equality because it's bounded to the "wearer"
			(creatureWearing = this.getEquipItem().getCreatureWearingEquipments())
					&& dEvent.getDamage().getDamageAmount() > 0) {
				// activate the ability
				cAttr = creatureWearing.getAttributes();
				if (this.isAbilityActive) {
					// yet active, update the value
					for (AttributeModification am : this.attributesToModify) {
//						this.getEquipItem().getBelongingEquipmentS
						cAttr.removeAttributeModifier(am);
					}
				}
				// activate the ability
				// compute the bonuses and malus
				lifeRegenOriginal = cAttr.getOriginalValue(AttributesTRAn.RegenLife.getIndex());
				this.attributesToModify[0].setValue(-(lifeRegenOriginal >> 1)); // the half
				lifeRegenOriginal >>= 2; // recycle as a temp
				this.attributesToModify[1].setValue(lifeRegenOriginal);
				this.attributesToModify[2].setValue(lifeRegenOriginal);
				if (this.isAbilityActive) {
					cAttr = creatureWearing.getAttributes();
					for (AttributeModification am : this.attributesToModify) {
						cAttr.applyAttributeModifier(am);
					}
				}
				this.setAccumulatedTimeElapsed(0);
				this.isAbilityActive = true;
				System.out.println("çççççççç a shielding weaking, now regen: " + this.attributesToModify[0].getValue());
			}
		}
	}

	@Override
	public void updateAttributesModifiersValues(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca) {
		for (AttributeModification am : this.attributesToModify) {
			am.setValue(0); // reset everyone
		}
	}
}