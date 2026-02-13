package tests;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import aaut.nn.impl.OutputToBoolean;
import aaut.nn.old.PolygonAttentionNN;
import aaut.tools.impl.MatrixInput2D;
import dataStructures.MapTreeAVL;
import geometry.pointTools.PolygonUtilities;
import tools.Comparators;
import tools.GraphicTools;
import tools.gui.swing.JListModelTreeMap;

public class PolygonDrawer {
	static final int WIDTH_MAP = 20, HEIGHT_MAP = 20, PIXEL_SIZE = 20;

	PDModel model;
	PDView view;

	PolygonDrawer() {
		model = new PDModel(WIDTH_MAP, HEIGHT_MAP, Color.BLUE);
		view = new PDView(model);
	}

	void show() {
		view.show();
	}

	//

	// TODO CLASS

	protected static interface PolyFilteredNotifier {
		void notifyMe(boolean[] res);
	}

	protected class PDView implements PolyFilteredNotifier {
		boolean yetShown;
		int widthList = 200;
		BufferedImage bi, biOracle;
		Color colFilter;
		JList<PointWithID> listPoint;
		LCR_Point cellRenderPoint;
		PDModel pdm;
		JFrame fin;
		JPanel jpCenterPoly, jpBuildPoly, jpImageFiltered, jpOracle, jbButtons;
		JButton testFilter, jbRebuildPolygon, jbClearPolygon;
		Border borderCellRenderFocused = BorderFactory.createLineBorder(Color.PINK, 1),
				borderCellRenderSelected = BorderFactory.createLineBorder(Color.CYAN, 1);
		Polygon polyOriginal, polygonScaled;
		boolean[] res;

		PDView(PDModel pdm) {
			this.pdm = pdm;
			pdm.setPFN(this);
			listPoint = new JList<>(pdm.jlmtm);
			cellRenderPoint = new LCR_Point();
			listPoint.setCellRenderer(cellRenderPoint);
			yetShown = false;
			colFilter = Color.BLUE;
			bi = biOracle = null;

			//
			jpCenterPoly = new JPanel();
			jbButtons = new JPanel();
			jpCenterPoly = new JPanel();
		}

		void show() {
			Container finPan;
			Border border;
			GridBagConstraints c;
			if (yetShown)
				return;
			c = new GridBagConstraints();
			fin = new JFrame("DrawPolygon");
			fin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			fin.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent evt) {
					resizeEveryone();
				}
			});
			finPan = fin.getContentPane();
			finPan.setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = c.gridwidth = 4;
			c.weighty = c.gridheight = 4;
			border = BorderFactory.createLineBorder(Color.BLUE, 2);
			jpCenterPoly.setBorder(border);
			finPan.add(jpCenterPoly, c);
			c.gridx = 4;
			c.weightx = c.gridwidth = 2;
			finPan.add(listPoint, c);

			c.gridx = 0;
			c.gridy = 4;
			c.weightx = c.gridwidth = 6;
			c.weighty = c.gridheight = 1;
			finPan.add(jbButtons, c);
			jbClearPolygon = new JButton("CLEAR poly");
			jbRebuildPolygon = new JButton("Build poly");
			testFilter = new JButton("Do Test") {
				private static final long serialVersionUID = 8453240L;

				@Override
				public boolean isEnabled() {
					return pdm.polyInput != null;
				}
			};
			jbButtons.add(jbClearPolygon);
			jbButtons.add(jbRebuildPolygon);
			jbButtons.add(testFilter);
			jbClearPolygon.addActionListener(e -> {
				pdm.clearPoly();
				fin.repaint();
			});
			jbRebuildPolygon.addActionListener(e -> {
				pdm.buildPoly();
				fin.repaint();
			});
			testFilter.addActionListener(e -> {
				pdm.doTest();
				fin.repaint();
			});

			jpCenterPoly.setLayout(new GridBagLayout());
			// TODO impostare un layout DECENTE
			jpBuildPoly = new JPanel() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {
					paintPolyBuilderPanel(g);
				}
			};
			jpImageFiltered = new JPanel() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {
					if (bi != null)
						g.drawImage(bi, 0, 0, null);
					g.setColor(Color.GREEN);
					GraphicTools.paintGrid(g, pdm.widthArea * PIXEL_SIZE, pdm.heightArea * PIXEL_SIZE, PIXEL_SIZE);
				}
			};
			jpOracle = new JPanel() {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {
					if (biOracle != null)
						g.drawImage(biOracle, 0, 0, null);
					g.setColor(Color.GREEN);
					GraphicTools.paintGrid(g, pdm.widthArea * PIXEL_SIZE, pdm.heightArea * PIXEL_SIZE, PIXEL_SIZE);
				}
			};

			border = BorderFactory.createLineBorder(Color.ORANGE, 2);
			jpBuildPoly.setBorder(border);
			jpImageFiltered.setBorder(border);
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.gridwidth = 1;
			c.gridheight = 2;
			jpCenterPoly.add(jpBuildPoly, c);
			jpBuildPoly.setSize(100, 100);
			c.gridx = 1;
			jpCenterPoly.add(jpImageFiltered, c);

			jpBuildPoly.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Point p;
					p = new Point(e.getX() / PIXEL_SIZE, e.getY() / PIXEL_SIZE);
					System.out.println(e.getLocationOnScreen() + ", (" + e.getX() + "; " + e.getY() + ") --> " + p);
					pdm.addPoint(p);
					jpBuildPoly.repaint();
				}
			});

			//

			fin.pack();
			fin.setSize(1200, 800);
			fin.setVisible(true);

			//
			System.out.println("jpCenterPoly size " + jpCenterPoly.getSize());
			System.out.println("jpBuildPoly size " + jpBuildPoly.getSize());
		}

		void resizeEveryone() {
//			int fw, fh;
			Dimension d;
			d = (Dimension) fin.getSize().clone();
//			fw = d.width;
//			fh = d.height;
			d.width -= widthList;
			d.height -= 50;
			jpCenterPoly.setSize(d);
			jpCenterPoly.setPreferredSize(jpCenterPoly.getSize());
			d.width >>= 1;
			jpBuildPoly.setSize(d);
			jpBuildPoly.setMinimumSize(d);
			jpBuildPoly.setPreferredSize(d);
			jpImageFiltered.setSize(d);
			jpImageFiltered.setMinimumSize(d);
			jpImageFiltered.setPreferredSize(d);
		}

		void paintPolyBuilderPanel(Graphics g) {
			g.setColor(Color.GREEN);
			GraphicTools.paintGrid(g, pdm.widthArea * PIXEL_SIZE, pdm.heightArea * PIXEL_SIZE, PIXEL_SIZE);

			g.setColor(Color.BLUE);
			if (pdm.polygon != null && polyOriginal != pdm.polygon) {
				int n, i;
				int[] xx, yy;
				Polygon polyOriginal, polygonScaled;
				polyOriginal = pdm.polygon;
				i = n = polyOriginal.npoints;
				xx = new int[n];
				yy = new int[n];
				while(--i >= 0) {
					xx[i] = polyOriginal.xpoints[i] * PIXEL_SIZE;
					yy[i] = polyOriginal.ypoints[i] * PIXEL_SIZE;
				}
				polygonScaled = new Polygon(xx, yy, n);
				g.drawPolygon(polygonScaled);
			}
		}

		void colorPixelSquare(BufferedImage b, int r, int c, int argb) {
			for (int rr = 0; rr < PIXEL_SIZE; rr++)
				for (int cc = 0; cc < PIXEL_SIZE; cc++)
					b.setRGB(c * PIXEL_SIZE + cc, r * PIXEL_SIZE + rr, argb);
		}

		@Override
		public void notifyMe(boolean[] res) {
			boolean b;
			int i, r, c, w, h, argb, rr, cc;
			w = pdm.widthArea;
			h = pdm.heightArea;
			argb = pdm.c.getRGB();
			// TODO color the Bufffered image
			bi = new BufferedImage(w * PIXEL_SIZE, h * PIXEL_SIZE, BufferedImage.TYPE_INT_ARGB);
			biOracle = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
			i = r = -1;
			rr = cc = c = 0;
//			try {
			while(++r < h) {
				c = -1;
				System.out.print("- " + r + ":\t");
				while(++c < w) {
//						if (res[++i]) {
					b = res[++i];
					if (b) {
						System.out.print("1, ");
						colorPixelSquare(bi, r, c, argb);
						for (rr = 0; rr < PIXEL_SIZE; rr++)
							for (cc = 0; cc < PIXEL_SIZE; cc++)
								bi.setRGB(c * PIXEL_SIZE + cc, r * PIXEL_SIZE + rr, argb);
					} else {
						System.out.print("0, ");
					}
				}
				System.out.println();
			}
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("OUT OF BOUND :\n- bi size: {w: " + bi.getWidth() + ", h: " + bi.getHeight()
//						+ "}\n- location causing: (x: "//
//						+ (c * PIXEL_SIZE + cc) + ", y: " + (r * PIXEL_SIZE + rr)//
//						+ ")\n- r: " + r + ", c: " + c);
//			}
			jpImageFiltered.repaint();
			fin.repaint();
		}

		class LCR_Point implements ListCellRenderer<PointWithID> {
			JPanel jpPoint;
			JLabel jlPointInfo;
			JBDeleting jbDeleteMe;
			MapTreeAVL<Integer, JBDeleting> buttonsCache; // cache all of the buttonsCache
			Dimension d;

			LCR_Point() {
				buttonsCache = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
				jpPoint = new JPanel();
				d = new Dimension(widthList, 50);
				jpPoint.setLayout(null);
				jpPoint.setSize(d);
				jpPoint.setPreferredSize(d);
				d.width -= d.height;
				jlPointInfo = new JLabel();
				jlPointInfo.setSize(d);
				jlPointInfo.setPreferredSize(d);
				d.width = d.height;
			}

			@Override
			public Component getListCellRendererComponent(JList<? extends PointWithID> list, PointWithID value,
					int index, boolean isSelected, boolean cellHasFocus) {
				Integer id;
				jpPoint.removeAll();
				// check if the polygon has been cleared
				if (pdm.mapBack.isEmpty()) {
					buttonsCache.clear();
					return null;
				}

				jpPoint.add(jlPointInfo);
				id = value.id;
				if (isSelected)
					jlPointInfo.setBorder(borderCellRenderSelected);
				else if (cellHasFocus)
					jlPointInfo.setBorder(borderCellRenderFocused);
				else
					jlPointInfo.setBorder(null);

				if (buttonsCache.containsKey(id)) {
					jbDeleteMe = buttonsCache.get(id);
				} else {
					jbDeleteMe = new JBDeleting(value);
					buttonsCache.put(id, jbDeleteMe);
				}
				jpPoint.add(jbDeleteMe);
				jbDeleteMe.setLocation(widthList - d.width, 0);
				jlPointInfo.setText(id + " - (" + value.p.getX() + "; " + value.p.getY() + ")");
				return jpPoint;
			}

			class JBDeleting extends JButton {
				/**
				 *
				 */
				private static final long serialVersionUID = 1L;
				PointWithID pwiReferring;

				public JBDeleting(PointWithID value) {
					super("X");
					this.pwiReferring = value;
					this.setSize(d);
					this.setPreferredSize(d);
					this.addActionListener(l -> {
						if (pdm.removePoint(pwiReferring)) {
							buttonsCache.remove(pwiReferring.id);
						}
					});
				}
			}
		}
	}

	//

	// TODO MODEL

	protected class PDModel {
		PDModel(int widthArea, int heightArea, Color c) {
			keyExtractor = p -> p.id; // identity
			this.widthArea = widthArea;
			this.heightArea = heightArea;
			this.c = c;
			mapBack = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
			jlmtm = JListModelTreeMap.newInstance(mapBack, keyExtractor);
//			points = mapBack.toListValue(keyExtractor);
			polyInput = null;
			otb = new OutputToBoolean();
			matrixInput = null;
			createInput();
		}

		int widthArea, heightArea;
		boolean[] expected;
		MatrixInput2D matrixInput;
		Color c;
		PolygonAttentionNN<MatrixInput2D, boolean[]> polyInput;
		Polygon polygon;
		OutputToBoolean otb;
//		List<PointWithID> points;
		private MapTreeAVL<Integer, PointWithID> mapBack;
		Function<PointWithID, Integer> keyExtractor;
		JListModelTreeMap<Integer, PointWithID> jlmtm;
		PolyFilteredNotifier pfn;

		void setPFN(PolyFilteredNotifier pfn) {
			this.pfn = pfn;
		}

		boolean removePoint(PointWithID p) {
			boolean b;
			b = jlmtm.remove(p);
			polyInput = null;
			polygon = null;
			return b;
		}

		void addPoint(Point2D p) {
			jlmtm.add(new PointWithID(p));
			polyInput = null;
			polygon = null;
		}

		void clearPoly() {
			mapBack.clear();
			polyInput = null;
			polygon = null;
		}

		void createInput() {
			int s, r, c;
			double[][] m;
			double[] row;
			m = new double[s = heightArea * widthArea][2];
			this.matrixInput = new MatrixInput2D(m);
			r = c = 0;
			expected = new boolean[s];
			for (int i = 0; i < s; i++) {
				row = m[i];
//				for (int c = 0; c < widthArea; c++) {
				row[0] = c;
				row[1] = r;
				if (++c >= widthArea) {
					c = 0;
					r++;
				}
//				}
			}
		}

		void buildPoly() {
			int size, i;
			int[] xx, yy, at;
			Point p;
			p = new Point();
			at = new int[1];
			at[0] = 0;
			size = mapBack.size();
			xx = new int[size];
			yy = new int[size];
			mapBack.forEach((id, pwi) -> {
				int index;
				index = at[0]++;
				xx[index] = (int) pwi.p.getX();
				yy[index] = (int) pwi.p.getY();
			});
			polygon = new Polygon(xx, yy, size);

			size = heightArea * widthArea;
			i = 0;
			for (int r = 0; r < heightArea; r++) {
				p.y = r;
				for (int c = 0; c < widthArea; c++) {
					p.x = c;
					expected[i++] = PolygonUtilities.isInside(p, polygon);
				}
			}

			System.out.println(PolygonUtilities.polygonToString(polygon));
			polyInput = new PolygonAttentionNN<>(polygon);
			polyInput.setLayerOutputCaster(otb);
		}

		void doTest() {
			boolean b;
			boolean[] r;
			r = null;
			System.out.println("DO TEST");
			r = polyInput.apply(matrixInput);
			// polyInput
			pfn.notifyMe(r);
			b = Arrays.equals(r, expected);
			System.out.println("\n\n is result equal to expected? " + b);
			if (!b) {
				System.out.println("got:");
				System.out.println(Arrays.toString(r));
				System.out.println("\n\nexpected:");
				System.out.println(Arrays.toString(expected));
			}
		}
	}

	//

	// TODO lel

	static class PointWithID {
		static int progID = 0;
		Integer id;
		Point2D p;

		PointWithID(Point2D p) {
			this.p = p;
			this.id = progID++;
		}
	}

	//

	// TODO MAIN

	public static void main(String[] args) {
		PolygonDrawer pd;
		pd = new PolygonDrawer();
		pd.show();
	}
}