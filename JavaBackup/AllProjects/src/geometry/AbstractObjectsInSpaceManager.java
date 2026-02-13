package geometry;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.isom.ObjLocatedCollectorIsom;
import geometry.pointTools.PointConsumer;
import geometry.pointTools.impl.ObjCollector;
import tools.NumberManager;
import tools.PathFinder;

/**
 * Abstract definition of a handler and manager for objects related to a "space"
 * concept (i.e. {@link ObjectLocated} and {@link ObjectShaped}).
 */
public interface AbstractObjectsInSpaceManager<Distance extends Number> extends Iterable<ObjectLocated>, Serializable {

	/**
	 * Representation of the space managed by this instance. Could be something
	 * simple or complex.<br>
	 * It's mandatory, usually.
	 */
	public AbstractShape2D getBoundingShape();

	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector();

	public ProviderShapeRunner getProviderShapeRunner();

	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector);

	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner);

	//

	//

	/**
	 * See {@link #getAt(int, int)};
	 *
	 * @param location the location to look at
	 * @return
	 */
	public default ObjectLocated getAt(Point location) { return getAt(location.x, location.y); }

	/**
	 * Returns the first available object which occupies that location, or
	 * {@code null} if none is available.
	 *
	 * @param x the x-component of the location to look at
	 * @param y the y-component of the location to look at
	 * @return the first available object which occupies that location, or
	 *         {@code null} if none is available
	 */
	public ObjectLocated getAt(int x, int y);

	/**
	 * Add the given {@link ObjectLocated} into the space managed by this manager.
	 * <br>
	 * Note: Should check if the given {@link ObjectLocated} lies inside the
	 * {@link AbstractShape2D} (provided by {@link #getBoundingShape()}) through its
	 * method {@link AbstractShape2D#contains(Point)}.
	 */
	public boolean add(ObjectLocated o);

	public boolean contains(ObjectLocated o);

	public default boolean containsAt(Point location) { return containsAt(location.x, location.y); }

	public default boolean containsAt(int x, int y) { return this.getAt(x, y) != null; }

	public boolean remove(ObjectLocated o);

	/** See {@link #addAt(ObjectLocated, int, int)}. */
	public default boolean addAt(ObjectLocated o, Point location) {
		return addAt(o, location.x, location.y);
	}

	/**
	 * Called by {@link #add(ObjectLocated)}, but adding the given object at the
	 * specified location (that could NOT be the one provided by
	 * {@link ObjectLocated#getLocation()}).
	 */
	public default boolean addAt(ObjectLocated o, int x, int y) {
		boolean c;
		int xo, yo;
		Point oldLocation;
		if (o == null)
			return false;
		oldLocation = o.getLocation();
		xo = oldLocation.x;
		yo = oldLocation.y;
		o.setLocation(x, y);
		c = add(o);
		o.setLocation(xo, yo);
		return c;
	}

	/** See {@link #containsAt(ObjectLocated, int, int)}. */
	public default boolean containsAt(ObjectLocated o, Point location) {
		return containsAt(o, location.x, location.y);
	}

	public default boolean containsAt(ObjectLocated o, int x, int y) {
		boolean c;
		int xo, yo;
		Point oldLocation;
		if (o == null)
			return false;
		oldLocation = o.getLocation();
		xo = oldLocation.x;
		yo = oldLocation.y;
		o.setLocation(x, y);
		c = contains(o);
		o.setLocation(xo, yo);
		return c;
	}

	/** See {@link #removeAt(ObjectLocated, int, int)}. */
	public default boolean removeAt(ObjectLocated o, Point location) {
		return removeAt(o, location.x, location.y);
	}

	public default boolean removeAt(ObjectLocated o, int x, int y) {
		boolean c;
		int xo, yo;
		Point oldLocation;
		if (o == null)
			return false;
		oldLocation = o.getLocation();
		xo = oldLocation.x;
		yo = oldLocation.y;
		o.setLocation(x, y);
		c = remove(o);
		o.setLocation(xo, yo);
		return c;
	}

	//

	public default boolean clearArea(AbstractShape2D areaToClear) { return remove(areaToClear, null); }

	public default boolean remove(AbstractShape2D areaToClear, Predicate<ObjectLocated> objectFilter) {
		Set<ObjectLocated> objToRemove;
		boolean[] removed = { true };
		objToRemove = fetch(areaToClear, objectFilter);
		if (objToRemove == null || objToRemove.isEmpty())
			return false;
//		objToRemove.forEach(this::remove);
		objToRemove.forEach(o -> { removed[0] = remove(o); });
		return removed[0];
	}

	//

//	public Set<ObjectLocated> fetch(double x, double y, Predicate<ObjectLocated> objectFilter);
//	public default Set<ObjectLocated> fetch(double x, double y) {return fetch(x, y, null);}
//	public default Set<ObjectLocated> fetch(Point whereToLookFor, Predicate<ObjectLocated> objectFilter) {
//		return this.fetch(whereToLookFor.getX(), whereToLookFor.getY(), objectFilter);}
//	public default Set<ObjectLocated> fetch(Point whereToLookFor) {
//		return this.fetch(whereToLookFor.getX(), whereToLookFor.getY());}

	public ObjLocatedCollectorIsom<Distance> newObjLocatedCollector(Predicate<ObjectLocated> objectFilter);

	/**
	 * Like {@link #fetch(AbstractShape2D)}, but providing a filtering over the
	 * found objects. A <code>null</code> filter should suggests "retrieve all
	 * objects found".
	 */
	public default Set<ObjectLocated> fetch(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter) {
		ObjLocatedCollectorIsom<Distance> c;
		c = newObjLocatedCollector(objectFilter);
		this.runOnShape(areaToLookInto, c);
		return c.getCollectedObjects();
	}

	/** Queris all objects located in the given area, if any. */
	public default Set<ObjectLocated> fetch(AbstractShape2D areaToLookInto) {
		return this.fetch(areaToLookInto, null);
	}

	/**
	 * Quereis all objects located in the given area, if any, moving that area along
	 * a specific path, that requires at least two point (the starting point must be
	 * provided, the last point is the end.
	 *
	 * @param areaToLookInto area to query
	 * @param collector      methodology to collect the objects found
	 */
	public Set<ObjectLocated> collectInPath(AbstractShape2D areaToLookInto, ObjCollector<ObjectLocated> collector,
			List<Point> path);

	/**
	 * Queries all objects located in the given area, if any, moving linearly that
	 * area along a specific path, that requires at least two point (the starting
	 * point must be provided, the last point is the end.
	 */
	public default Set<ObjectLocated> collectInPath(AbstractShape2D areaToLookInto, List<Point> path) {
		return this.collectInPath(areaToLookInto, null, path);
	}

	//
	/**
	 * As for {@link #runOnShape(AbstractShape2D, IsomConsumer)}, but giving the
	 * {@link AbstractShape2D} returned by {@link #getSpaceShape()}.
	 */
	public default void runOnWholeMap(PointConsumer action) { runOnShape(getBoundingShape(), action); }

	public default void runOnShape(AbstractShape2D shape, PointConsumer action) {
		AbstractShapeRunner runner;
		ProviderShapeRunner psr;
		if (shape == null || action == null)
			return;
		psr = this.getProviderShapeRunner();
		runner = psr.getShapeRunner(shape);
		if (runner == null)
			return;
		runner.runShape(shape, action);
	}

	/**
	 * Find a path from the starting point (first parameter) to the end (second
	 * parameter).<br>
	 * See
	 * {@link PathFinder#getPath(Object, Object, NumberManager, Predicate, boolean)}.
	 */
	public default <NoteType, NodeContent extends ObjectLocated, D extends Number> List<NoteType> getPath(
			NoteType start, NoteType destination, PathFinder<NoteType, NodeContent, D> pathFinder,
			NumberManager<D> numManager, Predicate<NodeContent> isWalkableTester,
			boolean returnPathToClosestNodeIfNotFound) {
		return pathFinder.getPath(start, destination, numManager, isWalkableTester, returnPathToClosestNodeIfNotFound);
	}

	/**
	 * See {@link #getPath(Point, Point, PathFinder, NumberManager, Predicate)}.
	 */
	public default <NoteType extends Point, NodeContent extends ObjectLocated, D extends Number> List<NoteType> getPath(
			NoteType start, NoteType destination, PathFinder<NoteType, NodeContent, D> pathFinder,
			NumberManager<D> numManager, boolean returnPathToClosestNodeIfNotFound) {
		return this.getPath(start, destination, pathFinder, numManager, null, returnPathToClosestNodeIfNotFound);
	}

	/**
	 * See
	 * {@link PathFinder#getPath(ObjectShaped, Object, NumberManager, Predicate)}.
	 */
	public default <NodeContent extends ObjectLocated, D extends Number> List<Point> getPath(
			ObjectShaped objRequiringToMove, Point destination, PathFinder<Point, NodeContent, D> pathFinder,
			NumberManager<D> numManager, Predicate<NodeContent> isWalkableTester,
			boolean returnPathToClosestNodeIfNotFound) {
		return pathFinder.getPath(objRequiringToMove, destination, numManager, isWalkableTester,
				returnPathToClosestNodeIfNotFound);
	}

	/**
	 * See {@link PathFinder#getPath(ObjectShaped, Object, NumberManager)}.
	 */
	public default <NodeContent extends ObjectLocated, D extends Number> List<Point> getPath(
			ObjectShaped objRequiringToMove, Point destination, PathFinder<Point, NodeContent, D> pathFinder,
			NumberManager<D> numManager, boolean returnPathToClosestNodeIfNotFound) {
		return this.getPath(objRequiringToMove, destination, pathFinder, numManager, null,
				returnPathToClosestNodeIfNotFound);
	}

	//

	// TODO OIS Consumer

}