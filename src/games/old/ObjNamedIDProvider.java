package games.old;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dataStructures.MapTreeAVL;
import tools.Comparators;
import tools.ObjectNamedID;

/**
 * Deprecate since 29/03/2020 because this would require to store instances of
 * classes of the given type, whose classes could create objects inside the
 * constructor, making this "storage" very heavy.
 */
@Deprecated
public class ObjNamedIDProvider<E extends ObjectNamedID> {

	Map<Integer, E> objsByID;
	Map<String, E> objsByName;
	GetAtProvider getAtProvider;
	Set<E> objsSet; // as set

	public ObjNamedIDProvider() {
		setNewMaps();
	}

	protected void setNewMaps() {
		MapTreeAVL<String, E> m;
		this.objsByID = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.STRING_COMPARATOR);
		this.objsByName = m;
		this.getAtProvider = newGetAtProvider();
		this.objsSet = m.toSetValue(a -> a.getName());
	}

	protected GetAtProvider newGetAtProvider() {
		return new GetAtProviderImpl((MapTreeAVL<String, E>) objsByName);
	}

	//

	public void addObjIdentified(E a) {
		this.objsByID.put(a.getID(), a);
		this.objsByName.put(a.getName(), a);
	}

	/**
	 * Should be avoided, should then prefer
	 * {@link #getObjIdentifiedByName(String)}.
	 */
	public E getObjIdentifiedByID(Integer id) {
		return this.objsByID.get(id);
	}

	/** Should be preferred over {@link #getObjIdentifiedByID(Integer)}. */
	public E getObjIdentifiedByName(String name) {
		return this.objsByName.get(name);
	}

	public E getAtIndex(int index) {
		return this.getAtProvider.getAtIndex(index);
	}

	public Set<E> getObjectsIdentified() {
		return objsSet;
	}

	public int getObjectsIdentifiedCount() {
		return this.objsByName.size();
	}

	//

	protected abstract class GetAtProvider {
		public abstract E getAtIndex(int index);
	}

	protected class GetAtProviderImpl extends GetAtProvider {
		MapTreeAVL<String, E> backMap;

		public GetAtProviderImpl(MapTreeAVL<String, E> backMap) {
			super();
			this.backMap = backMap;
		}

		@Override
		public E getAtIndex(int index) {
			Entry<String, E> e;
			e = backMap.getAt(index);
			return e == null ? null : e.getValue();
		}
	}

}
