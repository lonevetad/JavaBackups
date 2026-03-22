package tests.tDataStruct;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import dataStructures.MapTreeAVL;
import tools.Comparators;
import tools.Stringable;

// perchè esiste?
@Deprecated
public class Test_MultiISOMRetangularMap_V2 {
	public static final int MAXIMUM_SUBMAPS_EACH_SECTION = 4, MINIMUM_DIMENSION_MAP = 4;

	/**
	 * Construct
	 */
	public Test_MultiISOMRetangularMap_V2() {
		maps = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
		mapsAsList = maps.toListValue(r -> r.ID);
		root = null;
		xLeftTop = yLeftTop = xRightBottom = yRightBottom = width = height = 0;
		neverBuilt = true;
	}

	protected boolean neverBuilt;
	protected MapTreeAVL<Integer, MyRectangle> maps;
	protected List<MyRectangle> mapsAsList;
	protected NodeMultiISOMRectangular root;
	// of bounding box
	protected int xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height;

//

///

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

	protected void addNotRebuilding(MyRectangle r) {
		// TODO
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
		root = rebuild(null, mapsAsList, //
				xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height//
				, xLeftTop + (width >> 1)//
				, yLeftTop + (height >> 1)); // clear all
	}

	protected NodeMultiISOMRectangular rebuild(NodeMultiISOMRectangular father, List<MyRectangle> submaps, int xLeftTop,
			int yLeftTop, int xRightBottom, int yRightBottom, int width, int height, int xm, int ym) {
		int mxw, mxe, myn, mys, widthWest, widthEst, heightNorth, heightSouth;
		final int ymmmmm, xmmmmm;
		NodeMultiISOMRectangular n;
		List<MyRectangle> snw, sne, ssw, sse;
		n = new NodeMultiISOMRectangular(father);
		n.x = xLeftTop;
		n.y = yLeftTop;
		n.w = width;
		n.h = height;
		n.xm = xm;
		n.ym = ym;
		if (submaps.size() <= MAXIMUM_SUBMAPS_EACH_SECTION
		// || width<MINIMUM_DIMENSION_MAP||height<MINIMUM_DIMENSION_MAP
		) {
			n.submaps = submaps;
			return n;
		}
		snw = new LinkedList<>();
		sne = new LinkedList<>();
		ssw = new LinkedList<>();
		sse = new LinkedList<>();
		ymmmmm = ym;
		xmmmmm = xm;
		submaps.forEach(r -> {
			int xmr, ymr;
			xmr = r.x + (r.width >> 1);
			ymr = r.y + (r.height >> 1);
			if (ymr >= ymmmmm) {// sud
				((xmr >= xmmmmm) ? sse : ssw).add(r);
			} else {// nord
				((xmr >= xmmmmm) ? sne : snw).add(r);
			}
		});
		mxw = xLeftTop + (widthWest = ((xm - xLeftTop) >> 1));
		myn = yLeftTop + (heightNorth = ((ym - yLeftTop) >> 1));
		mxe = ++xm + (widthEst = ((xRightBottom - xm) >> 1));
		mys = ++ym + (heightSouth = ((yRightBottom - ym) >> 1));
		if (sse.size() > 0)
			n.sse = rebuild(n, sse, //
					xm, ym, xRightBottom, yRightBottom, widthEst, heightSouth, mxe, mys);
		if (ssw.size() > 0)
			n.ssw = rebuild(n, ssw, //
					xLeftTop, --ym, --xm, yRightBottom, widthWest, heightSouth, mxw, mys);
		if (sne.size() > 0)
			n.sne = rebuild(n, sne, //
					(xm + 1), yLeftTop, xRightBottom, yRightBottom, widthEst, heightNorth, mxe, myn);
		if (snw.size() > 0)
			n.snw = rebuild(n, snw, //
					xLeftTop, yLeftTop, xm, ym, widthWest, heightNorth, mxw, myn);
		return n;
	}

	//

	//

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append("Multi isom:");
		if (root != null) { root.toString(sb, 1); }
		return sb.toString();
	}

//

// TODO CLASSES

	//

	class NodeMultiISOMRectangular implements Stringable {
		private static final long serialVersionUID = 1L;
		protected int x, y, w, h, xm, ym;
		List<MyRectangle> submaps;
		NodeMultiISOMRectangular father, snw, sne, ssw, sse;

		NodeMultiISOMRectangular(NodeMultiISOMRectangular father) {
			this.father = father;
			submaps = null;
		}

		public boolean isLeaf() { return submaps != null; }

		@Override
		public void toString(StringBuilder sb, int tabLevel) {
			sb.append('\n');
			addTab(sb, tabLevel);
			sb.append("Node at level ").append(tabLevel).append(", is:\n");
			addTab(sb, tabLevel + 1);
			sb.append(super.toString()).append('\n');
			addTab(sb, tabLevel);
			sb.append("and has subparts:");
			if (isLeaf()) {
				int t;
				sb.append(" is leaf:\n");
				t = 1 + tabLevel;
				this.submaps.forEach(r -> {
					sb.append('\n');
					addTab(sb, t);
					sb.append(r);
				});
			} else {
				tabLevel++;
				if (snw != null) {
					sb.append('\n');
					snw.toString(sb, tabLevel);
				}
				if (sne != null) {
					sb.append('\n');
					sne.toString(sb, tabLevel);
				}
				if (ssw != null) {
					sb.append('\n');
					ssw.toString(sb, tabLevel);
				}
				if (sse != null) {
					sb.append('\n');
					sse.toString(sb, tabLevel);
				}
			}
		}
	}

	public static final void main(String[] args) {
		List<MyRectangle> rects;
		Test_MultiISOMRetangularMap_V2 t;
		rects = Arrays.asList(new MyRectangle[] { //
				new MyRectangle(-5, -3, 10, 8), // very wide, up part
				new MyRectangle(4, 5, 2, 5), // on right, vertical
				new MyRectangle(-2, 5, 4, 13), // on left, very high (vertical)
				new MyRectangle(2, 12, 7, 4), // on bottom, wide
		});
		System.out.println("rectangles:");
		rects.forEach(r -> System.out.println(r));
		t = new Test_MultiISOMRetangularMap_V2();
		t.addMaps(rects);
		System.out.println("\n\nadded");
		System.out.println(t);
	}

}