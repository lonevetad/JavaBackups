package dataStructures.isom;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import dataStructures.isom.internal.ISOMWrapperLocated;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager.CoordinatesDeltaForAdjacentNodes;
import dataStructures.isom.matrixBased.ObjLocatedCollectorMultimatrix;
import dataStructures.isom.pathFinders.PathFinderIsomDijkstra;
import geometry.AbstractMultiOISMRectangular;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.implementations.shapes.ShapeRectangle;
import tests.tDataStruct.Test_MultiISOMRetangularMap_V1;
import tools.Comparators;
import tools.MathUtilities;
import tools.Stringable;
import tools.UniqueIDProvider;

/**
 * TODO of 10/06/2020
 * <p>
 * Take inspiration from {@link MatrixInSpaceObjectsManager} to implement
 * {@link InSpaceObjectsManager} and {@link Test_MultiISOMRetangularMap_V1} to
 * implement {@link AbstractMultiOISMRectangular}.
 * <p>
 * To implement {@link NodeIsomProvider#getNodeAt(Point)}, use the
 * {@link Test_MultiISOMRetangularMap_V1
 *
 *
 * #getMapContaining(Point)} code.
 */
public class MultiISOMRetangularMap<Distance extends Number> extends AbstractMultiOISMRectangular<Distance>
		implements InSpaceObjectsManager<Distance> {
	private static final long serialVersionUID = -879653210489L;
	public static final int MAXIMUM_SUBMAPS_EACH_SECTION = 4, MINIMUM_DIMENSION_MAP = 4;
	protected static final UniqueIDProvider ID_PROV_MULTIISOM = UniqueIDProvider.newBasicIDProvider();

	public MultiISOMRetangularMap() { this(MAXIMUM_SUBMAPS_EACH_SECTION); }

	public MultiISOMRetangularMap(int maximumSubmapsEachSection) {
		super();
		if (maximumSubmapsEachSection < 1) {
			log.log("Incorrect number of maximum submaps on each section: " + maximumSubmapsEachSection);
			throw new IllegalArgumentException(
					"Incorrect number of maximum submaps on each section: " + maximumSubmapsEachSection);
		}
		this.ID = ID_PROV_MULTIISOM.getNewID();
		this.maximumSubmapsEachSection = maximumSubmapsEachSection;
		mapsLocatedInSpace = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.LONG_COMPARATOR);
		mapsAsList = mapsLocatedInSpace.toListValue(r -> r.ID);
		misomsHeld = new SetMapped<>(mapsLocatedInSpace.toSetValue(w -> w.ID), w -> {
			return (MatrixInSpaceObjectsManager<Distance>) w.getIsomHeld();
		});
		setObjectsAddedMap(MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR));
		shapeBoundingBox = null;// new ShapeRectangle()
		clear();
		setPathFinder(newPathFinder());
//		this.pathOptimizer = new PathOptimizer<Point>()
	}

	protected long idProg = 0;
//	protected PathFinderIsomAdapter<NodeIsom, D> pathFinder;
	// multi-misom stuffs
	// protected boolean neverBuilt;
	protected int maximumSubmapsEachSection;
	protected int maxDepth, xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height;
	protected NodeQuadtreeMultiISOMRectangular root;
	protected final MapTreeAVL<Long, ISOMWrapperLocated<Distance>> mapsLocatedInSpace;
	protected final Set<MatrixInSpaceObjectsManager<Distance>> misomsHeld;
	protected final List<ISOMWrapperLocated<Distance>> mapsAsList;
	protected ShapeRectangle shapeBoundingBox;
	// from isom
	protected final Long ID;
	protected Map<Long, ObjectLocated> objectsAddedMap;
	protected Set<ObjectLocated> objectsAddedSet;

	//

	// GETTER

	@Override
	public AbstractShape2D getBoundingShape() { return shapeBoundingBox; }

	public Set<MatrixInSpaceObjectsManager<Distance>> getMisomsHeld() { return misomsHeld; }

	// getters for tests

	/**
	 * Returns the maximum amount of {@link MatrixInSpaceObjectsManager} contained
	 * in each quadtree's sub-nodes allowed before the node is splitted into smaller
	 * areas.
	 */
	public int getMaximumSubmapsEachSection() { return maximumSubmapsEachSection; }

	public NodeQuadtreeMultiISOMRectangular getRoot() { return root; }

	public int getMaxDepth() { return maxDepth; }

	public int getxLeftTop() { return xLeftTop; }

	public int getyLeftTop() { return yLeftTop; }

	public int getxRightBottom() { return xRightBottom; }

	public int getyRightBottom() { return yRightBottom; }

	@Override
	public int getWidth() { return width; }

	@Override
	public int getHeight() { return height; }

	public Map<Long, ISOMWrapperLocated<Distance>> getMapsLocatedInSpace() { return mapsLocatedInSpace; }

	public ShapeRectangle getShapeRect() { return shapeBoundingBox; }

	//

	// SETTER

	@Override
	public boolean setID(Long newID) { return false; }

	@Override
	public void setShape(AbstractShape2D shape) { throw new UnsupportedOperationException("Shape is self-defined"); }

	/** Sets the map holding all objects in this space. */
	protected void setObjectsAddedMap(Map<Long, ObjectLocated> objectsAdded) {
		this.objectsAddedMap = objectsAdded;
		if (objectsAdded == null) // here and below, update the set
			this.objectsAddedSet = null;
		else {
			if (objectsAdded instanceof MapTreeAVL<?, ?>)
				this.objectsAddedSet = ((MapTreeAVL<Long, ObjectLocated>) objectsAdded).toSetValue(ol -> ol.getID());
			else
				this.objectsAddedSet = new SetMapped<>(objectsAdded.entrySet(), e -> e.getValue());
		}
	}

	//

	// TODO OTHER METHODS

	//

	/**
	 * Calls {@link #toCachingMatrixes(int)} passing the
	 * {@link #getMaximumSubmapsEachSection()}.
	 */
	public MultiISOMRetangularCaching<Distance> toCachingMatrixes() {
		return toCachingMatrixes(maximumSubmapsEachSection);
	}

	/**
	 * Returns an instance of {@link MultiISOMRetangularCaching}, see its
	 * documentation for further informations.
	 */
	public MultiISOMRetangularCaching<Distance> toCachingMatrixes(int maximumSubmapsEachSection) {
		return new MultiISOMRetangularCaching<>(maximumSubmapsEachSection);
	}

	/**
	 * See {@link #getNodeAt(int, int)}.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public NodeIsom<Distance> getNodeAt(Point location) { return getNodeAt(location.x, location.y); }

	/**
	 * Get the {@link NodeIsom} at the specific coordinates.<br>
	 * Those coordinates are to be meant <b>absolute</b>, <i>not</i> relatively to
	 * the containing map (a map is a {@link MatrixInSpaceObjectsManager}). <br>
	 * For instance, passing the point <code>{x:5, y:10}</code> as parameter (and
	 * assuming that this point is contained by the map located at
	 * <code>{x:1, y:3}</code>) will result in the {@link NodeIsom} located at
	 * <code>{x:5, y:10} - {x:1, y:3} = {x:4, y:7}</code> so the {@link NodeIsom}
	 * located at <code>{x:4, y:7}</code> on that map will be returned.
	 */
	@Override
	public NodeIsom<Distance> getNodeAt(int x, int y) {
		ISOMWrapperLocated<Distance> ml;
		ml = getMapLocatedContaining(x, y);
		if (ml == null)
			return null;
//		return ml.misom.getNodeAt(x - ml.x, y - ml.y);
		return ml.getNodeAt(x, y); // offset is moved here
	}

	protected PathFinderIsom<Distance> newPathFinder() { return new PathFinderIsomDijkstra<>(this); }

	@Override
	public void forEachNode(BiConsumer<NodeIsom<Distance>, Point> action) {
		this.mapsLocatedInSpace.forEach((i, mw) -> {
			mw.getIsomHeld().forEachNode((n, p) -> {
				// consider the offset
				p.x += mw.getx();
				p.y += mw.gety();
				action.accept(n, p);
			});
		});
	}

	@Override
	public Set<ObjectLocated> getAllObjectLocated() { return this.objectsAddedSet; }

	@Override
	public ObjectLocated getObjectLocated(Long ID) { return this.objectsAddedMap.get(ID); }

	@Override
	public void forEachAdjacents(NodeIsom<Distance> node,
			BiConsumer<NodeIsom<Distance>, Distance> adjacentDistanceConsumer) {
//		MatrixInSpaceObjectsManager<Distance> misom;
		ISOMWrapperLocated<Distance> mlis;
		Point p, absoluteNodeLocation;
		NodeIsom<Distance> adj;
		p = new Point();
		absoluteNodeLocation = node.getLocationAbsolute();
		for (CoordinatesDeltaForAdjacentNodes c : MatrixInSpaceObjectsManager.VALUES_CoordinatesDeltaForAdjacentNodes) {
			p.x = absoluteNodeLocation.x + c.dx;
			p.y = absoluteNodeLocation.y + c.dy;
			adj = null;
//			misom = getMISOMContaining(p);
			mlis = getMapLocatedContaining(p);
//			if (misom != null) {
			if (mlis != null) {
				adj = this.getNodeAt(p); // changed to "this" to let considering the misom's offset
				if (adj != null && mlis.getIsomHeld().containsAt(p))
					adjacentDistanceConsumer.accept(
							// misom
							adj, mlis.getIsomHeld().getWeightManager().fromDouble(c.weight));
			}
		}
	}

	/**
	 * Apply an action to each stored map and the point (top-left corner) which it's
	 * located in.
	 */
	public void forEachMap(BiConsumer<MatrixInSpaceObjectsManager<Distance>, Point> action) {
		final Point offset;
		offset = new Point();
		this.mapsLocatedInSpace.forEach((id, wrapper) -> {
			InSpaceObjectsManager<Distance> isom;
			isom = wrapper.getIsomHeld();
			if (!(isom instanceof MatrixInSpaceObjectsManager<?>)) { return; }
			offset.x = wrapper.getx();
			offset.y = wrapper.gety();
			action.accept((MatrixInSpaceObjectsManager<Distance>) isom, offset);
		});
	}

	@Override
	public ObjLocatedCollectorIsom<Distance> newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) {
		return new ObjLocatedCollectorMultimatrix<Distance>(this, objectFilter);
	}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	public boolean add(ObjectLocated o) {
		MatrixInSpaceObjectsManager<Distance> m;
		if (o == null)
			return false;
		m = getMISOMContaining(o.getLocation());
		if (m == null)
			return false;
		m.add(o);
		return true;
	}

	@Override
	public boolean contains(ObjectLocated o) {
		MatrixInSpaceObjectsManager<Distance> m;
		if (o == null)
			return false;
		m = getMISOMContaining(o.getLocation());
		if (m == null)
			return false;
		return m.contains(o);
	}

	@Override
	public boolean remove(ObjectLocated o) {
		MatrixInSpaceObjectsManager<Distance> m;
		if (o == null)
			return false;
		m = getMISOMContaining(o.getLocation());
		if (m == null)
			return false;
		return m.remove(o);
	}

	//

	// TODO multi-isom's methods

	//

	public void clear() { removeAllMaps(); }

	public void removeAllMaps() {
		mapsLocatedInSpace.clear();
		root = null;
		clearDimensionCache();
	}

	protected void clearDimensionCache() {
		// neverBuilt = true;
		shapeBoundingBox = null;
		maxDepth = 0;
		xLeftTop = yLeftTop = xRightBottom = yRightBottom = width = height = 0;
//		this.objectsAddedMap.clear();
	}

	/**
	 * See {@link #getMISOMContaining(int, int)}.
	 */
	public MatrixInSpaceObjectsManager<Distance> getMISOMContaining(Point p) { return getMISOMContaining(p.x, p.y); }

	/**
	 * See {@link #getMapLocatedContaining(int, int)}, but returning the
	 * {@link MatrixInSpaceObjectsManager} held.
	 */
	public MatrixInSpaceObjectsManager<Distance> getMISOMContaining(int x, int y) {
		ISOMWrapperLocated<Distance> mw;
		InSpaceObjectsManager<Distance> isom;
		mw = getMapLocatedContaining(x, y);
		if (mw == null) { return null; }
		isom = mw.getIsomHeld();
		if (!(isom instanceof MatrixInSpaceObjectsManager<?>)) { return null; }
		return (MatrixInSpaceObjectsManager<Distance>) isom;
	}

	/** See {@link #getMapLocatedContaining(int, int)}. */
	public ISOMWrapperLocated<Distance> getMapLocatedContaining(Point p) {
		return getMapLocatedContaining(p.x, p.y);
	}

	/**
	 * Returns a {@link ISOMWrapperLocated} that can contains the given point (those
	 * coordinates are absolute, not relative in any way, nor this class's space
	 * represented).
	 */
	public ISOMWrapperLocated<Distance> getMapLocatedContaining(int x, int y) {
		NodeQuadtreeMultiISOMRectangular n, prev;
		List<ISOMWrapperLocated<Distance>> submaps;
		n = prev = getRoot();
		if (n == null)
			return null;
		// traverse the tree
		while (n != null && (!n.isLeaf())) {
			prev = n;
			if (x >= n.xMiddle) { // east
				n = (y >= n.yMiddle) ? n.sse : n.sne;
			} else {// west
				n = (y >= n.yMiddle) ? n.ssw : n.snw;
			}
		}
		// get the collection of submaps
		submaps = (n != null) ? n.submaps : prev.submaps;
		if (submaps == null)
			return null;
		// if any holds that point, then return it
		for (ISOMWrapperLocated<Distance> r : submaps) {
			if (r.getIsomHeld().containsAt(x, y)) // MathUtilities.isInside(r, p))//
				return r;
		}
		return null; // Error 404
	}

	/**
	 * Add a map (a {@link MatrixInSpaceObjectsManager}) which is located, into the
	 * space, at the given point.<br>
	 * REMEMBER: the given coordinates are the top-left corner. <br>
	 * This point is the map's offset because the map's class's indexes (i.e. those
	 * passed to {@link MatrixInSpaceObjectsManager#getNodeAt(Point)})) are relative
	 * to its origin.
	 * <p>
	 * See {@link #getNodeAt(int, int)} to more informations and examples about the
	 * meaning of <i>offset</i> and <i>coordinates</i> both relative and absolute.
	 */
	public ISOMWrapperLocated<Distance> addMap(MatrixInSpaceObjectsManager<Distance> map, int x, int y) {
		return addMap(map, x, y, 0.0);
	}

	/**
	 * See {@link #addMap(MatrixInSpaceObjectsManager, int, int)}, but also
	 * providing a rotation to the given map.
	 */
	public ISOMWrapperLocated<Distance> addMap(MatrixInSpaceObjectsManager<Distance> map, int x, int y,
			double angleRotationDegrees) {
		int c;
		ISOMWrapperLocated<Distance> r;
		if (map == null || map.getWidth() < 1 || map.getWidth() < 1)
			return null;
		map.setTopLeftCorner(x, y);
//		r = new ISOMWrapperLocated<Distance>(this, map, x, y, angleRotationDegrees);
		r = new ISOMWrapperLocated<Distance>(map, x, y, angleRotationDegrees);
		r.setLocation(x, y);
		c = updateBoundingBox(r);
		if (c >= 0) {
			mapsLocatedInSpace.put(r.ID, r);
			if (c > 0)
				rebuild();
			else
				addNotRebuilding(r);
		}
		return r;
	}

	/** See {@link #addMap(MatrixInSpaceObjectsManager, int, int)}. */
	public ISOMWrapperLocated<Distance> addMap(MatrixInSpaceObjectsManager<Distance> map, Point locationLeftTop) {
		return addMap(map, locationLeftTop, 0.0);
	}

	public ISOMWrapperLocated<Distance> addMap(MatrixInSpaceObjectsManager<Distance> map, Point locationLeftTop,
			double angleRotationDegrees) {
		if (map == null || map.getWidth() < 1 || map.getWidth() < 1)
			return null;
		return addMap(map, locationLeftTop.x, locationLeftTop.y, angleRotationDegrees);
	}

//	public void addMaps(Collection<MatrixInSpaceObjectsManager<Distance>> mapsList) {
//		int[] cc = { -1 };
//		mapsList.forEach(r -> {
//			int c;
//			if (r != null) {
//				c = updateBoundingBox(r);
//				if (c >= 0) {
//					if (cc[0] < c)
//						cc[0] = c;
//					mapsLocatedInSpace.put(r.ID, r);
//				}
//			}
//		});
//		if (cc[0] > 0)
//			rebuild();
//		else
//			mapsList.forEach(this::addNotRebuilding);
//	}

	public void removeMap(ISOMWrapperLocated<Distance> r) {
		if (mapsLocatedInSpace.containsKey(r.ID)) {
			mapsLocatedInSpace.remove(r.ID);
			recalculateBoundingBox();
			/*
			 * TODO should perform a more fine action like adding maps
			 * ("if the map change the bounding box or not")
			 */
			rebuild();
		}
	}

	public void removeMapLocatedIn(MatrixInSpaceObjectsManager<Distance> map, Point location) {
		this.removeMapLocatedIn(map, location.x, location.y);
	}

	public void removeMapLocatedIn(MatrixInSpaceObjectsManager<Distance> map, int x, int y) {
		ISOMWrapperLocated<Distance> r;
		r = getMapLocatedContaining(x, y);
		if (r != null) { removeMap(r); }
	}

	public void removeMaps(Collection<ISOMWrapperLocated<Distance>> mapsList) {
		boolean[] cc = { false };
		mapsList.forEach(r -> {
			if (mapsLocatedInSpace.containsKey(r.ID)) {
				cc[0] = true;
				mapsLocatedInSpace.remove(r.ID);
			}
		});
		if (cc[0]) {
			recalculateBoundingBox();
			rebuild();
		}
	}

	protected void resetShape(boolean mustReallocate) {
		if (mustReallocate)
			shapeBoundingBox = new ShapeRectangle(0.0, 0, 0, true, 0, 0);
		shapeBoundingBox.setWidth(width);
		shapeBoundingBox.setHeight(height);
		shapeBoundingBox.setLeftTopCorner(getxLeftTop(), getyLeftTop());
	}

	protected void recalculateBoundingBox() {
		clearDimensionCache();
		mapsAsList.forEach(this::updateBoundingBox);
	}

	/**
	 * Update the bounding box. Returns a negative value if the shape are illegal
	 * (null or negative height or width), 0 if the bounding box is not changed and
	 * a positive value if the bounding box has successfully being updated.
	 */
	protected int updateBoundingBox(ISOMWrapperLocated<Distance> r) {
		boolean changed;
		int temp;
		if (r.width < 1 || r.height < 1) { return -1; }
		if (shapeBoundingBox == null) {
			xLeftTop = r.x;
			yLeftTop = r.y;
			width = r.width;
			height = r.height;
			xRightBottom = (r.x + r.width) - 1;
			yRightBottom = (r.y + r.height) - 1;
			resetShape(true);
			return 1;
		}
		changed = false;
		if (r.x < getxLeftTop()) {
			changed = true;
			xLeftTop = r.x;
		}
		if (r.y < getyLeftTop()) {
			changed = true;
			yLeftTop = r.y;
		}
		temp = r.x + r.width;
		if (temp > xRightBottom) {
			changed = true;
			xRightBottom = temp;
		}
		temp = r.y + r.height;
		if (temp > yRightBottom) {
			changed = true;
			yRightBottom = temp;
		}
		this.width = xRightBottom - getxLeftTop();
		this.height = yRightBottom - getyLeftTop();
		if (changed || getRoot() == null) {
			resetShape(false);
			return 1;
		}
		return 0;
	}

	public void rebuild() {
		this.maxDepth = 0;
		this.root = rebuild(null, mapsAsList, //
				getxLeftTop(), getyLeftTop(), xRightBottom, yRightBottom, width, height//
				, getxLeftTop() + (width >> 1)//
				, getyLeftTop() + (height >> 1)); // clear all
	}

	protected NodeQuadtreeMultiISOMRectangular rebuild(NodeQuadtreeMultiISOMRectangular father,
			List<ISOMWrapperLocated<Distance>> submaps, //
			int xLeftTop, int yLeftTop, int xRightBottom, int yRightBottom, int width, int height, //
			int xMiddle, int yMiddle) {
		final int mxw, mxe, myn, mys, widthWest, widthEst, heightNorth, heightSouth, yMiddlemmmm, xMiddlemmmm;
		NodeQuadtreeMultiISOMRectangular n;
		List<ISOMWrapperLocated<Distance>> snw, sne, ssw, sse;
		n = new NodeQuadtreeMultiISOMRectangular(father);
		if (this.getMaxDepth() < n.depth) { this.maxDepth = n.depth; }
		n.x = xLeftTop;
		n.y = yLeftTop;
		n.width = width;
		n.height = height;
		n.xMiddle = xMiddle;
		n.yMiddle = yMiddle;
		if (submaps.size() <= maximumSubmapsEachSection //
				|| width <= MINIMUM_DIMENSION_MAP || height <= MINIMUM_DIMENSION_MAP) {
			n.submaps = submaps;
			return n;
		}
		snw = new ArrayList<>(maximumSubmapsEachSection);
		sne = new ArrayList<>(maximumSubmapsEachSection);
		ssw = new ArrayList<>(maximumSubmapsEachSection);
		sse = new ArrayList<>(maximumSubmapsEachSection);
		yMiddlemmmm = yMiddle;
		xMiddlemmmm = xMiddle;
		mxw = xLeftTop + ((widthWest = (xMiddle - xLeftTop)) >> 1);
		myn = yLeftTop + ((heightNorth = (yMiddle - yLeftTop)) >> 1);
		mxe = ++xMiddle + ((widthEst = 1 + (xRightBottom - xMiddle)) >> 1);
		mys = ++yMiddle + ((heightSouth = 1 + (yRightBottom - yMiddle)) >> 1);
		xMiddle = xMiddlemmmm;
		yMiddle = yMiddlemmmm;
		submaps.forEach(r -> {
			if (MathUtilities.intersects(xLeftTop, yLeftTop, widthWest, heightNorth, r.x, r.y, r.width, r.height)) {
				snw.add(r);
			}
			if (MathUtilities.intersects(xMiddlemmmm + 1, yLeftTop, widthEst, heightNorth, r.x, r.y, r.width,
					r.height)) {
				sne.add(r);
			}
			if (MathUtilities.intersects(xMiddlemmmm + 1, yMiddlemmmm + 1, widthEst, heightSouth, r.x, r.y, r.width,
					r.height)) {
				sse.add(r);
			}
			if (MathUtilities.intersects(xLeftTop, yMiddlemmmm + 1, widthWest, heightSouth, r.x, r.y, r.width,
					r.height)) {
				ssw.add(r);
			}
		});
		if (snw.size() > 0)
			n.snw = rebuild(n, snw, //
					xLeftTop, yLeftTop, xMiddlemmmm, yMiddlemmmm, // corner points
					widthWest, heightNorth, //
					mxw, myn); // middle point
		if (sne.size() > 0)
			n.sne = rebuild(n, sne, //
					(xMiddlemmmm + 1), yLeftTop, xRightBottom, yMiddlemmmm, // corner points
					widthEst, heightNorth, // dimensions
					mxe, myn); // middle point
		if (ssw.size() > 0)
			n.ssw = rebuild(n, ssw, //
					xLeftTop, yMiddlemmmm + 1, xMiddlemmmm, yRightBottom, // corner points
					widthWest, heightSouth, // dimensions
					mxw, mys); // middle point
		if (sse.size() > 0)
			n.sse = rebuild(n, sse, //
					xMiddlemmmm + 1, yMiddlemmmm + 1, xRightBottom, yRightBottom, // corner points
					widthEst, heightSouth, // dimensions
					mxe, mys); // middle point
		return n;
	}

	/**
	 */
	protected void addNotRebuilding(ISOMWrapperLocated<Distance> map) {
		if (getRoot() == null)
			rebuild();
		else
			root = addNotRebuilding(map, getRoot());
	}

	/**
	 * Create a new {@link NodeQuadtreeMultiISOMRectangular} based on a given map
	 * (to be held) and a father node.
	 */
	protected NodeQuadtreeMultiISOMRectangular newNodeWith(ISOMWrapperLocated<Distance> map,
			NodeQuadtreeMultiISOMRectangular fatherNode) {
		NodeQuadtreeMultiISOMRectangular newNode;
		newNode = new NodeQuadtreeMultiISOMRectangular(fatherNode);
		if (this.getMaxDepth() < newNode.depth) { this.maxDepth = newNode.depth; }
		newNode.submaps = new ArrayList<>(maximumSubmapsEachSection);
		newNode.submaps.add(map);
		return newNode;
	}

	// the recursive part
	// returns the node given or a newly created one
	protected NodeQuadtreeMultiISOMRectangular addNotRebuilding(ISOMWrapperLocated<Distance> map,
			NodeQuadtreeMultiISOMRectangular currentNode) {
		if (currentNode.isLeaf()) {
			if (currentNode.submaps.size() < this.maximumSubmapsEachSection) {
				// just add and nothing more
				currentNode.submaps.add(map);
			} else {
				// else, build ricoursively
//				currentNode.submaps.add(map);
				List<ISOMWrapperLocated<Distance>> submaps;
				submaps = currentNode.submaps;
				currentNode.submaps = null;
				submaps.add(map);
				currentNode = rebuild(currentNode.father, /* currentNode. */submaps, currentNode.x, currentNode.y,
						currentNode.x + currentNode.width, currentNode.y + currentNode.width, currentNode.width,
						currentNode.height, currentNode.xMiddle, currentNode.yMiddle);
				if (this.getMaxDepth() < currentNode.depth) { this.maxDepth = currentNode.depth; }
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
//						currentNode.xMiddle, currentNode.yMiddle);
//			} else		{
		} else {
			int hw, hh, x_w_1, y_h_1;
			hw = currentNode.width >> 1;
			hh = currentNode.height >> 1;
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
							MathUtilities.intersects(x_w_1, currentNode.y, currentNode.width - hw, hh, map.x, map.y,
									map.width, map.height))) {
				currentNode.sne = (currentNode.sne == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.sne);
			}
			if ((currentNode.ssw != null && currentNode.ssw.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.ssw == null && //
							MathUtilities.intersects(currentNode.x, y_h_1, hw, currentNode.height - hh, map.x, map.y,
									map.width, map.height))) {
				currentNode.ssw = (currentNode.ssw == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.ssw);
			}
			if ((currentNode.sse != null && currentNode.sse.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.sse == null && //
							MathUtilities.intersects(x_w_1, y_h_1, currentNode.width - hw, currentNode.height - hh,
									map.x, map.y, map.width, map.height))) {
				currentNode.sse = (currentNode.sse == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.sse);
			}
		}
		return currentNode;
	}

	//

	//

	// TODO CLASSES

	//

	//

	/**
	 * Wrapper of a {@link MatrixInSpaceObjectsManager} that specify where that
	 * matrix-map is located in space: this class it's a {@link Rectangle} in
	 * fact.<br>
	 * Mainly used for internal stuffs like:
	 * <ul>
	 * <li>{@link MultiISOMRetangularMap#addMap(MatrixInSpaceObjectsManager, Point)}</li>
	 * <li>{@link NodeIsomProviderCachingMISOM}</li>
	 * <li>Produced {@link NodeIsomProviderCachingMISOM#getMisomAt(Point)}</li>
	 * </ul>
	 * <p>
	 * NOTE: as like as it's said in
	 * {@link MultiISOMRetangularMap#getNodeAt(int, int)}, each matrix's coordinates
	 * are relative to it, so a point like <code>{x:5, y:10}</code> of a map located
	 * at <code>{x:1, y:3}</code> is translated (using a notation abuse) to
	 * <code>{x:5, y:10} - {x:1, y:3} = {x:4, y:7}</code>, so the {@link NodeIsom}
	 * located at <code>{x:4, y:7}</code> will be taken into account.
	 */
//	public static class MatrixISOMLocatedInSpace<Dd extends Number> extends Rectangle implements ObjectLocated {
//		private static final long serialVersionUID = 560804524L;
//		public static final Function<MatrixISOMLocatedInSpace<?>, Long> ID_EXTRACTOR = w -> w.ID;
//		public static final Function<MatrixISOMLocatedInSpace<?>, MatrixInSpaceObjectsManager<?>> MISOM_EXTRACTOR = w -> w.misom;
//
//		/** See calls the constructor considering */
//		public MatrixISOMLocatedInSpace(MultiISOMRetangularMap<Dd> multi, MatrixInSpaceObjectsManager<Dd> misom, int x,
//				int y) {
//			this(multi, misom, x, y, 0.0);
//		}
//
//		public MatrixISOMLocatedInSpace(MultiISOMRetangularMap<Dd> multi, MatrixInSpaceObjectsManager<Dd> misom, int x,
//				int y, double angleRotation) {
//			super(x, y, misom.getWidth(), misom.getHeight());
//			this.multi = multi;
//			this.ID = this.multi.idProg++;
//			this.angleRotationDegrees = angleRotation;
//			this.misom = misom;
//		}
//
//		/**
//		 * See {@link MatrixISOMLocatedInSpace} for informations about the given
//		 * {@link Point}.
//		 */
//		public MatrixISOMLocatedInSpace(MultiISOMRetangularMap<Dd> multi, MatrixInSpaceObjectsManager<Dd> misom,
//				Point p) {
//			this(multi, misom, p.x, p.y);
//		}
//
//		/** In Degreed */
//		protected double angleRotationDegrees, sinCache, cosCache, sinInverseCache, cosInverseCache;
//		public final Long ID;
//		protected final MultiISOMRetangularMap<Dd> multi;
//		public final MatrixInSpaceObjectsManager<Dd> misom;
//
//		//
//
//		@Override
//		public Long getID() { return ID; }
//
//		public MatrixInSpaceObjectsManager<Dd> getMatrix() { return misom; }
//
//		public MatrixInSpaceObjectsManager<Dd> getISOM() { return misom; }
//
//		public double getAngleRotationDegrees() { return angleRotationDegrees; }
//
//		//
//
//		@Override
//		public boolean setID(Long newID) { return false; }
//
//		public void setAngleRotationDegrees(double angleRotationDegrees) {
//			double rad;
//			this.angleRotationDegrees = angleRotationDegrees % 360.0;
//			if (this.angleRotationDegrees < 0.0)
//				this.angleRotationDegrees += 360.0;
//			rad = Math.toRadians(this.angleRotationDegrees);
//			sinCache = Math.sin(rad);
//			cosCache = Math.cos(rad);
//			sinInverseCache = Math.sin(-rad);
//			cosInverseCache = Math.cos(-rad);
//		}
//
//		//
//
//		/** Absolute coordinates. */
//		public NodeIsom<Dd> getNodeAt(int x, int y) {
////			boolean isNinety;
//			if (angleRotationDegrees == 0.0) {
//				// consider the offset
//				return misom.getNodeAt(x - this.x, y - this.y);
//			} else if (angleRotationDegrees == 180.0) {
//				int xx, yy;
//				xx = ((this.x + width) - x);
//				yy = ((this.y + height) - y);
//				if ((width & 0x1) == 0) // even
//					xx++; // because of "rounding"
//				if ((height & 0x1) == 0) // even
//					yy++; // because of "rounding"
//				// consider the offset
//				return misom.getNodeAt(xx, yy);
//			}
////				else if ((isNinety = angleRotationDegrees == 90.0) || (angleRotationDegrees == 270.0)) {
////				int xx, yy; // centre
////				xx = this.x + (width >> 1);
//////				if((width&0x1)==0)xx--;
//////				if((height&0x1)==0)yy--;
////				yy = this.y + (height >> 1);
////
////				// top left corner, but rotated
////				// consider the offset
////				// it's rotated, so the coordinates are swapped
//////				return misom.getNodeAt(y-yy,x-xx);
////				throw new UnsupportedOperationException("Not still done");
////			} else if (angleRotationDegrees == 270.0) {
////				int xx, yy; // top left corner, but rotated
////				xx = this.x + (width >> 1);
////				yy = this.y + (height >> 1);
////				// consider the offset
////				// TODO rotate
////				// it's rotated, so the coordinates are swapped
//////				return misom.getNodeAt(y-yy,x-xx);
////				throw new UnsupportedOperationException("Not still done");
////			}
////			otherwise ..if (this.angleRotationDegrees != 0.0) {
//			else {
//				Point location;
//				// NOTE: actions in "makeRelativeAndRotate" are make here just to make them fast
//				location = this.misom.getLocation();
//				// make coordinates relative to the centre
////				x += location.x - (this.misom.getWidth() >> 1);
////				y += location.y - (this.misom.getHeight() >> 1);
//				x -= location.x;
//				y -= location.y;
////			s = Math.sin(a); // a is in radians
////			c = Math.cos(a);
////			rotationMatrix = [
////				[c, -s],
////				[s, c]
////			];
//				// [x,y] = dotProduct( [x,y], rotationMatrix);
////			xtemp = x * cosInverseCache;
////			ytemp = y * sinInverseCache;
//				x = (int) (x * cosInverseCache + y * sinInverseCache);
//				y = (int) ((y * cosInverseCache) - (x * sinInverseCache));
//			}
//
//			return misom.getNodeAt(x, y);
//		}
//
//		/**
//		 * Change the provided {@link Point}'s internal values equal to the relative
//		 * top-left corner of the {@link InSpaceObjectsManager} held by this class.
//		 */
//		public Point makePointRelativeToTopLeftCorner(Point p) {
//			PointInt location;
//			location = this.misom.getTopLetCorner();
//			p.x -= location.getX();
//			p.y -= location.getY();
//			return p;
//		}
//
//		/**
//		 * Calls {@link #makePointRelativeToTopLeftCorner(Point)} providing a newly
//		 * created {@link Point}. The given point is to be meant as <b>absolute</b>,
//		 * i.e. NOT relative to this {@link InSpaceObjectsManager}.
//		 */
//		public Point makePointRelativeToTopLeftCorner(int x, int y) {
//			return makePointRelativeToTopLeftCorner(new Point(x, y));
//		}
//
//		public Point makePointAbsoluteToTopLeftCorner(Point p) {
//			PointInt location;
//			location = this.misom.getTopLetCorner();
//			p.x += location.getX();
//			p.y += location.getY();
//			return p;
//		}
//
//		/** See {@link #makePointRelativeToTopLeftCorner(int, int)}. */
//		public Point makePointRelativeToCenter(Point p) {
//			Point location;
////			location = this.misom.getTopLetCorner();
////			p.x += (this.misom.getWidth() >> 1) - location.x;
////			p.y += (this.misom.getHeight() >> 1) - location.y;
////			return p;
//			location = this.misom.getLocation();
//			p.x -= location.x;
//			p.y -= location.y;
//			return p;
//		}
//
//		public Point makePointAbsoluteToCenter(int x, int y) { return makePointAbsoluteToCenter(new Point(x, y)); }
//
//		public Point makePointAbsoluteToCenter(Point p) {
//			Point location;
////			location = this.misom.getTopLetCorner();
////			p.x += location.x - (this.misom.getWidth() >> 1);
////			p.y += location.y - (this.misom.getHeight() >> 1);
//			location = this.misom.getLocation();
//			p.x += location.x;
//			p.y += location.y;
//			return p;
//		}
//
//		/**
//		 * The given {@link InSpaceObjectsManager} has a rotation in degrees (i.e.:
//		 * {@link #getAngleRotationDegrees()}), so the <b>relative</b> given point is
//		 * rotated relatively to the center so that it could be used as a
//		 * <i>2D-index</i>.
//		 */
//		public Point applyIsomsRotation(Point p) {
//			int x, y;
//			x = p.x;
//			y = p.y;
//			p.x = (int) (x * cosInverseCache + y * sinInverseCache);
//			p.y = (int) ((y * cosInverseCache) - (x * sinInverseCache));
//			return p;
//		}
//
//		/**
//		 * Calls {@link #makePointRelativeToTopLeftCorner(Point)} providing a newly
//		 * created {@link Point}.
//		 */
//		public Point applyIsomsRotation(int x, int y) { return applyIsomsRotation(new Point(x, y)); }
//
//		/**
//		 * Apply both {@link #makePointRelativeToTopLeftCorner(Point)} and then
//		 * {@link #applyIsomsRotation(Point)} to the given <b>absolute</b> point.
//		 */
//		public Point makeRelativeToCenterAndRotate(Point p) {
//			return applyIsomsRotation(makePointRelativeToCenter(p));
//		}
//
//		public Point makeRelativeToCenterAndRotate(int x, int y) {
//			return makeRelativeToCenterAndRotate(new Point(x, y));
//		}
//
//		public boolean add(ObjectLocated o) {
//			boolean c;
//			int xo, yo;// , x, y;
//			Point oldLocation;
//			if (o == null)
//				return false;
//			oldLocation = o.getLocation();
//			xo = oldLocation.x;
//			yo = oldLocation.y;
////			o.setLocation(xo - misomLocation.x, yo - misomLocation.y);
//			makeRelativeToCenterAndRotate(oldLocation);
//			c = this.multi.add(o);
//			o.setLocation(xo, yo);
//			return c;
//		}
//
//		public boolean contains(ObjectLocated o) {
//			boolean c;
//			int xo, yo;// , x, y;
//			Point oldLocation;
//			if (o == null)
//				return false;
//			oldLocation = o.getLocation();
//			xo = oldLocation.x;
//			yo = oldLocation.y;
////			o.setLocation(xo - misomLocation.x, yo - misomLocation.y);
//			makeRelativeToCenterAndRotate(oldLocation);
//			c = this.multi.contains(o);
//			o.setLocation(xo, yo);
//			return c;
//		}
//
//		public boolean remove(ObjectLocated o) {
//			boolean c;
//			int xo, yo;// , x, y;
//			Point oldLocation;
////			misomLocation = this.misom.getLocation();
//			if (o == null)
//				return false;
//			oldLocation = o.getLocation();
//			xo = oldLocation.x;
//			yo = oldLocation.y;
////			o.setLocation(xo - misomLocation.x, yo - misomLocation.y);
//			makeRelativeToCenterAndRotate(oldLocation);
//			c = this.multi.remove(o);
//			o.setLocation(xo, yo);
//			return c;
//		}
//
//		public void forEachNode(BiConsumer<NodeIsom<Dd>, Point> action) {
//			int hw, hh;
//			Point isomLocationCentre;
//			isomLocationCentre = this.misom.getLocation();
//			hw = (this.misom.getWidth() >> 1);
//			hh = (this.misom.getHeight() >> 1);
//			this.misom.forEachNode((n, p) -> {
//				int xRotated, yRotated;
//				// p is relative right now
//				// must be made absolute and rotated
//
//				// 1) make it from relative to top-left to relative to center
//				p.x -= hw;
//				p.y -= hh;
//				// 2) apply the rotation
//				xRotated = (int) (p.x * cosCache + p.y * sinCache);
//				yRotated = (int) ((p.y * cosCache) - (p.x * sinCache));
//				p.x = xRotated;
//				p.y = yRotated;
//				// 3) make it back relative to top-left corner and 4) absolute
//				p.x += hw + isomLocationCentre.x;
//				p.y += hh + isomLocationCentre.y;
//				// 5) DONE
//				action.accept(n, p);
//			});
//		}
//	}

	/**
	 * Node subsection of this quad-tree like class.<br>
	 * Public just for test stuffs
	 */
	public class NodeQuadtreeMultiISOMRectangular implements Stringable {
		private static final long serialVersionUID = 1L;
		protected int x, y, width, height, xMiddle, yMiddle, depth;
		List<ISOMWrapperLocated<Distance>> submaps;
		NodeQuadtreeMultiISOMRectangular father, snw, sne, ssw, sse;

		NodeQuadtreeMultiISOMRectangular(NodeQuadtreeMultiISOMRectangular father) {
			this.father = father;
			submaps = null;
			depth = (father == null) ? 1 : (father.depth + 1);
		}

		public int getX() { return this.x; }

		public int getY() { return this.y; }

		public int getWidth() { return this.width; }

		public int getHeight() { return this.height; }

		public int getXMiddle() { return this.xMiddle; }

		public int getYMiddle() { return this.yMiddle; }

		public int getDepth() { return this.depth; }

		public List<ISOMWrapperLocated<Distance>> getSubmaps() { return submaps; }

		public boolean isLeaf() { return submaps != null; }

//		public NodeMultiISOMRectangular getSnw() { return snw; }
//		public NodeMultiISOMRectangular getSne() { return sne; }
//		public NodeMultiISOMRectangular getSsw() { return ssw; }
//		public NodeMultiISOMRectangular getSse() { return sse; }

		/** Beware of <code>null</code>s! */
		public void forEachSubsection(Consumer<NodeQuadtreeMultiISOMRectangular> action) {
			action.accept(snw);
			action.accept(sne);
			action.accept(ssw);
			action.accept(sse);
		}

		public void recalculateEachSubsection(
				Function<NodeQuadtreeMultiISOMRectangular, NodeQuadtreeMultiISOMRectangular> action) {
			snw = action.apply(snw);
			sne = action.apply(sne);
			ssw = action.apply(ssw);
			sse = action.apply(sse);
		}

		@Override
		public String toString() { return "Node--[x=" + x + ", y=" + y + ", w=" + width + ", h=" + height + "]"; }

		public boolean intersectsWithMap(ISOMWrapperLocated<Distance> map) {
			return MathUtilities.intersects(x, y, width, height, map.x, map.y, map.width, map.height);
		}

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
	} // end NodeQuadtreeMultiISOMRectangular

}