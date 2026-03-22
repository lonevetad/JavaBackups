package tests.tDataStruct;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import dataStructures.quadTree.Matrix3DToImgAbove;
import dataStructures.quadTree.Matrix3DToImgAbove.RasterizerIterator;

public class T_Raster_01_mini {

	protected static Point2D[] centers = null;
	static {
		centers = new Point2D[] { new Point2D.Double(4, 5), //
				new Point2D.Double(23, 18), //
				new Point2D.Double(12, 8) //
		};
	}

	public static Map.Entry<Double, double[][]> newMap_1(final int side, final double maxHeight) {
		final int centh = centers.length;
		int c, ip;
		final double reduction, hmh, variance, coeff;
		double ttot, x, y, t1, t2, max, t;
		double[] m[], row;
		Point2D p;
		Map.Entry<Double, double[][]> e;

		hmh = maxHeight / 4.0;
		variance = 0.25;
		reduction = 1.0 / (Math.sqrt(Math.PI * 2.0) * variance);
		coeff = -0.5 * variance * variance;

		m = new double[side][side];
		max = 0;

		for (int r = 0; r < side; r++) {
			row = m[r];
			for (c = 0; c < side; c++) {
				ttot = 0.0;
				for (ip = 0; ip < centh; ip++) {
					p = centers[ip];
					if (r <= (y = p.getY()) && c <= (x = p.getX())) {
						t1 = c - x;
						t2 = r - y;
						t = Math.exp(coeff * ((t1 * t1 + t2 * t2) - hmh)) * maxHeight;
						ttot += t;
						if (t > max) {
							max = t;
						}
					}
				}
				row[c] = ttot * reduction;
			}
		}
		e = new E<>(Double.valueOf(max), m);
		return e;
	}

	public static void pr(double[][] m) {
		final int h, w;
		int r, c, len;
		double[] row;
		String s;
		h = m.length;
		w = (h <= 0) ? 0 : m[0].length;
		System.out.println("matrice r:" + h + " x c:" + w);
		for (r = 0; r < h; r++) {
			row = m[r];
			for (c = 0; c < w; c++) {
				s = String.format("%.3f", row[c]);

				System.out.print(s);
				len = 8 - s.length();
//				while (len >= 4) {
//					System.out.print('\t');
//					len -= 4;
//				}
				while (len-- > 0) {
					System.out.print(' ');
				}
			}
			System.out.println();
			System.out.println();
			System.out.println();
		}
	}

	public static void wr(String suffixName, double[][] m, final double maxVal) {
		int r, c, h, w, r_c, g_c, b_c, alpha, col;
		double fraction, reciprocal;
		double[] row;
		Color c1, c2;

		c1 = Color.YELLOW;
		c2 = Color.MAGENTA;
		h = m.length;
		w = m[0].length;
		alpha = 0xFF << 24;

//		return castMatrixBufferedImage(matrix, BufferedImage.TYPE_INT_ARGB);

//	public static BufferedImage castMatrixBufferedImage(int[][] matrix, int type) {
		BufferedImage bufferedImage = null;
		bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (r = 0; r < h; r++) {
			row = m[r];
			for (c = 0; c < w; c++) {
				fraction = row[c];
				if (fraction > maxVal) {
					fraction = maxVal;
				}
				fraction /= maxVal;
				reciprocal = 1 - fraction;
				r_c = (int) (c1.getRed() * fraction + reciprocal * c2.getRed());
				g_c = (int) (c1.getGreen() * fraction + reciprocal * c2.getGreen());
				b_c = (int) (c1.getBlue() * fraction + reciprocal * c2.getBlue());

				col = alpha | ((r_c & 0xFF) << 16) | ((g_c & 0xFF) << 8) | (b_c & 0xFF);
//				if (col != 0 && col != alpha) {
//					System.out.printf("r:%d - c:%d -> %d\n", r, c, col);
//				}
				bufferedImage.setRGB(c, r, col);
			}
		}

		try {
			boolean successInWriting = ImageIO.write(bufferedImage, "png",
					new File("./mini_image_raster_" + suffixName + ".png"));
			System.out.println("written? " + successInWriting);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Map.Entry<double[][], double[]> extractXYMFromMap(double[][] m) {
		int r, c, w, h, i;
		double[] xy[], z, row;
		h = m.length;
		w = m[0].length;
		z = new double[h * w];
		xy = new double[z.length][];
		i = 0;
		for (r = 0; r < h; r++) {
			row = m[r];
			for (c = 0; c < w; c++) {
				xy[i] = new double[] { c, r }; // <x,y>
				z[i++] = row[c];
			}
		}

		return new E<>(xy, z);
	}

	public static void main(String[] args) {
		final int side, maxPointsSubsection, maxEnqueuedRows;
		double maxHeight, stepSide;
		double[] m[], xy[], z;
		Map.Entry<Double, double[][]> eMap;
		Entry<Dimension, RasterizerIterator> eIter;
		Entry<double[][], double[]> eXYZ;
		Iterator<double[]> iter;

		side = 32;
		maxHeight = 8;

		eMap = newMap_1(side, maxHeight);
		m = eMap.getValue();
		maxHeight = eMap.getKey();

		pr(m);
		wr("_01", m, maxHeight);

		eXYZ = extractXYMFromMap(m);
		xy = eXYZ.getKey();
		z = eXYZ.getValue();
		m = null;
		System.gc();

		maxPointsSubsection = 8;
		stepSide = 0.25;
		maxEnqueuedRows = 3;
		System.out.printf("rasterising with stepside %f\n", stepSide);
		eIter = Matrix3DToImgAbove.rasterizeTopViewIteratorFrom(xy, z, maxPointsSubsection, stepSide, maxEnqueuedRows);
		System.out.println("building a matrix with this dimensions: " + eIter.getKey().toString());

		m = new double[(int) eIter.getKey().getHeight()][];
		iter = eIter.getValue();
		// build the
		for (int r = 0, len = m.length; r < len && iter.hasNext(); r++) {
			System.out.print("r " + r + " ..");
			m[r] = iter.next();
			System.out.println(" .. row " + r + " done");
		}

		System.out.println("write on ply file");
		Matrix3DToImgAbove.writePly("./img_3bubbles.ply", m);

		System.out.println("finished");
	}

	//

	public static class E<K, V> implements Map.Entry<K, V> {
		public K key;
		V value;

		public E(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return this.key;
		}

		@Override
		public V getValue() {
			return this.value;
		}

		@Override
		public V setValue(V value) {
			V old = this.value;
			this.value = value;
			return old;
		}

	}
}
