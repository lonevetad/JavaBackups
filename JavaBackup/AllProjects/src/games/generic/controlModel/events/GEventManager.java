package games.generic.controlModel.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GObjectsHolder;
import games.generic.controlModel.subimpl.GModalityET;

/**
 * One of the core classes.
 * <p>
 * Manager of {@link GEvent}s and their {@link GEventObserver}. In particular,
 * it's a {@link GObjectsHolder} of {@link GEventObserver}<br>
 * All {@link GEvent}s' effects should be applied "at the same time" because all
 * of them happens in the same "moment" (i.e. in the same cycle, implemented in
 * {@link GModalityET#doOnEachCycle(int)} if the modality is time-based) but
 * it's obviously hard to implement.
 * <p>
 * The implementation should allow to fire events immediately: fire them as the
 * are posted, without putting them to a hypothetical queue and waiting to be
 * performed. See {@link GEvent#isRequirigImmediateProcessing()}.
 */
public abstract class GEventManager implements GObjectsHolder<GEventObserver> {
	protected GModalityET gameModality; // back reference
//	protected MapTreeAVL<Integer, Queue<IGEvent>> eventsQueued;
	protected List<IGEvent> eventsQueued;

	public GEventManager(GModalityET gameModality) {
		this.gameModality = gameModality;
		/*
		 * this.eventsQueued =
		 * MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
		 * Comparators.INTEGER_COMPARATOR);
		 */
		eventsQueued = new ArrayList<IGEvent>(16);
	}

	//

	//

	public GModality getGameModality() { return gameModality; }

	/** Use with care */
//	public Queue<IGEvent> getEventsQueued() { return eventsQueued; }

	/**
	 * Use with caution.
	 * <p>
	 * 15/04/2020:<br>
	 * I honestly forgot why the events was grouped by their ID (and not their
	 * name?) while a simple queue would have been much simpler.
	 */
//	public Map<Integer, Queue<IGEvent>> getEventQueued() { return eventsQueued;}

	//

	/***/
	public void setGameModality(GModalityET gameModality) { this.gameModality = gameModality; }

	//

	// TODO ABSTRACT

	public abstract boolean addEventObserver(GEventObserver geo);

	public abstract boolean removeEventObserver(GEventObserver geo);

	public abstract void removeAllEventObserver();

	public abstract void notifyEventObservers(IGEvent ge);

	public abstract void forEachEventObservers(Consumer<GEventObserver> action);

	//

	// TODO CONCRETE METHODS

	/** Similar to {@link Collection#clear()}. */
	@Override
	public boolean removeAll() {
		removeAllEventObserver();
		return true;
	}

	@Override
	public void forEach(Consumer<GEventObserver> action) { forEachEventObservers(geo -> action.accept(geo)); }

	@Override
	public boolean add(GEventObserver o) {
		if (o == null) { return false; }
		return addEventObserver(o);
	}

	@Override
	public boolean remove(GEventObserver o) {
		if (o == null) { return false; }
		return removeEventObserver(o);
	}

	/**
	 * "Fire", "post", "add", call it as You want, but put the given {@link IGEvent}
	 * in some kind of event queue and (lately? separately? in a parallel way?)
	 * notify the {@link GEventObserver}s that the event has been occurred.
	 */
	public void fireEvent(IGEvent ge) {
		if (ge.isRequirigImmediateProcessing()) {
			this.notifyEventObservers(ge);
		} else {
			this.enqueueEvent(ge);
		}
	}

	/**
	 * Notify all observers and remove them from queue (Events cannot stay in the
	 * queue for more than one cycle because they are broadcasted at EACH game
	 * cycle, i.e. each {@link GModality#doOnEachCycle(long)})
	 */
	public void performAllEvents() {
		boolean canCycle;
		int eventsCount, eventsMax, eventsLeft;
		final GModalityET gm;
		List<IGEvent> q;
		IGEvent event;
		gm = this.gameModality;
		q = this.eventsQueued;
		eventsLeft = q.size();
		canCycle = true;
		eventsCount = 0;
		eventsMax = this.gameModality.getMaxEventProcessedEachStep();
		while (eventsLeft > 0) {

			// make me sleep and waiting the game to be resumed
			/**
			 * This pausing-on-fail-check has been added because the game thread should be
			 * paused as soon as possible if the user requested to pause the game, so the
			 * highest-grained checks should be performed.<br>
			 * If the {@link GModality} splits the "real game" and the "event management" in
			 * two different threads, then this check is way more required, to synchronize
			 * both threads upon sleeping and awakening.
			 */
			while (canCycle && (eventsLeft > 0) && gm.isRunningOrSleep()) {
//					l.remove(0).performEvent(gm);
				this.notifyEventObservers(event = q.remove(--eventsLeft)); // remove the first event
				event.onProcessingEnded();
				if (eventsMax > 0 && ++eventsCount > eventsMax) {
					canCycle = this.gameModality.handleExceedingEventsEnqueued(q);
				}
			}
		}
	}

	protected void enqueueEvent(IGEvent e) { this.eventsQueued.add(e); }
}