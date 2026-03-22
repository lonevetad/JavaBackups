package games.generic.controlModel;

import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.holders.GObjectsHolder;
import tools.Comparators;
import tools.ObjectWithID;

/**
 * One of the core classes.
 * <p>
 */
public abstract class GModel implements GObjectsHolder<ObjectWithID> {
	protected MapTreeAVL<Long, ObjectWithID> backmapAllObjectsUnfiltered;
	protected Set<ObjectWithID> allObjectsUnfiltered;
	@SuppressWarnings("rawtypes")
	protected MapTreeAVL<String, GObjectsHolder> objectsHoldersSpecialized;
	protected GMap mapCurrent;

	public GModel() {
		this.backmapAllObjectsUnfiltered = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.LONG_COMPARATOR);
		this.allObjectsUnfiltered = this.backmapAllObjectsUnfiltered.toSetValue(ObjectWithID.KEY_EXTRACTOR);
		this.objectsHoldersSpecialized = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.STRING_COMPARATOR);
		this.onCreate();
	}

	//

	//

	@Override
	public int objectsHeldCount() {
		int[] c = { 0 };
		this.objectsHoldersSpecialized.forEach((n, gh) -> { c[0] += gh.objectsHeldCount(); });
		return this.allObjectsUnfiltered.size() + c[0];
	}

	public GMap getMapCurrent() { return mapCurrent; }

	public void setMapCurrent(GMap mapCurrent) { this.mapCurrent = mapCurrent; }

	/**
	 * BEWARE: returns just the object NOT held by some {@link GObjectsHolder} added
	 * via {@link #addObjHolder(String, GObjectsHolder)}.<br>
	 * To access those {@link ObjectWithID} instances , just use the
	 * {@link #get(Integer)} or {@link #forEach(Consumer)} as described in super
	 * documentation, inherited in the following paragraph.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Set<ObjectWithID> getObjects() { return allObjectsUnfiltered; }

	//

	//

	public abstract void onCreate();

	@Override
	public boolean add(ObjectWithID o) {
		final boolean added[];
		if (o == null)
			return false;
		/*
		 * Using an array to bypass the forEach restriction to non-pointers (i.e.
		 * non-final variables)
		 */
		added = new boolean[] { false };
		this.objectsHoldersSpecialized.forEach((s, h) -> { added[0] |= h.add(o); });
		if (!added[0]) {
			// no one has added it: so I add it
			if (this.allObjectsUnfiltered.contains(o))
				return false;
			this.allObjectsUnfiltered.add(o);
		}
		return true;
	}

	@Override
	public boolean remove(ObjectWithID o) {
		RemoverOn_GObjectsHolder cc;
		if (o == null)
			return false;
//		if(this.objectsHoldersSpecialized.containsKey(o))
		cc = new RemoverOn_GObjectsHolder(o);
		this.objectsHoldersSpecialized.forEach(cc);
		if (cc.removed) { return true; }
		if (this.backmapAllObjectsUnfiltered.containsKey(o.getID())) {
			this.backmapAllObjectsUnfiltered.remove(o.getID());
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll() {
		boolean[] someoneRemoved;
		someoneRemoved = new boolean[] { this.backmapAllObjectsUnfiltered.size() != 0 };
		this.backmapAllObjectsUnfiltered.clear();
		this.objectsHoldersSpecialized.forEach((s, goh) -> {
			someoneRemoved[0] |= goh.objectsHeldCount() != 0;
			goh.removeAll();
		});
		return someoneRemoved[0];
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(ObjectWithID o) {
		if (o == null)
			return false;
		if (this.backmapAllObjectsUnfiltered.containsKey(o.getID()))
			return true;
//		if(this.objectsHoldersSpecialized.containsKey(o))
		for (@SuppressWarnings("rawtypes")
		Entry<String, GObjectsHolder> e : this.objectsHoldersSpecialized) {
			if (e.getValue().contains(o))
				return true;
		}
		return false;
	}

	@Override
	public ObjectWithID get(final Long id) {
		ObjectWithID o;
		if (id == null)
			return null;
		o = this.backmapAllObjectsUnfiltered.get(id);
		if (o != null)
			return o;
		for (@SuppressWarnings("rawtypes")
		Entry<String, GObjectsHolder> e : this.objectsHoldersSpecialized) {
			ObjectWithID oo;
			oo = e.getValue().get(id);
			if (oo != null)
				return oo;
		}
		return null;
	}

	@Override
	public void forEach(final Consumer<ObjectWithID> action) {
		final Consumer<ObjectWithID> innerConsumer;
		this.backmapAllObjectsUnfiltered.forEach((id, owid) -> { action.accept(owid); });
		innerConsumer = action::accept;
		this.objectsHoldersSpecialized.forEach((str, goh) -> { goh.forEach(innerConsumer); });
	}

	/**
	 * Various classes may require (or be designed to) manage {@link ObjectWithID},
	 * but the collection of {@link ObjectWithID} should belong to the Model. A
	 * solution is to force those classes to implement {@link GObjectsHolder} so
	 * that the Model can really hold ALL data.<br>
	 * Those classes (that I repeat, are kind of "managers") can access to the
	 * desired instances through the Model itself (slow but correct, talking about
	 * the "Responsibility" question) or their "shortcut-pointers". Beware of the
	 * side effects of this last way: some subclasses of {@link GModel} may
	 * implements "code", triggers, controls, restrictions, etc and rely to
	 * particular methods like the {@link #add(ObjectWithID)} or
	 * {@link #remove(ObjectWithID)} overrides. Using those shortcuts may lead to
	 * bugs, usually hard to be found.
	 */
	public boolean addObjHolder(String nameIdentifier, GObjectsHolder goHolder) {
		if (!this.objectsHoldersSpecialized.containsKey(nameIdentifier)) {
			this.objectsHoldersSpecialized.put(nameIdentifier, goHolder);
			return true;
		}
		return false;
	}

	public boolean removeObjHolder(String nameIdentifier) {
		if (!this.objectsHoldersSpecialized.containsKey(nameIdentifier)) {
			this.objectsHoldersSpecialized.remove(nameIdentifier);
			return true;
		}
		return false;
	}

	public boolean containsObjHolder(String nameIdentifier) {
		return this.objectsHoldersSpecialized.containsKey(nameIdentifier);
	}

	public void forEachObjHolder(BiConsumer<String, GObjectsHolder> action) {
		this.objectsHoldersSpecialized.forEach(action);
	}

	public GObjectsHolder getObjHolder(String nameIdentifier) {
		return this.objectsHoldersSpecialized.get(nameIdentifier);
	}

	//

	//

	protected static class RemoverOn_GObjectsHolder implements BiConsumer<String, GObjectsHolder> {
		boolean removed;
		ObjectWithID target;

		public RemoverOn_GObjectsHolder(ObjectWithID target) {
			super();
			this.target = target;
			this.removed = false;
		}

		@Override
		public void accept(String t, GObjectsHolder goh) { removed |= goh.remove(target); }
	}
}