package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.abilities.impl.AbilityModifyingSingleAttributeRealTime;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.events.EventDamageTRAn;

/**
 * Grants a life regeneration equals to the 12.5% of received damage,
 * accumulated each time it's received.<br>
 * At each "tick", equals to
 * {@link AbilityModifyingSingleAttributeRealTime#MILLISEC_ATTRIBUTE_UPDATE}, it
 * decrease by the maximum between {@link #MIN_VALUE_DECREMENT} and the 25%.
 */
//* 12.5%.
public class AMoreDamageReceivedMoreLifeRegen extends AbilityModifyingSingleAttributeRealTime
		implements GEventObserver {
	private static final long serialVersionUID = 5411087000163L;
	public static final int MIN_VALUE_DECREMENT = 4, RARITY = 3, THRESHOLD_DAMAGE_TO_TRIGGER = 8;
	public static final String NAME = "Pain Rinvigoring";

	public AMoreDamageReceivedMoreLifeRegen(GModality gm) {
		super(gm, NAME, AttributesTRAn.LifeRegen);
		this.eventsWatching = new ArrayList<>(2);
		this.addEventWatched(EventsTRAn.DamageReceived);
//		this.ticks = 0;
		this.thresholdTime = AbilityModifyingAttributesRealTime.MILLISEC_ATTRIBUTE_UPDATE;
		this.accumulatedLifeRegen = 0;
		setRarityIndex(RARITY);
	}

	protected int accumulatedLifeRegen;
//	protected long ticks;
	protected final long thresholdTime;
	protected List<String> eventsWatching;

	@Override
	public Long getObserverID() { return getID(); }

	@Override
	public List<String> getEventsWatching() { return eventsWatching; }

	@Override
	public long getTimeThreshold() { return thresholdTime; }

	//

	@Override
	public void resetAbility() {
		super.resetAbility();
//		ticks = 0;
//		thresholdTime = 1000;
		accumulatedLifeRegen = 0;
	}

	@Override
	public void act(GModality modality, int timeUnits) {
		AttributeModification am;
		am = this.getAttributesToModify()[0]; // the first one == the only one
		// do not waste computational time if no regeneration has to be applied
		if (am.getValue() > 0 || this.accumulatedLifeRegen > 0) { super.act(modality, timeUnits); }
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		if (EventsTRAn.DamageReceived.getName() == ge.getName()) {
			int d;
			EventDamageTRAn dEvent;
			dEvent = (EventDamageTRAn) ge;
			if (
			// check equality because it's bounded to the "wearer"
			dEvent.getTarget() == this.getOwner() && //
					(d = dEvent.getDamageReducedByTargetArmors()) >= THRESHOLD_DAMAGE_TO_TRIGGER) {
				d >>= 3; // "/ 8"
				if (d > 0) {
					this.accumulatedLifeRegen += d;
					if (this.accumulatedLifeRegen < 0) { this.accumulatedLifeRegen = Integer.MAX_VALUE; }
				}
			}
		}
	}

	@Override
	public void updateAttributeModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca,
			int levelTarget) {
		int v, alr;
		AttributeModification am;
		am = this.getAttributesToModify()[0]; // the first one == the only one
		v = am.getValue();
		alr = this.accumulatedLifeRegen;
		if (v > 0) {
			if (alr > 0) {
				v += alr;
				this.accumulatedLifeRegen = 0;
			}
//			if (++ticks >= (1000 / AbilityModifyingSingleAttributeRealTime.MILLISEC_ATTRIBUTE_UPDATE))
//				ticks = 0;
			// recycle "alr" as temporary variable
			alr = v >> 2; // 25%
			if (alr < MIN_VALUE_DECREMENT) { alr = MILLISEC_ATTRIBUTE_UPDATE; }
			v -= alr;
			if (v <= 0) { v = 0; }
			am.setValue(v);
		} else {
			if (alr > 0) { am.setValue(alr); }
			this.accumulatedLifeRegen = 0;
		}
	}
}