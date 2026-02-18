package dataStructures.quadTree;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Matrix3DToImgAbove {

	public static double[] toPrimitive(Double[] arr) {
		int colNum = arr.length;
		double[] row = new double[colNum];
		for (int c = 0; c < colNum; c++) {
			row[c] = arr[c];
		}
		return row;
	}

	public static double[][] toPrimitive(Double[][] m) {
		final int len;
		double[] ret[];
		len = m.length;
		ret = new double[len][];
		for (int i = 0; i < len; i++) {
			ret[i] = toPrimitive(m[i]);
			m[i] = null;
		}
		return ret;
	}

	/** Returns a "horizontally aligned" xyz matrix (3 rows, N columns) */
	public static double[][] readPlyMatrixXYZ(String fileNameFull) {
		BufferedReader br;
		double[][] mat;
		mat = null;
		try {
			int linesOfHeader;
			double[] x, y, z;
			String line, headerEnd, tokens[];
			br = new BufferedReader(new FileReader(fileNameFull));
			br.mark(0);

			long linesCount = br.lines().count();
			if (linesCount > (Integer.MAX_VALUE)) {
				br.close();
				throw new RuntimeException("too many lines to process: " + linesCount);
			}
			br.close();

			br = new BufferedReader(new FileReader(fileNameFull));
			headerEnd = "end_header";
			linesOfHeader = 1;
			while ((line = br.readLine()) != null && (!headerEnd.equals(line))) {
				linesOfHeader++;
			}
			mat = new double[3][((int) linesCount) - linesOfHeader];
			x = mat[0];
			y = mat[1];
			z = mat[2];

			// recycle linesOfHeader as index
			linesOfHeader = 0;
			while ((line = br.readLine()) != null) {
				tokens = line.split(" ");
				x[linesOfHeader] = Double.parseDouble(tokens[0]);
				y[linesOfHeader] = Double.parseDouble(tokens[1]);
				z[linesOfHeader] = Double.parseDouble(tokens[2]);
				linesOfHeader++;
			}

//			end_header

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mat;
	}

	/**
	 * Returns a pair, constituted by a "vertical" xy zmatrix (N rows, 2 columns)
	 * and a z vector (a single vertical column with N elements)
	 */
	public static Entry<double[][], double[]> readPlyMatrixXYAndZ(String fileNameFull) {
		BufferedReader br;
		double[] xy[], z;
		Entry<double[][], double[]> e;
		e = null;
		try {
			int linesOfHeader;
			String line, headerEnd, tokens[];
			br = new BufferedReader(new FileReader(fileNameFull));
			br.mark(0);

			long linesCount = br.lines().count();
			if (linesCount > (Integer.MAX_VALUE)) {
				br.close();
				throw new RuntimeException("too many lines to process: " + linesCount);
			}
			br.close();

			br = new BufferedReader(new FileReader(fileNameFull));
			headerEnd = "end_header";
			linesOfHeader = 1;
			while ((line = br.readLine()) != null && (!headerEnd.equals(line))) {
				linesOfHeader++;
			}
			xy = new double[((int) linesCount) - linesOfHeader][];
			z = new double[xy.length];

			// recycle linesOfHeader as index
			linesOfHeader = 0;
			while ((line = br.readLine()) != null) {
				tokens = line.split(" ");
				xy[linesOfHeader] = new double[] { Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]) };
				z[linesOfHeader] = Double.parseDouble(tokens[2]);
				linesOfHeader++;
			}
			br.close();

			e = new AbstractMap.SimpleImmutableEntry<>(xy, z);

//			end_header

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return e;
	}

	public static double[] getMaxXY(double[][] xy) {
		double xmax, ymax, row[];

		xmax = (row = xy[0])[0];
		ymax = row[1];
		for (int i = xy.length - 1; i > 0; i--) {
			row = xy[i];
			if (xmax < row[0]) {
				xmax = row[0];
			}
			if (ymax < row[1]) {
				ymax = row[1];
			}
		}
		return new double[] { xmax, ymax };
	}

	public static double[] getMaxXY(Double[][] xy) {
		double xmax, ymax, t;
		Double row[];

		xmax = (row = xy[0])[0];
		ymax = row[1];
		for (int i = xy.length - 1; i > 0; i--) {
			row = xy[i];
			t = row[0];
			if (xmax < t) {
				xmax = t;
			}
			t = row[1];
			if (ymax < t) {
				ymax = t;
			}
		}
		return new double[] { xmax, ymax };
	}

	public static double[][] rasterizePointsTopView(double[][] xy, double[] z, int maxPointsPerSubsection,
			double stepSquare) {
		double[] maxXY = getMaxXY(xy);
		return rasterizePointsTopView(xy, z, maxPointsPerSubsection, stepSquare, maxXY[0], maxXY[1]);
	}

	public static double[][] rasterizePointsTopView(Double[][] xy, Double[] z, int maxPointsPerSubsection,
			double stepSquare) {
		double[] xy_primit[], z_primit, maxXY;
		maxXY = getMaxXY(xy);
		xy_primit = toPrimitive(xy);
		xy = null;
		z_primit = toPrimitive(z);
		z = null;
		return rasterizePointsTopView(xy_primit, z_primit, maxPointsPerSubsection, stepSquare, maxXY[0], maxXY[1]);
	}

	public static double[][] rasterizePointsTopView(double[][] xy, double[] z, int maxPointsPerSubsection,
			double stepSquare, double xmax, double ymax) {
		int rows, columns;
		double zmax, row[], basey, temp;
		double[][] mat;
		QuadTreeDouble qt;
		ArrayList<Entry<Point2D.Double, Double>> resultQuery;
		Iterator<Entry<Point2D.Double, Double>> iter;

		mat = null;

		System.out.println("creating quadtree for " + z.length + " points");
		qt = new QuadTreeDouble(xy, z, maxPointsPerSubsection, 0.0, 0.0, xmax, ymax);

		xmax = Math.max(xmax, qt.getBoundingBox().xBottomLeft + qt.getBoundingBox().width);
		ymax = Math.max(ymax, qt.getBoundingBox().yBottomLeft + qt.getBoundingBox().height);

		columns = (int) Math.ceil(xmax / stepSquare);
		rows = (int) Math.ceil(ymax / stepSquare);

		System.out.println("creating matrix image (rows: " + rows + ", cols: " + columns + ") to return");
		mat = new double[rows][columns];

		System.out.println("start scanning");

		for (int r = 0; r < rows; r++) {

//			if ((r & 1024) == 0) {
			if ((r % 1024) == 0) {
				System.out.println(r);
				System.out.println(r);
				if ((r % 4096) == 0) {
					System.gc();
				}
			}

			row = mat[r];
			basey = stepSquare * r;

			for (int c = 0; c < columns; c++) {
				zmax = 0;

				resultQuery = qt.query(stepSquare * c, basey, stepSquare, stepSquare);

				if (resultQuery != null && (!resultQuery.isEmpty())) {
//					zmax = resultQuery.stream().map(e -> e.getValue()).max(Double::compareTo).get();
					iter = resultQuery.iterator();
					// maximize
					zmax = iter.next().getValue();
					while (iter.hasNext()) {
						temp = iter.next().getValue();
						if (temp > zmax) {
							zmax = temp;
						}
					}
				}

				row[c] = zmax;
			}
		}

		return mat;
	}

	public static double[][] rasterizePointsTopView(Double[][] xy, Double[] z, int maxPointsPerSubsection,
			double stepSquare, double xmax, double ymax) {
		double[] xy_primit[], z_primit;
		xy_primit = toPrimitive(xy);
		xy = null;
		z_primit = toPrimitive(z);
		z = null;
		return rasterizePointsTopView(xy_primit, z_primit, maxPointsPerSubsection, stepSquare);
	}

	/**
	 * Calls
	 * {@link #rasterizeTopViewIteratorFrom(double[][], double[], int, double)}.
	 */
	public static Entry<Dimension, RasterizerIterator> rasterizeTopViewIteratorFrom(final String path,
			int maxPointsPerSubsection, double stepSquare, int maxQueuedRows) {
		Entry<double[][], double[]> e;
		double[] xy[], z;

		e = readPlyMatrixXYAndZ(path);
		xy = e.getKey();
		z = e.getValue();
		return rasterizeTopViewIteratorFrom(xy, z, maxPointsPerSubsection, stepSquare, maxQueuedRows);
	}

	public static Entry<Dimension, RasterizerIterator> rasterizeTopViewIteratorFrom(Double[][] xy, Double[] z,
			int maxPointsPerSubsection, double stepSquare, int maxQueuedRows) {
		double[] xy_primit[], z_primit;
		xy_primit = toPrimitive(xy);
		xy = null;
		z_primit = toPrimitive(z);
		z = null;
		return rasterizeTopViewIteratorFrom(xy_primit, z_primit, maxPointsPerSubsection, stepSquare, maxQueuedRows);
	}

	/**
	 * Returns a pair holding the amount of rows and an iterator that returns each
	 * rasterized row. THe iterator blocks if the row hasn't been already
	 * rasterized.
	 */
	public static Entry<Dimension, RasterizerIterator> rasterizeTopViewIteratorFrom(final double[][] xy, double[] z,
			int maxPointsPerSubsection, double stepSquare, int maxQueuedRows) {
		RasterizerIterator ri;

		ri = new RasterizerIterator(xy, z, maxPointsPerSubsection, stepSquare, maxQueuedRows);
		// inspired by rasterizePointsTopView
		return new AbstractMap.SimpleImmutableEntry<>(new Dimension(ri.columns, ri.rows), ri);
	}

	public static double[][] rasterizePointsTopViewFromFile(final String path, int maxPointsPerSubsection,
			double stepSquare) {
		Entry<double[][], double[]> e;

		e = readPlyMatrixXYAndZ(path);

		return rasterizePointsTopView(//
				e.getKey(), // xy
				e.getValue(), // z
				maxPointsPerSubsection, stepSquare);
	}

	// ,

	//

	public static void writePly(String pathFileName, double[][] m) {
		BufferedWriter bw;
		/**
		 * 
		 * ply format ascii 1.0 element vertex 1528861 property double x property double
		 * y property double z end_header 18.360818945586310 10.504707280858383
		 * 0.140700000000038
		 */
		try {
			int cf, cols;
			final int intervalFlush = 1000;
			double[] row;
			bw = new BufferedWriter(new FileWriter(pathFileName));

			bw.write(
					"ply\nformat ascii 1.0\nelement vertex 1528861\nproperty double x\nproperty double y\nproperty double z\nend_header\n");
			bw.flush();
			cols = m[0].length;
			cf = intervalFlush;
			for (int r = 0, rr = m.length; r < rr; r++) {
				row = m[r];
				bw.write(Double.toString(row[0]));
				for (int c = 1; c < cols; c++) {
					bw.write(" ");
					bw.write(Double.toString(row[c]));
					if (cf-- > 0) {
						cf = intervalFlush;
						bw.flush();
					}
				}
				bw.write("\n");
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//

	//

	public static void testXYZ(String path) {
		double[][] m;
		m = readPlyMatrixXYZ(path);

		System.out.println("got matrix with " + m[0].length + " elements");
		System.out.println("first line: " + m[0][0] + " - " + m[0][1] + " - " + m[0][2]);
		// expected 18.360818945586310 10.504707280858383 0.140700000000038
	}

	public static void testXY_Z(String path) {
		Entry<double[][], double[]> e;
		double[] xy[], z;
		e = readPlyMatrixXYAndZ(path);
		xy = e.getKey();
		z = e.getValue();
		System.out.println("got matrix with " + z.length + " elements");
		System.out.println("first line: " + xy[0][0] + " - " + xy[0][1] + " - " + z[0]);
		// expected 18.360818945586310 10.504707280858383 0.140700000000038
	}

	public static void main_1(String[] args) {
		String path;

		System.out.println("start");
		path = "/home/marcoottina/Desktop/phd/papers_miei/agraria_1/dataset/PointCloud_v2__offset_filter.ply";
//		testXYZ(path);
		testXY_Z(path);
	}

	public static void main_2(String[] args) {
		double max, temp;
		int n = 50;
		long t;
		ArrayList<Entry<Object, Double>> a;

		System.out.println("start");
		a = new ArrayList<>(n);
		for (int i = n; i > 0; i--) {
			a.add(new AbstractMap.SimpleImmutableEntry<>(null, Double.valueOf(i)));
		}

		System.out.println("\niterator");
		t = System.currentTimeMillis();
		for (int rep = 10; rep > 0; rep--) {
//			iter = a.iterator();
//			max = iter.next().getValue();
//			while (iter.hasNext()) {
			max = Double.MIN_VALUE;
			for (Entry<Object, Double> e : a) {
//				temp = iter.next().getValue();
				temp = e.getValue();
				if (temp > max) {
					max = temp;
				}
			}
		}
		t = System.currentTimeMillis() - t;
		System.out.println("took: " + t);

		System.out.println("\nstream");
		t = System.currentTimeMillis();
		for (int rep = 10; rep > 0; rep--) {
			max = a.stream().map(e -> e.getValue()).max(Double::compareTo).get();
		}
		t = System.currentTimeMillis() - t;
		System.out.println("took: " + t);
	}

	public static void main_3(String[] args) {
		double stepSquare;
		double[][] rasterized;
		final String path;
		String filename;

		path = (args != null && args.length > 0 && args[0] != null && args[0].length() > 0) ? args[0]
				: "/home/marcoottina/Desktop/phd/papers_miei/agraria_1/dataset/";
		if (args != null && args.length > 1 && args[1] != null && args[1].length() > 0) {
			filename = args[1];
		} else {
			filename = "PointCloud_v2__offset_filter.ply";
		}

		if (args != null && args.length > 2 && args[2] != null && args[2].length() > 0) {
			stepSquare = Double.parseDouble(args[2]);
		} else {
			stepSquare = 0.125 / 16.0; // 0.005
		}
		System.out.println("start reading and rasterizing with stepSquare: " + stepSquare);
		rasterized = rasterizePointsTopViewFromFile(path + filename, 8, stepSquare);
		System.out.println("raster size: rows: " + rasterized.length + ", cols: " + rasterized[0].length);

		filename = String.format("%s%s_%f.ply", path,
				(args != null && args.length > 3 && args[3] != null && args[3].length() > 0) ? args[3] : "img_matrice",
				stepSquare);
		System.out.println("writing file: " + filename);
		writePly(filename, rasterized);
		System.out.println("written");
	}

	public static void main_4(String[] args) {
		int maxLines, len;
		double stepSquare;
		final String path;
		String filename;
		Entry<Dimension, RasterizerIterator> e;
		RasterizerIterator iter;
		Dimension d;

		path = (args != null && args.length > 0 && args[0] != null && args[0].length() > 0) ? args[0]
				: "/home/marcoottina/Desktop/phd/papers_miei/agraria_1/dataset/";
		if (args != null && args.length > 1 && args[1] != null && args[1].length() > 0) {
			filename = args[1];
		} else {
			filename = "_offset_filter.ply";
		}

		stepSquare = 1.0 / 256.0;

		System.out.println("creating the iterator");
		e = rasterizeTopViewIteratorFrom(path + filename, 8, stepSquare,
				Math.max(2, 128 - (int) (Math.ceil(1 / stepSquare))));

		d = e.getKey();
		System.out.println(d);

		maxLines = 10;

		System.out.println("start rasterizing");

		iter = e.getValue();
		boolean has_next;
		has_next = true;

		len = Math.min(((maxLines < 0) ? Integer.MAX_VALUE : maxLines), d.height);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (int r = 0; has_next && r < len; r++) {

			has_next = iter.hasNext();
			System.out.print("main -- " + r);
			System.out.println(", has next? " + has_next);
			if (has_next) {
				double[] row = iter.next();
				System.out.println("main -- frist 5 elements: " + row[0] + ", " + row[1] + ", " + row[2] + ", " + row[3]
						+ ", " + row[4]);
			}
		}

		System.out.println("shutdown");
		iter.shutdown();
		System.out.println("rows remaining: " + iter.rasterizedRows.size());
		System.out.println("\n\nEND");
	}

	public static void main(String[] args) {
		main_4(args);
	}

	//

//	static class EnvironmentRaster {
//		int maxPointsPerSubsection;
//		double stepSquare, xmax, ymax;
//		public EnvironmentRaster(int maxPointsPerSubsection, double stepSquare, double xmax, double ymax) {
//			super();
//			this.maxPointsPerSubsection = maxPointsPerSubsection;
//			this.stepSquare = stepSquare;
//			this.xmax = xmax;
//			this.ymax = ymax;
//		}
//		
//	}

	public static class RasterizerIterator implements Iterator<double[]>, Runnable {
		protected boolean canRun, abortedInternally;
		protected int currentRowIndex;
		protected final int columns, rows, maxPointsPerSubsection, maxQueuedRows;
		protected final double stepSquare;
		protected double xmax, ymax;
		protected final double[] xy[], z;
		protected final QuadTreeDouble qt;
		protected final LinkedList<double[]> rasterizedRows;
		protected ExecutorService threadRowRastering;

		public RasterizerIterator(double[][] xy, double[] z, int maxPointsPerSubsection, double stepSquare,
				int maxQueuedRows) {
			super();
			this.z = z;
			this.xy = xy;
			this.maxPointsPerSubsection = maxPointsPerSubsection;
			this.maxQueuedRows = maxQueuedRows;
			this.stepSquare = stepSquare;

//			System.out.println("creating quadtree for " + z.length + " points");
			this.qt = new QuadTreeDouble(xy, z, maxPointsPerSubsection);

			this.xmax = Math.max(this.xmax, this.qt.getBoundingBox().xBottomLeft + this.qt.getBoundingBox().width);
			this.ymax = Math.max(this.ymax, this.qt.getBoundingBox().yBottomLeft + this.qt.getBoundingBox().height);

			this.columns = (int) Math.ceil(this.xmax / stepSquare);
			this.rows = (int) Math.ceil(this.ymax / stepSquare);

			this.canRun = true;
			this.abortedInternally = false;
			this.currentRowIndex = 0;
			this.rasterizedRows = new LinkedList<>();
			this.threadRowRastering = Executors.newSingleThreadExecutor();
			this.threadRowRastering.submit(this);
		}

		public int getRowsAmount() {
			return this.rows;
		}

		public int getColumnsAmount() {
			return this.columns;
		}

		/**
		 * Stop the asynchronous creation of new rows (the ones returned by
		 * {@link #next()}).
		 */
		public void shutdown() {
			this.shutdown(true);
		}

		protected void shutdown(boolean isExternal) {
			this.abortedInternally = !isExternal;
			this.canRun = false;
			if (this.threadRowRastering == null) {
				return;
			}
			this.threadRowRastering.shutdown();
			this.threadRowRastering = null;
		}

		@Override
		public boolean hasNext() {
			boolean hn;
			synchronized (this.rasterizedRows) {
				hn = this.currentRowIndex < this.rows || this.rasterizedRows.size() > 0;
				if (!hn) {
					this.shutdown(false);
				}
			}
			return hn;
		}

		@Override
		public double[] next() {
			double[] row;
			row = null;
			synchronized (this.rasterizedRows) {
				while (this.hasNext() && row == null) {
					if (this.rasterizedRows.isEmpty()) {
						try {
							this.rasterizedRows.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
							row = null;
						}
					}
					if (this.hasNext()) {
						row = this.rasterizedRows.removeLast();

						synchronized (this.threadRowRastering) {
							if (this.rasterizedRows.isEmpty()
									|| (this.maxQueuedRows > 0 && this.maxQueuedRows > this.rasterizedRows.size())) {
								this.threadRowRastering.notifyAll();
							}
						}
					} else {
						LinkedList<double[]> rr = this.rasterizedRows;
						this.shutdown(false);
						rr.notifyAll();
					}
				}
			}
			return row;
		}

		@Override
		public void run() {
			int r, len;
			double basey, zmax, temp;
			double[] row;
			ArrayList<Entry<Point2D.Double, Double>> resultQuery;
			Iterator<Entry<Point2D.Double, Double>> iter;

			r = 0;
			len = this.rows;
			do {
				try {
					while (this.canRun && r < len) {

						// do not fill completely the memory
						synchronized (this.threadRowRastering) {
							if (this.maxQueuedRows > 0 && this.maxQueuedRows <= this.rasterizedRows.size()) {
								this.threadRowRastering.wait();
							}
						} // end of synchronized(threadRowRastering)

						row = new double[this.columns];
						basey = stepSquare * r;

						for (int c = 0; this.canRun && c < columns; c++) {
							zmax = 0;
							resultQuery = qt.query(stepSquare * c, basey, stepSquare, stepSquare);

							if (resultQuery != null && (!resultQuery.isEmpty())) {
								iter = resultQuery.iterator();
								// maximize
								zmax = iter.next().getValue();
								while (iter.hasNext()) {
									temp = iter.next().getValue();
									if (temp > zmax) {
										zmax = temp;
									}
								}
							}
							row[c] = zmax;
						}
						synchronized (this.rasterizedRows) {
							System.out.println("___ run adding");
							if (this.canRun) {
								this.rasterizedRows.add(row);
								this.currentRowIndex++;
							}
							this.rasterizedRows.notifyAll();
						}
						r++;
					} // end inner while
				} catch (InterruptedException e) {
					e.printStackTrace();
					r--; // retry
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				System.out.println("___ run's try done");
			} while (this.canRun && r < len);
		}

	}
}