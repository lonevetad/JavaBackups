package tests.tGame;

import java.util.Map;
import java.util.function.Consumer;

import games.generic.controlModel.GController;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.theRisingAngel.loaders.LoaderManagerTRAn;
import tests.tGame.tgEvent1.GC_E1;

public class LoaderManagerTRAnE_E1 extends LoaderManagerTRAn {

	public LoaderManagerTRAnE_E1(GController gameController) {
		super(gameController);
	}

	@Override
	protected void defineAdditionalPrimaryLoaders(Consumer<LoaderGeneric> loaderAdder) {
		this.getGameController().getLogger().log("LoaderManagerTRAnE_E1 calling defineAdditionalPrimaryLoaders");
		loaderAdder.accept(new LoaderDefiningDummyGModality(this));
	}

	@Override
	protected void enrichSetLoaderManagers(Map<Class<?>, LoaderGeneric> loaders) {
		this.getGameController().getLogger().log("LoaderManagerTRAnE_E1 calling enrichSetLoaderManagers");
		super.enrichSetLoaderManagers(loaders);
	}

	//

	public static class LoaderDefiningDummyGModality extends LoaderGeneric {

		public static final String LOADER_NAME_LoaderDefiningDummyGModality = "LoaderDefiningDummyGModality";

		@Override
		public String getNameID() {
			return LOADER_NAME_LoaderDefiningDummyGModality;
		}

		protected LoaderManagerTRAnE_E1 loaderManager_E1;

		public LoaderDefiningDummyGModality(LoaderManagerTRAnE_E1 loaderManager_E1) {
			super();
			this.loaderManager_E1 = loaderManager_E1;
		}

		void log(String t) {
			this.loaderManager_E1.getGameController().getLogger().log(t);
		}

		@Override
		public LoadStatusResult loadInto(GController gc) {
			if (gc instanceof GC_E1 thisGC_E1) {
				this.log(LOADER_NAME_LoaderDefiningDummyGModality + " creating new GModality ");
				GModality_E1 gme1 = (GModality_E1) thisGC_E1.newModalityByName(GModality_E1.NAME);
				this.log(LOADER_NAME_LoaderDefiningDummyGModality + " GModality created! setting it to GController_E1");
				thisGC_E1.setDummyGameModality_E1(gme1);
				this.log(LOADER_NAME_LoaderDefiningDummyGModality + " loading done ^^");
				return LoadStatusResult.Success;
			}
			throw new RuntimeException("What class is this Game Controller? " + gc.getClass().getName());
		}

	};
}
