package games.theRisingAngel.loaders;

import java.util.List;
import java.util.Map;

import games.generic.controlModel.GController;
import games.generic.controlModel.loaders.LoaderConfigurations;
import games.generic.controlModel.loaders.LoaderGMod;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.loaders.LoaderManager;
import games.generic.controlModel.loaders.LoaderUniqueIDProvidersState;
import games.generic.mods.GModInterface;
import games.theRisingAngel.GControllerTRAn;
import games.theRisingAngel.providers.GameObjectsProvidersHolderTRAn;

public class LoaderManagerTRAn extends LoaderManager {

	public LoaderManagerTRAn(GController gameController) { super(gameController); }

	@Override
	protected LoaderUniqueIDProvidersState newLoaderUniqueIDProvidersState() { // TODO Auto-generated method stub
		return new LoaderUniqueIDProvidersStateTRAn();
	}

	@Override
	protected LoaderConfigurations newLoaderConfigurations() { return new LoaderConfigurationsTRAn(); }

	@Override
	protected void enrichSetLoaderManagers(Map<Class<?>, LoaderGeneric> loaders) {
		GControllerTRAn gc;
		GameObjectsProvidersHolderTRAn goph;
		gc = (GControllerTRAn) this.gameController;
		goph = (GameObjectsProvidersHolderTRAn) gc.getGameObjectsProvidersHolder();

		loaders.put(LoaderAbilityTRAn.class, new LoaderAbilityTRAn(goph.getAbilitiesProvider()));
		loaders.put(LoaderEquipUpgradesTRAn.class, new LoaderEquipUpgradesTRAn(goph.getEquipUpgradesProvider()));
		loaders.put(LoaderItemsTRAn.class, new LoaderItemsTRAn(goph.getItemsProvider()));
		loaders.put(LoaderCreatureTRAn.class, new LoaderCreatureTRAn(goph.getCreaturesProvider()));
	}

	@Override
	protected LoaderGMod newLoaderGameMods() { // TODO Auto-generated method stub
		return new LoaderGMod(this.getGameController()) {

			@Override
			public LoadStatusResult loadInto(GController gc) { // TODO Auto-generated method stub
				System.out.println("mmmmmmmmmmmmmmmmmmmmMOD MANAGER TRAN");
				return LoadStatusResult.Success;
			}

			@Override
			public List<GModInterface> getAllLoadableGameMods() { // TODO Auto-generated method stub
				return null;
			}
		};
	}
}