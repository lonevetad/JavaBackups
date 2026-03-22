package games.generic.controlModel;

import java.awt.Point;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.SetMapped;
import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.InSpaceObjectsManagerImpl;
import dataStructures.isom.NodeIsom;
import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.holders.GObjectsHolder;
import games.generic.controlModel.objects.ObjectInSpace;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.IGameModalityEventBased;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ProviderShapesIntersectionDetector;
import geometry.pointTools.PointConsumer;

/**
 * One of the core classes.
 * <p>
 * "Space"-focused handler for objects in game.<br>
 * Wraps a {@link InSpaceObjectsManagerImpl} instance, delegating to it the real
 * object management, to let {@link GEvent}s to be fired through subclasses of
 * {@link GModality} returned by {@link #getGameModality()}.
 */
public interface GObjectsInSpaceManager extends GModalityHolder, GObjectsHolder<ObjectInSpace>, Cloneable {

	public static final String OISM_NAME = "oism";

	/** Delegates there the real objects management */
	public InSpaceObjectsManager<Double> getOIMManager();

	//

	/**
	 * Used by {@link GObjectsHolder} (like {@link GModel}) to add this instance of
	 * {@link GObjectsHolder}.
	 */
	public default String getNameGObjHolder() { return OISM_NAME; }

	/**
	 * The "space" concept could not be atomic and so could be divided in smaller
	 * parts, like the <i>meter</i> could be divided in <i>centimeters</i> or even
	 * <i>millimeters</i>. See the "return" section for further informations.
	 *
	 * @return The amount of <i>sub-units</i> that each <i>"space macro-unit"</i> is
	 *         subdivided into. A number greater than one means that the space has a
	 *         concept of <i>macro sections</i>, like <i>meter</i> has
	 *         <i>centimeters</i>. Just <code>1</code> means that the space is at
	 *         its finest granularity. A zero or negative number means <i>the space
	 *         can be infinitively subdivided</i>.
	 */
	public int getSpaceSubunitsEachMacrounits();

	//

	public boolean containsObject(ObjectInSpace o);

	/**
	 * To be intended as "teleport" from the object's location to a destination
	 * point. <br>
	 * <code>
		if(! contains(o)){ addObject(true, o); }
		else if(to==null)removeObject(false, o);
		// else then TELEPORT
	 * </code>
	 */
	public default boolean moveObject(ObjectInSpace o, Point to) {
		if (o == null)
			return false;
		if (!this.containsObject(o)) {
			o.setLocation(to);
			this.addObject(true, o);
		} else if (to == null) {
			this.removeObject(true, o);
		} else {
			GModality gm;
			Point prevLocation;
			prevLocation = new Point(o.getLocation());
			this.removeObject(false, o);
			o.setLocation(to);
			this.addObject(false, o);
			gm = this.getGameModality();
			if (gm instanceof IGameModalityEventBased) {
				IGameModalityEventBased gme;
				gme = (IGameModalityEventBased) gm;
				gme.getEventInterface().fireGameObjectMoved((GModalityET) gm, prevLocation, o);
			}
		}
		return true;
	}

	public default boolean addObject(ObjectInSpace o) { return addObject(true, o); }

	/**
	 * Set the object's location before adding it.
	 *
	 * @param fireEvent specify if a "add" event should be fired
	 */
	public default boolean addObject(boolean fireEvent, ObjectInSpace o) {
		boolean added;
		added = getOIMManager().add(o);
		if (added && fireEvent) {
			GModality gm;
			gm = this.getGameModality();
			if (gm instanceof IGameModalityEventBased) {
				IGameModalityEventBased gme;
				gme = (IGameModalityEventBased) gm;
				gme.getEventInterface().fireGameObjectAdded((GModalityET) gme, o);
			}
		}
		return added;
	}

	public default boolean removeObject(ObjectInSpace o) { return removeObject(true, o); }

	/**
	 * @param fireEvent specify if a "remove" event should be fired
	 */
	public default boolean removeObject(boolean fireEvent, ObjectInSpace o) {
		boolean removed;
		removed = getOIMManager().remove(o);
		if (removed && fireEvent) {
			GModality gm;
			gm = this.getGameModality();
			if (gm instanceof IGameModalityEventBased) {
				IGameModalityEventBased gme;
				gme = (IGameModalityEventBased) gm;
				gme.getEventInterface().fireGameObjectRemoved((GModalityET) gme, o);
			}
		}
		return removed;
	}

	//

	//

	// trom GObjHolder

	@Override
	public default Set<ObjectInSpace> getObjects() {
		SetMapped<ObjectLocated, ObjectInSpace> sm;
		sm = new SetMapped<>(this.getOIMManager().getAllObjectLocated(), ol -> { return (ObjectInSpace) ol; });
		sm.setReverseMapper(owid -> {
			if (owid instanceof ObjectLocated)
				return (ObjectLocated) owid;
			throw new IllegalArgumentException("Cannot add/remove a non-ObjectLocated to/from original set");
		});
		return sm;
	}

	@Override
	public default boolean add(ObjectInSpace o) {
		if (o == null)
			return false;
		return addObject(o);
	}

	@Override
	public default boolean remove(ObjectInSpace o) {
		if (o == null)
			return false;
		return removeObject(o);
	}

	@Override
	public default boolean contains(ObjectInSpace o) {
		if (o == null)
			return false;
		return containsObject(o);
	}

	@Override
	public default boolean removeAll() { return this.getOIMManager().removeAllObjects(); }

	@Override
	public default ObjectInSpace get(Long id) { return (ObjectInSpace) this.getOIMManager().getObjectLocated(id); }

	@Override
	public default void forEach(Consumer<ObjectInSpace> action) { this.getObjects().forEach(action); }

	//

	//

	// also proxies

	public default ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() {
		return getOIMManager().getProviderShapesIntersectionDetector();
	}

	public default Set<ObjectLocated> findAll(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter) {
		return getOIMManager().fetch(areaToLookInto, objectFilter);
	}

	/** Queris all objects located in the given area, if any. */
	public default Set<ObjectLocated> findAll(AbstractShape2D areaToLookInto) {
		return this.findAll(areaToLookInto, null);
	}

	/**
	 * Refers to
	 * {@link InSpaceObjectsManagerImpl#findInPath(AbstractShape2D, Predicate, List) }.
	 */
	public default Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		return getOIMManager().findInPath(areaToLookInto, objectFilter, path);
	}

	/**
	 * Refers to {@link findInPath(AbstractShape2D, Predicate, List) }, giving to it
	 * <code>null</code> as {@link Predicate}.
	 */
	public default Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, List<Point> path) {
		return findInPath(areaToLookInto, null, path);
	}

	public default void runOnShape(AbstractShape2D shape, PointConsumer action) {
		getOIMManager().runOnShape(shape, action);
	}

	public default NodeIsom<Double> getNodeAt(Point location) { return getOIMManager().getNodeAt(location); }
}