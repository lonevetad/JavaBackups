package oldToBeDeleted;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.isom.InSpaceObjectsManagerImpl;
import dataStructures.isom.MultiISOMRetangularMap;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.ObjLocatedCollectorIsom;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ProviderShapeRunner;
import geometry.ProviderShapesIntersectionDetector;
import tools.Comparators;
import tools.NumberManager;

/**
 * A set of {@link MatrixInSpaceObjectsManager} to build a map that could be
 * non-rectangular, i.e. non-simple shaped.
 * <p>
 *
 * @deprecated because it's replaced by {@link MultiISOMRetangularMap}
 */
@Deprecated
public abstract class ISOMRaggedMatrix<Distance extends Number> extends InSpaceObjectsManagerImpl<Distance> {

//TODO

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ISOMRaggedMatrix(NumberManager<Distance> weightManager) {
		super();
		this.weightManager = weightManager;
		chunks = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration, Comparators.INTEGER_COMPARATOR);
	}

	private int idChunkProgressive = 0;
	protected final NumberManager<Distance> weightManager;
	protected MapTreeAVL<Integer, ChunkMISOM> chunks;

	//

	//

	protected ChunkMISOM getChunkHoldingPoint(int x, int y) {
		ChunkMISOM c;
		// no smarter way right now than iterating through all chunks
		for (Entry<Integer, ChunkMISOM> e : chunks) {
			c = e.getValue();
			if (c.getBoundingShape().contains(x, y))
				return c;
		}
		return null; // TODO
	}

	protected void recalculateShape() {
		if (chunks.isEmpty())
			return;

	}

	public Map<Integer, ChunkMISOM> getChunks() { return chunks; }

	/**
	 * Add a rectangular chunk (i.e. a {@link InSpaceObjectsManagerImpl} having a
	 * {@link Rectangle} as a {@link AbstractShape2D}) to this set
	 */
//	public void addChunk(int xTopLeft, int yTopLeft, Dimension sizeRectangularRegion) {
	public void addChunk(Rectangle r) {
		int w, h;
		ChunkMISOM c;
		if (r == null)
			return;
		c = new ChunkMISOM(false, w = r.width, h = r.height, weightManager);
		c.getBoundingShape().setCenter(r.x + (w >> 1), r.y + (h >> 1));
		chunks.put(c.IDChunk, c);
	}

	public void linkChunks(Integer id1, Integer id2) { linkChunks(chunks.get(id1), chunks.get(id2)); }

	public void linkChunks(ChunkMISOM c1, ChunkMISOM c2) {
		c1.addAdjacent(c2);
		c2.addAdjacent(c1);
	}

	@Override
	public NodeIsom<Distance> getNodeAt(Point location) { return null; }

	public abstract NodeIsom<Distance> newNodeMatrix(int x, int y);

	@Override
	public AbstractShape2D getBoundingShape() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() { // TODO Auto-generated method
																						// stub
		return null;
	}

	@Override
	public ProviderShapeRunner getProviderShapeRunner() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) { // TODO Auto-generated method stub
	}

	@Override
	public boolean add(ObjectLocated o) { return false; }

	@Override
	public boolean contains(ObjectLocated o) { // TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(ObjectLocated o) { // TODO Auto-generated method stub
		return false;
	}

	@Override
	public ObjLocatedCollectorIsom<Distance> newObjLocatedCollector(Predicate<ObjectLocated> objectFilter) {
		return null;
	}

	@Override
	public Iterator<ObjectLocated> iterator() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ObjectLocated> getAllObjectLocated() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectLocated getObjectLocated(Integer ID) { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAllObjects() { // TODO Auto-generated method stub
		return false;
	}

	@Override
	public void forEachAdjacents(NodeIsom<Distance> node,
			BiConsumer<NodeIsom<Distance>, Distance> adjacentDistanceConsumer) {}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		// TODO Auto-generated method stub
		return null;
	}

	//

	//

	protected class ChunkMISOM extends MatrixInSpaceObjectsManager<Distance> {
		private static final long serialVersionUID = 1L;
		protected final Integer IDChunk;
		protected final Map<Integer, ChunkMISOM> adjacents;

		public ChunkMISOM(boolean isLazyNodeInstancing, int width, int height, NumberManager<Distance> weightManager) {
			super(isLazyNodeInstancing, width, height, weightManager);
			this.IDChunk = idChunkProgressive++;
			adjacents = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
					Comparators.INTEGER_COMPARATOR);
		}

		@Override
		public NodeIsom<Distance> newNodeMatrix(int x, int y) { return ISOMRaggedMatrix.this.newNodeMatrix(x, y); }

		public void addAdjacent(ChunkMISOM c) {
			if (c == null || c == this || adjacents.containsKey(c.IDChunk))
				return;
			adjacents.put(c.IDChunk, c);
		}

		@Override
		public void forEachNode(Consumer<NodeIsom<Distance>> action) { // TODO Auto-generated method stub
		}
	}

	//

	// TODO SpaceSubdivision

	/**
	 * Representation of the space represented by the super class.<br>
	 * That space is formed by chunks, who are organized as supergroups
	 *
	 * to compute what chunk is containing a particular point
	 */
	protected class ChunckManager {

	}
}