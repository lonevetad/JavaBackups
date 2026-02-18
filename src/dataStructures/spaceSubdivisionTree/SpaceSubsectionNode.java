package dataStructures.spaceSubdivisionTree;

import java.awt.geom.Point2D;
import java.util.Map;

import dataStructures.spaceSubdivisionTree.SpaceSubdivisions.SingleSpaceSubdivision;
import geometry.ObjectLocated;

/** A section node of the N-tree like structure. */
public abstract class SpaceSubsectionNode {

	public SpaceSubsectionNode(int level, SpaceSubsectionNode father, SingleSpaceSubdivision spaceSectionFromFather,
			double x, double y) {
		super();
		this.level = level;
		this.father = father;
		this.spaceSectionFromFather = spaceSectionFromFather;
		this.xCenter = x;
		this.yCenter = y;
	}

	//

	protected final int level;
	protected final double xCenter, yCenter;
	protected final SpaceSubsectionNode father;
	protected final SingleSpaceSubdivision spaceSectionFromFather;
//	protected final ShapesImplemented

	//

	public abstract int getMaxDepth();

	public int getLevel() { return level; }

	public double getXCenter() { return xCenter; }

	public double getYCenter() { return yCenter; }

	public SpaceSubsectionNode getFather() { return father; }

	public SingleSpaceSubdivision getSpaceSectionFromFather() { return spaceSectionFromFather; }

	public abstract void setChild(SingleSpaceSubdivision where, SpaceSubsectionNode child);

	public abstract Map<Point2D, ObjectLocated> getObjectsInSection();

	public int getObjectsCount() { return getObjectsInSection().size(); }
}