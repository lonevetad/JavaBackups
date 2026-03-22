package tools.minorTools;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import tools.CastingClass;
import tools.MathUtilities;

public class ManipulatingArrays {

	public static void main(String[] args) {
		int[] a = { 4, 5, -7, 132, 40, 1, 17 };

		// sort_MyInsection_Linear_Growing( a ) ;

		System.out.println("11111111_11 : " + 11111111_11 + " _____ " + Integer.toBinaryString(11111111_11));
		for (int i = 0; i < a.length; i++) {
			System.out.println();
			System.out.println(a[i] + " = " + Integer.toBinaryString(a[i]));
			a[i] = MathUtilities.negateInt(a[i]);
			System.out.println(a[i] + " = " + Integer.toBinaryString(a[i]));
		}
	}

	private ManipulatingArrays() {
	} // nessuna istanziazione

	// public static final QuickSort quickSort = new QuickSort();

	private static int tempInt = 0, index, j, pivot_Int;
	private static int[] aInt = null;

	public static void sort_QuickSort(int[] a, boolean growing) {
		if (growing) {
			sort_QuickSort_Growing(a);
		} else {
			sort_QuickSort_UnGrowing(a);
		}
	}

	public static void sort_QuickSort_Growing(int[] a) {
		sort_QuickSort_Growing(a, 0, a.length - 1);
	}

	public static void sort_QuickSort_Growing(int[] a, int minIndex, int maxIndex) {
		if (!((a == null) || (minIndex < 0) || (maxIndex <= 0) || (minIndex >= maxIndex) || (maxIndex >= a.length)
				|| (a.length < 2))) {
			aInt = a;
			sort_QuickSort_Growing_EverythingOK_Int(minIndex, maxIndex);
		} // else it makes non sense
	}

	private static void sort_QuickSort_Growing_EverythingOK_Int(int minIndex, int maxIndex) {
		// int , pivot; // int tempInt = 0;
		index = minIndex;
		j = maxIndex;
		pivot_Int = aInt[minIndex + (maxIndex - minIndex) / 2];
		while(index < j) { // ricerca di un elemento pi� piccolo del pivot_Int
			// e di uno pi� grande del pivot_Int da scambiare
			// vicendevolmente
			while(aInt[index] < pivot_Int) {
				index++;
			}
			while(aInt[j] > pivot_Int) {
				j--;
			}
			if (index <= j) // l' "=" � FONDAMENTALE: senza di esso si va in
			// stackOverflow
			{ // scambio
				tempInt = aInt[index];
				aInt[index] = aInt[j];
				aInt[j] = tempInt;
				// move index to next position on both sides
				index++;
				j--;
			}
		}
		// chiamata ricorsiva
		if (j > minIndex) {
			sort_QuickSort_Growing_EverythingOK_Int(minIndex, j);
		}
		if (index < maxIndex) {
			sort_QuickSort_Growing_EverythingOK_Int(index, maxIndex);
		}
	}

	public static void sort_QuickSort_UnGrowing(int[] a) {
		sort_QuickSort_UnGrowing(a, 0, a.length - 1);
	}

	public static void sort_QuickSort_UnGrowing(int[] a, int minIndex, int maxIndex) {
		if (!((a == null) || (minIndex < 0) || (maxIndex <= 0) || (minIndex >= maxIndex) || (maxIndex >= a.length)
				|| (a.length < 2))) {
			aInt = a;
			sort_QuickSort_UnGrowing_EverythingOK_Int(minIndex, maxIndex);
		}
		// else it makes non sense
	}

	private static void sort_QuickSort_UnGrowing_EverythingOK_Int(int minIndex, int maxIndex) {
		// int , pivot_Int; // int tempInt = 0;
		index = minIndex;
		j = maxIndex;
		pivot_Int = aInt[index + (j - index) / 2];
		while(index < j) { // ricerca di un elemento pi� piccolo del pivot_Int
			// e di uno pi� grande del pivot_Int da scambiare
			// vicendevolmente
			while(aInt[index] > pivot_Int) {
				index++;
			}
			while(aInt[j] < pivot_Int) {
				j--;
			}
			if (index <= j) // l' "=" � FONDAMENTALE: senza di esso si va in
			// stackOverflow
			{ // scambio
				tempInt = aInt[index];
				aInt[index] = aInt[j];
				aInt[j] = tempInt;
				// move index to next position on both sides
				index++;
				j--;
			}
		}
		// chiamata ricorsiva
		if (j > minIndex) {
			sort_QuickSort_UnGrowing_EverythingOK_Int(minIndex, j);
		}
		if (index < maxIndex) {
			sort_QuickSort_UnGrowing_EverythingOK_Int(index, maxIndex);
		}
	}

	private static double tempDoub = 0, pivot_Doub;
	private static double[] aDoub = null;

	public static void sort_QuickSort(double[] a, boolean growing) {
		if (growing) {
			sort_QuickSort_Growing(a);
		} else {
			sort_QuickSort_UnGrowing(a);
		}
	}

	public static void sort_QuickSort_Growing(double[] a) {
		sort_QuickSort_Growing(a, 0, a.length - 1);
	}

	public static void sort_QuickSort_Growing(double[] a, int minIndex, int maxIndex) {
		if (!((a == null) || (minIndex < 0) || (maxIndex <= 0) || (minIndex >= maxIndex) || (maxIndex >= a.length)
				|| (a.length < 2))) {
			aDoub = a;
			sort_QuickSort_Growing_EverythingOK_Doub(minIndex, maxIndex);
		} // else it makes non sense
	}

	private static void sort_QuickSort_Growing_EverythingOK_Doub(int minIndex, int maxIndex) {
		// int i = minIndex, j = maxIndex; // double tempDoub = 0;
		index = minIndex;
		j = maxIndex;
		pivot_Doub = aDoub[minIndex + (maxIndex - minIndex) / 2];
		while(index < j) { // ricerca di un elemento pi� piccolo del pivot_Doub
			// e di uno pi� grande del pivot_Doub da scambiare
			// vicendevolmente
			while(aDoub[index] < pivot_Doub) {
				index++;
			}
			while(aDoub[j] > pivot_Doub) {
				j--;
			}
			if (index <= j) // l' "=" � FONDAMENTALE: senza di esso si va in
			// stackOverflow
			{ // scambio
				tempDoub = aDoub[index];
				aDoub[index] = aDoub[j];
				aDoub[j] = tempDoub;
				// move index to next position on both sides
				index++;
				j--;
			}
		}
		// chiamata ricorsiva
		if (j > minIndex) {
			sort_QuickSort_Growing_EverythingOK_Doub(minIndex, j);
		}
		if (index < maxIndex) {
			sort_QuickSort_Growing_EverythingOK_Doub(index, maxIndex);
		}
	}

	public static void sort_QuickSort_UnGrowing(double[] a) {
		sort_QuickSort_UnGrowing(a, 0, a.length - 1);
	}

	public static void sort_QuickSort_UnGrowing(double[] a, int minIndex, int maxIndex) {
		if (!((a == null) || (minIndex < 0) || (maxIndex <= 0) || (minIndex >= maxIndex) || (maxIndex >= a.length)
				|| (a.length < 2))) {
			aDoub = a;
			sort_QuickSort_UnGrowing_EverythingOK_Doub(minIndex, maxIndex);
		}
		// else it makes non sense
	}

	private static void sort_QuickSort_UnGrowing_EverythingOK_Doub(int minIndex, int maxIndex) {
		// int i = minIndex, j = maxIndex; // double tempDoub = 0;
		index = minIndex;
		j = maxIndex;
		pivot_Doub = aDoub[index + (j - index) / 2];
		while(index < j) { // ricerca di un elemento pi� piccolo del pivot_Doub
			// e di uno pi� grande del pivot_Doub da scambiare
			// vicendevolmente
			while(aDoub[index] > pivot_Doub) {
				index++;
			}
			while(aDoub[j] < pivot_Doub) {
				j--;
			}
			if (index <= j) // l' "=" � FONDAMENTALE: senza di esso si va in
			// stackOverflow
			{ // scambio
				tempDoub = aDoub[index];
				aDoub[index] = aDoub[j];
				aDoub[j] = tempDoub;
				// move index to next position on both sides
				index++;
				j--;
			}
		}
		// chiamata ricorsiva
		if (j > minIndex) {
			sort_QuickSort_UnGrowing_EverythingOK_Doub(minIndex, j);
		}
		if (index < maxIndex) {
			sort_QuickSort_UnGrowing_EverythingOK_Doub(index, maxIndex);
		}
	}

	public static int[] sort_BucketCounting_Growing(int[] a) {
		int[] ret = null;
		if (a != null) {
			ret = new int[a.length];
			if (a.length > 0) {
				int min = a[0], max = a[0], i, c = 0, k = 0, len;
				for (i = 0; i < a.length; i++) {
					if (min > a[i]) {
						min = a[i];
					}
					if (max < a[i]) {
						max = a[i];
					}
				}
				len = max - min + 1;
				int[] f = new int[len];
				// riempimento di f
				for (i = 0; i < a.length; i++) {
					f[a[i] - min]++;
				}
				// ordinamento
				i = 0;
				while(k < len) {
					if (c++ < f[k]) {
						ret[i++] = k + min;
					} else {
						c = 0;
						k++;
					}
					// � equivalente a
					/*
					 * if( c < f[k]){ ret[i] = k + min ; i++; c++; } else { c = 0; k++; }
					 */
				}
			}
		}
		return ret;
	}

	public static int[] sort_BucketCounting_UnGrowing(int[] a) // a differenza
	// del growing,
	// distribuisco
	// gli elementi
	// dall'ultima
	// posizione
	// verso la
	// 0-esima
	{
		int[] ret = null;
		if (a != null) {
			ret = new int[a.length];
			if (a.length > 0) {
				int min = a[0], max = a[0], i, c = 0, k = 0, len;
				for (i = 0; i < a.length; i++) {
					if (min > a[i]) {
						min = a[i];
					}
					if (max < a[i]) {
						max = a[i];
					}
				}
				len = max - min + 1;
				int[] f = new int[len];
				// riempimento di f
				for (i = 0; i < a.length; /* i++ */) {
					f[a[i++] - min]++;
				}
				// ordinamento
				i = ret.length - 1;
				while(k < len) // in alternativa fare come scritto nei commenti
				// : "k = (len-1)" e fare "while( k >= 0 )"
				{
					if (c++ < f[k]) {
						ret[i--] = k + min;
					} // usare "i++" e "max - k" se si scorre da 0 a
						// ret.length-1
					else {
						c = 0;
						k++;
					}
				}
			}
		}
		return ret;
	}

	public static boolean isArrayAllRelativeToZero(long[] a, boolean trueMore_FalseLess,
			boolean trueEqual_FalseNotEqual) {
		boolean ret = a != null;
		if (ret) {
			if (a.length > 0) {
				int i = 0;
				if (trueEqual_FalseNotEqual) {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] >= 0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] <= 0;
						}
					}
				} else {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] > 0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] < 0;
						}
					}
				}
			}
		}
		return ret;
	}

	public static boolean isArrayAllRelativeToZero(double[] a, boolean trueMore_FalseLess,
			boolean trueEqual_FalseNotEqual) {
		boolean ret = a != null;
		if (ret) {
			if (a.length > 0) {
				int i = 0;
				if (trueEqual_FalseNotEqual) {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] >= 0.0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] <= 0.0;
						}
					}
				} else {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] > 0.0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] < 0.0;
						}
					}
				}
			}
		}
		return ret;
	}

	public static boolean isArrayAllRelativeToZero(float[] a, boolean trueMore_FalseLess,
			boolean trueEqual_FalseNotEqual) {
		boolean ret = a != null;
		if (ret) {
			if (a.length > 0) {
				int i = 0;
				if (trueEqual_FalseNotEqual) {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] >= 0.0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] <= 0.0;
						}
					}
				} else {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] > 0.0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] < 0.0;
						}
					}
				}
			}
		}
		return ret;
	}

	public static boolean isArrayAllRelativeToZero(int[] a, boolean trueMore_FalseLess,
			boolean trueEqual_FalseNotEqual) {
		boolean ret = a != null;
		if (ret) {
			if (a.length > 0) {
				int i = 0;
				if (trueEqual_FalseNotEqual) {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] >= 0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] <= 0;
						}
					}
				} else {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] > 0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] < 0;
						}
					}
				}
			}
		}
		return ret;
	}

	public static boolean isArrayAllRelativeToZero(short[] a, boolean trueMore_FalseLess,
			boolean trueEqual_FalseNotEqual) {
		boolean ret = a != null;
		if (ret) {
			if (a.length > 0) {
				int i = 0;
				if (trueEqual_FalseNotEqual) {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] >= 0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] <= 0;
						}
					}
				} else {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] > 0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] < 0;
						}
					}
				}
			}
		}
		return ret;
	}

	public static boolean isArrayAllRelativeToZero(byte[] a, boolean trueMore_FalseLess,
			boolean trueEqual_FalseNotEqual) {
		boolean ret = a != null;
		if (ret) {
			if (a.length > 0) {
				int i = 0;
				if (trueEqual_FalseNotEqual) {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] >= 0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] <= 0;
						}
					}
				} else {
					if (trueMore_FalseLess) {
						while(ret && i < a.length) {
							ret = a[i] > 0;
						}
					} else {
						while(ret && i < a.length) {
							ret = a[i] < 0;
						}
					}
				}
			}
		}
		return ret;
	}

	public static int[][] fillMatrix(int[][] m, int v) {
		if (m != null) {
			for (int r = 0; r < m.length; r++) {
				fillArray(m[r], v);
			}
		}
		return m;
	}

	public static double[][] fillMatrix(double[][] m, double v) {
		if (m != null) {
			for (int r = 0; r < m.length; r++) {
				fillArray(m[r], v);
			}
		}
		return m;
	}

	public static int[] fillArray(int[] riga, int v) {
		if (riga != null) {
			for (int c = 0; c < riga.length; c++) {
				riga[c] = v;
			}
		}
		return riga;
	}

	public static double[] fillArray(double[] riga, double v) {
		if (riga != null) {
			for (int c = 0; c < riga.length; c++) {
				riga[c] = v;
			}
		}
		return riga;
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

	// Image
	public static Image getScaledInstance(Image im, int width, int height, boolean isARGB) {
		BufferedImage bif = isARGB ? CastingClass.castImage_ARGB_ToBufferedImage(im)
				: CastingClass.castImage_RGB_ToBufferedImage(im);
		return getScaledInstance(bif, width, height);
	}

	/**
	 * Default image type = ARGB
	 */
	public static Image getScaledInstance(Image im, int width, int height) {
		return getScaledInstance(CastingClass.castImage_ARGB_ToBufferedImage(im), width, height);
	}

	public static Image getScaledInstance(Image im, double scaleWidth, double scaleHeight) {
		return getScaledInstance(CastingClass.castImage_ARGB_ToBufferedImage(im), scaleWidth, scaleHeight);
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
					while(rRet < height) {
						tempRigaRet = ret[rRet] = new int[width];
						cRet = 0;
						cm = 0.0;
						while((cRet < ret[rRet].length) && (cm < m.getWidth()) && ((rm) < m.getHeight())) {
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

	public static BufferedImage getScaledInstance(BufferedImage m, double scaleWidth, double scaleHeight) {
		int[][] ret = null;
		BufferedImage bif = null;
		if (m != null) {
			if (m.getHeight() > 0) {
				if (scaleWidth > 0.0 && scaleHeight > 0.0) {
					ret = new int[(int) (m.getHeight() * scaleHeight)][];
					double rm = 0, cm = 0;
					int rRet = 0, cRet = 0, len = m.getHeight(), lennn = (int) (len * scaleHeight),
							newWidth = (int) (m.getWidth() * scaleWidth);
					int[] tempRigaRet = null; // soluzione per ottimizzare gli
					// accessi in memoria delle
					// righe della matrice
					while(rRet < lennn) {
						tempRigaRet = ret[rRet] = new int[newWidth];
						cRet = 0;
						cm = 0.0;
						while((cRet < ret[rRet].length) && ((cm) < m.getWidth()) && ((rm) < m.getHeight())) {
							tempRigaRet[cRet] = m.getRGB((int) cm, (int) rm);
							// cm += 1.0 / scaleWidth ;
							cm = ++cRet / scaleWidth;
						}
						// rm += 1.0 / scaleHeight ;
						rm = ++rRet / scaleHeight;
					}
					// bif = CastingClass.castMatrixBufferedImage( ret );
					bif = new BufferedImage(newWidth, lennn, m.getType());
					for (int r = 0; r < lennn; r++) {
						tempRigaRet = ret[r]; // fissata una riga, opero solo su
						// quella
						for (int c = 0; c < newWidth; c++) {
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
		int[][] ret = null;
		if (m != null) {
			if (m.length > 0) {
				if (width > 0 && height > 0 /* && isTheMatrixRectangular(m) */) {
					ret = new int[height][];
					double rm = 0, cm = 0;
					int rRet = 0, cRet = 0; // contatori
					int[] tempRigaRet = null, tempRigaM; // soluzione per
					// ottimizzare gli
					// accessi in
					// memoria delle
					// righe della
					// matrice
					// sh = ((double)m.length) / height ; // � pi� utile
					while(rRet < height) { // System.out.println( "rRet = " +
						// rRet + "\t rm = " + rm);
						if ((tempRigaM = m[(int) rm]) != null) {
							tempRigaRet = ret[rRet] = new int[width];
							cRet = 0;
							cm = 0.0;
							while((cRet < tempRigaRet.length) && (cm < tempRigaM.length)) {
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

	public static int[][] getScaledInstance(int[][] m, double scaleWidth, double scaleHeight) {
		int[][] ret = null;
		if (m != null) {
			if (m.length > 0) {
				if (scaleWidth > 0.0 && scaleHeight > 0.0 && isTheMatrixRectangular(m)) {
					ret = new int[(int) (m.length * scaleHeight)][];
					double rm = 0, cm = 0;
					int rRet = 0, cRet = 0, len = m.length, lennn = (int) (len * scaleHeight);
					int[] tempRigaRet = null, tempRigaM; // soluzione per
					// ottimizzare gli
					// accessi in
					// memoria delle
					// righe della
					// matrice
					while(rRet < lennn) {
						if ((tempRigaM = m[(int) rm]) != null) {
							tempRigaRet = ret[rRet] = new int[(int) (tempRigaM.length * scaleWidth)];
							cRet = 0;
							cm = 0.0;
							while((cRet < ret[rRet].length) && ((cm) < (tempRigaM.length))) {
								tempRigaRet[cRet] = tempRigaM[(int) cm];
								// cm += 1.0 / scaleWidth ;
								cm = ++cRet / scaleWidth;
							}
						}
						// rm += 1.0 / scaleHeight ;
						rm = ++rRet / scaleHeight;
					}
				}
			} else {
				ret = new int[0][0];
			}
		}
		return ret;
	}

	public static int[][] getCopyAndGlue_Matrix(int[][] m, int howManyCopies) {
		return getCopyAndGlue_Matrix(m, howManyCopies, howManyCopies);
	}

	public static int[][] getCopyAndGlue_Matrix(int[][] m, int sRighe, int sColonne) {
		int[][] ret = m;
		if (m != null && (sRighe > 0) && (sColonne > 0)) {
			int numRig = m.length, numCol = 0;
			int tempIntNumRig = 0, tempIntNumColon = 0; // variabile d'appoggio
			// per evitare di
			// ripetere i calcoli
			// troppe volte nella
			// proposizione dei
			// cicli
			ret = new int[numRig * sRighe][]; // ripetizioneColonne
			// cicli di creazione
			for (int repetitionRows = 0; repetitionRows < sRighe; repetitionRows++) {
				tempIntNumRig = repetitionRows * numRig;
				for (int r = 0; r < numRig; r++) {
					if (m[r] == null) {
						ret[tempIntNumRig + r] = null;
					} else {
						ret[tempIntNumRig + r] = new int[m[r].length * sColonne];
					}
				}
			}
			// cicli di riempimento
			int r, ripetizioneColonne, c;
			int[] tempRiga;
			for (int repetitionRows = 0; repetitionRows < sRighe; repetitionRows++) {
				for (ripetizioneColonne = 0; ripetizioneColonne < sColonne; ripetizioneColonne++) {
					tempIntNumRig = repetitionRows * numRig;
					for (r = 0; r < numRig; r++) {
						tempRiga = m[r];
						if (tempRiga != null) {
							numCol = tempRiga.length;
							tempIntNumColon = numCol * ripetizioneColonne;
							for (c = 0; c < numCol; c++) {
								ret[tempIntNumRig + r][tempIntNumColon + c] = tempRiga[c];
							}
						}
					}
				}
			}
		}
		return ret;
	}

	public static int[][] getCopy_Matrix(int[][] matrix) {
		int[][] ret = null;
		if (matrix != null) {
			if (matrix.length != 0) {
				int[] tempRigaMatrix, tempRigaRet; // variabili per ottimizzare
				// l'accesso in memoria
				ret = new int[matrix.length][];
				for (int r = 0; r < matrix.length; r++) {
					tempRigaRet = ret[r] = new int[(tempRigaMatrix = matrix[r]).length];
					for (int c = 0; c < tempRigaMatrix.length; c++) {
						tempRigaRet[c] = tempRigaMatrix[c];
					}
				}
			} else {
				ret = new int[0][];
			}
		}
		return ret;
	}

	public static double[][] getCopy_Matrix(double[][] matrix) {
		double[][] ret = null;
		if (matrix != null) {
			if (matrix.length != 0) {
				ret = new double[matrix.length][];
				double[] tempRigaMatrix, tempRigaRet; // variabili per
				// ottimizzare l'accesso
				// in memoria
				for (int r = 0; r < matrix.length; r++) {
					tempRigaRet = ret[r] = new double[(tempRigaMatrix = matrix[r]).length];
					for (int c = 0; c < tempRigaMatrix.length; c++) {
						tempRigaRet[c] = tempRigaMatrix[c];
					}
				}
			} else {
				ret = new double[0][];
			}
		}
		return ret;
	}

	public static byte[][] getCopy_Matrix(byte[][] matrix) {
		byte[][] ret = null;
		if (matrix != null) {
			if (matrix.length != 0) {
				ret = new byte[matrix.length][];
				byte[] tempRigaMatrix, tempRigaRet; // variabili per ottimizzare
				// l'accesso in memoria
				for (int r = 0; r < matrix.length; r++) {
					tempRigaRet = ret[r] = new byte[(tempRigaMatrix = matrix[r]).length];
					for (int c = 0; c < tempRigaMatrix.length; c++) {
						tempRigaRet[c] = tempRigaMatrix[c];
					}
				}
			} else {
				ret = new byte[0][];
			}
		}
		return ret;
	}

	public static long[][] getCopy_Matrix(long[][] matrix) {
		long[][] ret = null;
		if (matrix != null) {
			if (matrix.length != 0) {
				ret = new long[matrix.length][];
				long[] tempRigaMatrix, tempRigaRet; // variabili per ottimizzare
				// l'accesso in memoria
				for (int r = 0; r < matrix.length; r++) {
					tempRigaRet = ret[r] = new long[(tempRigaMatrix = matrix[r]).length];
					for (int c = 0; c < tempRigaMatrix.length; c++) {
						tempRigaRet[c] = tempRigaMatrix[c];
					}
				}
			} else {
				ret = new long[0][];
			}
		}
		return ret;
	}

	public static int[] getCopy_Array(int[] vector) {
		int[] ret = null;
		if (vector != null) {
			ret = new int[vector.length];
			for (int r = 0; r < vector.length; r++) {
				ret[r] = vector[r];
			}
		}
		return ret;
	}

	public static double[] getCopy_Array(double[] vector) {
		double[] ret = null;
		if (vector != null) {
			ret = new double[vector.length];
			for (int r = 0; r < vector.length; r++) {
				ret[r] = vector[r];
			}
		}
		return ret;
	}

	public static byte[] getCopy_Array(byte[] vector) {
		byte[] ret = null;
		if (vector != null) {
			ret = new byte[vector.length];
			for (int r = 0; r < vector.length; r++) {
				ret[r] = vector[r];
			}
		}
		return ret;
	}

	public static long[] getCopy_Array(long[] vector) {
		long[] ret = null;
		if (vector != null) {
			ret = new long[vector.length];
			for (int r = 0; r < vector.length; r++) {
				ret[r] = vector[r];
			}
		}
		return ret;
	}

	public static ArrayList<int[]> getCopy_ArrayListVector_Int(ArrayList<int[]> alv) {
		ArrayList<int[]> ret = null;
		if (alv != null) {
			ret = new ArrayList<int[]>(alv.size());
			{
				for (int i1 = 0; i1 < alv.size(); i1++) {
					ret.add(getCopy_Array(alv.get(i1)));
				}
			}
		}
		return ret;
	}

	public static ArrayList<double[]> getCopy_ArrayListVector_Doub(ArrayList<double[]> alv) {
		ArrayList<double[]> ret = null;
		if (alv != null) {
			ret = new ArrayList<double[]>(alv.size());
			{
				for (int i1 = 0; i1 < alv.size(); i1++) {
					ret.add(getCopy_Array(alv.get(i1)));
				}
			}
		}
		return ret;
	}

	public static ArrayList<int[][]> getCopy_ArrayListMatrix_Int(ArrayList<int[][]> alm) {
		ArrayList<int[][]> ret = null;
		if (alm != null) {
			ret = new ArrayList<int[][]>(alm.size());
			{
				for (int i1 = 0; i1 < alm.size(); i1++) {
					ret.add(getCopy_Matrix(alm.get(i1)));
				}
			}
		}
		return ret;
	}

	public static ArrayList<double[][]> getCopy_ArrayListMatrix_Doub(ArrayList<double[][]> alm) {
		ArrayList<double[][]> ret = null;
		if (alm != null) {
			ret = new ArrayList<double[][]>(alm.size());
			{
				for (int i1 = 0; i1 < alm.size(); i1++) {
					ret.add(getCopy_Matrix(alm.get(i1)));
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

	public static boolean isTheMatrixRectangular(int[][] m) {
		boolean ret = m != null;
		if (ret) {
			ret = m.length > 0;
			if (ret) {
				int[] riga = m[0];
				if (riga != null) {
					int lengthFirstRow = riga.length, i1 = 0;
					while(ret && (++i1 < m.length)) {
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

	public static boolean isTheMatrixRectangular(boolean[][] m) {
		boolean ret = m != null;
		if (ret) {
			ret = m.length > 0;
			if (ret) {
				boolean[] riga = m[0];
				if (riga != null) {
					int l = riga.length, i1 = 1;
					// the (ret == true) could be unecessary, but sometimes the
					// (flag) gives bugs. The optimization will be made by the
					// compiler
					while(ret && (i1 < m.length)) {
						riga = m[i1];
						if (riga != null) {
							ret = riga.length == l;
						} else {
							ret = false;
						}
						i1++;
					}
				} else {
					ret = false;
				}
			}
		}
		return ret;
	}

	public static boolean isTheMatrixRectangular(double[][] m) {
		boolean ret = m != null;
		if (ret) {
			ret = m.length > 0;
			if (ret) {
				double[] riga = m[0];
				if (riga != null) {
					int l = riga.length, i1 = 1;
					// the (ret == true) could be unecessary, but sometimes the
					// (flag) gives bugs. The optimization will be made by the
					// compiler
					while(ret && (i1 < m.length)) {
						riga = m[i1];
						if (riga != null) {
							ret = riga.length == l;
						} else {
							ret = false;
						}
						i1++;
					}
				} else {
					ret = false;
				}
			}
		}
		return ret;
	}

	public static boolean isTheMatrixASquare(double[][] m) {
		boolean ret = true;
		if (m != null) {
			try {
				int l;
				if (m.length > 0 && ((l = m[0].length) == m.length)) {
					// the (ret == true) could be unecessary, but sometimes the
					// (flag) gives bugs. The optimization will be made by the
					// compiler
					for (int i1 = 1; ((i1 < m.length) && ret); i1++) {
						if (m[i1] != null) {
							ret = m[i1].length == l;
						}
					}
				} else {
					ret = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				ret = false;
			}
		} else {
			ret = false;
		}
		return ret;
	}

	public static boolean isTheMatrixASquare(int[][] m) {
		boolean ret = true;
		if (m != null) {
			try {
				int l;
				if (m.length > 0 && ((l = m[0].length) == m.length)) {
					// the (ret == true) could be unecessary, but sometimes the
					// (flag) gives bugs. The optimization will be made by the
					// compiler
					for (int i1 = 1; ((i1 < m.length) && ret); i1++) {
						if (m[i1] != null) {
							ret = m[i1].length == l;
						}
					}
				} else {
					ret = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				ret = false;
			}
		} else {
			ret = false;
		}
		return ret;
	}

	public static ArrayList<Integer> shuffleArrayListInteger(ArrayList<Integer> tmp) {
		ArrayList<Integer> ind = new ArrayList<Integer>(tmp.size());
		int t = 0;
		for (int i1 = 0; i1 < tmp.size(); i1++) {
			ind.add(tmp.get(i1));
		}
		Random rand = new Random();
		for (int i1 = 0; i1 < tmp.size(); i1++) {
			t = (int) (rand.nextDouble() * ind.size());
			tmp.set(i1, ind.get(t));
			ind.remove(t);
			ind.trimToSize();
		}
		return tmp;
	}

	public static ArrayList<Double> shuffleArrayListDouble(ArrayList<Double> tmp) {
		ArrayList<Double> ind = new ArrayList<Double>(tmp.size());
		int t = 0;
		for (int i1 = 0; i1 < tmp.size(); i1++) {
			ind.add(tmp.get(i1));
		}
		Random rand = new Random();
		for (int i1 = 0; i1 < tmp.size(); i1++) {
			t = (int) (rand.nextDouble() * ind.size());
			tmp.set(i1, ind.get(t));
			ind.remove(t);
			ind.trimToSize();
		}
		return tmp;
	}

	public static ArrayList<Integer> switchElement_ArrayListInteger(ArrayList<Integer> tmp) {
		for (int ii = 0; ii < (int) (tmp.size() / 2.0); ii++) {
			tempInt = tmp.get(ii);
			tmp.set(ii, tmp.get((tmp.size() - (ii + 1))));
			tmp.set((tmp.size() - (ii + 1)), tempInt);
		}
		return tmp;
	}

	public static ArrayList<Double> switchElement_ArrayListDouble(ArrayList<Double> tmp) {
		double t = 0;
		for (int ii = 0; ii < (int) (tmp.size() / 2.0); ii++) {
			t = tmp.get(ii);
			tmp.set(ii, tmp.get((tmp.size() - (ii + 1))));
			tmp.set((tmp.size() - (ii + 1)), t);
		}
		return tmp;
	}

	public static void sort_MyInsection_Proporzional_Growing(int[] a) {
		int lengthCurrentNewArray = 1, len = a.length, e = 0, posiz;
		int maxx = a[0], min = a[0];

		while(lengthCurrentNewArray < len) {
			if (a[lengthCurrentNewArray] > maxx) {
				maxx = a[lengthCurrentNewArray];
			} else if ((a[lengthCurrentNewArray] < min) && (a[lengthCurrentNewArray] != maxx)) {
				min = tempInt = a[lengthCurrentNewArray];
				// tempInt = a[ lengthCurrentNewArray ] ;
				// ora si scalano tutti gli elementi
				for (e = lengthCurrentNewArray; e > 0; e--) // ((e <=
				// lengthCurrentNewArray
				// ) && ( e < len))
				{
					a[e] = a[e - 1];
				}
				a[0] = tempInt;
			} else if (maxx != min) {
				posiz = (int) (((a[lengthCurrentNewArray] - min) / (maxx - min)) * (lengthCurrentNewArray - 1.0));
				if (a[posiz] > a[lengthCurrentNewArray]) {
					while((posiz > 0) && (a[posiz] > a[lengthCurrentNewArray])) {
						posiz--;
					}
					if (posiz < 0) {
						posiz = 0;
					} // se sono sceso sotto lo zero, mi setto a zero
					tempInt = a[lengthCurrentNewArray];
					// ora si scalano tutti gli elementi
					for (e = lengthCurrentNewArray; e > posiz; e--) // ((e <=
					// lengthCurrentNewArray
					// ) && ( e
					// < len))
					{
						a[e] = a[e - 1];
					}
					a[posiz] = tempInt;
				} else if (a[posiz] < a[lengthCurrentNewArray]) {
					while((posiz < lengthCurrentNewArray) && (a[posiz] <= a[lengthCurrentNewArray])) {
						posiz++;
					}
					if (posiz < a.length) {
						tempInt = a[lengthCurrentNewArray];
						// ora si scalano tutti gli elementi
						for (e = lengthCurrentNewArray; e > posiz; e--) // ((e
						// <=
						// lengthCurrentNewArray
						// ) &&
						// ( e <
						// len))
						{
							a[e] = a[e - 1];
						}
						a[posiz] = tempInt;
					}
				} else { // sono uguali
					tempInt = a[lengthCurrentNewArray];
					// ora si scalano tutti gli elementi
					for (e = lengthCurrentNewArray; e > posiz; e--) // ((e <=
					// lengthCurrentNewArray
					// ) && ( e
					// < len))
					{
						a[e] = a[e - 1];
					}
					a[posiz] = tempInt;
				}
			}
			lengthCurrentNewArray++;
		}
	}

	public static void sort_MyInsection_Proporzional_UnGrowing(int[] a) {
		int lengthCurrentNewArray = 1, len = a.length, e = 0, posiz; // tempInt
		// = 0,
		int maxx = a[0], min = a[0];

		while(lengthCurrentNewArray < len) {
			if (a[lengthCurrentNewArray] < min) {
				min = a[lengthCurrentNewArray];
			} else if ((a[lengthCurrentNewArray] > maxx) && (a[lengthCurrentNewArray] != min)) {
				maxx = tempInt = a[lengthCurrentNewArray];
				// tempInt = a[ lengthCurrentNewArray ] ;
				// ora si scalano tutti gli elementi
				for (e = lengthCurrentNewArray; e > 0; e--) // ((e <=
				// lengthCurrentNewArray
				// ) && ( e < len))
				{
					a[e] = a[e - 1];
				}
				a[0] = tempInt;
			} else if (maxx != min) {
				posiz = (int) ((1.0 - ((a[lengthCurrentNewArray] - min) / (maxx - min)))
						* (lengthCurrentNewArray - 1.0));
				if (a[posiz] > a[lengthCurrentNewArray]) {
					while((posiz < lengthCurrentNewArray) && (a[posiz] > a[lengthCurrentNewArray])) {
						posiz++;
					}
					if (posiz < a.length) {
						// posiz--;
						tempInt = a[lengthCurrentNewArray];
						// ora si scalano tutti gli elementi
						for (e = lengthCurrentNewArray; e > posiz; e--) // ((e
						// <=
						// lengthCurrentNewArray
						// ) &&
						// ( e <
						// len))
						{
							a[e] = a[e - 1];
						}
						a[posiz] = tempInt;
					}
				} else if (a[posiz] < a[lengthCurrentNewArray]) {
					while((posiz > 0) && (a[posiz] < a[lengthCurrentNewArray])) {
						posiz--;
					}
					tempInt = a[lengthCurrentNewArray];
					posiz++; // boh, it works
					// ora si scalano tutti gli elementi
					for (e = lengthCurrentNewArray; e > posiz; e--) // ((e <=
					// lengthCurrentNewArray
					// ) && ( e
					// < len))
					{
						a[e] = a[e - 1];
					}
					a[posiz] = tempInt;
				} else { // sono uguali
					tempInt = a[lengthCurrentNewArray];
					// ora si scalano tutti gli elementi
					for (e = lengthCurrentNewArray; e > posiz; e--) // ((e <=
					// lengthCurrentNewArray
					// ) && ( e
					// < len))
					{
						a[e] = a[e - 1];
					}
					a[posiz] = tempInt;
				}
			}
			lengthCurrentNewArray++;
		}
	}

	public static void sort_MyInsection_Linear_Growing(int[] a) {
		int i1 = 0, max = 1, e = 0; // tempInt = 0,
		while(max < a.length) {
			i1 = 0;
			while(i1 < max) // puntando al nuovo indice, cerco un elemento tra
			// quelli precedenti che sia ...
			{
				if (a[i1] >= a[max]) // maggiore di quello "nuovo" (max) .. se �
				// effettivamente maggiore, lo traslo
				{
					tempInt = a[max];
					// ora si scalano tutti gli elementi
					for (e = max; e > i1; e--) // ((e <= max) && ( e < len))
					{
						a[e] = a[e - 1];
					}
					a[i1] = tempInt;
				}
				i1++;
			}
			max++;
		}
	}

	public static void sort_MyInsection_Linear_UnGrowing(int[] a) {
		int i1 = 0, max = 1, len = a.length, e = 0; // tempInt = 0,
		while(max < len) {
			i1 = 0;
			while(i1 < max) // puntando al nuovo indice, cerco un elemento tra
			// quelli precedenti che sia ...
			{
				if (a[i1] <= a[max]) // maggiore di quello "nuovo" (max) .. se �
				// effettivamente maggiore, lo traslo
				{
					tempInt = a[max];
					// ora si scalano tutti gli elementi
					for (e = max; e > i1; e--) // ((e <= max) && ( e < len))
					{
						a[e] = a[e - 1];
					}
					a[i1] = tempInt;
				}
				i1++;
			}
			max++;
		}
	}

	public static ArrayList<Double> sort_MergeAlgorithm_Growing_DoubArrayList(ArrayList<Double> a) {
		return sort_MergeAlgorithm_Growing_DoubArrayList(a, 0, a.size() - 1); // sfrutta
		// la
		// ricorsivit�?
	}

	public static ArrayList<Double> sort_MergeAlgorithm_Growing_DoubArrayList(ArrayList<Double> a, double lo,
			double hi) {
		if (lo >= hi) {
			return null;
		}
		// uguale a if (hi < lo) { return; } //.. hi deve essere maggiore

		double mid = (lo + hi) / 2.0;

		sort_MergeAlgorithm_Growing_DoubArrayList(a, lo, mid);
		sort_MergeAlgorithm_Growing_DoubArrayList(a, (mid + 1), hi);

		int end_lo = (int) mid;
		int start_hi = (int) (mid + 1);

		while((lo <= end_lo) && (start_hi <= hi)) {
			if (a.get((int) lo) < a.get(start_hi)) {
				lo++;
			} else {
				double T = a.get(start_hi);

				for (int k = start_hi - 1; k >= lo; k--) {
					a.set(k + 1, a.get(k));
				}
				a.set((int) lo, T);
				lo++;
				end_lo++;
				start_hi++;
			}
		}
		return a;
	}

	public static ArrayList<Integer> sort_MergeAlgorithm_Growing_IntArrayList(ArrayList<Integer> a) {
		return sort_MergeAlgorithm_Growing_IntArrayList(a, 0, a.size() - 1); // sfrutta
		// la
		// ricorsivit�?
	}

	public static ArrayList<Integer> sort_MergeAlgorithm_Growing_IntArrayList(ArrayList<Integer> a, int lo, int hi) {
		if (lo >= hi) {
			return null;
		}
		// uguale a if (hi < lo) { return; } //.. hi deve essere maggiore

		int mid = (lo + hi) / 2;

		sort_MergeAlgorithm_Growing_IntArrayList(a, lo, mid);
		sort_MergeAlgorithm_Growing_IntArrayList(a, mid + 1, hi);

		int end_lo = mid;
		int start_hi = mid + 1;

		while((lo <= end_lo) && (start_hi <= hi)) {
			if (a.get(lo) < a.get(start_hi)) {
				lo++;
			} else {
				int T = a.get(start_hi);

				for (int k = start_hi - 1; k >= lo; k--) {
					a.set(k + 1, a.get(k));
				}
				a.set(lo, T);
				lo++;
				end_lo++;
				start_hi++;
			}
		}
		return a;
	}

	public static void sort_MergeAlgorithm_Growing_IntArray(int a[]) {
		sort_MergeAlgorithm_Growing_IntArray(a, 0, a.length - 1);
	}

	public static void sort_MergeAlgorithm_Growing_IntArray(int a[], int lo, int hi) {
		if (lo >= hi) {
			return;
		}
		// uguale a if (hi < lo) { return; } //.. hi deve essere maggiore

		int mid = (lo + hi) / 2;

		sort_MergeAlgorithm_Growing_IntArray(a, lo, mid);
		sort_MergeAlgorithm_Growing_IntArray(a, mid + 1, hi);

		int end_lo = mid;
		int start_hi = mid + 1;

		while((lo <= end_lo) && (start_hi <= hi)) {
			if (a[lo] < a[start_hi]) {
				lo++;
			} else {
				int T = a[start_hi];

				for (int k = start_hi - 1; k >= lo; k--) {
					a[k + 1] = a[k];
				}
				a[lo] = T;
				lo++;
				end_lo++;
				start_hi++;
			}
		}
	}

	/*
	 * public static int[] ordinamentoArrayInteger_ProporzionaleLineare (int[] lista
	 * , boolean growing) { return (growing == true) ?
	 * ordinamentoArrayInteger_Growing_ProporzionaleLineare(lista) :
	 * ordinamentoArrayInteger_UnGrowing_ProporzionaleLineare(lista) ; }
	 *
	 * public static int[] ordinamentoArrayInteger_UnGrowing_ProporzionaleLineare
	 * (int[] lista ) { return CastingClass.castIntegerArrayList_ToArray (
	 * ordinamentoArrayListInteger_UnGrowing_ProporzionaleLineare (
	 * CastingClass.castIntArray_ToArrayList(lista) ) ); }
	 *
	 * public static int[] ordinamentoArrayInteger_Growing_ProporzionaleLineare
	 * (int[] lista ) { return CastingClass.castIntegerArrayList_ToArray (
	 * ordinamentoArrayListInteger_Growing_ProporzionaleLineare (
	 * CastingClass.castIntArray_ToArrayList(lista) ) ); }
	 *
	 * public static double[] ordinamentoArrayDouble_ProporzionaleLineare (double[]
	 * lista , boolean growing) { return (growing == true) ?
	 * ordinamentoArrayDouble_Growing_ProporzionaleLineare(lista) :
	 * ordinamentoArrayDouble_UnGrowing_ProporzionaleLineare(lista) ; }
	 *
	 * public static double[] ordinamentoArrayDouble_UnGrowing_ProporzionaleLineare
	 * (double[] lista ) { return CastingClass.castDoubleArrayList_ToArray (
	 * ordinamentoArrayListDouble_UnGrowing_ProporzionaleLineare (
	 * CastingClass.castDoubleArray_ToArrayList(lista) ) ); }
	 *
	 * public static double[] ordinamentoArrayDouble_Growing_ProporzionaleLineare
	 * (double[] lista ) { return CastingClass.castDoubleArrayList_ToArray (
	 * ordinamentoArrayListDouble_Growing_ProporzionaleLineare (
	 * CastingClass.castDoubleArray_ToArrayList(lista) ) ); }
	 */

	public static ArrayList<Integer> sort_ProporzionaleLineare_ArrayListInteger(ArrayList<Integer> lista,
			boolean growing) {
		return growing ? sort_ProporzionaleLineare_Growing_ArrayListInteger(lista)
				: sort_ProporzionaleLineare_UnGrowing_ArrayListInteger(lista);
	}

	public static ArrayList<Integer> sort_ProporzionaleLineare_UnGrowing_ArrayListInteger(ArrayList<Integer> lista) {
		ArrayList<Integer> ret = new ArrayList<Integer>(); // lista.size());
		if (lista.size() > 1) {
			double max = lista.get(0);
			double min = lista.get(0);
			int posiz = 0; // ... ione
			ret.add(lista.get(0));
			for (int i1 = 1; i1 < lista.size(); i1++) {
				if (lista.get(i1) > max) {
					max = lista.get(i1);
					ret.add(0, lista.get(i1));
				} else if (lista.get(i1) < min) {
					min = lista.get(i1);
					ret.add(lista.get(i1));
				} else {
					if (max != min) // per evitare il diviso 0 di dopo
					{
						posiz = (int) ((1.0 - ((lista.get(i1) - min) / (max - min))) * (ret.size() - 1.0));
						// "1 - " � dato dal fatto che se lista.get(i) � molto
						// vicino al valore massimo, allora la frazione tende a
						// 1 e l'indice che ne risulta sarebbe prossimo a
						// .size().
						// Essendo l'ordine decrescente, il massimo � in
						// posizione 0, quindi la frazione dovrebbe fornire 0,
						// ossiaa (1- a/b)
						boolean add = false;
						if (ret.get(posiz) < lista.get(i1)) {
							if (posiz > 0) {
								for (int e = posiz - 1; ((e >= 0) && (!add)); e--) {
									if (ret.get(e) == lista.get(i1)) {
										ret.add(e, lista.get(i1));
										add = true;
									} else if (ret.get(e) > lista.get(i1)) {
										/*
										 * fintanto che l'elemento da aggiungere non � pi� grande, il ciclo
										 * prosegue.. altrimenti viene aggiunto e il ciclo si interrompe
										 */
										ret.add((e + 1), lista.get(i1));
										add = true;
									}
								}
								if (!add) {
									ret.add(0, lista.get(i1));
								}
							} else {
								ret.add(posiz, lista.get(i1));
							}
						} else if (ret.get(posiz) > lista.get(i1)) {
							if (posiz < (ret.size() - 1)) {
								for (int e = posiz + 1; ((e < ret.size()) && (!add)); e++) {
									if ((ret.get(e) == lista.get(i1)) || (ret.get(e) < lista.get(i1))) {
										ret.add(e, lista.get(i1));
										add = true;
									}
								}
								if (!add) {
									ret.add(lista.get(i1));
								}
							} else {
								ret.add(lista.get(i1));
							}
						} else {
							ret.add(posiz, lista.get(i1));
						}
					} else {
						ret.add(lista.get(i1));
					} // se massimo e minimo sono uguali, allora sia tutti i
						// numeri del "ret" sia quello da aggiungere sono
						// uguali, quindi lo posso aggiungere senza problemi
				}
			}
		} else {
			return lista;
		}
		return ret;
	}

	public static ArrayList<Integer> sort_ProporzionaleLineare_Growing_ArrayListInteger(ArrayList<Integer> lista) {
		ArrayList<Integer> ret = new ArrayList<Integer>(); // lista.size());
		if (lista.size() > 1) {
			double max = lista.get(0);
			double min = lista.get(0);
			int posiz = 0; // ... ione
			ret.add(lista.get(0));
			for (int i1 = 1; i1 < lista.size(); i1++) {
				if (lista.get(i1) > max) {
					max = lista.get(i1);
					ret.add(lista.get(i1));
				} else if (lista.get(i1) < min) {
					min = lista.get(i1);
					ret.add(0, lista.get(i1));
				} else {
					if (max != min) // per evitare il diviso 0 di dopo
					{
						posiz = (int) (((lista.get(i1) - min) / (max - min)) * (ret.size() - 1.0));
						boolean add = false;
						if (ret.get(posiz) < lista.get(i1)) {
							if (posiz < (ret.size() - 1)) {
								for (int e = posiz + 1; ((e < ret.size()) && (!add)); e++) {
									if ((ret.get(e) == lista.get(i1)) || (ret.get(e) > lista.get(i1))) {
										ret.add(e, lista.get(i1));
										add = true;
									}
								}
								if (!add) {
									ret.add(0, lista.get(i1));
								}
							} else {
								ret.add(lista.get(i1));
							}

						} else if (ret.get(posiz) > lista.get(i1)) {
							if (posiz > 0) {
								for (int e = posiz - 1; ((e >= 0) && (!add)); e--) {
									if (ret.get(e) == lista.get(i1)) {
										ret.add(e, lista.get(i1));
										add = true;
									} else if (ret.get(e) < lista.get(i1)) { // fintanto
										// che
										// l'elemento
										// da
										// aggiungere
										// �
										// pi�
										// grande,
										// il
										// ciclo
										// prosegue..
										// altrimenti
										// viene
										// aggiunto
										// e
										// il
										// ciclo
										// si
										// interrompe
										ret.add((e + 1), lista.get(i1));
										add = true;
									}
								}
								if (!add) {
									ret.add(0, lista.get(i1));
								}
							} else {
								ret.add(posiz, lista.get(i1));
							}
						} else {
							ret.add(posiz, lista.get(i1));
						}
					} else {
						ret.add(lista.get(i1));
					} // se massimo e minimo sono uguali, allora sia tutti i
						// numeri del "ret" sia quello da aggiungere sono
						// uguali, quindi lo posso aggiungere senza problemi
				}
			}
		} else {
			return lista;
		}
		return ret;
	}

	public static ArrayList<Double> sort_ProporzionaleLineare_ArrayListDouble(ArrayList<Double> lista,
			boolean growing) {
		return growing ? sort_ProporzionaleLineare_Growing_ArrayListDouble(lista)
				: sort_ProporzionaleLineare_UnGrowing_ArrayListDouble(lista);
	}

	public static ArrayList<Double> sort_ProporzionaleLineare_UnGrowing_ArrayListDouble(ArrayList<Double> lista) {
		ArrayList<Double> ret = new ArrayList<Double>(); // lista.size());
		if (lista.size() > 1) {
			double max = lista.get(0);
			double min = lista.get(0);
			int posiz = 0; // ... ione
			ret.add(lista.get(0));
			for (int i1 = 1; i1 < lista.size(); i1++) {
				if (lista.get(i1) > max) {
					max = lista.get(i1);
					ret.add(0, lista.get(i1));
				} else if (lista.get(i1) < min) {
					min = lista.get(i1);
					ret.add(lista.get(i1));
				} else {
					if (max != min) // per evitare il diviso 0 di dopo
					{
						posiz = (int) ((1.0 - ((lista.get(i1) - min) / (max - min))) * (ret.size() - 1.0));
						// "1 - " � dato dal fatto che se lista.get(i) � molto
						// vicino al valore massimo, allora la frazione tende a
						// 1 e l'indice che ne risulta sarebbe prossimo a
						// .size().
						// Essendo l'ordine decrescente, il massimo � in
						// posizione 0, quindi la frazione dovrebbe fornire 0,
						// ossiaa (1- a/b)
						boolean add = false;
						if (ret.get(posiz) < lista.get(i1)) {
							if (posiz > 0) {
								for (int e = posiz - 1; ((e >= 0) && (!add)); e--) {
									if (ret.get(e) == lista.get(i1)) {
										ret.add(e, lista.get(i1));
										add = true;
									} else if (ret.get(e) > lista.get(i1)) { // fintanto
										/*
										 * che l'elemento da aggiungere non � pi� grande, il ciclo prosegue..
										 * altrimenti viene aggiunto e il ciclo si interrompe
										 */
										ret.add((e + 1), lista.get(i1));
										add = true;
									}
								}
								if (!add) {
									ret.add(0, lista.get(i1));
								}
							} else {
								ret.add(posiz, lista.get(i1));
							}
						} else if (ret.get(posiz) > lista.get(i1)) {
							if (posiz < (ret.size() - 1)) {
								for (int e = posiz + 1; ((e < ret.size()) && (!add)); e++) {
									if ((ret.get(e) == lista.get(i1)) || (ret.get(e) < lista.get(i1))) {
										ret.add(e, lista.get(i1));
										add = true;
									}
								}
								if (!add) {
									ret.add(lista.get(i1));
								}
							} else {
								ret.add(lista.get(i1));
							}
						} else {
							ret.add(posiz, lista.get(i1));
						}
					} else {
						ret.add(lista.get(i1));
					} // se massimo e minimo sono uguali, allora sia tutti i
						// numeri del "ret" sia quello da aggiungere sono
						// uguali, quindi lo posso aggiungere senza problemi
				}
			}
		} else {
			return lista;
		}
		return ret;
	}

	public static ArrayList<Double> sort_ProporzionaleLineare_Growing_ArrayListDouble(ArrayList<Double> lista) // ordinamentoArrayListDouble
	{
		ArrayList<Double> ret = new ArrayList<Double>(); // lista.size());
		if (lista.size() > 1) {
			double max = lista.get(0);
			double min = lista.get(0);
			int posiz = 0; // ... ione
			ret.add(lista.get(0));
			for (int i1 = 1; i1 < lista.size(); i1++) {
				if (lista.get(i1) > max) {
					max = lista.get(i1);
					ret.add(lista.get(i1));
				} else if (lista.get(i1) < min) {
					min = lista.get(i1);
					ret.add(0, lista.get(i1));
				} else {
					if (max != min) // per evitare il diviso 0 di dopo
					{
						posiz = (int) (((lista.get(i1) - min) / (max - min)) * (ret.size() - 1.0));
						boolean add = false;
						if (ret.get(posiz) < lista.get(i1)) {
							if (posiz < (ret.size() - 1)) {
								for (int e = posiz + 1; ((e < ret.size()) && (!add)); e++) {
									if ((ret.get(e) == lista.get(i1)) || (ret.get(e) > lista.get(i1))) {
										ret.add(e, lista.get(i1));
										add = true;
									}
								}
								if (!add) {
									ret.add(0, lista.get(i1));
								}
							} else {
								ret.add(lista.get(i1));
							}

						} else if (ret.get(posiz) > lista.get(i1)) {
							if (posiz > 0) {
								for (int e = posiz - 1; ((e >= 0) && (!add)); e--) {
									if (ret.get(e) == lista.get(i1)) {
										ret.add(e, lista.get(i1));
										add = true;
									} else if (ret.get(e) < lista.get(i1)) {
										/*
										 * fintanto che l'elemento da aggiungere � pi� grande, il ciclo prosegue..
										 * altrimenti viene aggiunto e il ciclo si interrompe
										 */
										ret.add((e + 1), lista.get(i1));
										add = true;
									}
								}
								if (!add) {
									ret.add(0, lista.get(i1));
								}
							} else {
								ret.add(posiz, lista.get(i1));
							}
						} else {
							ret.add(posiz, lista.get(i1));
						}
					} else {
						ret.add(lista.get(i1));
					} // se massimo e minimo sono uguali, allora sia tutti i
						// numeri del "ret" sia quello da aggiungere sono
						// uguali, quindi lo posso aggiungere senza problemi
				}
			}
		} else {
			return lista;
		}
		return ret;
	}

	/***
	 * Ricerca un elemento nell'array dato. Se esiste, il metodo ritorna un numero
	 * positivo, ossia l'indice in cui si riscontra la prima ricorrenza di tale
	 * elemento . Se l'elemento cercato non esiste nell'array, o quest'ultimo �
	 * "null", restituisce -1 .
	 */
	public static int doesTheElementExist(int[] a, int e) {
		int ret = -1;
		if (a != null) {
			if (a.length > 0) {
				int i1 = 0, len = a.length;
				while(i1 < len && ret < 0) {
					ret = (a[i1] == e) ? i1 : -1;
					i1++;
				}
			}
		}
		return ret;
	}

	/***
	 * Ricerca un elemento nell'array dato. Se esiste, il metodo ritorna un numero
	 * positivo, ossia l'indice in cui si riscontra la prima ricorrenza di tale
	 * elemento . Se l'elemento cercato non esiste nell'array, o quest'ultimo �
	 * "null", restituisce -1 .
	 */
	public static int doesTheElementExist(double[] a, double e) {
		int ret = -1;
		if (a != null) {
			if (a.length > 0) {
				int i1 = 0, len = a.length;
				while(i1 < len && ret < 0) {
					ret = (a[i1] == e) ? i1 : -1;
					i1++;
				}
			}
		}
		return ret;
	}

	/***
	 * Ricerca un elemento nell'array dato. Se esiste, il metodo ritorna un numero
	 * positivo, ossia l'indice in cui si riscontra la prima ricorrenza di tale
	 * elemento . Se l'elemento cercato non esiste nell'array, o quest'ultimo �
	 * "null", restituisce -1 .
	 */
	public static int doesTheElementExist(float[] a, float e) {
		int ret = -1;
		if (a != null) {
			if (a.length > 0) {
				int i1 = 0, len = a.length;
				while(i1 < len && ret < 0) {
					ret = (a[i1] == e) ? i1 : -1;
					i1++;
				}
			}
		}
		return ret;
	}

	/***
	 * Ricerca un elemento nell'array dato. Se esiste, il metodo ritorna un numero
	 * positivo, ossia l'indice in cui si riscontra la prima ricorrenza di tale
	 * elemento . Se l'elemento cercato non esiste nell'array, o quest'ultimo �
	 * "null", restituisce -1 .
	 */
	public static int doesTheElementExist(long[] a, long e) {
		int ret = -1;
		if (a != null) {
			if (a.length > 0) {
				int i1 = 0, len = a.length;
				while(i1 < len && ret < 0) {
					ret = (a[i1] == e) ? i1 : -1;
					i1++;
				}
			}
		}
		return ret;
	}

	public static double assoluto(double d) {
		return (d < 0) ? -d : d;
	}

	public static double modulo(double[] a) {
		double ret = 0; // standard per array con lunghezza 0
		if (a != null) {
			if (a.length == 1) {
				ret = assoluto(a[0]);
			} else if (a.length > 1) { // teorema di pitagora esteso a
				// (a.length-1) dimensioni
				for (int ii = 0; ii < a.length; ii++) {
					ret += (a[ii] * a[ii]);
				}
				ret = Math.sqrt(ret);
			}
		}
		return ret;
	}

	public static double modulo(int[] a) {
		double ret = 0; // standard per array con lunghezza 0
		if (a != null) {
			if (a.length == 1) {
				ret = assoluto(a[0]);
			} else if (a.length > 1) { // teorema di pitagora esteso a
				// (a.length-1) dimensioni
				for (int ii = 0; ii < a.length; ii++) {
					ret += (a[ii] * a[ii]);
				}
				ret = Math.sqrt(ret);
			}
		}
		return ret;
	}

	public static String[] getStringCopy(String[] s) {
		String[] ret = null;
		if (s != null) {
			ret = new String[s.length];
			for (int i1 = 0; i1 < s.length; i1++) {
				ret[i1] = getStringCopy(s[i1]);
			}
		}
		return ret;
	}

	public static String getStringCopy(String s) {
		return new String(s).concat(" ").substring(0, s.length() - 1);
		// cos� sono sicuro che mi crei una nuova istanza di stringa
	}

	//

	//

	/** Retur null if the parameters are incorrect */
	public static BufferedImage getUniformBufferedImage(int width, int height, int value) {
		BufferedImage ret = null;
		if (width > 0 && height > 0) {
			ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			int r, c;
			for (r = 0; r < height; r++) {
				for (c = 0; c < width; c++) {
					ret.setRGB(c, r, value);
				}
			}
		}
		return ret;
	}

	//

	//

	//

	//

	// TODO COSE FIGHE

	// TODO disegna cerchi

	/**
	 *
	 * This method control the rightness of the parameters and if all it's ok, calls
	 * {@link #drawCircle_WithoutCkecks(int[][], int, int, int, int)}.
	 */
	public static boolean drawCircle(int x, int y, int ray, int[][] matrix, int argb) {
		boolean ok;
		int lastPoint, diameter;
		int[] riga;
		int r, rr, l;

		if (matrix == null) {
			return false;
		}

		ok = x > 0 && y > 0 && ray > 0 && y < matrix.length;
		if (ok) {
			diameter = (ray << 1) + 1;
			riga = matrix[y]; // controllo alla veloce
			ok &= ((lastPoint = (x + diameter)) < riga.length) && ((y + diameter) < matrix.length);
			if (ok) {

				// ora controlliamo che il cerchio stia ben dentro alla matrice
				r = 1;
				rr = y + 1;
				l = riga.length;
				/*
				 * "+1" perchè il controllo sulla y-esima riga è gia stato fatto
				 */
				while(ok && r++ < diameter) {
					ok = (riga = matrix[rr++]) != null;
					if (ok) {
						/*
						 * set into "l" the minimum between the shortest row and the actual one
						 */
						ok = lastPoint < (l = (riga.length < l ? riga.length : l));
					}
				}
				// drawCircle_Safe(x, y, r, matrix, argb);
				if (ok) {
					drawCircle_WithoutCkecks(matrix, x, y, ray, argb);
				}
			}
		}
		if (!ok) {
			drawCircle_SafeOutOfBounds(matrix, x, y, ray, argb);
		}
		return ok;
	}

	/**
	 * Draw the border of a circle.<br>
	 * The painted circle will have <code>(ray*2 +1)</code> diameter, so beware.
	 * <br>
	 * The point P(x,y) is the left-top vertex of the bounding square, so beware and
	 * adjust. <br>
	 * The combination of coordinates and ray is considered SAFE inside the code, as
	 * like as the matrix isn't null and is rectangular .. so the method could throw
	 * <code>IndexOutOfBoundsException</code> and <code>NullPointerException</code>.
	 * <p>
	 * BEWARE : there is any kind of check!<br>
	 * If checks are neededs, call
	 * {@link GeometryDrawer#drawCircle_BetterAndSafe(int, int, int, int[][], int)}
	 * instead.
	 *
	 * @throw <code>IndexOutOfBoundsException</code> and
	 *        <code>NullPointerException</code> because this method do no kind of
	 *        check.
	 * @param m    the matrix where to paint the circle
	 * @param x    the x-coordinate of the left-top bunding box's vertex
	 * @param y    the y-coordinate of the left-top bunding box's vertex
	 * @param ray  the ray of the circle .. the diameter will be
	 *             <code>(ray*2 +1)</code>
	 * @param argb the value (also a color, or whatever you want) to place into the
	 *             circle's border to draw it
	 */
	public static void drawCircle_WithoutCkecks(int[][] m, int x, int y, int ray, int argb) {
		if (ray > 0) {
			int[] rigaSup = null, rigaInf = null, rigaCenterSup = null, rigaCenterInf = null;
			if (ray == 1) {
				m[y][x + 1] = (rigaSup = m[y + 1])[x] = //
						rigaSup[x + 2] = m[y + 2][x + 1] = argb;
				/**
				 * } if (ray == 2) {// <br>
				 * rigaSup = m[y]; // <br>
				 * rigaSup[x + 2] = rigaSup[x + 1] = rigaSup[x + 3] = argb; // <br>
				 * rigaSup = m[y + 4]; // <br>
				 * rigaSup[x + 2] = rigaSup[x + 1] = rigaSup[x + 3] = argb; // <br>
				 * rigaSup = m[y + 1]; // <br>
				 * rigaInf = m[y + 3]; // <br>
				 * rigaCenterSup = m[y + 2]; // <br>
				 * rigaSup[x] = rigaSup[x + 4] = rigaInf[x] = rigaInf[x + 4] = rigaCenterSup[x]
				 * = rigaCenterSup[x + 4] = argb; // <br>
				 * }// <br>
				 * if (ray == 3) {// <br>
				 * rigaSup = m[y]; // <br>
				 * rigaSup[x + 2] = rigaSup[x + 3] = rigaSup[x + 4] = argb; // <br>
				 * rigaSup = m[y + 6]; // <br>
				 * rigaSup[x + 2] = rigaSup[x + 3] = rigaSup[x + 4] = argb; // <br>
				 * // <br>
				 * rigaSup = m[y + 1]; // <br>
				 * rigaInf = m[y + 5]; // <br>
				 * rigaSup[x + 1] = rigaSup[x + 5] = rigaInf[x + 1] = rigaInf[x + 5] = argb; //
				 * <br>
				 * rigaSup = m[y + 3]; // <br>
				 * rigaInf = m[y + 4]; // <br>
				 * rigaCenterSup = m[y + 2]; // <br>
				 * rigaSup[x] = rigaSup[x + 6] = rigaInf[x] = rigaInf[x + 6] = rigaCenterSup[x]
				 * = rigaCenterSup[x + 6] = argb; // <br>
				 */
			} else {
				int r = 0, c = 0, ray2 = ray << 1, ray_1 = ray - 1, halfRay = (ray >> 1) + ray % 2, rInf,
						ray1 = ray + 1, horizontalSymmetricOldC;
				double rRay = ray + 0.5;
				// disegno i punti cardinali
				rigaSup = m[ray + y];
				rigaSup[x] = rigaSup[ray2 + x] = m[y][ray + x] = m[ray2 + y][ray + x] = argb;
				/*
				 * aggiusto le righe in quanto inizio non dalla riga centrale, ma dalla prima e
				 * dal centro di essa rigaSup = m[0]; rigaInf = m[ray2];
				 */
				horizontalSymmetricOldC = ray1;
				rInf = ray2;
				c = ray_1;
				for (r = 0; r < halfRay; r++, rInf--) {

					// aggiorno le righe su cui devo lavorare

					rigaSup = m[r + y];
					rigaInf = m[rInf + y];
					/*
					 * ora sposto il puntatore "c" dal centro verso l'origine, fino a quando non
					 * supero il cerchio ideale, al che rientro .. uso ray1 per questioni di
					 * approssimazione grafica.. dopodichè traccio la congiungente
					 */

					while(0 < c && (Math.hypot(ray - c, (ray - r)) < rRay)) {
						/*
						 * Sapendo che il cerchio l'ho diviso in otto spicchi, coloro prima la fetta che
						 * dall'(abituale) angolo 90 va fino al 135, poi tutti i simmetrici .. il primo
						 * è il più a sinistra di tutti, seguito da quello in range (90-45), a quello
						 * (270-225), poi (270-315)
						 */
						rigaSup[c + x] = //
								rigaSup[horizontalSymmetricOldC + x] = //
										rigaInf[c + x] = //
												rigaInf[horizontalSymmetricOldC + x] = argb;
						/*
						 * poi i VERTICALI D: ... prima (180-135) , poi (0-45), poi (180-225), poi
						 * (0-315)
						 */
						// ottengo i puntatori alle righe per ottimizzare;
						rigaCenterSup = m[c + y];
						rigaCenterInf = m[horizontalSymmetricOldC + y];
						// coloro
						rigaCenterSup[r
								+ x] = rigaCenterSup[rInf + x] = rigaCenterInf[r + x] = rigaCenterInf[rInf + x] = argb;

						horizontalSymmetricOldC++;
						c--;
					}
				} // fine ciclo r
			}
		}
	}

	/**
	 * Ray 1 is not OutOfBoundsSafe.<br>
	 * See {@link #drawCircle_WithoutCkecks(int[][], int, int, int, int)} for
	 * further information
	 */

	public static void drawCircle_SafeOutOfBounds(int[][] m, int x, int y, int ray, int argb) {
		if (ray > 0) {
			int[] rigaSup = null, rigaInf = null, rigaCenterSup = null, rigaCenterInf = null;
			if (ray == 1) {
				m[y][x + 1] = (rigaSup = m[y + 1])[x] = //
						rigaSup[x + 2] = m[y + 2][x + 1] = argb;
				/**
				 * } if (ray == 2) {// <br>
				 * rigaSup = m[y]; // <br>
				 * rigaSup[x + 2] = rigaSup[x + 1] = rigaSup[x + 3] = argb; // <br>
				 * rigaSup = m[y + 4]; // <br>
				 * rigaSup[x + 2] = rigaSup[x + 1] = rigaSup[x + 3] = argb; // <br>
				 * rigaSup = m[y + 1]; // <br>
				 * rigaInf = m[y + 3]; // <br>
				 * rigaCenterSup = m[y + 2]; // <br>
				 * rigaSup[x] = rigaSup[x + 4] = rigaInf[x] = rigaInf[x + 4] = rigaCenterSup[x]
				 * = rigaCenterSup[x + 4] = argb; // <br>
				 * }// <br>
				 * if (ray == 3) {// <br>
				 * rigaSup = m[y]; // <br>
				 * rigaSup[x + 2] = rigaSup[x + 3] = rigaSup[x + 4] = argb; // <br>
				 * rigaSup = m[y + 6]; // <br>
				 * rigaSup[x + 2] = rigaSup[x + 3] = rigaSup[x + 4] = argb; // <br>
				 * // <br>
				 * rigaSup = m[y + 1]; // <br>
				 * rigaInf = m[y + 5]; // <br>
				 * rigaSup[x + 1] = rigaSup[x + 5] = rigaInf[x + 1] = rigaInf[x + 5] = argb; //
				 * <br>
				 * rigaSup = m[y + 3]; // <br>
				 * rigaInf = m[y + 4]; // <br>
				 * rigaCenterSup = m[y + 2]; // <br>
				 * rigaSup[x] = rigaSup[x + 6] = rigaInf[x] = rigaInf[x + 6] = rigaCenterSup[x]
				 * = rigaCenterSup[x + 6] = argb; // <br>
				 */
			} else {
				int r = 0, c = 0, ray2 = ray << 1, ray_1 = ray - 1, halfRay = (ray >> 1) + ray % 2, rInf,
						ray1 = ray + 1, horizontalSymmetricOldC, c_x, hc_x, r_y, rInf_y, c_y, hc_y, r_x, rInf_x;
				boolean canUsErigaSup, canUsErigaInf, canUsErigaCenterSup, canUsErigaCenterInf;
				double rRay = ray + 0.5;// (r5 ? 0.25 : 0);

				{// disegno i punti cardinali
					int ray_y = ray + y, ray2_x, ray_x, ray2_y;
					if (ray_y >= 0 && ray_y < m.length) {
						rigaSup = m[ray_y];
						if (x >= 0 && x < rigaSup.length) {
							rigaSup[x] = argb;
						}
						ray2_x = ray2 + x;
						if (ray2_x >= 0 && ray2_x < rigaSup.length) {
							rigaSup[ray2_x] = argb;
						}
					}
					ray_x = ray + x;
					if (ray_x >= 0) {
						int rrrr[];
						if (y >= 0 && y < m.length) {
							rrrr = m[y];
							if (ray_x >= 0 && ray_x < rrrr.length) {
								rrrr[ray_x] = argb;
							}
						}
						ray2_y = ray2 + y;
						if (ray2_y >= 0 && ray2_y < m.length) {
							rrrr = m[ray2_y];
							if (ray_x >= 0 && ray_x < rrrr.length) {
								rrrr[ray_x] = argb;
							}
						}
					}
				} // fine blocco punti cardinali

				/*
				 * aggiusto le righe in quanto inizio non dalla riga centrale, ma dalla prima e
				 * dal centro di essa rigaSup = m[0]; rigaInf = m[ray2];
				 */
				horizontalSymmetricOldC = ray1;
				rInf = ray2;
				c = ray_1;
				for (r = 0; r < halfRay; r++, rInf--) {
					/*
					 * aggiorno le righe su cui devo lavorare
					 */

					r_y = r + y;
					canUsErigaSup = r_y >= 0 && r_y < m.length;
					if (canUsErigaSup) {
						rigaSup = m[r_y];
					}

					rInf_y = rInf + y;
					canUsErigaInf = rInf_y >= 0 && rInf_y < m.length;
					if (canUsErigaInf) {
						rigaInf = m[rInf_y];
					}
					/*
					 * if (r5 && r > 0) { rRay = ray; }
					 */
					/*
					 * ora sposto il puntatore "c" dal centro verso l'origine, fino a quando non
					 * supero il cerchio ideale, al che rientro .. uso ray1 per questioni di
					 * approssimazione grafica.. dopodichè traccio la congiungente
					 */
					// oldc = c--;
					// horizontalSymmetricOldC = altezza - c;

					while(0 < c && (Math.hypot(ray - c, (ray - r)) < rRay)) {

						/*
						 * Sapendo che il cerchio l'ho diviso in otto spicchi, coloro prima la fetta che
						 * dall'(abituale) angolo 90 va fino al 135, poi tutti i simmetrici .. il primo
						 * è il più a sinistra di tutti, seguito da quello in range (90-45), a quello
						 * (270-225), poi (270-315)
						 */
						c_x = c + x;
						hc_x = horizontalSymmetricOldC + x;
						if (canUsErigaSup) {
							if (c_x >= 0 && c_x < rigaSup.length) {
								rigaSup[c_x] = argb;
							}
							if (hc_x >= 0 && hc_x < rigaSup.length) {
								rigaSup[hc_x] = argb;
							}
						}
						if (canUsErigaInf) {
							if (c_x >= 0 && c_x < rigaInf.length) {
								rigaInf[c_x] = argb;
							}
							if (hc_x >= 0 && hc_x < rigaInf.length) {
								rigaInf[hc_x] = argb;
							}
						}
						/*
						 * poi i VERTICALI D: ... prima (180-135) , poi (0-45), poi (180-225), poi
						 * (0-315)
						 */
						// ottengo i puntatori alle righe per ottimizzare;

						// "+y" è in dubbio
						c_y = c + y;
						canUsErigaCenterSup = c_y >= 0 && c_y < m.length;
						if (canUsErigaCenterSup) {
							rigaCenterSup = m[c_y];
						}
						hc_y = horizontalSymmetricOldC + y;
						canUsErigaCenterInf = hc_y >= 0 && hc_y < m.length;
						if (canUsErigaCenterInf) {
							rigaCenterInf = m[hc_y];
						}

						// coloro
						r_x = r + x;
						rInf_x = rInf + x;
						if (canUsErigaCenterSup) {
							if (r_x >= 0 && r_x < rigaCenterSup.length) {
								rigaCenterSup[r_x] = argb;
							}
							if (rInf_x >= 0 && rInf_x < rigaCenterSup.length) {
								rigaCenterSup[rInf_x] = argb;
							}
						}
						if (canUsErigaCenterInf) {
							if (r_x >= 0 && r_x < rigaCenterInf.length) {
								rigaCenterInf[r_x] = argb;
							}
							if (rInf_x >= 0 && rInf_x < rigaCenterInf.length) {
								rigaCenterInf[rInf_x] = argb;
							}
						}

						horizontalSymmetricOldC++;
						// old
						c--;
					}

				} // fine ciclo r
			}
		}
	}

	public static void fillCircle_Unsafe(int[][] m, int x, int y, int ray, int argb) {
		if (ray > 0) {
			if (ray > 8) {
				fillCircle_Unsafe_Tuned_OverRay8(m, x, y, ray, argb);
			} else {
				fillCircle_Unsafe_WorksPerfect(m, x, y, ray, argb);
			}
		}
	}

	/**
	 * EUREKA FUNZIONA correttamente.<br>
	 * Presenta operazioni ridondanti solo su alcuni pixel, come documentato da
	 * {@link #fillCircle_Unsafe_Tuned_OverRay8(int[][], int, int, int, int)}.
	 * <p>
	 * N.B.: NO CHECK IS PERFORMED ABOUT {@link NullPointerException} AND
	 * {@link IndexOutOfBoundsException}.
	 *
	 * @param m    the integer matrix to write the disc
	 * @param x    the x-component of the top-left corner of the bounding box of the
	 *             disc
	 * @param y    the y-component of the top-left corner of the bounding box of the
	 *             disc
	 * @param ray  the ray of the circle. BEWARE: the diameter will be
	 *             <code>(2*ray)+1</code>.
	 * @param argb the value used to write the disc
	 */
	public static void fillCircle_Unsafe_WorksPerfect(int[][] m, int x, int y, int ray, int argb) {
		int r, c, ray2, rInf, ray1, ray_1, horizontalSymmetricC, oldc, oldHorizC, halfRay, l,
				yPlusDiameter/* , logray */;
		int[] rigaSup, rigaInf, rigaCenterSup, rigaCenterInf;
		double rRay;
		if (ray > 0) {
			if (ray == 1) {
				m[y][x + 1] = (rigaSup = m[y + 1])[x] = //
						rigaSup[x + 1] = rigaSup[x + 2] = m[y + 2][x + 1] = argb;
			} else {
				r = 0;
				c = 0;
				ray2 = ray << 1;
				ray_1 = ray - 1;
				halfRay = (ray >> 1) + ray % 2;
				ray1 = ray + 1;
				rRay = ray + 0.5;
				// disegno i punti cardinali
				rigaSup = m[ray + y];
				rigaSup[x] = rigaSup[ray2 + x] = m[y][ray + x] = m[ray2 + y][ray + x] = argb;
				/*
				 * aggiusto le righe in quanto inizio non dalla riga centrale, ma dalla prima e
				 * dal centro di essa rigaSup = m[0]; rigaInf = m[ray2];
				 */
				horizontalSymmetricC = ray1;
				rInf = ray2;
				c = ray_1;
				yPlusDiameter = y + ray2;

				// logray = MetodiVari.bitMinNeeded(ray);

				for (r = 0; r < halfRay; r++, rInf--) {

					// aggiorno le righe su cui devo lavorare

					rigaSup = m[r + y];
					rigaInf = m[rInf + y];
					/*
					 * ora sposto il puntatore "c" dal centro verso l'origine, fino a quando non
					 * supero il cerchio ideale, al che rientro .. uso ray1 per questioni di
					 * approssimazione grafica.. dopodichè traccio la congiungente
					 */

					oldc = c;
					oldHorizC = horizontalSymmetricC;

					while(0 < c && (Math.hypot(ray - c, (ray - r)) < rRay)) {
						/*
						 * Sapendo che il cerchio l'ho diviso in otto spicchi, coloro prima la fetta che
						 * dall'(abituale) angolo 90 va fino al 135, poi tutti i simmetrici .. il primo
						 * è il più a sinistra di tutti, seguito da quello in range (90-45), a quello
						 * (270-225), poi (270-315)
						 */
						rigaSup[c + x] = //
								rigaSup[horizontalSymmetricC + x] = //
										rigaInf[c + x] = //
												rigaInf[horizontalSymmetricC + x] = argb;
						/*
						 * poi i VERTICALI D: ... prima (180-135) , poi (0-45), poi (180-225), poi
						 * (0-315)
						 */
						// ottengo i puntatori alle righe per ottimizzare;
						rigaCenterSup = m[c + y];
						rigaCenterInf = m[horizontalSymmetricC + y];
						// coloro
						rigaCenterSup[r
								+ x] = rigaCenterSup[rInf + x] = rigaCenterInf[r + x] = rigaCenterInf[rInf + x] = argb;

						// disegno le righe vicine al centro

						// if (r <= logray) {
						l = (ray2 - (r << 1)) - 1;
						drawHorizontalSpan(x + r + 1, y + c, l, m, argb);
						drawHorizontalSpan(x + r + 1, yPlusDiameter - c, l, m, argb);
						// }
						horizontalSymmetricC++;
						c--;
					}

					l = (oldHorizC - oldc) - 1;
					drawHorizontalSpan(x + oldc + 1, r + y, l, m, argb);
					drawHorizontalSpan(x + oldc + 1, yPlusDiameter - r, l, m, argb);
				} // fine ciclo r
					// last line, the horizontal diameter
				drawHorizontalSpan(x + 1, y + ray, ray2 - 1, m, argb);
			}
		}
	}

	/**
	 * ULTIMO FATTO (28/06/2016), BELLISSIMO, MA SBAGLIA i primi 8 raggi.<br>
	 * Ottimizzato rispetto a
	 * {@link #fillCircle_Unsafe_WorksPerfect(int[][], int, int, int, int)} secondo
	 * il seguente test : dopo 500'000 scritture su una matrice, con raggio 75,
	 * questo metodo impiega circa 33 secondi, l'altro 32. Quindi, mediamente, è
	 * migliore di un secondo ogni 500'000 operazioni.<br>
	 * Lo scarto di tempo può essere significativo solo con raggi più grandi.
	 */
	public static void fillCircle_Unsafe_Tuned_OverRay8(int[][] m, int x, int y, int ray, int argb) {
		int r, c, halfray, diameter, horizontalSymmetricC, oldc, oldHorizC, yPlusDiameter, l;
		double rRay;
		int[] rigaSup, rigaInf;
		if (ray > 0) {
			if (ray <= 8) {
				fillCircle_Unsafe_WorksPerfect(m, x, y, ray, argb);
			} else {
				/**
				 * Idea : ciclo dalla cima fino a metà raggio, disegnando righe orizzontali. La
				 * sezione da metà raggio fino al "diametro orizzontale", è speculare alla
				 * prima, quindi prendo le coordinate similari e traccio le linee orizzontali
				 * rimanenti. Infatti, prima disegno la cima e il fondo del cerchio,
				 * espandendomi e avvicinandomi al centro, poi per simmetria disegno dai punti
				 * di estrema sinistra e destra avvicinandomi ai poli. <br>
				 * Infine, traccio il diametro orizzontale.<br>
				 * Per ogni riga, tengo traccia del punto (della "x") da cui sono partito e mi
				 * muovo in moto retrogrado fintanto che rimango "entro il cerchio". Nel mentre,
				 * disegno. Appena esco, traccio la linea orizzontale a partire da quel
				 * punto-traccia, poi lo aggiorno.
				 */
				halfray = (ray >> 1) - 1/* + (ray & 0x1) */;
				r = -1;
				c = oldc = ray - 1;
				horizontalSymmetricC = oldHorizC = ray + 2;
				diameter = (ray << 1) + 1;
				rRay = ray + 0.5;
				yPlusDiameter = (y + diameter) - 1;

				while(++r < halfray) {

					rigaSup = m[y + r];
					rigaInf = m[yPlusDiameter - r];

					while(0 <= c && (Math.hypot(ray - c, (ray - r)) < rRay)) {
						rigaSup[x + c] = rigaSup[horizontalSymmetricC] = // argb;
								rigaInf[x + c] = rigaInf[horizontalSymmetricC] = argb;

						/*
						 * ora disegno il corrispettivo simmetrico a partire dalle estremità sinistra e
						 * destra
						 */
						// temp = m[yOfCenter - c];
						// temp[x + r] = temp[xPlusDiameter - r] = argb;

						l = diameter - (r << 1);
						drawHorizontalSpan(x + r, y + c, l, m, argb);
						// drawHorizontalSpan(x + r, y + (horizontalSymmetricC -
						// 1), l, m, argb);
						drawHorizontalSpan(x + r, yPlusDiameter - c, l, m, argb);

						c--;
						horizontalSymmetricC++;
					}

					/*
					 * drawHorizontalSpan(x + oldc, y + r, ((ray - c) << 1) + 1, m, argb);
					 */
					l = (oldHorizC - oldc) - 1;
					drawHorizontalSpan(x + oldc + 1, y + r, l, m, argb);
					drawHorizontalSpan(x + oldc + 1, yPlusDiameter - r, l, m, argb);

					oldc = c;
					oldHorizC = horizontalSymmetricC;

				}

				// diametro orizzontale
				drawHorizontalSpan(x, y + ray, diameter, m, argb);
			}
		}
	}

	private static boolean allInside(int[][] m, int x, int y, int height) {
		boolean b;
		int[] row;

		b = x >= 0 && inside(y, height);
		if (b) {
			row = m[y];
			b = row != null && x < row.length;
		}
		return b;
	}

	private static boolean inside(int n, int max) {
		return n >= 0 && n < max;
	}

	public static void fillCircle_Safe2(int[][] m, int x, int y, int ray, int argb) {
		int[] rigaSup, rigaInf;
		int r, c, ray2, rInf, ray1, horizontalSymmetricC, oldc, oldHorizC, height;
		long l;
		double rRay;

		if (ray < 0 || m == null) {
			return;
		}
		r = -1;
		l = 0;
		c = 0;// riciclo
		ray2 = ((ray << 1) + 1);
		height = (y < 0) ? ray2 + y : ray2;
		// null check
		while(++r < height) {
			if (m[c + r] == null) {
				l++;
			}
		}
		if (l == ray2) {
			/*
			 * tutte le righe che dovrebbero essere interessate dalla colorazione sono nulle
			 * .. error
			 */
			return;
		}
		height = m.length;

		if (height > 0 && ray > 0) {
			if (ray == 1) {
				if (y >= 0 && y < m.length) {
					if (allInside(m, x + 1, y, height)) {
						m[y][x + 1] = argb;
					}
				}
				if (inside(y + 1, height)) {
					rigaSup = m[y + 1];
					if (allInside(m, x, y, height)) {
						rigaSup[x] = argb;
					}
					if (allInside(m, x + 1, y, height)) {
						rigaSup[x + 1] = argb;
					}
					if (allInside(m, x + 2, y, height)) {
						rigaSup[x + 2] = argb;
					}
				}
				if (inside(y + 2, height)) {
					if (allInside(m, x + 1, y, height)) {
						m[y + 2][x + 1] = argb;
					}
				}
			} else {
				r = c = 0;
				ray2 = ray << 1;
				ray1 = ray + 1;
				rRay = ray + 0.5;// (r5 ? 0.25 : 0);
				// due dei punti cardinali
				if (inside(y, height)) {
					if (allInside(m, x + ray, y, height)) {
						m[y][ray + x] = argb;
					}
				}
				if (inside(y + ray2, height)) {
					if (allInside(m, x + ray, y + ray2, height)) {
						m[y + ray2][ray + x] = argb;
					}
				}
				// inizializzazione variabili
				horizontalSymmetricC = ray1;
				rInf = ray2;
				c = ray - 1;

				/*
				 * inizializzo le righe su cui le righe su cui devo lavorare
				 */
				rigaSup = inside(y, height) ? m[y] : null;
				rigaInf = inside(rInf + y, height) ? m[rInf + y] : null;

				while(r < ray) {

					/*
					 * ora sposto il puntatore "c" dal centro verso l'origine degli assi fino a
					 * quando non supero il cerchio ideale (in sostancia, sto ricalcando il bordo
					 * del disco), al che rientro e, successivamente questo ciclo, coloro la parte
					 * interna del cerchio .. uso ray1 per questioni di approssimazione grafica..
					 * dopodichè appunto traccio la congiungente tra la parte di bordo che ho
					 * appena tracciato
					 */
					// oldc = c--;
					// horizontalSymmetricOldC = altezza - c;

					while(0 <= c && (Math.hypot(ray - c, (ray - r)) < rRay)) {

						/*
						 * Sapendo che il cerchio l'ho diviso in otto spicchi, coloro prima la fetta che
						 * dall'(abituale) angolo 90 va fino al 135, poi tutti i simmetrici .. il primo
						 * è il più a sinistra di tutti, seguito da quello in range (90-45), a quello
						 * (270-225), poi (270-315)
						 */
						if (rigaSup != null) {
							if (inside(c + x, rigaSup.length)) {
								rigaSup[c + x] = argb;
							}
							if (inside(horizontalSymmetricC + x, rigaSup.length)) {
								rigaSup[horizontalSymmetricC + x] = argb;
							}
						}
						if (rigaInf != null) {
							if (inside(c + x, rigaInf.length)) {
								rigaInf[c + x] = argb;
							}
							if (inside(horizontalSymmetricC + x, rigaInf.length)) {
								rigaInf[horizontalSymmetricC + x] = argb;
							}
						}
						horizontalSymmetricC++;
						c--;
					}

					// ora disegno la riga sotto per riempire bene
					oldc = c;
					oldHorizC = horizontalSymmetricC;
					/*
					 * aggiorno le righe su cui devo lavorare NEL CICLO SUCCESSIVO
					 */
					if (++r < ray) {
						// e poi disegno li sotto la parte interna del cerchio
						if (inside(r + y, height)) {
							rigaSup = m[r + y];
							while(++oldc < oldHorizC) {
								if (allInside(m, oldc + x, r + y, height)) {
									rigaSup[oldc + x] = argb;
								}
							}
						}
						oldc = c;
						oldHorizC = horizontalSymmetricC;
						if (inside(--rInf + y, height)) {
							rigaInf = m[rInf + y];
							while(++oldc < oldHorizC) {
								if (allInside(m, oldc + x, rInf + y, height)) {
									rigaInf[oldc + x] = argb;
								}
							}
						}

					}

				} // fine ciclo r

				// disegno la linea centrale, non contemplata dal for
				// soprastante
				if (inside(ray + y, height)) {
					rigaSup = m[ray + y];
					while(ray2 >= 0) {
						if (allInside(m, ray2 + x, ray + y, height)) {
							rigaSup[ray2 + x] = argb;
						}
						ray2--;
					}
				}
			}
		}
	}

	// RECTANGLES
	/**
	 * Angle = 0.0°<br>
	 * Unsafe
	 */
	public static void fillRectange(int[][] m, int xx, int yy, int w, int h, int argb) {
		int r, c, row[], xtraslating;
		r = -1;
		while(++r < h) {
			row = m[yy++];
			c = -1;
			xtraslating = xx;
			while(++c < w) {
				row[xtraslating++] = argb;
			}
		}
	}

	/**
	 * Angle in degrees, counterclockwise.<br>
	 * Unsafe
	 */
	public static void fillRectange(int[][] m, int xx, int yy, int w, int h, int argb, double angDeg) {
		if (angDeg >= 360.0)
			angDeg %= 360.0;
		if (angDeg < 0.0)
			angDeg += 360.0;

		if (angDeg == 0.0) {
			fillRectange(m, xx, yy, w, h, argb);
		} else if (angDeg == 180.0) {
			fillRectange(m, xx - w, yy - h, w, h, argb);
		} else if (angDeg == 270.0) {
			fillRectange(m, xx, yy - w, h, w, argb);
		} else if (angDeg == 90.0) {
			fillRectange(m, xx - h, yy, h, w, argb);
		} else {
			// eh eh .. THE PROBLEM
			// fillRectange_RotatingPoints//
			fillRectange_RotatingSpan//
			(m, xx, yy, w, h, argb, angDeg);

		}
	}

	protected static void fillRectange_RotatingSpan(int[][] m, int xx, int yy, int w, int h, int argb, double angDeg) {
		int r, xfloor, yfloor;
		double rad, radTot, sin, cos, sinTot, cosTot;

		rad = Math.toRadians(angDeg);
		radTot = Math.toRadians(90 + angDeg);
		sin = Math.sin(rad);
		cos = Math.cos(rad);
		sinTot = Math.sin(radTot);
		cosTot = Math.cos(radTot);

		r = -1;
		while(++r < h) {

			xfloor = (xx + ((int) Math.round(r * cosTot)));
			yfloor = (yy + ((int) Math.round(r * sinTot)));

			drawRotatedSpan(m, xfloor, yfloor, w, argb, sin, cos);
			drawRotatedSpan(m, xfloor, yfloor + 1, w, argb, sin, cos);
			drawRotatedSpan(m, xfloor + 1, yfloor, w, argb, sin, cos);
			drawRotatedSpan(m, xfloor + 1, yfloor + 1, w, argb, sin, cos);
		}
	}

	/** Non so quanto sia corretto. */
	protected static void drawRotatedSpan(int[][] m, int xx, int yy, int w, int argb, double sin, double cos) {
		int c, row[], xfloor, yfloor;
		c = -1;
		while(++c < w) {
			yfloor = yy + (int) Math.round(c * sin);
			xfloor = xx + (int) Math.round(c * cos);

			row = m[yfloor];
			row[xfloor] = row[xfloor + 1] = argb;

			row = m[yfloor + 1];
			row[xfloor] = row[xfloor + 1] = argb;
		}
	}

	/**
	 * Draw a horizontal line, from the given coordinates, with length equals to "l"
	 * into the given matrix.<br>
	 * The line will be a sequence of "argb" into the y-row.<br>
	 * UNSAFE : checks missing about <br>
	 * - x, y, l : are them positive? l>0 ? x and y are inside the matrix?<br>
	 * - the matrix is not-null? all of his rows are not null? is the matrix a
	 * rectangle?
	 *
	 */

	protected static void drawHorizontalSpan(int x, int y, int l, int[][] m, int argb) {
		int i;
		int[] riga;

		riga = m[y];
		i = -1;
		while(++i < l) {
			riga[x + i] = argb;
		}
	}

}