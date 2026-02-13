package dataStructures.isom;

import java.awt.Point;

import dataStructures.isom.internal.ISOMWrapperLocated;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import dataStructures.isom.pathFinders.Heuristic8GridMovement;
import dataStructures.isom.pathFinders.PathFinderIsomAStar;
import geometry.AbstractShapeRunner;
import tools.NumberManager;

/**
 * Implementation of {@link NodeIsomProvider} optimized for iterating (as
 * {@link AbstractShapeRunner} subclasses does) over sets of {@link Point} (used
 * to obtain {@link NodeIsom}).<br>
 * It's optimized because it caches the instances of
 * {@link MatrixInSpaceObjectsManager}
 */
public class MultiISOMRetangularCaching<Dd extends Number> extends MultiISOMRetangularMap<Dd> {
	private static final long serialVersionUID = -58691725492810L;

	public MultiISOMRetangularCaching() {
		super();
		this.cachedMW = null;
	}

	public MultiISOMRetangularCaching(int maximumSubmapsEachSection) {
		super(maximumSubmapsEachSection);
		this.cachedMW = null;
	}

	protected ISOMWrapperLocated<Dd> cachedMW;

	public MatrixInSpaceObjectsManager<Dd> getCachedMisom() { return cachedMW == null ? null : cachedMW.misom; }

	///

	@Override
	public ISOMWrapperLocated<Dd> getMapLocatedContaining(int x, int y) {
		if (cachedMW == null || (!cachedMW.contains(x, y))) { cachedMW = super.getMapLocatedContaining(x, y); }
		return cachedMW;
	}

	//

	@Override
	protected PathFinderIsom<Dd> newPathFinder() {
// faster than Dijkstra thanks to 8-connectivity and unary neighbor's step's weight
//		return new PathFinderIsomBFS<>(this);
//		return new PathFinderIsomDijkstra<>(this);
//		return new PathFinderIsomAStar_Naive<>(this, new Heuristic8GridMovement<>(getWeightManager()));
		return new PathFinderIsomAStar<>(this, new Heuristic8GridMovement<>(getWeightManager()));
	}

	// override because of the NumberManager-dependency of heuristic for AStar
	@Override
	public void setWeightManager(NumberManager<Dd> numberManager) {
		this.weightManager = numberManager;
		this.setPathFinder(newPathFinder());
	}

	@Override
	public String toString() {
		return "MultiISOMRetangularCaching [\n\t idProg=" + idProg + ", maxDepth=" + maxDepth + ",\n\t xLeftTop="
				+ xLeftTop + ", yLeftTop=" + yLeftTop + ", width=" + width + ", height=" + height + "\n]";
	}
}