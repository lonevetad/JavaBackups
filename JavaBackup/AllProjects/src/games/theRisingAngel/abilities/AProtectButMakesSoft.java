package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.events.EventDamageTRAn;

/**
 * Upon receiving a damage, it is halved, but at the cost of a similar amount of
 * malus of "damage reduction". This malus, after {@link #DURATION_EFFECT} it
 * vanish, reduced by the 25% (previously it halves) at every update.
 */
public class AProtectButMakesSoft extends ASimpleFixedBufferVanishingTRAn {
	private static final long serialVersionUID = -58986254522086021L;
	public static final String NAME = "Crystal Glass";
	public static final int RARITY = 2, DURATION_EFFECT = 500/* 2000 */, DURATION_VANISH = 5000;
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] {
			AttributesTRAn.PhysicalDamageReduction, AttributesTRAn.MagicalDamageReduction };

	public AProtectButMakesSoft(GModality gameModality) {
		super(gameModality, NAME, AttributeModification.newEmptyArray(WHAT_TO_MODIFY));
		this.eventsWatching.add(EventsTRAn.DamageReceived.getName());
		this.setCumulative(false);
		super.setVanishingEffectDuration(DURATION_VANISH);
		super.setAbilityEffectDuration(DURATION_EFFECT);
		setRarityIndex(RARITY);
		malusVanishing = 0;
	}

	/**
	 * The amount of damage reduced, equal but opposite to the reduction malus to be
	 * applied.
	 */
	protected int malusVanishing;

//

	@Override
	protected boolean isAcceptableEvent(IGEvent ge) {
		EventDamageTRAn dEvent;
		if (EventsTRAn.DamageReceived.getName() == ge.getName()) {
			dEvent = (EventDamageTRAn) ge;
			if (dEvent.getTarget() == this.getOwner()
					// check equality because it's bounded to the "wearer"
					&& dEvent.getDamageOriginal().getDamageAmount() > 0) { return true; }
		}
		return false;
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int d;
//		super.notifyEvent(modality, ge);
		if (isAcceptableEvent(ge)) {
			if (++this.stackedTriggerCharges > 1) {
				setPhaseTo(PhaseAbilityVanishing.Finished);
			} else {
				EventDamageTRAn de;
				de = (EventDamageTRAn) ge;
				d = de.getDamageReducedByTargetArmors() >> 1;
				de.setDamageAmountToBeApplied(de.getDamageAmountToBeApplied() - d);
				malusVanishing = -d; // NEGATIVE
				setPhaseAbilityCurrent(PhaseAbilityVanishing.Active);
			}
		}
	}

	@Override
	public void updateModAttributesOnEffectActivation() {
		int d;
//		super.updateModAttributesOnEffectActivation();
		d = malusVanishing;
		this.attributesToModify[0].setValue(d);
		this.attributesToModify[1].setValue(d);
	}

	@Override
	public void updateModAttributesDuringVanishing() {
		super.updateModAttributesDuringVanishing();
		malusVanishing = 0;
	}

	@Override
	protected int computeNewAmountOnVanishing(AttributeModification am) {
		int av;
		av = am.getValue();
		av -= (av >> 2); // returns the 75%;
		return av;
	}
}