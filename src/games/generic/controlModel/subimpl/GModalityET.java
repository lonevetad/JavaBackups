package games.generic.controlModel.subimpl;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GModel;
import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.events.GEventManager;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.misc.GThread;
import games.generic.controlModel.misc.GThread.GTRunnableSimplestImplementation;
import games.generic.controlModel.objects.GameObjectGeneric;
import games.generic.controlModel.objects.TimedObject;
import geometry.ObjectLocated;

/**
 * Game modality based on real time and events.
 * <p>
 * ALL {@link GEvent}-firing methods are delegated to {@link GEventInterface}
 * (obtained through {@link GModalityET#getEventInterface()}) to make this class
 * thinner.
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link GEventInterface} to manage events (usually</li>
 * <li>{@link GameThreadsManager}</li>
 * </ul>
 */
public abstract class GModalityET extends GModality implements IGameModalityTimeBased, IGameModalityEventBased {
	/**
	 * Used by {@link #runSingleGameCycle()} and usually returned by
	 * {@link #getMinimumMillisecondsEachCycle()}.
	 */
	public static final int MIN_DELTA = 10, MAX_DELTA = 100, MAX_ELAPSED_TIME = 100;

	protected int lastElapsedDeltaTime = 0, maxEventProcessedEachStep;
	protected GEventInterface eventInterface;
	protected final GameThreadsManager gameThreadsManager;

	public GModalityET(GController controller, String modalityName) {
		super(controller, modalityName);
		this.lastElapsedDeltaTime = this.getMinimumMillisecondsEachCycle();
		this.gameThreadsManager = newGameThreadsManager();
		this.maxEventProcessedEachStep = IGameModalityEventBased.super.getMaxEventProcessedEachStep();
	}

	//

	/**
	 * Returns the minimum amount of milliseconds that is forced to be elapsed
	 * between each cycle. In fact, it's used inside {@link #runSingleGameCycle()}
	 * original implementation.<br>
	 * Set it as <code>0 (zero)</code> to remove each limit, especially FPS limits.
	 */
	public int getMinimumMillisecondsEachCycle() { return MIN_DELTA; }

	@Override
	public GModelTimeBased getModelTimeBased() { return (GModelTimeBased) model; }

	/** Access ALL {@link GEvent}-firing methods through this instance. */
	@Override
	public GEventInterface getEventInterface() { return eventInterface; }

	/**
	 * Should not be used, use with caution or use
	 * {@link GModalityET#getEventInterface()}) instead.
	 */
	public GEventManager getEventManager() { return eventInterface.getGameEventManager(); }

	public GModelET getGModelEventTimedObjectsHolder() { return (GModelET) this.getModel(); }

	@Override
	public int getMaxEventProcessedEachStep() { return maxEventProcessedEachStep; }

	//

	@Override
	public void setEventInterface(GEventInterface eventInterface) {
		this.eventInterface = eventInterface;
		this.getGModelEventTimedObjectsHolder().setEventManager(eventInterface.getGameEventManager());
	}

	@Override
	public void setMaxEventProcessedEachStep(int maxEventProcessedEachStep) {
		this.maxEventProcessedEachStep = maxEventProcessedEachStep;
	}

	//
	@Override
	public GModel newGameModel() { return new GModelET(); }

	public GameThreadsManager newGameThreadsManager() { return new GameThreadsManagerBase(this); }

	@Override
	public void onCreate() {
		this.eventInterface = newEventInterface();
		super.onCreate();
		this.getGModelEventTimedObjectsHolder().setEventManager(getEventManager());
	}

	//

	@Override
	public boolean addGameObject(GameObjectGeneric o) {
		boolean added;
		GEventInterface eventInterface;
		added = super.addGameObject(o);
		eventInterface = this.getGameObjectsManager().getGEventInterface();
		if (eventInterface != null && o instanceof ObjectLocated) {
			eventInterface.fireGameObjectAdded(this, (ObjectLocated) o);
		}
		return added;
	}

	@Override
	public boolean removeGameObject(GameObjectGeneric o) {
		boolean added;
		GEventInterface eventInterface;
		added = super.removeGameObject(o);
		eventInterface = this.getGameObjectsManager().getGEventInterface();
		if (eventInterface != null && o instanceof ObjectLocated) {
			eventInterface.fireGameObjectRemoved(this, (ObjectLocated) o);
		}
		return added;
	}

	/**
	 * No event fired: EVERYONE will be removed, so no one will hear that.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAllGameObjects() { return super.removeAllGameObjects(); }

	/**
	 * Override designed, by default simply calls
	 * {@link GameThreadsManager#instantiateAllThreads()}.
	 */
	protected void checkAndRebuildThreads() { this.gameThreadsManager.instantiateAllThreads(); }

	/**
	 * Start all kinds of threads
	 */
	protected void startAllThreads() { this.gameThreadsManager.startGThreads(); }

	@Override
	public void addGameThread(GThread t) { this.gameThreadsManager.addGThread(t); }

	public GThread addGameThread(GThread.GTRunnable runner) {
		GThread gt;
		gt = new GThread(runner);
		this.addGameThread(gt);
		return gt;
	}

	public GThread addGameThreadSimplyStoppable(Runnable runner) {
		GTRunnableSimplestImplementation gr;
		gr = new GameModalityBasedGTRunnable(this);
		gr.setRunnerDelegated(runner);
		return this.addGameThread(gr);
	}

	@Override
	public void removeGameThread(GThread t) { this.gameThreadsManager.removeGThread(t); }

	/**
	 * Used by other objects and threads (like GUI, sound, animation, the game's
	 * ones, etc) to check if the game is running or: If NOT (i.e. the game is
	 * paused), make that thread sleep <br>
	 * Differs from {@link #isRunning()}, see it for differences.
	 * <p>
	 * Delegate the implementation to
	 * {@link GameThreadsManager#isGModalityRunningOrSleep()}.
	 */
	public boolean isRunningOrSleep() { return this.gameThreadsManager.isGModalityRunningOrSleep(); }

	@Override
	public void startGame() {
		checkAndRebuildThreads();
		System.out.println("\n\n STARTING THREADS, SOON \n\n\n");
		startAllThreads();
	}

	@Override
	public void resume() {
		super.resume();
		this.gameThreadsManager.resumeThreads();
	}

	@Override
	public void closeAll() {
		super.closeAll();
		this.gameThreadsManager.stopThreads();
	}

	//

	//

	/**
	 * Override designed.
	 * <p>
	 * Define THE REAL GAME cycle.<br Everything about a cycle-based game is run
	 * there.<br>
	 * Even card-games, chess like and other "not-real time" games (i.e.: turn-based
	 * or other kinds of games that I'm currently not able to list) could use this
	 * method: just pause the {@link Thread} waiting for the event to be fired. OR
	 * simply override both this method and {@link #runSingleGameCycle()} to simply
	 * execute events and not time-based stuffs.
	 * <p>
	 * Invoked by {@link #runSingleGameCycle()}.
	 * <p>
	 * NOTE:<br>
	 * {@link GModality} implementing "real time" games should implement the
	 * interface {@link IGameModalityTimeBased} and implement this method as a
	 * simple invocation of {@link IGameModalityTimeBased#progressElapsedTime(int)}
	 */
	public void doOnEachCycle(int timeUnit) {
		// simple oferrides that simply calls this
		progressElapsedTime(timeUnit); // from IGModTImeBased etc
	}

	@Override
	public void progressElapsedTime(final int timeUnit) {
		GEventManager gem;
		this.getModelTimeBased().forEachTimedObject((to) -> {
			to.act(this, timeUnit); // fai progredire QUALSIASI cosa: abilità che si ricaricano col tempo,
			// rigenerazioni, movimento di proiettili e cose, etc
		});
		gem = this.eventInterface.getGameEventManager();
		if (gem != null)
			gem.performAllEvents();
	}

	/**
	 * <i/>WARNING</i>: <b>NOT</b> override designed,<br>
	 * but allowed (in case of tuning or enhancement): just redefine
	 * {@link #doOnEachCycle(int)} instead.
	 * <p>
	 * Perform a SINGLE game cycle/step.<br>
	 * Should be called by the game's thread inside a cycle.<br>
	 * It's designed as a single cycle also to allow an optional step-by-step
	 * execution (some game engines allow it).
	 * <p>
	 * IMPLEMENTATION NOTE:<br>
	 * Each cycle lasts at least the amount of milliseconds provided by
	 * {@link #getMinimumMillisecondsEachCycle()} (currently it returns
	 * {@link #MIN_DELTA}) and a <code>Thread.sleep(..)</code> is invoked if the
	 * elapsed time during the cycle is lower than this threshold, so the maximum
	 * FPS count is <code>1000/{@link #getMinimumMillisecondsEachCycle()}</code>.
	 */
	public void runSingleGameCycle() {
		long start;
		int timeToSleep, minDelta;
		minDelta = this.getMinimumMillisecondsEachCycle();
		if (isAlive()) {
			while (isRunningOrSleep()) {
				start = System.currentTimeMillis();
				doOnEachCycle(lastElapsedDeltaTime);
				timeToSleep = ((int) (System.currentTimeMillis() - start)); // used as temp
				if (timeToSleep > MAX_ELAPSED_TIME)
					timeToSleep = MAX_ELAPSED_TIME;
				lastElapsedDeltaTime = timeToSleep;
				timeToSleep = (minDelta - timeToSleep + 1);

				// "+1" as a rounding factor for nanoseconds
				if (timeToSleep > 0) {
					if (timeToSleep > MAX_DELTA)
						timeToSleep = MAX_DELTA; // do not exceed
					lastElapsedDeltaTime = minDelta; // because the total ime elapsed in this cycle is this amount
					try {
						Thread.sleep(timeToSleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("died runGame");
			try {
				Thread.sleep(Math.min(MIN_DELTA, minDelta));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//

// TODO objects handlers

	/**
	 * Proxy-like method.
	 */
	public void addTimedObject(TimedObject to) { this.getModelTimeBased().addTimedObject(to); }

	/**
	 * Proxy-like method.
	 */
	public void addEventObserver(GEventObserver geo) { this.getEventManager().addEventObserver(geo); }

//	public void fireEvent(GEvent event) { this.getEventManager(). }

//	protected void checkAndRebuildThreads() {
//		if (this.threadGame == null)
//			this.threadGame = new GThread(new RunGameInstance());
//	}

	//

	//

	// TODO class

	//

	public static class GameThreadsManagerBase extends GameThreadsManager {
		public GameThreadsManagerBase(GModalityET gmodality) { super(gmodality); }

		@Override
		public void instantiateAllThreads() { this.addGThread(new GThread(new RunnerOnGameInstance(gmodality))); }
	}

	// previously was ThreadGame_GameRunner_E1
	/**
	 * The runner of the entire game (that's why it executes
	 * <code>runSingleGameCycle()</code>).
	 */
	protected static class RunnerOnGameInstance extends GTRunnableSimplestImplementation {
		protected GModalityET gmodality;

		public RunnerOnGameInstance(GModalityET gmodality) {
			super();
			this.gmodality = gmodality;
			this.setRunnerDelegated(() -> gmodality.runSingleGameCycle());
		}
	}

	protected static class GameModalityBasedGTRunnable extends GTRunnableSimplestImplementation {
		protected final GModalityET gameModality;

		public GameModalityBasedGTRunnable(GModalityET gameModality) {
			super();
			this.gameModality = gameModality;
		}

		@Override
		protected boolean canRunRunnerCycle() { return gameModality.isRunningOrSleep(); }
	}
}