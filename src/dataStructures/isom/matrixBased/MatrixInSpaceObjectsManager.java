package dataStructures.isom.matrixBased;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import dataStructures.isom.InSpaceObjectsManagerImpl;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.ObjLocatedCollectorIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.ProviderShapeRunner;
import geometry.ProviderShapesIntersectionDetector;
import geometry.implementations.PathOptimizerPoint;
import geometry.implementations.ProviderShapeRunnerImpl;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.PointConsumer;
import tools.Comparators;
import tools.LoggerMessages;
import tools.NumberManager;
import tools.UniqueIDProvider;

/** Rectangular matrix-based implementation */
public abstract class MatrixInSpaceObjectsManager<Distance extends Number> extends InSpaceObjectsManagerImpl<Distance> {
	private static final long serialVersionUID = 6663104159265L;
	protected static final Double ONE = 1.0, SQRT_TWO = /* Math.max(justOne + 0.5, */Math.sqrt(2);
	public static final CoordinatesDeltaForAdjacentNodes[] VALUES_CoordinatesDeltaForAdjacentNodes = CoordinatesDeltaForAdjacentNodes
			.values();
	protected static final UniqueIDProvider idProvider = UniqueIDProvider.newBasicIDProvider();
//	public static enum OperationOnShape {Add, Remove, Replace, Collect;}

	public static enum CoordinatesDeltaForAdjacentNodes {
		// TOP(0, -1), RIGHT(+1, 0), BOTTOM(0, 1), LEFT(-1, 0);
		NORD(0, -1, ONE), SUD(0, 1, ONE), OVEST(-1, 0, ONE), EST(1, 0, ONE)//
		, NORD_EST(1, -1, SQRT_TWO), SUD_EST(1, 1, SQRT_TWO), SUD_OVEST(-1, 1, SQRT_TWO), NORD_OVEST(-1, -1, SQRT_TWO)//
		;

		public final int dx, dy;
		public final Double weight;

		CoordinatesDeltaForAdjacentNodes(int dxx, int dyy, double w) {
			dx = dxx;
			dy = dyy;
			this.weight = w;
		}
	}

	public MatrixInSpaceObjectsManager(boolean isLazyNodeInstancing, int width, int height,
			NumberManager<Distance> weightManager) {
		this(idProvider.getNewID(), isLazyNodeInstancing, width, height, weightManager);
	}

	public MatrixInSpaceObjectsManager(Long ID, boolean isLazyNodeInstancing, int width, int height,
			NumberManager<Distance> weightManager) {
		super();
		this.setID(ID);
		this.isLazyNodeInstancing = isLazyNodeInstancing;
//		this.height = height;
//		this.width = width;
		this.weightManager = weightManager;
		onCreate(width, height);
		// TODO create intersectors and runners
//		ProviderShapesIntersectionDetector shapeIntersectionDetectorProvider;
//		ProviderShapeRunner shapeRunnerProvider = new ProviderShapeRunnerImpl() ???? e quello che fa cache dei cerchi?
	}

//	protected Comparator IDOwidComparator;
	protected final boolean isLazyNodeInstancing;
//	protected int width, height;
	protected NodeIsom<Distance>[][] matrix;
	protected AbstractShape2D shape;
	protected ProviderShapesIntersectionDetector shapeIntersectionDetectorProvider;
	protected ProviderShapeRunner shapeRunnerProvider;
	protected Map<Long, ObjectLocated> objectsAdded;
	protected Set<ObjectLocated> objectsAddedSet;

	//

	protected void onCreate(int width, int height) {
		this.shape = new ShapeRectangle(0, width >> 1, height >> 1, true, width, height);
		setObjectsAdded(MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.LONG_COMPARATOR));
		setProviderShapeRunner(ProviderShapeRunnerImpl.getInstance());
		setProviderShapesIntersectionDetector(ProviderShapesIntersectionDetector.getInstance());
		setPathOptimizer(PathOptimizerPoint.getInstance());
		reinstanceMatrix();
	}

	//

	@Override
	public AbstractShape2D getBoundingShape() { return shape; }

	@Override
	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() {
		return shapeIntersectionDetectorProvider;
	}

	@Override
	public ProviderShapeRunner getProviderShapeRunner() { return shapeRunnerProvider; }

	public boolean isLazyNodeInstancing() { return isLazyNodeInstancing; }

	public ProviderShapeRunner getShapeRunnerProvider() { return shapeRunnerProvider; }

	/** Use with caution. */
	public Map<Long, ObjectLocated> getObjectsAdded() { return objectsAdded; }

	@Override
	public Set<ObjectLocated> getAllObjectLocated() { return objectsAddedSet; }

	public NodeIsom<Distance>[] getRow(int y) { return (y < 0 || y >= matrix.length) ? null : matrix[y]; }

	//

	// TODO SETTER

	@Override
	public void setShape(AbstractShape2D shape) {}

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		this.shapeIntersectionDetectorProvider = providerShapesIntersectionDetector;
	}

	public void setShapeRunnerProvider(ProviderShapeRunner shapeRunnerProvider) {
		this.shapeRunnerProvider = shapeRunnerProvider;
	}

	/** Sets the map holding all objects in this space. */
	public void setObjectsAdded(Map<Long, ObjectLocated> objectsAdded) {
		this.objectsAdded = objectsAdded;
		if (objectsAdded == null)
			this.objectsAddedSet = null;
		else {
			if (objectsAdded instanceof MapTreeAVL<?, ?>)
				this.objectsAddedSet = ((MapTreeAVL<Long, ObjectLocated>) objectsAdded).toSetValue(ol -> ol.getID());
			else
				this.objectsAddedSet = new SetMapped<>(objectsAdded.entrySet(), e -> e.getValue());
		}
	}

	@Override
	public void setLog(LoggerMessages log) { this.log = LoggerMessages.loggerOrDefault(log); }

	@Override
	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) {
		this.shapeRunnerProvider = providerShapeRunner;
	}

	//

	//

	public abstract NodeIsom<Distance> newNodeMatrix(int x, int y);

	@Override
	public void forEachNode(Consumer<NodeIsom<Distance>> action) {
		int r, c, w, h;
		NodeIsom<Distance>[] row, m[];
		m = this.matrix;
		if (action == null || m == null)
			return;
		h = this.getHeight();
		w = this.getWidth();
		r = -1;
		while (++r < h) {
			row = m[r];
			c = -1;
			while (++c < w) {
				action.accept(row[c]);
			}
		}
	}

	@Override
	public void forEachNode(BiConsumer<NodeIsom<Distance>, Point> action) {
		int r, c, w, h;
		NodeIsom<Distance>[] row, m[];
		Point p;
		m = this.matrix;
		if (action == null || m == null)
			return;
		h = this.getHeight();
		w = this.getWidth();
		p = new Point();
		r = -1;
		while (++r < h) {
			row = m[r];
			c = -1;
			while (++c < w) {
				p.y = r;// reset me also
				p.x = c;
				action.accept(row[c], p);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void reinstanceMatrix() {
		int r, c, w, h;
		NodeIsom<Distance>[] row, m[];
		m = this.matrix = new NodeIsom[h = this.getHeight()][w = this.getWidth()];
		if (isLazyNodeInstancing)
			return;
		r = -1;
		while (++r < h) {
			row = m[r];
			c = -1;
			while (++c < w) {
				row[c] = newNodeMatrix(c, r);
			}
		}
	}

	/**
	 * The point is relative to this map (i.e.: negative values will cause an
	 * {@link Exception}).
	 */
	@Override
	public NodeIsom<Distance> getNodeAt(Point p) {
		int x, y;
		NodeIsom<Distance> n;
//		n = matrix[y = (int) p.getY()][x = (int) p.getX()];
		n = null;
		y = p.y;
		x = p.x;
		if (y >= 0 && x >= 0 && y < matrix.length && x < matrix[0].length) {
			n = matrix[y][x];
		} else
			return null;
		if (isLazyNodeInstancing && n == null) { n = matrix[y][x] = newNodeMatrix(x, y); }
		return n;
	}

	/** See {@link #getNodeAt(Point)}. */
	@Override
	public NodeIsom<Distance> getNodeAt(int x, int y) {
		NodeIsom<Distance> n;
		n = matrix[y][x];
		if (isLazyNodeInstancing && n == null) { n = matrix[y][x] = newNodeMatrix(x, y); }
		return n;
	}

	@Override
	public ObjectLocated getAt(int x, int y) { return getNodeAt(x, y).getObject(0); }

	//

	@Override
	public void runOnWholeMap(PointConsumer action) {
		Point p;
		p = new Point();
		int r, c, w, h;
		NodeIsom<Distance>[] row, m[];
//		NodeIsom node;
		m = this.matrix;
		h = this.getHeight();
		w = this.getWidth();
		r = -1;
		while (++r < h) {
			row = m[r];
			c = -1;
			p.y = r;
			while (++c < w) {
				if (row[c] != null) {
//				if ((node = row[c]) != null) {
					p.x = c;
					action.accept(p);
				}
			}
		}
	}

	@Override
	public ObjLocatedCollectorIsom<Distance> newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) {
		return new ObjLocatedCollectorMatrix<>(this, objectFilter);
	}

	// TODO add

	@Override
	public ObjectLocated getObjectLocated(Long ID) { return this.objectsAdded.get(ID); }

	@Override
	public boolean add(ObjectLocated o) {
		AdderObjLocated<Distance> a;
		if (o == null)
			return false;
		a = new AdderObjLocated<>(this, o);
		if (a instanceof ObjectShaped) {
			runOnShape(((ObjectShaped) o).getShape(), a);
		} else {
			a.accept(o.getLocation());
		}
		this.getObjectsAdded().put(o.getID(), o);
		return true;
	}

	@Override
	public boolean contains(ObjectLocated o) { return o != null && this.getObjectsAdded().containsKey(o.getID()); }

	@Override
	public boolean remove(ObjectLocated o) {
		RemoverObjLocated<Distance> r;
		if ((o == null) || (this.getObjectsAdded().remove(o.getID()) != null))
			return false;
		r = new RemoverObjLocated<>(this, o);
		if (r instanceof ObjectShaped) {
			runOnShape(((ObjectShaped) o).getShape(), r);
		} else {
			r.accept(o.getLocation());
		}
		return true;
	}

	@Override
	public boolean removeAllObjects() {
		Thread tClearer;
		if (this.getObjectsAdded().isEmpty())
			return false;
		this.getObjectsAdded().clear();
		tClearer = new Thread(() -> {
			runOnWholeMap(p -> {
				NodeIsom<Distance> n;
				n = getNodeAt(p);
				if (n != null)
					n.removeAllObjects();
			});
		});
		tClearer.start();
		return true;
	}

	@Override
	public void forEachAdjacents(NodeIsom<Distance> node,
			BiConsumer<NodeIsom<Distance>, Distance> adjacentDistanceConsumer) {
		boolean xNotTooOnRight, xStrictlyPositive;
		int x;
		NodeIsom<Distance> row[];
		x = node.x;
		xStrictlyPositive = x > 0;
		xNotTooOnRight = x < (matrix[0].length - 1);
		if (node.y > 0) {
			row = matrix[node.y - 1];
			if (xStrictlyPositive)
				adjacentDistanceConsumer.accept(row[x - 1], weightManager.fromDouble(SQRT_TWO));
			adjacentDistanceConsumer.accept(row[x], weightManager.fromDouble(ONE));
			if (xNotTooOnRight)
				adjacentDistanceConsumer.accept(row[x + 1], weightManager.fromDouble(SQRT_TWO));
		}
		if (xStrictlyPositive)
			adjacentDistanceConsumer.accept(matrix[node.y][x - 1], weightManager.fromDouble(ONE));
		if (xNotTooOnRight)
			adjacentDistanceConsumer.accept(matrix[node.y][x + 1], weightManager.fromDouble(ONE));
		if (node.y < (matrix.length - 1)) {
			row = matrix[node.y + 1];
			if (xStrictlyPositive)
				adjacentDistanceConsumer.accept(row[x - 1], weightManager.fromDouble(SQRT_TWO));
			adjacentDistanceConsumer.accept(row[x], weightManager.fromDouble(ONE));
			if (xNotTooOnRight)
				adjacentDistanceConsumer.accept(row[x + 1], weightManager.fromDouble(SQRT_TWO));
		}

	}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		ObjLocatedCollectorMatrix<Distance> coll;
		coll = new ObjLocatedCollectorMatrix<>(this, objectFilter);
		return this.collectInPath(areaToLookInto, coll, path);
	}

	@Override
	public Iterator<ObjectLocated> iterator() { return new IteratorNodeIsom(); }

	//

	// classes

	protected static abstract class ActionOnPointWithObj<D extends Number> extends PointConsumerRowOptimizer<D> {
		private static final long serialVersionUID = 1L;
		ObjectLocated oToManipulate;

		public ActionOnPointWithObj(MatrixInSpaceObjectsManager<D> misom, ObjectLocated oToManipulate) {
			super(misom);
			this.oToManipulate = oToManipulate;
		}
	}

	protected static class AdderObjLocated<D extends Number> extends ActionOnPointWithObj<D> {
		public AdderObjLocated(MatrixInSpaceObjectsManager<D> misom, ObjectLocated oToManipulate) {
			super(misom, oToManipulate);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void acceptImpl(Point p) {
			NodeIsom<D> n;
			n = this.misom.getNodeAt(p);
			if (n != null) {
				n.addObject(oToManipulate);
				System.out.println("YEAH nodeIsom found at: " + p);
			} else {
				System.out.println("No nodeIsom found at: " + p);
			}
		}
	}

	protected static class RemoverObjLocated<D extends Number> extends ActionOnPointWithObj<D> {
		public RemoverObjLocated(MatrixInSpaceObjectsManager<D> misom, ObjectLocated oToManipulate) {
			super(misom, oToManipulate);
		}

		private static final long serialVersionUID = 1L;

		@Override
		public void acceptImpl(Point p) {
			NodeIsom<D> n;
			n = this.misom.getNodeAt(p);
			if (n != null)
				n.removeObject(oToManipulate);
		}
	}

	protected class IteratorNodeIsom implements Iterator<ObjectLocated> {
		int r, c, w, h;
		Iterator<ObjectLocated> nodeIteratingIterator;
		NodeIsom<Distance> nodeIterating;

		protected IteratorNodeIsom() {
			r = c = 0;
			h = getHeight();
			w = getWidth();
			nodeIteratingIterator = null;
			nodeIterating = null;
		}

		@Override
		public boolean hasNext() { return r < h; }

		@Override
		public ObjectLocated next() {
			NodeIsom<Distance> n;
			n = null;
			// find the first available
			if (nodeIteratingIterator == null || (!nodeIteratingIterator.hasNext())) {
				while (hasNext() && ((n = getNodeAt(c, r)) == null || n.countObjectAdded() == 0)) {
					toNext();
				}
			}
			if (n == null || (!hasNext()))
				return null; // nothing more to check
			if (nodeIterating != n) {// is the first NodeIsom one discovered or a new one, not jet iterated?
				nodeIteratingIterator = n.iterator();
				nodeIterating = n;
			}
			return nodeIteratingIterator.next();
		}

		protected void toNext() {
			if (r < h && ++c >= w) {
				c = 0;
				r++;
			}
		}
	}
}