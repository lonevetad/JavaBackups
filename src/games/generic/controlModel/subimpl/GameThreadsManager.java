package games.generic.controlModel.subimpl;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.misc.GThread;
import tools.Comparators;

/** Manager for game threads */
public abstract class GameThreadsManager {

	public GameThreadsManager(GModalityET gmodality) {
		this.gmodality = gmodality;
		this.threadsAlreadyStarted = false;
		this.pauseThreadsLock = new Object();
		this.threadsSleeping = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR);
		this.activeThreads = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR);
//		new LinkedList<>();
	}

	protected boolean threadsAlreadyStarted;
	protected final GModalityET gmodality;
	protected final Object pauseThreadsLock;
	protected final Map<Long, GThread> threadsSleeping, activeThreads; // List<ThreadGame>

	public abstract void instantiateAllThreads();

	//

	public int ActiveThreadsCount() { return this.activeThreads.size(); }

	/**
	 * Used by a {@link GThread} to check if the game is running and, depending on
	 * this implementation, pause the thread (sleep).
	 */
	public boolean isGModalityRunningOrSleep() {
		if (!(gmodality.isRunning() && gmodality.isAlive())) {
			try {
				synchronized (pauseThreadsLock) {
					GThread t;
					t = (GThread) Thread.currentThread();
					threadsSleeping.put(t.getId(), t);
//					threadsSleeping.add(t);
					this.pauseThreadsLock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // make me sleep
		}
		return true;
	}

	public void addGThread(GThread t) {
		this.activeThreads.put(t.getId(), t);
		if (threadsAlreadyStarted) { t.start(); }
	}

	public void removeGThread(GThread t) {
		t.stopAndDie();
		this.activeThreads.remove(t.getId());
	}

	public void startGThreads() {
		if (!threadsAlreadyStarted) {
			threadsAlreadyStarted = true;
			this.activeThreads.forEach((id, t) -> t.start());
		}
	}

	/**
	 * Resume all sleeping threads (that starts sleeping due to a
	 * {@link #isGModalityRunningOrSleep()} call).
	 */
	public void resumeThreads() {
		synchronized (pauseThreadsLock) {
			this.pauseThreadsLock.notifyAll();
			this.threadsSleeping.clear();
		}
	}

	/** Stop them all! (Calling {@link GThread#stopAndDie()}. */
	public void stopThreads() {
		synchronized (pauseThreadsLock) {
			this.activeThreads.forEach((id, t) -> {
				if (!this.threadsSleeping.containsKey(id))
					t.stopAndDie();
			});
			this.activeThreads.clear();
			this.pauseThreadsLock.notifyAll();
			this.threadsSleeping.forEach(//
					(id, t) -> { // t
						/*
						 * try { t.interrupt(); } catch (SecurityException e) { e.printStackTrace(); }
						 */
						t.stopAndDie();
					});
			this.threadsSleeping.clear();
		}
	}
}