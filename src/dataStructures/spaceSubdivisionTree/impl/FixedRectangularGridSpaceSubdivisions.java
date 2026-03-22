package dataStructures.spaceSubdivisionTree.impl;

import java.awt.geom.Point2D;

import dataStructures.spaceSubdivisionTree.SpaceSubdivisions;
import dataStructures.spaceSubdivisionTree.SpaceSubsectionNode;

public abstract class FixedRectangularGridSpaceSubdivisions extends SpaceSubdivisions {
	private static final long serialVersionUID = 4780502332030165645L;

	public abstract int getRowsSubdivisionsAmount();

	public abstract int getColumnsSubdivisionsAmount();

	@Override
	public int countSubdivisions() {
		return Math.abs(getRowsSubdivisionsAmount() + getColumnsSubdivisionsAmount());
	}

	@Override
	public SingleSpaceSubdivision sectionCointaining(Point2D position, SpaceSubsectionNode n) {
		double r, c;
		RectangularSpaceSubsectionNode node;
		if (!(n instanceof RectangularSpaceSubsectionNode))
			return null;
		node = (RectangularSpaceSubsectionNode) n;
		c = Math.round((position.getX() - node.getXCenter()) / node.getWidth());
		r = Math.round((position.getY() - node.getYCenter()) / node.getHeight());
		return this.getSpaceSectionsByRowColumn(r, c);
	}

	//

	//

	public abstract class RectangularSingleSpaceSubdivision extends SingleSpaceSubdivision {
		private static final long serialVersionUID = -4780502332030165645L;

		public RectangularSingleSpaceSubdivision(double xDeltaFromCenter, double yDeltaFromCenter) {
			super(xDeltaFromCenter, yDeltaFromCenter);
		}

		@Override
		public Point2D centerOfDeeperSubsection(SpaceSubsectionNode subsection) {
			Point2D p;
			RectangularSpaceSubsectionNode ssnn;
			if (subsection == null)
				return null;

			ssnn = (RectangularSpaceSubsectionNode) subsection;
			p = new Point2D.Double(//
					subsection.getXCenter()
							+ ((ssnn.getWidth() / getColumnsSubdivisionsAmount()) * this.getXDeltaFromCenter()), //
					subsection.getYCenter()
							+ ((ssnn.getHeight() / getRowsSubdivisionsAmount()) * this.getYDeltaFromCenter()));
			return p;
		}

	}
}