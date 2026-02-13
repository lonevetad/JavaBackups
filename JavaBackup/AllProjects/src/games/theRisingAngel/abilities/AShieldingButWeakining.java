package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityAttributesModsVanishingOverTime;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.enums.RaritiesTRAn;
import games.theRisingAngel.events.EventDamageTRAn;
import tools.ObjectWithID;

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
public class AShieldingButWeakining extends AbilityAttributesModsVanishingOverTime {
	private static final long serialVersionUID = -5898625452208602145L;
	public static final boolean IS_TESTING = false;
	public static final String NAME = "Stonefying Skin";
	public static final RaritiesTRAn RARITY = RaritiesTRAn.Good;
	public static final int DURATION_EFFECT = IS_TESTING ? 2000 : 5000; // 750
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] { AttributesTRAn.LifeRegen,
			AttributesTRAn.PhysicalDamageReduction, AttributesTRAn.MagicalDamageReduction };

	public AShieldingButWeakining(GModality gm) {
		super(gm, NAME, WHAT_TO_MODIFY// AttributeModification.newEmptyArray(WHAT_TO_MODIFY)
		);
		this.eventsWatching.add(EventsTRAn.DamageReceived.getName());
		this.setCumulative(false);
		setRarityIndex(RARITY.getIndex());
	}

//	public CreatureSimple getCreatureReferred() {return creatureReferred;}

	@Override
	public int getAbilityEffectDuration() { return DURATION_EFFECT; }

	@Override
	public int getVanishingEffectDuration() {
		return 0; // ends immediately
	}

	@Override
	public void setAbilityEffectDuration(int abilityEffectDuration) {}

	@Override
	public void setVanishingEffectDuration(int vanishingEffectDuration) {}

	//

	@Override
	public void resetAbility() {
		super.resetAbility();
		this.setAccumulatedTimeElapsed(0);
		performAbility(null); // to nullify it
	}

	@Override
	protected boolean isAcceptableEvent(IGEvent ge) {
		EventDamageTRAn dEvent;
		if (EventsTRAn.DamageReceived.getName() == ge.getName()) {
			dEvent = (EventDamageTRAn) ge;
			if (dEvent.getTarget() == this.getOwner() // this.getEquipItem().getCreatureWearingEquipments()
					// check equality because it's bounded to the "wearer"
					&& dEvent.getDamageReducedByTargetArmors() > 0) { return true; }
		}
		return false;
	}

	@Override
	public void updateModAttributesOnEffectActivation() {
		// no kind of updated
	}

	@Override
	public void doUponAbilityActivated() {
		int lifeRegenAmount;
		BaseCreatureRPG creatureWearing;
		CreatureAttributes cAttr;
		AttributeModification am;
		ObjectWithID o;
		o = getOwner();
		creatureWearing = (o instanceof BaseCreatureRPG) ? ((BaseCreatureRPG) o) : null; // this.getEquipItem().getCreatureWearingEquipments();
		cAttr = creatureWearing.getAttributes();
		// lifeRegenAmount = cAttr.getValue(AttributesTRAn.RigenLife.getIndex()); //
		// Original
		lifeRegenAmount = creatureWearing.getLifeRegeneration();
		(am = this.attributesToModify[0]).setValue(-(lifeRegenAmount >> 1)); // the half
		cAttr.applyAttributeModifier(am);
		lifeRegenAmount >>= 2; // recycle as a temp
		(am = this.attributesToModify[1]).setValue(lifeRegenAmount);
		cAttr.applyAttributeModifier(am);
		(am = this.attributesToModify[2]).setValue(lifeRegenAmount);
		cAttr.applyAttributeModifier(am);
	}

	@Override
	public void doUponAbilityStartsVanishing() { setPhaseTo(PhaseAbilityVanishing.Finished); }

	// TODO verificare se tali "corpi dei metodi" devono essere questi, in vista
	// delle modifiche apportate alle sopraclassi e soprainterfacce

	@Override
	public void vanishEffect(int timeUnits) {
		// override as a safe guard
		setPhaseTo(PhaseAbilityVanishing.Finished);
	}

	@Override
	public void doUponAbilityEffectEnds() {
		super.doUponAbilityEffectEnds();
		removeAndNullifyEffects();
	}

	@Override
	public void doUponAbilityRefreshed() {}

	@Override
	public void updateModAttributesDuringVanishing() { setPhaseTo(PhaseAbilityVanishing.Finished); }

	@Override
	public int getVanishingTimeThresholdUpdate() { return GModalityTRAnBaseWorld.TIME_SUBUNITS_EACH_TIME_UNIT_TRAn; }
}