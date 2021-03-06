package dataStructures.isom;

import geometry.pointTools.PointConsumer;

public interface IsomConsumer extends PointConsumer {
	/**
	 * perform an action to the given {@link InSpaceObjectsManager}, referring to a
	 * given coordinates of the type (row, column) and the object hold by related
	 * node, if any.
	 */
//	public void accept(GridObjectManager gom, ObjectWithID node, int row, int column);

	public InSpaceObjectsManager getInSpaceObjectsManager();

	public void setInSpaceObjectsManager(InSpaceObjectsManager isom);
}