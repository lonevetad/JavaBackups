package games.generic.controlModel.holders;

import games.generic.controlModel.currency.CurrencySet;

public interface CurrencyHolder {
	public CurrencySet getCurrencies();

	public void setCurrencies(CurrencySet currencies);
}