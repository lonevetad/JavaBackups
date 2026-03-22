package games.generic.view;

import java.awt.Image;
import java.awt.image.BufferedImage;

import tools.CastingClass;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.ImageAnimationHolder;
import videogamesOldVersion.common.mainTools.AnimatedImage;

/**
 * Simple object holding two {@link BufferedImage} (one for the original size as
 * read by file and one resized) and a {@link AnimatedImage} (who is used both
 * for original and resized size, thanks to its implementation that keeps inside
 * it the original {@link BufferedImage} and the resized ones).
 * <p>
 * The original {@link BufferedImage} and the {@link AnimatedImage} are used for
 * sharing and reducing I/O operations and memory resources.<br>
 * WARNING: In particular, {@link AnimatedImage} is shared but its internal and
 * <i>original<i> {@link BufferedImage} is shared. The : if it's resized through
 * {@link AnimatedImage#resizeAllImages(int, int)}, then ALL of the objects
 * holding an instance of {@link AnimatedImage} coming from this class will be
 * affected.<br>
 * Call once the method {@link #cloneAnimationFromThis()} to make the
 * {@link AnimatedImage} stored independent.
 *
 * @deprecated too old and clugged
 */
@Deprecated
public class TileImage implements ImageAnimationHolder {
	private static final long serialVersionUID = 5418910009096512990L;

	public TileImage() { super(); }

	public TileImage(ImageAnimationHolder iah) {
		this();
		setFrom(iah);
	}

	protected Long ID;
	protected String imageName;
	protected transient AnimatedImage animatedImage;
	protected transient BufferedImage imageResized;
	protected transient BufferedImage imageOriginal;

	//

	// TODO GETTER

	@Override
	public Long getID() { return ID; }

	@Override
	public String getImageName() { return imageName; }

	@Override
	public AnimatedImage getAnimatedImage() { return animatedImage; }

	@Override
	public BufferedImage getImageOriginal() {
		return this.animatedImage != null ? (imageOriginal = animatedImage.getImageOriginalSize()) : imageOriginal;
	}

	/** Works as a cache and shall be used. */
	@Override
	public BufferedImage getImageResized() {
		return animatedImage != null ? (imageResized = animatedImage.getImageResized()) : imageResized;
	}

	@Override
	public int getWidth() {
		return animatedImage != null ? animatedImage.getWidth() : (imageResized != null ? imageResized.getWidth() : -1);
	}

	@Override
	public int getHeight() {
		return animatedImage != null ? animatedImage.getHeight()
				: (imageResized != null ? imageResized.getHeight() : -1);
	}

	//

	@Override
	public TileImage setID(Integer iD) {
		ID = iD;
		return this;
	}

	@Override
	public TileImage setImageOriginal(BufferedImage imageOriginal) {
		this.imageOriginal = imageOriginal;
		return this;
	}

	@Override
	public TileImage setAnimatedImage(AnimatedImage animatedImage) {
		this.animatedImage = animatedImage;
		if (animatedImage != null) {
			setImageOriginal(animatedImage.getImageOriginalSize());
			setImageResized(animatedImage.getImageResized());
		}
		return this;
	}

	@Override
	public TileImage setImageResized(BufferedImage imageResized) {
		this.imageResized = imageResized;
		return this;
	}

	@Override
	public TileImage setImageName(String imageName) {
		this.imageName = imageName;
		return this;
	}

	//

	// TODO OTHER PUBLIC

	public TileImage setFrom(ImageAnimationHolder iah) {
		if (iah != null) {
			// set the original images
			this.ID = iah.getID();
			this.imageName = iah.getImageName();
			copyImagesFrom(iah);
		}
		return this;
	}

	public TileImage copyImagesFrom(ImageAnimationHolder iah) {
		if (iah != null) {
			// set the original images
			this.setImageOriginal(iah.getImageOriginal());
			this.setImageResized(iah.getImageResized());
			this.setAnimatedImage(iah.getAnimatedImage().cloneSharingSequence());
			// if (iah instanceof TileImage) {
			// TileImage ti;
			// ti = (TileImage) iah;
			// this.imageResized = ti.imageResized;
			// clone the resized, if any
			// this.animatedImageResized = ti.animatedImageResized == null ? null :
			// ti.animatedImageResized.clone();
			// }
		}
		return this;
	}

	/**
	 * Scale the original image (or animated image) to the given size (IN REAL PIXEL
	 * !!) and save the new image in a cache. That new image can be obtained through
	 * {@link #getImageResized()} or {@link #getAnimatedImageResized()}.
	 */
	public TileImage scaleImages(int width, int height) {
		boolean imageDone;
		AnimatedImage air;
		// AnimatedImage aio;
		BufferedImage bir, bio;
		air = getAnimatedImage();
		// aio = null;// getAnimatedImage();
		bir = getImageResized();
		bio = getImageOriginal();
		imageDone = false;
		if (air != null || bio != null) {
			if (air != null) {
				if (air == null || air.getWidth() != width || air.getHeight() != height) {
					// air = air.clone();
					setAnimatedImage(
							(air.getWidth() != width || air.getHeight() != height) ? air.resizeAllImages(width, height)
									: air);
				}
				setImageResized(air.getImageResized());
				imageDone = true;
			}
			if ((!imageDone) && bio != null) {
				if (air == null || bir.getWidth() != width || bir.getHeight() != height) {
					setImageResized((bio.getWidth() == width && bio.getHeight() == height)//
							? bio
							: CastingClass.castImage_ARGB_ToBufferedImage(//
									bio.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
				}
			}
		} // else no image avaiable
		return this;
	}

	@Override
	public boolean passTime(long millisPassed) {
		// AnimatedImage animatedImage;
		// animatedImage = getAnimatedI/mage();
		if (animatedImage != null) {
			if (animatedImage.passTime(millisPassed))
				setImageResized(animatedImage.getImageResized());
		}
		return false;
	}

	/*** See the WARNING section of this class : {@link TileImage}. */
	public boolean cloneAnimationFromThis() {
		if (animatedImage != null) {
			animatedImage = animatedImage.clone();
			return true;
		}
		return false;
	}

	@Override
	public TileImage clone() { return new TileImage(this); }
}