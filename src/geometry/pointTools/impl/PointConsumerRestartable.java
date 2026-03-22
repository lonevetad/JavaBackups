package geometry.pointTools.impl;

import geometry.pointTools.PointConsumer;

public interface PointConsumerRestartable extends PointConsumer {

	public void restart();
}