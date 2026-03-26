package games.generic.controlModel.currency;

import java.util.Map;
import java.util.Objects;

import games.generic.controlModel.GModality;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONArray;
import tools.json.types.JSONBoolean;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;

public abstract class CurrencySet implements JSONable {

	private static final long serialVersionUID = -949401544050800932L;
	public static final String FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT = "canFireCurrencyChangeEvent";
	public static final String FIELD_VALUES = "values";
	public static final String FIELD_CURRENCIES = "currencies";

	public static final int BASE_CURRENCY_INDEX = 0;

	public static interface CurrencyAmountConsumer {
		public void performAction(Currency currency, int amount);
	}

	//

	/**
	 * The {@link GModality} is not mandatory: i's used to compute, in the default
	 * implementation, the value of {@link #isFiringEvents()}:
	 * <code>return gameModality != null</code><br>
	 * The "typeAmount" parameter could be an enumeration's size.
	 */
	public CurrencySet(GModality gameModality, Currency[] currencies) {
		Objects.requireNonNull(currencies);
		if (currencies.length < 1) {
			throw new IllegalArgumentException("Cannot exist no currency types, just don't create me instead!");
		}
		this.gameModality = gameModality;
		this.currencies = currencies;
		this.values = new int[currencies.length];
		this.canFireCurrencyChangeEvent = false;
	}

	protected boolean canFireCurrencyChangeEvent;
	protected int[] values;
	protected Currency[] currencies;
	protected GModality gameModality;

	//

	public GModality getGameModality() {
		return gameModality;
	}

	public int getCurrencyAmount(Currency c) {
		return this.values[c.getIndex()];
	}

	public boolean isFiringEvents() {
		return canFireCurrencyChangeEvent && gameModality != null;
	}

	public boolean canFireCurrencyChangeEvent() {
		return canFireCurrencyChangeEvent;
	}

	public Currency[] getCurrencies() {
		return currencies;
	}

	//

	public void setCanFireCurrencyChangeEvent(boolean canFireCurrencyChangeEvent) {
		this.canFireCurrencyChangeEvent = canFireCurrencyChangeEvent;
	}

	public void setGameModality(GModality gameModality) {
		this.gameModality = gameModality;
	}

	public void setGameModaliy(GModality gameModality) {
		this.gameModality = gameModality;
	}

	public void setCurrencyAmount(Currency c, int newAmount) {
		/**
		 * Set the currency amount of a given type, identified by the first parameter,
		 * to the provided amount, which is the second parameter.
		 *
		 * @param indexType an identifier, usually an index, that identifies the
		 *                  currency type
		 * @param newAmount the amount of the type of currency intended to set
		 */
		int old, indexCurrency;
		indexCurrency = c.getIndex();
		old = this.values[indexCurrency];
		this.values[indexCurrency] = newAmount;
		if (isFiringEvents()) {
			fireCurrencyChangeEvent(this.gameModality, c, old, newAmount);
		}
	}

	protected void setValues(int[] vals) {
		this.values = vals;
	}

	protected void setCurrencies(Currency[] curr) {
		this.currencies = curr;
	}

	//

	//

	public int[] getCurrencyAmounts() {
		int[] valuesToReturn = new int[this.values.length];
		System.arraycopy(this.values, 0, valuesToReturn, 0, this.values.length);
		return valuesToReturn;
	}

	/**
	 * Shorthand to
	 * <code>{@link #setCurrencyAmount(int, int)}( indexType, {@link #getCurrencyAmount(int)}(indexType) + delta)</code>
	 */
	public void alterCurrencyAmount(Currency c, int delta) {
		int old, newAmount, indexCurrency;
		indexCurrency = c.getIndex();
		old = this.values[indexCurrency];
		this.values[indexCurrency] = newAmount = (old + delta);
		if (isFiringEvents()) {
			fireCurrencyChangeEvent(this.gameModality, c, old, newAmount);
		}
	}

	public void forEachCurrency(CurrencyAmountConsumer citac) {
		int i, n;
		n = this.values.length;
		i = -1;
		while (++i < n) {
			citac.performAction(currencies[i], this.values[i]);
		}
	}

	public abstract void fireCurrencyChangeEvent(GModality gameModality, Currency currency, int oldValue, int newValue);

	@Override
	public String toString() {
		final StringBuilder sb;
		sb = new StringBuilder(16);
		sb.append("CurrencySet [");
		forEachCurrency((currency, a) -> sb.append(" (").append(currency.getName()).append(':').append(a).append("), ")//
		);
		sb.append(']');
		return sb.toString();
	}

	//

	// JSON-related

	//

	public abstract Currency currencyFromIndex(GModality gm, int index, int amount);

	public abstract Currency currencyFromJSONMap(GModality gm, Map<String, Object> jsonMap);

	public abstract Currency currencyFromJSONObject(GModality gm, JSONObject jsonObj);

	@Override
	public void toJSONValue(JSONObject wrapper) {
		if (wrapper == null) {
			throw new IllegalArgumentException("Provided JSON wrapper cannot be null");
		}
		// canFireCurrencyChangeEvent
		JSONBoolean jsonedCFCCE = new JSONBoolean(this.canFireCurrencyChangeEvent());
		wrapper.addField(FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT, jsonedCFCCE);
		// values
		JSONInt[] valuesArray = new JSONInt[this.values.length];
		for (int i = 0; i < this.values.length; i++) {
			valuesArray[i] = new JSONInt(this.values[i]);
		}
		JSONArray jsonedValues = new JSONArray(true, valuesArray, JSONTypes.Int);
		wrapper.addField(FIELD_VALUES, jsonedValues);
		// currencies
		JSONValue[] currenciesArray = new JSONValue[this.currencies.length];
		for (int i = 0; i < this.currencies.length; i++) {
			currenciesArray[i] = this.currencies[i].toJSONValue();
		}
		JSONArray jsonedCurrencies = new JSONArray(true, currenciesArray, JSONTypes.Object);
		wrapper.addField(FIELD_CURRENCIES, jsonedCurrencies);
	}

	public void loadFromJSONArray(GModality gm, JSONValue valCurr) throws IllegalArgumentException {
		if (!(valCurr.isType(JSONTypes.ArrayHomogeneousType))) {
			this.raiseExceptionIllegalTypeField(FIELD_CURRENCIES, JSONTypes.ArrayHomogeneousType, valCurr);
		}
		JSONArray valCurrArr = (JSONArray) valCurr;
		final Currency[] currArr = new Currency[valCurrArr.getElementsAmount()];
		valCurrArr.forEach((index, currJSON) -> {
			if (currJSON.isType(JSONTypes.Int)) {
				currArr[index] = currencyFromIndex(gm, index, ((JSONInt) currJSON).asInt());
			} else {
				if (!currJSON.isType(JSONTypes.Object)) {
					this.raiseExceptionIllegalTypeField(FIELD_CURRENCIES + JSONable.SEPARATOR_INDEX + index,
							JSONTypes.Object, currJSON);
				}
				currArr[index] = currencyFromJSONObject(gm, (JSONObject) currJSON);
			}
		});
		this.setCurrencies(currArr);
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		if (wrapper == null) {
			throw new IllegalArgumentException("Provided JSONObject wrapper cannot be null");
		}
		if (!wrapper.hasField(FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT)) {
			this.raiseExceptionMissingField(FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT, JSONTypes.Boolean);
		}
		JSONValue valCFCCE = wrapper.getFieldValue(FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT);
		if (!valCFCCE.isType(JSONTypes.Boolean)) {
			this.raiseExceptionIllegalTypeField(FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT, JSONTypes.Boolean, valCFCCE);
		}
		this.setCanFireCurrencyChangeEvent(valCFCCE.asBoolean());
		//
		if (!wrapper.hasField(FIELD_VALUES)) {
			this.raiseExceptionMissingField(FIELD_VALUES, JSONTypes.Object);
		}
		JSONValue valValues = wrapper.getFieldValue(FIELD_VALUES);
		if (!(valValues.isType(JSONTypes.ArrayHomogeneousType))) {
			this.raiseExceptionIllegalTypeField(FIELD_VALUES, JSONTypes.ArrayHomogeneousType, valValues);
		}
		this.setValues(valValues.asArrayInt());
		//
		if (!wrapper.hasField(FIELD_CURRENCIES)) {
			this.raiseExceptionMissingField(FIELD_CURRENCIES, JSONTypes.Object);
		}
		JSONValue valCurr = wrapper.getFieldValue(FIELD_CURRENCIES);
		loadFromJSONArray(gm, valCurr);
	}

	public void loadFromJSONArray(GModality gm, Object[] valCurrArr) throws IllegalArgumentException {
		Currency[] currArr = new Currency[valCurrArr.length];
		for (int i = 0; i < valCurrArr.length; i++) {
			Object curr = valCurrArr[i];
			if (!(curr instanceof Map<?, ?>)) {
				this.raiseExceptionIllegalTypeField(FIELD_CURRENCIES + "_#_" + i, JSONTypes.Object, curr);
			}
			currArr[i] = this.currencyFromJSONMap(gm, (Map<String, Object>) curr);
		}
		this.setCurrencies(currArr);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}
		if (!jsonMap.containsKey(FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT)) {
			this.raiseExceptionMissingField(FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT, JSONTypes.Boolean);
		}
		Object valCFCCE = jsonMap.get(FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT);
		if (!(valCFCCE instanceof Boolean)) {
			this.raiseExceptionIllegalTypeField(FIELD_CAN_FIRE_CURRENCY_CHANGE_EVENT, JSONTypes.Boolean, valCFCCE);
		}
		this.setCanFireCurrencyChangeEvent((Boolean) valCFCCE);
		//
		if (!jsonMap.containsKey(FIELD_VALUES)) {
			this.raiseExceptionMissingField(FIELD_VALUES, JSONTypes.Object);
		}
		Object valValues = jsonMap.get(FIELD_VALUES);
		if (!(valValues instanceof int[])) {
			this.raiseExceptionIllegalTypeField(FIELD_VALUES, JSONTypes.ArrayHomogeneousType, valValues);
		}
		this.setValues((int[]) valValues);
		//
		if (!jsonMap.containsKey(FIELD_CURRENCIES)) {
			this.raiseExceptionMissingField(FIELD_CURRENCIES, JSONTypes.Object);
		}
		Object valCurr = jsonMap.get(FIELD_CURRENCIES);
		if (!(valCurr instanceof Object[])) {
			this.raiseExceptionIllegalTypeField(FIELD_CURRENCIES, JSONTypes.ArrayHomogeneousType, valCurr);
		}
		Object[] valCurrArr = (Object[]) valCurr;
		loadFromJSONArray(gm, valCurrArr);
	}

}