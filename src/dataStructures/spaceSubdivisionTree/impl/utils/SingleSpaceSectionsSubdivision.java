package dataStructures.spaceSubdivisionTree.impl.utils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import dataStructures.spaceSubdivisionTree.SpaceSubdivisions.SingleSpaceSubdivision;
import geometry.ObjectLocated;

public class SingleSpaceSectionsSubdivision implements Serializable {
	private static final long serialVersionUID = 88871083003029858L;
	final SingleSpaceSubdivision spaceSubdivision;
	final List<ObjectLocated> objectsInSection;

	SingleSpaceSectionsSubdivision(SingleSpaceSubdivision dir) {
		this.spaceSubdivision = dir;
		this.objectsInSection = new LinkedList<>();
	}

	//

	public SingleSpaceSubdivision getSpaceSubdivision() {
		return spaceSubdivision;
	}

	public List<ObjectLocated> getObjectsInSection() {
		return objectsInSection;
	}

	@Override
	public String toString() {
		return "SingleSpaceSectionsSubdivision [spaceSubdivision=" + spaceSubdivision + ", objectsInSection="
				+ objectsInSection + "]";
	}
}