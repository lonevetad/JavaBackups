package tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import tools.minorTools.ManipulatingArrays;

public class CastingClass {

	private CastingClass() {
	} // do not instantiate it

	public static BufferedImage[] getBufferedImageFramesFromGIF(String path, String name, boolean shouldWriteImage) {
		BufferedImage[] ret = null;
		if (path == null) {
			path = "";
		}
		if (name != null) {
			try {
				File gifFile = new File(path + name + ".gif");
				if (gifFile.exists() && gifFile.canRead()) {
					ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();
					ImageInputStream in = ImageIO.createImageInputStream(gifFile);
					reader.setInput(in);
					BufferedImage image = null;
					int count = reader.getNumImages(true);
					ret = new BufferedImage[count];
					for (int i = 0; i < count; i++) {
						ret[i] = image = reader.read(i);
						if (shouldWriteImage) {
							ImageIO.write(image, "PNG",
									new File(path + File.pathSeparatorChar + "output " + i + ".png"));
						}
					}
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return ret;
	}

	public static BufferedImage castImageIconToBufferedImage(ImageIcon imaico1, boolean isARGB) {
		return castImage_ToBufferedImage(castIconToImage(imaico1), isARGB);
	}

	public static Image iconToImage(Icon icon) {
		Image ret = null;

		if (icon instanceof ImageIcon) {
			ret = ((ImageIcon) icon).getImage();
		} else {
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gd = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gd.getDefaultConfiguration();
			BufferedImage image = gc.createCompatibleImage(w, h);
			Graphics2D g = image.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			ret = image;
		}
		return ret;
	}

	/*
	 * public ArrayList<BufferedImage> getFrames(File gif) throws IOException {
	 * ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>(1);
	 * ImageReader ir = new ImageReader(new ImageReaderSpi());
	 * ir.setInput(ImageIO.createImageInputStream(gif)); for (int i = 0; i <
	 * ir.getNumImages(true); i++) { //
	 * frames.add(ir.getRawImageType(i).createBufferedImage(ir.getWidth(i), //
	 * ir.getHeight(i))); frames.add(ir.read(i)); } return frames; }
	 */

	public static int castAssemblyRGBandAlphaToARGB(int rgb, byte alpha) {
		return ((rgb & MathUtilities.negateInt(255 << 24)) | (alpha << 24));
	}

	public static int castARGBbytesToARGBInt(byte alpha, byte red, byte green, byte blue) {
		return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 0) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 16);
	}

	public static boolean isTrasparentARGB(int n) { // 0xff000000 = -16777216 =
													// 1111 1111 0000 0000 0000
													// 0000 0000 0000
		return (((n & 0xff000000) >> 24) == 0x00);
	}

	public static Polygon castPointListToPolygon(List<Point> list) {
		Polygon ret = null;
		if (list != null) {
			int i = 0, counterNotNull = 0, validIndex = 0;
			while (i < list.size()) {
				if (list.get(i) != null) {
					counterNotNull++;
				}
				i++;
			}
			int[] xx = new int[counterNotNull], yy = new int[counterNotNull];
			for (i = 0; i < list.size(); i++) {
				if (list.get(i) != null) {
					xx[validIndex] = list.get(i).x;
					yy[validIndex] = list.get(i).y;
					validIndex++;
				}
			}
			ret = new Polygon(xx, yy, counterNotNull);
		}
		return ret;
	}

	public static Image castIconToImage(Icon icon) {
		Image im = null;
		if (icon != null) {
			if (icon instanceof ImageIcon) {
				im = ((ImageIcon) icon).getImage();
			} else {
				int w = icon.getIconWidth();
				int h = icon.getIconHeight();
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice gd = ge.getDefaultScreenDevice();
				GraphicsConfiguration gc = gd.getDefaultConfiguration();
				BufferedImage image = gc.createCompatibleImage(w, h);
				Graphics2D g = image.createGraphics();
				icon.paintIcon(null, g, 0, 0);
				g.dispose();
				im = image;
			}
		}
		return im;
	}

	public static Polygon castBufferedImageToPolygon_BuondingNotTransparentPixel(BufferedImage bim) {
		return castBufferedImageToPolygon_BuondingNotTransparentPixel(bim, 4);
	}

	public static Polygon castBufferedImageToPolygon_BuondingNotTransparentPixel(BufferedImage bim, int precision) {
		Polygon ret = null;
		if (precision < 0) {
			precision = -precision;
		}
		if (bim != null && precision > 0) {
			int h = bim.getHeight(), w = bim.getWidth();
			if (w > 0 && h > 0) {
				ArrayList<Point> p = new ArrayList<Point>(w * 2 + h * 2);
				int de = -1 - precision, x = 0, y = 0, prevx = de, prevy = de, lastx = -1 - precision,
						lasty = -1 - precision, firstx = de, firsty = de;
				boolean inRangeY, inRangeX, notTooNear, isTrasparent, atLeastOneItemAdded;
				Point mp;
				try {
					inRangeY = inRangeX = notTooNear = isTrasparent = true;
					atLeastOneItemAdded = false;
					// direzione orizzontale
					x = 0;
					for (; x < w; x++) {
						y = 0;
						do {// ricerca
							isTrasparent = isTrasparentARGB(bim.getRGB(x, y));
							y++;
							inRangeY = y < h;
						} while (inRangeY && isTrasparent);
						notTooNear = Math.abs(y - prevy) >= precision;
						if ((!isTrasparent) && inRangeY) { // se ho trovato un
															// non trasparente
							if (atLeastOneItemAdded) {
								if (notTooNear) {
									mp = new Point(x - 1, prevy);
									/*
									 * punto precedente allo "stacco di distanza "
									 */
									if (!mp.equals(p.get(p.size() - 1))) {
										/*
										 * se l'ultimo � unguale a quello che devo aggiungere, ossia lo stacco, non lo
										 * aggiungo
										 */
										p.add(mp);
									}
									p.add(mp = new Point(x, y));
									prevy = y;
								}
							} else {
								firstx = x;
								firsty = y;
								p.add(mp = new Point(x, y));
								prevy = y;
								atLeastOneItemAdded = true;
							}
							lastx = x; // indice che tiene conto dell'ultima x
										// trovata, ossia l'ultimo punto prima
										// di sole colonne trasparenti
						}
					} // fine for
					if (lastx == w) {
						lastx--;
					}
					if (p.size() > 0 && !(p.get(p.size() - 1).equals(mp = new Point(lastx, prevy)))) {
						p.add(mp);
					}
					y = prevy; // si riprende dall'ultima y trovata
					prevx = lastx;
					for (; y < h; y++) {
						x = lastx;
						do {
							isTrasparent = isTrasparentARGB(bim.getRGB(x, y));
							x--;
							inRangeX = x >= 0;
						} while (inRangeX && isTrasparent);
						notTooNear = Math.abs(x - prevx) >= precision;
						if ((!isTrasparent) && inRangeX) { // se ho trovato un
															// non trasparente
							if (atLeastOneItemAdded) {
								if (notTooNear) {
									mp = new Point(prevx, y - 1);
									if (!mp.equals(p.get(p.size() - 1))) {
										p.add(mp);
									}
									p.add(mp = new Point(x, y));
									prevx = x;
								}
							} else {
								mp = new Point(x, y);
								if (!mp.equals(p.get(p.size() - 1))) {
									p.add(mp);
								}
								prevx = x;
								atLeastOneItemAdded = true;
							}
							lasty = y;
						}
					} // fine for
					if (lasty == h) {
						lasty--;
					}
					if (p.size() > 0 && (!p.get(p.size() - 1).equals(mp = new Point(prevx, lasty)))) {
						p.add(mp);
					}
					prevy = lasty;
					for (x = prevx - 1; x >= 0; x--) {
						y = lasty;
						do {
							isTrasparent = isTrasparentARGB(bim.getRGB(x, y));
							y--;
							inRangeY = y >= 0;
						} while (inRangeY && isTrasparent);
						notTooNear = Math.abs(y - prevy) >= precision;
						if ((!isTrasparent) && inRangeY) { // se ho trovato un
															// non trasparente
							if (atLeastOneItemAdded) {
								if (notTooNear) {
									mp = new Point(x + 1, prevy);
									if (!mp.equals(p.get(p.size() - 1))) {
										p.add(mp);
									}
									p.add(mp = new Point(x, y));
									prevy = y;
								}
							} else {
								mp = new Point(x, y);
								if (!mp.equals(p.get(p.size() - 1))) {
									p.add(mp);
								}
								prevy = y;
								atLeastOneItemAdded = true;
							}
							lastx = x;
						}
					} // fine for
					if (lastx == w) {
						lastx--;
					}
					if (p.size() > 0 && (!p.get(p.size() - 1).equals(mp = new Point(lastx, prevy)))) {
						p.add(mp);
					}
					prevx = lastx;
					y = prevy;
					for (; ((y >= 0) && (firstx != x) && (firsty != y)); y--) {
						x = lastx;
						do {
							isTrasparent = isTrasparentARGB(bim.getRGB(x, y));
							x++;
							inRangeX = x < w;
						} while (inRangeX && isTrasparent);
						notTooNear = Math.abs(x - prevx) >= precision;
						if ((!isTrasparent) && inRangeX) { // se ho trovato un
															// non trasparente
							if (atLeastOneItemAdded) {
								if (notTooNear) {
									mp = new Point(prevx, y + 1);
									if (!mp.equals(p.get(p.size() - 1))) {
										p.add(mp);
									}
									p.add(mp = new Point(x, y));
									prevx = x;
								}
							} else {
								mp = new Point(x, y);
								if (!mp.equals(p.get(p.size() - 1))) {
									p.add(mp);
								}
								prevx = x;
								atLeastOneItemAdded = true;
							}
							lasty = y;
						}
					} // fine for
					if (lasty == h) {
						lasty--;
					}
					mp = new Point(prevx - 1, lasty + 1);
					/*
					 * i -1 ed i +1 sono casi eccezionali, per ovviare a dei bug
					 */
					if (p.size() > 0 && (!p.get(p.size() - 1).equals(mp)) && !mp.equals(p.get(0))) {
						p.add(mp);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				int i = 0, s = p.size();
				if (s > 0) {
					int[] xx = new int[s], yy = new int[s];
					while (i < s) {
						xx[i] = p.get(i).x;
						yy[i] = p.get(i).y;
						i++;
					}
					ret = new Polygon(xx, yy, s);
				}
			}
		}
		return ret;
	}

	public static BufferedImage castBufferedImage_To_ARGB(BufferedImage bi) {
		return getCopyBufferedImage(bi, BufferedImage.TYPE_INT_ARGB);
	}

	public static BufferedImage castBufferedImage_To_ARGBPRE(BufferedImage bi) {
		return getCopyBufferedImage(bi, BufferedImage.TYPE_INT_ARGB_PRE);
	}

	public static BufferedImage castBufferedImage_To_RGB(BufferedImage bi) {
		return getCopyBufferedImage(bi, BufferedImage.TYPE_INT_RGB);
	}

	public static BufferedImage getCopyBufferedImage(BufferedImage bi) {
		return getCopyBufferedImage(bi, bi.getType());
	}

	public static BufferedImage getCopyBufferedImage(BufferedImage bi, int type) {
		BufferedImage bif = null;
		if (bi != null) {
			bif = new BufferedImage(bi.getWidth(), bi.getHeight(), type);
			for (int r = 0; r < bi.getHeight(); r++) {
				for (int c = 0; c < bi.getWidth(); c++) {
					bif.setRGB(c, r, bi.getRGB(c, r));
				}
			}
		}
		return bi;
	}

	public static BufferedImage castMatrixBufferedImage_RGB(int[][] matrix) {
		return castMatrixBufferedImage(matrix, BufferedImage.TYPE_INT_RGB);
	}

	public static BufferedImage castMatrixBufferedImage_ARGB_PRE(int[][] matrix) {
		return castMatrixBufferedImage(matrix, BufferedImage.TYPE_INT_ARGB_PRE);
	}

	public static BufferedImage castMatrixBufferedImage(int[][] matrix) {
		return castMatrixBufferedImage(matrix, BufferedImage.TYPE_INT_RGB /* TYPE_CUSTOM */);
	}

	public static BufferedImage castMatrixBufferedImage_ARGB(int[][] matrix) {
		return castMatrixBufferedImage(matrix, BufferedImage.TYPE_INT_ARGB);
	}

	public static BufferedImage castMatrixBufferedImage(int[][] matrix, boolean trueARGB_FalseRGB) {
		BufferedImage ret = (trueARGB_FalseRGB) ? castMatrixBufferedImage(matrix, BufferedImage.TYPE_INT_ARGB)
				: castMatrixBufferedImage(matrix, BufferedImage.TYPE_INT_RGB);
		return ret;
	}

	public static BufferedImage castMatrixBufferedImage(int[][] matrix, int type) {
		BufferedImage bufferedImage = null;
		if (matrix != null) {
			if (matrix[0] != null) {
				bufferedImage = new BufferedImage(matrix[0].length, matrix.length, type);
				for (int r = 0; r < matrix.length; r++) {
					if (matrix[r] != null) {
						for (int c = 0; c < matrix[r].length; c++) {
							bufferedImage.setRGB(c, r, matrix[r][c]);
						}
					}
				}
			}
		}
		return bufferedImage;
	}

	public static int[][] castBufferedImageMatrix(BufferedImage bi) {
		int[][] ret = null;
		if (bi != null) {
			int righe = bi.getHeight(), colonne = bi.getWidth();
			ret = new int[righe][];
			int[] riga;
			for (int r = 0; r < righe; r++) {
				riga = ret[r] = new int[colonne];
				for (int c = 0; c < colonne; c++) {
					riga[c] = bi.getRGB(c, r); // da notare l'inversione di
												// indici
				}
			}
		} else {
			System.err.println("bi is null in casting it to a matrix");
		}
		return ret;
	}

	public static BufferedImage castImage_ToBufferedImage(Image im, boolean isARGB) {
		if (im instanceof BufferedImage)
			return (BufferedImage) im;
		return isARGB ? CastingClass.castImage_ARGB_ToBufferedImage(im)
				: CastingClass.castImage_RGB_ToBufferedImage(im);
	}

	public static BufferedImage castImage_RGB_ToBufferedImage(Image im) {
		if (im instanceof BufferedImage)
			return (BufferedImage) im;
		return castImageToBufferedImage(im, im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
	}

	public static BufferedImage castImageToBufferedImage(Image im, int w, int h, int typeImage) {
		BufferedImage bi;
		Graphics bg;
		if (im instanceof BufferedImage)
			return (BufferedImage) im;
		bi = new BufferedImage(w, h, typeImage);
		if (im != null) {
			bg = bi.getGraphics();
			bg.drawImage(im, 0, 0, null);
			bg.dispose();
		}
		return bi;
	}

	public static BufferedImage castImage_ARGB_ToBufferedImage(Image im) {
		if (im instanceof BufferedImage)
			return (BufferedImage) im;
		return castImageToBufferedImage(im, im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	}

	//

	//

	public static final BufferedImage[] dissasembleImage(BufferedImage bi, int xstart, int ystart, int w, int h,
			int xjump, int yjump) {
		BufferedImage[] ret = null;
		if (bi != null && w > 0 && h > 0 && xstart >= 0 && ystart >= 0 && xjump >= 0 && yjump >= 0) {
			int wbi = bi.getWidth(), hbi = bi.getHeight();
			if (wbi >= (xstart + w) && hbi >= (ystart + h)) {
				int c, r, i, t = bi.getType(), //
						/* i due indici che scorrono "temp" */
						cc, rr, //
						/*
						 * i punti di partenza da cui far ripartire gli scorrimenti di "bi" originale,
						 * nei due cicli più interni, per ottimizzare gli accessi a "bi"
						 */
						ct, rt;
				BufferedImage temp;
				ArrayList<BufferedImage> buff = new ArrayList<BufferedImage>(
						(int) (((((double) wbi - xstart) / (w + xjump)) * (((double) hbi - ystart) / (h + yjump))))
								+ 1);
				i = 0;
				r = ystart;
				while (r < hbi) {

					c = xstart;
					while (c < wbi) {

						temp = new BufferedImage(w, h, t);
						rt = r;

						// ciclo di settaggio di temp
						for (rr = 0; (rr < h && rt < hbi); rr++) {
							ct = c;
							for (cc = 0; (cc < w && ct < wbi); cc++) {
								temp.setRGB(cc, rr, bi.getRGB(ct++, rt));
							}
							rt++;
						}

						buff.add(temp);
						// increment c
						c += xjump + w + 1;
					}

					// increment r
					r += yjump + h + 1;

					// ret[i++] = temp;
				}
				int s = buff.size();
				ret = new BufferedImage[s];
				System.out.println("array's length on creation : " + ret.length);
				i = -1;
				while (++i < s) {
					ret[i] = buff.get(i);
				}
			}
		}
		return ret;
	}

	public static class CCBeans {

		// fra126-4.qwe58-7d
		public static double castStringDouble(String stringa_da_controllare) {
			double final_number = 0.0;
			boolean atLeastOneDigitFound = false;
			boolean prima_virgola_trovata = false;
			int lunghezza_stringa = stringa_da_controllare.length();
			int segno = 1;
			int x = 0;
			char numeriSimboli[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+', '.', ',' };
			int k = 0;
			String numeri_prima_del_punto = "";
			String numeri_dopo_il_punto = "";
			boolean somethingFound = false;
			while (k < lunghezza_stringa) {
				somethingFound = false;
				for (int g = 0; ((g < numeriSimboli.length) & (!somethingFound)); g++) {
					if (stringa_da_controllare.charAt(k) == numeriSimboli[g]) {
						if (g < 10) {// digits
							atLeastOneDigitFound = true;
							if (prima_virgola_trovata) {
								numeri_dopo_il_punto = stringa_da_controllare.charAt(k) + numeri_dopo_il_punto;
							}
							// if ( prima_virgola_trovata == false)
							else {
								numeri_prima_del_punto = numeri_prima_del_punto + stringa_da_controllare.charAt(k);
							}
							somethingFound = true;
						} else if ((g == 10) || (g == 11)) {// segno
							if (!atLeastOneDigitFound) {
								if (g == 10) {
									segno = -1;
								}
								somethingFound = true;
							} // se la prima cosa "utile" che trovo � un segno,
								// allora lo conto ..
								// se invece ho gi� trovato almeno una cifra o un
								// punto all'inizio (il quale far� poi presupporre
								// di anteporre uno 0), lo ignoro
						} else if ((g == 12) || (g == 13)) {
							prima_virgola_trovata = true;
							// somethingFound = true; // forse si potrebbe anche
							// omettere
						} // virgola
					}
				}
				k++;
			}
			while (x < numeri_prima_del_punto.length()) {
				// calcolo parte intera, prima della virgola
				somethingFound = false;
				for (int y = 0; ((!somethingFound) && (y < numeriSimboli.length)); y++) {
					if (numeri_prima_del_punto.charAt(x) == numeriSimboli[y]) {
						final_number += (y * Math.pow(10, (numeri_prima_del_punto.length() - (x + 1))));
						somethingFound = true;
					}
				}
				x++;
			}
			x = 0;
			if (prima_virgola_trovata) {
				while (x < numeri_dopo_il_punto.length()) {
					// calcolo parte decimale, dopo della virgola

					somethingFound = false;
					for (int y = 0; ((!somethingFound) && (y < numeriSimboli.length)); y++) {
						if (numeri_dopo_il_punto.charAt(x) == numeriSimboli[y]) {
							final_number += (y / (numeri_dopo_il_punto.length() - x));
							somethingFound = true;
						}
					}
					x++;
				}
			}
			return final_number * segno;
		}

		public static String castDoubleString_IgnoreUselessZero(double num) {
			String ret = castDoubleString(num, 30);
			while (ret.charAt(ret.length() - 1) == '0') {
				ret = ret.substring(0, ret.length() - 1);
			}
			if ((ret.charAt(ret.length() - 1) == '.') || (ret.charAt(ret.length() - 1) == ',')) {
				ret = ret.substring(0, ret.length() - 1);
			}
			return ret;
		}

		public static String castDoubleString(double num) {
			return castDoubleString(num, 0);
		}

		public static String castDoubleString(double num, int precisione_decimale) {
			String ret = "";
			if ((precisione_decimale < 0) | (precisione_decimale > 30)) {
				precisione_decimale = 10;
			}
			if (num != 0.0) {
				int contatoreCifreDecimali = 0;
				int parte_intera = (int) num;
				double parteDecimale = 0; // num - (double) parte_intera;
				int tempParteIntera = 0;
				String stringIntera = "";
				String stringDecimale = "";
				// char numeri[] = {'0','1','2','3','4','5','6','7','8','9' };
				char segno = ' ';
				if (parte_intera < 0) {
					segno = '-';
					parte_intera = -parte_intera;
				}
				parteDecimale = ((parte_intera < 0) ? -num : num) - parte_intera;
				if (parteDecimale < 0) {
					parteDecimale = -parteDecimale;
				}
				while ((parte_intera >= 1)) {
					tempParteIntera = parte_intera % 10;
					parte_intera = parte_intera / 10;
					/*
					 * casting a int automatico ... ( parte_intera - temp) � inutile perch� la cifra
					 * decimale viene troncata dal casting
					 */

					stringIntera = tempParteIntera + stringIntera;
					/*
					 * il casting da numero intero a stringa lo fa gi� o il compilatore o la JVM
					 * (java virtual machine equivalente a ... stringIntera = numeri[
					 * tempParteIntera ] + stringIntera;
					 */
				}
				tempParteIntera = (int) (Math.pow(10, (stringIntera.length())));
				if (precisione_decimale > 0) {
					while (contatoreCifreDecimali < precisione_decimale) {
						parteDecimale = parteDecimale * 10.0;
						stringDecimale = stringDecimale + (((int) parteDecimale) % 10);
						/*
						 * stesso discorso del casting da int
						 */contatoreCifreDecimali++;
					}
				}
				ret = ((stringDecimale.length() == 0) ? (segno + stringIntera)
						: (segno + stringIntera + "." + stringDecimale)).trim();
				for (int x = ret.length(); x < precisione_decimale; x++) {
					ret = ret + '0';
				}
			} else {
				ret = "0";
				if (precisione_decimale > 0) {
					ret = ret + '.';
					for (int x = 0; x < precisione_decimale; x++) {
						ret = ret + '0';
					}
				}
			}
			return ret;
		} // fine castDoubleString

		public static Polygon castEllipse2Polygon(Ellipse2D e, int numberOfPoints) {
			return castEllipse2Polygon(e, numberOfPoints, 0.0);
		}

		public static Polygon castEllipse2Polygon(Ellipse2D e, double lengthOfSegment, double degreesRotation) {
			Polygon ret = null;
			if ((lengthOfSegment > 0.0) && (e != null)) {
				double a = (e.getWidth() / 2.0), b = (e.getHeight() / 2.0);
				// double area = Math.PI * a * b;
				double circ = Math.PI * (3.0 * (a + b) - Math.sqrt((3.0 * a + b) * (a + 3.0 * b)));
				double np = (circ / lengthOfSegment) + 1.0;
				ret = castEllipse2Polygon(e, (int) np, degreesRotation);
			}
			return ret;
		}

		public static Polygon castEllipse2Polygon(Ellipse2D e, int numberOfPoints, double degreesRotation) {
			Polygon ret = null;
			if ((numberOfPoints > 3) && (e != null)) {
				double x = e.getCenterX(), y = e.getCenterY();
				double a = (e.getWidth() / 2.0), b = (e.getHeight() / 2.0);
				// double area = Math.PI * a * b, circ = Math.PI * ( 3.0*(a+b) -
				// Math.sqrt( (3.0*a + b) * (a + 3.0 * b) )) ;
				double np = numberOfPoints;
				int xx[] = new int[numberOfPoints];
				int yy[] = new int[numberOfPoints];
				Point p; // then in start the rotation
				int ii, xxx = 0, yyy = 0;
				AffineTransform at = new AffineTransform();
				at.rotate(Math.toRadians(degreesRotation), x, y);
				for (double i = 0.0; i < np; i += 1.0) {
					ii = (int) i;
					xxx = (int) (x + a * Math.cos(Math.toRadians(((360.0 / np) * i))));
					yyy = (int) (y + b * Math.sin(Math.toRadians(((360.0 / np) * i))));
					p = new Point(xxx, yyy);
					at.transform(p, p);
					xx[ii] = p.x;
					yy[ii] = p.y;
				}
				ret = new Polygon(xx, yy, (int) np);
			}
			return ret;
		}

		public static Polygon castEllipse2Polygon(Ellipse2D e) {
			return castEllipse2Polygon(e, 5.0); // previouse : 72 dots
		}

		public static Polygon castEllipse2Polygon(Ellipse2D e, double lengthOfSegment) {
			Polygon ret = null;
			if ((lengthOfSegment > Double.MIN_VALUE) && (e != null)) {
				double a = (e.getWidth() / 2.0), b = (e.getHeight() / 2.0);
				double circ = Math.PI * (3.0 * (a + b) - Math.sqrt((3.0 * a + b) * (a + 3.0 * b)));
				/*
				 * double area = Math.PI a b;
				 */
				double np = (circ / lengthOfSegment) + 1.0;
				ret = castEllipse2Polygon(e, (int) np);
			}
			return ret;
		}

		public static void roteatePolygon(Polygon poly, double degreesRotation, double xCenter, double yCenter) {
			Point p; // then in start the rotation
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians(degreesRotation), xCenter, yCenter);
			for (int i = 0; i < poly.npoints; i++) {
				p = new Point(poly.xpoints[i], poly.ypoints[i]);
				at.transform(p, p);
				poly.xpoints[i] = p.x;
				poly.ypoints[i] = p.y;
			}
		}

		public static byte[][][] castBufferedImage_MatrixOf_Byte(BufferedImage bi) {
			byte[][][] ret = null;
			if (bi != null) {
				int righe = bi.getHeight(), colonne = bi.getWidth(), argb = 0;
				ret = new byte[righe][][];
				for (int ri = 0; ri < righe; ri++) {
					ret[ri] = new byte[colonne][];
					for (int c = 0; c < colonne; c++) {
						ret[ri][c] = new byte[4];
						argb = bi.getRGB(c, ri);
						ret[ri][c][0] = (byte) (argb);
						ret[ri][c][1] = (byte) (argb >> 8);
						ret[ri][c][2] = (byte) (argb >> 16);
						ret[ri][c][3] = (byte) (argb >> 24);
					}
				}
			}
			return ret;
		}

		public static byte[][][] castMatrixOf_IntToByte(int[][] m) {
			byte[][][] ret = null;
			if (m != null) {
				int righe = m.length, colonne = 0;
				int argb = 0;
				ret = new byte[righe][][];
				for (int ri = 0; ri < righe; ri++) {
					colonne = m[ri].length;
					ret[ri] = new byte[colonne][];
					for (int c = 0; c < colonne; c++) {
						ret[ri][c] = new byte[4];
						argb = m[ri][c];
						/*
						 * 0x00ff0000, // Red 0x0000ff00, // Green 0x000000ff, // Blue 0x0 //Alpha
						 */
						ret[ri][c][0] = (byte) ((argb) & 0xFF);
						ret[ri][c][1] = (byte) ((argb >> 8) & 0xFF);
						ret[ri][c][2] = (byte) ((argb >> 16) & 0xFF);
						ret[ri][c][3] = (byte) ((argb >> 24) & 0xFF);
					}
				}
			}
			return ret;
		}

		public static int[][] castMatrixOf_ByteToInt(byte[][][] m) {
			int[][] ret = null;
			if (m != null) {
				int righe = m.length, colonne = 0;
				int argb = 0;
				ret = new int[righe][];
				for (int ri = 0; ri < righe; ri++) {
					colonne = m[ri].length;
					ret[ri] = new int[colonne];
					for (int c = 0; c < colonne; c++) {
						argb = 0;
						for (int z = 0; z < 4; z++) {
							argb += (m[ri][c][z] << (8 * z));
						}
						ret[ri][c] = argb;
					}
				}
			}
			return ret;
		}

		public static int[][] castMatrixOf_ByteToInt_2(byte[][][] m) {
			int[][] ret = null;
			if (m != null) {
				int righe = m.length, colonne = 0;
				int argb = 0;
				ret = new int[righe][];
				for (int ri = 0; ri < righe; ri++) {
					colonne = m[ri].length;
					ret[ri] = new int[colonne];
					for (int c = 0; c < colonne; c++) {
						argb = 0;
						for (int z = 0; z < 4; z++) {
							argb += ((m[ri][c][z] << (8 * z)) & 0xFF);
						}
						ret[ri][c] = argb;
					}
				}
			}
			return ret;
		}

		public static int[][] castVector_Matrix_Nx1(int[] v) {
			int[][] ret = null;
			if (v != null) {
				ret = new int[v.length][1];
				for (int i = 0; i < v.length; i++) {
					ret[i][0] = v[i];
				}
			}
			return ret;
		}

		public static double[][] castVector_Matrix_Nx1(double[] v) {
			double[][] ret = null;
			if (v != null) {
				ret = new double[v.length][1];
				for (int i = 0; i < v.length; i++) {
					ret[i][0] = v[i];
				}
			}
			return ret;
		}

		public static <T> T[] castArrayList_Array(ArrayList<T> al) {
			@SuppressWarnings("unchecked")
			T[] ret = (T[]) new Object[al.size()]; // I don't know if it works
			int i = 0;
			for (T e : al) {
				try {
					ret[i] = e;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				i++;
			}
			return ret;
		}

		// equivalent to CastingClass.castConcat_IntMatrix_ToArray(
		// CastingClass.castIntArrayList_Vettori_ToMatrix(
		public static int[] castConcat_IntArrayList_Vettori_ToArray(ArrayList<int[]> integers) {
			int[] ret = null;
			if (integers != null) {
				int len = 0, counter = 0;
				for (int i = 0; i < integers.size(); i++) // conto gli elemento
															// totali di tutta la
															// matrice
				{
					if (integers.get(i) != null) {
						len += integers.get(i).length;
					}
				}
				ret = new int[len];
				for (int r = 0; r < integers.size(); r++) {
					if (integers.get(r) != null) {
						for (int c = 0; c < integers.get(r).length; c++) {
							ret[counter] = integers.get(r)[c];
							counter++;
							// ret[++counter] = integers.get(r)[c]; // si pu�
							// compattare il tutto con cos�?
						}
					}
				}

			}
			return ret;
		}

		public static double[] castConcat_DoubArrayList_Vettori_ToArray(ArrayList<double[]> integers) {
			double[] ret = null;
			if (integers != null) {
				int len = 0, counter = 0;
				for (int i = 0; i < integers.size(); i++) // conto gli elemento
															// totali di tutta la
															// matrice
				{
					if (integers.get(i) != null) {
						len += integers.get(i).length;
					}
				}
				ret = new double[len];
				for (int r = 0; r < integers.size(); r++) {
					if (integers.get(r) != null) {
						for (int c = 0; c < integers.get(r).length; c++) {
							ret[counter] = integers.get(r)[c];
							counter++;
							// ret[++counter] = integers.get(r)[c]; // si pu�
							// compattare il tutto con cos�?
						}
					}
				}

			}
			return ret;
		}

		public static int[] castConcat_IntMatrix_ToArray(int[][] m) {
			int[] ret = null;
			if (m != null) {
				int len = 0, counter = 0;
				for (int i = 0; i < m.length; i++) // conto gli elemento totali di
													// tutta la matrice
				{
					if (m[i] != null) {
						len += m[i].length;
					}
				}
				ret = new int[len];
				for (int r = 0; r < m.length; r++) {
					if (m[r] != null) {
						for (int c = 0; c < m[r].length; c++) {
							ret[counter] = m[r][c];
							counter++;
							// ret[++counter] = m[r][c]; // si pu� compattare il
							// tutto con cos�?
						}
					}
				}
			}
			return ret;
		}

		public static double[] castConcat_DoubMatrix_ToArray(double[][] m) {
			double[] ret = null;
			if (m != null) {
				int len = 0, counter = 0;
				for (int i = 0; i < m.length; i++) // conto gli elemento totali di
													// tutta la matrice
				{
					if (m[i] != null) {
						len += m[i].length;
					}
				}
				ret = new double[len];
				for (int r = 0; r < m.length; r++) {
					if (m[r] != null) {
						for (int c = 0; c < m[r].length; c++) {
							ret[counter] = m[r][c];
							counter++;
							// ret[++counter] = m[r][c]; // si pu� compattare il
							// tutto con cos�?
						}
					}
				}
			}
			return ret;
		}

		public static int[] castDoubInt_Array(double[] ar) {
			int[] ret = null;
			if (ar != null) {
				ret = new int[ar.length];
				for (int i = 0; i < ar.length; i++) {
					ret[i] = (int) ar[i];
				}
			}
			return ret;
		}

		public static double[] castIntDoub_Array(int[] ar) {
			double[] ret = null;
			if (ar != null) {
				ret = new double[ar.length];
				for (int i = 0; i < ar.length; i++) {
					ret[i] = ar[i];
				}
			}
			return ret;
		}

		public static int[][] castDoubInt_Matrix(double[][] ma) {
			int[][] ret = null;
			if (ma != null) {
				ret = new int[ma.length][];
				int c = 0;
				for (int r = 0; r < ma.length; r++) {
					if (ma[r] != null) {
						ret[r] = new int[ma[r].length];
						for (c = 0; c < ma[r].length; c++) {
							ret[r][c] = (int) ma[r][c];
						}
					}
				}
			}
			return ret;
		}

		public static double[][] castIntDoub_Matrix(int[][] ma) {
			double[][] ret = null;
			if (ma != null) {
				ret = new double[ma.length][];
				int c = 0;
				for (int r = 0; r < ma.length; r++) {
					if (ma[r] != null) {
						ret[r] = new double[ma[r].length];
						for (c = 0; c < ma[r].length; c++) {
							ret[r][c] = ma[r][c];
						}
					}
				}
			}
			return ret;
		}

		public static int[] castIntegerArrayList_ToArray(ArrayList<Integer> integers) {
			int[] ret = new int[integers.size()];
			Iterator<Integer> iterator = integers.iterator();
			for (int i = 0; i < ret.length; i++) {
				ret[i] = iterator.next().intValue();
			}
			return ret;
		}

		public static double[] castDoubleArrayList_ToArray(ArrayList<Double> doubles) {
			double[] ret = new double[doubles.size()];
			Iterator<Double> iterator = doubles.iterator();
			for (int i = 0; i < ret.length; i++) {
				ret[i] = iterator.next().intValue();
			}
			return ret;
		}

		public static int[][] castIntArrayList_Vettori_ToMatrix(ArrayList<int[]> integers) {
			int[][] ret = null;
			if (integers != null) {
				ret = new int[integers.size()][];
				for (int e = 0; e < integers.size(); e++) {
					if (integers.get(e) != null) {
						ret[e] = new int[integers.get(e).length];
						for (int i = 0; i < ret.length; i++) {
							ret[e][i] = integers.get(e)[i];
						}
					}
				}
			}
			return ret;
		}

		public static double[][] castDoubArrayList_Vettori_ToMatrix(ArrayList<double[]> doubles) {
			double[][] ret = null;
			if (doubles != null) {
				ret = new double[doubles.size()][];
				for (int e = 0; e < doubles.size(); e++) {
					if (doubles.get(e) != null) {
						ret[e] = new double[doubles.get(e).length];
						for (int i = 0; i < ret.length; i++) {
							ret[e][i] = doubles.get(e)[i];
						}
					}
				}
			}
			return ret;
		}

		public static ArrayList<Integer> castIntArray_ToArrayList(int[] integers) {
			ArrayList<Integer> ret = new ArrayList<Integer>(integers.length);
			for (int t : integers) {
				ret.add(t);
			}
			return ret;
		}

		public static ArrayList<Double> castDoubleArray_ToArrayList(double[] doubles) {
			ArrayList<Double> ret = new ArrayList<Double>(doubles.length);
			for (double t : doubles) {
				ret.add(t);
			}
			return ret;
		}

		public static ArrayList<double[][]> castIntDoub_ArrayList_Matrix(ArrayList<int[][]> al) {
			ArrayList<double[][]> ret = null;
			if (al != null) {
				ret = new ArrayList<double[][]>(al.size());
				for (int i = 0; i < al.size(); i++) // per ogni matrice
				{
					if (al.get(i) != null) {
						ret.add(new double[al.get(i).length][]);
						for (int c = 0; c < al.get(i).length; c++) // per ogni riga
						{
							if (al.get(i)[c] != null) {
								ret.get(i)[c] = new double[al.get(i)[c].length];
								for (int e = 0; e < al.get(i)[c].length; e++) // per
																				// ogni
																				// elemento
																				// della
																				// riga
																				// (
																				// ossia,
																				// colonna
																				// )
								{
									ret.get(i)[c][e] = al.get(i)[c][e];
								}
							}
						}
					} else {
						ret.add(null);
					}
				}
			}
			return ret;
		}

		public static ArrayList<int[][]> castDoubInt_ArrayList_Matrix(ArrayList<double[][]> al) {
			ArrayList<int[][]> ret = null;
			if (al != null) {
				ret = new ArrayList<int[][]>(al.size());
				for (int i = 0; i < al.size(); i++) // per ogni matrice
				{
					if (al.get(i) != null) {
						ret.add(new int[al.get(i).length][]);
						for (int c = 0; c < al.get(i).length; c++) // per ogni riga
						{
							if (al.get(i)[c] != null) {
								ret.get(i)[c] = new int[al.get(i)[c].length];
								for (int e = 0; e < al.get(i)[c].length; e++) // per
																				// ogni
																				// elemento
																				// della
																				// riga
																				// (
																				// ossia,
																				// colonna
																				// )
								{
									ret.get(i)[c][e] = (int) al.get(i)[c][e];
								}
							}
						}
					} else {
						ret.add(null);
					}
				}
			}
			return ret;
		}

		public static ArrayList<int[]> castDoubInt_ArrayList_Vettore(ArrayList<double[]> al) {
			ArrayList<int[]> ret = null;
			if (al != null) {
				ret = new ArrayList<int[]>(al.size());
				for (int i = 0; i < al.size(); i++) // per ogni vettore
				{
					if (al.get(i) != null) {
						ret.add(new int[al.get(i).length]);
						for (int e = 0; e < al.get(i).length; e++) // per ogni
																	// elemento
						{
							ret.get(i)[e] = (int) al.get(i)[e];
						}
					} else {
						ret.add(null);
					}
				}
			}
			return ret;
		}

		public static ArrayList<double[]> castIntDoub_ArrayList_Vettore(ArrayList<int[]> al) {
			ArrayList<double[]> ret = null;
			if (al != null) {
				ret = new ArrayList<double[]>(al.size());
				for (int i = 0; i < al.size(); i++) // per ogni vettore
				{
					if (al.get(i) != null) {
						ret.add(new double[al.get(i).length]);
						for (int e = 0; e < al.get(i).length; e++) // per ogni
																	// elemento
						{
							ret.get(i)[e] = al.get(i)[e];
						}
					} else {
						ret.add(null);
					}
				}
			}
			return ret;
		}

		public static int[][][] castBufferedImage_MatrixOf_ARGB(BufferedImage bi) {
			int[][][] ret = null;
			if (bi != null) {
				int righe = bi.getHeight(), colonne = bi.getWidth(), argb = 0;
				ret = new int[righe][][];
				for (int ri = 0; ri < righe; ri++) {
					ret[ri] = new int[colonne][];
					for (int c = 0; c < colonne; c++) {
						ret[ri][c] = new int[4];
						argb = bi.getRGB(c, ri);
						ret[ri][c][0] = ((argb) & 0xFF); // blue ?
						ret[ri][c][1] = ((argb >> 8) & 0xFF); // green ?
						ret[ri][c][2] = ((argb >> 16) & 0xFF); // red ?
						ret[ri][c][3] = ((argb >> 24) & 0xFF); // alpha ?
					}
				}
			}
			return ret;
		}

		public static ArrayList<Point2D> castMatrixToListPoint2D(boolean[][] m) {
			ArrayList<Point2D> ret = null;
			if (m != null && ManipulatingArrays.isTheMatrixRectangular(m)) {
				boolean[] riga;
				ret = new ArrayList<Point2D>(100);
				int c;
				for (int r = 0; r < m.length; r++) {
					riga = m[r];
					for (c = 0; c < riga.length; c++) {
						if (riga[c]) {
							ret.add(new Point2D.Double(c, r));
						}
					}
				}
				ret.trimToSize();
			}
			return ret;
		}

		/**
		 * Non-Zero values means a point
		 */
		public static ArrayList<Point2D> castMatrixToListPoint2D(int[][] m) {
			ArrayList<Point2D> ret = null;
			if (m != null && ManipulatingArrays.isTheMatrixRectangular(m)) {
				int[] riga;
				ret = new ArrayList<Point2D>(100);
				int c;
				for (int r = 0; r < m.length; r++) {
					riga = m[r];
					for (c = 0; c < riga.length; c++) {
						if (riga[c] != 0) {
							ret.add(new Point2D.Double(c, r));
						}
					}
				}
				ret.trimToSize();
			}
			return ret;
		}
	}
}