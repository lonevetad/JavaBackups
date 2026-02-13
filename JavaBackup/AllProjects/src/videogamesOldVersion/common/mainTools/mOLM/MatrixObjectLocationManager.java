package videogamesOldVersion.common.mainTools.mOLM;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.List;

import tools.ObjectWithID;
import videogamesOldVersion.common.FullReloadEnvironment;
import videogamesOldVersion.common.PainterMolm;
import videogamesOldVersion.common.abstractCommon.Memento;
import videogamesOldVersion.common.abstractCommon.referenceHolderAC.ShapeSpecificationHolder;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractPathFinder;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractShapeRunners;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.LoggerMessages;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.PainterMOLMNullItem;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;

/**
 * Implementation of {@link AbstractMatrixObjectLocationManager}.
 * <p>
 * In C# is called GOLIPM (Game Object Location Intersection Pathfinding
 * Manager).
 */
@SuppressWarnings("deprecation")
public class MatrixObjectLocationManager implements AbstractMatrixObjectLocationManager/* , ShapeRunners */ {

	private static final long serialVersionUID = -130816751145464148L;
	protected static final double justOne = 1.0, sqrtTwo = /* Math.max(justOne + 0.5, */Math.sqrt(2)// )//
	;

	public static enum OperationOnShape {
		Add, Remove, Replace, Collect;
	}

	public enum COLORS_VISIT {
		WHITE, GRAY, BLACK;
	}

	/**
	 * Il senso delle coordinate è orario MA l'o rigine degli assi cartesiani
	 * è*"in alto a sinistra","top-left".
	 */
	public static enum CoordinatesDeltaForAdjacentNodes {
		// TOP(0, -1), RIGHT(+1, 0), BOTTOM(0, 1), LEFT(-1, 0);
		NORD(0, -1, justOne), SUD(0, 1, justOne), OVEST(-1, 0, justOne), EST(1, 0, justOne)//
		, NORD_EST(1, -1, sqrtTwo), SUD_EST(1, 1, sqrtTwo), SUD_OVEST(-1, 1, sqrtTwo), NORD_OVEST(-1, -1, sqrtTwo)//
		;

		final int dx, dy;
		final double weight;

		CoordinatesDeltaForAdjacentNodes(int dxx, int dyy, double w) {
			dx = dxx;
			dy = dyy;
			this.weight = w;
		}
	}

	public static enum CrossDirections {
		RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0), UP(0, -1);
		final int dx, dy, index;

		CrossDirections(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
			index = SF.n++;
		}

		private static class SF {
			static int n = 0;
		}
	}

	public static final CoordinatesDeltaForAdjacentNodes[] valuesCDFAN = CoordinatesDeltaForAdjacentNodes.values();
	public static final CrossDirections[] valuesCrossDirections = CrossDirections.values();
	public static final int LENGTH_valuesCDFAN = valuesCDFAN.length,
			LENGTH_valuesCrossDirections = valuesCrossDirections.length//
			, DEFAULT_WIDTH = 64, DEFAULT_HEIGHT = DEFAULT_WIDTH;

	public MatrixObjectLocationManager() {
		// this(ShapeRunners.getInstance());
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public MatrixObjectLocationManager(int width, int height) {
		// this.shapeRunners = sr;
		super();
		this.width = this.height = 0;
		setLog(null);
		resizeMatrix(width, height);
		// reallocMatrixMap();
		counterRunPathFind = 0;
	}

	protected MatrixObjectLocationManager(MementoMOLM m, FullReloadEnvironment fre) {
		super();
		reloadState(m, fre);
	}

	//

	// TODO FIELDS

	int width, height;
	long counterRunPathFind;
	NodeMatrix[][] matrix;
	transient AbstractShapeRunners/* _WithCoordinates */ shapeRunners;
	transient AbstractPathFinder pathFinder;
	transient LoggerMessages log;

	//

	// TODO public methods

	@Override
	public void resetDefaultInstances() {
		this.shapeRunners = ShapeRunners.getInstance();
		this.pathFinder = PathFinderOLD.getInstance();
		this.log = LoggerMessages.LOGGER_DEFAULT;
	}

	// getter

	@Override
	public LoggerMessages getLog() {
		return log;
	}

	public ObjectWithID getAt(int xx, int yy) {
		NodeMatrix n;
		n = getNodeMatrix(xx, yy);
		return n == null ? null : n.getItem();
	}

	@Override
	public int getWidthMicropixel() {
		return width;
	}

	public int getWidth() {
		return getWidthMicropixel();
	}

	@Override
	public int getHeightMicropixel() {
		return height;
	}

	public int getHeight() {
		return getHeightMicropixel();
	}

	@Override
	public NodeMatrix getNewNodeMatrix() {
		return getNewNodeMatrix(0, 0);
	}

	@Override
	public NodeMatrix getNewNodeMatrix(int xx, int yy) {
		return new NodeMatrix_MorePrecisePathLength(xx, yy);
	}

	public long getCounterRunPathFind() {
		return counterRunPathFind;
	}

	public long getCounterRunPathFind_ForNewPathfindRun() {
		return ++counterRunPathFind;
	}

	@Override
	public AbstractShapeRunners getShapeRunners() {
		return shapeRunners;
	}

	@Override
	public AbstractPathFinder getPathFinder() {
		return pathFinder;
	}

	// setter

	@Override
	public MatrixObjectLocationManager setLog(LoggerMessages log) {
		this.log = LoggerMessages.loggerOrDefault(log);
		return this;
	}

	@Override
	public MatrixObjectLocationManager setShapeRunners(AbstractShapeRunners shapeRunners) {
		this.shapeRunners = AbstractShapeRunners.getOrDefault(shapeRunners);
		return this;
	}

	@Override
	public MatrixObjectLocationManager setPathFinder(AbstractPathFinder pathFinder) {
		this.pathFinder = AbstractPathFinder.getOrDefault(pathFinder);
		return this;
	}

	//

	// TODO FOR EACH

	@Override
	public void forEach(DoSomethingWithNode dswn) {
		int r, c, h, w;
		NodeMatrix[] row, m[];
		h = height;
		w = width;
		m = matrix;
		r = -1;
		while(++r < h && dswn.canContinueCycling()) {
			c = -1;
			row = m[r];
			while(++c < w && dswn.canContinueCycling()) {
				dswn.doOnNode(this, row[c], c, r);
			}
		}
	}
//
//	public void forEach(DoSomethingWithCoordinates dswc) {
//		forEach_OptimizedForRows(dswc);
//	}
//
//	public void forEach_OptimizedForRows(DoSomethingWithCoordinates dswc) {
//		int r, c, h, w;
//		NodeMatrix[] row, m[];
//
//		h = height;
//		w = width;
//		m = matrix;
//		r = -1;
//		while (++r < h && dswc.canContinueCycling()) {
//			c = -1;
//			row = m[r];
//			while (++c < w && dswc.canContinueCycling()) {
//				// dswc.doOnCoordinates(m, c, r);
//				dswc.doOnRow(row, c, r);
//			}
//		}
//	}
//
//	public void forEach_Coordinates(DoSomethingWithCoordinates dswc) {
//		int r, c, h, w;
//		NodeMatrix[] m[];
//		h = height;
//		w = width;
//		m = matrix;
//		r = -1;
//		while (++r < h && dswc.canContinueCycling()) {
//			c = -1;
//			// row = m[r];
//			while (++c < w && dswc.canContinueCycling()) {
//				dswc.doOnCoordinates(m, c, r);
//			}
//		}
//	}

	//

	// TODO PAINTING ON GRAPHICS

	public void paintOn(Graphics g) {
		paintOn(g, null);
	}

	public void paintOn(Graphics g, PainterMOLMNullItem pmoni) {
		forEach(new PainterMolm(g, pmoni));
	}

	//

	// TODO PATH-FINDING

	@Override
	public List<Point> extractShortestPath(int xStart, int yStart, int xDest, int yDest) {
		AbstractPathFinder apf;
		apf = getPathFinder();
		if (apf == null) {
			log.log("ERROR: on extractShortestPath, cannot extract path because pathfinder null");
			return null;
		} else
			return pathFinder.findPath(this, xStart, yStart, xDest, yDest);
	}

	@Override
	public List<Point> extractShortestPath(int xStart, int yStart, int xDest, int yDest, ShapeSpecification ss) {
		AbstractPathFinder apf;
		apf = getPathFinder();
		if (apf == null) {
			log.log("ERROR: on extractShortestPath, cannot extract path because pathfinder null");
			return null;
		} else
			return pathFinder.findPath(this, xStart, yStart, xDest, yDest, ss);
	}

	@Override
	public Polygon boundingPolygon(int x, int y, int width, int height) {
		return BoundingPolygonExtractor.boundingPolygon(this, x, y, width, height);
	}

	//

	// TODO modifier

	@Override
	public String addOnShape(ShapeSpecification ss, ObjectWithID toBeAdded, boolean removePrevIfShapeSpecHolder) {
		return runOnShape(ss, new Replacer(this, toBeAdded, null, true, removePrevIfShapeSpecHolder));
	}
	// ObjectShaped

	/*
	 * public String removeOnShape(ShapeSpecification ss, ObjectWithID toBeRemoved,
	 * boolean removePrevIfShapeSpecHolder) { return runOnShape(ss, new
	 * Replacer(this, null, toBeRemoved, true, removePrevIfShapeSpecHolder)); }
	 */

	@Override
	public String replaceOnShape(ShapeSpecification ss, ObjectWithID toBeAdded, ObjectWithID toBeRemoved,
			boolean removePrevIfShapeSpecHolder) {
		if (toBeAdded == toBeRemoved)
			return null;
		return runOnShape(ss, new Replacer(this, toBeAdded, toBeRemoved, false, removePrevIfShapeSpecHolder));
	}

	@Override
	public CollectedObject collectOnShape(ShapeSpecification ss) {
		return collectOnShape(ss, ItemCollector.COLLECT_ALL);
	}

	public CollectedObject collectOnShape(ShapeSpecification ss, int threshold) {
		String textError;
		ItemCollector col;
		col = new ItemCollector(threshold);
		textError = runOnShape(ss, col);
		if (textError == null)
			return (col);
		else
			log.log(textError);
		return null;
	}

	// overload
	@Override
	public CollectedObject collectOnShapeReferringTo(ShapeSpecification ss, ObjectWithID whoMustNotBeCollected) {
		return collectOnShapeReferringTo(ss, whoMustNotBeCollected, ItemCollector.COLLECT_ALL);
	}

	// here we are
	public CollectedObject collectOnShapeReferringTo(ShapeSpecification ss, ObjectWithID whoMustNotBeCollected,
			int threshold) {
		if (/* collectOnSame_ValueNullitySolidity && */ whoMustNotBeCollected == null)
			return collectOnShape(ss, threshold);
		return collectOnShapeReferringTo(new CollectorObstacles(whoMustNotBeCollected, threshold), ss);
	}

	public CollectedObject collectOnShapeReferringTo(ItemCollector col, ShapeSpecification ss) {
		String textError;
		if (col == null || ss == null)
			return null;
		// col.clear();
		textError = runOnShape(ss, col);
		if (textError == null)
			return col;
		return null;
	}

	@Override
	public String runOnShape(ShapeSpecification ss, DoSomethingWithNode dswc) {
		StringBuilder textError;

		if (this.matrix == null)
			return "ERROR: on runOnShape, matrix is null";

		if (ss != null && dswc != null && shapeRunners != null) {
			return this.shapeRunners.runOnShape(this, ss, dswc);
		}
		// else error
		textError = new StringBuilder();
		if (ss == null)
			textError.append("ShapesImplemented null");
		if (dswc == null) {
			if (textError.length() > 0)
				textError.append(", ");
			textError.append("DoSomethingWithNode null");
		}
		if (shapeRunners == null) {
			if (textError.length() > 0)
				textError.append(", ");
			textError.append("shapeRunners null");
		}

		return textError.length() == 0 ? null : textError.toString();
	}

	/** BEWARE : it cleans the collector before collect. */
	public boolean isThereSomeoneOnShape(ItemCollector ic, ShapeSpecification ss) {
		return howManyOnShape(ic, ss) > 0;
	}

	/*
	 * new Collector_SameNullVal_Or_Solidity(whoMustNotBeCollected, threshold,
	 * collectOnSame_ValueNullitySolidity)
	 */

	@Override
	public int howManyOnShape(ShapeSpecification ss, ObjectWithID whoMustNotBeCollected) {
		return howManyOnShape(
				new CollectorObstacles(whoMustNotBeCollected, ItemCollector.COLLECT_FIRST_FOUND_TO_CHECK_EMPITY)
						.setCollectIfBothNull(false).setMustCollectNulls(true),
				ss);
	}

	public int howManyOnShape(ItemCollector ic, ShapeSpecification ss) {
		CollectedObject collectedStuffs;
		if (ss == null || ic == null)
			return -1;
		ic.clear();
		collectedStuffs = collectOnShapeReferringTo(ic, ss);
		if (collectedStuffs == null) {
			log.log("ERROR: on isThereSomeoneOnShape, collectedStuffs is null");
			return -1;
		}
		return collectedStuffs.howManyCollected();
		// collectedObjects.size() > 0);
	}

	//

	@Override
	public void clearMatrix() {
		int r, c, h, w;
		NodeMatrix[] row, m[];
		// forEach(MatrixCleaner.getInstance());
		/* I could recycle, but this way I optimize */
		h = height;
		w = width;
		m = matrix;
		r = -1;
		while(++r < h) {
			c = -1;
			row = m[r];
			while(++c < w) {
				row[c].item = null;
			}
		}
	}

	public boolean removeCompletly(ObjectWithID o) {
		if (o != null) {
			forEach(new Remover(o));
			return true;
		}
		return false;
	}

	@Override
	public boolean resizeMatrix(int width, int height) throws IllegalArgumentException {
		boolean wGrown, hGrown;
		int oldw, oldh, r, c, rows, columns;
		NodeMatrix[] rowNew, mNew[], rowOLD, mOLD[];

		if (width < 1 || height < 1)
			throw new IllegalArgumentException("Invalid dimensions: width: " + width + ", height: " + height);

		if (this.width == 0 || this.height == 0) {
			this.width = width;
			this.height = height;
			reallocMatrixMap();
			return true;
		} else if (this.width != width || this.height != height) {
			oldw = this.width;
			oldh = this.height;
			this.width = width;
			this.height = height;
			wGrown = oldw < width;
			hGrown = oldh < height;

			mOLD = matrix;
			mNew = new NodeMatrix[height][width];

			// rettangolo-intersezione
			rows = Math.min(oldh, height);
			columns = Math.min(oldw, width);
			r = -1;
			while(++r < rows) {
				c = -1;
				rowNew = mNew[r];
				rowOLD = mOLD[r];
				while(++c < columns) {
					// rowNew[c] = this.getNewNodeMatrix(c, r);
					rowNew[c] = rowOLD[c];
				}
			}
			// rettangoli-crescita, se esistono
			if (hGrown) {
				r--;
				columns = width;
				while(++r < height) {
					c = -1;
					rowNew = mNew[r];
					while(++c < columns) {
						rowNew[c] = this.getNewNodeMatrix(c, r);
					}
				}
			}
			if (wGrown) {
				/*
				 * qui bisogna sempre e comunque fare il numero minimo di colonne tra quelle
				 * precedenti e quelle nuove, perche le colonne nuove aggiunte sono state fatte
				 * nell'if precedente
				 */
				r = -1;
				while(++r < rows) {
					c = columns - 1;
					rowNew = mNew[r];
					while(++c < width) {
						rowNew[c] = this.getNewNodeMatrix(c, r);
					}
				}
			}
			// else : reduced size, all ok

			matrix = mNew;
			return true;
		}
		return false;
	}

	//

	// TODO static methods

	public static MatrixObjectLocationManager newInstance() {
		return newInstance(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public static MatrixObjectLocationManager newInstance(int w, int h) {
		return new MatrixObjectLocationManager(w, h);
	}

	public static MatrixObjectLocationManager newDefaultInstance() {
		return newDefaultInstance(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public static MatrixObjectLocationManager newDefaultInstance(int w, int h) {
		return MatrixObjectLocationManager.newInstance(w, h).setPathFinder(PathFinderOLD.getInstance())
				.setShapeRunners(ShapeRunners.getInstance());
	}

	//

	// TODO other methods

	/** BEWARE: Very heavy operation */

	@Override
	public void reallocMatrixMap(int width, int height) throws IllegalArgumentException {
		int r, c;
		NodeMatrix[] row, m[];
		if (width < 1 || height < 1)
			throw new IllegalArgumentException("Incorrect MOLM new size: width: " + width + ", height: " + height);
		matrix = m = new NodeMatrix[this.height = height][this.width = width];
		r = -1;
		while(++r < height) {
			c = -1;
			row = m[r];
			while(++c < width) {
				row[c] = this.getNewNodeMatrix(c, r);
			}
		}
	}

	@Override
	public NodeMatrix[][] getMatrix() {
		return matrix;
	}

	@Override
	public NodeMatrix getNodeMatrix(int xx, int yy) {
		NodeMatrix n;
		n = null;
		if (xx >= 0 && yy >= 0 && xx < width && yy < height) {
			n = matrix[yy][xx];
		}
		return n;
	}

	//

	// TODO CLASSES

	public class NodeMatrix_MorePrecisePathLength extends NodeMatrix {

		private static final long serialVersionUID = 21389409302007777L;

		public NodeMatrix_MorePrecisePathLength() {
			super();
		}

		public NodeMatrix_MorePrecisePathLength(int xOfThisNode, int yOfThisNode) {
			super(xOfThisNode, yOfThisNode);
		}

		int horizontalVerticalSteps, obiqueSteps;

		@Override
		protected void doOnReinitialization() {
			horizontalVerticalSteps = obiqueSteps = 0;
		}

		@Override
		public void copyPathFrom(NodeMatrix from) {
			NodeMatrix_MorePrecisePathLength n;
			if (from != null) {
				super.copyPathFrom(from);
				if (from instanceof NodeMatrix_MorePrecisePathLength) {
					n = (NodeMatrix_MorePrecisePathLength) from;
					horizontalVerticalSteps = n.horizontalVerticalSteps;
					obiqueSteps = n.obiqueSteps;
				}
			}
		}

		@Override
		public void addArcWeight(NodeMatrix from, CoordinatesDeltaForAdjacentNodes arc) {
			int t;// optimize microcode avoiding reloading "this" too many
					// times
			if (from != null && arc != null) {
				copyPathFrom(from);
				if (arc.weight == MatrixObjectLocationManager.justOne)
					horizontalVerticalSteps++;
				else
					obiqueSteps++;
				this.fullPathLength = horizontalVerticalSteps + ((t = obiqueSteps) == 0 ? 0 :
				// (Math.sqrt((t * t) << 1))
						(t * MatrixObjectLocationManager.sqrtTwo));
			}
		}
	}

	// RUNNERS

	/*
	 * protected static class RunnerRectangle implements ShapeRunner {
	 *
	 * @Override public boolean runArea(NodeMatrix[][] mm, ShapeSpecification gs,
	 * DoSomethingWithCoordinates dswc) { SS_Rectangular ssr; if (gs instanceof
	 * SS_Rectangular) { ssr = (SS_Rectangular) gs; } return false; } }
	 */

	//

	protected static abstract class DSWN_Generic implements DoSomethingWithNode, Serializable {
		private static final long serialVersionUID = 32302178814L;

		@Override
		public Object doOnItem(AbstractMatrixObjectLocationManager molm, ObjectWithID item, int x, int y) {
			return null;
		}
	}

	public static class MatrixCleaner extends DSWN_Generic {

		private static final long serialVersionUID = -1111111561096L;
		protected static MatrixCleaner instance;

		protected MatrixCleaner() {
			super();
		}

		public static MatrixCleaner getInstance() {
			if (instance == null)
				instance = new MatrixCleaner();
			return instance;
		}

		@Override
		public Object doOnNode(AbstractMatrixObjectLocationManager molm, NodeMatrix node, int x, int y) {
			if (node != null)
				node.item = null;
			return null;
		}

		@Override
		public Object doOnItem(AbstractMatrixObjectLocationManager molm,
				videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID item, int x, int y) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	protected static class Remover extends DSWN_Generic {
		private static final long serialVersionUID = 5130698778L;

		boolean shouldReplaceAll;
		ObjectWithID itemToBeRemoved, itemToBeAdded;

		public Remover(ObjectWithID itemToBeRemoved) {
			this(true, itemToBeRemoved, null);
		}

		public Remover(boolean shouldReplaceAll, ObjectWithID itemToBeRemoved, ObjectWithID itemToBeAdded) {
			super();
			this.shouldReplaceAll = shouldReplaceAll;
			this.itemToBeRemoved = itemToBeRemoved;
			this.itemToBeAdded = itemToBeAdded;
		}

		@Override
		public Object doOnNode(AbstractMatrixObjectLocationManager molm, NodeMatrix node, int x, int y) {
			if (shouldReplaceAll || node.item == itemToBeRemoved) {
				node.item = itemToBeAdded;
			}
			return null;
		}

		@Override
		public Object doOnItem(AbstractMatrixObjectLocationManager molm,
				videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID item, int x, int y) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	/**
	 * f {@link Replacer#itemToBeRemoved} is NOT null, then this implementation
	 * replaces each instance of {@link Replacer#itemToBeRemoved} with
	 * {@link Replacer#itemToAdd}.<b>Otherwise, this implementation replaces
	 * everything it method-user founds with {@link Replacer#itemToAdd}, no matter
	 * what it was before.<br>
	 * No other checks (null-check included) are performed.
	 */
	protected static class Replacer extends DSWN_Generic {
		private static final long serialVersionUID = 5130698777L;

		boolean shouldReplaceAll, removePrevIfShapeSpecHolder;
		ObjectWithID itemToBeRemoved, itemToBeAdded;
		MatrixObjectLocationManager molm;

		protected Replacer(MatrixObjectLocationManager molm, ObjectWithID itemToBeAdded, ObjectWithID itemToBeRemoved) {
			this(molm, itemToBeAdded, itemToBeRemoved, false);
		}

		protected Replacer(MatrixObjectLocationManager molm, ObjectWithID itemToBeAdded, ObjectWithID itemToBeRemoved,
				boolean shouldReplaceAll) {
			this(molm, itemToBeAdded, itemToBeRemoved, shouldReplaceAll, false);
		}

		protected Replacer(MatrixObjectLocationManager molm, ObjectWithID itemToBeAdded, ObjectWithID itemToBeRemoved,
				boolean shouldReplaceAll, boolean removePrevIfShapeSpecHolder) {
			this.itemToBeAdded = itemToBeAdded;
			this.itemToBeRemoved = itemToBeRemoved;
			this.shouldReplaceAll = shouldReplaceAll;

			/**
			 * if, before the replace operation, all objects lying inside the
			 * itemToBeAdded's area must be removed, respecting to their whole shape.<br>
			 * So, they must be collected and removed indipendently
			 */
			if (removePrevIfShapeSpecHolder && itemToBeAdded != null
					&& (itemToBeAdded instanceof ShapeSpecificationHolder)) {
				ShapeSpecification ss;
				CollectedObject co;
				Remover re;
				ss = ((ShapeSpecificationHolder) itemToBeAdded).getShapeSpecification();
				co = molm.collectOnShape(ss);
				if (co != null) {
					re = new Remover(false, null, null);
					co.forEach((i, o) -> {
						if ((o != itemToBeRemoved) && (o instanceof ShapeSpecificationHolder)) {
							re.itemToBeRemoved = o;
							molm.runOnShape(((ShapeSpecificationHolder) o).getShapeSpecification(), re);
						}
					});
				}
			}
		}

		/*
		 * public Object doOnRow_OptimizedIfShouldntReplaceAll(NodeMatrix[] a, int xx) {
		 * ObjectWithID o; NodeMatrix n; o = null; if ((n = a[xx]).item == itemToRemove
		 * || shouldReplaceAll) { o = itemToRemove; n.item = itemToPut; } return o; }
		 */

		@Override
		public Object doOnNode(AbstractMatrixObjectLocationManager molm, NodeMatrix node, int x, int y) {
			// ObjectWithID o;
			// o = node.item;
			if (shouldReplaceAll || node.item == itemToBeRemoved) {
				// o = itemToRemove;
				node.item = itemToBeAdded;
			}
			// return o;
			return null;
		}

		@Override
		public Object doOnItem(AbstractMatrixObjectLocationManager molm,
				videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID item, int x, int y) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	// TODO Collector_SameNullVal_Or_Solidity
	/**
	 * Classe tipicamente utilizzata durante il PathFind per percorrere percorsi "da
	 * me (whoIAm) percorribili", ossia percorsi in cui non si incontrano ostacoli
	 * ... per esempio, strisce di prato-strada-sterrato.ghiaia-sabbia-ecc hanno
	 * stessa percorribilita', quindi nel metodo
	 * {@link MatrixObjectLocationManager#isThereSomeoneInArea(ShapeSpecification, ShapeRunner_WithCoordinates)}
	 * bisognerebbe ignorare (quindi NON raccogliere) tutti gli oggetti "diversi"
	 * per percorribilita', ergo settare la variabile
	 * {@link CollectorObstacles#collectAll} to <code>false</code>.
	 */
	public static class CollectorObstacles extends ItemCollector {
		private static final long serialVersionUID = 897923266990L;
		final boolean isWhoIAmNotNull, isWhoIAmNotSolid;
		boolean collectIfBothNull;
		final ObjectWithID whoIAm;

		public CollectorObstacles(ObjectWithID whoIAm) {
			this(whoIAm, ItemCollector.COLLECT_ALL);
		}

		public CollectorObstacles(ObjectWithID whoIAm, int threshold) {
			super(threshold);
			// isWhoIAmNotSolid = ObjectWithID.extractIsNotSolid(whoIAm)
			isWhoIAmNotSolid = (isWhoIAmNotNull = (this.whoIAm = whoIAm) != null) ? whoIAm.isNotSolid() : true;
			collectIfBothNull = !isWhoIAmNotNull;
		}

		public boolean isWhoIAmNotNull() {
			return isWhoIAmNotNull;
		}

		public boolean isWhoIAmNotSolid() {
			return isWhoIAmNotSolid;
		}

		public boolean isCollectIfBothNull() {
			return collectIfBothNull;
		}

		public ObjectWithID getWhoIAm() {
			return whoIAm;
		}

		public CollectorObstacles setCollectIfBothNull(boolean collectIfBothNull) {
			this.collectIfBothNull = collectIfBothNull;
			if (collectIfBothNull) {
				mustCollectNulls = true;
			}
			return this;
		}

		/**
		 * An object can be collected if at least on of the following conditions are
		 * true:
		 * <ul>
		 * <li>all must be collected</li>
		 * <li>we are both {@code null} (<i>nulls object cannot pass through everywhere
		 * like ghosts, but just through null areas, like Magic the Gatherig 's Shadow
		 * ability: "A creature with shadow can block or be blocked by only creatures
		 * with shadow".</i>)</li>
		 * <li>we have different solidity</li>
		 * </ul>
		 * <p>
		 * Versione piu' compatta ma meno commentata di
		 * {@link #calculateIfCanAddToTree_ThrowingConsistencyException(ObjectWithID)}.
		 */
		@Override
		public boolean canBeCollected(ObjectWithID o) {
			boolean onull;
			onull = (o == null);
			return mustCollectAll || //
					(mustCollectNulls && (onull || (collectIfBothNull == (onull == isWhoIAmNotNull))))//
					|| ((whoIAm != o) && (isWhoIAmNotSolid == ObjectWithID.extractIsNotSolid(o)));
		}

	}// fine Collector_SameNullVal_Or_Solidity

	//

	// TODO MEMENTO

	@Override
	public boolean reloadState(Memento m) {
		MementoMOLM mmolm;
		if (m != null && m instanceof MementoMOLM) {
			mmolm = (MementoMOLM) m;
			AbstractMatrixObjectLocationManager.super.reloadState(mmolm);
			this.counterRunPathFind = mmolm.counterRunPathFind;
			return true;
		}
		return false;
	}

	@Override
	public MementoMOLM createMemento() {
		return new MementoMOLM(this);
	}

	// class

	public static class MementoMOLM extends AbstractMementoMOLM {
		private static final long serialVersionUID = -89526050825007L;
		long counterRunPathFind = 0;

		public MementoMOLM() {
			super();
		}

		public MementoMOLM(MatrixObjectLocationManager molm) {
			super(molm);
			this.counterRunPathFind = molm.counterRunPathFind;
			molm.fillMementoMOLM(this);
		}

		@Override
		public MatrixObjectLocationManager reinstanceFromMe(FullReloadEnvironment fre) {
			return new MatrixObjectLocationManager(this, fre);
		}
	}

	@Override
	public videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.Memento createMemento() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean reloadState(videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.Memento m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LoggerMessages getLog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollectedObject collectOnShapeReferringTo(ShapeSpecification ss,
			videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID whoMustNotBeCollected) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String replaceOnShape(ShapeSpecification ss,
			videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID toBeAdded,
			videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID toBeRemoved,
			boolean removePrevIfShapeSpecHolder) {
		// TODO Auto-generated method stub
		return null;
	}
}