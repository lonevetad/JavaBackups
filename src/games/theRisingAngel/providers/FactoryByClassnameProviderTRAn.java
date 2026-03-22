package games.theRisingAngel.providers;

import games.generic.controlModel.GModality;
import games.generic.controlModel.providers.FactoryByClassnameProvider;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EquipmentUpgradeCategory;
import games.theRisingAngel.enums.RaritiesTRAn;
import games.theRisingAngel.enums.RechargeableResourcesTRAn;
import games.theRisingAngel.enums.TribesTRAn;

@Deprecated
public class FactoryByClassnameProviderTRAn extends FactoryByClassnameProvider {

	public FactoryByClassnameProviderTRAn() {
		super();
	}

	@Override
	public void loadAllFactories(GModality gm) {
		this.addFactory(AttributesTRAn.class, AttributesTRAn.FACTORY);
		this.addFactory(EquipmentUpgradeCategory.class, EquipmentUpgradeCategory.FACTORY);
		this.addFactory(RaritiesTRAn.class, RaritiesTRAn.FACTORY);
		this.addFactory(TribesTRAn.Tribe.class, TribesTRAn.Tribe.FACTORY);
		this.addFactory(RechargeableResourcesTRAn.class, RechargeableResourcesTRAn.FACTORY);
	}

}
