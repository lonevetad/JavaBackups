package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityBaseImpl;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventResourceRecharge;
import games.generic.controlModel.holders.CurrencyHolder;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.objects.LivingObject;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.enums.RechargeableResourcesTRAn;
import tools.ObjectWithID;

/**
 * Upon receiving life healing, make the owner earn N percentage of the base
 * currency, if possible.
 * <p>
 * N = 50.
 */
public class ALifeHealingMakesEarnBaseCurrency extends AbilityBaseImpl implements GEventObserver {
	private static final long serialVersionUID = -5898625452208602147L;
	public static final String NAME = "Healthy wallet in healthy body";
	public static final int RARITY = 4;

	public ALifeHealingMakesEarnBaseCurrency() {
		super();
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAn.ResourceRechargeReceived.getName());
		this.hasReceivedOddReneration = false;
		setRarityIndex(RARITY);
	}

	protected boolean hasReceivedOddReneration;
	protected List<String> eventsWatching;

//	public CreatureSimple getCreatureReferred() {return creatureReferred;}

	@Override
	public List<String> getEventsWatching() { return eventsWatching; }

	@Override
	public void performAbility(GModality gm, int targetLevel) {}

	@Override
	public void resetAbility() { this.hasReceivedOddReneration = false; }

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		if (EventsTRAn.ResourceRechargeReceived.getName() == ge.getName()) {
			int amoutRecharged;
			EventResourceRecharge<?> eventRecharging;
			CurrencyHolder ch;
			CurrencySet cs;
			LivingObject ol;
			ObjectWithID owid;
			eventRecharging = (EventResourceRecharge<?>) ge;
			if (eventRecharging.getResourceAmountRecharged().getRechargedResource() != RechargeableResourcesTRAn.Life)
				return;
			amoutRecharged = eventRecharging.getResourceAmountRecharged().getRechargedAmount();
			owid = getOwner();
			if (eventRecharging.getTarget() == owid //
//					&&(amoutHealed > 1
//							|| (hasReceivedOddReneration && amoutHealed > 0))//
					&& (owid instanceof CurrencyHolder)) {
				ol = (LivingObject) owid;
				if ((amoutRecharged & 0x1) == 1) {
					if (hasReceivedOddReneration) {
						amoutRecharged++;
						hasReceivedOddReneration = false;
					} else {
						hasReceivedOddReneration = true;
					}
				}
				if (ol.getLife() < ol.getLifeMax()) {
					// apply only if the regeneration has really happened
					ch = (CurrencyHolder) owid;
					cs = ch.getCurrencies();
					cs.alterCurrencyAmount(//
							cs.getCurrencies()[CurrencySet.BASE_CURRENCY_INDEX], //
							(amoutRecharged >> 1));
				}
			}
		}
	}
}