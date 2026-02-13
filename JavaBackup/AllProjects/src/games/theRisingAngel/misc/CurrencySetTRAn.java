package games.theRisingAngel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.Currency;
import games.generic.controlModel.misc.CurrencySet;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.enums.CurrenciesTRAn;
import games.theRisingAngel.events.GEventInterfaceTRAn;

public class CurrencySetTRAn extends CurrencySet {

	public CurrencySetTRAn(GModality gameModality) { super(gameModality, CurrenciesTRAn.CURRENCIES); }

	@Override
	public void fireCurrencyChangeEvent(GModality gm, Currency currency, int oldValue, int newValue) {
		GModalityTRAnBaseWorld gmt;
		GEventInterfaceTRAn gei;
		gmt = (GModalityTRAnBaseWorld) gm;
		gei = (GEventInterfaceTRAn) gmt.getEventInterface();
		gei.fireCurrencyChangeEvent(gmt, currency, oldValue, newValue);
	}
}