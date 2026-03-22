package dataStructures.isom.matrixBased;

import java.awt.Point;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.MultiISOMRetangularCaching;
import dataStructures.isom.MultiISOMRetangularMap;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.ObjLocatedCollectorIsom;
import dataStructures.isom.internal.ISOMWrapperLocated;
import geometry.ObjectLocated;

/**
 * Not advised to be used.
 * <p>
 * An {@link ObjLocatedCollectorIsom} that benefits from 2 types of caches:
 * <ul>
 * <li>{@link MatrixInSpaceObjectsManager} in case of use under multi-"misom"
 * environment (i.e.: {@link MultiISOMRetangularMap}), cached tanks to
 * {@link NodeIsomProviderCachingMISOM}.</li>
 * <li>{@link NodeIsom}, cached thanks to {@link PointConsumerRowOptimizer}
 * (it's a inner instance, not a superclass).</li>
 * </ul>
 */
public class ObjLocatedCollectorMultimatrix<Distance extends Number> implements ObjLocatedCollectorIsom<Distance> {
	private static final long serialVersionUID = 43L;

	public ObjLocatedCollectorMultimatrix(MultiISOMRetangularMap<Distance> multiMatrix,
			Predicate<ObjectLocated> targetFilter) {
		this.isomProvider = multiMatrix;
		this.olcm = new ObjLocatedCollectorMatrix<>(null, targetFilter) {
			private static final long serialVersionUID = 6390384107528L;

			@Override
			public NodeIsomProvider<Distance> getNodeIsomProvider() { return isomProvider; }
		};
	}

	protected MultiISOMRetangularMap<Distance> isomProvider;
	protected ObjLocatedCollectorMatrix<Distance> olcm; // this is the implementation for SINGLE matrix!!

	@Override
	public void accept(Point location) {
		ISOMWrapperLocated<Distance> ml;
		InSpaceObjectsManager<Distance> misom;
		ml = isomProvider.getMapLocatedContaining(location);
		if (ml == null)
			return; // no null allowed here!
		misom = ml.getIsomHeld();

		if (!(misom instanceof MatrixInSpaceObjectsManager<?>)) { return; }
		if (misom != olcm.getMisom()) {
			// reset the row-cache
			olcm.setMisom((MatrixInSpaceObjectsManager<Distance>) misom);
		}
		// shift the point to the offset
		location = new Point(location.x - ml.getx(), location.y - ml.gety());
		olcm.accept(location);
	}

	// proxies

	@Override
	public Predicate<ObjectLocated> getTargetsFilter() { return olcm.targetFilter; }

	@Override
	public Set<ObjectLocated> getCollectedObjects() { return olcm.getCollectedObjects(); }

	@Override
	public NodeIsomProvider<Distance> getNodeIsomProvider() { return isomProvider; }

	@Override
	public void setNodeIsomProvider(NodeIsomProvider<Distance> nodeIsomProvider) {
		if (nodeIsomProvider instanceof MultiISOMRetangularCaching<?>)
			this.isomProvider = (MultiISOMRetangularCaching<Distance>) nodeIsomProvider;
	}

}