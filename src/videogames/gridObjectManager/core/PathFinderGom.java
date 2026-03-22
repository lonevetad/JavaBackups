package videogames.gridObjectManager.core;

import java.util.List;

public interface PathFinderGom {
	public default <E> List<NodeGOM> findPath(GridObjectManager gom, NodeGOM start, NodeGOM dest, AbstractShape2D shape) {
		return findPath(gom, start, dest, shape, null);
	}

	public <E> List<NodeGOM> findPath(GridObjectManager gom, NodeGOM start, NodeGOM dest, AbstractShape2D shape,
			PathOptimizer optimizer);
}