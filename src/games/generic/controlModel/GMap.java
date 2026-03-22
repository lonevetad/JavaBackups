package games.generic.controlModel;

import java.util.Set;
import java.util.function.Consumer;

import games.generic.controlModel.holders.GObjectsHolder;
import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import games.generic.controlModel.objects.ObjectInSpace;
import tools.ObjectNamedID;
import tools.UniqueIDProvider;

/**
 * TODO: è una mappa attuale, o i dati grezzi per generare una mappa? o solo un
 * gestore?
 * <p>
 * Holds and manages all objects in the current game, as described by
 * {@link GObjectsHolder} but also helped by the delegate
 * {@link GObjectsInSpaceManager}, obtained by {@link #getGOISMDelegated()}.
 */
public class GMap implements GObjectsHolder<ObjectInSpace>, ObjectNamedID {
	private static final long serialVersionUID = -4521201066L;
	private static UniqueIDProvider UIDP_GMAP = null;
	public static final UIDProviderLoadedListener UIDP_LOADED_LISTENER_GMAP = uidp -> {
		if (uidp != null) { UIDP_GMAP = uidp; }
	};

	public static UniqueIDProvider getUniqueIDProvider_GMap() { return UIDP_GMAP; }

	//

	public GMap(String mapName) {
		this.mapName = mapName;
		ID = UIDP_GMAP.getNewID();
	}

	protected Long ID;
	protected String mapName;
	protected GObjectsInSpaceManager goismDelegated;

	@Override
	public Long getID() { return ID; }

	@Override
	public String getName() { return mapName; }

	/**
	 * That's the delegate for managing the space of this map and the objects to put
	 * them in
	 */
	public GObjectsInSpaceManager getGOISMDelegated() { return goismDelegated; }

	public String getNameGObjHolder() { return goismDelegated.getNameGObjHolder(); }

	//

	@Override
	public boolean setID(Long newID) {
		if (this.ID != null || newID == null) { return false; }
		this.ID = newID;
		return true;
	}

	/**
	 * Set the delegator cited in the class's documentation.
	 */
	public void setGOISMDelegated(GObjectsInSpaceManager isomDelegated) { this.goismDelegated = isomDelegated; }

	@Override
	public Set<ObjectInSpace> getObjects() { return goismDelegated.getObjects(); }

	@Override
	public int objectsHeldCount() { return this.goismDelegated.objectsHeldCount(); }

	@Override
	public boolean add(ObjectInSpace o) { return goismDelegated.add(o); }

	@Override
	public boolean remove(ObjectInSpace o) { return goismDelegated.remove(o); }

	@Override
	public boolean removeAll() { return goismDelegated.removeAll(); }

	@Override
	public boolean contains(ObjectInSpace o) { return goismDelegated.contains(o); }

	@Override
	public ObjectInSpace get(Long id) { return goismDelegated.get(id); }

	@Override
	public void forEach(Consumer<ObjectInSpace> action) { goismDelegated.forEach(action); }
}