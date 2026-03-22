package dataStructures.isom.pathFinders;

import java.util.Comparator;
import java.util.function.Predicate;

import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.PathFinderIsom;
import dataStructures.isom.PathFinderIsomFrontierBased;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.NumberManager;

public abstract class PathFinderIsomBaseImpl<Distance extends Number> implements PathFinderIsomFrontierBased<Distance> {
//cannot be static
	protected final Comparator<NodeInfoFrontierBased<Distance>> COMPARATOR_NINFO_AS_POINT = newComparatorNodeInfo();

	protected PathFinderIsomBaseImpl(NodeIsomProvider<Distance> nodeIsomProvider) {
		super();
		this.nodeIsomProvider = nodeIsomProvider;
	}

	protected PathFinderIsomBaseImpl() { super(); }

//	protected final boolean isSynchronized;
	protected NodeIsomProvider<Distance> nodeIsomProvider;

	@Override
	public NodeIsomProvider<Distance> getNodeIsomProvider() { return nodeIsomProvider; }

	public void setNodeIsomProvider(NodeIsomProvider<Distance> nodeIsomProvider) {
		this.nodeIsomProvider = nodeIsomProvider;
	}

	//

	//

	//

	protected abstract class AdjacentForEacherBaseImpl extends AbstractAdjacentForEacher<Distance> {

		protected AdjacentForEacherBaseImpl(//
				Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
		}
	}

	protected abstract class ShapedAdjacentForEacherBaseImpl extends AbstractShapedAdjacentForEacher<Distance> {

		protected ShapedAdjacentForEacherBaseImpl(PathFinderIsom<Distance> pathFinderIsom, NodeIsomProvider<Distance> m,
				AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(pathFinderIsom, m, shape, isWalkableTester, distanceManager);
		}

		/*
		 * @Override public boolean isAdjacentNodeWalkable(NodeIsom adjacentNode) {
		 * shapeWalkableChecker.restart(); shape.setCenter(adjacentNode.x,
		 * adjacentNode.y); isom.runOnShape(shape, shapeWalkableChecker); return
		 * shapeWalkableChecker.isWalkable(); }
		 */
	}
}