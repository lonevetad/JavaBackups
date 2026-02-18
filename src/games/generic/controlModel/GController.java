package games.generic.controlModel;

import java.util.List;
import java.util.Map;
import java.util.Random;

import dataStructures.MapTreeAVL;
import games.generic.GameOptions;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.holders.ProbabilityOfContextesHolders;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.loaders.LoaderManager;
import games.generic.controlModel.loaders.LoaderManager.LoadingObserver;
import games.generic.controlModel.misc.GModalityFactory;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.view.GameView;
import tools.Comparators;
import tools.LoggerMessages;

/**
 * One of the Core classes.<br>
 * Layer between the View and the actual game: {@link GModality} instances.
 * There lies ALL game's implementations (i.e.: when the player "plays", what
 * he/she can do depends on {@link GModality}s' implementations. Think about
 * playing cards, RPG or driving vehicles).<br>
 * The Controller manages:
 * <ul>
 * <li>Settings</li>
 * <li>Saves</li>
 * <li>Player's data</li>
 * <li>Chat and other media channels</li>
 * <li>{@link GModality}, creating and destroying them</li>
 * <li>Connections: Internet, DataBase, banks, etc</li>
 * <li>If available, markets, editors, etc</li>
 * <li>etc</li>
 * </ul>
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link }></li>
 * </ul>
 */
public abstract class GController {

	protected boolean isAlive;
	protected Map<String, GModalityFactory> gameModalitiesFactories;

	protected final LoggerMessages logger;
	protected final GameOptions gameOptions;
	protected final LoaderManager loaderManager;
	protected UserAccountGeneric user;
	protected GModality currentGameModality;
	protected ProbabilityOfContextesHolders probabilityOfContextesHolders;
	public static final Random RANDOM = new Random();

	/** Create everything and loads everything as well. */
	public GController() { this(null); }

	public GController(LoggerMessages logger) {
		this.isAlive = false;
		this.logger = LoggerMessages.loggerOrDefault(logger);
		this.loaderManager = this.newLoaderManager();
		this.gameOptions = this.newGameOptions();
	}

	//

	public GModality getCurrentGameModality() { return currentGameModality; }

	public Map<String, GModalityFactory> getGameModalitiesFactories() { return gameModalitiesFactories; }

	public ProbabilityOfContextesHolders getProbabilityOfContextesHolders() { return probabilityOfContextesHolders; }

//	public List<LoaderGameObjects<? extends ObjectNamed>> getGameObjectsLoader() { return gameObjectsLoader; }

	public GameOptions getGameOptions() { return gameOptions; }

	public UserAccountGeneric getUser() { return user; }

	public static Random getRandom() { return RANDOM; }

	public LoggerMessages getLogger() { return logger; }

	public LoaderManager getLoaderManager() { return loaderManager; }

	//

	public void setCurrentGameModality(GModality currentGameModality) {
		this.currentGameModality = currentGameModality;
	}

	//

	//

	// TODO NEW-related methods

	/**
	 * Add all {@link GModalityFactory}} to the set of possible modalities,
	 * identified by {@link #getGameModalitiesFactories()}.
	 */
	protected abstract void defineGameModalitiesFactories();

	/**
	 * Returns a new {@link GameOptions} instance.
	 *
	 * @return
	 */
	protected abstract GameOptions newGameOptions();

	protected abstract LoaderManager newLoaderManager();

	/** See {@link UserAccountGeneric} to see what is meant. */
	protected abstract UserAccountGeneric newUserAccount();

	/**
	 * Defines and return a specific instance of {@link GameObjectsProvidersHolder}
	 * for a particular subclass of {@link GModality}: some games could define
	 * different sets of game objects, based on the modality they are destined to be
	 * used.<br>
	 * Usually, this method just return the value of a single variable.
	 * <p>
	 * For instance, the game "The Rising Army" will differs two game modalities:
	 * one having some degree of randomness, the other one that is totally
	 * deterministic and aspects like damage ranges or "something that scatters
	 * randomly, like cluster bombs" are fixed (for instance, each grapes of cluster
	 * bombs expands in symmetrically and radially). So, there would exists
	 * abilities designed in a parallel way so that they could have random intervals
	 * or fixed values. (In that case, it's advised to define the ability only once
	 * and only one {@link GameObjectsProvidersHolder}, just passing to that
	 * ability's constructor the flag about if the game modality allows randomness
	 * or not).
	 */
	protected abstract GameObjectsProvidersHolder newGameObjectProvidersHolderFor(GModality gm);

	//

	// TODO INITIALIZATION METHODS

	//

	/**
	 * Override designed BUT call <code>super.</code>{@link #initNonFinalStuffs()}}.
	 */
	protected void initNonFinalStuffs() {
		this.probabilityOfContextesHolders = new ProbabilityOfContextesHolders();
		this.gameModalitiesFactories = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				Comparators.STRING_COMPARATOR);
		defineGameModalitiesFactories();
		user = this.newUserAccount();
	}

//	public <E extends ObjectNamed> void addGameObjectLoader(LoaderGameObjects<E> ol) { this.gameObjectsLoader.add(ol); }

	/**
	 * Delegates to {@link LoaderManager#addLoader(LoaderGeneric)}).
	 */
	public void addLoader(games.generic.controlModel.loaders.LoaderGeneric loader) {
		if (loader != null) { this.loaderManager.addLoader(loader); }
	}

	/**
	 * Delegates to {@link LoaderManager#addLoadingProcessObserver(LoaderGeneric)}).
	 */
	public void addLoadingProcessObserver(LoadingObserver lo) {
		if (lo != null) { loaderManager.addLoadingProcessObserver(lo); }
	}

	/**
	 * Delegates to {@link LoaderManager#loadAll()}).
	 */
	public final List<LoaderGeneric> loadAll() { return this.loaderManager.loadAll(); }

//

	//

	//

	public boolean isAlive() { return isAlive; }

	public boolean isPlaying() {
		GModality gm;
		gm = this.getCurrentGameModality();
		return gm != null && gm.isRunning();
	}

	/**
	 * The name must be selected from the keys used during the call
	 * {@link #defineGameModalitiesFactories()} or, at least, contained by
	 * {@link #getGameModalitiesFactories()}.
	 */
	public GModality newModalityByName(String name) {
		GModalityFactory gmf;
		gmf = this.getGameModalitiesFactories().get(name);
		return gmf == null ? null : gmf.newGameModality(this, name);
	}

	/**
	 * Override designed. BUT call <code>super.</code>{@link #startGame()}}.<br>
	 * Starts the current {@link GModality}.
	 * <p>
	 * Call {@link #setCurrentGameModality(GModality)}} before invoking it.
	 */
	public void startGame() {
		this.isAlive = true;
		this.getCurrentGameModality().startGame();
		this.resumeGame(); // make it run, added on 19/03/2020
	}

	/** Override designed BUT call <code>super.</code>{@link #pauseGame()}}. */
	public void pauseGame() {
		this.getCurrentGameModality().pause();
	}

	/** Override designed BUT call <code>super.</code>{@link #resumeGame()}}. */
	public void resumeGame() {
		this.getCurrentGameModality().resume();
	}

	/**
	 * DESTRY EVERYTHING WITH DOUBLE OF THANOS'S EFFICEINCY.
	 * <p>
	 * Remember to set the flag {@link #isAlive} to <code>false</code>.
	 */
	public void closeAll() {
		this.isAlive = false;
		if (this.getCurrentGameModality() != null) {
			this.getCurrentGameModality().closeAll();
			this.setCurrentGameModality(null);
		}
	}

	//

	/**
	 * Begins the interaction between players. May notify the {@link GameView}
	 *
	 * @param thisPlayer  the current player, the one playing
	 * @param otherPlayer another player from somewhere else (a remote one, maybe)
	 */
	public void beginsPlayesInteraction(PlayerGeneric thisPlayer, PlayerGeneric otherPlayer) {

	}

	//

}