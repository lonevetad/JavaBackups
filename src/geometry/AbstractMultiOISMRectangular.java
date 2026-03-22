package geometry;

import java.util.Iterator;

import dataStructures.isom.InSpaceObjectsManagerImpl;
import dataStructures.isom.PathFinderIsom;
import geometry.implementations.PathOptimizerPoint;
import geometry.implementations.ProviderShapeRunnerImpl;
import tests.tDataStruct.Test_MultiISOMRetangularMap_V1;
import tools.LoggerMessages;

/** Refers to {@link Test_MultiISOMRetangularMap_V1}. */
public abstract class AbstractMultiOISMRectangular<Distance extends Number> extends InSpaceObjectsManagerImpl<Distance>
		implements AbstractObjectsInSpaceManager<Distance> {
	private static final long serialVersionUID = 1L;

	public AbstractMultiOISMRectangular() {
		log = LoggerMessages.LOGGER_DEFAULT;
		setProviderShapeRunner(ProviderShapeRunnerImpl.getInstance());
		setProviderShapesIntersectionDetector(ProviderShapesIntersectionDetector.getInstance());
		setPathOptimizer(PathOptimizerPoint.getInstance());
	}

	protected ProviderShapeRunner providerShapeRunner;
	protected ProviderShapesIntersectionDetector providerShapesIntersectionDetector;

	//
	//

	@Override
	public PathFinderIsom<Distance> getPathFinder() { return pathFinder; }

	@Override
	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() {
		return providerShapesIntersectionDetector;
	}

	@Override
	public ProviderShapeRunner getProviderShapeRunner() { return providerShapeRunner; }

	//

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		this.providerShapesIntersectionDetector = providerShapesIntersectionDetector;
	}

	@Override
	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) { // TODO Auto-generated method stub
		this.providerShapeRunner = providerShapeRunner;
	}

	@Override
	public Iterator<ObjectLocated> iterator() { // TODO Auto-generated method stub
		return null;
	}
}