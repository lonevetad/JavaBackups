package dataStructures.quadTree.old;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import dataStructures.quadTree.NodeQuadtree.PointDataConsumers;
import dataStructures.quadTree.NodeQuadtree.SubsectionData;
import dataStructures.quadTree.old.NodeQuadtree_V01.SubsectionData_V01;

public class Quadtree_V01<P extends Point2D, D> implements Serializable {
	private static final long serialVersionUID = 30877282782054L;

	public static Quadtree_V01<Point2D.Double, Double> fromXYDoublePairWithZAxes(double[][] dataset, double[] zAxes,
			int maxPointsPerSubsection) {
		return fromPointDoubleWithZAxes((java.awt.geom.Point2D.Double[]) Arrays.stream(dataset)
				.map(a -> new Point2D.Double(a[0], a[1])).toArray(), zAxes, maxPointsPerSubsection);
	}

	public static Quadtree_V01<Point2D.Double, Double> fromPointDoubleWithZAxes(Point2D.Double[] dataset,
			double[] zAxes, int maxPointsPerSubsection) {
		return new Quadtree_V01<Point2D.Double, Double>(i -> {
			return new Point2D.Double[i];
		}, dataset, (Double[]) Arrays.stream(zAxes).mapToObj(Double::valueOf // d -> Double.valueOf(d) //
		).toArray(), maxPointsPerSubsection);
	}

	public static Quadtree_V01<Point, Integer> fromPointIntWithZAxes(Point[] dataset, int[] zAxes,
			int maxPointsPerSubsection) {
		return new Quadtree_V01<Point, Integer>(i -> {
			return new Point[i];
		}, dataset, (Integer[]) Arrays.stream(zAxes).mapToObj(Integer::valueOf // d -> Integer.valueOf(d) //
		).toArray(), maxPointsPerSubsection);
	}

	/**
	 * Maximum amount of point that a subsection can store before splitting.
	 */
	protected final int maxPointsPerSubsection;
	protected int size, maxDepth;
	protected SubsectionData_V01 boundingBox;
	protected NodeQuadtree_V01<P, D> root;
	protected final Function<Integer, P[]> pointsArrayProducer;

	/**
	 * Returns an array holding, respectively, the x and y components of the
	 * bottom-left Point2D of the given points set's bounding box, its height and
	 * its width
	 */
	protected static <P extends Point2D> SubsectionData_V01 extractBoundingBox(P[] initialPoints) {
		double xMax, yMax, temp;
		P p;
		SubsectionData_V01 sd;
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

	public Quadtree_V01(Function<Integer, P[]> pointsArrayProducer, P[] initialPoints, D datas[],
			int maxPointsPerSubsection) {
		this(pointsArrayProducer, initialPoints, datas, maxPointsPerSubsection, extractBoundingBox(initialPoints));
	}

	public Quadtree_V01(Function<Integer, P[]> pointsArrayProducer, P[] initialPoints, D datas[],
			int maxPointsPerSubsection, double xBottomLeft, double yBottomLeft, double height, double width) {
		this(pointsArrayProducer, initialPoints, datas, maxPointsPerSubsection,
				new SubsectionData(xBottomLeft, yBottomLeft, height, width));
	}

	protected Quadtree_V01(Function<Integer, P[]> pointsArrayProducer, P[] initialPoints, D datas[],
			int maxPointsPerSubsection, SubsectionData_V01 boundingBox) {
		Objects.requireNonNull(pointsArrayProducer);
		this.pointsArrayProducer = pointsArrayProducer;
		this.maxPointsPerSubsection = maxPointsPerSubsection;
		this.boundingBox = boundingBox;
		this.size = 0;
		this.root = buildFromDataset(initialPoints, datas);
	}

	//

	protected NodeQuadtree_V01<P, D> newNode(NodeQuadtree_V01<P, D> father, int maxPointsPerSubsection,
			SubsectionData_V01 boundingBox) {
		return new NodeQuadtree_V01<P, D>(this.pointsArrayProducer, father, maxPointsPerSubsection, boundingBox);
	}

	protected NodeQuadtree_V01<P, D> newNode(NodeQuadtree_V01<P, D> father, int maxPointsPerSubsection,
			double xBottomLeft, double yBottomLeft, double width, double height) {
		return newNode(father, maxPointsPerSubsection, new SubsectionData(xBottomLeft, yBottomLeft, width, height));
	}

	// GETTER

	@SuppressWarnings("unchecked")
	protected NodeQuadtree_V01<P, D>[] newSubsectionsArray(int size) {
		return new NodeQuadtree_V01[size];
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
	public SubsectionData_V01 getBoundingBox() {
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
	 * Same method as {@link #getSubsectionFor(P, NodeQuadtree_V01)}, but returns
	 * just the index
	 * 
	 * @param p
	 * @param startingNode
	 * @return
	 */
	protected int getSubsectionIndexFor(P p, NodeQuadtree_V01<P, D> startingNode) {
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
	 * Evolution of {@link #getSubsectionIndexFor(P, NodeQuadtree_V01)}; returns the
	 * whole description of the subsection of the given node that could store the
	 * given point.
	 * 
	 * @param p
	 * @param startingNode
	 * @return
	 */
	protected SubsectionData_V01 getSubsectionFor(P p, NodeQuadtree_V01<P, D> startingNode) {
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

		return new SubsectionData(x, y, hh, ww, index, 0);
	}

	protected NodeQuadtree_V01<P, D> findNodeCouldContain(NodeQuadtree_V01<P, D> startingNode, P p) {
		boolean canIter;
		NodeQuadtree_V01<P, D> tempNode;

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

	protected NodeQuadtree_V01<P, D> buildFromDataset(P[] initialPoints, D datas[]) {
		NodeQuadtree_V01<P, D> root;

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

	@SuppressWarnings("unchecked")
	protected void addNode(NodeQuadtree_V01<P, D> nodeMaybeHolder, P p, D data) {
		NodeQuadtree_V01.PointAdditionStatus statusAddition;

		nodeMaybeHolder = this.findNodeCouldContain(nodeMaybeHolder, p);
		if (nodeMaybeHolder == null) {
			return; // error
		}

		statusAddition = nodeMaybeHolder.canPointBeAdded(p);
		if (statusAddition == NodeQuadtree_V01.PointAdditionStatus.OutOfBounds) {
			return;
		}

		if (statusAddition == NodeQuadtree_V01.PointAdditionStatus.InSubsectionsOnly) {
			SubsectionData_V01 sd;
			NodeQuadtree_V01<P, D> subsections[], s;
			P[] oldPoints;

			subsections = nodeMaybeHolder.subsections;
			oldPoints = nodeMaybeHolder.points;
			if ((nodeMaybeHolder.isLeaf() || subsections == null) && oldPoints != null && oldPoints.length > 0) {
				int oldAmountOfPoints;
				Object[] oldData;

				oldPoints = nodeMaybeHolder.getPoints();
				oldAmountOfPoints = nodeMaybeHolder.getAmountPointsStored();
				oldData = nodeMaybeHolder.getDataPoints();
				nodeMaybeHolder.clearNode();

				subsections = this.newSubsectionsArray(NodeQuadtree_V01.SUBDIVISIONS_AMOUNT);
				nodeMaybeHolder.setSubsections(subsections);

				// re-add the previous nodes
				for (int i = 0; i < oldAmountOfPoints; i++) {
					sd = this.getSubsectionFor(oldPoints[i], nodeMaybeHolder);
					s = subsections[sd.indexSubsection];
					if (s == null) {
						s = subsections[sd.indexSubsection] = this.newNode(nodeMaybeHolder, maxPointsPerSubsection, sd);
					}
					s.addPoint(oldPoints[i], (D) oldData[i]);
				}
				oldPoints = null;
				oldData = null;
			}

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
		final NodeQuadtree_V01<P, D> oldRoot, newRoot;
		final SubsectionData_V01 bb;

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
		oldRoot.forEachPointData((p, data, index, depth) -> addNode(newRoot, p, data));

		addNode(newRoot, point, optionalData);
	}

	/**
	 * @param action
	 */
	protected void forEachSubdivisionMaxDepth(BiConsumer<SubsectionData_V01, Integer> action,
			NodeQuadtree_V01<P, D> currentNode) {
		action.accept(currentNode.boundingBox, maxDepth);
		if (!currentNode.isLeaf()) {
			NodeQuadtree_V01<P, D> ss[], s;
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

	protected int collectPoints(LinkedList<Entry<P[], Object[]>> leaves, NodeQuadtree_V01<P, D> currentNode) {
		int l, partialSize;
		NodeQuadtree_V01<P, D> ss[];

		if (currentNode.isLeaf()) {
			leaves.add(new AbstractMap.SimpleImmutableEntry<>(currentNode.getPoints(), currentNode.getDataPoints()));
			return currentNode.getPoints().length;
		}

		partialSize = 0;
		ss = currentNode.getSubsections();
		l = ss == null ? 0 : ss.length;
		if (l > 0) {
			for (int i = 0; i < l; i++) {
				if (ss[i] != null) {
					partialSize += collectPoints(leaves, ss[i]);
				}
			}
		}
		return partialSize;
	}

	protected int query(final LinkedList<Entry<P[], Object[]>> leaves, NodeQuadtree_V01<P, D> currentNode,
			SubsectionData_V01 areaToSearch) {
		int totalSize;
		if (!currentNode.boundingBox.intersects(areaToSearch)) {
			return 0;
		}

		if (currentNode.isLeaf()) {
			ArrayList<P> alp;
			ArrayList<Object> ald;
			P[] points;
			Object[] ad;

			points = currentNode.points;
			alp = new ArrayList<>(currentNode.amountPointsStored);
			ald = new ArrayList<>(currentNode.amountPointsStored);
			ad = currentNode.dataPoints;

			for (int i = 0, l = currentNode.amountPointsStored; i < l; i++) {
				if (areaToSearch.isInside(points[i])) {
					alp.add(points[i]);
					ald.add(ad[i]);
				}
			}
			if (alp.size() <= 0) {
				return 0;
			}

			if (points.length != alp.size()) {
				points = alp.toArray(this.pointsArrayProducer.apply(alp.size()));
				ad = ald.toArray();
			}

			leaves.add(new AbstractMap.SimpleImmutableEntry<>(points, ad));
			return points.length;
		}

		NodeQuadtree_V01<P, D> s, ss[];
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

	@SuppressWarnings("unchecked")
	protected ArrayList<Entry<P, D>> convertList(LinkedList<Entry<P[], Object[]>> paginatedList, int totalSize) {
		P[] p;
		Object[] d;
		ArrayList<Entry<P, D>> ret;

		ret = new ArrayList<>(totalSize);
		for (Entry<P[], Object[]> e : paginatedList) {
			p = e.getKey();
			d = e.getValue();
			if (p != null) {
				for (int i = 0, l = p.length; i < l; i++) {
					if (p[i] != null) {
						ret.add(new AbstractMap.SimpleImmutableEntry<>(//
								p[i], //
								(D) d[i]));
					}
				}
			}
		}
		return ret;
	}

	// PUBLIC

	public ArrayList<Entry<P, D>> collectAllPointsData() {
		int totalSize;
		LinkedList<Entry<P[], Object[]>> leaves;

		leaves = new LinkedList<>();
		totalSize = this.collectPoints(leaves, root);

		assert totalSize == this.size;
		if (totalSize != this.size) {
			throw new RuntimeException("BUG: upon collecting points, calculated size (" + totalSize
					+ ") is different from the acual one (" + this.size + ")");
		}

		return convertList(leaves, totalSize);
	}

	public ArrayList<Entry<P, D>> query(double xBottomLeft, double yBottomLeft, double width, double height) {
		int totalSize;
		LinkedList<Entry<P[], Object[]>> leaves;

		leaves = new LinkedList<>();
		totalSize = query(leaves, root, new SubsectionData(xBottomLeft, yBottomLeft, width, height));

		return convertList(leaves, totalSize);
	}

	public ArrayList<Entry<P, D>> query(Rectangle r) {
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
	public void forEachSubdivisionMaxDepth(BiConsumer<SubsectionData_V01, Integer> action) {
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