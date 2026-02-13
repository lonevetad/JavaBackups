package games.theRisingAngel.enums;

import games.generic.controlModel.misc.Currency;
import tools.UniqueIDProvider;

public enum CurrenciesTRAn implements Currency {
	Ottinidi, SpiritualEssence;

	public static final UniqueIDProvider CURRENCY_ID_PROVIDER;
	public static final CurrenciesTRAn[] CURRENCIES;
	public static final IndexToObjectBackmapping INDEX_TO_CURRENCY_TRAn;

	static {
		CURRENCY_ID_PROVIDER = UniqueIDProvider.newBasicIDProvider();
		CURRENCIES = CurrenciesTRAn.values();
		for (CurrenciesTRAn c : CURRENCIES) {
			c.ID = CURRENCY_ID_PROVIDER.getNewID();
		}
		INDEX_TO_CURRENCY_TRAn = (int i) -> CURRENCIES[i];
	}

	protected Long ID = null;

	@Override
	public int getIndex() { return this.ordinal(); }

	@Override
	public Long getID() { return this.ID; }

	@Override
	public String getName() { return this.name(); }

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() { return INDEX_TO_CURRENCY_TRAn; }

	@Override
	public boolean setID(Long newID) {
		if (newID == null || newID == this.ID || (this.ID != null && this.ID.longValue() == newID.longValue())) {
			return false;
		}
		this.ID = newID;
		return true;
	}
}
