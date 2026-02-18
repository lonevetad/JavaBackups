package games.theRisingAngel.misc;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.currency.Currency;
import games.generic.controlModel.currency.CurrencySet;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.enums.CurrenciesTRAn;
import games.theRisingAngel.events.GEventInterfaceTRAn;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONObject;

public class CurrencySetTRAn extends CurrencySet {

	public CurrencySetTRAn(GModality gameModality) {
		super(gameModality, CurrenciesTRAn.CURRENCIES);
	}

	@Override
	public void fireCurrencyChangeEvent(GModality gm, Currency currency, int oldValue, int newValue) {
		GModalityTRAnBaseWorld gmt;
		GEventInterfaceTRAn gei;
		gmt = (GModalityTRAnBaseWorld) gm;
		gei = (GEventInterfaceTRAn) gmt.getEventInterface();
		gei.fireCurrencyChangeEvent(gmt, currency, oldValue, newValue);
	}

	@Override
	public Currency currencyFromJSONMap(Map<String, Object> jsonMap) {
		String currName = (String) jsonMap.get(Currency.FIELD_NAME);
		return CurrenciesTRAn.valueOf(currName);
	}

	@Override
	public Currency currencyFromJSONObject(JSONObject jsonObj) {
		JSONValue jsonValName = jsonObj.getFieldValue(Currency.FIELD_NAME);
		if (!jsonValName.isType(JSONTypes.String)) {
			this.raiseExceptionIllegalTypeField(Currency.FIELD_NAME, JSONTypes.String, jsonValName);
		}
		String currName = jsonValName.asString();
		return CurrenciesTRAn.valueOf(currName);
	}
}