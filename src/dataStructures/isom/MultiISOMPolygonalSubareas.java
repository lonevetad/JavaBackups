package dataStructures.isom;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import dataStructures.isom.MultiISOMRetangularMap.NodeQuadtreeMultiISOMRectangular;
import dataStructures.isom.internal.ISOMWrapperLocated;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager.CoordinatesDeltaForAdjacentNodes;
import dataStructures.isom.pathFinders.Heuristic8GridMovement;
import dataStructures.isom.pathFinders.PathFinderIsomAStar;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.PointInt;
import geometry.ProviderShapeRunner;
import geometry.ProviderShapesIntersectionDetector;
import geometry.ShapesIntersectionDetector;
import geometry.implementations.shapes.ShapeRectangle;
import tools.Comparators;
import tools.NumberManager;
import tools.Stringable;
import tools.UniqueIDProvider;

// TODO nella "addMap", considerare	la Shape dell'ISOM da aggiungere E
/**
 * Takes inspiration from {@link MultiISOMRetangularCaching}, but areas could be
 * polygonal, no matter what.
 */
public class MultiISOMPolygonalSubareas<Distance extends Number> extends InSpaceObjectsManagerImpl<Distance>
		implements InSpaceObjectsManager<Distance> {
	private static final long serialVersionUID = 1L;
	protected static final UniqueIDProvider ID_PROV_MULTI_ISOM_POLY = UniqueIDProvider.newBasicIDProvider();

	public MultiISOMPolygonalSubareas(int maximumSubmapsEachSection) {
		super();
		if (maximumSubmapsEachSection < 1) {
			log.log("Incorrect number of maximum submaps on each section: " + maximumSubmapsEachSection);
			throw new IllegalArgumentException(
					"Incorrect number of maximum submaps on each section: " + maximumSubmapsEachSection);
		}
		this.setID(ID_PROV_MULTI_ISOM_POLY.getNewID());
		this.maximumSubmapsEachSection = maximumSubmapsEachSection;
		this.uidProvider = UniqueIDProvider.newBasicIDProvider();
		mapsLocatedInSpace = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.LONG_COMPARATOR);
		mapsAsList = mapsLocatedInSpace.toListValue(r -> r.ID);
		isomsHeldCenterLocated = new SetMapped<>(mapsLocatedInSpace.toSetValue(ISOMWrapperLocated::getID),
				ISOMWrapperLocated::getIsomAndLocation);
		setObjectsAddedMap(MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR));
	}

	protected final int maximumSubmapsEachSection;
	protected int maxDepth, xLeftTop, yLeftTop, xRightBottom, yRightBottom, width, height;
	protected NodeQuadtreeISOM root;
	protected final MapTreeAVL<Long, ISOMWrapperLocated<Distance>> mapsLocatedInSpace;
	protected final Set<Entry<InSpaceObjectsManager<Distance>, PointInt>> isomsHeldCenterLocated;
	protected final List<ISOMWrapperLocated<Distance>> mapsAsList;
	protected ShapeRectangle shapeBoundingBox;
	protected Map<Long, ObjectLocated> objectsAddedMap;
	protected Set<ObjectLocated> objectsAddedSet;
	//
	protected final UniqueIDProvider uidProvider;
	protected ProviderShapeRunner providerShapeRunner;
	/** Used on adding algorithm */
	protected ProviderShapesIntersectionDetector providerShapesIntersectionDetector;
	//
	protected ISOMWrapperLocated<Distance> cachedIsom;

	///

	//

	//

	public ISOMWrapperLocated<Distance> getCachedMisom() { return cachedIsom == null ? null : cachedIsom /* .misom */; }

	public NodeQuadtreeISOM getRoot() { return root; }

	public int getMaxDepth() { return maxDepth; }

	public int getxLeftTop() { return xLeftTop; }

	public int getyLeftTop() { return yLeftTop; }

	public int getxRightBottom() { return xRightBottom; }

	public int getyRightBottom() { return yRightBottom; }

	@Override
	public int getWidth() { return width; }

	@Override
	public int getHeight() { return height; }

	@Override
	public AbstractShape2D getBoundingShape() { return this.shapeBoundingBox; }

	@Override
	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() {
		return this.providerShapesIntersectionDetector;
	}

	@Override
	public ProviderShapeRunner getProviderShapeRunner() { return this.providerShapeRunner; }

	@Override
	public Set<ObjectLocated> getAllObjectLocated() { return this.objectsAddedSet; }

	@Override
	public ObjectLocated getObjectLocated(Long ID) { return this.objectsAddedMap.get(ID); }

	/**
	 * Returns a set of pairs of {@link InSpaceObjectsManager} and its absolute
	 * location of its top-left corner (assuming a null rotation, i.e.
	 * {@link NodeQuadtreeISOM#get}
	 */

	public Set<Entry<InSpaceObjectsManager<Distance>, PointInt>> getIsomsHeldCenterLocated() {
		return isomsHeldCenterLocated;
	}

	///

	//

	@Override
	public boolean setID(Long newID) { return false; }

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		this.providerShapesIntersectionDetector = providerShapesIntersectionDetector;
	}

	@Override
	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) { // TODO Auto-generated method stub
		this.providerShapeRunner = providerShapeRunner;
	}

	@Override
	public void setShape(AbstractShape2D shape) {}

	protected PathFinderIsom<Distance> newPathFinder() {
		return new PathFinderIsomAStar<>(this, new Heuristic8GridMovement<>(getWeightManager()));
	}

	@Override
	public void setWeightManager(NumberManager<Distance> numberManager) {
		this.weightManager = numberManager;
		this.setPathFinder(newPathFinder());
	}

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

	// TODO other public methods

	//

	// but this following method is fundamental as public ones

	/**
	 * Create a new {@link NodeQuadtreeMultiISOMRectangular} based on a given map
	 * (to be held) and a father node.
	 */
	protected NodeQuadtreeISOM newNodeWith(ISOMWrapperLocated<Distance> map, NodeQuadtreeISOM fatherNode) {
		NodeQuadtreeISOM newNode;
		newNode = new NodeQuadtreeISOM(fatherNode);
		if (this.getMaxDepth() < newNode.depth) { this.maxDepth = newNode.depth; }
		newNode.submaps = new ArrayList<>(4);
		newNode.submaps.add(map);
		return newNode;
	}

	public void clear() { removeAllMaps(); }

	public void removeAllMaps() {
		mapsLocatedInSpace.clear();
		root = null;
		clearDimensionCache();
	}

	@Override
	public NodeIsom<Distance> getNodeAt(Point location) { return getNodeAt(location.x, location.y); }

	@Override
	public NodeIsom<Distance> getNodeAt(int x, int y) {
		ISOMWrapperLocated<Distance> ml;
		ml = getMapLocatedContaining(x, y);
		if (ml == null)
			return null;
		return ml.getNodeAt(x, y); // offset is implemented in this delegated function
	}

	@Override
	public void forEachNode(BiConsumer<NodeIsom<Distance>, Point> action) {
		this.mapsLocatedInSpace.forEach((i, mw) -> mw.forEachNode(action));
	}

	/**
	 * See {@link MultiISOMRetangularMap#getMapLocatedContaining(Point)}, since it's
	 * a similar function.
	 */
	public ISOMWrapperLocated<Distance> getMapLocatedContaining(Point p) {
		return getMapLocatedContaining(p.x, p.y);
	}

	/**
	 * See {@link MultiISOMRetangularMap#getMapLocatedContaining(int, int)}, since
	 * it's a similar function.
	 */
	public ISOMWrapperLocated<Distance> getMapLocatedContaining(int x, int y) {
		NodeQuadtreeISOM n, prev;
		List<ISOMWrapperLocated<Distance>> submaps;

		n = prev = getRoot();
		if (n == null)
			return null;
		// check cache
//		providerShapesIntersectionDetector.getShapesIntersectionDetector(si1, si2)
		if (cachedIsom != null && cachedIsom.getShape().contains(x, y)) { return cachedIsom; }

		// traverse the tree
		while (!(n == null || n.isLeaf())) {
			prev = n;
			if (x >= n.getXMiddle()) { // east
				n = (y >= n.getYMiddle()) ? n.sse : n.sne;
			} else {// west
				n = (y >= n.getYMiddle()) ? n.ssw : n.snw;
			}
		}
		// get the collection of submaps
		submaps = (n != null) ? n.submaps : prev.submaps;
		if (submaps == null)
			return null;
		// if any holds that point, then return it
		for (ISOMWrapperLocated<Distance> r : submaps) {
//			if (r.contains(x, y)) // MathUtilities.isInside(r, p))//
			if (r.getIsomHeld().getShape().contains(x, y))
				return r;
		}
		return null; // Error 404
	}

	// TODO addMap

	/**
	 * See
	 * {@link MultiISOMRetangularMap#addMap(MatrixInSpaceObjectsManager, int, int)},
	 * since it's a similar function.<br>
	 * REMEMBER: the given coordinates are the top-left corner.
	 */
	public ISOMWrapperLocated<Distance> addMap(InSpaceObjectsManager<Distance> map, int x, int y) {
		return addMap(map, x, y, 0.0);
	}

	/**
	 * See
	 * {@link MultiISOMRetangularMap#addMap(MatrixInSpaceObjectsManager, int, int,double)},
	 * since it's a similar function.
	 */
	public ISOMWrapperLocated<Distance> addMap(InSpaceObjectsManager<Distance> map, int x, int y,
			double angleRotationDegrees) {
		int c;
		ISOMWrapperLocated<Distance> r;
		if (map == null || map.getWidth() < 1 || map.getWidth() < 1)
			return null;
		map.setTopLeftCorner(x, y);
		System.out.println("DEBUG-level LOG of " + MultiISOMPolygonalSubareas.class.getName());
		System.out.println(
				"  setting top-left corner in (x:" + x + ";y:" + y + ") , but map gives: " + map.getTopLeftCorner());
		System.out.println("  center point: " + map.getCenter() + ", w: " + map.getWidth() + ", h: " + map.getHeight());
		r = new ISOMWrapperLocated<>(map);
		r.setAngleRotationDegrees(angleRotationDegrees);
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
	public ISOMWrapperLocated<Distance> addMap(InSpaceObjectsManager<Distance> map, Point locationLeftTop) {
		return addMap(map, locationLeftTop, 0.0);
	}

	public ISOMWrapperLocated<Distance> addMap(InSpaceObjectsManager<Distance> map, Point locationLeftTop,
			double angleRotationDegrees) {
		if (map == null || map.getWidth() < 1 || map.getWidth() < 1)
			return null;
		return addMap(map, locationLeftTop.x, locationLeftTop.y, angleRotationDegrees);
	}

	public void rebuild() {
		this.maxDepth = 0;
		this.root = rebuild(null, mapsAsList, //
				this.getxLeftTop(), this.getyLeftTop(), this.xRightBottom, this.yRightBottom, this.width, this.height//
				, this.getxLeftTop() + (this.width >> 1)//
				, this.getyLeftTop() + (this.height >> 1)); // clear all
	}

//	public void addMaps(Collection<InSpaceObjectsManager<Distance>> mapsList) {
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

	public void removeMapLocatedIn(InSpaceObjectsManager<Distance> map, Point location) {
		this.removeMapLocatedIn(map, location.x, location.y);
	}

	public void removeMapLocatedIn(InSpaceObjectsManager<Distance> map, int x, int y) {
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

	@Override
	public void forEachAdjacents(NodeIsom<Distance> node,
			BiConsumer<NodeIsom<Distance>, Distance> adjacentDistanceConsumer) {
//		ISOMWrapperLocated<Distance> misom;
		ISOMWrapperLocated<Distance> mlis;
		Point p, absoluteNodeLocation;
		NodeIsom<Distance> adj;
		p = new Point();
		absoluteNodeLocation = node.getLocationAbsolute();
		for (CoordinatesDeltaForAdjacentNodes c : MatrixInSpaceObjectsManager.VALUES_CoordinatesDeltaForAdjacentNodes) {
			p.x = absoluteNodeLocation.x + c.dx;
			p.y = absoluteNodeLocation.y + c.dy;
			adj = null;
//			misom = getMapLocatedContaining(p);
			mlis = getMapLocatedContaining(p);
//			if (misom != null) {
			if (mlis != null) {
				adj = this.getNodeAt(p); // changed to "this" to let considering the misom's offset
				if (adj != null && mlis.getShape().contains(p))
					adjacentDistanceConsumer.accept(adj, getWeightManager().fromDouble(c.weight));
			}
		}
	}

	@Override
	public boolean add(ObjectLocated o) {
		ISOMWrapperLocated<Distance> m;
		if (o == null)
			return false;
		m = getMapLocatedContaining(o.getLocation());
		if (m == null)
			System.out.println("---not found D:");
		if (m == null)
			return false;
		System.out.println("--- found isom wrapper at: " + o.getLocation());
		return m.add(o);
	}

	@Override
	public boolean contains(ObjectLocated o) {
		ISOMWrapperLocated<Distance> m;
		if (o == null)
			return false;
		m = getMapLocatedContaining(o.getLocation());
		if (m == null)
			return false;
		return m.contains(o);
	}

	@Override
	public boolean remove(ObjectLocated o) {
		ISOMWrapperLocated<Distance> m;
		if (o == null)
			return false;
		m = getMapLocatedContaining(o.getLocation());
		if (m == null)
			return false;
		return m.remove(o);
	}

	@Override
	public ObjectLocated getAt(int x, int y) {
		ISOMWrapperLocated<Distance> r;
		r = this.getMapLocatedContaining(x, y);
		if (r == null) { return null; }
		return r.getNodeAt(x, y).getObject(0);
	}

	// TODO protected

	/**
	 * See
	 * {@link MultiISOMRetangularMap#updateBoundingBox(dataStructures.isom.MultiISOMRetangularMap.MatrixISOMLocatedInSpace)},
	 * since it's a similar function.
	 */
	protected int updateBoundingBox(ISOMWrapperLocated<Distance> isom) {
		boolean changed;
		int temp;
		AbstractShape2D sh;
		Rectangle r;
		sh = isom.getShape();
		r = sh.getBoundingBox();
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
		//
		return 0;
	}

	protected NodeQuadtreeISOM rebuild(NodeQuadtreeISOM father, List<ISOMWrapperLocated<Distance>> submaps, //
			int xLeftTop, int yLeftTop, int xRightBottom, int yRightBottom, int width, int height, //
			int xMiddle, int yMiddle) {
		final int middlexw, middlexe, middleyn, middleys, widthWest, widthEst, heightNorth, heightSouth, yMiddlemmmm,
				xMiddlemmmm;
		NodeQuadtreeISOM n;
		List<ISOMWrapperLocated<Distance>> snw, sne, ssw, sse;
		final ShapeRectangle shapeTempSubarea;
		n = new NodeQuadtreeISOM(father);
		if (this.getMaxDepth() < n.depth) { this.maxDepth = n.depth; }
//		n.x = xLeftTop;
//		n.y = yLeftTop;
//		n.width = width;
//		n.height = height;
		n.shapeBBSubarea.setRectangle(xLeftTop, yLeftTop, width, height);
//		n.xMiddle = xMiddle;
//		n.yMiddle = yMiddle;
		if (submaps.size() <= maximumSubmapsEachSection //
				|| width <= MultiISOMRetangularMap.MINIMUM_DIMENSION_MAP
				|| height <= MultiISOMRetangularMap.MINIMUM_DIMENSION_MAP) {
			n.submaps = submaps;
			return n;
		}
		snw = new ArrayList<>(maximumSubmapsEachSection);
		sne = new ArrayList<>(maximumSubmapsEachSection);
		ssw = new ArrayList<>(maximumSubmapsEachSection);
		sse = new ArrayList<>(maximumSubmapsEachSection);
		yMiddlemmmm = yMiddle;
		xMiddlemmmm = xMiddle;
		middlexw = xLeftTop + ((widthWest = (xMiddle - xLeftTop)) >> 1);
		middleyn = yLeftTop + ((heightNorth = (yMiddle - yLeftTop)) >> 1);
		middlexe = ++xMiddle + ((widthEst = 1 + (xRightBottom - xMiddle)) >> 1);
		middleys = ++yMiddle + ((heightSouth = 1 + (yRightBottom - yMiddle)) >> 1);
		xMiddle = xMiddlemmmm;
		yMiddle = yMiddlemmmm;
		shapeTempSubarea = new ShapeRectangle(true);
		shapeTempSubarea.setAngleRotation(0);

		submaps.forEach(r -> {
			final ShapesIntersectionDetector intersectionDetector;
			final AbstractShape2D shapeMap;

			shapeMap = r.getIsomHeld().getShape();
			intersectionDetector = providerShapesIntersectionDetector.getShapesIntersectionDetector(shapeTempSubarea,
					shapeMap);
			shapeTempSubarea.setRectangle(xLeftTop, yLeftTop, widthWest, heightNorth);
			if (intersectionDetector.areIntersecting(shapeTempSubarea, shapeMap)) { snw.add(r); }
			shapeTempSubarea.setRectangle(xMiddlemmmm + 1, yLeftTop, widthEst, heightNorth);
			if (intersectionDetector.areIntersecting(shapeTempSubarea, shapeMap)) { sne.add(r); }
			shapeTempSubarea.setRectangle(xMiddlemmmm + 1, yMiddlemmmm + 1, widthEst, heightSouth);
			if (intersectionDetector.areIntersecting(shapeTempSubarea, shapeMap)) { sse.add(r); }
			shapeTempSubarea.setRectangle(xLeftTop, yMiddlemmmm + 1, widthWest, heightSouth);
			if (intersectionDetector.areIntersecting(shapeTempSubarea, shapeMap)) { ssw.add(r); }
		});
		if (snw.size() > 0)
			n.snw = rebuild(n, snw, //
					xLeftTop, yLeftTop, xMiddlemmmm, yMiddlemmmm, // corner points
					widthWest, heightNorth, //
					middlexw, middleyn); // middle point
		if (sne.size() > 0)
			n.sne = rebuild(n, sne, //
					(xMiddlemmmm + 1), yLeftTop, xRightBottom, yMiddlemmmm, // corner points
					widthEst, heightNorth, // dimensions
					middlexe, middleyn); // middle point
		if (ssw.size() > 0)
			n.ssw = rebuild(n, ssw, //
					xLeftTop, yMiddlemmmm + 1, xMiddlemmmm, yRightBottom, // corner points
					widthWest, heightSouth, // dimensions
					middlexw, middleys); // middle point
		if (sse.size() > 0)
			n.sse = rebuild(n, sse, //
					xMiddlemmmm + 1, yMiddlemmmm + 1, xRightBottom, yRightBottom, // corner points
					widthEst, heightSouth, // dimensions
					middlexe, middleys); // middle point
		return n;
		// TODO to-do convert from MultiISOMRectangular to M..Poly
	}

	protected void addNotRebuilding(ISOMWrapperLocated<Distance> map) {
		if (getRoot() == null)
			rebuild();
		else
			root = addNotRebuilding(map, getRoot());
	}

	// TODO to-do convert from MultiISOMREctangular to M..Poly
	protected NodeQuadtreeISOM addNotRebuilding(ISOMWrapperLocated<Distance> map, NodeQuadtreeISOM currentNode) {
		int xcn, ycn, wcn, hcn;
		xcn = currentNode.getXTopLeft();
		ycn = currentNode.getYTopLeft();
		wcn = currentNode.getWidthSubmap();
		hcn = currentNode.getHeightSubmap();
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
				currentNode = rebuild(currentNode.father, /* currentNode. */submaps, xcn, ycn, xcn + wcn, ycn + wcn,
						wcn, hcn, currentNode.getXMiddle(), currentNode.getYMiddle());
				if (this.getMaxDepth() < currentNode.depth) { this.maxDepth = currentNode.depth; }
			}

		} else {
			int hw, hh, x_w_1, y_h_1;
			AbstractShape2D shapeMap;
			ShapeRectangle shapeNodeSubsection;
			ShapesIntersectionDetector intersectionDetector;
			shapeMap = map.getShape();
			hw = wcn >> 1;
			hh = hcn >> 1;
			x_w_1 = xcn + hw + 1;
			y_h_1 = ycn + hh + 1;
			// for each subsection ..
			shapeNodeSubsection = new ShapeRectangle(0.0, xcn, ycn, true, hw, hh);
			intersectionDetector = providerShapesIntersectionDetector.getShapesIntersectionDetector(shapeNodeSubsection,
					shapeMap);
			if ((currentNode.snw != null && currentNode.snw.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.snw == null && //
//							MathUtilities.intersects(xcn, ycn, hw, hh, shapeMap.getXTopLeft(), shapeMap.getYTopLeft(),
//									shapeMap.getWidth(), shapeMap.getHeight())
							intersectionDetector.areIntersecting(shapeNodeSubsection, shapeMap)//
					)) {
				currentNode.snw = (currentNode.snw == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.snw);
			}
			shapeNodeSubsection.setRectangle(x_w_1, ycn, wcn - hw, hh);
			if ((currentNode.sne != null && currentNode.sne.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.sne == null && //
//							MathUtilities.intersects(x_w_1, ycn, wcn - hw, hh, shapeMap.getXTopLeft(),
//									shapeMap.getYTopLeft(), shapeMap.getWidth(), shapeMap.getHeight())
							intersectionDetector.areIntersecting(shapeNodeSubsection, shapeMap)//
					)) {
				currentNode.sne = (currentNode.sne == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.sne);
			}
			shapeNodeSubsection.setRectangle(xcn, y_h_1, hw, hcn - hh);
			if ((currentNode.ssw != null && currentNode.ssw.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.ssw == null && //
//							MathUtilities.intersects(xcn, y_h_1, hw, hcn - hh, shapeMap.getXTopLeft(),
//									shapeMap.getYTopLeft(), shapeMap.getWidth(), shapeMap.getHeight())
							intersectionDetector.areIntersecting(shapeNodeSubsection, shapeMap)//
					)) {
				currentNode.ssw = (currentNode.ssw == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.ssw);
			}
			shapeNodeSubsection.setRectangle(x_w_1, y_h_1, wcn - hw, hcn - hh);
			if ((currentNode.sse != null && currentNode.sse.intersectsWithMap(map)) //
					|| // or that area does NOT exists BUT could hold the new map
					(currentNode.sse == null && //
//							MathUtilities.intersects(x_w_1, y_h_1, wcn - hw, hcn - hh, shapeMap.getXTopLeft(),
//									shapeMap.getYTopLeft(), shapeMap.getWidth(), shapeMap.getHeight())
							intersectionDetector.areIntersecting(shapeNodeSubsection, shapeMap)//
					)) {
				currentNode.sse = (currentNode.sse == null) ? newNodeWith(map, currentNode)
						: addNotRebuilding(map, currentNode.sse);
			}
		}
		return currentNode;
	}

	//

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

	protected void clearDimensionCache() {
		// neverBuilt = true;
		shapeBoundingBox = null;
		maxDepth = 0;
		xLeftTop = yLeftTop = xRightBottom = yRightBottom = width = height = 0;
//		this.objectsAddedMap.clear();
	}

	//

	// TODO CLASS

	/** It's still rectangular */
	protected class NodeQuadtreeISOM implements Stringable {
		private static final long serialVersionUID = 530125748L;
		protected int /* xLeftTop, yLeftTop, width, height, xMiddle, yMiddle, */
		depth;
		List<ISOMWrapperLocated<Distance>> submaps;
		NodeQuadtreeISOM father, snw, sne, ssw, sse;
		ShapeRectangle shapeBBSubarea;

		protected NodeQuadtreeISOM(MultiISOMPolygonalSubareas<Distance>.NodeQuadtreeISOM father) {
			super();
			this.father = father;
			submaps = null;
			depth = (father == null) ? 1 : (father.depth + 1);
			shapeBBSubarea = new ShapeRectangle(true);
		}

		public List<ISOMWrapperLocated<Distance>> getSubmaps() { return submaps; }

		public boolean isLeaf() { return submaps != null; }

		public int getXTopLeft() { return shapeBBSubarea.getXTopLeft(); }

		public int getYTopLeft() { return shapeBBSubarea.getYTopLeft(); }

		public int getWidthSubmap() { return shapeBBSubarea.getWidth(); }

		public int getHeightSubmap() { return shapeBBSubarea.getHeight(); }

		/** Similar (or same) to the concept of "centre". */
		public int getXMiddle() {
			return // this.xMiddle;
			shapeBBSubarea.getCenter().x;
		}

		/** See {@link #getXMiddle()}. */
		public int getYMiddle() {
			return // this.yMiddle;
			shapeBBSubarea.getCenter().y;
		}

		public int getDepth() { return this.depth; }

		/** Beware of <code>null</code>s! */
		public void forEachSubsection(Consumer<NodeQuadtreeISOM> action) {
			action.accept(snw);
			action.accept(sne);
			action.accept(ssw);
			action.accept(sse);
		}

		public void recalculateEachSubsection(Function<NodeQuadtreeISOM, NodeQuadtreeISOM> action) {
			snw = action.apply(snw);
			sne = action.apply(sne);
			ssw = action.apply(ssw);
			sse = action.apply(sse);
		}

		public boolean intersectsWithMap(ISOMWrapperLocated<Distance> map) {
//			return MathUtilities.intersects(getXTopLeft(), getYTopLeft(), getWidthSubmap(), getHeightSubmap(), map.x, map.y, map.width,
//					map.height);
			return false;
		}
	}

	//

	//

	//

	//

	//

	@Override
	public ObjLocatedCollectorIsom<Distance> newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) { // TODO
																												// Auto-generated
																												// method
																												// stub
		return null;
	}

	@Override
	public Iterator<ObjectLocated> iterator() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		// TODO Auto-generated method stub
		return null;
	}
}