package videogamesOldVersion.common.abstractCommon;

import java.io.Serializable;

import videogamesOldVersion.common.abstractCommon.referenceHolderAC.ImageAnimationHolder;

public interface AnimationName extends Serializable {
	/**
	 * Returns the name of the animation. Could be overrided by
	 * {@link java.lang.Enum}.
	 */
	public String name();

	/**
	 * Retrun a composed name formed thi way:<br>
	 * <code>{@link ImageAnimationHolder#getAnimatedImage()} + "_" + {@link AnimationName#name()}</code>.
	 */
	public default String fileNameImage(ImageAnimationHolder tima) {
		return tima == null ? null : tima.getImageName() + "_" + name();
	}
}
