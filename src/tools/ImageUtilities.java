package tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtilities {

	protected static final String[] extensionImageFile = { "png", "jpg", "jpeg", "bmp", "gif" };

	//

	public static BufferedImage imageToBufferedImage(Image im) {
		BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(im, 0, 0, null);
		bg.dispose();
		return bi;
	}

	public static BufferedImage readImageFromFile(File file) throws IOException {
		return ImageIO.read(file);
	}

	public static void writeBufferedImageToPNG(File file, BufferedImage bufferedImage) throws IOException {
		ImageIO.write(bufferedImage, "png", file);
	}

	public static void writeBufferedImageToJPG(File file, BufferedImage bufferedImage) throws IOException {
		ImageIO.write(bufferedImage, "jpg", file);
	}

	public static void writeImageToPNG(File file, Image image) throws IOException {
		writeBufferedImageToPNG(file, CastingClass.castImage_ARGB_ToBufferedImage(image));
	}

	public static void writeImageToJPG(File file, Image image) throws IOException {
		writeBufferedImageToJPG(file, CastingClass.castImage_ARGB_ToBufferedImage(image));
	}

	public static BufferedImage toCompatibleImage(BufferedImage image) {
		// obtain the current system graphical settings
		GraphicsConfiguration gfx_config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		/*
		 * if image is already compatible and optimized for current system settings,
		 * simply return it
		 */
		if (image.getColorModel().equals(gfx_config.getColorModel()))
			return image;

		// image is not optimized, so create a new image that is
		BufferedImage new_image = gfx_config.createCompatibleImage(image.getWidth(), image.getHeight(),
				image.getTransparency());

		// get the graphics context of the new image to draw the old image on
		Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		// actually draw the image and dispose of context no longer needed
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		// return the new optimized image
		return new_image;
	}

	//

	public static final boolean writeImages(BufferedImage[] a, String pathFolder, String name, String extensionNoDot,
			boolean counterAtEnd) {
		boolean ret, lastwritestatus, notFound;
		int i;
		String pathname, toLowCase;
		ret = a != null && a.length > 0 && pathFolder != null && name != null && extensionNoDot != null;
		if (ret) {
			if (!(pathFolder.charAt(pathFolder.length() - 1) != File.separatorChar)) {
				pathFolder = pathFolder + File.separatorChar;
			}
			if (extensionNoDot.length() == 0) {
				extensionNoDot = ImageUtilities.extensionImageFile[0];
			} else {
				notFound = true;
				toLowCase = extensionNoDot.toLowerCase();
				for (i = 0; (i < ImageUtilities.extensionImageFile.length && notFound); i++) {
					notFound = !(toLowCase.contains(ImageUtilities.extensionImageFile[i]));
				}
				if (notFound) {
					extensionNoDot = ImageUtilities.extensionImageFile[0];
				} else {
					if (toLowCase.startsWith(".")) {
						toLowCase = toLowCase.substring(1);
					}
				}
				extensionNoDot = toLowCase;
			}
			// estensione preparata
			pathname = pathFolder + name + ' ';
			i = -1;
			lastwritestatus = true;
			while (++i < a.length) {
				if (a[i] != null) {
					lastwritestatus = writeImage((counterAtEnd ? pathname + i : pathFolder + i + " " + name)//
							, a[i], extensionNoDot);
					// System.out.println("on index : " + i + "\tlast print
					// status : " + lastwritestatus);
					ret &= lastwritestatus;
				} else {
					System.err.println("a[" + i + "] is null on writing");
				}
			}
		} else {
			System.err.println("first check fail");
		}
		return ret;
	}

	public static boolean writeImage(BufferedImage bi, String path, String name, String extensionNoDot) {
		return writeImage(path + name, bi, extensionNoDot);
	}

	public static boolean writeImage(String pathAndName, BufferedImage bi) {
		return writeImage(pathAndName, bi, "png");
	}

	private static boolean writeImage(String pathAndName, BufferedImage bi, String extensionNoDot) {
		boolean successInWriting = false;
		try {
			if (bi != null) {
				successInWriting = ImageIO.write(bi, extensionNoDot, new File(pathAndName + '.' + extensionNoDot));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return successInWriting;
	}

	public static BufferedImage readImage(String path, String nameNoExtension) {
		BufferedImage ret = null;
		if (path != null && nameNoExtension != null && nameNoExtension.length() > 0) {
			File f;
			String pathCompleto, pathParziale;
			int i = -1;
			if (path.charAt(path.length() - 1) != File.separatorChar) {
				path = path + File.separatorChar;
			}
			pathParziale = path + nameNoExtension;
			while (ret == null && ++i < ImageUtilities.extensionImageFile.length) {
				pathCompleto = pathParziale + '.' + ImageUtilities.extensionImageFile[i];
				f = new File(pathCompleto);
				if (f.exists()) {
					switch (i) {
					case (0): {
						// png
						try {
							ret = ImageIO.read(f);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
					case (1): // jpg
					case (2): {
						// jpeg
						ret = castImageToBufferedImage(Toolkit.getDefaultToolkit().getImage(pathCompleto));
						break;
					}
					case (3): {
						// bmp
						try {
							ret = ImageIO.read(f);
						} catch (Exception e) {
							e.printStackTrace();
						}

						break;
					}
					case (4): {
						try {
							ret = castImageToBufferedImage((new ImageIcon(pathCompleto)).getImage());
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
					default: {
						// nothing
					}
					}
				}
			}
		}
		return ret;
	}

	public static BufferedImage castImageToBufferedImage(Image im) {
		BufferedImage bi = null;
		Graphics bg;
		if (im != null) {
			bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			bg = bi.createGraphics();
			bg.drawImage(im, 0, 0, null);
			bg.dispose();
		}
		return bi;
	}

//

	public static class ImageBeans {

	}
}