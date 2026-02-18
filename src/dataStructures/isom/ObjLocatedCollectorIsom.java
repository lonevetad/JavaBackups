package dataStructures.isom;

import java.awt.Point;
import java.util.Set;

import geometry.ObjectLocated;
import geometry.pointTools.impl.ObjCollector;

public interface ObjLocatedCollectorIsom<Distance extends Number>
		extends ObjCollector<ObjectLocated>, NodeIsomConsumer<Distance> {

	@Override
	public default ObjectLocated getAt(Point location) {
		NodeIsom<Distance> n;
		n = getNodeAt(location);
		return (n == null) ? null : n.getObject(0); // just a random object
	}

//	public NodeIsom getNodeAt(Point location);

	@Override
	public default void consume(NodeIsom<Distance> n) {
		final Set<ObjectLocated> co;
		if (n == null)
			return;
		co = getCollectedObjects();
		n.forEachAcceptableObject(getTargetsFilter(), co::add);
	}

	@Override
	default void accept(Point location) { NodeIsomConsumer.super.accept(location); }
}