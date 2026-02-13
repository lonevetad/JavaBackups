package videogamesOldVersion.common.abstractCommon.referenceHolderAC;

import java.awt.image.BufferedImage;

import games.generic.view.TileImage;
import tools.ObjectWithID;
import videogamesOldVersion.common.mainTools.AnimatedImage;

/**
 * Represent a class holding an image, more precisely {@link BufferedImage},
 * and/or an {@link AnimatedImage}.
 */
public interface ImageAnimationHolder extends ObjectWithID {

	public static ImageAnimationHolder newDefaultImplementation() { return new TileImage(); }

	/** Get the name associated to this image/animation. */
	public String getImageName();

	/**
	 * Returns the current image. <br>
	 * If the implementor has a field of type {@link BufferedImage} called
	 * {@code image}, then this method should be implemented as following:
	 *
	 * <pre>
	 * <code>
	 * public BufferedImage getImageResized() {
	 * 	AnimatedImage ai;
	 * 	ai = getAnimatedImage();
	 * 	return ai != null ? ai.getImage() : image;
	 * }
	 * </code>
	 * </pre>
	 */
	public BufferedImage getImageResized();

	public BufferedImage getImageOriginal();

	/**
	 * Get the {@link AnimatedImage} associated to this image holder.
	 * <p>
	 * Usual use:
	 *
	 * <pre>
	 * <code>
	AnimatedImage animatedImage;
	animatedImage = getAnimatedImage();
	if (animatedImage != null) {
	//do something
	}
	 * </code>
	 * </pre>
	 */
	public AnimatedImage getAnimatedImage();

	public int getWidth();

	public int getHeight();

	public boolean passTime(long millisPassed);

	//

	// TODO SETTER

	public ImageAnimationHolder setID(Integer iD);

	public ImageAnimationHolder setImageName(String imageName);

	public ImageAnimationHolder setImageResized(BufferedImage image);

	public ImageAnimationHolder setImageOriginal(BufferedImage image);

	/**
	 * Set the animation. <br>
	 * If the implementor has a field of type {@link BufferedImage} called
	 * {@code image}, then this method should be implemented as following:
	 *
	 * <pre>
	 * <code>
	 * public default ObjectTiled setAnimatedImage(AnimatedImage ai) {
	 * 	this.animatedImage = ai;
		if (ai != null) {
			setImageOriginal(ai.getImageOriginalSize());
			setImageResized(ai.getImageResized());
		}
		return this;
	 * </code>
	 * </pre>
	 */
	public ImageAnimationHolder setAnimatedImage(AnimatedImage ai);

	//

	// TODO OTHERS

	// public boolean reloadImage();

	//

	// TODO DEFAULT
	/**
	 * Update any possible animation with the given amount of time (expressed in
	 * milliseconds).
	 */
	public default void updateAnimation(int millis) { getNextImage(millis); }

	/**
	 * Update any possible animation with the given amount of time (expressed in
	 * milliseconds) and get the current image (that is a animation's frame if that
	 * animation exists, or a "static" image otherwise).<br>
	 * Beware of <code>null</code>s.
	 *
	 * @param millis update the progression of animation by given amount of time in
	 *               milliseconds, then return the current frame
	 */
	public default BufferedImage getNextImage(int millis) {
		AnimatedImage ai;
		BufferedImage bi;
		if ((ai = getAnimatedImage()) == null && getImageOriginal() == null) {
			// reloadImage();
			return null;
		}
		if (ai != null) {
			if (millis > 0)
				ai.passTime(millis);
			setImageResized(bi = ai.getImageResized());
			setImageOriginal(ai.getImageOriginalSize());
			return bi;
		}
		bi = getImageResized();
		return bi != null ? bi : getImageOriginal();
	}
}