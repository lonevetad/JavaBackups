package games.generic.controlModel.subimpl;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.holders.GObjectsHolder;
import games.generic.controlModel.objects.TimedObject;
import tools.Comparators;
import tools.ObjectWithID;

public class GObjectsHolderImpl<K, T extends ObjectWithID> implements GObjectsHolder<T> {

	protected final Function<T, K> keyExtractor;
	// TODO : DUPLICARE LA MAPPA PER GLI INTERI, SENNO' NON POSSO FARE LA GET
	protected final MapTreeAVL<Long, T> objectsByID;
	protected final MapTreeAVL<K, T> objectsHeld;
	protected final Set<T> objectsHeld_Set;

	public static <OWID extends ObjectWithID> GObjectsHolderImpl<Long, OWID> newDefault(Class<OWID> clazz) {
		return new GObjectsHolderImpl<Long, OWID>(Comparators.LONG_COMPARATOR, ObjectWithID::getID);
	}

	public GObjectsHolderImpl(Comparator<K> keyComparator, Function<T, K> keyExtractor) {
		this.keyExtractor = keyExtractor;
		this.objectsByID = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR);
		this.objectsHeld = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, keyComparator);
		this.objectsHeld_Set = this.objectsHeld.toSetValue(keyExtractor);
	}

	@Override
	public Set<T> getObjects() { return objectsHeld_Set; }

	@Override
	public int objectsHeldCount() { return this.objectsHeld.size(); }

	@Override
	public boolean add(T o) {
		K k;
		if (o == null || (!(o instanceof TimedObject)))
			return false;
		k = keyExtractor.apply(o);
		if (objectsHeld.containsKey(k)) { return false; }
		this.objectsHeld.put(k, o);
		this.objectsByID.put(o.getID(), o);
		return true;
	}

	@Override
	public boolean remove(T o) {
		K k;
		Long id;
		id = o.getID();
		k = keyExtractor.apply(o);
		if (this.objectsHeld.containsKey(k)) {
			this.objectsHeld.remove(id);
			this.objectsByID.remove(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll() {
		this.objectsHeld.clear();
		this.objectsByID.clear();
		return true;
	}

	@Override
	public boolean contains(T o) { return objectsHeld.containsKey(o.getID()); }

	@Override
	public T get(Long id) { return objectsByID.get(id); }

	public T getByKey(K id) { return objectsHeld.get(id); }

	@Override
	public void forEach(Consumer<T> action) { objectsHeld.forEach((id, to) -> action.accept(to)); }
}