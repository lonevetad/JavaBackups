package games.generic.controlModel.misc;

import java.util.Objects;

import games.generic.controlModel.GModality;

public abstract class CurrencySet {
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
		if (currencies.length < 1)
			throw new IllegalArgumentException("Cannot exist no currency types, just don't create me instead!");
		this.gameModality = gameModality;
		this.currencies = currencies;
		this.values = new int[currencies.length];
		this.canFireCurrencyChangeEvent = false;
	}

	protected boolean canFireCurrencyChangeEvent;
	protected final int[] values;
	protected final Currency[] currencies;
	protected GModality gameModality;

	//

	public GModality getGameModality() { return gameModality; }

	public int getCurrencyAmount(Currency c) { return this.values[c.getIndex()]; }

	public boolean isFiringEvents() { return canFireCurrencyChangeEvent && gameModality != null; }

	public boolean canFireCurrencyChangeEvent() { return canFireCurrencyChangeEvent; }

	public Currency[] getCurrencies() { return currencies; }

	//

	public void setCanFireCurrencyChangeEvent(boolean canFireCurrencyChangeEvent) {
		this.canFireCurrencyChangeEvent = canFireCurrencyChangeEvent;
	}

	public void setGameModality(GModality gameModality) { this.gameModality = gameModality; }

	public void setGameModaliy(GModality gameModality) { this.gameModality = gameModality; }

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
		if (isFiringEvents())
			fireCurrencyChangeEvent(this.gameModality, c, old, newAmount);
	}

	//

	//

	/**
	 * Shorthand to
	 * <code>{@link #setCurrencyAmount(int, int)}( indexType, {@link #getCurrencyAmount(int)}(indexType) + delta)</code>
	 */
	public void alterCurrencyAmount(Currency c, int delta) {
		int old, newAmount, indexCurrency;
		indexCurrency = c.getIndex();
		old = this.values[indexCurrency];
		this.values[indexCurrency] = newAmount = (old + delta);
		if (isFiringEvents())
			fireCurrencyChangeEvent(this.gameModality, c, old, newAmount);
	}

	public void forEachCurrency(CurrencyAmountConsumer citac) {
		int i, n;
		n = this.values.length;
		i = -1;
		while (++i < n)
			citac.performAction(currencies[i], this.values[i]);
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

}