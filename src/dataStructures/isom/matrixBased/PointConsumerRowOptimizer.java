package dataStructures.isom.matrixBased;

import java.awt.Point;

import dataStructures.isom.NodeIsom;
import geometry.pointTools.impl.PointConsumerRestartable;

public abstract class PointConsumerRowOptimizer<Distance extends Number> implements PointConsumerRestartable {
	private static final long serialVersionUID = 1L;

	public PointConsumerRowOptimizer(MatrixInSpaceObjectsManager<Distance> misom) {
		this.misom = misom;
		y = 0;
		rowCache = null;
	}

	protected int y;
	protected NodeIsom<Distance>[] rowCache;
	protected MatrixInSpaceObjectsManager<Distance> misom;

//

//

	public int getY() { return y; }

	public NodeIsom<Distance>[] getRowCache() { return rowCache; }

	public MatrixInSpaceObjectsManager<Distance> getMisom() { return misom; }

	//

	public PointConsumerRowOptimizer<Distance> setMisom(MatrixInSpaceObjectsManager<Distance> misom) {
		if (this.misom != misom) {
			rowCache = null; // empty the cache
			y = -1;
		}
		this.misom = misom;
		return this;
	}

	//

	public abstract void acceptImpl(Point t);

	@Override
	public final void accept(Point p) {
		int yy;
		if (p == null)
			return;
		yy = (int) p.getY();
		if (rowCache == null || yy != y)
			rowCache = misom.getRow(y = yy);
		acceptImpl(p);
	}

	@Override
	public void restart() {
		y = 0;
		rowCache = null;
	}
}