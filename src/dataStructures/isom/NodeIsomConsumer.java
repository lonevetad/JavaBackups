package dataStructures.isom;

import java.awt.Point;

import geometry.pointTools.PointConsumer;

public interface NodeIsomConsumer<Distance extends Number> extends PointConsumer {

	public NodeIsomProvider<Distance> getNodeIsomProvider();

	public void setNodeIsomProvider(NodeIsomProvider<Distance> nodeIsomProvider);

	//

	/** Beware of nulls. */
	public void consume(NodeIsom<Distance> n);

	@Override
	public default void accept(Point location) { consume(getNodeAt(location)); }

	/** Proxy method that uses {@link NodeIsomProvider}. */
	public default NodeIsom<Distance> getNodeAt(Point location) {
		NodeIsomProvider<Distance> nip;
		nip = getNodeIsomProvider();
		return (nip == null) ? null : nip.getNodeAt(location);
	}

	/** Proxy method that uses {@link NodeIsomProvider}. */
	public default NodeIsom<Distance> getNodeAt(int x, int y) {
		NodeIsomProvider<Distance> nip;
		nip = getNodeIsomProvider();
		return (nip == null) ? null : nip.getNodeAt(x, y);
	}
}