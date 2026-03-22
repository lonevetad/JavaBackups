package dataStructures.graph;

import java.io.Serializable;
import java.util.function.Predicate;

import tools.NumberManager;
import tools.PathFinder;

public interface PathFindStrategy<NodeType, Distance extends Number>
		extends PathFinder<NodeType, NodeType, Distance>, Serializable {

	public abstract PathGraph<NodeType, Distance> getPath(GraphSimple<NodeType, Distance> graph, NodeType start,
			NodeType dest, NumberManager<Distance> distanceManager, Predicate<NodeType> isWalkableTester);
}