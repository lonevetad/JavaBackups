package videogamesOldVersion.common;

import java.util.Map;

import videogamesOldVersion.common.abstractCommon.AbstractMapGame;
import videogamesOldVersion.common.abstractCommon.GameMechanismType;
import videogamesOldVersion.common.abstractCommon.GameModelGeneric;
import videogamesOldVersion.common.abstractCommon.MainController;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.ThreadsHolder;
import videogamesOldVersion.common.mainTools.RunnableSuspendible;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObserverMolm;

/**
 * This class holds all of the implementation of the game.<br>
 * Holds also the {@link GameModelGeneric} and game's threads/Runners and their
 * implementations.
 */
public abstract class GameMechanism implements ThreadsHolder {
	private static final long serialVersionUID = 95841061984911L;

	public GameMechanism() {
		// setObservableMolmManager(newMOLMManagerObservable());
		setGameModel(newGameModelGeneric());
		observableMolmManager = getNewMolmManagerObservable();
		if (observableMolmManager == null)
			throw new NullPointerException("MolmManagerObservable is null on costructor-time.");
	}

	public GameMechanism(MainController mainController) {
		this();
		this.mainController = mainController;
	}

	// model
	protected int gameTypeOrdinal;
	protected GameModelGeneric gameModel;
	protected MOLMManagerObservable observableMolmManager;
	// to let it work
	protected transient MainController mainController;
	protected transient Thread threadGame;
	protected transient RunnableSuspendible runnableGame;

	//

	// TODO GETTER

	/**
	 * A map game could be specifically created for a specific kind of game, so that
	 * map is related to a specific instance of {@link GameMechanism}.<br>
	 * Usually, to specify the "default" GameMechanism, <code>0</code> or
	 * <code>-1</code> should be returned;
	 * <p>
	 * This value could be taken by an instance of {@link GameMechanismType}.
	 */
	public int getGameTypeOrdinal() {
		return gameTypeOrdinal;
	}

	public MainController getMainController() {
		return mainController;
	}

	public MOLMManagerObservable getObservableMolmManager() {
		return observableMolmManager;
	}

	public GameModelGeneric getGameModel() {
		return gameModel;
	}

	@Override
	public Map<Long, Thread> getPoolThread() {
		return mainController.getPoolThread();
	}

	//

	// TODO SETTER

	/** See {@link #getGameTypeOrdinal()}. */
	public GameMechanism setGameTypeOrdinal(int gameTypeOrdinal) {
		this.gameTypeOrdinal = gameTypeOrdinal;
		return this;
	}

	public GameMechanism setMainController(MainController mainController) {
		this.mainController = mainController;
		return this;
	}

	public GameMechanism setGameModel(GameModelGeneric gameModel) {
		this.gameModel = gameModel;
		return this;
	}

	@Override
	public ThreadsHolder setPoolThread(Map<Long, Thread> poolThread) {
		return mainController.getGameMechanism();
	}

	public GameModelGeneric setMapGameCurrent(AbstractMapGame mapGameCurrent) {
		// AbstractMOLMManager mm;
		GameModelGeneric gmg;
		gmg = gameModel.setMapGameCurrent(mapGameCurrent);
		/*
		 * mm = mapGameCurrent.getMolmManagerDelegated(); if (mm instanceof
		 * MOLMManagerObservable) this.observableMolmManager = (MOLMManagerObservable)
		 * mm;
		 */
		gmg.setMolmManagerDelegated(getObservableMolmManager());
		return gmg;
	}

	//

	// TODO ABSTRACT

	protected abstract RunnableSuspendible newRunnableGame();

	// protected abstract MOLMManagerObservable newMOLMManagerObservable();

	protected abstract GameModelGeneric newGameModelGeneric();

	// TODO PUBLIC

	/**
	 *
	 * @since 06 April 2018
	 */
	public MOLMManagerObservable getNewMolmManagerObservable() {
		return new MOLMManagerObservable();
	}

	public AbstractMapGame newMapGame(String mapName) {
		return newMapGame(mapName, null);
	}

	public AbstractMapGame newMapGame(String mapName, ObserverMolm obs) {
		return newMapGame(mapName, obs, MOLMManager.DEFAULT_MOLM_WIDTH_MICROPIXEL,
				MOLMManager.DEFAULT_MOLM_HEIGHT_MICROPIXEL);
	}

	public AbstractMapGame newMapGame(String mapName, ObserverMolm obs, int widthMicropixel, int heightMicropixel) {
		MapGame.MapGameBuilder mgb;
		mgb = MapGame.startBuilding(this.mainController, mapName);
		mgb.setMolmManagerDelegated(getObservableMolmManager());
		if (obs != null)
			mgb.addMolmObserver(obs);
		return mgb.setReallocMolmsNeeded(true).setSizeMicropixel(widthMicropixel, heightMicropixel)
				.setGameTypeOrdinal(gameTypeOrdinal).build();
	}

	public AbstractMapGame setNewMapGame(String mapName) {
		return setNewMapGame(mapName, null);
	}

	public AbstractMapGame setNewMapGame(String mapName, ObserverMolm obs) {
		return setNewMapGame(mapName, obs, MOLMManager.DEFAULT_MOLM_WIDTH_MICROPIXEL,
				MOLMManager.DEFAULT_MOLM_HEIGHT_MICROPIXEL);
	}

	public AbstractMapGame setNewMapGame(String mapName, ObserverMolm obs, int widthMicropixel, int heightMicropixel) {
		AbstractMapGame mg;
		mg = newMapGame(mapName, obs, widthMicropixel, heightMicropixel);
		this.setMapGameCurrent(mg);
		return mg;
	}

	@Override
	public boolean allocThreads() {
		boolean allocated;
		allocated = false;
		if (runnableGame == null) {
			this.addThreadToPool(threadGame = new Thread(runnableGame = newRunnableGame()));
			allocated = true;
		}
		return allocated;
	}

	@Override
	public void destroyThreads() {
		pauseThreads();
		if (threadGame != null) {
			removeThreadFromPool(threadGame);
			threadGame = null;
			runnableGame = null;
		}

	}

	@Override
	public void startThreads() {
		mainController.startThread(threadGame);
	}

	@Override
	public void pauseThreads() {
		if (runnableGame != null)
			runnableGame.suspendRunnable();
	}

	@Override
	public void resumeThreads() {
		if (runnableGame != null)
			runnableGame.resumeRunnable();
	}

}