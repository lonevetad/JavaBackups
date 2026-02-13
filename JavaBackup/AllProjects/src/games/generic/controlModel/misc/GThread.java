package games.generic.controlModel.misc;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;

/**
 * When the game ({@link GModality}, to be precise) shuts down ALL threads
 * should stops and being destroyed. This class is designed (hopefully) to do so
 * through {@link #stopAndDie()}.
 */
public class GThread extends Thread {

	protected GTRunnable tgr;

	public GThread(GTRunnable target) {
		super(target);
		this.tgr = target;
	}

	// makes me stops

	/**
	 * As explained in this class's documentation ({@link GThread}), this method
	 * should stop this thread to run and, then, die (being able do be destroyed and
	 * un-allocated). <br>
	 * To do so, invoke {@link GTRunnable#stopAndDie()}.
	 */
	public void stopAndDie() { this.tgr.stopAndDie(); }

	/**
	 * Attempts to restart the {@link GTRunnable} by invoking
	 * {@link GTRunnable#restart()}.<br>
	 * It's not guaranteed that its {@link GTRunnable#run()} effectively starts
	 * again, it's recommended to create a new instance of this class (and it's
	 * suggested to instantiate the runner again too).
	 */
	public void restart() { this.tgr.restart(); }

	//

	//

	public static interface GTRunnable extends Runnable {

		/**
		 * Execute the game, usually by invoking
		 * {@link GModality#runSingleGameCycle()}.<br>
		 * Implementation example:<br>
		 *
		 * <pre>
		 * <code>
		 * public void run() {
		 * 	while( this.gameNotShuttedDown ){
		 * 		getGameModality().runGameCycle();
		 * 	}
		 * }
		 * </code>
		 * </pre>
		 *
		 * Setting a local flag to false should be enough, usually no greater complexity
		 * is required.<br>
		 * (NOTE: To "pause" the game do not rely on {@link #stopAndDie()}, use
		 * {@link GController#pauseGame()} instead!)
		 * <p>
		 * Inherited documentation:
		 * <p>
		 * {@inheritDoc}
		 */
		@Override
		public void run();

		/**
		 * See {@link GThread#stopAndDie()} for more informations. <br>
		 * NOTE: To "pause" the game do not rely on {@link #stopAndDie()}, use
		 * {@link GController#pauseGame()} instead!
		 * <p>
		 * Implementation example:<br>
		 *
		 * <pre>
		 * <code>
		 * public void stopAndDie(){
		 * 	this.gameNotShuttedDown = false;
		 * }
		 * </code>
		 * </pre>
		 *
		 */
		public void stopAndDie();

		/**
		 * Attempts to restart the this class.<br>
		 * Usually invoked by {@link GThread#restart()}, see that documentation.
		 */
		public void restart();
	}

	/**
	 * Hoping that the {@link Runnable} delegate (the constructor's parameter) is
	 * performing just one cycle, this class invokes it as a "cycle's action".
	 */
	public static class GTRunnableSimplestImplementation implements GTRunnable {
		protected boolean isWorking = true; // when the
		protected Runnable runnerDelegated;

		public GTRunnableSimplestImplementation() {
			super();
//			this.runnerDelegated = runnerDelegated;
		}

		public Runnable getRunnerDelegated() { return runnerDelegated; }

		public void setRunnerDelegated(Runnable runnerDelegated) { this.runnerDelegated = runnerDelegated; }

		@Override
		public final void run() {
			if (!isWorking)
				throw new IllegalStateException(
						"This runner GTRunnable has already died! Re-create me or find a way to restart me (not only just by calling restart())");
			runRunner();
		}

		protected boolean canRunRunnerCycle() { return isWorking; }

		protected void runRunner() {
			while (canRunRunnerCycle()) {
				this.runnerDelegated.run();
			}
		}

		@Override
		public void stopAndDie() { this.isWorking = false; }

		@Override
		public void restart() { this.isWorking = true; }
	}
}