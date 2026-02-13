package oldToBeDeleted;

import java.awt.geom.Point2D;
import java.util.function.Predicate;

import geometry.AbstractShape2D;
import geometry.ObjectLocated;

@Deprecated
public abstract class ObjectsInSpaceConsumerImpl implements ObjectsInSpaceConsumer {
	protected AbstractShape2D areaToLookInto;
	protected Predicate<ObjectLocated> objectFilter;

	public ObjectsInSpaceConsumerImpl() {
		this(null, null);
	}

	public ObjectsInSpaceConsumerImpl(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter) {
		super();
		this.areaToLookInto = areaToLookInto;
		this.objectFilter = objectFilter;
	}

	//

	@Override
	public AbstractShape2D getArea() {
		return areaToLookInto;
	}

	@Override
	public Predicate<ObjectLocated> getObjectFilter() {
		return objectFilter;
	}

	@Override
	public ObjectsInSpaceConsumer setArea(AbstractShape2D areaToLookInto) {
		this.areaToLookInto = areaToLookInto;
		return this;
	}

	@Override
	public ObjectsInSpaceConsumer setObjectFilter(Predicate<ObjectLocated> objectFilter) {
		this.objectFilter = objectFilter;
		return this;
	}

	@Override
	public final void accept(Point2D location, ObjectLocated objectInSpace) {
		if ((areaToLookInto != null && (!areaToLookInto.contains(objectInSpace.getLocation()))) || (objectFilter != null && (!objectFilter.test(objectInSpace))))
			return;
		acceptImpl(location, objectInSpace);
	}
}