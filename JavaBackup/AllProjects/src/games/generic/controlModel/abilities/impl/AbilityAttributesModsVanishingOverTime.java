package games.generic.controlModel.abilities.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.creature.CreatureSimple;

/**
 * See super-documentation of {@link AbilityVanishingOverTime}.<br>
 * The vanishing effect could be defined by overriding {@link #vanishEffect()}.
 * Currently it is diminished depending on the result of
 * {@link #computeNewAmountOnVanishing(AttributeModification)}.
 * <p>
 * The ability could simple be active (and each acceptable {@link IGEvent} reset
 * the "active-counter") or cumulative, meaning that the
 * {@link AttributeModification} can be applied more times, for instance one for
 * every event received while is not {@link PhaseAbilityVanishing#Inactive}
 * (plus one, to encode the triggering). To set this flag, call
 * {@link #setCumulative(boolean)},
 */
public abstract class AbilityAttributesModsVanishingOverTime extends AbilityModifyingAttributesRealTime
		implements GEventObserver, AbilityVanishingOverTime {
	private static final long serialVersionUID = 485154325880277L;

	public AbilityAttributesModsVanishingOverTime(GModality gameModality, String name,
			AttributeIdentifier[] attributesModified) {
		super(gameModality, name, attributesModified);
//		setAttributesToModify(attributesModified);
		initAAMVOT();
	}

	public AbilityAttributesModsVanishingOverTime(GModality gameModality, String name,
			AttributeModification[] attributesModifications) {
		super(gameModality, name, attributesModifications);
		initAAMVOT();
	}

	protected boolean isCumulative;
	/**
	 * Counter to count the amount of triggering events received between each
	 * "owner's attributes' updates over time" (updates used to make this ability
	 * "real time")
	 */
	protected volatile int eventsReceivedBeweenUpdateds, stackedTriggerCharges;
	protected int maxAmountStackedTriggerCharges;
	/**
	 * Differs from {@link #accumulatedTimeElapsedForUpdating} because this one is
	 * referred to the vanishing ability (both for base ability and vanishing
	 * effect)
	 */
	protected int accumulatedTimePhaseAbililty, vanishUpdateTimeMacrounits;
	protected int[] newModifiersAmountOnVanishing;
//	protected int abilityEffectDuration, vanishingEffectDuration;
	protected PhaseAbilityVanishing phaseAbilityCurrent;
	protected List<String> eventsWatching;
	/**
	 * Original values, used to set
	 * {@link AbilityModifyingAttributesRealTime#attributesToModify} during the
	 * ability evolution and act as a "original values cache"
	 */
	protected AttributeModification[] attributesToModifyOriginal;

	//

	//

	protected void initAAMVOT() {
		this.eventsWatching = new LinkedList<>();
		this.phaseAbilityCurrent = PhaseAbilityVanishing.Inactive;
		this.isCumulative = false;
		this.eventsReceivedBeweenUpdateds = 0;
		this.accumulatedTimePhaseAbililty = 0;
		this.vanishUpdateTimeMacrounits = 0;
//		this.modificationsAppliedAtLeastOnce = false;
		this.maxAmountStackedTriggerCharges = this.stackedTriggerCharges = 0;
	}

	//

	// TODO GETTER

	@Override
	public List<String> getEventsWatching() { return eventsWatching; }

	@Override
	public boolean isCumulative() { return isCumulative; }

	@Override
	public PhaseAbilityVanishing getPhaseAbilityCurrent() { return phaseAbilityCurrent; }

	@Override
	public int getAccumulatedTimePhaseAbililty() { return accumulatedTimePhaseAbililty; }

	/**
	 * Returns the amount of cumulated effect activation, if any. See
	 * {@link #getMaxAmountStackedTriggerCharges()}.
	 */
	public int getStackedTriggerCharges() { return stackedTriggerCharges; }

	/**
	 * If this ability {@link #isCumulative()}, then the ability can stack and the
	 * effects can be applied more than once; but the cumulated effect could be very
	 * huge. To limit this, set this bound, this cap of the stacked effects.<br>
	 * BEWARE: setting it to <code>0 (zero)</code> means <i>unbound</i> and negative
	 * numbers are forbidden (which are converted to <code>0 (zero)</code>).
	 * <p>
	 * It's almost parallel to {@link #isCumulative()} but cannot be confused: the
	 * difference exists to avoid integer overflow (very rare) and for faster
	 * checks. Even so, setting {@link #isCumulative()} to <code>true</code> and
	 * this value o <code>1</code> makes the same effect of setting the flag to
	 * <code>false</code>.
	 */
	public int getMaxAmountStackedTriggerCharges() { return maxAmountStackedTriggerCharges; }

	/**
	 * When the time goes by and the ability is in phase
	 * {@link PhaseAbilityVanishing#Vanishing}
	 */
	public abstract int getVanishingTimeThresholdUpdate();

	@Override
	public GModality getGameModality() { return null; }

	//

	@Override
	public void setGameModality(GModality gameModality) {}

	/** See [{@link #getMaxAmountStackedTriggerCharges()}. */
	public void setMaxAmountStackedTriggerCharges(int maxAmountStackedTriggerCharges) {
		if (maxAmountStackedTriggerCharges < 0)
			this.maxAmountStackedTriggerCharges = 0;
		else
			this.maxAmountStackedTriggerCharges = maxAmountStackedTriggerCharges;
	}

	@Override
	public void setCumulative(boolean isCumulable) {
		boolean oldValue;
		oldValue = this.isCumulative;
		this.isCumulative = isCumulable;
		if (oldValue != isCumulable) {
			if (isCumulable)
				reinstanceAttributesOriginal();
			else
				this.attributesToModifyOriginal = null; // remove cache
		}
	}

	public void setAttributesToModify(AttributeModification[] attributesMods) {
		this.attributesToModify = attributesMods;
		newModifiersAmountOnVanishing = new int[attributesMods.length];
		Arrays.fill(newModifiersAmountOnVanishing, 0);
		if (isCumulative)
			reinstanceAttributesOriginal();
	}

	@Override
	public void setPhaseAbilityCurrent(PhaseAbilityVanishing pav) { this.phaseAbilityCurrent = pav; }

	@Override
	public void setAccumulatedTimePhaseAbililty(int accumulatedTimeAbililtyVanishing) {
		this.accumulatedTimePhaseAbililty = accumulatedTimeAbililtyVanishing;
	}

	//

	protected void reinstanceAttributesOriginal() {
		int n;
		AttributeModification am;
		attributesToModifyOriginal = new AttributeModification[n = attributesToModify.length];
		while (--n >= 0) {
			am = attributesToModify[n];
			attributesToModifyOriginal[n] = new AttributeModification(am.getAttributeModified(), am.getValue());
			am.setValue(0); // ability is inactive now
		}
	}

	@Override
	public void resetAbility() {
		super.resetAbility();
		this.phaseAbilityCurrent = PhaseAbilityVanishing.Inactive;
		this.eventsReceivedBeweenUpdateds = 0;
		this.accumulatedTimePhaseAbililty = 0;
		this.vanishUpdateTimeMacrounits = 0;
		this.stackedTriggerCharges = 0;
		removeAndNullifyEffects();
		Arrays.fill(newModifiersAmountOnVanishing, 0);
	}

	@Override
	public void onRemovingFromOwner(GModality gm) {
		super.onRemovingFromOwner(gm);
		// remove previously added attributes
//		if (modificationsAppliedAtLeastOnce) {
		// do not remove more than once
//			modificationsAppliedAtLeastOnce = false;
//		removeAndNullifyEffects();
		resetAbility();
//		}
	}

	protected void removeAndNullifyEffects() {
		CreatureAttributes ca;
		ca = getOwnerAttributes(); // getAttributesWearer();
		if (ca == null)
			return;
		for (AttributeModification am : attributesToModify) {
			ca.removeAttributeModifier(am);
			am.setValue(0);
		}
	}

//	public void onEquip(GModality gm) {
//		modificationsAppliedAtLeastOnce = true;
//		super.onEquip(gm);
//	}

	protected boolean isAcceptableEvent(IGEvent e) { return this.eventsWatching.contains(e.getName()); }

	/**
	 * Override designed.<br>
	 * Default implementation returns the half of the current value.
	 */
	protected int computeNewAmountOnVanishing(AttributeModification am) { return am.getValue() >> 1; }

	@Override
	protected void applyAttributeModifications() {
		// nullify this effect: this ability is not active at start
	}

	@Override
	public void doUponAbilityActivated() {
		removeAndNullifyEffects();
		this.stackedTriggerCharges = 1;
	}

	@Override
	public void doUponAbilityEffectEnds() {
		this.stackedTriggerCharges = 0;
		// nullify modifications
		// finished or Inactive, don't care, it's ended
		setPhaseAbilityCurrent(PhaseAbilityVanishing.Inactive);
	}

	/**
	 * Override designed.<br>
	 * The usual (and current) implementation is to accumulate each effects for each
	 * event trigger if {@link #isCumulative()}, otherwise simply reset the effect's
	 * values.<br>
	 * Some subclasses could define their own proper methods and computations.
	 * <p>
	 * REMEMBER to reset {@link #eventsReceivedBeweenUpdateds}.
	 */
	public void updateModAttributesOnEffectActivation() {
		int n;
		AttributeModification am;
		n = this.attributesToModify.length;
		if (isCumulative) {
			// add all activations caused by events between each update
			if (eventsReceivedBeweenUpdateds > 0) {
				while (--n >= 0) {
					am = this.attributesToModify[n];
					// add up linearly
					am.setValue(am.getValue()
							+ eventsReceivedBeweenUpdateds * this.attributesToModifyOriginal[n].getValue());
				}
			}
		} else { // reset to original value //
			while (--n >= 0) {
				this.attributesToModify[n].setValue(this.attributesToModifyOriginal[n].getValue());
			}
		}
		eventsReceivedBeweenUpdateds = 0;
	}

	/**
	 * Override designed.<br>
	 * Similar to {@link #updateModAttributesOnEffectActivation()} about the main
	 * idea, but related to "vanishing phase"
	 * ({@link PhaseAbilityVanishing#Vanishing}).<br>
	 * This method could apply the results of the {@link #vanishEffect(int)} calls,
	 * which is called during {@link #evolveAbilityStatus(GModality, int)}, or just
	 * being empty because it totally relies on {@link #vanishEffect(int)}..
	 */
	public void updateModAttributesDuringVanishing() {
		int s;
		s = this.attributesToModify.length;
		while (--s >= 0) {
			this.attributesToModify[s].setValue(this.newModifiersAmountOnVanishing[s]);
		}
	}

	public void clearModAttributesUponFinishedOrInactivated() {
		for (AttributeModification am : this.attributesToModify) {
			am.setValue(0);
		}
	}

	@Override
	public void act(GModality modality, int timeUnits) {
		evolveAbilityStatus(modality, timeUnits);
		// first make the ability to evolve, then update values
		super.act(modality, timeUnits);
	}

	/*
	 * moved to the upper interface: previously was using the instance fields, now
	 * calls the getters and setters
	 */
//	public void evolveAbilityStatus(GModality modality, int timeUnits) { .. }

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		if (isAcceptableEvent(ge)) {
			// activate the ability
			if (isCumulative && (this.stackedTriggerCharges < this.maxAmountStackedTriggerCharges
					|| this.maxAmountStackedTriggerCharges == 0)) {
				// count the activation (or refresh if the bound is not 1) if possible
				eventsReceivedBeweenUpdateds++;
				if (++this.stackedTriggerCharges < 0)
					this.stackedTriggerCharges = Integer.MAX_VALUE;
			} // but still allows refreshing the counter
			reActivateAbility(modality);
			if (accumulatedTimePhaseAbililty > 0) // just to be sure
				accumulatedTimePhaseAbililty = 0;
		}
	}

	@Override
	public void updateAttributeModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca,
			int targetLevel) {
		PhaseAbilityVanishing phaseAbility;
		phaseAbility = phaseAbilityCurrent;
		if (phaseAbility == PhaseAbilityVanishing.Active) {
			updateModAttributesOnEffectActivation();
		} else if (phaseAbility == PhaseAbilityVanishing.Vanishing) {
			updateModAttributesDuringVanishing();
		} else { // if (phaseAbility == PhaseAbilityVanishing.Endend) {}
			clearModAttributesUponFinishedOrInactivated();
		}
	}

	@Override
	public void vanishEffect(int timeUnits) {
		int newValue, oldValue, t, s;
		AttributeModification am;
		this.vanishUpdateTimeMacrounits += timeUnits;
		while (this.vanishUpdateTimeMacrounits >= (t = getTimeSubUnitsEachUnit())) {
			this.vanishUpdateTimeMacrounits -= t;
//			for (AttributeModification am : this.attributesToModify) {

			s = this.attributesToModify.length;
			while (--s >= 0) {
				am = this.attributesToModify[s];
//				this.attributesToModify[s].setValue(this.newModifiersAmountOnVanishing[s]);
				oldValue = am.getValue();
				if (oldValue != 0) {
					newValue = computeNewAmountOnVanishing(am);
					newModifiersAmountOnVanishing[s] = newValue;
				}
			}
		}
	}
}