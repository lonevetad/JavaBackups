package games.generic.controlModel.loaders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GController;
import games.generic.controlModel.loaders.LoaderGeneric.LoadStatusResult;
import tools.Comparators;
import tools.LoggerMessages;

/**
 * A Manager that manages all {@link LoaderGeneric}, loads them and manages the
 * events during loading (like success / failure)
 *
 * <p>
 * NOTE: as stated in its JavaDocs, the loader
 * {@link LoaderUniqueIDProvidersState} HAS to be the first one
 *
 * @author ottin
 *
 */
public abstract class LoaderManager {
	public static final long MILLISECONDS_MAX_WAIT_LOADING_DEFAULT = 60000 * 15;

	/**
	 * Marks a class as observing the loading process performed by a
	 * {@link LoaderGeneric}.<br>
	 * The load process is the following
	 * <ol>
	 * <li>All {@link LoaderGeneric} start runnings notified by
	 * {@link #notifyAllLoadingProcessStarted(int)}.</li>
	 * <li>A {@link LoaderGeneric} completes, thus
	 * {@link #notifyLoadingProcessCompleted(LoaderGeneric, LoadStatusResult)} is
	 * invoked providing the loading result (success or fail, more or less).</li>
	 * <li>All {@link LoaderGeneric} has ended</li>
	 * </ol>
	 *
	 * @author ottin
	 *
	 */
	public static interface LoadingObserver extends Serializable {
		/**
		 * Notifies the observer (the class implementing this {@link LoadingObserver}
		 * instance.<br>
		 *
		 * @param loader
		 * @param completitionResult
		 */
		public void notifyLoadingProcessCompleted(LoaderGeneric loader,
				LoaderGeneric.LoadStatusResult completitionResult);

		/**
		 * Notify the start the loading process, providing the amount of loaders that
		 * will be run.
		 *
		 * @param loadersAmount the amount of loader that will run.
		 */
		public void notifyAllLoadingProcessStarted(int loadersAmount);

		/**
		 * Notify the end of the whole loading process. The parameter states what
		 * {@link LoaderGeneric} failed the loading process
		 */
		public void notifyAllLoadingProcessEnded(List<LoaderGeneric> failedLoaders);
	}

	//

	/**
	 * Define the {@link LoaderGeneric} priority. Those loaders has to be loaded
	 * BEFORE any other ones returned by {@link }
	 *
	 * @author ottin
	 *
	 */
	public static enum LoadersPrimary {
		UIDPState, Configurations, GameMods
	}

	//

	public LoaderManager(GController gameController) {
		this.loadOnlyOnce = true;
		this.hasAlreadyLoaded = false;
		this.gameController = gameController;

		this.prioritaryLoaders = new LoaderGeneric[LoaderManager.LoadersPrimary.values().length];
		this.prioritaryLoaders[LoadersPrimary.UIDPState.ordinal()] = this.newLoaderUniqueIDProvidersState();
		this.prioritaryLoaders[LoadersPrimary.Configurations.ordinal()] = this.newLoaderConfigurations();
		this.prioritaryLoaders[LoadersPrimary.GameMods.ordinal()] = this.newLoaderGameMods();

		this.poolParallelLoaders = null;
		this.otherLoaders = new ArrayList<>();
		this.loadersAndStatusObservers = new LinkedList<>();

		this.millisecondsMaximumWaitLoadingProcess = MILLISECONDS_MAX_WAIT_LOADING_DEFAULT;
//		this.addLoader(this.gameModsLoader);
	}

	protected boolean loadOnlyOnce, hasAlreadyLoaded;
//	protected final List<LoaderGameObjects<? extends ObjectNamed>> gameObjectsLoader;
	protected final GController gameController;
	protected final LoaderGeneric[] prioritaryLoaders;
	protected final List<LoaderGeneric> otherLoaders;
	protected final List<LoadingObserver> loadersAndStatusObservers;
	protected ExecutorService poolParallelLoaders;
	protected long millisecondsMaximumWaitLoadingProcess;

	// getters

	public GController getGameController() { return gameController; }

	public List<LoaderGeneric> getLoaders() { return otherLoaders; }

	public boolean isLoadOnlyOnce() { return loadOnlyOnce; }

	public boolean isHasAlreadyLoaded() { return hasAlreadyLoaded; }

	public LoaderUniqueIDProvidersState getLoaderUIDPStates() {
		return (LoaderUniqueIDProvidersState) this.prioritaryLoaders[LoadersPrimary.UIDPState.ordinal()];
	}

	public LoaderConfigurations getLoaderConfigurations() {
		return (LoaderConfigurations) prioritaryLoaders[LoadersPrimary.Configurations.ordinal()];
	}

	public LoaderGMod getLoaderGameMods() { return (LoaderGMod) prioritaryLoaders[LoadersPrimary.GameMods.ordinal()]; }

	public ExecutorService getPoolParallelLoaders() { return poolParallelLoaders; }

	public long getMillisecondsMaximumWaitLoadingProcess() { return millisecondsMaximumWaitLoadingProcess; }

	// setters

	public void setMillisecondsMaximumWaitLoadingProcess(long millisecondsMaximumWaitLoadingProcess) {
		this.millisecondsMaximumWaitLoadingProcess = millisecondsMaximumWaitLoadingProcess;
	}

	public void setLoadOnlyOnce(boolean loadOnlyOnce) { this.loadOnlyOnce = loadOnlyOnce; }

	//

	// TODO

	//

	//

	// TODO NEW methods

	//

	protected abstract LoaderUniqueIDProvidersState newLoaderUniqueIDProvidersState();

	protected abstract LoaderConfigurations newLoaderConfigurations();

	protected abstract LoaderGMod newLoaderGameMods();

	/**
	 * Create and returns an {@link ExecutorService} used to perform the parallel
	 * loading of all {@link LoaderGeneric} (added through {@link}
	 *
	 * @return
	 */
	protected ExecutorService newExecutorForParallelLoading() { return Executors.newWorkStealingPool(); }

	//

	// TODO ABSTRACT methods

	/**
	 * Override designed BUT call for <i>super</i>.
	 *
	 * @param loaders
	 */
	protected abstract void enrichSetLoaderManagers(Map<Class<?>, LoaderGeneric> loaders);

	//

	/**
	 * Creates all ---{@link LoaderGeneric} instances and add them using
	 * {@link #addLoader(LoaderGeneric)}.
	 */
//	protected abstract List<LoaderGeneric> getAllKnownLoaders();

	//

	// TODO PUBLIC

	//

	/**
	 * Add the provided loader to the list of loaders (returned by
	 * {@link #getLoaders()}).
	 *
	 * @param loader the loader that is desired to be run
	 */
	public void addLoader(LoaderGeneric loader) { if (loader != null) { this.otherLoaders.add(loader); } }

	public void addLoadingProcessObserver(LoadingObserver lo) {
		if (lo != null) { this.loadersAndStatusObservers.add(lo); }
	}

	/**
	 * Delegates to {@link LoaderManager#loadAll()}).
	 */
	public final void defineSetLoaders() {
		Map<Class<?>, LoaderGeneric> loaders;
		loaders = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.CLASS_COMPARATOR);

		enrichSetLoaderManagers(loaders);

		loaders.forEach((c, loader) -> { this.addLoader(loader); });
	}

	//

	// TODO LOAD methods

	//

	/**
	 * Should NOT be overrided.
	 * <p>
	 * Load everything that needs to be loaded. Return whether the whole loading
	 * process is successful.
	 *
	 * @return as stated here
	 *         {@link #manageLoadersStatusResults(LoadStatusResult[])}, it returns
	 *         the list of {@link LoaderGeneric} that failed the loading process: if
	 *         empty or null, then the whole loading process was successful and no
	 *         loader failed.
	 */
	public List<LoaderGeneric> loadAll() {
		List<LoaderGeneric> failedLoaders;
		final LoaderGeneric.LoadStatusResult[] results;
		final BiConsumer<LoaderGeneric, LoaderGeneric.LoadStatusResult> allObserversNotifier;
		int[] index = { 0 };

		System.out.println("\n\n\n\n start loading :D \n\n\n");
		if (loadOnlyOnce && this.hasAlreadyLoaded) { return null; }

//		getAllKnownLoaders().forEach(this::addLoader);

		this.defineSetLoaders();

		results = new LoaderGeneric.LoadStatusResult[this.otherLoaders.size()];

		allObserversNotifier = (loader, status) -> {
			try {
				this.loadersAndStatusObservers.forEach(lo -> lo.notifyLoadingProcessCompleted(loader, status));
			} catch (Exception e) {
				this.gameController.getLogger().logException(e);
			}
		};

		this.loadersAndStatusObservers
				.forEach(lo -> lo.notifyAllLoadingProcessStarted(results.length + this.prioritaryLoaders.length));

		int ii = 0;
		for (LoaderGeneric loaderPrioritary : this.prioritaryLoaders) {
			LoadStatusResult res;

			System.out.println("\nLOADING prioritary: " + ii++ + ", "
					+ (loaderPrioritary == null ? "NULL!!" : loaderPrioritary.getClass().getName()));

			res = loaderPrioritary.loadInto(this.gameController);
			if (res == LoadStatusResult.CriticalFail) {
				throw new RuntimeException("The loader " + loaderPrioritary.getClass() + " failed loading.");
			}
			allObserversNotifier.accept(loaderPrioritary, res);
		}

		// now non-prioritary loaders
		System.out.println("this.otherLoaders size: " + this.otherLoaders.size());
		if (this.poolParallelLoaders == null) { this.poolParallelLoaders = this.newExecutorForParallelLoading(); }
		if (this.poolParallelLoaders != null) {

			this.otherLoaders.forEach(l -> {
				final int i = index[0]++;
				poolParallelLoaders.execute(() -> {
					LoaderGeneric.LoadStatusResult res;
					results[i] = res = l.loadInto(this.gameController);
					allObserversNotifier.accept(l, res);
				});
			});
			poolParallelLoaders.shutdown();
			try {
				poolParallelLoaders.awaitTermination(this.getMillisecondsMaximumWaitLoadingProcess(),
						TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				this.gameController.getLogger().logException(e);
			}

		} else {
			this.otherLoaders.forEach(l -> {
				final int i = index[0]++;
				LoaderGeneric.LoadStatusResult res;
				results[i] = res = l.loadInto(this.gameController);
				allObserversNotifier.accept(l, res);
			});
		}

		failedLoaders = this.manageLoadersStatusResults(results);
		this.hasAlreadyLoaded = failedLoaders == null || failedLoaders.isEmpty();
		this.loadersAndStatusObservers.forEach(lo -> lo.notifyAllLoadingProcessEnded(failedLoaders));
		return failedLoaders;
	}

	/**
	 * Override-designed<br>
	 * Manage the results, defining the action(s) to take upon some failure. It
	 * returns the list of {@link LoaderGeneric} that failed the loading process: if
	 * empty or null, then the whole loading process was successful and no loader
	 * failed.
	 *
	 * @param results the loader loading results
	 * @return the list of {@link LoaderGeneric} that failed the loading process: if
	 *         empty or null, then the whole loading process was successful and no
	 *         loader failed.
	 */
	protected List<LoaderGeneric> manageLoadersStatusResults(LoaderGeneric.LoadStatusResult[] results) {
		int i;
		List<LoaderGeneric> failedLoadersIndexes;
		LoggerMessages log;

		i = 0;
		failedLoadersIndexes = new LinkedList<>();
		for (LoaderGeneric.LoadStatusResult s : results) {
			if (s == LoaderGeneric.LoadStatusResult.CriticalFail) {
				failedLoadersIndexes.add(this.otherLoaders.get(i));
			}
			i++;
		}
		if (failedLoadersIndexes.isEmpty()) { return failedLoadersIndexes; }

		log = this.gameController.getLogger();
		failedLoadersIndexes.forEach(loaderFailed -> {
			log.logNoNewLine("Failed loader of   class ");
			log.logNoNewLine(loaderFailed.getClass().getName());
			log.log("\n");
		});
		return failedLoadersIndexes;
	}
}
