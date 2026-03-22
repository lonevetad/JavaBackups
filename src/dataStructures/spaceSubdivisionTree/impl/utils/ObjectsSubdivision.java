package dataStructures.spaceSubdivisionTree.impl.utils;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import dataStructures.spaceSubdivisionTree.SpaceSubdivisions.SingleSpaceSubdivision;
import dataStructures.spaceSubdivisionTree.SpaceSubdivisionsManagerTree;
import dataStructures.spaceSubdivisionTree.SpaceSubsectionNode;
import dataStructures.spaceSubdivisionTree.impl.SubsectionDivisionRulerImpl;
import geometry.ObjectLocated;

public class ObjectsSubdivision implements Serializable {
	private static final long serialVersionUID = -9636625120628784L;
	//
	private boolean forceSpread;
	boolean neverTryiedToSpread;
	SingleSpaceSectionsSubdivision[] obsjSpreadedEachSpaceSections = null; // reminds to "directionsByRows"
	final SpaceSubsectionNode sub;
	final SpaceSubdivisionsManagerTree subsectionBelonger;

	public ObjectsSubdivision(SpaceSubdivisionsManagerTree subsectionBelonger, SpaceSubsectionNode sub) {
		this.sub = sub;
		this.setForceSpread(false);
		this.neverTryiedToSpread = true;
		this.subsectionBelonger = subsectionBelonger;
	}

	//

	public boolean isForceSpread() { return forceSpread; }

	public void setForceSpread(boolean forceSpread) { this.forceSpread = forceSpread; }

	public boolean isEnoughSpreaded() { return isForceSpread() || getObsjSpreaded() != null; }

	public boolean requireCalculculation() {
		return neverTryiedToSpread
				&& (isForceSpread() || sub.getObjectsCount() > SubsectionDivisionRulerImpl.MIN_THRESHOLD);
	}

	public void recalculateSubdivision() {
		if (neverTryiedToSpread) {
//			int r, c;
//			double squareSideLength;
			ObjectLocated o;
			SingleSpaceSectionsSubdivision dirSub, mostDenseSubdivision;
			SingleSpaceSectionsSubdivision[] oSED;
//			SquareNineSpaceSubdivisions.NineRectangular
			SingleSpaceSubdivision dir;
			Point2D location;
			Map<Point2D, ObjectLocated> objsInMap;
			objsInMap = sub.getObjectsInSection();
			if (objsInMap == null) {
				neverTryiedToSpread = false;
				return;
			}
			oSED = new SingleSpaceSectionsSubdivision[subsectionBelonger.getSpaceSubdivisions().countSubdivisions()];
			mostDenseSubdivision = null;
			for (Entry<Point2D, ObjectLocated> e : objsInMap.entrySet()) {
				o = e.getValue();
				/*
				 * compute in which subsection the object lies. Knowing that
				 * SquareNineSpaceSubdivisions.NineRectangularSingleSpaceSubdivision are
				 * organized in a rectangular distribution, the row and columns are simply the
				 * "normalization" of the position relative to the object location
				 */
				location = o.getLocation();

				try {
					// old version
//				squareSideLength = sub.getSquareSideLength();
//					c = (int) Math.round((location.getX() - sub.getX()) / squareSideLength);
//					r = (int) Math.round((location.getY() - sub.getY()) / squareSideLength);
//					dir = SquareNineSpaceSubdivisions.NineRectangularSingleSpaceSubdivision .getSpaceSectionsByRowColumn(r, c);
//					dir = SpaceSubdivisionsManagerTree_Nine.sectionCointaining_Version2(location, sub);
					dir = subsectionBelonger.getSpaceSubdivisions().sectionCointaining(location, sub);

					dirSub = oSED[dir.getOrdinal()];
					if (dirSub == null)// lazy initalization
						oSED[dir.getOrdinal()] = dirSub = new SingleSpaceSectionsSubdivision(dir);
					dirSub.objectsInSection.add(o); // a bit violating the Law of Demeter but it's accepted for
													// performance reasons
					// , Update most dense
					if (mostDenseSubdivision == null
							|| mostDenseSubdivision.objectsInSection.size() < dirSub.objectsInSection.size())
						mostDenseSubdivision = dirSub;
				} catch (IndexOutOfBoundsException ioobe) {
					System.out.println(
							"ERROR: the follwing object has been moved and now does not lies in this subsection: "
									+ this.sub.toString());
					System.out.println(o);
					ioobe.printStackTrace();
				}
			}
			if (mostDenseSubdivision != null && (forceSpread
					|| (mostDenseSubdivision.objectsInSection.size() < SubsectionDivisionRulerImpl.MIN_THRESHOLD
							&& mostDenseSubdivision.objectsInSection
									.size() >= SubsectionDivisionRulerImpl.MIN_THRESHOLD_TO_SPREAD))) {
				obsjSpreadedEachSpaceSections = oSED;
			}
			neverTryiedToSpread = false;
		}
	}

	public SingleSpaceSectionsSubdivision[] getObsjSpreaded() {
		if (requireCalculculation())
			recalculateSubdivision();
		return obsjSpreadedEachSpaceSections;
	}

	//

	@Override
	public String toString() {
		return "ObjectsSubdivision [forceSpread=" + isForceSpread() + ", obsjSpreadedEachSpaceSections="
				+ Arrays.toString(obsjSpreadedEachSpaceSections) + ", sub=" + sub + "]";
	}
}