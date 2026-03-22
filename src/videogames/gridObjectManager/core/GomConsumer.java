package videogames.gridObjectManager.core;

import geometry.pointTools.PointConsumer;

public interface GomConsumer extends PointConsumer {
	/**
	 * perform an action to the given {@link GridObjectManager}, referring to a
	 * given coordinates of the type (row, column) and the object hold by related
	 * node, if any.
	 */
//	public void accept(GridObjectManager gom, ObjectWithID node, int row, int column);

	public GridObjectManager getGridObjectManager();

	public void setGridObjectManager(GridObjectManager gom);

}