package games.generic.controlModel.subimpl;

import java.util.Set;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.holders.TimedObjectHolder;
import games.generic.controlModel.objects.TimedObject;
import tools.Comparators;

/**
 *
 * @deprecated replaced by
 *
 * @author ottin
 *
 */
@Deprecated
public class TimedObjectHolder_Impl1 extends TimedObjectHolder {

	protected MapTreeAVL<Long, TimedObject> timedObjects;
	protected Set<TimedObject> timedObjects_Set;

	public TimedObjectHolder_Impl1() {
		this.timedObjects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.LONG_COMPARATOR);
		this.timedObjects_Set = this.timedObjects.toSetValue(TimedObject::getID);
	}

	@Override
	public Set<TimedObject> getObjects() { return timedObjects_Set; }

	@Override
	public int objectsHeldCount() { return this.timedObjects.size(); }

	@Override
	public boolean add(TimedObject o) {
		if (o == null || (!(o instanceof TimedObject)) || timedObjects.containsKey(o.getID())) { return false; }
		timedObjects.put(o.getID(), o);
		return true;
	}

	@Override
	public boolean remove(TimedObject o) {
		if (timedObjects.containsKey(o.getID())) {
			timedObjects.remove(o.getID());
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll() {
		this.timedObjects.clear();
		return true;
	}

	@Override
	public boolean contains(TimedObject o) { return timedObjects.containsKey(o.getID()); }

	@Override
	public TimedObject get(Long id) { return timedObjects.get(id); }

	@Override
	public void forEach(Consumer<TimedObject> action) { timedObjects.forEach((id, to) -> action.accept(to)); }

}