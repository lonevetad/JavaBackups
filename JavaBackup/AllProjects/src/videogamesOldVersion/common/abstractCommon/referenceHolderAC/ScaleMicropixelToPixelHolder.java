package common.abstractCommon.referenceHolderAC;

import videogamesOldVersion.common.mainTools.mOLM.MatrixObjectLocationManager;

public interface ScaleMicropixelToPixelHolder {

	public static final int SCALE_DEFAULT = 16;

	/**
	 * The scale used to scale the shape from
	 * {@link MatrixObjectLocationManager}'s "<i>micropixel</i>" to the real,
	 * graphic pixels.<br>
	 * {@code int} was chosen because it's more efficient than {@code double}.
	 */
	public int getScaleMicropixelToRealpixel();

	/** Return this if some changes has been performed, null otherwise */
	public ScaleMicropixelToPixelHolder setScaleMicropixelToRealpixel(int scaleMicropixelToRealpixel);
}