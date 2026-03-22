package geometry.implementations;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import geometry.PathOptimizer;
import tools.MathUtilities;

public class PathOptimizerSimple implements PathOptimizer<Point2D> {
	public static final CollinearCalculator<Point2D> COLLINEAR_POINT = MathUtilities::areCollinear;
//	public static final CollinearCalculator<Point2D> COLLINEAR_POINT = (p1,p2,p3)->MathUtilities.areCollinear(pfirst, psecond, pthird);
	protected static final PathOptimizerSimple singleton = new PathOptimizerSimple();

	public static PathOptimizerSimple getInstance() { return singleton; }

	private PathOptimizerSimple() {}

	@Override
	public List<Point2D> optimizePath(List<Point2D> pathList) {
		LinkedList<Point2D> lr;
		Iterator<Point2D> iter;
		Point2D pfirst, psecond, pnext;
		if (pathList == null)
			return null;
		if (pathList.size() <= 2)
			// base case: not enought element to be filtered
			// while (nfirst != null) { lr.add(nfirst.getItem()); }
			return pathList;
		lr = new LinkedList<>();
		iter = pathList.iterator();
		// take the first, as start
		pfirst = iter.next();
		lr.add(pfirst);
		// surely the second element exists because of the size's check
		psecond = iter.next();
		/*
		 * Search for the "next" element not-collinear, keeping memory of the "last"
		 * collinear one. As that "next" element is found (the non collinear one), add
		 * that "last" element, which will become the first of the next iteration, and
		 * make that "next" element as the second
		 */
		do {
			pnext = iter.next(); // move to the next element, that will be tested
			if (COLLINEAR_POINT.areCollinear(pfirst, psecond, pnext)) {
				// collinear -> search for the next one
				psecond = pnext; // keep memory of the last collinear one
			} else {
				lr.addLast(psecond);
				// turn the angle
				pfirst = psecond;
				psecond = pnext;
			}
		} while (iter.hasNext());
		// the end
		lr.add(pnext);
		return lr;
	}
}