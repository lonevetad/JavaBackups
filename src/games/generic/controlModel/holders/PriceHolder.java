package games.generic.controlModel.holders;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.currency.CurrencySet;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONObject;

public interface PriceHolder extends JSONable {
	public static final String FIELD_PRICES_MODIFICATIONS = "price"; // pricesModifications

	/**
	 * Any attributes could apply a bonus or a malus to the price of everything it's
	 * applied on.
	 */
	public CurrencySet getPricesModifications();

	public void setPricesModifications(CurrencySet priceModifications);

	//

	// JSON-related

	//
	@Override
	public default void toJSONValue(JSONObject wrapper) {
		wrapper.addField(FIELD_PRICES_MODIFICATIONS, this.getPricesModifications().toJSONValue());
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		// prices
		if (!wrapper.hasField(FIELD_PRICES_MODIFICATIONS)) {
			this.raiseExceptionMissingField(FIELD_PRICES_MODIFICATIONS, JSONTypes.Object);
		}
		JSONValue pricesModsJSONed = wrapper.getFieldValue(FIELD_PRICES_MODIFICATIONS);
		CurrencySet cs = gm.getGameObjectsProvider().newCurrencyHolder();
		if (pricesModsJSONed.isType(JSONTypes.Object)) {
			cs.loadFromJSONObject(gm, (JSONObject) pricesModsJSONed);
		} else if (pricesModsJSONed.isType(JSONTypes.ArrayHomogeneousType)) {
			cs.loadFromJSONArray(gm, pricesModsJSONed);
		} else {
			this.raiseExceptionIllegalTypeField(FIELD_PRICES_MODIFICATIONS, JSONTypes.Object, pricesModsJSONed);
		}
		this.setPricesModifications(cs);
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		// prices
		if (!jsonMap.containsKey(FIELD_PRICES_MODIFICATIONS)) {
			this.raiseExceptionMissingField(FIELD_PRICES_MODIFICATIONS, JSONTypes.Object);
		}
		Object pricesModsJSONed = jsonMap.get(FIELD_PRICES_MODIFICATIONS);
		if (!(pricesModsJSONed instanceof Map<?, ?>)) {
			this.raiseExceptionIllegalTypeField(FIELD_PRICES_MODIFICATIONS, JSONTypes.Object, pricesModsJSONed);
		}
		CurrencySet cs = gm.getGameObjectsProvider().newCurrencyHolder();
		cs.loadFromJSONMap(gm, (Map<String, Object>) pricesModsJSONed);
	}
}
