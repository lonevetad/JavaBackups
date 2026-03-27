package games.theRisingAngel.loaders;

import java.util.List;
import java.util.function.Consumer;

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

	public LoaderManagerTRAn(GController gameController) {
		super(gameController);
	}

	@Override
	protected LoaderUniqueIDProvidersState newLoaderUniqueIDProvidersState() { // TODO Auto-generated method stub
		return new LoaderUniqueIDProvidersStateTRAn();
	}

	@Override
	protected LoaderConfigurations newLoaderConfigurations() {
		return new LoaderConfigurationsTRAn();
	}

	@Override
	protected void enrichSetLoaderManagers(Consumer<LoaderGeneric> loaderAdder) {
		GControllerTRAn gc;
		GameObjectsProvidersHolderTRAn goph;
		gc = (GControllerTRAn) this.gameController;
		goph = (GameObjectsProvidersHolderTRAn) gc.getSharedGameObjectsProvidersHolder();
		loaderAdder.accept(new LoaderAbilityTRAn(goph.getAbilitiesProvider()));
		loaderAdder.accept(new LoaderEquipUpgradesTRAn(goph.getEquipUpgradesProvider()));
		loaderAdder.accept(new LoaderItemsTRAn(goph.getItemsProvider()));
		loaderAdder.accept(new LoaderEquipTRAn(goph.getEquipmentsProvider()));
		loaderAdder.accept(new LoaderCreatureTRAn(goph.getCreaturesProvider()));
	}

	@Override
	protected LoaderGMod newLoaderGameMods() {
		return new LoaderGModTRAn(this.getGameController());
	}

	//

	public static class LoaderGModTRAn extends LoaderGMod {

		public static final String LOADER_NAME_LoaderGModTRAn = "LoaderGModTRAn";

		@Override
		public String getNameID() {
			return LOADER_NAME_LoaderGModTRAn;
		}

		public LoaderGModTRAn(GController gameController) {
			super(gameController);
		}

		@Override
		public LoadStatusResult loadInto(GController gc) {
			gc.getLogger().logAndPrint("LoaderGModTRAn loading ...\n");
			return LoadStatusResult.Success;
		}

		@Override
		public List<GModInterface> getAllLoadableGameMods() {
			throw new UnsupportedOperationException("Don't know how to do it at the moment ....");
		}
	}

}