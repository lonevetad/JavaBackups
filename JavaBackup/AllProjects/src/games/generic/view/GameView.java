package games.generic.view;

import java.util.List;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GameLauncher;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.loaders.LoaderManager.LoadingObserver;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.view.misc.LoadingProcessView;

/**
 * Super uber maxi big container of ALL GUI (Graphic User Interface) stuff (in
 * fact, it's instantiated by {@link GameLauncher}).
 */
public abstract class GameView {

	public GameView(GController gc) {
		super();
		this.gameController = gc;
		this.viewOptions = this.newViewOptions();
	}

	protected final GController gameController;
	protected final ViewOptions viewOptions;
	protected LoadingProcessView loadingProcessView;

	//

	// TODO PROXY

	/** Proxy method, beware of nulls. */
	public GModality getCurrentModality() {
		return gameController.getCurrentGameModality();
	}

	public GController getGameController() { return gameController; }

	public ViewOptions getViewOptions() { return viewOptions; }

	/** Proxy method, beware of nulls. */
	public PlayerGeneric getCurrentPlayerInGame() {
		return gameController.getCurrentGameModality().getPlayer();
	}

	public LoadingProcessView getLoadingProcessView() { return loadingProcessView; }

	//

	// NEW methods

	protected abstract ViewOptions newViewOptions();

	protected abstract LoadingProcessView newLoadingProcessView();

	//

	// TODO abstract methods

	//

	/**
	 * Instantiate some non-GUI objects, like those returned by
	 * {@link GameView#getAllViewRelatedLoaders()}
	 */
	public abstract void initializeNonGUIParts();

	/**
	 * Initialize the whole GUI not already created BUT not those components created
	 * during the {@link #initializeNonGUIParts()} nor the component returned by
	 * {@link #newLoadingProcessView()}.
	 */
	public abstract void continueViewInitialization();

	/**
	 * Last step of the {@link #initAndShow()} process: set up the View after all
	 * loadings and instantiations and shows the result.
	 */
	public abstract void finishViewSetupAndShow();

	/**
	 * Open a GUI menu' or do something else. It should be done in a separate
	 * {@link Thread} <br>
	 * See also
	 * {@link GController#beginsPlayesInteraction(PlayerGeneric, PlayerGeneric)}.
	 *
	 * @param thisPlayer
	 * @param otherPlayer
	 */
	public abstract void beginsPlayesInteraction(PlayerGeneric thisPlayer, PlayerGeneric otherPlayer);

	public abstract List<LoaderGeneric> getAllViewRelatedLoaders();

	public abstract List<LoadingObserver> getAllLoadersObservers();

	//

	// TODO base implementations

	//

	/**
	 * Initialize the whole GUI .
	 * <p>
	 * These are the steps, more or less, the following:
	 * <ol>
	 * <li>create a {@link LoadingProcessView} through
	 * {@link #newLoadingProcessView()}.</li>
	 * <li>invokes {@link #initializeNonGUIParts()}</li>
	 * <li>invokes {@link #continueViewInitialization()}</li>
	 * <li>add all {@link LoaderGeneric} returned by
	 * {@link #getAllViewRelatedLoaders()} to the loading process using
	 * {@link GController#addLoader(games.generic.controlModel.misc.LoaderGeneric)}.
	 * </li>
	 * <li>add all {@link LoadingObserver} returned by
	 * {@link #getAllLoadersObservers()} to the observers set through
	 * {@link GController#addLoadingProcessObserver(LoadingObserver)}.</li>
	 * <li>invokes {@link #finishViewSetupAndShow()} as last step.</li>
	 * </ol>
	 */
	public final void initAndShow() {
		List<LoaderGeneric> loaders;
		List<LoadingObserver> loadersObservers;

		this.loadingProcessView = this.newLoadingProcessView();

		this.gameController.addLoadingProcessObserver(this.loadingProcessView);

		this.initializeNonGUIParts();
		this.continueViewInitialization();

		loaders = this.getAllViewRelatedLoaders();
		if (loaders != null) { loaders.forEach(this.gameController::addLoader); }

		loadersObservers = this.getAllLoadersObservers();
		if (loadersObservers != null) { loadersObservers.forEach(this.gameController::addLoadingProcessObserver); }

		this.finishViewSetupAndShow();
	}
}