package dataStructures.spaceSubdivisionTree.impl;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Map;

import dataStructures.spaceSubdivisionTree.SpaceSubdivisionsManagerTree;
import dataStructures.spaceSubdivisionTree.SpaceSubsectionNode;
import dataStructures.spaceSubdivisionTree.impl.utils.ObjectsSubdivision;
import dataStructures.spaceSubdivisionTree.impl.utils.SubsectionDivisionRuler;
import geometry.ObjectLocated;

//TODO generalize from "Enna" to something else
/** Works under the "rectangular assumption */
public class SubsectionDivisionRulerImpl implements SubsectionDivisionRuler {
	private static final long serialVersionUID = 2829317112540506L;
	public static final int MIN_THRESHOLD = 7;
	public static final int MIN_THRESHOLD_TO_SPREAD = (MIN_THRESHOLD + (MIN_THRESHOLD & 0x1)) >>> 1;

	@Override
	public ObjectsSubdivision isSubdivisionNeeded(SpaceSubsectionNode subsection,
			SpaceSubdivisionsManagerTree subsectionBelonger) {
		int objsCount;
		ObjectsSubdivision os;
		objsCount = subsection.getObjectsCount();
		os = null;
		if (objsCount <= MIN_THRESHOLD)
			return null;

		os = areObjsEnoughSpread(subsection, subsectionBelonger);
		if (!os.isEnoughSpreaded())
			return null;
		if (areObjsTooClumped(subsection, subsectionBelonger)
				|| areObjsTooDistantFromCenter(subsection, subsectionBelonger))
			return os;
		return null;
	}

	protected static boolean areObjsTooClumped(SpaceSubsectionNode sub,
			SpaceSubdivisionsManagerTree subsectionBelonger) {
		double meanDistanceFromCentroid;
		final double objCountDouble, squareSideSubsection;
		Map<Point2D, ObjectLocated> objectsInSections;
		Iterator<Map.Entry<Point2D, ObjectLocated>> iterator;
		final Point2D meanPoint; // that is the centroid

		RectangularSpaceSubsectionNode rectSubNode;
		FixedRectangularGridSpaceSubdivisions recSpaceSubds;
		// try to have more informations
		rectSubNode = (RectangularSpaceSubsectionNode) sub;
		recSpaceSubds = (FixedRectangularGridSpaceSubdivisions) subsectionBelonger.getSpaceSubdivisions();

		objectsInSections = sub.getObjectsInSection();
		if (objectsInSections == null)
			return false;
//todo

		/*
		 * first or: la distanza media dal centro supera la dimensione di un quadrato,
		 * ossia la dimensione di una sottosezione
		 */

		// calculate the mean
		// to avoid overlow, divide each value to the count of the objects
		objCountDouble = objectsInSections.size();
//		mean = new double[2]; // it's a 2D point
		meanPoint = new Point2D.Double(0, 0);
		objectsInSections.forEach((p, obj) -> {
			meanPoint.setLocation((meanPoint.getX() + (p.getX() / objCountDouble)),
					(meanPoint.getY() + (p.getY() / objCountDouble))); //
//			meanPoint.x += p.getX() / objCountDouble;
//			meanPoint.y += p.getY() / objCountDouble;
		});
		// more or less, that's the mean
		squareSideSubsection = Math.min(rectSubNode.getWidth(), rectSubNode.getHeight()) //
				/ Math.min(recSpaceSubds.getColumnsSubdivisionsAmount(), recSpaceSubds.getRowsSubdivisionsAmount());
		iterator = objectsInSections.entrySet().iterator();
		meanDistanceFromCentroid = 0;
		while (iterator.hasNext() && meanDistanceFromCentroid > squareSideSubsection)
			meanDistanceFromCentroid += meanPoint.distance(iterator.next().getKey()) / objCountDouble;
		// the mean is too near? -> too clumped
		return meanDistanceFromCentroid <= squareSideSubsection;
	}

	protected static boolean areObjsTooDistantFromCenter(SpaceSubsectionNode sub,
			SpaceSubdivisionsManagerTree subsectionBelonger) {
		double meanDistanceFromCenter;
		final double squareSideSubsection, objCountDouble;
		Map<Point2D, ObjectLocated> objectsInSections;
		Iterator<Map.Entry<Point2D, ObjectLocated>> iterator;
		final Point2D centerPoint;

		RectangularSpaceSubsectionNode rectSubNode;
		FixedRectangularGridSpaceSubdivisions recSpaceSubds;
		// try to have more informations
		rectSubNode = (RectangularSpaceSubsectionNode) sub;
		recSpaceSubds = (FixedRectangularGridSpaceSubdivisions) subsectionBelonger.getSpaceSubdivisions();

		/*
		 * first or: la distanza media dal centro supera la dimensione di un quadrato,
		 * ossia la dimensione di una sottosezione
		 */
		objectsInSections = sub.getObjectsInSection();
		if (objectsInSections == null)
			return false;
		centerPoint = new Point2D.Double(sub.getXCenter(), sub.getYCenter());
		/*
		 * 3.0 because sections are 9, i.e. 3x3 subdivision, so the subsection has a 1/3
		 * length of its square
		 */
		objCountDouble = objectsInSections.size();
		squareSideSubsection = Math.min(rectSubNode.getWidth(), rectSubNode.getHeight()) //
				/ Math.min(recSpaceSubds.getColumnsSubdivisionsAmount(), recSpaceSubds.getRowsSubdivisionsAmount());
		iterator = objectsInSections.entrySet().iterator();
		meanDistanceFromCenter = 0;
//		for (Entry<Point2D.Double, ObjectLocated> e : objectsInSections.entrySet())
		while (iterator.hasNext() && meanDistanceFromCenter <= squareSideSubsection)
			meanDistanceFromCenter += centerPoint.distance(iterator.next().getKey()) / objCountDouble;
		// the mean is too distant? -> soo too clumped
		return meanDistanceFromCenter > squareSideSubsection;
	}

	protected static ObjectsSubdivision areObjsEnoughSpread(SpaceSubsectionNode sub,
			SpaceSubdivisionsManagerTree subsectionBelonger) {
		// Set<..,..> objectsInSection
		int objsCount;
		ObjectsSubdivision os;
		os = new ObjectsSubdivision(subsectionBelonger, sub);
		objsCount = sub.getObjectsCount();
		if (objsCount > MIN_THRESHOLD) {
			os.setForceSpread(true);
			return os;
		}
		if (objsCount < MIN_THRESHOLD_TO_SPREAD)
			return os;
		// And so on....
		os.recalculateSubdivision();
		return os;
	}
}