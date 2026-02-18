package videogames.gridObjectManager.impl.coreImpl;

import java.util.Iterator;
import java.util.LinkedList;

public class PathOptimizerImpl implements PathOptimizerOLD {
	private static final long serialVersionUID = 467840001201380L;

	public PathOptimizerImpl() {
	}

	@Override
	public <E> LinkedList<E> optimizePath(LinkedList<E> l, CollinearCalculator<E> cc) {
		LinkedList<E> lr;
		Iterator<E> iter;
		E pfirst, psecond, pnext;
		if (l == null)
			return null;
		if (l.size() <= 2)
			// base case: not enought element to be filtered
			// while (nfirst != null) { lr.add(nfirst.getItem()); }
			return l;
		lr = new LinkedList<>();
		iter = l.iterator();
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
			pnext = iter.next(); // it exists thanks to the while condition
			if (cc.areCollinear(pfirst, psecond, pnext)) {
				// collinear -> search for the next one
				psecond = pnext; // keep memory of the last collinear one
				pnext = iter.next(); // move to the next element, that will be tested
			} else {
				lr.add(psecond);
				// turn the angle
				pfirst = psecond;
				psecond = pnext;
			}
		} while(iter.hasNext());
		// the end
		lr.add(pnext);
		return lr;
	}
}