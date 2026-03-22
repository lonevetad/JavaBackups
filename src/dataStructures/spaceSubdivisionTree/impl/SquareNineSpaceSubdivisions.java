package dataStructures.spaceSubdivisionTree.impl;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dataStructures.spaceSubdivisionTree.SpaceSubsectionNode;

public class SquareNineSpaceSubdivisions extends FixedRectangularGridSpaceSubdivisions {
	private static final long serialVersionUID = 408851780430598300L;
	protected static double ZERO = 0.0// Double.valueOf(0.0)
			, ONE = 1.0// Double.valueOf(1.0)
			, MINUS_ONE = -1.0; // Double.valueOf(-1.0);
	// ,SQRT_TWO = Double.valueOf(Math.sqrt(2.0));
	private static final SquareNineSpaceSubdivisions singletoneSquareNineSpaceSubdivisions;

	public static final NineRectangularSingleSpaceSubdivision CENTER, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH,
			SOUTH_WEST, WEST, NORTH_WEST;
	/**
	 * Disposed in clock-wise, except for the center, and the North is at lower
	 * y-coordinate than South.
	 */
	protected static final NineRectangularSingleSpaceSubdivision[] valuesSpaceSections;
	protected static final NineRectangularSingleSpaceSubdivision[][] rowsInLIne;

	//

	/*
	 * public Point2D.Double transform(Point2D p, double ray) { if (ray < 0.0 || p
	 * == null) return null; return new Point2D.Double(xDeltaFromCenter * p.getX() *
	 * ray, yDeltaFromCenter * p.getY() * ray); }
	 */

	static {
		singletoneSquareNineSpaceSubdivisions = new SquareNineSpaceSubdivisions();

		CENTER = singletoneSquareNineSpaceSubdivisions.new NineRectangularSingleSpaceSubdivision(ZERO, ZERO);
		// , (p, r) -> p.setLocation(p.getX() + r, p.getY() + r)), //
		NORTH = singletoneSquareNineSpaceSubdivisions.new NineRectangularSingleSpaceSubdivision(ZERO, MINUS_ONE);
		// , (p, r) -> p.setLocation(p.getX() + r, p.getY())), //
		NORTH_EAST = singletoneSquareNineSpaceSubdivisions.new NineRectangularSingleSpaceSubdivision(ONE, MINUS_ONE);
		// , (p, r) -> p.setLocation(p.getX() + (r * 2.0), p.getY())), //
		EAST = singletoneSquareNineSpaceSubdivisions.new NineRectangularSingleSpaceSubdivision(ONE, ZERO);
		// , (p, r) -> p.setLocation(p.getX(), p.getY())), //
		SOUTH_EAST = singletoneSquareNineSpaceSubdivisions.new NineRectangularSingleSpaceSubdivision(ONE, ONE);
		/*
		 * , (p, r) -> { double dr; dr = +(r * 2.0); p.setLocation(p.getX() + dr,
		 * p.getY() + dr); })
		 */
		SOUTH = singletoneSquareNineSpaceSubdivisions.new NineRectangularSingleSpaceSubdivision(ZERO, ONE);
		// , (p, r) -> p.setLocation(p.getX() + r, p.getY() + (r * 2.0))), //
		SOUTH_WEST = singletoneSquareNineSpaceSubdivisions.new NineRectangularSingleSpaceSubdivision(MINUS_ONE, ONE);
		// , (p, r) -> p.setLocation(p.getX(), p.getY() + (r * 2.0))), //

		WEST = singletoneSquareNineSpaceSubdivisions.new NineRectangularSingleSpaceSubdivision(MINUS_ONE, ZERO);
		// , (p, r) -> p.setLocation(p.getX(), p.getY())), //

		NORTH_WEST = singletoneSquareNineSpaceSubdivisions.new NineRectangularSingleSpaceSubdivision(MINUS_ONE,
				MINUS_ONE);
		// , null), //
		singletoneSquareNineSpaceSubdivisions.progressiveCounterSubsections = 0;

		valuesSpaceSections = new NineRectangularSingleSpaceSubdivision[] { CENTER, NORTH, NORTH_EAST, EAST, SOUTH_EAST,
				SOUTH, SOUTH_WEST, WEST, NORTH_WEST };
		singletoneSquareNineSpaceSubdivisions.setList(valuesSpaceSections);
		rowsInLIne = new NineRectangularSingleSpaceSubdivision[][] { //
				new NineRectangularSingleSpaceSubdivision[] { NORTH_WEST, NORTH, NORTH_EAST }, //
				new NineRectangularSingleSpaceSubdivision[] { WEST, CENTER, EAST }, //
				new NineRectangularSingleSpaceSubdivision[] { SOUTH_WEST, SOUTH, SOUTH_EAST },//
		};
	}// end of static block

	public static SquareNineSpaceSubdivisions getInstance() {
		return singletoneSquareNineSpaceSubdivisions;
	}

	/*
	 * protected interface CenterCornerShifter { // Shifts and modify the given
	 * center, belonging to a greater zone, to the // center of its children (the
	 * Center subsquare is left untouhed) // public void shift(Point2D.Double
	 * centerCorner, double thirdSizeSection); }
	 */

	//

	//

	// TODO constructor

	private SquareNineSpaceSubdivisions() {
		super();
		this.progressiveCounterSubsections = 0;
	}

	private void setList(SingleSpaceSubdivision[] allSubdivisions) {
		List<SingleSpaceSubdivision> subs;
		if (allSubdivisions == null) {
		}
		subs = new ArrayList<>(allSubdivisions.length);
		for (SingleSpaceSubdivision ss : allSubdivisions)
			subs.add(ss);
		this.subdivisions = Collections.unmodifiableList(subs);
		subs = null;
	}

	private int progressiveCounterSubsections;
	private List<SingleSpaceSubdivision> subdivisions;

	//

	// TODO INHERITED METHODS

	@Override
	public int countSubdivisions() {
		return 9;// subdivisions.size();
	}

	public SingleSpaceSubdivision getSpaceSectionsByRowColumn(int row, int column) {
		return rowsInLIne[row][column];
	}

	@Override
	public SingleSpaceSubdivision getSpaceSectionsByRowColumn(double row, double column) {
		return getSpaceSectionsByRowColumn((int) row, (int) column);
	}

	@Override
	public SingleSpaceSubdivision newSingleSpaceSubdivision() {
		throw new UnsupportedOperationException("The \"nine\" subdivisions are defined by an internal enumeration");
	}

	@Override
	public List<SingleSpaceSubdivision> getSubdivisions() {
		return this.subdivisions;
	}

	@Override
	public int getRowsSubdivisionsAmount() {
		return 3;
	}

	@Override
	public int getColumnsSubdivisionsAmount() {
		return 3; // since it's a 3x3
	}

	/* Overrided to perform better */
	@Override
	public SingleSpaceSubdivision sectionCointaining(Point2D position, SpaceSubsectionNode n) {
		double halfSubnodeWidth, halfSubnodeHeight, nx, ny, xpos, ypos;
		SingleSpaceSubdivision ret;
		RectangularSpaceSubsectionNode node;
		NineRectangularSingleSpaceSubdivision[] row;

		node = (RectangularSpaceSubsectionNode) n;
		halfSubnodeWidth = node.getWidth();
		halfSubnodeHeight = node.getHeight();

		nx = node.getXCenter();
		ny = node.getYCenter();
		xpos = position.getX();
		ypos = position.getY();
		ret = null;

		if ((ny + halfSubnodeHeight) < ypos)
			row = rowsInLIne[0]; // north-part
		else if ((ny - halfSubnodeHeight) > ypos)
			row = rowsInLIne[1]; // south-part
		else
			row = rowsInLIne[2]; // center-part
		if ((nx + halfSubnodeWidth) < xpos)
			ret = row[2]; // east
		else if ((nx - halfSubnodeWidth) < ypos)
			ret = row[0]; // west
		else
			ret = row[1]; // center

//		if ((nx + halfSubnodeWidth) < xpos) {
//			// east side
//			if ((ny + halfSubnodeHeight) < ypos)
//				ret = NORTH_EAST;
//			else if ((ny - halfSubnodeHeight) > ypos)
//				ret = SOUTH_EAST;
//			else
//				ret = EAST;
//		} else if ((nx - halfSubnodeWidth) < ypos) {
//			// west side
//			if ((ny + halfSubnodeHeight) < ypos)
//				ret = NORTH_WEST;
//			else if ((ny - halfSubnodeHeight) > ypos)
//				ret = SOUTH_WEST;
//			else
//				ret = WEST;
//		} else {
//			// center column
//			if ((ny + halfSubnodeHeight) < ypos)
//				ret = NORTH;
//			else if ((ny - halfSubnodeHeight) > ypos)
//				ret = SOUTH;
//			else
//				ret = CENTER;
//		}
		return ret;
	}

//

//

	protected class NineRectangularSingleSpaceSubdivision extends RectangularSingleSpaceSubdivision {
		private static final long serialVersionUID = -408851780430598300L;

		public NineRectangularSingleSpaceSubdivision(double xDeltaFromCenter, double yDeltaFromCenter) {
			super(xDeltaFromCenter, yDeltaFromCenter);
			this.ordinal = progressiveCounterSubsections++;
		}

		private int ordinal;

		@Override
		public int getOrdinal() {
			return ordinal;
		}
	}
}