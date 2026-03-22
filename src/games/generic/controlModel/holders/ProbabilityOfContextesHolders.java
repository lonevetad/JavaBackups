package games.generic.controlModel.holders;

import java.util.Map;

import dataStructures.MapTreeAVL;
import tools.ObjectNamedID;
import tools.WeightedSetOfRandomOutcomes;

public class ProbabilityOfContextesHolders {

	public ProbabilityOfContextesHolders() {
		this.weightedIndexesContextes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				ObjectNamedID.COMPARATOR_OBJECT_NAMED_ID);
	}

	protected Map<ObjectNamedID, WeightedSetOfRandomOutcomes> weightedIndexesContextes;

	public Map<ObjectNamedID, WeightedSetOfRandomOutcomes> getWeightedIndexesContextes() {
		return weightedIndexesContextes;
	}

	public void addWeightedIndexesContext(ObjectNamedID name, WeightedSetOfRandomOutcomes weightedIndexes) {
		this.weightedIndexesContextes.put(name, weightedIndexes);
	}

	public WeightedSetOfRandomOutcomes getWeightedIndexesContext(ObjectNamedID name) {
		return weightedIndexesContextes.get(name);
	}
}