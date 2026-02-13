package videogamesOldVersion.common.abstractCommon.referenceHolderAC;

import java.io.Serializable;

import videogamesOldVersion.common.FrameHolderDEFAULT;
import videogamesOldVersion.common.gui.GameObjectInMapView;

public interface FrameHolder extends Serializable {

	public int getLastFrame();

	/**
	 * Returned the current frame associated with some kind of painting process.<br>
	 * It's its task to update this value through {@link #setCurrentFrame(int)}.
	 * <p>
	 * If some {@link MainHolder} subclasses implements this interface, like
	 * {@link GameObjectInMapView}, then this method could be implemented as:
	 *
	 * <pre>
	 * <code>
	 * public int getCurrentFrame(){
	 * 	MainGeneric m = getMain();
	 * 	return m != null ? m.getCurrentFrame() : Integer.MIN_VALUE;
	 * }
	 * </code>
	 * </pre>
	 */
	public int getCurrentFrame();

	public FrameHolder setLastFrame(int lastFrame);

	public static FrameHolder newDefaultFrameHolder() {
		return new FrameHolderDEFAULT();
	}
}