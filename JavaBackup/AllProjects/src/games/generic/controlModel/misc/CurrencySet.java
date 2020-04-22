package games.generic.controlModel.misc;

import games.generic.controlModel.GModality;

public abstract class CurrencySet {
	public static final int BASE_CURRENCY_INDEX = 0;

	int[] values;
	GModality gm;

	/** The "typeAmount" parameter could be an enumeration's size. */
	public CurrencySet(GModality gm, int typesAmount) {
		this.gm = gm;
		if (typesAmount < 1)
			throw new IllegalArgumentException("Cannot exist no currency types, just don't create me instead!");
		this.values = new int[typesAmount];
	}

	//

	public GModality getGm() {
		return gm;
	}

	public int getMoneyAmount(int indexType) {
		return this.values[indexType];
	}

	//

	public void setGm(GModality gm) {
		this.gm = gm;
	}

	public void setMoneyAmount(int indexType, int newAmount) {
		int old;
		old = this.values[indexType];
		this.values[indexType] = newAmount;
		fireMoneyChangeEvent(this.gm, indexType, old, newAmount);
	}

	//

	public abstract void fireMoneyChangeEvent(GModality gm, int indexType, int oldValue, int newValue);
}