package dataStructures.spaceSubdivisionTree;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;

import dataStructures.spaceSubdivisionTree.impl.FixedRectangularGridSpaceSubdivisions;

/**
 * Holds a set of space subdivisions used by
 * {@link SpaceSubdivisionsManagerTree}.<br>
 * The space could be divided in any way: in a circular way, hexagonal or
 * rectangular, as {@link FixedRectangularGridSpaceSubdivisions} does.
 */
public abstract class SpaceSubdivisions implements Serializable {
	private static final long serialVersionUID = 920464924056003666L;

	/**
	 * A simple factory to allow creating different subclasses to be instantiated
	 */
	public abstract SingleSpaceSubdivision newSingleSpaceSubdivision();

	/**
	 * Returns all of the {@link SingleSpaceSubdivision} included in this
	 * implementation.<br>
	 * Note: the list should be unmodifiable.
	 */
	public abstract List<SingleSpaceSubdivision> getSubdivisions();

	/**
	 * Returns the amount of subdivisions that could be created by this
	 * implementation.
	 */
	public int countSubdivisions() {
		return getSubdivisions().size();
	}

	/**
	 * Considering that the space is divided in adjacent subsections and those can
	 * be organized in a matrix-like form in some way, then this method should
	 * return the {@link SingleSpaceSubdivision} associated to the given
	 * coordinates. Those coordinates should be normalized and maybe also integers.
	 * <p>
	 * This should work under the assumption that the most top-left
	 * {@link SingleSpaceSubdivision} has <code>(0;0)</code> coordinates and others
	 * subdivisions has a positive, incremental coordinates. <br>
	 * Obviously, this is not a constraint: its implementation and their users are
	 * free to use negative coordinates, but it's discouraged.<br>
	 * In short: the origin should be at <code>(0;0)</code>.
	 * </p>
	 * <p>
	 * Example: in a 3x3 square subdivision, the top-left has <code>(0;0)</code>
	 * coordinates while the bottom-right has <code>(2,2)</code> coordinates.
	 * </p>
	 */
	public abstract SingleSpaceSubdivision getSpaceSectionsByRowColumn(double row, double column);

	public abstract SingleSpaceSubdivision sectionCointaining(Point2D position, SpaceSubsectionNode node);

	//

	//

	/** Single subdivision of the space, listed by {@link SpaceSubdivisions}. */
	public abstract class SingleSpaceSubdivision implements Serializable {
		private static final long serialVersionUID = -920464924056003666L;

		public SingleSpaceSubdivision(double xDeltaFromCenter, double yDeltaFromCenter) {
			super();
			this.xDeltaFromCenter = xDeltaFromCenter;
			this.yDeltaFromCenter = yDeltaFromCenter;
		}

		protected final double xDeltaFromCenter, yDeltaFromCenter;

		/**
		 * Returns a unique index associated with this subdivision, reflecting the
		 * {@link Enum#ordinal()} concept.
		 */
		public abstract int getOrdinal();

		/**
		 * Returns an optional name associated with this subdivision. Could be helpful.
		 */
//		public abstract String getName();

		/**
		 * The x-component of {@link #getCoordinatesFromCenter()}.
		 */
		public double getXDeltaFromCenter() {
			return xDeltaFromCenter;
		}

		/**
		 * The y-component of {@link #getCoordinatesFromCenter()}.
		 */
		public double getYDeltaFromCenter() {
			return yDeltaFromCenter;
		}

		//

		/**
		 * Works in a similar way to
		 * {@link SpaceSubdivisions#getSpaceSectionsByRowColumn(double, double)}, but
		 * with the main difference that the origin is located in the <i>central</i>
		 * subdivision.
		 */
		public Point2D getCoordinatesFromCenter() {
			return new Point2D.Double(xDeltaFromCenter, yDeltaFromCenter);
		}

		/**
		 * Take the center of the given subsection and returns the point of the center
		 * of an hypothetical new subdivision (one level deeper) if the given subsection
		 * would be divided.
		 *
		 * @param subsection the subsection to refer to.
		 */
		public abstract Point2D centerOfDeeperSubsection(SpaceSubsectionNode subsection);// double
																							// thirdSizeSection
	}
}