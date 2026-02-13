package videogamesOldVersion.common.mainTools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

import javax.swing.ImageIcon;

import common.utilities.Methods;
import tools.CastingClass;
import tools.FileUtilities;
import videogamesOldVersion.common.abstractCommon.MainController;

/**
 * Implementazione molto brutta: tiene una lista linkata di BufferedImage
 * (quindi questa classe NON e' serializzabile) e ciclicamente cambia frame ogni
 * N millisecondi (specificata dalla durata dell'frame.<br>
 * I frame vengono effettivamente cambiati solo su richiesta e
 */

// TODO TO BE TESTED
public class AnimatedImage implements Serializable {

	private static final long serialVersionUID = 666666666666L;

	public AnimatedImage() {
		head = null;
		size = 0;
		totalImageTime = 0;
		width = height = 0;
		reset();
	}

	public AnimatedImage(AnimatedImage a) { this(a, true); }

	public AnimatedImage(AnimatedImage ai, boolean makeNodeListIndipendent) {
		this();
		// doOnCloning(a, makeNodeListIndipendent);
		NodeAI n;
		// startTime = a.startTime;
		this.startTime = 0;
		// timePassed = a.timePassed;
		this.timePassed = 0;
		// System.currentTimeMillis();
		this.totalImageTime = 0;
		this.width = ai.width;
		this.height = ai.height;
		n = ai.head;
		if (n != null) {
			if (makeNodeListIndipendent) {
				do {
					addImage(n.clone());
					now = now.next;
				} while ((n = n.next) != ai.head);
				// setting now
				/*
				 * now = head; do { now = now.next; } while ((n = n.next) != a.head);
				 */
			} else {
				ai.imageFilename = this.imageFilename;
				ai.head = this.head;
				ai.now = this.now;
				ai.size = this.size;
				ai.totalImageTime = totalImageTime;
				ai.width = width;
				ai.height = height;
			}
		}
	}

	public AnimatedImage(String imageFilename) {
		this();
		this.imageFilename = imageFilename;
	}

	int size, width, height;
	transient long startTime, timePassed, totalImageTime;
	String imageFilename;
	transient NodeAI head, now;

	//

	int getSize() { return size; }

	long getTotalImageTime() { return totalImageTime; }

	public long getStartTime() { return startTime; }

	public long getTimePassed() { return timePassed; }

	public int getWidth() { return width; }

	public int getHeight() { return height; }

	public String getImageFilename() { return imageFilename; }

	//

	public AnimatedImage reset() {
		startTime = System.currentTimeMillis();
		timePassed = 0;
		now = head;
		return this;
	}

	/** Add the image on end of the (circular) queue. */
	public void addImage(BufferedImage bi, int millis) {
		if (bi == null)
			throw new NullPointerException(imageFilename + " Cannot add a null image to sequence");
		if (millis < 1)
			throw new IllegalArgumentException(imageFilename + " Invalid milliseconds: " + millis);
		addImage(new NodeAI(bi, millis));
	}

	/** See {@link AnimatedImage#addImage(BufferedImage, int)}. */
	protected void addImage(NodeAI n) {
		if (head == null) {
			size = 1;
			head = now = n;
			n.prev = n.next = n;
			totalImageTime = n.millis;
		} else {
			synchronized (this) {
				size++;
				n.next = head;
				if (head.prev != null) {
					head.prev.next = n;
					n.prev = head.prev;
				}
				head.prev = n;
			}
		}
		width = Math.max(width, n.bi.getWidth());
		height = Math.max(height, n.bi.getHeight());
		if ((totalImageTime += n.millis) < 0) { totalImageTime = Long.MAX_VALUE; }
	}

	protected long timePassed() {
		long prev;
		prev = startTime;
		return (startTime = System.currentTimeMillis()) - prev;
	}

	/*
	 * public BufferedImage getImage() { return getImage(timePassed()); } public
	 * BufferedImage getImage(long millisPassed) { BufferedImage b; if (head ==
	 * null) return null; b = now.bi; passTime(millisPassed); return b; }
	 */
	public BufferedImage getImageResized() {
		if (head == null)
			return null;
		return now.bi;
	}

	public BufferedImage getImageOriginalSize() {
		if (head == null)
			return null;
		return now.biOriginal;
	}

	public BufferedImage getImageAndPassTime(long millisPassed) {
		BufferedImage b;
		b = getImageResized();
		passTime(millisPassed);
		return b;
	}

	public BufferedImage getImageAfterTimePassed() { return getImageAfterTimePassed(timePassed()); }

	public BufferedImage getImageAfterTimePassed(long millisPassed) {
		passTime(millisPassed);
		return getImageResized();
	}

	/** Return true if the image-frame has been changed */
	public boolean passTime(long millisPassed) {
		long m;

		if (millisPassed > 0) {
			if (millisPassed > totalImageTime)
				millisPassed %= totalImageTime;
			if (millisPassed > 0) {
				// System.out.println("animated image: pass time " +
				// millisPassed + ", timePassed: " + timePassed);
				timePassed += millisPassed;
				if (timePassed >= (m = now.millis)) {

					/*
					 * System.out.println( "animated image: start cycle: size " + size +
					 * ", timePassed: " + timePassed + ", m: " + m);
					 */

					synchronized (this) {
						do {
							now = now.next;
							// System.out.println("timePassed:\t" + timePassed +
							// " - " + now);
						} while ((timePassed -= m) >= (m = now.millis) && timePassed > 0);
					}
					// System.out.println("animated image: End CYCLE " +
					// millisPassed);
					return true;
				}
				// System.out.println("animated image: passed time " +
				// millisPassed);
			}
		}
		return false;
	}

	/** Deep clone of its nodes */

	// protected void doOnCloning(AnimatedImage ai, boolean makeNodeListIndipendent)
	// {

	@Override
	public String toString() { return "AnimatedImage: " + imageFilename; }

	/**
	 * Calls {@link #clone(boolean)} giving <code>true</code> as parameter.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public AnimatedImage clone() { return clone(true); }

	/**
	 * If the boolean parameter is <code>true</code>, then a deep clone of this
	 * nodes is made, so every modification to the original instance won't affect
	 * the clone, and vice-versa..<br>
	 * Otherwise,
	 */
	public AnimatedImage clone(boolean makeNodeListIndipendent) { return new AnimatedImage(this); }

	/**
	 * Performs a shallow copy.<br>
	 * Like {@link #clone()}, but any modifications to this images sequence will be
	 * reflected to all other shallow copies and the original ones.<br>
	 * In particular, if the images will be resized to the original instance or one
	 * of the copies, every other instances (the original and the clones made in
	 * this way) will be affected.
	 */
	public AnimatedImage cloneSharingSequence() { return new AnimatedImage(this, false); }

	public AnimatedImage resizeAllImages(int width, int height) {
		NodeAI iter;
		if (width < 1 || height < 1 || head == null || (width == this.width && height == this.height))
			return this;
		iter = head;
		do {
			iter.bi = CastingClass.castImage_ARGB_ToBufferedImage(
					iter.biOriginal.getScaledInstance(width, height, Image.SCALE_SMOOTH));
		} while ((iter = iter.next) != head);
		return this;
	}

	// TODO newInstanceReadingImagesFromFolder
	/**
	 * Search a folder where all images inside it respect the following pattern:<br>
	 * "ordinalIndexOfImage millisecondsThisFrameLast imageNameIgnored".
	 */
	public static AnimatedImage newInstanceReadingImagesFromFolder(String pathStartLookingImage, String filename,
			Class<?> classToReferToReadBMP) {
		int i, len/* , indexExtension */;
		File folder, fileList[];
		String ss, nomeImmagine;
		BufferedImage b;
		Object o;
		AnimatedImage ai;

		ai = null;
		if (pathStartLookingImage == null) {
			System.out.println("pathStartLookingImage null");
			return null;
		}
		if (filename == null) { throw new IllegalArgumentException("The name must not be null"); }

		if ((folder = FileUtilities.searchFile(pathStartLookingImage, filename)) != null) {
			fileList = folder.listFiles();
			if ((len = fileList.length) > 0) {
				ss = folder.getPath() + File.separatorChar;
				Arrays.sort(fileList, MainController.FILE_COMPARATOR);
				ai = new AnimatedImage(filename);
				i = -1;
				while (++i < len) {

					o = FileUtilities.getBufferedImageScanningFoldersAndSubFolders(
							FileUtilities.removeExtension(nomeImmagine = fileList[i].getName()), ss,
							classToReferToReadBMP);
					if (o != null) {
						b = null;
						if (o instanceof ImageIcon) {
							b = CastingClass.castImage_ARGB_ToBufferedImage(((ImageIcon) o).getImage());
						} else if (o instanceof BufferedImage) { b = (BufferedImage) o; }
						if (b != null) {
							ai.addImage(b, extractMillisecondFromFormattedString(nomeImmagine)
							/**
							 * convertStringToInteger(nomeImmagine.substring(nomeImmagine.indexOf(' ') + 1,
							 * // <br>
							 * ((indexExtension = // indexFilenameExtension//<br>
							 * indexLastDigit(nomeImmagine) + 1) >= 0) ? indexExtension :
							 * nomeImmagine.length()))// <br>
							 */

							);
						}
					}
				}
			}
		}
		return ai;
	}

	protected static int extractMillisecondFromFormattedString(String nomeImmagine) {
		int i, j, len;
		char c;
		// j = i = nomeImmagine.indexOf(' ');

		len = nomeImmagine.length();
		/* jump the first number : it's just an ordinal */
		j = -1;
		// while (Character.isDigit(nomeImmagine.charAt(++j)))
		while (++j < len && (c = nomeImmagine.charAt(j)) >= '0' && c <= '9')
			;

		i = j;
		/* get the amount of milliseconds */
		// otteniamo l'indice dell'ultima cifra (senza altri caratteri nel
		// mezzo)
		while (++j < len && (c = nomeImmagine.charAt(j)) >= '0' && c <= '9')
			;
		return Methods.convertStringToInteger(nomeImmagine, i + 1, j);
	}

	//

	// TODO METHOD STUFF

	//

	// TODO CLASS

	protected static class NodeAI implements Serializable {
		private static final long serialVersionUID = 164040746121102L;
		int millis;
		transient BufferedImage bi, biOriginal;
		NodeAI next, prev;

		NodeAI() { super(); }

		NodeAI(BufferedImage bi, int millis) {
			if (bi == null) { throw new IllegalArgumentException("Given image is null"); }
			if (millis < 1) { throw new IllegalArgumentException("Incorrect time: " + millis); }
			this.bi = biOriginal = bi;
			this.millis = millis;
		}

		NodeAI(NodeAI n) {
			millis = n.millis;
			bi = n.bi;
			biOriginal = n.biOriginal;
		}

		@Override
		public NodeAI clone() { return new NodeAI(this); }
	}
}