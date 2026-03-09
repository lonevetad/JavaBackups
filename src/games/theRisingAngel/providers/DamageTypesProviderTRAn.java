package games.theRisingAngel.providers;

import games.generic.controlModel.providers.DamageTypesProvider;
import games.theRisingAngel.enums.DamageTypesTRAn;

public class DamageTypesProviderTRAn extends DamageTypesProvider<DamageTypesTRAn> {

	public DamageTypesProviderTRAn() {
		super();
	}

	@Override
	public DamageTypesTRAn[] getEnumValues() {
		return DamageTypesTRAn.VALUES;
	}
}
