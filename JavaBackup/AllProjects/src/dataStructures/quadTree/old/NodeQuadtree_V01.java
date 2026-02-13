package dataStructures.quadTree.old;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/**
 * Node of a Quadtree implementation. <br>
 * It allow to associate to each Point2D some data (whose type is the generics
 * of this class).
 */
public class NodeQuadtree_V01<P extends Point2D, D> implements Serializable {
	private static final long serialVersionUID = -32014411458502L;

	public static final int SUBDIVISIONS_AMOUNT = 4;

	public static enum PointAdditionStatus {
		InThisNode, InSubsectionsOnly, OutOfBounds
	}

	protected int amountPointsStored = 0;
	protected final int maxPointsPerSubsection;
	protected final SubsectionData_V01 boundingBox;
	protected P[] points = null;
	protected Object[] dataPoints = null;
	protected NodeQuadtree_V01<P, D> subsections[] = null, father;
	protected final Function<Integer, P[]> pointsArrayProducer;

	public NodeQuadtree_V01(Function<Integer, P[]> pointsArrayProducer, NodeQuadtree_V01<P, D> father,
			int maxPointsPerSubsection, SubsectionData_V01 sd) {
		super();
		Objects.requireNonNull(pointsArrayProducer);
		this.pointsArrayProducer = pointsArrayProducer;

		if (maxPointsPerSubsection <= 0) {
			throw new IllegalArgumentException(
					"maxPointsPerSubsection ( " + maxPointsPerSubsection + " ) must have be greater than zero.");
		}
		this.maxPointsPerSubsection = maxPointsPerSubsection;

		if (sd == null) {
			throw new IllegalArgumentException("the data about the bounding box can't be null");
		}
		if ((sd.height <= 0.0) || (sd.width <= 0.0)) {
			throw new IllegalArgumentException("width ( " + sd.width + " ) must be greater than zero.");
		}
		sd.setDepth((father == null) ? 0 : father.getDepth() + 1);
		this.boundingBox = sd;

		this.father = father;
		this.amountPointsStored = 0;
		this.points = null;
		this.subsections = null;
	}

	// getters-setters

	/**
	 * @return the amountPointsStored
	 */
	public int getAmountPointsStored() {
		return amountPointsStored;
	}

	/**
	 * @return the the maximum amount of points this node can store before its
	 *         manager class has to subdivide itd
	 */
	public int getMaxPointsPerSubsection() {
		return maxPointsPerSubsection;
	}

	/**
	 * development-only
	 *
	 * @return the subsections
	 */
	protected NodeQuadtree_V01<P, D>[] getSubsections() {
		return subsections;
	}

	/**
	 * @return the points
	 */
	public P[] getPoints() {
		return points;
	}

	/**
	 * @return the dataPoints
	 */
	public Object[] getDataPoints() {
		return dataPoints;
	}

	// SETTER

	/*
	 * @param subsections the subsections to set
	 */
	public void setSubsections(NodeQuadtree_V01<P, D>[] subsections) {
		this.subsections = subsections;
	}

	// PUBLIC METHODS

	public boolean hasSubsections() {
		return this.subsections != null;
	}

	public boolean isLeaf() {
		return this.subsections == null;
	}

	public boolean isInside(P p) {
		if (p == null) {
			return false;
		}
		return this.boundingBox.isInside(p);
	}

	/***
	 * Tells if the given
	 *
	 * @param p
	 * @return
	 */
	public PointAdditionStatus canPointBeAdded(P p) {
		if (p == null) {
			return null;
		}
		if (!this.isInside(p)) {
			return PointAdditionStatus.OutOfBounds;
		}
		if (this.amountPointsStored >= this.maxPointsPerSubsection || this.subsections != null) {
			return PointAdditionStatus.InSubsectionsOnly;
		}
		return PointAdditionStatus.InThisNode;
	}

	/**
	 * Note: if the returned value is negative, then either the given point is
	 * <code>null</code> or this subsection is full (see
	 * {@link #getMaxPointsPerSubsection()}
	 *
	 * @param p    P to be added
	 * @param data data associated with the point
	 * @return index at which the P was stored, if it was possible, <code>-1</code>
	 *         otherwise.
	 */
	public int addPoint(P p, D data) {
		int i;
		if (this.amountPointsStored >= this.maxPointsPerSubsection || p == null) {
			return -1;
		}

		if ((i = this.amountPointsStored++) == 0) {
			this.points = this.pointsArrayProducer.apply(this.maxPointsPerSubsection);
			this.dataPoints = new Object[this.maxPointsPerSubsection];
		}
		this.points[i] = p;
		this.dataPoints[i] = data;
		return i;
	}

	/**
	 * Returns the P stored at the given index, if any.
	 *
	 * @param index the index of the desired point
	 * @return
	 */
	public P getPointAt(int index) {
		if (index < 0 || index >= this.amountPointsStored) {
			return null;
		}
		return this.points[index];
	}

	/**
	 * Similar to {@link #getPointAt(int)}, but returns the associated data instead.
	 *
	 * @param index the index of the desired point's data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public D getDataAt(int index) {
		if (index < 0 || index >= this.amountPointsStored) {
			return null;
		}
		return (D) this.dataPoints[index];
	}

	public NodeQuadtree_V01<P, D> getSubsectionAt(int index) {
		NodeQuadtree_V01<P, D>[] ss;
		if (index < 0 || (ss = this.subsections) == null || index >= ss.length) {
			return null;
		}
		return this.subsections[index];
	}

	//

	public void clearNode() {
		/*
		 * Garbage Collector will do the work if (subsections != null) { for
		 * (NodeQuadtree<P,D> n : this.subsections) { if (n != null) { n.clearNode(); }
		 * } }
		 */
		this.subsections = null;
		this.points = null;
		this.dataPoints = null;
		this.amountPointsStored = 0;
	}

	@SuppressWarnings("unchecked")
	public void forEachPointData(PointDataConsumers<P, D> action) {
		if (this.points != null && this.isLeaf()) {
			for (int i = 0, l = this.amountPointsStored; i < l; i++) {
				action.action(this.points[i], (D) this.dataPoints[i], i, this.getDepth());
			}
		} else {
			if (this.subsections != null) {
				NodeQuadtree_V01<P, D> s;
				for (int i = 0, l = this.subsections.length; i < l; i++) {
					s = this.subsections[i];
					if (s != null) {
						s.forEachPointData(action);
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		this.toString(sb);
		return sb.toString();
	}

	public void toString(StringBuilder sb) {
		String preTab;
		{
			StringBuilder ptsb = new StringBuilder(getDepth());
			for (int d = this.getDepth(); d > 0; d--) {
				ptsb.append('\t');
			}
			preTab = ptsb.toString();
		}
		sb.append(preTab).append('{').append('\n');
		sb.append(preTab).append("depth: ").append(getDepth()).append(',').append('\n');
		sb.append(preTab).append("boundingBox: ").append(boundingBox);
		if (this.isLeaf()) {
			int l;
			P p, pp[];
			Object oo[];
			sb.append(',').append('\n').append(preTab).append("amountPointsStored: ").append(amountPointsStored);
			sb.append(',').append('\n').append(preTab).append("points; [");

			pp = this.points;
			l = pp == null ? 0 : this.amountPointsStored;
			if (l > 0) {
				p = pp[0];
				sb.append("{x: ").append(p.getX()).append(", y: ").append(p.getY()).append('}');
				for (int i = 1; i < l; i++) {
					p = pp[i];
					sb.append(", {x: ").append(p.getX()).append(", y: ").append(p.getY()).append('}');
				}
			}

			sb.append("],\n").append(preTab).append("data: ");
			if ((oo = this.dataPoints) != null) {
				sb.append(String.valueOf(oo[0]));
				for (int i = 1; i < l; i++) {
					sb.append(',').append(' ').append(String.valueOf(oo[i]));
				}
			} else {
				sb.append('[').append(']');
			}
			sb.append('\n');
		} else {
			int l;
			NodeQuadtree_V01<P, D> s, ss[];
			ss = this.subsections;
			l = ss == null ? 0 : ss.length;
			sb.append(',').append('\n').append(preTab).append("subsections; [");
			if (l > 0) {
				s = ss[0];
				if (s != null) {
					s.toString(sb.append('\n'));
				}
				for (int i = 1; i < l; i++) {
					s = ss[i];
					if (s != null) {
						sb.append(',').append('\n');
						s.toString(sb);
					}
				}
			}
			sb.append('\n').append(preTab).append(']').append('\n');
		}
		sb.append(preTab).append('}');
	}

	//

	//

	//

	/**
	 * @return
	 * @see dataStructures.quadTree.NodeQuadtree.SubsectionData#getxBottomLeft()
	 */
	public double getxBottomLeft() {
		return boundingBox.getxBottomLeft();
	}

	/**
	 * @return
	 * @see dataStructures.quadTree.NodeQuadtree.SubsectionData#getyBottomLeft()
	 */
	public double getyBottomLeft() {
		return boundingBox.getyBottomLeft();
	}

	/**
	 * @return
	 * @see dataStructures.quadTree.NodeQuadtree.SubsectionData#getHeight()
	 */
	public double getHeight() {
		return boundingBox.getHeight();
	}

	/**
	 * @return
	 * @see dataStructures.quadTree.NodeQuadtree.SubsectionData#getWidth()
	 */
	public double getWidth() {
		return boundingBox.getWidth();
	}

	/**
	 * @return
	 * @see dataStructures.quadTree.NodeQuadtree.SubsectionData#getDepth()
	 */
	public int getDepth() {
		return boundingBox.getDepth();
	}

	/**
	 * @param xBottomLeft
	 * @see dataStructures.quadTree.NodeQuadtree.SubsectionData#setxBottomLeft(double)
	 */
	public void setxBottomLeft(double xBottomLeft) {
		boundingBox.setxBottomLeft(xBottomLeft);
	}

	/**
	 * @param yBottomLeft
	 * @see dataStructures.quadTree.NodeQuadtree.SubsectionData#setyBottomLeft(double)
	 */
	public void setyBottomLeft(double yBottomLeft) {
		boundingBox.setyBottomLeft(yBottomLeft);
	}

	/**
	 * @param height
	 * @see dataStructures.quadTree.NodeQuadtree.SubsectionData#setHeight(double)
	 */
	public void setHeight(double height) {
		boundingBox.setHeight(height);
	}

	/**
	 * @param width
	 * @see dataStructures.quadTree.NodeQuadtree.SubsectionData#setWidth(double)
	 */
	public void setWidth(double width) {
		boundingBox.setWidth(width);
	}

	//

	public static class SubsectionData_V01 implements Serializable {
		private static final long serialVersionUID = 452156050523789L;
		public final int indexSubsection;
		public int depth;
		public double xBottomLeft, yBottomLeft, height, width;

		public SubsectionData_V01(double xBottomLeft, double yBottomLeft, double width, double height) {
			this(xBottomLeft, yBottomLeft, width, height, 0, 0);
		}

		public SubsectionData_V01(double xBottomLeft, double yBottomLeft, double width, double height, int indexSubsection,
				int depth) {
			super();
			this.indexSubsection = indexSubsection;
			this.xBottomLeft = xBottomLeft;
			this.yBottomLeft = yBottomLeft;
			this.height = height;
			this.width = width;
			this.depth = depth;
		}

		//

		/**
		 * @return the xBottomLeft
		 */
		public double getxBottomLeft() {
			return xBottomLeft;
		}

		/**
		 * @return the yBottomLeft
		 */
		public double getyBottomLeft() {
			return yBottomLeft;
		}

		/**
		 * @return the height
		 */
		public double getHeight() {
			return height;
		}

		/**
		 * @return the width
		 */
		public double getWidth() {
			return width;
		}

		/**
		 * @return the depth
		 */
		public int getDepth() {
			return depth;
		}

		//

		/**
		 * @param xBottomLeft the xBottomLeft to set
		 */
		public void setxBottomLeft(double xBottomLeft) {
			this.xBottomLeft = xBottomLeft;
		}

		/**
		 * @param yBottomLeft the yBottomLeft to set
		 */
		public void setyBottomLeft(double yBottomLeft) {
			this.yBottomLeft = yBottomLeft;
		}

		/**
		 * @param height the height to set
		 */
		public void setHeight(double height) {
			this.height = height;
		}

		/**
		 * @param width the width to set
		 */
		public void setWidth(double width) {
			this.width = width;
		}

		/**
		 * @param depth the depth to set
		 */
		public void setDepth(int depth) {
			this.depth = depth;
		}

		public <P extends Point2D> boolean isInside(P p) {
			double px, py;
			if (p == null) {
				return false;
			}
			px = p.getX();
			py = p.getY();
			if (px < this.xBottomLeft || py < this.yBottomLeft || px > (this.xBottomLeft + this.width)
					|| py > (this.yBottomLeft + this.height)) {
				return false;
			}
			return true;
		}

		public boolean intersects(SubsectionData_V01 r) {
			double x1 = Math.max(this.xBottomLeft, r.xBottomLeft);
			double y1 = Math.max(this.yBottomLeft, r.yBottomLeft);
			double x2 = Math.min(this.xBottomLeft + this.width, r.xBottomLeft + r.width);
			double y2 = Math.min(this.yBottomLeft + this.height, r.yBottomLeft + r.height);
			return !((x2 - x1) <= 0.0 || (y2 - y1) <= 0.0);
		}

		///

		@Override
		public String toString() {
			return "{SubsectionData : {bottomLeftPoint: {x:" + xBottomLeft + ", y:" + yBottomLeft + "}, size: {width: "
					+ width + ", height: " + height + "}, indexSubsection:" + indexSubsection + "}}";
		}
	}

	//

	/**
	 * Ssee {@link PointDataConsumers#action(P, Object, int, int)}.
	 * 
	 * @author marcoottina
	 *
	 * @param <T>
	 */
	public static interface PointDataConsumers<P extends Point2D, T> {
		/**
		 * Defines an action for a given point, its associated data (if any,
		 * <code>null</code> otherwise), that point's index inside the containing
		 * subsection node and the depth of that node.
		 * 
		 * @param point current point
		 * @param data  data associated with the point, if any (<code>null</code>
		 *              otherwise)
		 * @param the   subsection node holds the point at this index
		 * @param depth of the subsection node holding the point
		 */
		public void action(P point, T data, int index, int depth);
	}
}
