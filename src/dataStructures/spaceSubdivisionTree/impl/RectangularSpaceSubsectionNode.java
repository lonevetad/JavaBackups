package dataStructures.spaceSubdivisionTree.impl;

import dataStructures.spaceSubdivisionTree.SpaceSubdivisions.SingleSpaceSubdivision;
import dataStructures.spaceSubdivisionTree.SpaceSubsectionNode;

/*node of manager_tree*/
public abstract class RectangularSpaceSubsectionNode extends SpaceSubsectionNode {

	public RectangularSpaceSubsectionNode(int level, SpaceSubsectionNode father,
			SingleSpaceSubdivision spaceSectionFromFather, double x, double y, double width, double height) {
		super(level, father, spaceSectionFromFather, x, y);
		this.width = width;
		this.height = height;
	}

	protected final double width, height;

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}