package games.generic.controlModel.gObj;

import games.generic.controlModel.misc.CurrencySet;

public interface CurrencyHolder {
	public CurrencySet getCurrencies();

	public void setCurrencies(CurrencySet currencies);
}