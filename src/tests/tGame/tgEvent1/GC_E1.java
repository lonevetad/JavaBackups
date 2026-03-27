package tests.tGame.tgEvent1;

import java.io.IOException;
import java.util.List;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.factories.GModalityFactory;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.loaders.LoaderGeneric.LoadStatusResult;
import games.generic.controlModel.loaders.LoaderManager;
import games.generic.controlModel.loaders.LoaderManager.LoadingObserver;
import games.theRisingAngel.GControllerTRAn;
import tests.tGame.GModality_E1;
import tests.tGame.LoaderManagerTRAnE_E1;
import tools.log.LoggerMessages;
import tools.log.LoggerOnFile;

public class GC_E1 extends GControllerTRAn {

	public GC_E1() {
		super();
		this.testLoaderManager = null;
	}

	protected LoaderManagerTRAnE_E1 testLoaderManager;

	@Override
	protected LoggerMessages newLogger(LoggerMessages log) {
		try {
			return (log != null) ? log : new LoggerOnFile();
		} catch (IOException e) {
			log = LoggerMessages.LOGGER_DEFAULT;
			e.printStackTrace();
			log.logException(e);
			return log;
		}
	}

	public GModality_E1 getDummyGameModality_E1() {
		return (GModality_E1) this.getCurrentGameModality();
	}

	public void setDummyGameModality_E1(GModality_E1 dummyGameModality_E1) {
		this.setCurrentGameModality(dummyGameModality_E1);
	}

	@Override
	protected void defineGameModalitiesFactories() {
		super.defineGameModalitiesFactories();
		this.addGameModalityFactory(//
				new GModalityFactory(GModality_E1.NAME) {
					@Override
					public GModality newGameModality(GController gc, String gameModalityName) {
						return new GModality_E1(gc);
					}
				});
	}

	@Override
	protected LoaderManager newLoaderManager() {
		this.testLoaderManager = new LoaderManagerTRAnE_E1(this);
		return this.testLoaderManager;
	}

	protected void onInstantiatedGModality_E1() {
		this.getSharedGameObjectsProvidersHolder().setGameModality(getDummyGameModality_E1());
	}

	@Override
	public void prepareLoadingAll() {
		super.prepareLoadingAll();
//		this.dummyGameModality_E1 = this.new

		this.addLoadingProcessObserver(new LoadingObserver() {

			@Override
			public void notifyLoadingProcessCompleted(LoaderGeneric loader, LoadStatusResult completitionResult) {
				logger.log("GC_E1 loader " + loader.getNameID() + " completed: " + completitionResult.name());
			}

			@Override
			public void notifyAllLoadingProcessStarted(int loadersAmount) {
				logger.log("GC_E1 started " + loadersAmount + " started.");

			}

			@Override
			public void notifyAllLoadingProcessEnded(List<LoaderGeneric> failedLoaders) {
				logger.log("GC_E1  " + failedLoaders.size() + " failed!: ");
				failedLoaders.forEach(lg -> logger.log("\t " + lg.getNameID()));
			}

			@Override
			public void notifyLoadingProcessStarted(LoaderGeneric loader) {
				logger.log("GC_E1 loader " + loader.getNameID() + " started.");
				if (LoaderManagerTRAnE_E1.LoaderDefiningDummyGModality.LOADER_NAME_LoaderDefiningDummyGModality
						.equals(loader.getNameID())) {
					// game modality has been instantiated
					onInstantiatedGModality_E1();
				}
			}
		});
	}

	@Override
	public void startGame() {
		super.startGame();
		// this.getCurrentGameModality().startGame(); REMOVED: already performed inside
		// "super.startGame();"
	}

	@Override
	public void closeAll() {
//		isAlive = false;
		super.closeAll();
//		this.getCurrentGameModality().closeAll(); // yet done in super
	}

}