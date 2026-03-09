package tools;

import games.generic.controlModel.GModality;
import tools.json.types.JSONObject;

/**
 * See {@link WeightedSetOfRandomOutcomes}.
 */
public interface ObjWithRarityWeight extends ObjectNamedID {
	public int getRarityWeight();

	public ObjWithRarityWeight setRarityWeight(int weight);

	@Override
	public void toJSONValue(JSONObject wrapper) {
		// TODO
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		// TODO
	}
}