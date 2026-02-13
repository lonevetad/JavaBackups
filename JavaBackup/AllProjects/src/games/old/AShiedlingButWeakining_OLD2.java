package games.old;

<<<<<<< HEAD
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.gObj.creature.BaseCreatureRPG;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityAttributesModsVanishingOverTime;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
<<<<<<<< HEAD:JavaBackup/AllProjects/src/games/old/AShiedlingButWeakining_OLD2.java
import games.theRisingAngel.abilities.AShieldingButWeakining;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.events.EventDamageTRAn;

/**
 * See {@link AShieldingButWeakining}.
=======
import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.abilities.AShieldingButWeakining;
import games.theRisingAngel.events.EventDamageTRAn;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.misc.AttributesTRAn;

/**
<<<<<<< HEAD
 * See {@link AShieldingButWeakining}.
=======
 * See {@link AShiedlingButWeakining}.
>>>>>>> master
<<<<<<< HEAD
=======
>>>>>>> develop
>>>>>>> develop
 */
public class AShiedlingButWeakining_OLD2 extends AbilityModifyingAttributesRealTime implements GEventObserver {
	private static final long serialVersionUID = -5898625452208602145L;
	public static final boolean IS_TESTING = true;
	public static final String NAME = "Stonefying Skin";
	public static final int DURATION_EFFECT = IS_TESTING ? 2000 : 5000; // 750
<<<<<<< HEAD
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] { AttributesTRAn.RegenLife,
=======
<<<<<<< HEAD
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] { AttributesTRAn.RigenLife,
>>>>>>> master
=======
<<<<<<< HEAD
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] { AttributesTRAn.RegenLife,
=======
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] { AttributesTRAn.RigenLife,
>>>>>>> master
>>>>>>> develop
>>>>>>> develop
			AttributesTRAn.DamageReductionPhysical, AttributesTRAn.DamageReductionMagical };

	public AShiedlingButWeakining_OLD2() {
		super(WHAT_TO_MODIFY, NAME);
		this.eventsWatching = new ArrayList<>(2);
<<<<<<< HEAD
		this.eventsWatching.add(EventsTRAn.DamageReceived.getName());
<<<<<<< HEAD
=======
=======
<<<<<<< HEAD
		this.eventsWatching.add(EventsTRAn.DamageReceived.getName());
>>>>>>> develop
		ticks = 0;
		timeThreshold = DURATION_EFFECT;
		isAbilityActive = false;
========
import games.theRisingAngel.events.EventDamageTRAn;
import games.theRisingAngel.events.EventsTRAr;

/**
 * Upon taking damage, reduce it by
 * <code>a Q percentage of life regeneration * N</code> by reducing it by H for
 * a certain amount of time T. Each damage taken reset the counter.<br>
 * It's like <i>enduring the skin as a stone: it's harder, but also harder to
 * heal</i>.<br>
 * Default values:
 * <ul>
 * <li>Q = 25%</li>
 * <li>H = 50%</li>
 * <li>N = 1</li>
 * <li>T = 2000 // milliseconds, for testing, 5000 on production</li>
 * </ul>
 */
public class AShiedlingButWeakining extends AbilityAttributesModsVanishingOverTime {
	private static final long serialVersionUID = -5898625452208602145L;
	public static final boolean IS_TESTING = false;
	public static final String NAME = "Stonefying Skin";
	public static final int DURATION_EFFECT = IS_TESTING ? 2000 : 5000; // 750
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] { AttributesTRAn.RigenLife,
			AttributesTRAn.DamageReductionPhysical, AttributesTRAn.DamageReductionMagical };

	public AShiedlingButWeakining() {
		super(AttributeModification.newEmptyArray(WHAT_TO_MODIFY), NAME);
		this.eventsWatching.add(EventsTRAr.DamageReceived.getName());
		this.setCumulative(false);
>>>>>>>> master:JavaBackup/AllProjects/src/games/theRisingAngel/abilities/AShiedlingButWeakining.java
	}

//	public CreatureSimple getCreatureReferred() {return creatureReferred;}

	@Override
	public int getAbilityEffectDuration() {
		return DURATION_EFFECT;
	}

	@Override
	public int getVanishingEffectDuration() {
		return 0; // ends immediately
	}

	@Override
	public void setAbilityEffectDuration(int abilityEffectDuration) {
	}

	@Override
	public void setVanishingEffectDuration(int vanishingEffectDuration) {
	}

	//

	@Override
	public void resetAbility() {
		super.resetAbility();
=======
		this.eventsWatching.add(EventsTRAr.DamageReceived.getName());
>>>>>>> develop
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
	public long getTimeThreshold() {
		return timeThreshold;
	}

	@Override
	public void resetAbility() {
		super.resetAbility();
		ticks = 0;
		isAbilityActive = false;
>>>>>>> master
		this.setAccumulatedTimeElapsed(0);
		performAbility(null); // to nullify it
	}

	@Override
<<<<<<< HEAD
<<<<<<<< HEAD:JavaBackup/AllProjects/src/games/old/AShiedlingButWeakining_OLD2.java
=======
>>>>>>> master
	public void act(GModality modality, int timeUnits) {
		if (isAbilityActive) {
			// act only if the ability is active
			super.act(modality, timeUnits);
		} // if no, then do not waste the time
<<<<<<< HEAD
========
	protected boolean isAcceptableEvent(IGEvent ge) {
		EventDamageTRAn dEvent;
		if (EventsTRAr.DamageReceived.getName() == ge.getName()) {
			dEvent = (EventDamageTRAn) ge;
			if (dEvent.getTarget() == this.getEquipItem().getCreatureWearingEquipments()
					// check equality because it's bounded to the "wearer"
					&& dEvent.getDamage().getDamageAmount() > 0) {
				return true;
			}
		}
		return false;
>>>>>>>> master:JavaBackup/AllProjects/src/games/theRisingAngel/abilities/AShiedlingButWeakining.java
	}

	@Override
	public void updateActiveEffectModAttributes() {
		// no kind of updated
	}

	@Override
<<<<<<<< HEAD:JavaBackup/AllProjects/src/games/old/AShiedlingButWeakining_OLD2.java
=======
	}

	@Override
	public void performAbility(GModality modality) {
		super.performAbility(modality);
		isAbilityActive = false;
	}

	@Override
>>>>>>> master
	public void notifyEvent(GModality modality, IGEvent ge) {
		int lifeRegenOriginal;
		EventDamageTRAn dEvent;
		BaseCreatureRPG creatureWearing;
		CreatureAttributes cAttr;
		AttributeModification am;
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
			dEvent = (EventDamageTRAn) ge;
			if (dEvent.getTarget() ==
			// check equality because it's bounded to the "wearer"
			(creatureWearing = this.getEquipItem().getCreatureWearingEquipments())
					&& dEvent.getDamage().getDamageAmount() > 0) {
				// activate the ability
				if (!this.isAbilityActive) {
					cAttr = creatureWearing.getAttributes();
					// activate the ability
					// compute the bonuses and malus
<<<<<<< HEAD
					lifeRegenOriginal = cAttr.getValue(AttributesTRAn.RegenLife.getIndex()); // Original
=======
<<<<<<< HEAD
					lifeRegenOriginal = cAttr.getValue(AttributesTRAn.RigenLife.getIndex()); // Original
>>>>>>> master
=======
<<<<<<< HEAD
					lifeRegenOriginal = cAttr.getValue(AttributesTRAn.RegenLife.getIndex()); // Original
=======
					lifeRegenOriginal = cAttr.getValue(AttributesTRAn.RigenLife.getIndex()); // Original
>>>>>>> master
>>>>>>> develop
>>>>>>> develop
					(am = this.attributesToModify[0]).setValue(-(lifeRegenOriginal >> 1)); // the half
					cAttr.applyAttributeModifier(am);
					lifeRegenOriginal >>= 2; // recycle as a temp
					(am = this.attributesToModify[1]).setValue(lifeRegenOriginal);
					cAttr.applyAttributeModifier(am);
					(am = this.attributesToModify[2]).setValue(lifeRegenOriginal);
					cAttr.applyAttributeModifier(am);
				}
				this.setAccumulatedTimeElapsed(0);
				this.isAbilityActive = true;
			}
		}
<<<<<<< HEAD
========
	public void doUponAbilityActivated() {
		int lifeRegenAmount;
		BaseCreatureRPG creatureWearing;
		CreatureAttributes cAttr;
		AttributeModification am;
		System.out.println("shield weakening activated");
		creatureWearing = this.getEquipItem().getCreatureWearingEquipments();
		cAttr = creatureWearing.getAttributes();
		// lifeRegenAmount = cAttr.getValue(AttributesTRAn.RigenLife.getIndex()); //
		// Original
		lifeRegenAmount = creatureWearing.getLifeRegenation();
		(am = this.attributesToModify[0]).setValue(-(lifeRegenAmount >> 1)); // the half
		cAttr.applyAttributeModifier(am);
		lifeRegenAmount >>= 2; // recycle as a temp
		(am = this.attributesToModify[1]).setValue(lifeRegenAmount);
		cAttr.applyAttributeModifier(am);
		(am = this.attributesToModify[2]).setValue(lifeRegenAmount);
		cAttr.applyAttributeModifier(am);
>>>>>>>> master:JavaBackup/AllProjects/src/games/theRisingAngel/abilities/AShiedlingButWeakining.java
=======
>>>>>>> master
	}

	// overrided to optimize the iteration over the array
	@Override
<<<<<<< HEAD
	public void doUponAbilityStartsVanishing() {
		System.out.println("start vanishing shield weakening");
	}

	@Override
	public void doUponAbilityEffectEnds() {
		System.out.println("effect ended shield weakening");
		// nothing more than a simple reset (yet implemented elsewhere)
	}

	@Override
	public void vanishEffect() {
		// override as a safe guard
		setPhaseTo(PhaseAbilityVanishing.Finished);
	}

	@Override
	public void doUponAbilityRefreshed() {
		System.out.println("effect refreshed shield weakening");
=======
	protected void updateAttributes(GModality gm, EquipmentItem ei, CreatureSimple ah, CreatureAttributes ca) {
		this.isAbilityActive = false;
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
			am.setValue(0);
		}
	}

	@Override
	public void updateAttributesModifiersValues(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca) {
//		for (AttributeModification am : this.attributesToModify) {
//			am.setValue(0); // reset everyone
//		}
>>>>>>> master
	}
}