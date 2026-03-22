package dataStructures.treeSimilStrat;

import dataStructures.EditCosts;
import dataStructures.NodeComparable;
import dataStructures.SortedSetEnhanced;
import tools.EditDistance;
import tools.EditDistance.EqualityChecker;
import tools.IterableSized;
import tools.impl.EditDistanceLevenshtein;

/** Uses the {@link EditDistance}. It's the best one since nows */
public class DissonanceTreeAlgo_Mine5<T> extends ADissonanceTreeAlgo_Mine<T> {

	public DissonanceTreeAlgo_Mine5() {}

	@Override
	public long computeDissonance(NodeAlteringCosts<T> nodeAlteringCost, NodeComparable<T> t1, NodeComparable<T> t2) {
		long[] totalDissonance = { 0 };
		computeDissonance_PrepareStuffs(totalDissonance, nodeAlteringCost, t1, t2);
		return totalDissonance[0];
	}

	protected void computeDissonance_PrepareStuffs(long[] totalDissonance, NodeAlteringCosts<T> nodeAlteringCost,
			NodeComparable<T> t1, NodeComparable<T> t2) {
		EqualityChecker<NodeComparable<T>> equalityChecker;
		EditCosts<NodeComparable<T>> cac;
		final EditDistance ed;
		ed = new EditDistanceLevenshtein();
		equalityChecker = (n1, n2) -> false;// need to perform always the "cac". If equal nodes -> diss == 0
		cac = // here comes the bigger part
				new EditCosts<>() {
					@Override
					public long insertion(NodeComparable<T> element) {
						return nodeAlteringCost.insertion(element) + //
						getActionCostWholeSubtree(nodeAlteringCost, true, element.getChildrenNC());
					}

					@Override
					public long deletion(NodeComparable<T> element) {
						return nodeAlteringCost.deletion(element) + //
						getActionCostWholeSubtree(nodeAlteringCost, false, element.getChildrenNC());
					}

					@Override
					public long substitution(NodeComparable<T> element, NodeComparable<T> newLabel) {
						// delegate to recursion
						long[] d = { 0 };
						computeDissonance_on_nodes(d, nodeAlteringCost, element, newLabel, equalityChecker, this, ed); // RECURSION
						return d[0];
					}
				};
		computeDissonance_on_nodes(totalDissonance, nodeAlteringCost, t1, t2, equalityChecker, cac, ed);
	}

	protected void computeDissonance_on_nodes(long[] totalDissonance, NodeAlteringCosts<T> nodeAlteringCost,
			NodeComparable<T> t1, NodeComparable<T> t2, EqualityChecker<NodeComparable<T>> equalityChecker,
			EditCosts<NodeComparable<T>> cac, EditDistance ed) {
		totalDissonance[0] += t1.getKeyComparator().compare(t1.getKeyIdentifier(), t2.getKeyIdentifier()) == 0 //
				? 0
				: //
				nodeAlteringCost.renameNodeCost(t1, t2.getKeyIdentifier());

		computeDissonance_on_children(totalDissonance, nodeAlteringCost, t1.getChildrenNC(), t2.getChildrenNC(),
				equalityChecker, cac, ed);
	}

	// the most important part
	protected void computeDissonance_on_children(long[] totalDiss, NodeAlteringCosts<T> nodeAlteringCost,
			SortedSetEnhanced<NodeComparable<T>> ff, SortedSetEnhanced<NodeComparable<T>> fg,
			EqualityChecker<NodeComparable<T>> eqCheck, EditCosts<NodeComparable<T>> cac,
			EditDistance ed) {
		boolean ffEmpty, fgEmpty;
		ffEmpty = (ff == null || ff.isEmpty());
		fgEmpty = (fg == null || fg.isEmpty());
		if (ffEmpty || fgEmpty) {
			if (ffEmpty && fgEmpty)
				return;
			else if (ffEmpty) {
				handleForestWithMissingCounterpart(totalDiss, nodeAlteringCost, fg, false);
				return;
			} else if (fgEmpty) {
				handleForestWithMissingCounterpart(totalDiss, nodeAlteringCost, ff, true);
				return;
			}
		} else if (ff.size() == 1 && fg.size() == 1) { // recursion
			computeDissonance_on_nodes(totalDiss, nodeAlteringCost, ff.first(), fg.first(), eqCheck, cac, ed);
			return;
		}
		// else ...
		totalDiss[0] += ed.editDistance(IterableSized.from(ff), IterableSized.from(fg), eqCheck, cac);
	}

	protected void handleForestWithMissingCounterpart(long[] totalDiss, NodeAlteringCosts<T> nodeAlteringCost,
			SortedSetEnhanced<NodeComparable<T>> forest, boolean isInsert) {
		if (forest == null || forest.isEmpty())
			return;
		totalDiss[0] += getActionCostWholeSubtree(nodeAlteringCost, isInsert, forest);
	}
}