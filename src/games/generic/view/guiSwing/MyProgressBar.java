package games.generic.view.guiSwing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MyProgressBar extends JLabel {

	public static void main(String[] args) {
		try {
			String nomeImmagine;
			nomeImmagine = MyProgressBar.class.getSimpleName();
			/*
			 * = MyProgressBar.class.getName(); try { nomeImmagine =
			 * nomeImmagine.substring(MyProgressBar.class.getPackage().getName() .length() +
			 * 1, nomeImmagine.length()); } catch (Exception exc) { exc.printStackTrace(); }
			 */
			// path = nomeImmagine;
			// bi = castMatrixBufferedImage_ARGB( getMatrixPixel() ) ;
			System.out.println(writeImage(nomeImmagine + " _immagine ORIGINALE", matricePixelOriginal));
			/*
			 * getMatrixPixelOriginal() ) );
			 *
			 * CastingClass.
			 *
			 * File.separatorChar +
			 */
			MyProgressBar mpb = new MyProgressBar(500, 200, 0, 5, 0);
			System.out.println(
					"originale redimensionata " + writeImage(nomeImmagine + " _originale redimensionata", mpb.bi));

			mpb.setColorBackgroundBar(BLUE_CYAN_MANA, BLUE_LIGHT_BACKGROUND);
			// System.out.println( "resized " + writeImage( nomeImmagine +
			// " _resized" , mpb.getMatrixPixel() ) );
			System.out.println("resized " + writeImage(nomeImmagine + " _resized", mpb.bi));

			mpb.setValue(1);
			mpb.setColorBackgroundBar(GREENWATER_SHIELD, SAND_BACKGROUND);
			// System.out.println( "recolored " + writeImage( nomeImmagine +
			// " _recolored" , mpb.getMatrixPixel() ) );
			System.out.println("recolored " + writeImage(nomeImmagine + " _recolored", mpb.bi));

			mpb.setValue(2);
			mpb.setColorBackgroundBar(UNKNOWN_PURPLE_DARK, UNKNOWN_EMERALD_LIGHT);
			// System.out.println( "recolored " + writeImage( nomeImmagine +
			// " _recolored 2" , mpb.getMatrixPixel() ) );
			System.out.println("recolored " + writeImage(nomeImmagine + " _recolored 2", mpb.bi));

			mpb.setValue(3);
			mpb.setColorBackgroundBar(ORANGE_EXPERIENCE, LILLA);
			// System.out.println( "recolored " + writeImage( nomeImmagine +
			// " _recolored 3" , mpb.getMatrixPixel() ) );
			System.out.println("recolored " + writeImage(nomeImmagine + " _recolored 3", mpb.bi));

			mpb.setValue(4);
			mpb.setColorBackgroundBar(REDBLOOD_LIFE, PINK_SALMON_BACKGROUND);
			// System.out.println( "recolored " + writeImage( nomeImmagine +
			// " _recolored 4" , mpb.getMatrixPixel() ) );
			System.out.println("recolored " + writeImage(nomeImmagine + " _recolored 4", mpb.bi));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("FINISH");
	}

	// private static String path = "";

	static {
		// initialize();
		matricePixelOriginal = getMatrixPixel_New();
	}

	public MyProgressBar() { constructor(widthOriginal, heightOriginal, 0, 100, 0); }

	public MyProgressBar(int valoreMin, int valoreMax, int valoreNow) {
		this(widthOriginal, heightOriginal, valoreMin, valoreMax, valoreNow);
	}

	public MyProgressBar(int widthProg, int heightProg) { constructor(widthProg, heightProg, 0, 100, 0); }

	public MyProgressBar(Dimension size) { this(size, 0, 100, 0); }

	public MyProgressBar(Dimension size, int valoreMin, int valoreMax, int valoreNow) {
		if (size != null) {
			constructor((int) size.getWidth(), (int) size.getHeight(), valoreMin, valoreMax, valoreNow);
		} else {
			constructor(widthOriginal, heightOriginal, valoreMin, valoreMax, valoreNow);
		}
	}

	public MyProgressBar(int widthProg, int heightProg, int valoreMin, int valoreMax, int valoreNow) {
		constructor(widthProg, heightProg, valoreMin, valoreMax, valoreNow);
	}

	// TODO
	private void constructor(int widthProg, int heightProg, int valoreMin, int valoreMax, int valoreNow) {
		// setting values
		if (valoreMin >= 0) { valMin = valoreMin; }
		if (valoreMax >= 0) { valMax = valoreMax; }
		setValue(valoreNow, false);
		initializeBufferedImage();
		// initializeBufferedImage();
		// aggiorno le dimensioni di tutto e, di conseguenza, dell'immagine
		// setSizeProgBar( widthProg, heightProg );
		setSize(widthProg, heightProg);
		this.setColorBackgroundBar(colorBackground, colorBar);
	}

	private BufferedImage bi = null;

	public static enum RotationAngleDeg {
		LEFT_EMPTY_RIGHT_FULL(0.0), LEFT_FULL_RIGHT_EMPTY(180.0), BOTTOM_EMPTY_UP_FULL(270.0),
		TOP_EMPTY_DOWN_FULL(90.0);

		final double value;

		RotationAngleDeg(double n) { value = n; }

		public double getValue() { return value; }
	}

	public static enum RotationPointStatement {
		LEFT_ROTATION_POINT(0), CENTER_ROTATION_POINT(1), RIGHT_ROTATION_POINT(2), TOP_ROTATION_POINT(0),
		BOTTOM_ROTATION_POINT(2);

		final int value;

		RotationPointStatement(int n) { value = n; }

		public int getValue() { return value; }
	}

	private static int heightOriginal = 0, widthOriginal = 0;
	private int valNow = 0, valMax = 100, valMin = 0, height = 0, width = 0,
			xRotationPointStatement = RotationPointStatement.CENTER_ROTATION_POINT.value,
			yRotationPointStatement = RotationPointStatement.CENTER_ROTATION_POINT.value;

	// punti interessanti
	private int minXbg = 0, minYbg = 0, maxXbg = matricePixelOriginal[0].length, maxYbg = matricePixelOriginal.length;
	/*
	 * punti di inizio e fine del background della barra .. si intende della matrice
	 * ORIGINALE
	 */

	private double angDeg = RotationAngleDeg.LEFT_EMPTY_RIGHT_FULL.value;

	// private int[][] matricePixel ;
	private static int[][] matricePixelOriginal;

	private Color colorBackground = GREENWATER_SHIELD; // LIGHTGREEN
	private Color colorBar = GREEN_OLD_BACKGROUND;

	/**
	 * Colori propri dell'immagine di bordo che non possono essere scelti come
	 * colori di sfondo eo di barra. Nel qual caso un utente volesse scegliere
	 * questi valori interi i sono conbinazioni di int), si setta il colore new
	 * Color ( col.getRGB()+1 )
	 */
	private static final Color[] partsOfBorder = new Color[] { new Color(-3620889, true), // violet
			new Color(-3159830, true), // DarkViolet
			new Color(-1185288, true), // LightViolet
			new Color(-921863, true) // LightSand
	};

	public static final Color

	/** life : strong red, a bit dark, similar to blood, but less bright */
	REDBLOOD_LIFE = new Color(237, 24, 35), PINK_SALMON_BACKGROUND = new Color(240, 142, 159), // for
																								// life
																								// :
			BLUE_CYAN_MANA = new Color(0, 116, 255), // mana : azzurrino blue, a
														// mixtur of blue and
														// cyan
			BLUE_LIGHT_BACKGROUND = new Color(157, 201, 255), // for mana ( 170,
																// 208, 255 )
			GREENWATER_SHIELD = new Color(10, 252, 190), // shield : gren-water
															// very light
			ORANGE_EXPERIENCE = new Color(255, 128, 0), // experience : a middle
														// strong orange
			ORANGE_LIGHT_BACKGROUND = new Color(249, 207, 145), // usual
																// background ..
																// shield?
			GREEN_LIGHTEST_BACKGROUND = new Color(149, 255, 149), // for shield
																	// :
			SAND_BACKGROUND = new Color(239, 228, 176), // for exp :
			GREEN_LIGHT_BACKGROUND = new Color(149, 255, 149), // usual
																// background ..
																// shield?
			REDSALMON_BACKGROUND = new Color(236, 117, 138), // for life 2 :
			LILLA = new Color(204, 116, 211), // lilla, boh .. it's cool xD ;
			UNKNOWN_EMERALD_LIGHT = new Color(32, 192, 92), // light emerald,
															// boh .. it's cool
															// xD ;
			UNKNOWN_EMERALD_BRILLANT = new Color(32, 192, 92), // light emerald,
																// boh .. it's
																// cool xD ;
			UNKNOWN_PURPLE_DARK = new Color(149, 0, 149), // viola so dark and
															// strong
			GREEN_OLD_BACKGROUND = new Color(64, 255, 64), //
			USER_DEFINITED = new Color(0, 0, 0);

	// GETTERS

	private static final long serialVersionUID = -6244367458005809673L;

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		AffineTransform aT = g2.getTransform();
		Shape oldshape = g2.getClip();
		double xxx = this.getWidth() * (xRotationPointStatement / 2.0);
		double yyy = this.getHeight() * (yRotationPointStatement / 2.0);
		aT.rotate(Math.toRadians(angDeg), xxx, yyy); // x e y sono le coordinate
														// relative all'origine
														// di default, aventi
														// coordinate P(getX(),
														// getY())
		g2.setTransform(aT);
		g2.setClip(oldshape);
		super.paintComponent(g);
	}

	public BufferedImage getBufferedImage() {
		/*
		 * if ( matricePixel == null ) { initializePixelMatrix(); } return
		 * castMatrixBufferedImage_ARGB( matricePixel ) ; //getBufferedImageCopy( bi );
		 */
		if (bi == null) { initializeBufferedImage(); }
		return getBufferedImageCopy(bi);
	}

	public int[][] getMatrixPixel() {
		/*
		 * if ( matricePixel == null ) { initializePixelMatrix(); } return
		 * getMatriceCopy ( matricePixel ) ;
		 */
		return castBufferedImageMatrix(bi);
	}

	public static int[][] getMatrixPixelOriginal() { return getMatriceCopy(matricePixelOriginal); }

	/**
	 * Be carefull : it create new istances of Color at every call
	 */
	public Color getColorBackGround() {
		// Color = colors [ colorBackGround ]; return new Color( c.getRed(),
		// c.getGreen(), c.getBlue() );
		// return new Color( colors [ colorBackGround ].getRGB() );
		return new Color(colorBackground.getRGB());
	}

	/**
	 * Be carefull : it create new istances of Color at every call
	 */
	public Color getColorBar() {
		// return new Color( colors [ colorBar ].getRGB() );
		return new Color(colorBar.getRGB());
	}

	public double getAngleRotation() { return angDeg; }

	public int getValueNow() { return valNow; }

	public int getValueMin() { return valMin; }

	public int getValueMax() { return valMax; }

	public int getXRotationPointStatement() { return xRotationPointStatement; }

	public int getYRotationPointStatement() { return yRotationPointStatement; }

	// RotationPointStatement.

	// SETTERS
	public void setRotationPointStatement(RotationPointStatement rsX, RotationPointStatement rsY) {
		setXRotationPointStatement(rsX);
		setYRotationPointStatement(rsY);
	}

	public void setXRotationPointStatement(RotationPointStatement rs) {
		if (rs != null) {
			if (rs.compareTo(RotationPointStatement.BOTTOM_ROTATION_POINT) == 0
					|| rs.compareTo(RotationPointStatement.TOP_ROTATION_POINT) == 0) {
				/*
				 * non vanno bene loro due
				 */
				xRotationPointStatement = RotationPointStatement.CENTER_ROTATION_POINT.value;
			} else {
				xRotationPointStatement = rs.value;
			}
		} else {
			xRotationPointStatement = RotationPointStatement.CENTER_ROTATION_POINT.value;
		}
	}

	public void setYRotationPointStatement(RotationPointStatement rs) {
		if (rs != null) {
			if (rs.compareTo(RotationPointStatement.LEFT_ROTATION_POINT) == 0
					|| rs.compareTo(RotationPointStatement.RIGHT_ROTATION_POINT) == 0) {
				/*
				 * non vanno bene loro due
				 */
				yRotationPointStatement = RotationPointStatement.CENTER_ROTATION_POINT.value;
			} else {
				yRotationPointStatement = rs.value;
			}
		} else {
			yRotationPointStatement = RotationPointStatement.CENTER_ROTATION_POINT.value;
		}
	}

	public void setAngleDegRotationPaint(RotationAngleDeg ra) {
		if (ra != null) {
			angDeg = ra.value;
		} else {
			angDeg = RotationAngleDeg.LEFT_EMPTY_RIGHT_FULL.value;
		}
	}

	public int setValue(int value) { return setValue(value, true); }

	private int setValue(int value, boolean mustUpdate) {
		int oldValue = valNow;
		boolean changed = false;
		if (value < valMin) { value = valMin; }
		if (value > valMax) { value = valMax; }
		if (valNow != value) {
			valNow = value;
			changed = true;
		}
		// System.out.print( "\n\t...\t changed " + changed);
		if (mustUpdate || changed) {
			// restoreMinMaxPointDraw();
			updateImage(oldValue);
			// System.out.print( "\t...\t and updated ");
		}
		// System.out.println();
		return oldValue;
	}

	public int setMaxValue(int maxValue) {
		int oldValue = valMax;
		if (maxValue > 0) {
			valMax = maxValue;
			setValue(valNow, true); // chiamata per aggiustarlo
		}
		return oldValue;
	}

	public int setMinValue(int minValue) {
		int oldValue = valMin;
		if (minValue >= 0) {
			valMin = minValue;
			setValue(valNow, true); // chiamata per aggiustarlo
		}
		return oldValue;
	}

	private void setSizeProgBar(int w, int h) {
		int ww = (w < 2) ? 2 : w;
		int hh = (h < 2) ? 2 : h;
		if (height != hh || width != ww) {
			// matricePixel = getScaledInstance(matricePixelOriginal, ww, hh);
			bi = castMatrixBufferedImage_ARGB(getScaledInstance(matricePixelOriginal, ww, hh));
			height = hh;
			width = ww;
			// System.out.println( " ... colorBackground = " +
			// colorBackground.getRGB() + "\n\t... -16057154 delete alpha -> " +
			// deleteAlpha(-16057154) + "\t casting in RGB by color " + new
			// Color(-16057154).getRGB() ); // -16057154
			restoreMinMaxPointDraw();
			updateImage(valNow);
		}
	}

	@Override
	public void setSize(int newWidth, int newHeigth) {
		super.setSize(newWidth, newHeigth);
		setPreferredSize(getSize());
		setSizeProgBar(newWidth, newHeigth);
	}

	/*
	 * public boolean setColorBackground( int index ) { Colors[] cc = values();
	 * boolean setted = index >= 0 && index < cc.length; if( setted ) {
	 * colorBackground = cc[ index ]; } return setted; }
	 */
	// TODO
	public boolean setColorBackground(Color col) {
		int oldColorBackground = colorBackground.getRGB();
		boolean setted = (col != null) && (col.getRGB() != oldColorBackground) && (!isTheColorTheSameOfTheBorder(col));
		if (setted) {
			colorBackground = col; // USER_DEFINITED.setColor_Enum(col) ;
			updateImage(colorBackground.getRGB(), colorBar.getRGB(), colorBar.getRGB(), oldColorBackground);
		}
		return setted;
	}

	public boolean setColorBackground(int argb) {
		byte alpha, red, green, blue;
		red = (byte) ((argb) & 0xFF);
		green = (byte) ((argb >> 8) & 0xFF);
		blue = (byte) ((argb >> 16) & 0xFF);
		alpha = (byte) ((argb >> 24) & 0xFF);
		int oldColorBackground = colorBackground.getRGB();
		boolean setted = (castARGBbytesToARGBInt(alpha, red, green, blue) != oldColorBackground)
				&& (!isTheColorTheSameOfTheBorder(new Color(argb)));
		if (setted) {
			colorBackground = new Color(red, green, blue, alpha); // USER_DEFINITED.setColor_Enum(
																	// red,
																	// green,
																	// blue,
																	// alpha ) ;
			updateImage(colorBackground.getRGB(), colorBar.getRGB(), colorBar.getRGB(), oldColorBackground);
		}
		return setted;
	}

	public boolean setColorBar(Color col) {
		int oldColorBar = colorBar.getRGB();
		boolean setted = (col != null) && (col.getRGB() != oldColorBar) && (!isTheColorTheSameOfTheBorder(col));
		if (setted) {
			colorBar = col; // USER_DEFINITED.setColor_Enum(col) ;
			updateImage(colorBackground.getRGB(), colorBar.getRGB(), oldColorBar, colorBackground.getRGB());
		}
		return setted;
	}

	public boolean setColorBar(int argb) {
		byte alpha, red, green, blue;
		red = (byte) ((argb) & 0xFF);
		green = (byte) ((argb >> 8) & 0xFF);
		blue = (byte) ((argb >> 16) & 0xFF);
		alpha = (byte) ((argb >> 24) & 0xFF);
		// Color c = new Color(red, green, blue, alpha );
		int oldColorBar = colorBar.getRGB();
		boolean setted = (castARGBbytesToARGBInt(alpha, red, green, blue) != oldColorBar)
				&& (!isTheColorTheSameOfTheBorder(new Color(argb)));
		if (setted) {
			colorBar = new Color(red, green, blue, alpha); // USER_DEFINITED.setColor_Enum(
															// red, green, blue,
															// alpha ) ;
			updateImage(colorBackground.getRGB(), colorBar.getRGB(), oldColorBar, colorBackground.getRGB());
		}
		return setted;
	}

	public boolean setColorBackgroundBar(int colBackg, int colBar) {
		int oldColorBar = colorBar.getRGB(), oldColorBackground = colorBackground.getRGB();
		boolean settedBar = (colBar != oldColorBar) && (!isTheColorTheSameOfTheBorder(new Color(colBar))),
				settedBackg = (colBackg != oldColorBackground) && (!isTheColorTheSameOfTheBorder(new Color(colBackg))),
				ret;
		if (settedBar) {
			byte alpha, red, green, blue;
			red = (byte) ((colBar) & 0xFF);
			green = (byte) ((colBar >> 8) & 0xFF);
			blue = (byte) ((colBar >> 16) & 0xFF);
			alpha = (byte) ((colBar >> 24) & 0xFF);
			colorBar = new Color(red, green, blue, alpha); // USER_DEFINITED.setColor_Enum(
															// red, green, blue,
															// alpha ) ;
		}
		if (settedBackg) {
			byte alpha, red, green, blue;
			red = (byte) ((colBackg) & 0xFF);
			green = (byte) ((colBackg >> 8) & 0xFF);
			blue = (byte) ((colBackg >> 16) & 0xFF);
			alpha = (byte) ((colBackg >> 24) & 0xFF);
			colorBackground = new Color(red, green, blue, alpha); // USER_DEFINITED.setColor_Enum(
																	// red,
																	// green,
																	// blue,
																	// alpha ) ;
		}
		ret = settedBar || settedBackg;
		if (ret) { updateImage(colorBackground.getRGB(), colorBar.getRGB(), oldColorBar, oldColorBackground); }
		return ret;
	}

	public boolean setColorBackgroundBar(Color colBackg, Color colBar) {
		boolean setted = false;
		if (colBar != null && colBackg != null) {
			int oldColorBar = colorBar.getRGB();
			boolean settedBar = (colBar.getRGB() != oldColorBar) && (!isTheColorTheSameOfTheBorder(colBar));
			if (settedBar) {
				colorBar = colBar; // USER_DEFINITED.setColor_Enum( colBar ) ;
			}
			int oldColorBackground = colorBackground.getRGB();
			boolean settedBackg = (colBackg.getRGB() != oldColorBackground)
					&& (!isTheColorTheSameOfTheBorder(colBackg));
			// System.out.println("\t\t\t\t settedBackg = " + settedBackg );
			if (settedBackg) {
				colorBackground = colBackg; // USER_DEFINITED.setColor_Enum(
											// colBackg ) ;
			}
			updateImage(colorBackground.getRGB(), colorBar.getRGB(), oldColorBar, oldColorBackground);
			setted = settedBar || settedBackg;
		}
		return setted;
	}

	public double setAngleRotation(double angleDegrees) {
		double oldValue = angDeg, newValue = angleDegrees % 360.0;
		while (newValue < 360.0) {
			newValue += 360.0;
		}
		if (oldValue != newValue) {
			angDeg = newValue;
			repaint();
		}
		return oldValue;
	}

	public boolean updateImage() {
		// return updateImage( colorBackground.getRGB(), colorBar.getRGB(),
		// colorBar.getRGB(), colorBackground.getRGB() );
		return updateImage(valNow);
	}

	private boolean updateImage(int colBg, int colBar, int oldColorBar, int oldColorBackground) {
		boolean somethingChanged = (colorBar.getRGB() != oldColorBar)
				|| (colorBackground.getRGB() != oldColorBackground); // , b;
		// if ( somethingChanged ) {
		// System.out.println(" image updating ...");
		// restoreMinMaxPointDraw();
		// b =
		updateMatrixPixel(colBg, colBar, oldColorBar, oldColorBackground);
		// setIcon( new ImageIcon ( castMatrixBufferedImage_ARGB(matricePixel) )
		// );
		setIcon(new ImageIcon(bi));
		if (isVisible()) { repaint(); }
		// System.out.println("somethingChanged : " + somethingChanged +
		// "\t updateMatrixPixel returns : " + b );
		// System.out.println("\t image updated");
		// }
		return somethingChanged;
	}

	private boolean updateImage(int oldValue) {
		boolean somethingChanged;
		// if ( somethingChanged ) {
		// System.out.println("\n image updating ...");
		// restoreMinMaxPointDraw();
		somethingChanged = updateMatrixPixel(oldValue);
		setIcon(new ImageIcon(bi));
		if (isVisible()) { repaint(); }
		// System.out.println("\t image updated");
		// }
		return somethingChanged;
	}

	public boolean updateMatrixPixel() {
		return updateMatrixPixel(colorBackground.getRGB(), colorBar.getRGB(), colorBar.getRGB(),
				colorBackground.getRGB());
	}

	private boolean updateMatrixPixel(int colBg, int colBar, int oldColorBar, int oldColorBackground) {
		int r, c, nParti = valMax - valMin, colOldMatrice // riga[],
				, lastPixelWidth; // l'ultimo pixel che andr� dipinto con il
									// colore
									// della barra, dopodich� andranno dipinti
									// con il
									// colore del background
		// ricerca del primo pixel e dell'ultimo in cui � presente la barra (si
		// ipotizza che la barra abbia i bordi laterali verticali)
		boolean somethingChanged = false, b = false, bb = false, bbb = false;
		if (nParti > 0) {
			lastPixelWidth = (int) (((double) ((maxXbg - minXbg) * (valNow - valMin))) / ((double) nParti)) + minXbg;
		} else {
			lastPixelWidth = minXbg;
		}
		System.out.println("\t\t lastPixelWidth = " + lastPixelWidth);
		for (r = minYbg; r <= maxYbg; r++) { // per ogni riga
			// riga = matricePixel[r];
			for (c = minXbg; c <= lastPixelWidth && c < width; c++) { // scorrimento
																		// di
																		// tutte
																		// le
																		// colonne
				// colOldMatrice = riga[c]; // variabile ausiliaria per poter
				// essere pi� comprensibile e, spero, performante
				colOldMatrice = bi.getRGB(c, r);
				// if ( colOldMatrice == colBg || colOldMatrice ==
				// oldColorBackground || colOldMatrice == oldColorBar) { // ora
				// si colora con il colore della barra .. se c'era gi�, o era
				// semplicemente diverso ( perch� faceva parte del disegno ),
				// allora lo ignoro
				b = isColorsEqual(new Color(colOldMatrice), new Color(colBg));
				bb = isColorsEqual(new Color(colOldMatrice), new Color(oldColorBackground));
				bbb = isColorsEqual(new Color(colOldMatrice), new Color(oldColorBar));
				if (b || bb || bbb) {
					bi.setRGB(c, r, colBg);
					somethingChanged = true;
				}
			}
			for (; c <= maxXbg; c++) {
				colOldMatrice = bi.getRGB(c, r);
				b = isColorsEqual(new Color(colOldMatrice), new Color(colBar));
				bb = isColorsEqual(new Color(colOldMatrice), new Color(oldColorBackground));
				bbb = isColorsEqual(new Color(colOldMatrice), new Color(oldColorBar));
				if (b || bb || bbb) {
					bi.setRGB(c, r, colBar);
					somethingChanged = true;
				}
			}
			// if( r % 5 == 0 ) { writeImage( ("mpb"+ File.separatorChar + path
			// + " DURING " + r) , matricePixelOriginal); }
			// if( r % 5 == 0 ) { writeImage( ("mpb"+ File.separatorChar + path
			// + " DURING " + r) , bi); }
		}
		// System.out.println( "\t\t somethingChanged = " + somethingChanged );
		if (somethingChanged) { setIcon(new ImageIcon(bi)); }
		// System.out.println( "\t minXbg = " + minXbg + "\t minYbg = " + minYbg
		// + "\t maxXbg = " + maxXbg + "\t maxYbg = " + maxYbg );
		return somethingChanged;
	}

	private boolean updateMatrixPixel(int oldValue) {
		int r, c, nParti = valMax - valMin, colOldMatrice, colBg, colBar // riga[],
				, lastPixelWidth, oldPixelWidth; // l'ultimo pixel che andr�
													// dipinto con
													// il colore della barra,
													// dopodich�
													// andranno dipinti con il
													// colore
													// del background
		// ricerca del primo pixel e dell'ultimo in cui � presente la barra (si
		// ipotizza che la barra abbia i bordi laterali verticali)
		boolean somethingChanged = false, b = false;// boolean shouldWrite =
													// true;
		if (oldValue > valMax) { oldValue = valMax; }
		if (nParti > 0) {
			lastPixelWidth = (int) (Math.round(((double) ((maxXbg - minXbg) * (valNow - valMin))) / ((double) nParti)))
					+ minXbg;
			oldPixelWidth = (int) (((double) ((maxXbg - minXbg) * (oldValue - valMin))) / ((double) nParti)) + minXbg;
			if (oldPixelWidth < minXbg) { oldPixelWidth = minXbg; }
			if (lastPixelWidth < minXbg) { lastPixelWidth = minXbg; }
		} else {
			lastPixelWidth = oldPixelWidth = minXbg;
		}
		// System.out.println( "\t\t lastPixelWidth = " + lastPixelWidth );
		colBg = colorBackground.getRGB();
		colBar = colorBar.getRGB();
		if (oldValue == valNow) { // se si passa un valore uguale a quello
									// attuale, allora ridipingo tutto .. questa
									// scelta viene fatta se semplicemente si
									// ridimensiona la progressbar o si variano
									// i valori max e/o min
			for (r = minYbg; r <= maxYbg; r++) { // per ogni riga
				for (c = minXbg; c <= lastPixelWidth && c < width; c++) { // scorrimento
																			// di
																			// tutte
																			// le
																			// colonne
					colOldMatrice = bi.getRGB(c, r);
					b = isColorsEqual(new Color(colOldMatrice), colorBar);
					if (b) {
						bi.setRGB(c, r, colBg);
						somethingChanged = true;
					}
				}
				for (; c <= maxXbg; c++) {
					colOldMatrice = bi.getRGB(c, r);
					b = isColorsEqual(new Color(colOldMatrice), colorBackground);
					if (b) {
						bi.setRGB(c, r, colBar);
						somethingChanged = true;
					}
				}
				// if( shouldWrite && (r % 10 == 0) ) { writeImage( ("mpb"+
				// File.separatorChar + path + " DURING " + r) , bi); }
			}
		} else if (oldValue < valNow) {
			for (r = minYbg; r <= maxYbg; r++) { // per ogni riga
				for (c = oldPixelWidth; c <= lastPixelWidth && c < width; c++) { // scorrimento
																					// di
																					// tutte
																					// le
																					// colonne
					colOldMatrice = bi.getRGB(c, r);
					b = isColorsEqual(new Color(colOldMatrice), colorBar); // colorBackground
					if (b) { bi.setRGB(c, r, colBg); }
					// if( shouldWrite && (r % 10 == 0) ) { writeImage( ("mpb"+
					// File.separatorChar + path + " DURING " + r) , bi); }
				}
			}
			somethingChanged = true;
		} else {
			for (r = minYbg; r <= maxYbg; r++) { // per ogni riga
				for (c = lastPixelWidth; c <= oldPixelWidth; c++) {
					colOldMatrice = bi.getRGB(c, r);
					b = isColorsEqual(new Color(colOldMatrice), colorBackground); // colorBar
					if (b) { bi.setRGB(c, r, colBar); }
				}
				// if( shouldWrite && (r % 10 == 0) ) { writeImage( ("mpb"+
				// File.separatorChar + path + " DURING " + r) , bi); }
			}
			somethingChanged = true;
		}
		// System.out.println( "\t\t somethingChanged = " + somethingChanged );
		if (somethingChanged) {
			if (bi == null) { initializeBufferedImage(); }
			setIcon(new ImageIcon(bi));
		}
		// System.out.println( "\t minXbg = " + minXbg + "\t minYbg = " + minYbg
		// + "\t maxXbg = " + maxXbg + "\t maxYbg = " + maxYbg );
		return somethingChanged;
	}

	/**
	 * Restore the current image and bufferedImage to the original returns true if
	 * and only if the current matrix of pixel submits changes ather the method
	 * returns.
	 */
	public boolean restoreOriginal() {
		boolean hasChanged = false;
		if (matricePixelOriginal == null) {
			initializePixelMatrix();
			hasChanged = true; // then, it will surely submit changes
		} else {
			int r, c, rigaMp[], rigaOr[], colOrig; // colBi /*the color of the
													// buffered image at
													// position (x,y) */ ;
			if (height != heightOriginal || width != widthOriginal) { // se la
																		// matrice
																		// aveva
																		// dimensioni
																		// variate,
																		// copiaaaaaaa
				matricePixelOriginal = getMatriceCopy(matricePixelOriginal);
				height = heightOriginal;
				width = widthOriginal;
				hasChanged = true; // then, it will surely submit changes
			} else {
				for (r = 0; r < matricePixelOriginal.length; r++) { // per ogni
																	// riga ...
					rigaOr = matricePixelOriginal[r]; // salvo l'indirizzo in
														// memoria della riga in
														// una variabile di
														// supporto per una
														// migliore performance
					rigaMp = matricePixelOriginal[r];
					for (c = 0; c < rigaOr.length; c++) { // fissata una riga r,
															// per ogni colonana
															// ...
						colOrig = rigaOr[c]; // prendo il singolo pixel della
												// matrice originale e lo salvo
												// in una variabile di supporto
						if (rigaMp[c] != colOrig) {
							rigaMp[c] = colOrig;
							hasChanged = true;
						}
					}
				}
			}
		}
		if (hasChanged) { restoreMinMaxPointDraw(); }
		return hasChanged;
	}

	// TODO
	private void restoreMinMaxPointDraw() { // trasparensza : -16057154
		int r, c; // , riga[];, colBg = deleteAlpha(colorBackground.getRGB());
		// int alpha = colorBackground.getAlpha();
		minXbg = width;
		minYbg = -1;
		maxXbg = -1;
		maxYbg = height;
		boolean notFound = true;
		Color cbg = colorBackground;
		// ricerca dei minimi
		for (r = 0; (r < height); r++) {
			notFound = true;
			// riga = matricePixelOriginal[r];
			for (c = 0; (c < width && notFound);) {
				// notFound = ! ( (riga[c] == colBg) || (riga[c] ==
				// castAssemblyRGBandAlphaToARGB( colBg, alpha) ) ) ;

				// notFound = ! ( isColorsEqual ( new Color( riga[c] ) , cbg ) )
				// ;

				// notFound = ! ( isColorsEqual ( new Color( bi.getRGB(c, r) ) ,
				// cbg ) ) ;

				notFound = !(isColorsEqual(new Color(bi.getRGB(c, r)), cbg) || isColorsEqual(
						new Color(castAssemblyRGBandAlphaToARGB(bi.getRGB(c, r), bi.getRGB(c, r))), cbg));

				// notFound = ! ( ( deleteAlpha(riga[c]) == colBg) || (
				// deleteAlpha(riga[c]) == castAssemblyRGBandAlphaToARGB( colBg,
				// alpha) ) ) ;
				if (notFound) { c++; }
				// notFound = ! ( isColorsEqual ( new Color(
				// castAssemblyRGBandAlphaToARGB( bi.getRGB(c, r), bi.getRGB(c,
				// r) ) ) , cbg ) ) ;
			}
			if (!notFound) {
				if (minXbg > c) { minXbg = c; }
				if (minYbg < 0) { minYbg = r; }
			}
		}
		// se i minimi non sono stati trovati, allora si riparte
		if (minYbg < 0) { minYbg = 0; }
		if (minXbg == width) { minXbg = 0; }
		// ricerca dei massimi
		notFound = true;
		for (r = height - 1; (r >= minYbg); r--) {
			notFound = true;
			// riga = matricePixelOriginal[r];
			for (c = width - 1; (c >= minXbg && notFound); c--) {
				// notFound = ! ( (riga[c] == colBg) || (riga[c] ==
				// castAssemblyRGBandAlphaToARGB( colBg, alpha) ) ) ;

				// notFound = ! ( isColorsEqual ( new Color( riga[c] ) , cbg ) )
				// ;

				// notFound = ! ( isColorsEqual ( new Color( bi.getRGB(c, r) ) ,
				// cbg ) ) ;
				// notFound = ! ( isColorsEqual ( new Color(
				// castAssemblyRGBandAlphaToARGB( bi.getRGB(c, r), bi.getRGB(c,
				// r) ) ) , cbg ) ) ;

				notFound = !(isColorsEqual(new Color(bi.getRGB(c, r)), cbg) || isColorsEqual(
						new Color(castAssemblyRGBandAlphaToARGB(bi.getRGB(c, r), bi.getRGB(c, r))), cbg));

				// notFound = ! ( ( deleteAlpha(riga[c]) == colBg) || (
				// deleteAlpha(riga[c]) == castAssemblyRGBandAlphaToARGB( colBg,
				// alpha) ) ) ;
			}
			if (!notFound) {
				if (maxXbg < c || maxXbg < minXbg) { maxXbg = c; }
				if (maxYbg == height) { maxYbg = r; }
			}
		}
		if (maxYbg == height || maxYbg < minYbg) { maxYbg = height - 1; }
		if (maxXbg < 0 || maxXbg < minXbg) { maxXbg = width - 1; }
		// System.out.println( "\t\t\t minXbg = " + minXbg + " minYbg = " +
		// minYbg + " maxXbg = " + maxXbg + "maxYbg = " + maxYbg );
	}

	private static int[][] getMatrixPixel_New() {
		int[][] ret = null, tempMat;
		initialize();
		int[][][] referenceMp = { mp_FirstOfFive, mp_SecondOfFive, mp_ThirdOfFive, mp_FourthOfFive, mp_FifthOfFive };
		int r, c, t;
		heightOriginal = 0;
		for (r = 0; r < referenceMp.length; r++) {
			heightOriginal += referenceMp[r].length;
		}
		ret = new int[heightOriginal][];
		int[] rigaRet, rigaMp;
		t = r = 0;
		int rRet = 0;
		widthOriginal = referenceMp[0][0].length;
		while (t < referenceMp.length) { // per ogni matrice 2D
			tempMat = referenceMp[t]; // le singole matrici
			r = 0; // indice delle righe delle matrici frammentate a 5
			while (r < tempMat.length) { // per ogni riga di una matrice 2D
											// fissata
				rigaMp = tempMat[r];
				rigaRet = ret[rRet] = new int[widthOriginal];
				// rigaRet = ret[ rRet ] = new int[ widthOriginal = (rigaMp =
				// tempMat[r]).length ] ;
				for (c = 0; c < rigaMp.length; c++) { // per ogni colonna della
														// riga fissata
					rigaRet[c] = rigaMp[c];
				}
				r++;
				rRet++;
			}
			t++;
		}
		nullify();
		return ret;
	}

	private static int[][] mp_FirstOfFive, mp_SecondOfFive, mp_ThirdOfFive, mp_FourthOfFive, mp_FifthOfFive;

	private static void initialize() {
		if (mp_FirstOfFive == null) { mp_FirstOfFive = get_matricePixel_FirstOfFive(); }
		if (mp_SecondOfFive == null) { mp_SecondOfFive = get_matricePixel_SecondOfFive(); }
		if (mp_ThirdOfFive == null) { mp_ThirdOfFive = get_matricePixel_ThirdOfFive(); }
		if (mp_FourthOfFive == null) { mp_FourthOfFive = get_matricePixel_FourthOfFive(); }
		if (mp_FifthOfFive == null) {
			mp_FifthOfFive = get_matricePixel_FifthOfFive();
			// matricePixel = getMatrixPixel_New();
		}
	}

	public static boolean isTheColorTheSameOfTheBorder(Color col) {
		boolean notPresent = col == null;
		if (notPresent) {
			for (int i = 0; i < partsOfBorder.length && notPresent; i++) {
				notPresent = isColorsEqual(partsOfBorder[i], col);
			}
		}
		return notPresent;
	}

	private void initializePixelMatrix() {
		if (matricePixelOriginal == null) {
			matricePixelOriginal = getMatrixPixel_New();
			// getMatriceCopy(matricePixelOriginal);
			height = heightOriginal; // matricePixel.length;
			width = widthOriginal;
		}
		// if( bi == null ) bi = castMatrixBufferedImage(matricePixel,
		// BufferedImage.TYPE_INT_ARGB );
	}

	private void initializeBufferedImage() {
		if (matricePixelOriginal == null) {
			matricePixelOriginal = getMatrixPixel_New();
			// getMatriceCopy(matricePixelOriginal);
			height = heightOriginal; // matricePixel.length;
			width = widthOriginal;
		}
		if (bi == null) { bi = castMatrixBufferedImage(matricePixelOriginal, BufferedImage.TYPE_INT_ARGB); }
	}

	private static void nullify() {
		mp_FirstOfFive = mp_SecondOfFive = mp_ThirdOfFive = mp_FourthOfFive = mp_FifthOfFive = null;
		System.gc();
		// matricePixel = getMatrixPixel_New();
	}

	/*
	 * class JLBProgBar extends JLabel {
	 *
	 * @Override public void paintComponent(Graphics g) { Graphics2D g2 =
	 * (Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	 * RenderingHints.VALUE_ANTIALIAS_ON); AffineTransform aT = g2.getTransform();
	 * Shape oldshape = g2.getClip(); double xxx = this.getWidth()/2.0; double yyy =
	 * this.getHeight()/2.0; aT.rotate(Math.toRadians(angDeg) ,xxx, yyy); // x e y
	 * sono le coordinate relative all'origine di default, aventi coordinate
	 * P(getX(), getY()) g2.setTransform(aT); g2.setClip(oldshape);
	 * super.paintComponent(g); }
	 *
	 * @Override public void setSize( int width, int height) { super.setSize(width,
	 * height); setSizeProgBar( jl.getWidth(), jl.getHeight() ); } }
	 */
	public static final int[][] get_matricePixel_FirstOfFive() {
		return new int[][] { new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618, -1316618,
				-1316618, -1316618, -1316618, -2106895, -2106895, -4147485, -4147485, -3620889, -3620889, -3620889,
				-3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618, -4147485, -3620889,
						-3620889, -4147485, -4147485, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-1316874, -1316874, -1316874, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -1316618, -1316618, -4476703, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-4081692, -4081692, -4081692, -4081692, -4081692, -4081692, -4081692, -3620889, -3620889,
						-4147485, -4147485, -4147485, -4410911, -4410911, -1645836, -1645836, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -1316618, -1316618, -4213277, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -4015900, -4476703, -4476703, -4476703,
						-2106895, -2106895, -2106895, -2106895, -2106895, -2106895, -2106895, -4476703, -4476703,
						-4476703, -4015900, -4015900, -3686682, -3686682, -4147485, -4147485, -4344862, -1579788,
						-1579788, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-1316618, -4213277, -3620889, -3620889, -3686682, -3620889, -3620889, -3686682, -3620889,
						-1316618, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -2238480, -2238480, -4476703, -4081948, -4015900, -3620889, -3686682, -4147485,
						-4213277, -1448203, -1448203, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618,
						-4213277, -3620889, -3620889, -3620889, -3620889, -3620889, -2106895, -1316618, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -1843214, -2370065, -4476703, -3686938, -3620889,
						-3686938, -4147741, -4147741, -4081692, -1316618, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618, -4081692,
						-3620889, -3620889, -3686938, -3620889, -3752730, -2106895, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -921863, -921863, -921863, -921863,
						-921863, -921863, -921863, -921863, -921863, -921863, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2501650, -4410911, -3950108, -3620889,
						-3620889, -3752474, -4213277, -921863, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1053448, -4213277, -3752474,
						-3620889, -3620889, -3950108, -2106895, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3357464, -3884059, -3884059, -4213277, -4213277, -4213277,
						-4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -4147485, -3884059,
						-3884059, -592389, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-2567698, -4015900, -3620889, -3620889, -3620889, -4213277, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, -790278, -4213277, -3620889, -3620889,
						-3620889, -4015900, -2567698, 16777215, 16777215, 16777215, 16777215, 16777215, 2147220221,
						-3225879, -3752474, -3752474, -3884059, -3752730, -3818523, -3950107, -4015900, -4410910,
						-4410910, -4410910, -4410910, -4410910, -4410910, -4015900, -3950107, -3950108, -3752730,
						-3752730, -4213533, -4147485, -3752474, 2147220221, 16777215, 16777215, 16777215, 16777215,
						16777215, -2238224, -4410910, -3620889, -3620889, -3620889, -3752730, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, -921863, -3752730, -3620889, -3620889, -3620889,
						-4410910, -2238224, 16777215, 16777215, 16777215, 16777215, 16777215, -3159830, -4279069,
						-3884059, -3818266, -3818266, -3949851, -4410654, -4081693, -2830868, -2304273, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2304273, -2830868, -2830868, -4410654,
						-3949851, -3620889, -3686682, -3818266, -4279069, -3159830, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -4015900, -3620889, -3620889, -3620889, -4213533, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, -921863, -4213533, -3620889, -3620889, -3620889, -4015900,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3489305, -3884315, -3620889,
						-3620889, -3620889, -3620889, -2962453, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-2962453, -4345118, -3620889, -3620889, -3620889, -3884315, -3489305, 16777215, 16777215,
						16777215, 16777215, 16777215, -2435858, -3620889, -3620889, -3620889, -3620889, -592389,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, -921863, -3620889, -3620889, -3620889, -3620889, -4213533,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3489305, -3884315, -3620889,
						-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -4345118, -3620889, -3620889, -3620889, -3818523, -2896661, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -4213533,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, -3159830, -3620889, -3620889, -3620889, -3620889, -921863,
						16777215, 16777215, 16777215, 16777215, 16777215, -3818523, -3620889, -3620889, -3620889,
						-4344862, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3291671,
						-4147485, -4279326, -4279326, -4279326, -4279326, -3489304, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3949851, 16777215,
						16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889, -4213533,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, -921863, -3159830, -3620889, -3620889, -3620889, -4213533, -921863,
						16777215, 16777215, 16777215, 16777215, -790278, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4344862, -3884059,
						-3686682, -3620889, -3818523, -4279326, -4279326, -3489304, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215,
						16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, -921863, -3620889, -3620889, -3620889, -3620889, -3159830, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -2830868,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4345118, -3620889, -3620889,
						-3620889, -3620889, -3291671, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3818523, 16777215,
						16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889, -4213533,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, -921863, -3620889, -3620889, -3620889, -3620889, -3159830, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -2830868,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3686682, -3620889, -3620889,
						-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -2435858, -4345118, -3620889, -3620889, -3620889, -3818267, -3489305, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -4213533,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, -921863, -3620889, -3620889, -3620889, -3620889, -3159830, 16777215,
						16777215, 16777215, 16777215, 16777215, -3818266, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4147485, -3818266, -3620889,
						-3620889, -3620889, -4081693, -2304273, 16777215, 16777215, 16777215, 16777215, -2830868,
						-4081693, -4015900, -3620889, -3620889, -3818266, -4279069, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889, -4213533, -592389,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 } };
	}

	public static final int[][] get_matricePixel_SecondOfFive() {
		return new int[][] { new int[] { 16777215, 16777215, -1185288, -3159830, -3620889, -3620889, -3620889, -3159830,
				-921863, 16777215, 16777215, 16777215, 16777215, -790278, -3620889, -3620889, -3620889, -3620889,
				-4081693, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3752474, -4213533, -3752730,
				-3620889, -3686682, -4015900, -4410910, -4410910, -4410910, -4410910, -3950107, -3686682, -3752730,
				-3752730, -4213533, -3752474, 2147220221, 16777215, 16777215, 16777215, 16777215, 16777215, -2238224,
				-3950107, -3620889, -3620889, -4213533, -592389, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889,
				-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, -3159830, -3620889, -3620889, -3620889, -3620889, -921863,
						16777215, 16777215, 16777215, 16777215, -921863, -4213277, -3620889, -3620889, -3620889,
						-3686938, -2567698, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -592389,
						-3884059, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277,
						-4213277, -3884059, -3884059, -592389, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -2567698, -4015900, -3620889, -3620889, -3620889, -592389, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316874, -1316874,
						-1316874, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -1316874, -1316874, -1316874, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316874, -1316874,
						-1316874, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -1316874, -1316874, -1316874, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, -1185288, -3620889, -3620889, -3620889, -3620889, -4213533,
						-921863, 16777215, 16777215, 16777215, 16777215, 16777215, -4213277, -3752474, -3620889,
						-3620889, -3950108, -4081693, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -921863, -921863, -921863, -921863, -921863, -921863, -921863, -921863, -921863,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4081693,
						-3950108, -3620889, -3620889, -3752474, -4213277, -921863, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -1645836, -4410911, -4410911, -4147485, -4147485, -4147485,
						-3620889, -3620889, -4081692, -4081692, -4081692, -4081692, -4081692, -4081692, -4081692,
						-3620889, -3620889, -4147485, -4147485, -4147485, -4410911, -4410911, -1645836, -1645836,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -1645836, -4410911, -4410911, -4147485, -4147485, -4147485,
						-3620889, -3620889, -4081692, -4081692, -4081692, -4081692, -4081692, -4081692, -4081692,
						-3620889, -3620889, -4147485, -4147485, -4147485, -4410911, -4410911, -1645836, -1645836,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, -921863, -4213533, -3620889, -3620889, -3620889, -3620889,
						-921863, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618, -4081692, -3818266,
						-3620889, -3620889, -3686938, -4476703, -2370065, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1843214, -2370065, -4476703,
						-3686938, -3620889, -3686938, -4147741, -4081692, -1316618, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -1579788, -3818267, -4344862, -4147485, -3686682, -3686682, -4015900, -4015900,
						-4476703, -4476703, -4476703, -2106895, -2106895, -2106895, -2106895, -2106895, -2106895,
						-2106895, -4476703, -4476703, -4476703, -4015900, -4015900, -3686682, -3686682, -4147485,
						-4147485, -4344862, -1579788, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -1579788, -3818267, -4344862, -4147485, -3686682, -3686682, -4015900, -4015900,
						-4476703, -4476703, -4476703, -2106895, -2106895, -2106895, -2106895, -2106895, -2106895,
						-2106895, -4476703, -4476703, -4476703, -4015900, -4015900, -3686682, -3686682, -4147485,
						-4147485, -4344862, -1579788, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, -1185288, -3620889, -3620889, -3620889, -3620889,
						-4213533, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3686682,
						-4147485, -3752730, -3686682, -3620889, -4015900, -4476703, -2238480, -2238480, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -2238480, -2238480, -4476703, -4081948, -4015900, -3620889,
						-3686682, -4147485, -4213277, -1448203, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-3686682, -4147485, -3752730, -3686682, -3620889, -4015900, -4476703, -2238480, -2238480,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -2238480, -2238480, -4476703, -4081948, -4015900,
						-3620889, -3686682, -4147485, -4213277, -1448203, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-3686682, -4147485, -3752730, -3686682, -3620889, -4015900, -4476703, -2238480, -2238480,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -2238480, -2238480, -4476703, -4081948, -4015900,
						-3620889, -3686682, -4147485, -4213277, -1448203, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, -921863, -4213533, -3620889, -3620889, -3620889,
						-3620889, -4213533, -921863, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-1579788, -3818267, -4344862, -4147485, -3686682, -3686682, -4015900, -4015900, -4476703,
						-4476703, -4476703, -2106895, -2106895, -2106895, -2106895, -2106895, -2106895, -2106895,
						-4476703, -4476703, -4476703, -4015900, -4015900, -3686682, -3686682, -4147485, -4147485,
						-4344862, -1579788, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618, -4081692,
						-3818266, -3620889, -3620889, -3686938, -4476703, -2370065, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1843214, -2370065,
						-4476703, -3686938, -3620889, -3686938, -4147741, -4081692, -1316618, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618, -4081692,
						-3818266, -3620889, -3620889, -3686938, -4476703, -2370065, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1843214, -2370065,
						-4476703, -3686938, -3620889, -3686938, -4147741, -4081692, -1316618, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, -1185288, -4213533, -3620889, -3620889,
						-3620889, -3620889, -4213533, -921863, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -1645836, -4410911, -4410911, -4147485, -4147485, -4147485,
						-3620889, -3620889, -4081692, -4081692, -4081692, -4081692, -4081692, -4081692, -4081692,
						-3620889, -3620889, -4147485, -4147485, -4147485, -4410911, -4410911, -1645836, -1645836,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1185033, -4213277, -3752474,
						-3620889, -3620889, -3950108, -4081693, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033,
						-1185033, -1185033, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -4081693, -3950108, -3620889, -3620889, -3752474, -4213277, -1185033, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1185033, -4213277, -3752474,
						-3620889, -3620889, -3950108, -4081693, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033,
						-1185033, -1185033, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -4081693, -3950108, -3620889, -3620889, -3752474, -4213277, -1185033, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1185288, -4213533, -3620889,
						-3620889, -3620889, -3620889, -4213533, -921863, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316874, -1316874, -1316874,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -1316874, -1316874, -1316874, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4213277, -3620889, -3620889,
						-3620889, -3686938, -2567698, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-1118985, -3884059, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277,
						-4213277, -4213277, -3884059, -3884059, -1118985, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -2567698, -4015900, -3620889, -3620889, -3620889, -1118985, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4213277, -3620889, -3620889,
						-3620889, -3686938, -2567698, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-1118985, -3884059, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277,
						-4213277, -4213277, -3884059, -3884059, -1118985, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -2567698, -4015900, -3620889, -3620889, -3620889, -1118985, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -921863, -4213533,
						-3620889, -3620889, -3620889, -3620889, -3620889, -4213533, -921863, -921863, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-3620889, -4081693, 16777215, 16777215, 16777215, 16777215, 16777215, -658438, -3752474,
						-4213533, -3752730, -3620889, -3686682, -4015900, -4410910, -4410910, -4410910, -4410910,
						-3950107, -3686682, -3752730, -3752730, -4213533, -3752474, -987400, 16777215, 16777215,
						16777215, 16777215, 16777215, -2238224, -3950107, -3620889, -3620889, -4213533, -1118985,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-3620889, -4081693, 16777215, 16777215, 16777215, 16777215, 16777215, -658438, -3752474,
						-4213533, -3752730, -3620889, -3686682, -4015900, -4410910, -4410910, -4410910, -4410910,
						-3950107, -3686682, -3752730, -3752730, -4213533, -3752474, -987400, 16777215, 16777215,
						16777215, 16777215, 16777215, -2238224, -3950107, -3620889, -3620889, -4213533, -1118985,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -921863,
						-4213533, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -4213533, -4213533,
						-921863, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3818266, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4147485, -3818266,
						-3620889, -3620889, -3620889, -4081693, -2304273, 16711422, 16711422, 16711422, 16711422,
						-2830868, -4081693, -4015900, -3620889, -3620889, -3818266, -4279069, -3620889, 16777215,
						16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889, -4213533,
						-1118985, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3818266, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4147485, -3818266,
						-3620889, -3620889, -3620889, -4081693, -2304273, 16711422, 16711422, 16711422, 16711422,
						-2830868, -4081693, -4015900, -3620889, -3620889, -3818266, -4279069, -3620889, 16777215,
						16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889, -4213533,
						-1118985, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -921863, -4213533, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -4213533, -921863, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3686682, -3620889,
						-3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777214, -2435858, -4345118, -3620889, -3620889, -3620889, -3818267, -3489305,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-4213533, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3686682, -3620889,
						-3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777214, -2435858, -4345118, -3620889, -3620889, -3620889, -3818267, -3489305,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-4213533, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -921863, -4213533, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4345118, -3620889,
						-3620889, -3620889, -3620889, -3291671, -658181, -658181, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 2147220221, -3620889, -3620889, -3620889, -3620889, -3818523,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-4213533, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4345118, -3620889,
						-3620889, -3620889, -3620889, -3291671, -658181, -658181, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 2147220221, -3620889, -3620889, -3620889, -3620889, -3818523,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-4213533, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3950107, -3620889, -3620889, -3620889,
						-3620889, -526597, 16777215, 16777215, 16777215, 16777215, 16777215, -395012, -4344862,
						-3884059, -3686682, -3620889, -3818523, -4279326, -4279326, -3489304, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3950107, -3620889, -3620889, -3620889,
						-3620889, -526597, 16777215, 16777215, 16777215, 16777215, 16777215, -395012, -4344862,
						-3884059, -3686682, -3620889, -3818523, -4279326, -4279326, -3489304, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2765076, -3818523, -3620889, -3620889,
						-3620889, -4344862, -395012, 16777215, 16777215, 16777215, 16777215, 16777215, -526597,
						-3291671, -4147485, -4279326, -4279326, -4279326, -4279326, -3489304, 16777215, 16777215,
						16777215, 16777215, 16777215, -395012, -3620889, -3620889, -3620889, -3620889, -3949851,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-4213533, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2765076, -3818523, -3620889, -3620889,
						-3620889, -4344862, -395012, 16777215, 16777215, 16777215, 16777215, 16777215, -526597,
						-3291671, -4147485, -4279326, -4279326, -4279326, -4279326, -3489304, 16777215, 16777215,
						16777215, 16777215, 16777215, -395012, -3620889, -3620889, -3620889, -3620889, -3949851,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-4213533, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3423256, -3818523, -3620889,
						-3620889, -3620889, -4345118, 2147220221, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -395012, -658181, -658181, -658181, -658181, 16777215, 16777215, 16777215, 16777215,
						16777215, 2147220221, -4345118, -3620889, -3620889, -3620889, -3818523, -2896661, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -4213533,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3423256, -3818523, -3620889,
						-3620889, -3620889, -4345118, 2147220221, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -395012, -658181, -658181, -658181, -658181, 16777215, 16777215, 16777215, 16777215,
						16777215, 2147220221, -4345118, -3620889, -3620889, -3620889, -3818523, -2896661, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -4213533,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-3489305, -3884315, -3620889, -3620889, -3620889, -4345118, -2962453, 16579838, 16777214,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16579838, -2962453, -4345118, -3620889, -3620889, -3620889, -3884315,
						-3489305, 16777215, 16777215, 16777215, 16777215, 16777215, -2435858, -3620889, -3620889,
						-3620889, -3620889, -1118985, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3489305, -3884315, -3620889, -3620889, -3620889, -4345118,
						-2962453, 16579838, 16777214, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16579838, -2962453, -4345118, -3620889,
						-3620889, -3620889, -3884315, -3489305, 16777215, 16777215, 16777215, 16777215, 16777215,
						-2435858, -3620889, -3620889, -3620889, -3620889, -1118985, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3159830, -4279069, -3884059, -3818266, -3620889, -3949851, -4410654, -4081693,
						-2830868, -2304273, 16711422, 16711422, 16711422, 16711422, 16711422, 16711422, -2304273,
						-2830868, -2830868, -4410654, -3949851, -3620889, -3686682, -3818266, -4279069, -3159830,
						16777215, 16777215, 16777215, 16777215, 16777215, 16711422, -4015900, -3620889, -3620889,
						-3620889, -4213533, -855815, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3159830, -4279069, -3884059, -3818266, -3620889,
						-3949851, -4410654, -4081693, -2830868, -2304273, 16711422, 16711422, 16711422, 16711422,
						16711422, 16711422, -2304273, -2830868, -2830868, -4410654, -3949851, -3620889, -3686682,
						-3818266, -4279069, -3159830, 16777215, 16777215, 16777215, 16777215, 16777215, 16711422,
						-4015900, -3620889, -3620889, -3620889, -4213533, -855815, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215 },
				new int[] { 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -987400, -3225879, -3752474, -4213533, -3884059, -3752730,
						-3818523, -3950107, -4015900, -4410910, -4410910, -4410910, -4410910, -4410910, -4410910,
						-4015900, -3950107, -3950108, -3752730, -3752730, -4213533, -4147485, -3752474, -987400,
						16777215, 16777215, 16777215, 16777215, 16777215, -2238224, -4410910, -3620889, -3620889,
						-3620889, -3752730, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -987400, -3225879,
						-3752474, -4213533, -3884059, -3752730, -3818523, -3950107, -4015900, -4410910, -4410910,
						-4410910, -4410910, -4410910, -4410910, -4015900, -3950107, -3950108, -3752730, -3752730,
						-4213533, -4147485, -3752474, -987400, 16777215, 16777215, 16777215, 16777215, 16777215,
						-2238224, -4410910, -3620889, -3620889, -3620889, -3752730, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215 } };
	}

	private static final int[][] get_matricePixel_ThirdOfFive() {
		return new int[][] { new int[] { 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
				-3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
				-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
				-3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, -1118985, -3357464, -3884059, -3884059, -4213277, -4213277, -4213277, -4213277, -4213277,
				-4213277, -4213277, -4213277, -4213277, -4213277, -4147485, -3884059, -3884059, -1118985, -789766,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -2567698, -4015900, -3620889, -3620889,
				-3620889, -4213277, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
				-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
				-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
				-3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, -1118985, -3357464, -3884059, -3884059, -4213277, -4213277, -4213277, -4213277,
				-4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -4147485, -3884059, -3884059, -1118985,
				-789766, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -2567698, -4015900, -3620889,
				-3620889, -3620889, -4213277, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, 16777215 },
				new int[] { 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033,
						-1185033, -1185033, -1185033, -1185033, -855815, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -2501650, -4410911, -3950108, -3620889, -3620889,
						-3752474, -4213277, -1185033, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1185033,
						-1185033, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033,
						-855815, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-2501650, -4410911, -3950108, -3620889, -3620889, -3752474, -4213277, -1185033, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -2370065, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -2370065, -4476703, -4015900, -3620889, -3620889, -3686938, -4147741,
						-4081692, -1316618, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-2370065, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -2370065, -4476703,
						-4015900, -3620889, -3620889, -3686938, -4147741, -4081692, -1316618, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, -1185033, -1185033, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -2238480, -2238480,
						-2238480, -4476703, -4476703, -4015900, -3620889, -3686682, -3752730, -4147485, -4213277,
						-1448203, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215,
						-1185033, -1185033, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -2238480, -2238480, -2238480, -4476703, -4476703,
						-4015900, -3620889, -3686682, -3752730, -4147485, -4213277, -1448203, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3686682, -4476703, -4476703, -4476703, -4476703, -4476703,
						-4476703, -3620889, -2106895, -2106895, -2106895, -2106895, -4476703, -4081693, -4476703,
						-4476703, -4015900, -4015900, -3686682, -3686682, -3752474, -4147485, -4344862, -3818267,
						-1579788, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3686682, -4476703, -4476703, -4476703, -4476703, -4476703, -4476703, -3620889, -2106895,
						-2106895, -2106895, -2106895, -4476703, -4081693, -4476703, -4476703, -4015900, -4015900,
						-3686682, -3686682, -3752474, -4147485, -4344862, -3818267, -1579788, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3686682, -3620889, -3620889, -3620889, -3620889, -3620889, -4476703, -4476703,
						-4476703, -4476703, -4081692, -4081692, -4476703, -4476703, -4476703, -4476703, -4476703,
						-3752474, -4147485, -4147485, -4410911, -4410911, -3949852, -1645836, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3686682,
						-3620889, -3620889, -3620889, -3620889, -3620889, -4476703, -4476703, -4476703, -4476703,
						-4081692, -4081692, -4476703, -4476703, -4476703, -4476703, -4476703, -3752474, -4147485,
						-4147485, -4410911, -4410911, -3949852, -1645836, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -4476703, -4476703, -4476703, -4476703, -4476703, -4476703, -3620889, -3620889,
						-3225879, -1316874, -1316874, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-4476703, -4476703, -4476703, -4476703, -4476703, -4476703, -3620889, -3620889, -3225879,
						-1316874, -1316874, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889 } };
	}

	private static final int[][] get_matricePixel_FourthOfFive() {
		return new int[][] { new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -1316874, -1316874, -3225879, -3620889, -3620889, -4476703, -4476703,
						-4476703, -4476703, -4476703, -4476703, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -1316874, -1316874, -3225879, -3620889, -3620889, -4476703, -4476703, -4476703,
						-4476703, -4476703, -4476703, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1645836, -3949852,
						-4410911, -4410911, -4147485, -4147485, -3752474, -4476703, -4476703, -4476703, -4476703,
						-4476703, -4081692, -4081692, -4476703, -4476703, -4476703, -4476703, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3686682, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -1645836, -3949852, -4410911, -4410911,
						-4147485, -4147485, -3752474, -4476703, -4476703, -4476703, -4476703, -4476703, -4081692,
						-4081692, -4476703, -4476703, -4476703, -4476703, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3686682, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -1579788, -3818267, -4344862, -4147485, -3752474,
						-3686682, -3686682, -4015900, -4015900, -4476703, -4476703, -4081693, -4476703, -2106895,
						-2106895, -2106895, -2106895, -3620889, -4476703, -4476703, -4476703, -4476703, -4476703,
						-4476703, -3686682, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -1579788, -3818267, -4344862, -4147485, -3752474, -3686682, -3686682, -4015900,
						-4015900, -4476703, -4476703, -4081693, -4476703, -2106895, -2106895, -2106895, -2106895,
						-3620889, -4476703, -4476703, -4476703, -4476703, -4476703, -4476703, -3686682, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -1448203, -4213277, -4147485, -3752730, -3686682, -3620889, -4015900,
						-4476703, -4476703, -2238480, -2238480, -2238480, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1185033, -1185033,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1448203,
						-4213277, -4147485, -3752730, -3686682, -3620889, -4015900, -4476703, -4476703, -2238480,
						-2238480, -2238480, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -1185033, -1185033, 16777215, 16777215, 16777215,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-1316618, -4081692, -4147741, -3686938, -3620889, -3620889, -4015900, -4476703, -2370065,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -2370065, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618, -4081692, -4147741,
						-3686938, -3620889, -3620889, -4015900, -4476703, -2370065, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -2370065, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1185033,
						-4213277, -3752474, -3620889, -3620889, -3950108, -4410911, -2501650, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -855815, -1185033, -1185033,
						-1185033, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -1185033, -4213277, -3752474, -3620889, -3620889,
						-3950108, -4410911, -2501650, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -855815, -1185033, -1185033, -1185033, -1185033, -1185033, -1185033,
						-1185033, -1185033, -1185033, -1185033, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889 },
				new int[] { 16777215, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -4213277,
						-3620889, -3620889, -3620889, -4015900, -2567698, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -789766, -1118985, -3884059, -3884059, -4147485, -4213277, -4213277,
						-4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277, -3884059,
						-3884059, -3357464, -1118985, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -4213277, -3620889, -3620889, -3620889, -4015900,
						-2567698, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -789766, -1118985,
						-3884059, -3884059, -4147485, -4213277, -4213277, -4213277, -4213277, -4213277, -4213277,
						-4213277, -4213277, -4213277, -4213277, -3884059, -3884059, -3357464, -1118985, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, 16777215 } };
	}

	private static final int[][] get_matricePixel_FifthOfFive() {
		return new int[][] { new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3752730, -3620889, -3620889,
				-3620889, -4410910, -2238224, 16777215, 16777215, 16777215, 16777215, 16777215, -987400, -3752474,
				-4147485, -4213533, -3752730, -3752730, -3950108, -3950107, -4015900, -4410910, -4410910, -4410910,
				-4410910, -4410910, -4410910, -4015900, -3950107, -3818523, -3752730, -3884059, -4213533, -3752474,
				-3225879, -987400, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889,
				-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
				-3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
				-3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
				16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3752730, -3620889, -3620889,
				-3620889, -4410910, -2238224, 16777215, 16777215, 16777215, 16777215, 16777215, -987400, -3752474,
				-4147485, -4213533, -3752730, -3752730, -3950108, -3950107, -4015900, -4410910, -4410910, -4410910,
				-4410910, -4410910, -4410910, -4015900, -3950107, -3818523, -3752730, -3884059, -4213533, -3752474,
				-3225879, -987400, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889,
				-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
				-3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
				-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889,
				-3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -855815, -4213533, -3620889, -3620889,
						-3620889, -4015900, 16711422, 16777215, 16777215, 16777215, 16777215, 16777215, -3159830,
						-4279069, -3818266, -3686682, -3620889, -3949851, -4410654, -2830868, -2830868, -2304273,
						16711422, 16711422, 16711422, 16711422, 16711422, 16711422, -2304273, -2830868, -4081693,
						-4410654, -3949851, -3620889, -3818266, -3884059, -4279069, -3159830, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -855815,
						-4213533, -3620889, -3620889, -3620889, -4015900, 16711422, 16777215, 16777215, 16777215,
						16777215, 16777215, -3159830, -4279069, -3818266, -3686682, -3620889, -3949851, -4410654,
						-2830868, -2830868, -2304273, 16711422, 16711422, 16711422, 16711422, 16711422, 16711422,
						-2304273, -2830868, -4081693, -4410654, -3949851, -3620889, -3818266, -3884059, -4279069,
						-3159830, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -1118985, -3620889, -3620889, -3620889,
						-3620889, -2435858, 16777215, 16777215, 16777215, 16777215, 16777215, -3489305, -3884315,
						-3620889, -3620889, -3620889, -4345118, -2962453, 16579838, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777214,
						16579838, -2962453, -4345118, -3620889, -3620889, -3620889, -3884315, -3489305, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1118985,
						-3620889, -3620889, -3620889, -3620889, -2435858, 16777215, 16777215, 16777215, 16777215,
						16777215, -3489305, -3884315, -3620889, -3620889, -3620889, -4345118, -2962453, 16579838,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777214, 16579838, -2962453, -4345118, -3620889, -3620889, -3620889,
						-3884315, -3489305, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215,
						16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -4213533, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, -2896661, -3818523, -3620889,
						-3620889, -3620889, -4345118, 2147220221, 16777215, 16777215, 16777215, 16777215, 16777215,
						-658181, -658181, -658181, -658181, -395012, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 2147220221, -4345118, -3620889, -3620889, -3620889, -3818523, -3423256, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -4213533, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, -2896661, -3818523, -3620889,
						-3620889, -3620889, -4345118, 2147220221, 16777215, 16777215, 16777215, 16777215, 16777215,
						-658181, -658181, -658181, -658181, -395012, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 2147220221, -4345118, -3620889, -3620889, -3620889, -3818523, -3423256, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154, -16057154,
						-16057154, -16057154, -16057154, -16057154, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -4213533, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, -3949851, -3620889, -3620889,
						-3620889, -3620889, -395012, 16777215, 16777215, 16777215, 16777215, 16777215, -3489304,
						-4279326, -4279326, -4279326, -4279326, -4147485, -3291671, -526597, 16777215, 16777215,
						16777215, 16777215, 16777215, -395012, -4344862, -3620889, -3620889, -3620889, -3818523,
						-2765076, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -4213533, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, -3949851, -3620889, -3620889,
						-3620889, -3620889, -395012, 16777215, 16777215, 16777215, 16777215, 16777215, -3489304,
						-4279326, -4279326, -4279326, -4279326, -4147485, -3291671, -526597, 16777215, 16777215,
						16777215, 16777215, 16777215, -395012, -4344862, -3620889, -3620889, -3620889, -3818523,
						-2765076, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889,
						-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3489304,
						-4279326, -4279326, -3818523, -3620889, -3686682, -3884059, -4344862, -395012, 16777215,
						16777215, 16777215, 16777215, 16777215, -526597, -3620889, -3620889, -3620889, -3620889,
						-3950107, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889,
						-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3489304,
						-4279326, -4279326, -3818523, -3620889, -3686682, -3884059, -4344862, -395012, 16777215,
						16777215, 16777215, 16777215, 16777215, -526597, -3620889, -3620889, -3620889, -3620889,
						-3950107, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -4213533, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, -3818523, -3620889, -3620889,
						-3620889, -3620889, 2147220221, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-658181, -658181, -3291671, -3620889, -3620889, -3620889, -3620889, -4345118, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -4213533, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, -3818523, -3620889, -3620889,
						-3620889, -3620889, 2147220221, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-658181, -658181, -3291671, -3620889, -3620889, -3620889, -3620889, -4345118, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -4213533, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, -3489305, -3818267, -3620889,
						-3620889, -3620889, -4345118, -2435858, 16777214, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3686682, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -4213533, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, -3489305, -3818267, -3620889,
						-3620889, -3620889, -4345118, -2435858, 16777214, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889, -3686682, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -2830868, -3620889, -3620889, -3620889,
						-3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -1118985, -4213533, -3620889, -3620889,
						-3620889, -2830868, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -4279069,
						-3818266, -3620889, -3620889, -4015900, -4081693, -2830868, 16711422, 16711422, 16711422,
						16711422, -2304273, -4081693, -3620889, -3620889, -3620889, -3818266, -4147485, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3818266, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -1118985, -4213533, -3620889, -3620889,
						-3620889, -2830868, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -4279069,
						-3818266, -3620889, -3620889, -4015900, -4081693, -2830868, 16711422, 16711422, 16711422,
						16711422, -2304273, -4081693, -3620889, -3620889, -3620889, -3818266, -4147485, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889, -3620889,
						-3818266, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1118985, -4213533, -3620889,
						-3620889, -3950107, -2238224, 16777215, 16777215, 16777215, 16777215, 16777215, -987400,
						-3752474, -4213533, -3752730, -3752730, -3686682, -3950107, -4410910, -4410910, -4410910,
						-4410910, -4015900, -3686682, -3620889, -3752730, -4213533, -3752474, -658438, 16777215,
						16777215, 16777215, 16777215, 16777215, -4081693, -3620889, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1118985, -4213533, -3620889,
						-3620889, -3950107, -2238224, 16777215, 16777215, 16777215, 16777215, 16777215, -987400,
						-3752474, -4213533, -3752730, -3752730, -3686682, -3950107, -4410910, -4410910, -4410910,
						-4410910, -4015900, -3686682, -3620889, -3752730, -4213533, -3752474, -658438, 16777215,
						16777215, 16777215, 16777215, 16777215, -4081693, -3620889, -3620889, -3620889, -3620889,
						-2830868, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1118985, -3620889,
						-3620889, -3620889, -4015900, -2567698, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -1118985, -3884059, -3884059, -4213277, -4213277, -4213277, -4213277, -4213277,
						-4213277, -4213277, -4213277, -4213277, -3884059, -1118985, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -2567698, -3686938, -3620889, -3620889, -3620889, -4213277,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1118985, -3620889,
						-3620889, -3620889, -4015900, -2567698, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -1118985, -3884059, -3884059, -4213277, -4213277, -4213277, -4213277, -4213277,
						-4213277, -4213277, -4213277, -4213277, -3884059, -1118985, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -2567698, -3686938, -3620889, -3620889, -3620889, -4213277,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1185033, -4213277,
						-3752474, -3620889, -3620889, -3950108, -4081693, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -1185033, -1185033, -1185033, -1185033, -1185033,
						-1185033, -1185033, -1185033, -1185033, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -4081693, -3950108, -3620889, -3620889, -3752474, -4213277, -1185033,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1185033, -4213277,
						-3752474, -3620889, -3620889, -3950108, -4081693, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, -1185033, -1185033, -1185033, -1185033, -1185033,
						-1185033, -1185033, -1185033, -1185033, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, -4081693, -3950108, -3620889, -3620889, -3752474, -4213277, -1185033,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618,
						-4081692, -4147741, -3686938, -3620889, -3686938, -4476703, -2370065, -1843214, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-2370065, -4476703, -3686938, -3620889, -3620889, -3818266, -4081692, -1316618, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -1316618,
						-4081692, -4147741, -3686938, -3620889, -3686938, -4476703, -2370065, -1843214, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-2370065, -4476703, -3686938, -3620889, -3620889, -3818266, -4081692, -1316618, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -1448203, -4213277, -4147485, -3686682, -3620889, -4015900, -4081948, -4476703,
						-2238480, -2238480, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -2238480, -2238480, -4476703,
						-4015900, -3620889, -3686682, -3752730, -4147485, -3686682, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, -1448203, -4213277, -4147485, -3686682, -3620889, -4015900, -4081948, -4476703,
						-2238480, -2238480, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -2238480, -2238480, -4476703,
						-4015900, -3620889, -3686682, -3752730, -4147485, -3686682, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -1579788, -4344862, -4147485, -4147485, -3686682, -3686682,
						-4015900, -4015900, -4476703, -4476703, -4476703, -2106895, -2106895, -2106895, -2106895,
						-2106895, -2106895, -2106895, -4476703, -4476703, -4476703, -4015900, -4015900, -3686682,
						-3686682, -4147485, -4344862, -3818267, -1579788, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, -1579788, -4344862, -4147485, -4147485, -3686682, -3686682,
						-4015900, -4015900, -4476703, -4476703, -4476703, -2106895, -2106895, -2106895, -2106895,
						-2106895, -2106895, -2106895, -4476703, -4476703, -4476703, -4015900, -4015900, -3686682,
						-3686682, -4147485, -4344862, -3818267, -1579788, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -1645836, -1645836, -4410911, -4410911,
						-4147485, -4147485, -4147485, -3620889, -3620889, -4081692, -4081692, -4081692, -4081692,
						-4081692, -4081692, -4081692, -3620889, -3620889, -4147485, -4147485, -4147485, -4410911,
						-4410911, -1645836, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, -1645836, -1645836, -4410911, -4410911,
						-4147485, -4147485, -4147485, -3620889, -3620889, -4081692, -4081692, -4081692, -4081692,
						-4081692, -4081692, -4081692, -3620889, -3620889, -4147485, -4147485, -4147485, -4410911,
						-4410911, -1645836, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-1316874, -1316874, -1316874, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -1316874, -1316874, -1316874, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						-1316874, -1316874, -1316874, -3620889, -3620889, -3620889, -3620889, -3620889, -3620889,
						-3620889, -3620889, -3620889, -3620889, -3620889, -1316874, -1316874, -1316874, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 },
				new int[] { 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889,
						-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, -3620889, -3620889, -3620889,
						-3620889, -3620889, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215, 16777215,
						16777215, 16777215, 16777215, 16777215, 16777215 } };
	}

	// TAKEN FROM OTHER CLASSES IN THE PACKAGE .. I COPYIED THIS METHODS JUST TO
	// MAKE THIS CLASS INDIPENDENT

	// public static int deleteAlpha( int argb ) { return ((argb<<7)*2)>>8; }

	public static boolean isColorsEqual(Color c1, Color c2) {
		return (c1.getRed() == c2.getRed()) && (c1.getGreen() == c2.getGreen()) && (c1.getBlue() == c2.getBlue())
		// && ( c1.getAlpha() == c2.getAlpha() )
		;
	}

	public static int deleteAlpha(int argb) { return (argb << 8) >> 8; }

	public static int castAssemblyRGBandAlphaToARGB(int rgb, int alpha) {
		return ((rgb & 0x00FFFFFF)
				/*
				 * ((rgb & negateInt(255 << 24)) // setto l'ultimo byte a 0, cos' da
				 * "lasciare il posto" per l'alpha
				 */
				| (alpha << 24));
	}

	public static int negateInt(int n) { return n ^ 0xFFFFFFFF; }

	public static int castARGBbytesToARGBInt(byte alpha, byte red, byte green, byte blue) {
		return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 0) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 16);
	}

	public static int[][] getMatriceCopy(int[][] m) {
		int[][] ret = null;
		if (m != null) {
			int rigaM[], rigaRet[], r, c;
			ret = new int[m.length][];
			for (r = 0; r < m.length; r++) {
				rigaM = m[r];// prendo la r-esima riga dell'array da copiare
				if (rigaM != null) {
					rigaRet = ret[r] = new int[rigaM.length]; // prendo
																// l'erresima
																// riga
																// dell'array di
																// destinazione
					for (c = 0; c < rigaM.length; c++) {
						rigaRet[c] = rigaM[c];
					}
				}
			}
		}
		return ret;
	}

	public static BufferedImage getBufferedImageCopy(BufferedImage bi) {
		BufferedImage ret = null;
		if (bi != null) {
			int r, c, w = bi.getWidth(), h = bi.getHeight();
			ret = new BufferedImage(w, h, bi.getType());
			for (r = 0; r < h; r++) {
				for (c = 0; c < w; c++) {
					ret.setRGB(c, r, bi.getRGB(c, r));
				}
			}
		}
		return ret;
	}

	public static BufferedImage castMatrixBufferedImage_ARGB(int[][] matrix) {
		return castMatrixBufferedImage(matrix, BufferedImage.TYPE_INT_ARGB);
	}

	public static BufferedImage castMatrixBufferedImage(int[][] matrix, int type) {
		BufferedImage bufferedImage = null;
		if (matrix != null) {
			if (matrix[0] != null) {
				bufferedImage = new BufferedImage(matrix[0].length, matrix.length, type);
				int[] riga;
				for (int r = 0; r < matrix.length; r++) {
					if ((riga = matrix[r]) != null) {
						for (int c = 0; c < riga.length; c++) {
							bufferedImage.setRGB(c, r, riga[c]);
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

	public static BufferedImage getScaledInstance(BufferedImage m, int width, int height) {
		BufferedImage bif = null;
		int[][] ret = null;
		if (m != null) {
			if (m.getHeight() > 0 && 0 < m.getWidth()) {
				if (width > 0 && height > 0) {
					ret = new int[height][];
					double rm = 0, cm = 0, sw /* , sh */;
					int rRet = 0, cRet = 0;
					// sh = height / ((double)m.length) ;
					int[] tempRigaRet = null; // soluzione per ottimizzare gli
												// accessi in memoria delle
												// righe della matrice
					// sh = ((double)m.getHeight()) / height ; // ignoro di
					// calcolarmi sh per evitare errori di approssimazione
					// tipici del "floating point" a numero di cifre decimli
					// finite
					sw = width / ((double) m.getWidth());
					while (rRet < height) {
						tempRigaRet = ret[rRet] = new int[width];
						cRet = 0;
						cm = 0.0;
						while ((cRet < ret[rRet].length) && (cm < m.getWidth()) && ((rm) < m.getHeight())) {
							tempRigaRet[cRet] = m.getRGB((int) cm, (int) rm);
							// cm += 1.0 / sw ;
							cm = ++cRet / sw;
						}
						// rm += sh ;
						rm = ((double) (m.getHeight() * ++rRet)) / height;
					}
					// bif = CastingClass.castMatrixBufferedImage( ret );
					bif = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
					for (int r = 0; r < height; r++) {
						tempRigaRet = ret[r]; // fissata una riga, opero solo su
												// quella
						for (int c = 0; c < width; c++) {
							bif.setRGB(c, r, tempRigaRet[c]);
						}
					}
				}
			} else {
				bif = new BufferedImage(0, 0, BufferedImage.TYPE_INT_ARGB);
			}
		}
		return bif;
	}

	public static int[][] getScaledInstance(int[][] m, int width, int height) {
		int[][] ret = m;
		if (m != null) {
			if (m.length > 0) {
				if (width > 0 && height > 0 /* && isTheMatrixRectangular(m) */) {
					ret = new int[height][];
					double rm = 0, cm = 0;
					int rRet = 0, cRet = 0; // contatori - indice
					int[] tempRigaRet = null, tempRigaM; // soluzione per
															// ottimizzare gli
															// accessi in
															// memoria delle
															// righe della
															// matrice
					// sh = ((double)m.length) / height ; // � pi� utile
					while (rRet < height) {
						if ((tempRigaM = m[(int) rm]) != null) {
							tempRigaRet = ret[rRet] = new int[width];
							cRet = 0;
							cm = 0.0;
							while ((cRet < tempRigaRet.length) && (cm < tempRigaM.length)) {
								tempRigaRet[cRet] = tempRigaM[(int) cm];
								// cm += 1.0 / sw ;
								cm = ((double) (tempRigaM.length * ++cRet)) / ((double) width);
							}
						}
						// rm += sh ;
						rm = ((double) m.length * ++rRet) / (height);
					}
				}
			} else {
				ret = new int[0][0];
			}
		}
		return ret;
	}

	public boolean writeImage(String pathAndName) { return writeImage(pathAndName, bi); }

	public static boolean writeImage(String pathAndName, int[][] m) {
		boolean successInWriting = false;
		try {
			successInWriting = ImageIO.write(castMatrixBufferedImage_ARGB(m), "png", new File(pathAndName + ".png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return successInWriting;
	}

	// TODO
	public static boolean writeImage(int[][] m) {
		boolean successInWriting = false;
		try {
			String nomeImmagine = MyProgressBar.class.getName();
			try {
				nomeImmagine = nomeImmagine.substring(MyProgressBar.class.getPackage().getName().length() + 1,
						nomeImmagine.length());
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			successInWriting = ImageIO.write(castMatrixBufferedImage_ARGB(m), "png", new File(nomeImmagine + ".png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return successInWriting;
	}

	public static boolean writeImage(String pathAndName, BufferedImage bufim) {
		boolean successInWriting = false;
		try {
			successInWriting = ImageIO.write(bufim, "png", new File(pathAndName + ".png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return successInWriting;
	}

	// TODO
	public static boolean writeImage(BufferedImage bufim) {
		boolean successInWriting = false;
		try {
			String nomeImmagine = MyProgressBar.class.getName();
			try {
				nomeImmagine = nomeImmagine.substring(MyProgressBar.class.getPackage().getName().length() + 1,
						nomeImmagine.length());
			} catch (Exception exc) {
				exc.printStackTrace();
			}
			successInWriting = ImageIO.write(bufim, "png", new File(nomeImmagine + ".png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return successInWriting;
	}

	public void setPreferredSize(int wp, int hp) { super.setPreferredSize(new Dimension(wp, hp)); }

}