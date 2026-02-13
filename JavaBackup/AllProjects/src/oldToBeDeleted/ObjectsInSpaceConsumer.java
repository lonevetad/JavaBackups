package oldToBeDeleted;

import java.awt.geom.Point2D;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import geometry.AbstractShape2D;
import geometry.ObjectLocated;

/**
 * Apply an action to a given point in this space and to a given instance of
 * {@link ObjectLocated}.
 */
@Deprecated
public interface ObjectsInSpaceConsumer extends BiConsumer<Point2D, ObjectLocated> {

	/**
	 * Filter the whole space to the given one. If <code>null</code>, then the whole
	 * space is used
	 */
	public AbstractShape2D getArea();

	/**
	 * Filter the objects provided. If <code>null</code>, then every objects are
	 * accepted.
	 */
	public Predicate<ObjectLocated> getObjectFilter();

	public ObjectsInSpaceConsumer setArea(AbstractShape2D areaToLookInto);

	public ObjectsInSpaceConsumer setObjectFilter(Predicate<ObjectLocated> objectFilter);

	@Override
	public default void accept(Point2D location, ObjectLocated objectInSpace) {
		AbstractShape2D areaToLookInto;
		Predicate<ObjectLocated> objectFilter;
		areaToLookInto = getArea();
		objectFilter = getObjectFilter();
		if ((areaToLookInto != null && (!areaToLookInto.contains(objectInSpace.getLocation()))) || (objectFilter != null && (!objectFilter.test(objectInSpace))))
			return;
		acceptImpl(location, objectInSpace);
	}

	public void acceptImpl(Point2D t, ObjectLocated u);
}