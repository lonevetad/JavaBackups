package tests.tDataStruct;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import dataStructures.MapTreeAVL;
import stuffs.logic.AtomLogicProposition;
import tools.Comparators;
import tools.MathUtilities;
import tools.Stringable;

/**
 * Holder for sub-maps taking inspiration from quad-trees. Retrieve a sub-map
 * holding a specific point with {@link #getMapContaining(Point)} (beware of
 * <code>null</code>).
 * <p>
 * I hope that no map overlaps, because no check is performed.
 */
public class Test_MultiISOMRetangularMap_V1 {
	public static final int MAXIMUM_SUBMAPS_EACH_SECTION = 4, MINIMUM_DIMENSION_MAP = 4;

	public Test_MultiISOMRetangularMap_V1(int maximumSubmapsEachSection) {
		if (maximumSubmapsEachSection < 1)
			throw new IllegalArgumentException(
					"Incorrect number of maximum submaps on each section: " + maximumSubmapsEachSection);
		this.maximumSubmapsEachSection = maximumSubmapsEachSection;
		maps = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		mapsAsList = maps.toListValue(r -> r.ID);
		clear();
	}

	protected boolean neverBuilt;
	protected int maxDepth, xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height;
	protected final int maximumSubmapsEachSection;
	protected final MapTreeAVL<Integer, MyRectangle> maps;
	protected final List<MyRectangle> mapsAsList;
	protected NodeMultiISOMRectangular root;
	// of bounding box

//

//

	public void removeAllMaps() {
		maps.clear();
		root = null;
		clearDimensionCache();
	}

	protected void clearDimensionCache() {
		neverBuilt = true;
		maxDepth = 0;
		xLeftTop = yLeftTop = xRightBottom = yRightBottom = width = height = 0;
	}

	public void clear() { removeAllMaps(); }

	public MyRectangle getMapContaining(Point p) {
		NodeMultiISOMRectangular n, prev;
		List<MyRectangle> submaps;
		if (root == null)
			return null;
		n = prev = root;
		// traverse the tree
		while (!(n == null || n.isLeaf())) {
			prev = n;
			if (p.x >= n.xm) { // east
				n = (p.y >= n.ym) ? n.sse : n.sne;
			} else {// west
				n = (p.y >= n.ym) ? n.ssw : n.snw;
			}
		}
		// get the collection of submaps
		submaps = (n != null) ? n.submaps : prev.submaps;
		if (submaps == null)
			return null;
		// if any holds that point, then return it
		for (MyRectangle r : submaps) {
			if (r.contains(p)) // MathUtilities.isInside(r, p)
				return r;
		}
		return null; // Error 404
	}

	public void addMap(MyRectangle r) {
		int c;
		if (r == null)
			return;
		c = updateBoundingBox(r);
		if (c < 0)
			return;
		maps.put(r.ID, r);
		if (c > 0)
			rebuild();
		else
			addNotRebuilding(r);
	}

	public void addMaps(Collection<MyRectangle> mapsList) {
		int[] cc = { -1 };
		mapsList.forEach(r -> {
			int c;
			if (r != null) {
				c = updateBoundingBox(r);
				if (c >= 0) {
					if (cc[0] < c)
						cc[0] = c;
					maps.put(r.ID, r);
				}
			}
		});
		if (cc[0] > 0)
			rebuild();
		else
			mapsList.forEach(this::addNotRebuilding);
	}

	protected void addNotRebuilding(MyRectangle map) {
		// TODO
//		throw new UnsupportedOperationException("Too lazy to think a so-variable scenario");
		if (root == null)
			rebuild();
		else
			root = addNotRebuilding(map, root);
	}

	protected NodeMultiISOMRectangular newNodeWith(MyRectangle map, NodeMultiISOMRectangular currentNode) {
		NodeMultiISOMRectangular newNode;
		newNode = new NodeMultiISOMRectangular(currentNode);
		newNode.submaps = new ArrayList<>(maximumSubmapsEachSection);
		newNode.submaps.add(map);
		return newNode;
	}

	// the recursive part
	// returns the node given or a newly created one
	protected NodeMultiISOMRectangular addNotRebuilding(MyRectangle map, NodeMultiISOMRectangular currentNode) {
		if (currentNode.isLeaf()) {
			if (currentNode.submaps.size() < this.maximumSubmapsEachSection) {
				// just add and nothing more
				currentNode.submaps.add(map);
			} else {
				// else, build ricoursively
				currentNode.submaps.add(map);
				currentNode = rebuild(currentNode.father, currentNode.submaps, currentNode.x, currentNode.y,
						currentNode.x + currentNode.w, currentNode.y + currentNode.w, currentNode.w, currentNode.h,
						currentNode.xm, currentNode.ym);
			}

			// V2, later discarded
//			List<MISOMWrapper<Distance>> newSetSubmaps;
//			if (currentNode.isLeaf()) {
//				newSetSubmaps = currentNode.submaps;
//				if (newSetSubmaps.size() >= this.maximumSubmapsEachSection) {
//					final List<MISOMWrapper<Distance>> tempSubmaps; // a final field is required
//					tempSubmaps = new LinkedList<>();
//					newSetSubmaps.forEach(mw_ -> tempSubmaps.add(mw_));
//					newSetSubmaps = tempSubmaps;
//				} // else just add and nothing more
//				newSetSubmaps.add(r);
//				// rebuild if necessary
//				currentNode = rebuild(currentNode.father, newSetSubmaps, currentNode.x, currentNode.y,
//						currentNode.x + currentNode.w, currentNode.y + currentNode.w, currentNode.w, currentNode.h,
//						currentNode.xm, currentNode.ym);
//			} else		{
		} else {
			int hw, hh, x_w_1, y_h_1;
			hw = currentNode.w >> 1;
			hh = currentNode.h >> 1;
			x_w_1 = currentNode.x + hw + 1;
			y_h_1 = currentNode.y + hh + 1;
			// for each subsection ..
			if ((currentNode.snw != null && currentNode.snw.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.snw == null && //
							MathUtilities.intersects(currentNode.x, currentNode.y, hw, hh, map.x, map.y, map.width,
									map.height))) {
				currentNode.snw = (currentNode.snw == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.snw);
			}
			if ((currentNode.sne != null && currentNode.sne.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.sne == null && //
							MathUtilities.intersects(x_w_1, currentNode.y, currentNode.w - hw, hh, map.x, map.y,
									map.width, map.height))) {
				currentNode.sne = (currentNode.sne == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.sne);
			}
			if ((currentNode.ssw != null && currentNode.ssw.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.ssw == null && //
							MathUtilities.intersects(currentNode.x, y_h_1, hw, currentNode.h - hh, map.x, map.y,
									map.width, map.height))) {
				currentNode.ssw = (currentNode.ssw == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.ssw);
			}
			if ((currentNode.sse != null && currentNode.sse.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.sse == null && //
							MathUtilities.intersects(x_w_1, y_h_1, currentNode.w - hw, currentNode.h - hh, map.x, map.y,
									map.width, map.height))) {
				currentNode.sse = (currentNode.sse == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.sse);
			}
		}
		return currentNode;
	}

	public void removeMap(MyRectangle r) {
		if (maps.containsKey(r.ID)) {
			maps.remove(r.ID);
			recalculateBoundingBox();
			rebuild();
		}
	}

	public void removeMap(Collection<MyRectangle> mapsList) {
		boolean[] cc = { false };
		mapsList.forEach(r -> {
			if (maps.containsKey(r.ID)) {
				cc[0] = true;
				maps.remove(r.ID);
			}
		});
		if (cc[0]) {
			recalculateBoundingBox();
			rebuild();
		}
	}

	protected void recalculateBoundingBox() {
		clearDimensionCache();
		mapsAsList.forEach(this::updateBoundingBox);
	}

	protected int updateBoundingBox(MyRectangle r) {
		boolean changed;
		int t;
		if (r.width < 1 || r.height < 1) { return -1; }
		if (neverBuilt) {
			neverBuilt = false;
			xLeftTop = r.x;
			yLeftTop = r.y;
			width = r.width;
			height = r.height;
			xRightBottom = (r.x + r.width) - 1;
			yRightBottom = (r.y + r.height) - 1;
			return 1;
		}
		changed = false;
		if (r.x < xLeftTop) {
			changed = true;
			xLeftTop = r.x;
		}
		if (r.y < yLeftTop) {
			changed = true;
			yLeftTop = r.y;
		}
		t = r.x + r.width;
		if (t > xRightBottom) {
			changed = true;
			xRightBottom = t;
		}
		t = r.y + r.height;
		if (t > yRightBottom) {
			changed = true;
			yRightBottom = t;
		}
		this.width = xRightBottom - xLeftTop;
		this.height = yRightBottom - yLeftTop;
		return (changed || root == null) ? 1 : 0;
	}

	protected void rebuild() {
		this.maxDepth = 0;
		this.root = rebuild(null, mapsAsList, //
				xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height//
				, xLeftTop + (width >> 1)//
				, yLeftTop + (height >> 1)); // clear all
	}

	protected NodeMultiISOMRectangular rebuild(NodeMultiISOMRectangular father, List<MyRectangle> submaps, //
			int xLeftTop, int yLeftTop, int xRightBottom, int yRightBottom, int width, int height, //
			int xm, int ym) {
		final int mxw, mxe, myn, mys, widthWest, widthEst, heightNorth, heightSouth, ymmmmm, xmmmmm;
		NodeMultiISOMRectangular n;
		List<MyRectangle> snw, sne, ssw, sse;
		n = new NodeMultiISOMRectangular(father);
		if (this.maxDepth < n.depth) { this.maxDepth = n.depth; }
		n.x = xLeftTop;
		n.y = yLeftTop;
		n.w = width;
		n.h = height;
		n.xm = xm;
		n.ym = ym;
		if (submaps.size() <= maximumSubmapsEachSection //
				|| width <= MINIMUM_DIMENSION_MAP || height <= MINIMUM_DIMENSION_MAP) {
			n.submaps = submaps;
			return n;
		}
		snw = new ArrayList<>(maximumSubmapsEachSection);
		sne = new ArrayList<>(maximumSubmapsEachSection);
		ssw = new ArrayList<>(maximumSubmapsEachSection);
		sse = new ArrayList<>(maximumSubmapsEachSection);
		ymmmmm = ym;
		xmmmmm = xm;
		mxw = xLeftTop + ((widthWest = (xm - xLeftTop)) >> 1);
		myn = yLeftTop + ((heightNorth = (ym - yLeftTop)) >> 1);
		mxe = ++xm + ((widthEst = 1 + (xRightBottom - xm)) >> 1);
		mys = ++ym + ((heightSouth = 1 + (yRightBottom - ym)) >> 1);
		xm = xmmmmm;
		ym = ymmmmm;
		submaps.forEach(r -> {
			if (MathUtilities.intersects(xLeftTop, yLeftTop, widthWest, heightNorth, r.x, r.y, r.width, r.height)) {
				snw.add(r);
			}
			if (MathUtilities.intersects(xmmmmm + 1, yLeftTop, widthEst, heightNorth, r.x, r.y, r.width, r.height)) {
				sne.add(r);
			}
			if (MathUtilities.intersects(xmmmmm + 1, ymmmmm + 1, widthEst, heightSouth, r.x, r.y, r.width, r.height)) {
				sse.add(r);
			}
			if (MathUtilities.intersects(xLeftTop, ymmmmm + 1, widthWest, heightSouth, r.x, r.y, r.width, r.height)) {
				ssw.add(r);
			}
		});
		if (snw.size() > 0)
			n.snw = rebuild(n, snw, //
					xLeftTop, yLeftTop, xmmmmm, ymmmmm, // corner points
					widthWest, heightNorth, //
					mxw, myn); // middle point
		if (sne.size() > 0)
			n.sne = rebuild(n, sne, //
					(xmmmmm + 1), yLeftTop, xRightBottom, ymmmmm, // corner points
					widthEst, heightNorth, // dimensions
					mxe, myn); // middle point
		if (ssw.size() > 0)
			n.ssw = rebuild(n, ssw, //
					xLeftTop, ymmmmm + 1, xmmmmm, yRightBottom, // corner points
					widthWest, heightSouth, // dimensions
					mxw, mys); // middle point
		if (sse.size() > 0)
			n.sse = rebuild(n, sse, //
					xmmmmm + 1, ymmmmm + 1, xRightBottom, yRightBottom, // corner points
					widthEst, heightSouth, // dimensions
					mxe, mys); // middle point
		return n;
	}

	//

	//

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append("Multi isom: having size: ");
		sb.append("[ (x: ").append(xLeftTop).append(", y: ").append(yLeftTop).append(") - (w: ").append(width)
				.append(", h: ").append(height).append(") ]");
		if (root != null) { root.toString(sb, 1); }
		return sb.toString();
	}

	static void printTab(int d) {
		for (int i = d; i > 0; i--) {
			System.out.print('\t');
		}
	}
//

// TODO CLASSES

	//

	class NodeMultiISOMRectangular implements Stringable {
		private static final long serialVersionUID = 1L;
		protected int x, y, w, h, xm, ym, depth;
		List<MyRectangle> submaps;
		NodeMultiISOMRectangular father, snw, sne, ssw, sse;

		NodeMultiISOMRectangular(NodeMultiISOMRectangular father) {
			this.father = father;
			submaps = null;
			depth = (father == null) ? 1 : (father.depth + 1);
		}

		public boolean isLeaf() { return submaps != null; }

		public boolean intersectsWithMap(MyRectangle map) {
			return MathUtilities.intersects(x, y, w, h, map.x, map.y, map.width, map.height);
		}

		@Override
		public String toString() { return "Node--[x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]"; }

		@Override
		public void toString(StringBuilder sb, int tabLevel) {
			sb.append('\n');
			addTab(sb, tabLevel);
			sb.append("Node at level ").append(tabLevel).append(", is:");
			addTab(sb, tabLevel + 1);
			sb.append(toString());
			addTab(sb, tabLevel);
			sb.append("and has subparts:");
			if (isLeaf()) {
				int t;
				sb.append(" is leaf:n");
				t = 1 + tabLevel;
				this.submaps.forEach(r -> {
//					sb.append('\n');
					addTab(sb, t);
					sb.append(r);
				});
//				sb.append(t);
			} else {
				tabLevel++;
				if (snw != null) { snw.toString(sb, tabLevel); }
				if (sne != null) { sne.toString(sb, tabLevel); }
				if (ssw != null) { ssw.toString(sb, tabLevel); }
				if (sse != null) { sse.toString(sb, tabLevel); }
			}
		}
	}

	public static final void main(String[] args) {
		int factor;
		List<MyRectangle> rects;
		Test_MultiISOMRetangularMap_V1 t;
		T_MISOM_GUI gui;
		rects = Arrays.asList(new MyRectangle[] { //
				new MyRectangle(-5, -3, 10, 8), // very wide, up part
				new MyRectangle(4, 5, 2, 5), // on right, vertical
				new MyRectangle(-2, 5, 4, 13), // on left, very high (vertical)
				new MyRectangle(2, 12, 7, 4), // on bottom, wide
		});
		System.out.println("rectangles:");
		rects.forEach(r -> System.out.println(r));
		t = new Test_MultiISOMRetangularMap_V1(MAXIMUM_SUBMAPS_EACH_SECTION);
		t.addMaps(rects);
		System.out.println("\n\nadded");
		System.out.println(t);
		System.out.println("\n\n\n mega add");

		rects = Arrays.asList(new MyRectangle[] { //
				new MyRectangle(0, 0, 20, 80).setName("a"), //
				new MyRectangle(20, 12, 40, 8).setName("b"), //
				new MyRectangle(60, 4, 25, 24).setName("c"), //
				new MyRectangle(56, 28, 12, 20).setName("d"), // d
				new MyRectangle(48, 36, 8, 8).setName("e"), //
				new MyRectangle(32, 32, 16, 24).setName("f"), // f
				new MyRectangle(26, 36, 6, 20).setName("g"), //
				new MyRectangle(24, 56, 12, 12).setName("h"), //
				new MyRectangle(50, 48, 34, 10).setName("i"), // i
				new MyRectangle(38, 64, 10, 16).setName("l"), //
				new MyRectangle(52, 58, 8, 28).setName("m"), //
				new MyRectangle(68, 60, 5, 12).setName("n"), //
				new MyRectangle(36, -10, 20, 7).setName("o"), // o
				new MyRectangle(28, 4, 8, 8).setName("p"), //
				new MyRectangle(20, 76, 12, 8).setName("q"), //
				new MyRectangle(72, 28, 12, 16).setName("r"), // r
				new MyRectangle(60, 72, 16, 8).setName("s"), //
				new MyRectangle(45, -3, 5, 15).setName("t"), //
				new MyRectangle(48, 70, 4, 5).setName("u"), //
				new MyRectangle(73, 70, 1, 1).setName("v"), //
				new MyRectangle(73, 68, 1, 1).setName("x"), //
				new MyRectangle(73, 66, 1, 1).setName("y"), //
				new MyRectangle(73, 64, 1, 1).setName("w"), //
				new MyRectangle(73, 62, 1, 1).setName("z"), // z
				new MyRectangle(40, 56, 5, 5).setName("h2"), //
		});
		factor = 8;
		System.out.println("start mega add " + rects.size());
		for (MyRectangle r : rects) {
			r.x *= factor;
			r.y *= factor;
			r.width *= factor;
			r.height *= factor;
			System.out.println("- rectangle: " + r);
//			System.out.println(r.x + ", " + r.y + ", " + r.width + ", " + r.height);
		}
//		t = new Test_MultiISOMRetangularMap_V1();
		System.out.println("\n\nadded");
		gui = new T_MISOM_GUI(t);
		gui.rebuildGUI();
		gui.resetRects(rects);
		System.out.println(t);
		System.out.println("\n\n\n mega add");
	}

	//

	//

	//

	//
	static class T_MISOM_GUI {
		static int idProgNewRect = 100;
//		boolean isRectangleDrawning;
		JFrame win;
		JPanel jp;
		JScrollPane jsp;
		List<MyRectangle> rects = null;
		Test_MultiISOMRetangularMap_V1 t;
		Point pStartDrawningRect = null, pEndDrawningRect = null;
		MyRectangle newRect = null;

		public T_MISOM_GUI(Test_MultiISOMRetangularMap_V1 t) {
			super();
			this.t = t;
		}

		void rebuildGUI() {
			MouseAdapter ma;
			win = new JFrame("Test Multi ISOM");
			win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			jp = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					paintRectsAndSections(g);
				}
			};
			jsp = new JScrollPane(jp);
			jsp.setViewportView(jp);
			win.add(jsp);
			win.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					Dimension d;
					d = win.getSize();
					d.width -= 10;
					d.height -= 25;
					jsp.setSize(d);
					jsp.setPreferredSize(d);
				}
			});
			ma = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) { onMouseClick(e); }

				@Override
				public void mousePressed(MouseEvent e) {
					System.out.println("____ pressed on " + e.getPoint());
					onStartDrawningNewRectangle(e.getPoint());
					jp.repaint();
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					pEndDrawningRect = e.getPoint();
//					System.out.println("����dragged on " + pEndDrawningRect);
					if (t.xLeftTop < 0) { pEndDrawningRect.x += t.xLeftTop; }
					if (t.yLeftTop < 0) { pEndDrawningRect.y += t.yLeftTop; }
					updateStartEndPoinNewRectangle();
					jp.repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					System.out.println("____ released on " + e.getPoint());
					onEndDrawningNewRectangle(e.getPoint());
					jp.repaint();
				}
			};
			jp.addMouseListener(ma);
			jp.addMouseMotionListener(ma);
			win.setSize(500, 500);
			win.setVisible(true);
		}

		void paintRectsAndSections(Graphics g) {
			Color c;
			int minx, miny;
			minx = t.xLeftTop - 1;
			miny = t.yLeftTop - 1;
			c = Color.GREEN.darker();
			if (t != null && t.maps != null) {
//				for (MyRectangle r : rects) {
				t.maps.forEach((id, r) -> {
					g.setColor(c);
//					g.drawRect(r.x - minx, r.y - miny, r.width, r.height);
					g.fillRect(r.x - minx, r.y - miny, r.width, r.height);
					g.setColor(Color.BLACK);
					g.drawString(r.name, ((r.x - minx) + (r.width >> 1)), ((r.y - miny) + (r.height >> 1)));
					g.drawString(r.name, (r.x - minx), (r.y - miny));
				}//
				);
			}
			if (this.newRect != null) {
				g.setColor(Color.RED);
				g.drawRect(//
						(t.xLeftTop >= 0) ? this.newRect.x : //
								this.newRect.x - t.xLeftTop,
						(t.yLeftTop >= 0) ? this.newRect.y : //
								this.newRect.y - t.yLeftTop, //
						this.newRect.width, this.newRect.height);
			}
			if (t != null && t.root != null) { paintSubdivision(g); }
		}

		void paintSubdivision(Graphics g) {
			int halfDepth, minx, miny;
			minx = t.xLeftTop - 1;
			miny = t.yLeftTop - 1;
			NodeMultiISOMRectangular n;
			LinkedList<NodeMultiISOMRectangular> nodes;
			nodes = new LinkedList<>();
			n = t.root;
			nodes.add(n);
			while (!nodes.isEmpty()) {
				n = nodes.removeFirst();
				g.setColor(Color.RED);
				g.drawString(Integer.toString(n.depth), n.xm - minx, n.ym - miny);
				if (!n.isLeaf()) {
					g.setColor(Color.LIGHT_GRAY);
					halfDepth = n.depth >> 1;
					// horizonta
					g.fillRect(n.x - minx, n.ym - (halfDepth + miny), n.w, 1 + (t.maxDepth - n.depth));
//vertical
					g.fillRect(n.xm - (halfDepth + minx), n.y - miny, 1 + (t.maxDepth - n.depth), n.h);
					if (n.sne != null) { nodes.add(n.sne); }
					if (n.snw != null) { nodes.add(n.snw); }
					if (n.sse != null) { nodes.add(n.sse); }
					if (n.ssw != null) { nodes.add(n.ssw); }
				}
			}
		}

		void resetRects(List<MyRectangle> r) {
			this.rects = r;
			t.clear();
			t.addMaps(rects);
			jp.setSize(t.width + 200, t.height + 200);
			jp.setPreferredSize(jp.getSize());
		}

		void onMouseClick(MouseEvent me) {
			Point whereClicked;
			MyRectangle mapFound;
			whereClicked = me.getPoint();
			// consider the offset
			if (t.xLeftTop < 0) { whereClicked.x += t.xLeftTop; }
			if (t.yLeftTop < 0) { whereClicked.y += t.yLeftTop; }
			System.out.println("finding a map on " + whereClicked + " ..");
			mapFound = t.getMapContaining(whereClicked);
			System.out.println(".. map found: " + mapFound);
			System.out.println("me.getButton(): " + me.getButton());
			if (mapFound != null && me.getButton() == MouseEvent.BUTTON3) { t.removeMap(mapFound); }
		}

		void updateStartEndPoinNewRectangle() {
			int temp;
			Point ps, pe;
			if (this.pStartDrawningRect.x == this.pEndDrawningRect.x
					|| this.pStartDrawningRect.y == this.pEndDrawningRect.y)
				return;
			ps = new Point(this.pStartDrawningRect);
			pe = new Point(this.pEndDrawningRect);
			if (ps.x > pe.x) {
				temp = ps.x;
				ps.x = pe.x;
				pe.x = temp;
			}
			if (ps.y > pe.y) {
				temp = ps.y;
				ps.y = pe.y;
				pe.y = temp;
			}
			if (newRect == null) {
				this.newRect = new MyRectangle(ps, new Dimension( //
						pe.x - ps.x, //
						pe.y - ps.y));
				this.newRect.setName( // use the atom to make the name cool
						(new AtomLogicProposition(true, idProgNewRect++)).getName());
			} else {
				this.newRect.x = ps.x;
				this.newRect.y = ps.y;
				this.newRect.width = pe.x - ps.x;
				this.newRect.height = pe.y - ps.y;
			}
		}

		void onStartDrawningNewRectangle(Point pointEnd) {
//			this.isRectangleDrawning = true;
			this.newRect = null;
			this.pEndDrawningRect = null;
			this.pStartDrawningRect = pointEnd;
			// consider the offset
			if (t.xLeftTop < 0) { pointEnd.x += t.xLeftTop; }
			if (t.yLeftTop < 0) { pointEnd.y += t.yLeftTop; }
		}

		void onEndDrawningNewRectangle(Point pointEnd) {
			this.pEndDrawningRect = pointEnd;
			// consider the offset
			if (t.xLeftTop < 0) { pointEnd.x += t.xLeftTop; }
			if (t.yLeftTop < 0) { pointEnd.y += t.yLeftTop; }
			updateStartEndPoinNewRectangle();
//			this.isRectangleDrawning = false;
			buildAndAddMap();
			this.pStartDrawningRect = null;
			this.pEndDrawningRect = null;
		}

		void buildAndAddMap() {
			if (this.newRect == null)
				return;
			System.out.println("\n\n adding new map: " + newRect);
			t.addMap(newRect);
			this.newRect = null;
			jp.setSize(t.width + 200, t.height + 200);
			jp.setPreferredSize(jp.getSize());
			jp.repaint();
		}
	}
}