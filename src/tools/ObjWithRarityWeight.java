package tools;

import java.util.Map;

import games.generic.controlModel.GModality;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;

/**
 * See {@link WeightedSetOfRandomOutcomes}.
 */
public interface ObjWithRarityWeight extends ObjectNamedID {
	public static final String FIELD_RARITY_WEIGHT = "rarityWeight";

	public int getRarityWeight();

	public ObjWithRarityWeight setRarityWeight(int weight);

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		ObjectNamedID.super.toJSONValue(wrapper);
		wrapper.addField(FIELD_RARITY_WEIGHT, new JSONInt(getRarityWeight()));
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		ObjectNamedID.super.loadFromJSONObject(gm, wrapper);
		if (wrapper == null) {
			throw new IllegalArgumentException("Provided JSONObject wrapper cannot be null");
		}
		if (!wrapper.hasField(FIELD_RARITY_WEIGHT)) {
			this.raiseExceptionMissingField(FIELD_RARITY_WEIGHT, JSONTypes.Int);
		}
		JSONValue rarityWeightJSONed_value = wrapper.getFieldValue(FIELD_RARITY_WEIGHT);
		if (!rarityWeightJSONed_value.isType(JSONTypes.Int)) {
			this.raiseExceptionIllegalTypeField(FIELD_RARITY_WEIGHT, JSONTypes.Int, rarityWeightJSONed_value);
		}
		this.setRarityWeight(rarityWeightJSONed_value.asInt());
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		ObjectNamedID.super.loadFromJSONMap(gm, jsonMap);
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSONObject map cannot be null");
		}
		if (!jsonMap.containsKey(FIELD_RARITY_WEIGHT)) {
			this.raiseExceptionMissingField(FIELD_RARITY_WEIGHT, JSONTypes.Int);
		}
		Object rarityWeightJSONed_value = jsonMap.get(FIELD_RARITY_WEIGHT);
		if (!(rarityWeightJSONed_value instanceof Integer)) {
			this.raiseExceptionIllegalTypeField(FIELD_RARITY_WEIGHT, JSONTypes.Int, rarityWeightJSONed_value);
		}
		this.setRarityWeight((Integer) rarityWeightJSONed_value);
	}
}