package dataStructures.isom.matrixBased;

import java.awt.Point;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.ObjLocatedCollectorIsom;
import geometry.ObjectLocated;
import tools.Comparators;

/**
 * Collector of objects (it's a subclass of {@link ObjLocatedCollectorIsom})
 * that's optimized for matrix-based {@link InSpaceObjectsManager} (in fact, it
 * requires a {@link MatrixInSpaceObjectsManager}) and based to just a single
 * instance of it.
 */
public class ObjLocatedCollectorMatrix<Distance extends Number> extends PointConsumerRowOptimizer<Distance>
		implements ObjLocatedCollectorIsom<Distance> {
	private static final long serialVersionUID = 1L;

	public ObjLocatedCollectorMatrix(MatrixInSpaceObjectsManager<Distance> misom,
			Predicate<ObjectLocated> targetFilter) {
		super(misom);
		this.targetFilter = targetFilter;
		objFoundBack = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.LONG_COMPARATOR);
		objFound = objFoundBack.toSetValue(ObjectLocated.KEY_EXTRACTOR);
	}

	protected MapTreeAVL<Long, ObjectLocated> objFoundBack;
	protected Set<ObjectLocated> objFound;
	protected Predicate<ObjectLocated> targetFilter;

	@Override
	public Predicate<ObjectLocated> getTargetsFilter() { return targetFilter; }

	@Override
	public Set<ObjectLocated> getCollectedObjects() { return objFound; }

	@Override
	public NodeIsom<Distance> getNodeAt(Point location) {
		int x;
		x = (int) location.getX();
		return (0 <= x && x < this.rowCache.length) ? this.rowCache[x] : null;
	}

	@Override
	public void acceptImpl(Point location) {
		/*
		 * NodeIsom n; n = this.rowCache[(int) t.getX()];
		 * n.forEachAcceptableObject(targetFilter, o -> { if (o == null) return;
		 * objFound.put(o.getID(), o); });
		 */
		ObjLocatedCollectorIsom.super.accept(location);
	}

	@Override
	public NodeIsomProvider<Distance> getNodeIsomProvider() { return this.getMisom(); }

	@Override
	public void setNodeIsomProvider(NodeIsomProvider<Distance> nodeIsomProvider) {
		setMisom((MatrixInSpaceObjectsManager<Distance>) nodeIsomProvider);
	}
}