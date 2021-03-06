package grammars.transfer;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import grammars.NodeParsedSentence;
import tools.CloserGetter;
import tools.ClosestMatch;
import tools.NodeComparableSynonymIndexed;

public class TransferTranslationRuleBased_V4 extends ATransferTranslationRuleBased {
	public static final CloserGetter<Map.Entry<NodeParsedSentence, TransferRule>> CLOSE_GETTER_NPS_TR = (eo, e1,
			e2) -> CloserGetter.getCloserTo(eo, (e11, e22) -> NodeComparableSynonymIndexed.DIFF_COMPUTER_NODE
					.getDifference(e11.getKey(), e22.getKey()), e1, e2);

	public TransferTranslationRuleBased_V4() {
		rulesGivenLHS = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, NodeParsedSentence.COMPARATOR_NODE_NPS);
	}

	protected MapTreeAVL<NodeParsedSentence, TransferRule> rulesGivenLHS;

	@Override
	public void forEachRule(Consumer<TransferRule> c) { this.rulesGivenLHS.forEach((syn, rule) -> c.accept(rule)); }

	@Override
	public void addRule(TransferRule rule) {
		rulesGivenLHS.put(rule.lhsTemplate, rule);
		System.out.println("\nrule added:");
		System.out.println(rule);
	}

	@Override
	public NodeParsedSentence transfer(NodeParsedSentence rootSubtree) {
		TransferRule rule;
//		NodeSubtreeDependency transferred;
		System.out.println("\n searching");
		rule = this.getBestRuleFor(rootSubtree);
		System.out.println("best rule is");
		if (rule == null) {
			System.out.println(".......null");
		} else {
			System.out.println(rule);
		}
		return rule == null ? null : rule.applyTransferRule(this, rootSubtree);
	}

	@Override
	protected TransferRule getBestRuleFor(NodeParsedSentence subtreeToTransfer) {
//		NodeParsedSentence maybeALhs;
		ClosestMatch<Entry<NodeParsedSentence, TransferRule>> ruleMatched = this.rulesGivenLHS
				.closestMatchOf(subtreeToTransfer);
		if (ruleMatched == null)
			return null;
		// a "ClosestMatch" could have an exact match or just approximation
		return ruleMatched.getClosetsMatchToOriginal(CLOSE_GETTER_NPS_TR).getValue();
	}
}