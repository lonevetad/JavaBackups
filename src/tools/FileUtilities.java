package tools;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public final class FileUtilities {

	private FileUtilities() {
	}

	public static String writeObject(Object o, String pathNameExt) {
		String error;
		ObjectOutputStream oos;
		error = pathNameExt == null ? "ERROR: on writeObject, path null" : null;
		if (o == null) {
			if (error == null)
				error = "ERROR: on writeObject, object null";
			else
				error += " and object null";
		}
		if (error == null) {
			try {
				oos = new ObjectOutputStream(new FileOutputStream(new File(pathNameExt)));
				oos.writeObject(o);
				oos.flush();
				oos.close();
			} catch (Exception e) {
				error = "ERROR: on writeObject, exception:\n\t" + e;
				e.printStackTrace();
			}
		}
		return error;
	}

	public static Object readObject(String pathNameExt) {
		Object o;
		ObjectInputStream ois;
		if (pathNameExt == null)
			throw new IllegalArgumentException("ERROR: on writeObject, path null");
		o = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(pathNameExt)));
			o = ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			/*
			 * try { PrintWriter pw; e.printStackTrace( pw = new
			 * PrintWriter((LoaderTests.getInstance().getPath()) +
			 * "Exception on read object.txt")); pw.flush(); pw.close(); } catch
			 * (FileNotFoundException e1) { e1.printStackTrace(); }
			 */
			System.out.println(
					"EXCEPTION CLASS: " + e.getClass().getName() + "\n get clause: " + e.getCause().getMessage());
		}
		return o;
	}

	public static File searchFile(String pathToStart, String filename) {
		File f;
		f = searchDirectoryContainingFilename(pathToStart, filename);
		if (f != null) {
			f = new File(f.getPath() + File.separatorChar + filename);
		}
		return f;
	}

	public static File searchDirectoryContainingFilename(String pathToStart, String filename) {
		boolean mustContinue;
		int i, l/* ,indexEstension */;
		File f, fl[], temp;
		String s, srec;
		if (filename == null || filename.trim().equals(""))
			return null;
		if (pathToStart == null || pathToStart.trim().equals(""))
			pathToStart = File.separator;

		/*
		 * //removeExtension// indexEstension= indexFilenameExtension(filename); if(
		 * indexEstension>0){ filename=filename.substring(0, indexEstension); }else
		 * if(indexEstension==0){ //the name return null; }
		 */
		// System.out.println("Start with ---" + pathToStart);
		f = null;
		if (!(f = new File(pathToStart + filename)).exists()) {
			if ((temp = new File(pathToStart)).exists() && temp.isDirectory()) {
				fl = temp.listFiles();
				if (fl != null && (l = fl.length) > 0) {
					i = 0;
					do {
						mustContinue = true;
						f = fl[i];
						// System.out.println(f.getName());
						if (!(s = f.getName()).equals(filename)) {
							/*
							 * se non l'ho trovato, allora continuo.. se f e' una cartella, ricorsione
							 */
							if (f.isDirectory()) {
								srec = (pathToStart.charAt(pathToStart.length() - 1) == File.separatorChar) ? //
										(pathToStart + s) : (pathToStart + File.separatorChar + s);
								mustContinue = (f = searchDirectoryContainingFilename(srec, filename)) == null;
							}
						} else {
							// System.out.println("FOUND : " + pathToStart);
							f = temp;
							mustContinue = false;
						}
					} while (mustContinue && ++i < l);
					if (i >= l)
						f = null;
				} else {
					f = null;// error
				}
			} else {
				f = null;// error
			}
			// fl=()
		} // else : trovato
		return f;
	}

	public static Object getBufferedImageScanningFoldersAndSubFolders(String nameOfFile, String subpath,
			Class<?> classToReferToReadBMP) {
		Object ret = null;
		/*
		 * se il file non esiste nel percorso specificato, ottenere l'array di file dal
		 * subpath tramite la classe FIle, poi ciclare e per ofni cartella far ripartire
		 * ricorsivamente questo metodo fino a quando non si trova l'immagine, ossia ret
		 * != null. Questo sarà il segnale che termninerà la ricorsione.
		 */
		ret = getBufferedImage(nameOfFile, subpath, classToReferToReadBMP);
		if (ret == null) {
			File[] files = new File(subpath).listFiles();
			int i = 0;
			String s, nameSorroundedWithSlashAndDot = File.separatorChar + nameOfFile + ".";
			if (files == null)
				return null;
			while ((ret == null) && i < files.length) {
				try {
					s = files[i].getCanonicalPath();
					// System.out.println(s);
					if (new File(s).isDirectory()) {
						// System.out.println("\t\t inside the if");
						// ripartiamo da quella cartella
						ret = getBufferedImageScanningFoldersAndSubFolders(nameOfFile, s, classToReferToReadBMP);
					} else {
						// altrimento ho un file non-cartella .. provo a
						// leggerlo, magari l'ho trovato
						ret = getBufferedImage(nameOfFile, subpath, classToReferToReadBMP);
						if (ret == null && s.contains(nameSorroundedWithSlashAndDot)) {
							try {
								ret = ImageIO.read(new File(s));
							} catch (Exception e) {
								// e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				i++;
			}
		}
		return ret;
	}

	public static Image getImage_Order_PngJpg(String path) {
		Image ret = null;
		if (path != null) {
			File f = new File(path);
			if (f.exists()) {
				try {
					if (path.endsWith(".png")) {
						ret = ImageIO.read(f);
					} else if (path.endsWith(".jpg")) {
						ret = Toolkit.getDefaultToolkit().getImage(path);
						if (ret.getHeight(null) < 1 || ret.getWidth(null) < 1) {
							ret = ImageIO.read(f);
						}
					} else {
						try {
							ret = ImageIO.read(f);
						} catch (IOException ioex) {
							ioex.printStackTrace();

							f = new File(path + ".png");
							if (f.exists()) {
								ret = ImageIO.read(f);
							} else {
								f = new File(path + ".jpg");
								if (f.exists()) {
									ret = Toolkit.getDefaultToolkit().getImage(path);
									if (ret.getHeight(null) < 1 || ret.getWidth(null) < 1) {
										ret = ImageIO.read(f);
									}
								} else {
									ret = ImageIO.read(f);
								}
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return ret;
	}

	public static BufferedImage getBufferedImage(String path) {
		BufferedImage ret = null;
		if (path != null) {
			File f = new File(path);
			if (f.exists()) {
				try {
					if (path.endsWith(".png")) {
						ret = ImageIO.read(f);
					} else if (path.endsWith(".jpg")) {
						try {
							ret = CastingClass.castImage_ToBufferedImage(Toolkit.getDefaultToolkit().getImage(path),
									true);
						} catch (IllegalArgumentException iae) {
							ret = ImageIO.read(f);
						}
						if (ret.getHeight(null) < 1 || ret.getWidth(null) < 1) {
							ret = ImageIO.read(f);
						}
					} else {
						try {
							ret = ImageIO.read(f);
						} catch (IOException ioex) {
							ioex.printStackTrace();

							f = new File(path + ".png");
							if (f.exists()) {
								ret = ImageIO.read(f);
							} else {
								f = new File(path + ".jpg");
								if (f.exists()) {
									try {
										ret = CastingClass.castImage_ToBufferedImage(
												Toolkit.getDefaultToolkit().getImage(path), true);
									} catch (IllegalArgumentException iae) {
										ret = ImageIO.read(f);
									}
									if (ret.getHeight(null) < 1 || ret.getWidth(null) < 1) {
										ret = ImageIO.read(f);
									}
								} else {
									ret = ImageIO.read(f);
								}
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				if (!path.endsWith(".jpg")) {
					ret = getBufferedImage(path + ".png");
				}
				if ((ret == null) && (!path.endsWith(".jpg"))) {
					ret = getBufferedImage(path + ".jpg");
				}
			}
		}
		return ret;
	}

	public static ImageIcon getImageIcon_Order_GifPngJpg(String pathNoExtension) {
		return getImageIcon_Order_GifPngJpgBmp(pathNoExtension, -1, -1); // this
		// means
		// the
		// original
		// size
	}

	public static ImageIcon getImageIcon_Order_GifPngJpgBmp(String pathNoExtension) {
		// the original size
		return getImageIcon_Order_GifPngJpgBmp(pathNoExtension, -1, -1);
	}

	public static ImageIcon getImageIcon_Order_GifPngJpgBmp(String pathNoExtension, int width, int height) {
		ImageIcon ret = null;
		if (pathNoExtension != null) {
			String[] extensions = { ".gif", ".png", ".jpg", ".bmp" };
			String s = "";
			for (int i = 0; ((i < extensions.length) && (ret == null)); i++) {
				s = pathNoExtension + extensions[i];
				File f = new File(s);
				if (f.exists()) {
					switch (i) {
					case (0): {
						ret = new ImageIcon(s); // i don't know how to both
						// scale a gif and keep the
						// animation
						break;
					}
					case (1): {
						ret = subPart_GetImageIcon_Order_GifPngJpg(s, width, height);
						break;
					}
					case (2): {
						ret = subPart_GetImageIcon_Order_GifPngJpg(s, width, height);
						break;
					}
					case (3): { // keepeng the same soluzion now, if i will
						// find
						// a better solution i will have only a
						// little
						// bit to do to apply the "discover"
						ret = subPart_GetImageIcon_Order_GifPngJpg(s, width, height);
						break;
					}
					default: { // just to be shure
						ret = subPart_GetImageIcon_Order_GifPngJpg(s, width, height);
						break;
					}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * This method returns a <i>ImageIcon</i> for <i>gif</i> files,
	 * <i>BufferedImage</i> for <i>png</i> and <i>jpg</i> files, undefinied object
	 * (maybe BuffederImage) for <i>bmp</i> files.
	 */
	public static Object readImage_Order_GifPngJpgBmp(String pathNameNoExtension, Class<?> classToReferToReadBMP) {
		Object ret = null;
		if (pathNameNoExtension != null) {
			String s = "", dot = "";
			if (!pathNameNoExtension.endsWith(".")) {
				dot = ".";
			}
			String[] extensions = { dot + "gif", dot + "png", dot + "jpg", dot + "bmp" };
			int i = 0;
			while ((i < extensions.length) && (ret == null)) {
				s = pathNameNoExtension + extensions[i];
				File f = new File(s);
				if (f.exists()) {
					if (i == 0) {
						// non so come altro fare ...
						/*
						 * ret = CastingClass.castImage_ToBufferedImage( (new ImageIcon(s)).getImage(),
						 * true);
						 */
						ret = new ImageIcon(s);
						/*
						 * ImageIcon icon;Image image = icon.getImage();
						 *
						 * // Create empty BufferedImage, sized to Image ret = new
						 * BufferedImage(image.getWidth(null), image.getHeight(null),
						 * BufferedImage.TYPE_INT_ARGB);
						 *
						 * // Draw Image into BufferedImage Graphics g = ret.getGraphics();
						 * g.drawImage(image, 0, 0, null);
						 */
					} else if (i == 1) {
						try {
							ret = ImageIO.read(f);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (i == 2) {
						Image im = Toolkit.getDefaultToolkit().getImage(s);
						if (im != null && (im.getWidth(null) > 0 && im.getHeight(null) > 0)) {
							ret = CastingClass.castImage_ToBufferedImage(im, true);
						} else {
							try {
								ret = ImageIO.read(f);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else if (i == 3) {
						try {
							if (classToReferToReadBMP != null) {
								ret = ImageIO.read(classToReferToReadBMP.getResource(s));
							} else {
								ret = ImageIO.read(f);
							}
						} catch (Exception e) {
							e.printStackTrace();
							try {
								ret = ImageIO.read(f);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					} else {
						try {
							ret = ImageIO.read(f);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				i++;
			}
		}
		return ret;
	}

	private static ImageIcon subPart_GetImageIcon_Order_GifPngJpg(String pathAndExtension, int width, int height) {
		ImageIcon ret = null;
		Image im = getImage_Order_PngJpg(pathAndExtension);
		if (im != null) {
			if (width > 0 && height > 0) {
				ret = new ImageIcon(im.getScaledInstance(width, height, Image.SCALE_SMOOTH));
			} else {
				ret = new ImageIcon(im);
			}
		}
		return ret;
	}

	/**
	 * See {@link readImage_Order_GifPngJpgBmp}for further informations.
	 */
	public static Object getBufferedImage(String filenameNoExtension, String subpath, Class<?> classToReferToReadBMP) {
		Object ret = null;
		try {
			if (filenameNoExtension == null) {
				filenameNoExtension = "";
			}
			if (subpath == null) {
				subpath = "";
			}
			ret = readImage_Order_GifPngJpgBmp(subpath + filenameNoExtension, classToReferToReadBMP);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	//

	public static int indexFilenameExtension(String fn) {
		int i;
		if (fn == null || fn.trim().equals(""))
			return -1;
		i = fn.length();
		while (--i >= 0 && fn.charAt(i) != '.')
			;
		return i;
	}

	public static boolean hasExtension(String fn) {
		return indexFilenameExtension(fn) >= 0;
	}

	public static String removeExtension(String s) {
		int i;
		i = indexFilenameExtension(s);
		if (i < 0)
			return s;
		else if (i == 0)
			return "";
		return s.substring(0, i);
	}

	public static String getPath(Class<?> clas) {
		int i;
		String path;
		path = null;
		try {
			path = new File(clas.getCanonicalName()).getCanonicalPath();
			i = path.lastIndexOf('.');
			path = (i > 0) ? (path.substring(0, i) + File.separatorChar) : "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getNotExistingFilename(String originalPathname, String extension) {
		int i;
		String r;

		r = originalPathname;
		if (originalPathname != null && extension != null) {
			r = originalPathname + extension;

			if ((new File(r)).exists()) {
				i = -1;
				originalPathname = originalPathname + " ";
				while ((new File(r = originalPathname + ++i + extension)).exists())
					;
			}
		}
		return r;
	}
}