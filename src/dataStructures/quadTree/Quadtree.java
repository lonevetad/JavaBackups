package dataStructures.quadTree;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import dataStructures.quadTree.NodeQuadtree.PointDataConsumers;
import dataStructures.quadTree.NodeQuadtree.SubsectionData;

public class Quadtree<P extends Point2D, D> implements Serializable {
	private static final long serialVersionUID = 30877282782054L;

	public static final Comparator<Point2D.Double> COMPARATOR_POINT_2D_DOUBLE_X_FIRST = (p1, p2) -> {
		int c;
		double t1, t2;
		if (p1 == p2) {
			return 0;
		}
		if (p1 == null) {
			return -1;
		}
		if (null == p2) {
			return 1;
		}
		t1 = p1.x;
		t2 = p2.x;
		c = (t1 == t2) ? 0 : (t1 > t2 ? 1 : -1);
		if (c != 0) {
			return c;
		}
		t1 = p1.y;
		t2 = p2.y;
		return (t1 == t2) ? 0 : (t1 > t2 ? 1 : -1);
	}, COMPARATOR_POINT_2D_DOUBLE_Y_FIRST = (p1, p2) -> {
		int c;
		double t1, t2;
		if (p1 == p2) {
			return 0;
		}
		if (p1 == null) {
			return -1;
		}
		if (null == p2) {
			return 1;
		}
		t1 = p1.y;
		t2 = p2.y;
		c = (t1 == t2) ? 0 : (t1 > t2 ? 1 : -1);
		if (c != 0) {
			return c;
		}
		t1 = p1.x;
		t2 = p2.x;
		return (t1 == t2) ? 0 : (t1 > t2 ? 1 : -1);
	};

	public static Quadtree<Point2D.Double, Double> fromXYDoublePairWithZAxes(double[][] dataset, double[] zAxes,
			int maxPointsPerSubsection) {
		return fromPointDoubleWithZAxes((java.awt.geom.Point2D.Double[]) Arrays.stream(dataset)
				.map(a -> new Point2D.Double(a[0], a[1])).toArray(), zAxes, maxPointsPerSubsection);
	}

	public static Quadtree<Point2D.Double, Double> fromPointDoubleWithZAxes(Point2D.Double[] dataset, double[] zAxes,
			int maxPointsPerSubsection) {
		return new Quadtree<Point2D.Double, Double>(dataset,
				(Double[]) Arrays.stream(zAxes).mapToObj(Double::valueOf).toArray(), maxPointsPerSubsection);
	}

	public static Quadtree<Point, Integer> fromPointIntWithZAxes(Point[] dataset, int[] zAxes,
			int maxPointsPerSubsection) {
		return new Quadtree<Point, Integer>(dataset,
				(Integer[]) Arrays.stream(zAxes).mapToObj(Integer::valueOf).toArray(), maxPointsPerSubsection);
	}

	/**
	 * Maximum amount of point that a subsection can store before splitting.
	 */
	protected final int maxPointsPerSubsection;
	protected int size, maxDepth;
	protected SubsectionData boundingBox;
	protected NodeQuadtree<P, D> root;

	/**
	 * Returns an array holding, respectively, the x and y components of the
	 * bottom-left Point2D of the given pointsAndData set's bounding box, its height
	 * and its width
	 */
	protected static <P extends Point2D> SubsectionData extractBoundingBox(P[] initialPoints) {
		double xMax, yMax, temp;
		P p;
		SubsectionData sd;
		sd = new SubsectionData(0.0, 0.0, 0.0, 0.0);

		if (initialPoints == null || initialPoints.length <= 0) {
			return sd;
		}

		p = initialPoints[0];
		sd.xBottomLeft = xMax = p.getX();
		sd.yBottomLeft = yMax = p.getY();

		for (int i = 1, len = initialPoints.length; i < len; i++) {
			p = initialPoints[i];

			temp = p.getX();
			if (temp < sd.xBottomLeft) {
				sd.xBottomLeft = temp;
			}
			if (temp > xMax) {
				xMax = temp;
			}
			temp = xMax - sd.xBottomLeft; // width
			if (temp > sd.width) {
				sd.width = temp;
			}

			temp = p.getY();
			if (temp < sd.yBottomLeft) {
				sd.yBottomLeft = temp;
			}
			if (temp > yMax) {
				yMax = temp;
			}
			temp = yMax - sd.yBottomLeft; // height
			if (temp > sd.height) {
				sd.height = temp;
			}
		}

		return sd;
	}

	public Quadtree(P[] initialPoints, D datas[], int maxPointsPerSubsection) {
		this(initialPoints, datas, maxPointsPerSubsection, extractBoundingBox(initialPoints));
	}

	public Quadtree(P[] initialPoints, D datas[], int maxPointsPerSubsection, double xBottomLeft, double yBottomLeft,
			double height, double width) {
		this(initialPoints, datas, maxPointsPerSubsection, new SubsectionData(xBottomLeft, yBottomLeft, height, width));
	}

	protected Quadtree(P[] initialPoints, D datas[], int maxPointsPerSubsection, SubsectionData boundingBox) {
		if ((boundingBox.height <= 0.0)) {
			throw new IllegalArgumentException("height ( " + boundingBox.height + " ) must be greater than zero.");
		}
		if ((boundingBox.width <= 0.0)) {
			throw new IllegalArgumentException("width ( " + boundingBox.width + " ) must be greater than zero.");
		}
		this.maxPointsPerSubsection = maxPointsPerSubsection;
		this.boundingBox = boundingBox;
		this.size = 0;
		this.root = buildFromDataset(initialPoints, datas);
	}

	//

	protected NodeQuadtree<P, D> newNode(NodeQuadtree<P, D> father, int maxPointsPerSubsection,
			SubsectionData boundingBox) {
		return new NodeQuadtree<>(father, maxPointsPerSubsection, boundingBox);
	}

	protected NodeQuadtree<P, D> newNode(NodeQuadtree<P, D> father, int maxPointsPerSubsection, double xBottomLeft,
			double yBottomLeft, double width, double height) {
		return newNode(father, maxPointsPerSubsection, new SubsectionData(xBottomLeft, yBottomLeft, width, height));
	}

	// GETTER

	@SuppressWarnings("unchecked")
	protected NodeQuadtree<P, D>[] newSubsectionsArray(int size) {
		return new NodeQuadtree[size];
	}

	/**
	 * Maximum amount of point that a subsection can store before splitting.
	 * 
	 * @return the maxPointsPerSubsection
	 */
	public int getmaxPointsPerSubsection() {
		return maxPointsPerSubsection;
	}

	/**
	 * @return the boundingBox
	 */
	public SubsectionData getBoundingBox() {
		return boundingBox;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the maxDepth
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	//

	/**
	 * Same method as {@link #getSubsectionFor(P, NodeQuadtree)}, but returns just
	 * the index
	 * 
	 * @param p
	 * @param startingNode
	 * @return
	 */
	protected int getSubsectionIndexFor(P p, NodeQuadtree<P, D> startingNode) {
		boolean isLeft, isBottom;
		int index;
		double xMid, yMid;
		xMid = startingNode.getxBottomLeft() + (startingNode.getWidth() / 2.0);
		yMid = startingNode.getyBottomLeft() + (startingNode.getHeight() / 2.0);

		isLeft = p.getX() < xMid;
		isBottom = p.getY() < yMid;

		if (isLeft) {
			if (isBottom) {
				index = 0;
			} else {
				index = 1;
			}
		} else {
			if (isBottom) {
				index = 3;
			} else {
				index = 2;
			}
		}
		return index;
	}

	/**
	 * Evolution of {@link #getSubsectionIndexFor(P, NodeQuadtree)}; returns the
	 * whole description of the subsection of the given node that could store the
	 * given point.
	 * 
	 * @param p
	 * @param startingNode
	 * @return
	 */
	protected SubsectionData getSubsectionFor(P p, NodeQuadtree<P, D> startingNode) {
		boolean isLeft, isBottom;
		int index;
		double xMid, yMid, ww, hh, x, y;
		ww = startingNode.getWidth() / 2.0;
		hh = startingNode.getHeight() / 2.0;
		xMid = (x = startingNode.getxBottomLeft()) + ww;
		yMid = (y = startingNode.getyBottomLeft()) + hh;
		isLeft = p.getX() < xMid;
		isBottom = p.getY() < yMid;

		if (isLeft) {
			if (isBottom) {
				index = 0;
			} else {
				index = 1;
				y = yMid;
			}
		} else {
			x = xMid;
			if (isBottom) {
				index = 3;
			} else {
				index = 2;
				y = yMid;
			}
		}

		return new SubsectionData(x, y, ww, hh, index, 0);
	}

	protected NodeQuadtree<P, D> findNodeCouldContain(NodeQuadtree<P, D> startingNode, P p) {
		boolean canIter;
		NodeQuadtree<P, D> tempNode;

		if (!startingNode.isInside(p)) {
			return null;
		}

		tempNode = startingNode;
		canIter = true;
		while (canIter && (!startingNode.isLeaf())) {

			tempNode = startingNode.getSubsectionAt(this.getSubsectionIndexFor(p, startingNode));
			if (tempNode == null) {
				canIter = false;
			} else {
				startingNode = tempNode;
			}
		}
		return startingNode;
	}

	protected NodeQuadtree<P, D> buildFromDataset(P[] initialPoints, D datas[]) {
		NodeQuadtree<P, D> root;

		root = this.newNode(null, maxPointsPerSubsection, this.boundingBox);
		if (datas != null) {
			for (int i = 0, len = initialPoints.length; i < len; i++) {
				addNode(root, initialPoints[i], datas[i]);
			}
		} else {
			for (int i = 0, len = initialPoints.length; i < len; i++) {
				addNode(root, initialPoints[i], null);
			}
		}
		return root;
	}

	protected void addNode(NodeQuadtree<P, D> nodeMaybeHolder, P p, D data) {
		NodeQuadtree.PointAdditionStatus statusAddition;

		nodeMaybeHolder = this.findNodeCouldContain(nodeMaybeHolder, p);
		if (nodeMaybeHolder == null) {
			return; // error
		}

		statusAddition = nodeMaybeHolder.canPointBeAdded(p);
		if (statusAddition == NodeQuadtree.PointAdditionStatus.OutOfBounds) {
			return;
		}

		if (statusAddition == NodeQuadtree.PointAdditionStatus.InSubsectionsOnly) {
			NodeQuadtree<P, D> subsections[];
			Map<P, List<D>> oldPoints;

			subsections = nodeMaybeHolder.subsections;
			oldPoints = nodeMaybeHolder.pointsAndData;
			if ((nodeMaybeHolder.isLeaf() || subsections == null) && oldPoints != null && oldPoints.size() > 0) {

				nodeMaybeHolder.clearNode();

				subsections = this.newSubsectionsArray(NodeQuadtree.SUBDIVISIONS_AMOUNT);
				nodeMaybeHolder.setSubsections(subsections);

				// re-add the previous nodes
				final NodeQuadtree<P, D> currentNode = nodeMaybeHolder;
				final NodeQuadtree<P, D> subsections_final[] = subsections;
				oldPoints.forEach((oldp, l) -> {
					SubsectionData sd;
					NodeQuadtree<P, D> s;
					final NodeQuadtree<P, D> s_final;

					sd = this.getSubsectionFor(oldp, currentNode);
					s = subsections_final[sd.indexSubsection];
					if (s == null) {
						s = subsections_final[sd.indexSubsection] = this.newNode(currentNode, maxPointsPerSubsection,
								sd);
					}
					s_final = s;
					l.forEach(d -> s_final.addPoint(oldp, d));
				});
			}

			SubsectionData sd;
			NodeQuadtree<P, D> s;
			sd = this.getSubsectionFor(p, nodeMaybeHolder);
			if (sd == null) {
				return;
			}
			s = subsections[sd.indexSubsection];
			if (s == null) {
				s = this.newNode(nodeMaybeHolder, maxPointsPerSubsection, sd);
				subsections[sd.indexSubsection] = s;
			}
			// shift to the newly-created subsection
			nodeMaybeHolder = s;
		} // else InThisNode

		if (!nodeMaybeHolder.isLeaf()) {
			throw new RuntimeException(nodeMaybeHolder.toString());
		}
		int index_aggiunta = nodeMaybeHolder.addPoint(p, data);
		if (index_aggiunta < 0) { // recursion
			this.addNode(nodeMaybeHolder, p, data);
		} else {
			this.size++;
		}
		{
			int depth;
			depth = nodeMaybeHolder.getDepth();
			if (this.maxDepth < depth) {
				this.maxDepth = depth;
			}
		}
	}

	protected void rebuildAndAdd(P point, D optionalData) {
		final NodeQuadtree<P, D> oldRoot, newRoot;
		final SubsectionData bb;

		// save and clear
		oldRoot = this.root;
		bb = this.boundingBox;
		this.root = null;
		this.clear();

		// adjust bounding box
		{
			double x, y;
			x = point.getX();
			y = point.getY();
			if (x < bb.xBottomLeft) {
				bb.setWidth(bb.xBottomLeft + bb.width - x);
				bb.setxBottomLeft(x);
			} else {
				double maxx;
				maxx = bb.xBottomLeft + bb.width;
				if (x > maxx) {
					bb.setWidth(x - bb.xBottomLeft);
				}
			}
			if (y < bb.yBottomLeft) {
				bb.setHeight((bb.yBottomLeft + bb.height - y));
				bb.setyBottomLeft(y);
			} else {
				double maxy;
				maxy = bb.yBottomLeft + bb.height;
				if (y > maxy) {
					bb.setHeight(y - bb.yBottomLeft);
				}
			}
		}
		this.boundingBox = bb;

		// re-add
		this.root = newRoot = this.newNode(null, maxPointsPerSubsection, this.boundingBox);
		oldRoot.forEachPointData((p, data, depth) -> addNode(newRoot, p, data));

		addNode(newRoot, point, optionalData);
	}

	/**
	 * @param action
	 */
	protected void forEachSubdivisionMaxDepth(BiConsumer<SubsectionData, Integer> action,
			NodeQuadtree<P, D> currentNode) {
		action.accept(currentNode.boundingBox, maxDepth);
		if (!currentNode.isLeaf()) {
			NodeQuadtree<P, D> ss[], s;
			ss = currentNode.subsections;
			if (ss == null) {
				return;
			}
			for (int i = 0, l = ss.length; i < l; i++) {
				s = ss[i];
				if (s != null) {
					forEachSubdivisionMaxDepth(action, s);
				}
			}
		}
	}

//	protected int collectPoints(final LinkedList<List<Entry<P, List<D>>>> leaves, NodeQuadtree<P, D> currentNode) {
//		int l, partialSize;
//		NodeQuadtree<P, D> ss[];
//		Map<P, List<D>> points;
//
//		if (currentNode.isLeaf()&& points != null && points.size() > 0) {
//			leaves.add(currentNode.getDataPoints());
//			return currentNode.getAmountPointsStored();
//		}
//
//		partialSize = 0;
//		ss = currentNode.getSubsections();
//		l = ss == null ? 0 : ss.length;
//		if (l > 0) {
//			for (int i = 0; i < l; i++) {
//				if (ss[i] != null) {
//					partialSize += collectPoints(leaves, ss[i]);
//				}
//			}
//		}
//		return partialSize;
//	}

	protected int query(final LinkedList<List<Entry<P, List<D>>>> leaves, NodeQuadtree<P, D> currentNode,
			SubsectionData areaToSearch) {
		int totalSize;
		Map<P, List<D>> points;
		if (!currentNode.boundingBox.intersects(areaToSearch)) {
			return 0;
		}

		points = currentNode.pointsAndData;
		if (currentNode.isLeaf() && points != null && points.size() > 0) {
			int[] totalCollected = { 0 };
			ArrayList<Entry<P, List<D>>> collectedPoints;

			collectedPoints = new ArrayList<>(currentNode.amountPointsStored);

			points.forEach((p, l) -> {
				if (areaToSearch.isInside(p)) {
					totalCollected[0] += l.size();
					collectedPoints.add(new AbstractMap.SimpleImmutableEntry<>(p, l));
				}
			});
			if (collectedPoints.size() <= 0) {
				return 0;
			}

			leaves.add(collectedPoints);
			return totalCollected[0];
		}

		NodeQuadtree<P, D> s, ss[];
		ss = currentNode.subsections;
		totalSize = 0;

		if (ss != null && ss.length > 0) {
			for (int i = 0, l = ss.length; i < l; i++) {
				s = ss[i];
				if (s != null) {
					totalSize += query(leaves, s, areaToSearch);
				}
			}
		}
		return totalSize;
	}

	protected ArrayList<Entry<P, D>> convertList(LinkedList<List<Entry<P, List<D>>>> paginatedList, int totalSize) {
		P p;
		ArrayList<Entry<P, D>> ret;

		ret = new ArrayList<>(totalSize);
		// for each subsection-leaf visited successfully
		for (List<Entry<P, List<D>>> le : paginatedList) {
			// for each overlapping point
			for (Entry<P, List<D>> eOverlappingNode : le) {
				p = eOverlappingNode.getKey();
				for (D data : eOverlappingNode.getValue()) {
					ret.add(new AbstractMap.SimpleImmutableEntry<>(p, data));
				}
			}
		}
		return ret;
	}

	// PUBLIC

	public ArrayList<Entry<P, D>> collectAllPointsData() {
//		int totalSize;
//		LinkedList<List<Entry<P, List<D>>>> leaves;
//
//		leaves = new LinkedList<>();
//		totalSize = this.collectPoints(leaves, root);
//
//		assert totalSize == this.size;
//		if (totalSize != this.size) {
//			throw new RuntimeException("BUG: upon collecting points, calculated size (" + totalSize
//					+ ") is different from the acual one (" + this.size + ")");
//		}
//
//		return convertList(leaves, totalSize);
		return this.query(this.boundingBox.toRectangle());
	}

	public ArrayList<Entry<P, D>> query(double xBottomLeft, double yBottomLeft, double width, double height) {
		int totalSize;
		LinkedList<List<Entry<P, List<D>>>> leaves;

		leaves = new LinkedList<>();
		totalSize = query(leaves, root, new SubsectionData(xBottomLeft, yBottomLeft, width, height));

		return convertList(leaves, totalSize);
	}

	public ArrayList<Entry<P, D>> query(Rectangle2D r) {
		return this.query(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}

	public void clear() {
		this.root = null;
		this.size = 0;
		this.maxDepth = 0;
		this.boundingBox = null;
	}

	/**
	 * Add the given point and the given optional data to the map, returns
	 * <code>true</code> if a resize has been required (the bounding box has
	 * changed).
	 * 
	 * @param point
	 * @param optionalData
	 * @return <code>true</code> if the bounding box has changed.
	 */
	public boolean addPoint(P point, D optionalData) {
		if (point == null) {
			throw new IllegalArgumentException("Can't add a null point.");
		}
		if (this.boundingBox.isInside(point)) {
			this.addNode(root, point, optionalData);
			return false;
		}

		this.rebuildAndAdd(point, optionalData);
		return true;
	}

	public void forEachPointData(PointDataConsumers<P, D> action) {
		this.root.forEachPointData(action);
	}

	/**
	 * For each subdivision and max depth overall
	 * 
	 * @param action
	 */
	public void forEachSubdivisionMaxDepth(BiConsumer<SubsectionData, Integer> action) {
		this.forEachSubdivisionMaxDepth(action, this.root);
	}

	//

	@Override
	public String toString() {
		StringBuilder sb;
		sb = new StringBuilder(16 + (this.size << 3));

		sb.append("{Quadtree : {\nboundingBox:").append(boundingBox).append(",\nmaxPointsPerSubsection:")
				.append(maxPointsPerSubsection).append(",\nsize:").append(size).append(",\nroot_subsections:\n");
		root.toString(sb);
		return sb.toString();
	}

}