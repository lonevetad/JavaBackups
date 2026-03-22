package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.attributes.AttributeModification;
import games.generic.controlModel.currency.Currency;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.holders.CurrencyHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.theRisingAngel.HelperWithAttributeModificationsTRAn;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.events.EventDamageTRAn;

public class ADamageReductionCurrencyBased extends AbilityModifyingAttributesRealTime
		implements HelperWithAttributeModificationsTRAn, GEventObserver {
	private static final long serialVersionUID = -69287821202158L;
	public static final String NAME = "Buying Reducion ";
	public static final int RARITY = 3;

	public ADamageReductionCurrencyBased(GModality gm) {// (DamageTypesTRAn dt
		this(gm, RARITY);
	}

	public ADamageReductionCurrencyBased(GModality gameModality, int rarity) {
		// super(NAME + dt.getName(), AttributesTRAn.damageReductionByType(dt));
		super(gameModality, NAME + rarity,
				new AttributesTRAn[] { AttributesTRAn.PhysicalDamageReduction, AttributesTRAn.MagicalDamageReduction });
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAn.DamageReceived.getName());
		perThousandFraction = 0;
		maximumReduction = 0;
		setRarityIndex(rarity);
		this.setPerThousandFraction(25 + (10 * rarity));
		this.setMaximumReduction(20 + (10 * rarity));
	}

	protected int perThousandFraction, maximumReduction;
	protected List<String> eventsWatching;

	//

	@Override
	public Long getObserverID() {
		return getID();
	}

	/**
	 * Get the "percentage" (but it's over a thousand, 1000, not the classical
	 * hundred of "%") of the currency held to be converted to
	 * {@link AttributesTRAn.PhysicalDamageReduction}.
	 */
	public int getPerThousandFraction() {
		return perThousandFraction;
	}

	public int getMaximumReduction() {
		return maximumReduction;
	}

	public void setMaximumReduction(int maximumReduction) {
		this.maximumReduction = maximumReduction;
	}

	protected int getDefaultCurrencyAmount(CreatureSimple c) {
		int a;
		CurrencySet ch;
		if (!(c instanceof CurrencyHolder)) {
			return 0;
		}
		ch = ((CurrencyHolder) c).getCurrencies();
		a = ch.getCurrencyAmount(ch.getCurrencies()[CurrencySet.BASE_CURRENCY_INDEX]);
		return a > 0 ? a : 0;
	}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	//

	public void setPerThousandFraction(int perThousandFraction) {
		this.perThousandFraction = perThousandFraction;
	}

	//

	@Override
	public void updateAttributesModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca,
			int levelAbility) {
		int reduct;
		AttributeModification am;
		reduct = (getDefaultCurrencyAmount(ah) * getPerThousandFraction()) / 1000;
		if (maximumReduction > 0 && reduct > maximumReduction) {
			reduct = maximumReduction;
		}
		am = super.getAttributesToModify()[0];
		am.setValue(reduct);
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int a;
		EventDamageTRAn ed;
		BaseCreatureRPG c;
		CurrencySet ch;
		Currency curr;
		if (ge.getName() == EventsTRAn.DamageReceived.getName()) {
			ed = (EventDamageTRAn) ge;
			// damage = ed.getDamage();
			c = (BaseCreatureRPG) ed.getTarget();
			if (!(c instanceof CurrencyHolder)) {
				return;
			}
			ch = ((CurrencyHolder) c).getCurrencies();
			curr = ch.getCurrencies()[CurrencySet.BASE_CURRENCY_INDEX];
			a = ch.getCurrencyAmount(curr);
			a -= Math.min(//
					((a * getPerThousandFraction()) / 1000), //
					((maximumReduction * getPerThousandFraction()) / 1000));
			ch.setCurrencyAmount(curr, //
					a > 0 ? a : 0);
			// super.getAttributeToModify().
		}
	}
}