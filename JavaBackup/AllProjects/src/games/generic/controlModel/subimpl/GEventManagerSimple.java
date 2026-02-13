package games.generic.controlModel.subimpl;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.events.GEventManager;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import tools.Comparators;

/** Broadcast the event to ALL observers, without any selection. */
public class GEventManagerSimple extends GEventManager {

	public Set<GEventObserver> observersSet;
	public MapTreeAVL<Long, GEventObserver> observers; // previously GEventObserver
	protected EventNotifier notifier;

	public GEventManagerSimple(GModalityET gameModality) {
		super(gameModality);
		this.observers = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.LONG_COMPARATOR);
		this.observersSet = this.observers.toSetValue(GEventObserver::getID);
		this.notifier = new EventNotifier(this);
	}

	@Override
	public boolean addEventObserver(GEventObserver geo) {
		if (this.observers.containsKey(geo.getID()))
			return false;
		this.observers.put(geo.getObserverID(), geo);
		return true;
	}

	@Override
	public boolean removeEventObserver(GEventObserver geo) {
		if (this.observers.containsKey(geo.getID())) {
			this.observers.remove(geo.getObserverID());
			return true;
		}
		return false;
	}

	@Override
	public void removeAllEventObserver() { this.observers.clear(); }

	@Override
	public void forEachEventObservers(Consumer<GEventObserver> action) {
		this.observers.forEach((id, obs) -> action.accept(obs));
	}

	@Override
	public void notifyEventObservers(IGEvent ge) {
		notifier.ge = ge;
		this.observers.forEach(notifier);
	}

	@Override
	public int objectsHeldCount() { return this.observers.size(); }

	@Override
	public Set<GEventObserver> getObjects() { return this.observersSet; }

	@Override
	public boolean contains(GEventObserver o) { return this.observers.containsKey(o.getID()); }

	@Override
	public GEventObserver get(Long id) { return this.observers.get(id); }

	//

	//

	protected static class EventNotifier implements BiConsumer<Long, GEventObserver> {
		protected IGEvent ge;
		protected GEventManager gem;

		public EventNotifier(GEventManager gem) {
			super();
			this.gem = gem;
		}

		@Override
		public void accept(Long t, GEventObserver o) { o.notifyEvent(gem.getGameModality(), ge); }
	}
}