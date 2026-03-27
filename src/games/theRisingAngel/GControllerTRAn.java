package games.theRisingAngel;

import games.generic.GameOptions;
import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.factories.GModalityFactory;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.loaders.LoaderManager;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GControllerRPG;
import games.theRisingAngel.loaders.LoaderManagerTRAn;
import games.theRisingAngel.providers.GameObjectsProvidersHolderTRAn;

public class GControllerTRAn extends GControllerRPG {

	public GControllerTRAn() {
		super();
		// this.initNonFinalStuffs(); delegated to the loader
	}

	//

	//

	//

	@Override
	protected GameOptions newGameOptions() {
		return new GameOptionsTRAn(this);
	}

	@Override
	protected void defineGameModalitiesFactories() {
		System.out.println("DEFINE GAME MODALITIES FACTORIES IN GControllerTRAn");
		this.addGameModalityFactory( //
				new GModalityFactory(GModalityTRAnBaseWorld.NAME) {
					@Override
					public GModality newGameModality(GController gc, String gameModalityName) {
						return new GModalityTRAnBaseWorld(gc, gameModalityName);
					}

				});
	}

	@Override
	protected GameObjectsProvidersHolderRPG newSharedGameObjectProvidersHolder() {
		return new GameObjectsProvidersHolderTRAn(null);
	}

	@Override
	protected LoaderManager newLoaderManager() {
		return new LoaderManagerTRAn(this);
	}

	@Override
	protected UserAccountGeneric newUserAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initNonFinalStuffs() {
		getSharedGameObjectsProvidersHolder().setGameModality(getCurrentGameModality());

		super.initNonFinalStuffs();
		System.out.println("GControllerTRAn init non final stuff done\n\n");
	}

	@Override
	public void prepareLoadingAll() {
		super.prepareLoadingAll();
	}

	@Override
	public void afterLoadingAll() {
	}

}