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
	private static final long serialVersionUID = 198048378304102L;

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
	public Currency currencyFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		Object currObj = jsonMap.get(Currency.FIELD_NAME);
		if (!(currObj instanceof String)) {
			this.raiseExceptionIllegalTypeField(Currency.FIELD_NAME, JSONTypes.String, currObj);
		}
		String currName = (String) currObj;
		return CurrenciesTRAn.valueOf(currName);
	}

	@Override
	public Currency currencyFromJSONObject(GModality gm, JSONObject jsonObj) {
		JSONValue jsonValName = jsonObj.getFieldValue(Currency.FIELD_NAME);
		if (!jsonValName.isType(JSONTypes.String)) {
			this.raiseExceptionIllegalTypeField(Currency.FIELD_NAME, JSONTypes.String, jsonValName);
		}
		String currName = jsonValName.asString();
		return CurrenciesTRAn.valueOf(currName);
	}

	@Override
	public Currency currencyFromIndex(GModality gm, int index, int amount) {
		return CurrenciesTRAn.CURRENCIES[index];
	}
}