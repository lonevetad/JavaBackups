package games.theRisingAngel.loaders;

import games.generic.controlModel.GController;
import games.generic.controlModel.loaders.LoaderGameObjects;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;

public class LoaderCreatureTRAn extends LoaderGameObjects<BaseCreatureRPG> {

	public LoaderCreatureTRAn(GameObjectsProvider<BaseCreatureRPG> objProvider) { super(objProvider); }

	@Override
	public LoadStatusResult loadInto(GController gc) {
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Physical));
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Magical));
//		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, gmm -> new AMoreDamageReceivedMoreLifeRegen());
		return LoadStatusResult.Success;
	}
}