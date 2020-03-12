package tools.minorTools;

import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * 
 * This class provides a main method,
 * {@link #boundingPolygon_UnckeckedParameters(int[][], int, int)}, and some
 * utilities.<br>
 * The main method could be rewritten to accept any type of matrix (boolean ,
 * Object's pointer [<code>null</code] means <code>transparent</code>], ecc).
 * <br>
 * Please, read their javadoc: all of the required documentation is there. <br>
 * <br>
 * Forgive me for my bad English and the missing full translation of the
 * {@link #boundingPolygon_UnckeckedParameters(int[][], int, int)} 's javadoc
 * (i'm Italian), i'll do as soon as possible. <br>
 * <br>
 * Made 20-November-2015<br>
 * {@author Ottina Marco - ottins1995@gmail.com }
 */
public class BufferedImageBoundingPolygon {

	/** Instances are useless */
	private BufferedImageBoundingPolygon() {
	}

	/** Just for a fast use. */
	private static final CrossDirections[] valuesCrossDirections = CrossDirections.values();

	/**
	 * An enumeration enumerating a simple set of direction where the
	 * CrossWalker can move.<br>
	 * Their parameter are the delta of the x and y axes.
	 */
	public static enum CrossDirections {
		RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0), UP(0, -1);
		final int dx, dy, index;

		CrossDirections(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
			index = SF.n++;
		}

		private static class SF {
			static int n = 0;
		}
	}

	/*
	 * public static Polygon boundingPolygon(BufferedImage bi) { Polygon ret =
	 * null; if (bi != null) { PointList pl = null; Punto p, peekPoint, primo,
	 * tempPoint; int x = 0, y = 0, w = bi.getWidth(), h = bi.getHeight(); if (w
	 * > 0 && h > 0) {
	 * 
	 * } } return ret; }
	 */

	/**
	 * A simple method that check if the matrix border's are fully transparent
	 * (before it, the matrix must be accepted [rectangular, not-null,
	 * not-empty]).
	 * 
	 * @return false if in the border of the matrix there is at least one opaque
	 *         pixel, true otherwise.
	 */
	public static boolean boundsContainsTrasparentPixel(int[][] m) {
		boolean safe = true;
		if (m != null && m.length > 0 && isTheMatrixRectangular(m)) {
			int row1[], row2[], r = 0, c, l_1 = m.length - 1, w;
			// orizzontale
			w = (row1 = m[0]).length;
			row2 = m[l_1];
			c = -1;
			while (safe && ++c < w) {
				safe = isTrasparent(row1[c]) && isTrasparent(row2[c]);
			}
			/*
			 * se orizzontalmente, nelle righe più esterne non c'è un punto
			 * opaco, allora va bene siamo nel caso semplice
			 */
			if (safe) {
				c = w - 1;
				r = -1;
				while (safe && ++r <= l_1) {
					row1 = m[r];
					safe = isTrasparent(row1[0]) && isTrasparent(row1[c]);
				}
			} else {

			}
		}
		return !safe;
	}

	// TODO boundingPolygon(BufferedImage)
	/** Refer to {@link #boundingPolygon(int[][])} */
	public static Polygon boundingPolygon(BufferedImage m) {
		Polygon ret = null;
		if (m != null) {
			int w = m.getWidth(), h = m.getHeight();
			if (w > 0 && h > 0) {
				// roba
				PointList pl = null;
				LightWeightPoint p, peekPoint, primo, tempPoint;
				int x = 0, y = -1;
				boolean pointStartNotFound = true;
				while (pointStartNotFound && ++y < h) {
					x = -1;
					while (pointStartNotFound && ++x < w) {
						pointStartNotFound = isTrasparent(m.getRGB(x, y));
					}
				}
				if (!pointStartNotFound) {
					try {
						pl = new PointList(primo = new LightWeightPoint(x, y));
						if (isTrasparent(m, x + 1, y) == 1 && isTrasparent(m, x - 1, y) == 1
								&& isTrasparent(m, x, y + 1) == 1) {
							ret = new Polygon(new int[] { x }, new int[] { y }, 1);
							pl = null;
						} else {
							Direction d = new Direction();
							int[] xx, yy, mat[];
							(peekPoint = (p = primo.clone()).clone()).move(d.direction);
							do {
								switch (isTrasparent(peekPoint, m)) {
								case (-1): {
									tempPoint = p.illusionMoving(d.setToClockWise());
									peekPoint.set(tempPoint);
									pl.add(p);
									p.set(tempPoint);
									peekPoint.move(d.direction);
									break;
								}
								case (0): {
									tempPoint = p.illusionMoving(d.getCounterclockWise());
									switch (isTrasparent(tempPoint, m)) {
									case (-1):// out ob bounds
									case (1): {
										p.set(peekPoint);
										peekPoint.move(d.direction);
										break;
									}
									case (0): {
										pl.add(p);
										peekPoint.set(p);
										peekPoint.move(d.setToCounterclockWise());
										p.set(tempPoint);
										peekPoint.move(d.direction);
										break;
									}
									}
									break;
								}
								case (1): {
									tempPoint = p.illusionMoving(d.getCounterclockWise());
									switch (isTrasparent(tempPoint, m)) {
									case (0): {
										pl.add(p);
										peekPoint.set(p);
										peekPoint.move(d.setToCounterclockWise());
										p.set(tempPoint);
										peekPoint.move(d.direction);
										break;
									}
									case (-1):// out of bounds
									case (1): {
										tempPoint = p.illusionMoving(d.getClockWise());
										switch (isTrasparent(tempPoint, m)) {
										case (0): {
											pl.add(p);
											peekPoint.set(p);
											peekPoint.move(d.setToClockWise());
											p.set(tempPoint);
											peekPoint.move(d.direction);
											break;
										}
										case (-1): // out of bounds
										case (1): {
											pl.add(p);
											d.setToClockWise();
											d.setToClockWise();
											peekPoint = p.illusionMoving(d.direction);
											break;
										}
										}
										break;
									}
									}// // end second switch
									break;
								} // end "if ( peek opaque )"
								}// end first switch
							} while (!primo.equals(p));
							mat = pl.toArray();
							xx = mat[0];
							yy = mat[1];
							pl = null; // let the GC do its work
							m = null;
							ret = new Polygon(xx, yy, yy.length);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Same as {@link #boundingPolygon_UnckeckedParameters(int[][], int, int)},
	 * but slower because it checks the correctness of the parameters, matrix
	 * included (see {@link #isTheMatrixRectangular(int[][])} for further
	 * informations) .
	 */
	public static Polygon boundingPolygon(int[][] m) {
		Polygon ret = null;
		if (m != null && m.length > 0 && isTheMatrixRectangular(m)) {
			int w = m[0].length;
			if (w > 0) {
				ret = boundingPolygon_UnckeckedParameters(m, w, m.length);
			}
		}
		return ret;
	}

	/**
	 * This method, called <i>CrossWalker</i>, extract the most external bounds
	 * of a given BufferedImage considering transparent pixels as part of the
	 * background (the functioning methods uses a integer matrix and consider
	 * <code>0x00??????</code> as transparent and <code>?</code> could be any
	 * value from <code>0</code> to <code>f</code> in hexadecimal), returning
	 * that (probably) irregular bounds as a Polygon instance.<br>
	 * Some unthinkable cases are ignored, for example : image with one or both
	 * dimensions less than 3 pixels<br>
	 * <br>
	 * The Polygon overlaps always the image and CoordinatesOutOfBounds cannot
	 * be thrown inside the main part of the algorithm thanks to the
	 * {@link #isTrasparent(int[][], int, int)}. <br>
	 * <br>
	 * IMPORTANT :
	 * <ul>
	 * <li>NO PARAMETHERS CHECK : all of them are made on
	 * {@link #boundingPolygon(int[][])}.<br>
	 * to improve performance, the parameters aren't checked.<br>
	 * Likely problems :
	 * <ul>
	 * <li>NULLS, of any type (whole matrix or rows)</li>
	 * <li>Ragged matrix (it should be rectangular)</li>
	 * <li><code>w<code> (width) and <code>h</code> (height) could be negative
	 * or inconsistent to the given matrix, in any way (for example, a width
	 * could be greater to the matrix's one).</li>
	 * <li>Empty matrix (i.e., full-transparent matrix).<br>
	 * This may cause no exception or error in general (as like a infinity
	 * loop), but a not good behavior and/or result</li>
	 * <li>The preparation-part of the algorithm could be slow while looking for
	 * the first pixel to start.</li>
	 * </ul>
	 * </li>
	 * <li>This method consider ONLY adjacent pixels. If two near pixels are in
	 * diagonal, for example P(0,2) and Q(1,3), won't be two near part of the
	 * polygon, but the algorithm will turn his walking-direction in some of
	 * onesis cross' way.<br>
	 * Maybe, in a future version, this restriction will be fixed.</li>
	 * </ul>
	 * <br>
	 * <br>
	 * <br>
	 * <br>
	 * Descrizione dell'algoritmo **INVARIANTE*** : ricalco l'immagine (non il
	 * bordo esterno) E il punto in senso antiorario di "p" (quello che poi
	 * diventerà pPrev nel ciclo successivo) è trasparente<br>
	 * <br>
	 * **ALGORITMO**<br>
	 * <br>
	 * sono in un punto "p", guardo quello seguendo la direzione, che sarà
	 * "peekPoint".<br>
	 * If ( peekPoint opaco )
	 * <ul>
	 * <li>----Se è opaco, verifico che quello vicino in senso antiorario
	 * (osservando i punti "a croce") . <br>
	 * if( vicino antiorario e trasparente )
	 * <ul>
	 * <li>Se si : proseguo nella stessa direzione e tutto fila liscio (avanzo
	 * di un passo MA NON aggiungo il punto -> ottimizzazione della lista)</li>
	 * <li>se no : sono in un angolo antiorario! aggiungo il punto p, cambio
	 * direzione, avanzo di un passo<br>
	 * </li>
	 * </ul>
	 * </li>
	 * <li>// else, ossia "se peekPoint è trasparente" .. valuto il vicino
	 * antiorario di p<br>
	 * ------cerco in antiorario, una sola volta (se facessi il controllo di 2
	 * volte antiorario tornerei indietro, sputtanando tutto)---- <br>
	 * <br>
	 * if( vicino di p antiorario è opaco)<br>
	 * <ul>
	 * <li>___ se si : altro angolo antiorario ! aggiungo il punto, cambio
	 * direzione, avanzo di un passo ... strano che abbiamo trovato un angolo e
	 * peek è trasparente O.O che immagine strana...vabbeh, no problema</li>
	 * <li>___ se no : devo cercare in senso orario da "p" : (marker-branck °°°°
	 * per non perdere l'orientamento nel codice)<br>
	 * 
	 * if( orario di p è opaco)
	 * <ul>
	 * <li>__ se è opaco : altro angolo , ma orario! aggiungo il punto, cambio
	 * direzione, avanzo di un passo</li>
	 * <li>__ --se no, CORNA : vol dire che ho raggiunto una punta spessa UN
	 * pixel -> mi giro (appunto) di 180°, aggiungo il punto, faccio un passo e
	 * proseguo (graficamente torno indietro, ma in realtà sto procedendo)</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @param m
	 *            : the matrix to work on
	 * @param w
	 *            : the width of the matrix (not only of a region)
	 * 
	 * @param h
	 *            : the height (m.length) of the matrix (not only of a region)
	 */
	public static Polygon boundingPolygon_UnckeckedParameters(int[][] m, int w, int h) {
		// TODO boundingPolygon_UnckeckedParameters(int[][] m, int w, int h)
		Polygon ret = null;
		PointList pl = null;
		LightWeightPoint p, peekPoint, primo, tempPoint;
		int x = 0, y = -1;
		// , w = m.getWidth(), h = m.getHeight();

		/*
		 * looks for the starting point : the leftmost and lowest (nearest to
		 * zero) point that is opaque.
		 */
		if (w > 0 && h > 0) {
			boolean pointStartNotFound = true;
			int[] row;
			while (pointStartNotFound && ++y < h) {
				x = -1;
				row = m[y];
				while (pointStartNotFound && ++x < w) {
					pointStartNotFound = isTrasparent(// m.getRGB(x, y)
							row[x]);
				}
			}
			if (!pointStartNotFound) {
				try {
					// al = new ArrayList<Punto>(w * h);
					pl = new PointList(primo = new LightWeightPoint(x, y));

					// se sono l'unico punto in circolazione, ho finito
					if (isTrasparent(m, x + 1, y) == 1 && isTrasparent(m, x - 1, y) == 1
							&& isTrasparent(m, x, y + 1) == 1) {
						/*
						 * questo controllo è ridondante : è stato fatto nei
						 * while
						 */
						// && isTrasparent(m ,x, y - 1)==1
						ret = new Polygon(new int[] { x }, new int[] { y }, 1);
						pl = null;
					} else {
						// il caso NORMALE : una immagine un po' piu piena
						Direction d = new Direction();
						int[] xx, yy, mat[];
						// segnamo il punto iniziale
						(peekPoint = (p = primo.clone()).clone()).move(d.direction);
						// tempPoint = p.clone();
						// THE CYCLE
						do {
							// verifico se p è opaco
							switch (isTrasparent(peekPoint, m)) {
							case (-1): {
								// out of bounds
								/*
								 * considerando che mi muovo sguendo
								 * l'invariante, incontrando il "baratro" della
								 * matrice/immagine, devo svoltare .. in senso
								 * orario (è difficile da spiegare,
								 * l'immaginazione aiuta)
								 */
								tempPoint = p.illusionMoving(d.setToClockWise());
								peekPoint.set(tempPoint);
								// mi giro e muovo p
								// peekPoint.move(d.setToClockWise();
								pl.add(p);
								p.set(tempPoint);
								peekPoint.move(d.direction);
								break;
							}
							case (0): {
								// peek è opaco, verifico l'antiorario
								tempPoint = p.illusionMoving(d.getCounterclockWise());
								switch (isTrasparent(tempPoint, m)) {
								case (-1):// out ob bounds
								case (1): {
									// trasp .. continuo caso 1");
									// TUTTO OK, proseguo senza
									// aggiungere
									p.set(peekPoint);
									peekPoint.move(d.direction);
									// peekPoint.toString());
									break;
								}
								case (0): {
									// sono in un angolo antiorario !
									pl.add(p);
									peekPoint.set(p);
									// mi giro e muovo p
									peekPoint.move(d.setToCounterclockWise());
									/*
									 * mi muovo verso tempPoint in quanto,
									 * essendo opaco, è sicuramente un punto
									 * valido
									 */
									p.set(tempPoint);
									peekPoint.move(d.direction);
									break;
								}
								}
								break;
							}
							case (1): {
								// peek è trasparente, verifico l'antiorario
								tempPoint = p.illusionMoving(d.getCounterclockWise());
								switch (isTrasparent(tempPoint, m)) {
								case (0): {
									// ALTRO ANGOLO ANTIORARIO
									pl.add(p);
									peekPoint.set(p);
									// mi giro e muovo p
									peekPoint.move(d.setToCounterclockWise());
									// similmente dall'angolo sopra, mi
									// muovo
									p.set(tempPoint);
									peekPoint.move(d.direction);
									break;
								}
								case (-1):// out of bounds
								case (1): {
									/*
									 * cerco in senso orario da p .. se è opaco,
									 * nuovo angolo, altrimenti p era una corna
									 * e sto tornando indietro (ma è tutto ok)
									 */
									tempPoint = p.illusionMoving(d.getClockWise());
									switch (isTrasparent(tempPoint, m)) {
									case (0): {
										// ccorner, ma ORARIO !
										pl.add(p);
										peekPoint.set(p);
										// i turn myself and move p
										peekPoint.move(d.setToClockWise());
										p.set(tempPoint);
										peekPoint.move(d.direction);
										break;
									}
									case (-1): // out of bounds
									case (1): {
										// "p" is a HORN, a point
										// .. come back
										// turn to 180°
										pl.add(p);
										d.setToClockWise();
										d.setToClockWise();
										peekPoint = p.illusionMoving(d.direction);
										break;
									}
									}
									break;
								}
								}// // end second switch
								break;
							} // end "if ( peek opaque )"

							}// end first switch
						} while (!primo.equals(p));
						// conversion list into polygon
						mat = pl.toArray();
						xx = mat[0];
						yy = mat[1];
						pl = null; // let the GC do its work
						m = null;
						/*
						 * share the array because they will be useless inside
						 * this method
						 */
						ret = new Polygon(xx, yy, yy.length);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			/*
			 * else { }
			 */
		}
		return ret;
	}

	private static int isTrasparent(LightWeightPoint p, BufferedImage bi) {
		return isTrasparent(bi, p.x, p.y);
	}

	/**
	 * See {@link #isTrasparent(int[][], int, int)} for further informations.
	 */
	public static int isTrasparent(BufferedImage bi, int x, int y) {
		if (bi != null && x >= 0 && y >= 0 && y < bi.getHeight() && x < bi.getWidth()) {
			return isTrasparent(bi.getRGB(x, y)) ? 1 : 0;
		}
		return -1;
	}

	/**
	 * See {@link #isTrasparent(int[][], int, int)} for further informations.
	 */
	private static int isTrasparent(LightWeightPoint p, int[][] matrix) {
		return isTrasparent(matrix, p.x, p.y);
	}

	/**
	 * @param matrix
	 *            : the matrix to extract and test the pixel
	 * @param x
	 *            : the x-coordinate of the pixel to be tested
	 * @param y
	 *            : the y-coordinate of the pixel to be tested
	 * @return
	 *         <ul>
	 *         <li>1 if the given pixel is transparent (see
	 *         {@link #isTrasparent(int)} for further information)</li>
	 *         <li>0 if the given pixel is NOT transparent</li>
	 *         <li>-1 if the matrix (or the y-row) is NULL or the given pixel is
	 *         OUT OF BOUNDS</li>
	 *         </ul>
	 */
	public static int isTrasparent(int[][] matrix, int x, int y) {
		if (matrix != null && x >= 0 && y >= 0 && y < matrix.length) {
			int[] r = matrix[y];
			if (r != null && x < r.length) {
				return isTrasparent(r[x]) ? 1 : 0;
			}
		}
		return -1;
	}

	/**
	 * Return TRUE if the color, expressed in ARGB value, has the alpha (8
	 * leftmost bit) equals to zero.
	 */
	public static boolean isTrasparent(int n) {
		// using a mask
		return ((n & 0xff000000) >>> 24) == 0;
	}

	/**
	 * The opposite (faster than a boolean negation) of
	 * {@link #isTrasparent(int)}.
	 */
	public static boolean isNotTrasparent(int n) {
		// using a mask
		return ((n & 0xff000000) >>> 24) != 0;
	}

	//

	//

	private static final class PointList implements Serializable {

		private static final long serialVersionUID = 560149840847963736L;
		Node head, tail;
		int size;

		PointList(LightWeightPoint p) {
			this(p.x, p.y);
		}

		PointList(int x, int y) {
			tail = head = new Node(x, y);
			size = 1;
		}

		void add(LightWeightPoint p) {
			add(p.x, p.y);
		}

		void add(int x, int y) {
			tail.next = new Node(x, y);
			tail = tail.next;
			size++;
		}

		class Node {
			Node next;
			int x, y;

			Node(int xx, int yy) {
				x = xx;
				y = yy;
				next = null;
			}
		}

		int[][] toArray() {
			int[] m[] = { new int[size], new int[size] }, a, b;
			a = m[0];
			b = m[1];
			Node n = head;
			int i = 0;
			while (n != null) {
				a[i] = n.x;
				b[i++] = n.y;
				n = n.next;
			}
			return m;
		}
	}

	/**
	 * @param m
	 *            the matrix to be tested
	 * @return <code>true</code> if the matrix if contains no-null raws and is
	 *         rectangular; <code>false</code> in every other cases.
	 */
	public static boolean isTheMatrixRectangular(int[][] m) {
		boolean ret = m != null;
		if (ret) {
			ret = m.length > 0;
			if (ret) {
				int[] riga = m[0];
				if (riga != null) {
					int lengthFirstRow = riga.length, i1 = 0;
					while (ret && (++i1 < m.length)) {
						riga = m[i1];
						ret = (riga != null) ? riga.length == lengthFirstRow : false;
					}
				} else {
					ret = false;
				}
			}
		}
		return ret;
	}

	//

	private static final class LightWeightPoint implements Serializable {
		private static final long serialVersionUID = 54098051514909900L;
		int x, y;

		LightWeightPoint(int x, int y) {
			this.x = x;
			this.y = y;
		}

		LightWeightPoint(LightWeightPoint p) {
			set(p);
		}

		void set(LightWeightPoint p) {
			// set(p.x, p.y);
			this.x = p.x;
			this.y = p.y;
		}

		// void set(int x, int y) { this.x = x; this.y = y; }

		LightWeightPoint move(CrossDirections d) {
			x += d.dx;
			y += d.dy;
			return this;
		}

		/** I clone myself, the i move (and then return) that clone. */
		LightWeightPoint illusionMoving(CrossDirections d) {
			return this.clone().move(d);
		}

		public boolean equals(LightWeightPoint p) {
			return x == p.x && y == p.y;
		}

		@Override
		public String toString() {
			return "LightWeightPoint [x=" + x + ", y=" + y + ']';
		}

		@Override
		public LightWeightPoint clone() {
			return new LightWeightPoint(this);
		}
	}

	/**
	 * A simple class holding a <code>CrossDirections</code> instance and simple
	 * methods to change his value or get s clockwise and counter clockwise
	 * directions.
	 */
	private static final class Direction implements Serializable {
		private static final long serialVersionUID = 640654870800000004L;
		CrossDirections direction;

		Direction() {
			direction = CrossDirections.RIGHT;
		}

		CrossDirections getCounterclockWise() {
			int i = direction.index - 1;
			if (i < 0) {
				i = valuesCrossDirections.length - 1;
			}
			return valuesCrossDirections[i];
		}

		CrossDirections getClockWise() {
			int i = direction.index + 1;
			if (i >= valuesCrossDirections.length) {
				i = 0;
			}
			return valuesCrossDirections[i];
		}

		// setter
		CrossDirections setDirection(CrossDirections di) {
			return direction = di;
		}

		/** Set the current direction to their counter clockwise ones. */
		CrossDirections setToCounterclockWise() {
			return setDirection(getCounterclockWise());
		}

		CrossDirections setToClockWise() {
			return setDirection(getClockWise());
		}

		@Override
		public String toString() {
			return direction.name();
		}

	}
}