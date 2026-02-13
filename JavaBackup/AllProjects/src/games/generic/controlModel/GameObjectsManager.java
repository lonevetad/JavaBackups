package games.generic.controlModel;

import java.awt.Point;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.SetMapped;
import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.events.GEventManager;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.objects.LivingObject;
import games.generic.controlModel.objects.MovingObject;
import games.generic.controlModel.objects.ObjectInSpace;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.generic.controlModel.subimpl.GModalityET;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.ObjectWithID;

/**
 * One of the core classes.
 * <p>
 * Manages all games objects, performing actions based on their types, like:
 * <ul>
 * <li>For space-based object, like {@link ObjectInSpace}, they could be added,
 * moved, removed, queried.</li>
 * <li>For life-based object, like {@link LivingObject}, then they could inflict
 * and receive damage, heal, destroy/kill, spawn, revive, transform</li>
 * <li>For event-based object, like {@link GEventObserver}, then refers to the
 * instance of {@link GEventManager} provided by {@link #getGEventInterface()}
 * (which is taken from {@link GModalityET#getEventManager()}).</li>
 * </ul>
 * Usually this class is used inside RPG or RTS games, but it's not mandatory.
 * <p>
 * Kind of object handled:
 * <ol>
 * <li>{@link MovingObject} (through {@link InSpaceObjectsManager}
 * instance)</li>
 * <li>{@link LivingObject} (through methods like
 * {@link #dealsDamageTo(DamageDealerGeneric, CreatureSimple, DamageGeneric)}).</li>
 * <li>{@link ObjectInSpace} (as n°1)</li>
 * <li>{@link GEventObserver} (through {@link GEventInterface} instance)</li>
 * </ol>
 */
public interface GameObjectsManager extends GModalityHolder {

	/** Returns the delegator who manage who manages the "space" concept. */
	public GObjectsInSpaceManager getGObjectInSpaceManager();

	/** Returns the delegator who manage the "event handling" concept. */
	public GEventInterface getGEventInterface();

	public void setGObjectsInSpaceManager(GObjectsInSpaceManager isom);

	public void setGEventInterface(GEventInterface gem);

	//

	// TODO: BUNCH OF PROXIES

	//

	public default void addToSpace(ObjectInSpace mo) { getGObjectInSpaceManager().addObject(mo); }

	public default void removeFromSpace(ObjectInSpace mo) { getGObjectInSpaceManager().removeObject(mo); }

	/** See {@link #findInArea(AbstractShape2D, Predicate)}. */
	public default Set<ObjectInSpace> findInArea(AbstractShape2D shape) {
		return this.findInArea(shape, null);
	}

	/** DO NOT ADD ITEMS TO THE RETURNED SET. */
	public default Set<ObjectInSpace> findInArea(AbstractShape2D shape, Predicate<ObjectInSpace> objectFilter) {
		Set<ObjectLocated> r;
		Set<ObjectInSpace> s;
		Predicate<ObjectLocated> pOL;
		s = null;
		pOL = null;
		if (objectFilter != null) {
			pOL = ol -> {
//				if (ol instanceof ObjectInSpace) // null can always be casted to some class
				return objectFilter.test((ObjectInSpace) ol);
//				return false;
			};
		}
		r = getGObjectInSpaceManager().findAll(shape, pOL);
		if (r == null)
			return null;
		s = new SetMapped<>(r, ol -> (ObjectInSpace) ol);
		return s;
	}

	/**
	 * Teleport the given object to a new given location.<br>
	 * Checks about the new location and all kinds of object's tracking and
	 * memorizing are delegated to {@link GObjectsInSpaceManager} returned by
	 * {@link #getGObjectInSpaceManager()}.<br>
	 * On default implementation, it just calls
	 * {@link #removeFromSpace(MovingObject)} and then
	 * {@link #addToSpace(MovingObject)}.
	 */
	public default void moveTo(ObjectInSpace mo, Point newLocation) {
		if (mo == null)
			throw new IllegalArgumentException("The object to move is null");
		removeFromSpace(mo);
		mo.setLocation(newLocation);
		addToSpace(mo);
	}

	//

	// TODO todo other creature's related methods

	/**
	 * One of the core functionality.
	 * <p>
	 * Manage EVERYTHING about the "dealing damage" concept, calculating the damage,
	 * reductions, critical strikes, multipliers, firing events, calling
	 * {@link LivingObject#receiveDamage(GModality, DamageGeneric, ObjectWithID)}
	 * and so on.
	 */
	public default void dealsDamageTo(DamageDealerGeneric source, CreatureSimple target, DamageGeneric damage) {
		this.getGEventInterface().fireDamageDealtEvent((GModalityET) getGameModality(), source, target, damage);
		target.receiveDamage(getGameModality(), damage, source);
	}
}