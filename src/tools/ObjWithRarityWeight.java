package tools;

/**
 * See {@link WeightedSetOfRandomOutcomes}.
 */
public interface ObjWithRarityWeight extends ObjectNamedID {
	public int getRarityWeight();

	public ObjWithRarityWeight setRarityWeight(int weight);
}