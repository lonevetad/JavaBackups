package dataStructures.spaceSubdivisionTree.impl.utils;

import java.io.Serializable;

import dataStructures.spaceSubdivisionTree.SpaceSubdivisionsManagerTree;
import dataStructures.spaceSubdivisionTree.SpaceSubsectionNode;

/** Predicate to decide if a subdivision should ve performed */
public interface SubsectionDivisionRuler extends Serializable {
	public ObjectsSubdivision isSubdivisionNeeded(SpaceSubsectionNode subsection,
			SpaceSubdivisionsManagerTree subsectionBelonger);
}