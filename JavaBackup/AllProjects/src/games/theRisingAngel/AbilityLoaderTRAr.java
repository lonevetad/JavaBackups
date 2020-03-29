package games.theRisingAngel;

import games.generic.controlModel.GController;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.misc.LoaderAbilities;
import games.generic.controlModel.misc.ObjGModalityBasedProvider;
import games.theRisingAngel.abilities.ADamageReductionCurrencyBased;
import games.theRisingAngel.abilities.AMoreDamageReceivedMoreLifeRegen;

public class AbilityLoaderTRAr extends LoaderAbilities {

	public AbilityLoaderTRAr(ObjGModalityBasedProvider<AbilityGeneric> objProvider) {
		super(objProvider);
	}

	@Override
	public void loadInto(GController gcontroller) {
		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Physical));
		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
				gc -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Magical));
		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, gc -> new AMoreDamageReceivedMoreLifeRegen());

	}

}