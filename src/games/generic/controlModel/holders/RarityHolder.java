package games.generic.controlModel.holders;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.EquipmentItem;
import tools.WeightedSetOfRandomOutcomes;
import tools.json.JSONTypes;
import tools.json.JSONable;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;

/**
 * An object, like an {@link EquipmentItem}'s ability, could be created randomly
 * (like those equipments abilities, that could be added randomly to random
 * objects, but some abilities could be stronger or weaker, so they could be
 * much or less rare than others).<br>
 * The value (usually positive) returned by {@link #getRarityIndex()} represents
 * this concept and (probably) the rarity index.
 * <p>
 * Could be used with the {@link WeightedSetOfRandomOutcomes}.
 */
public interface RarityHolder extends JSONable {
	public static final String FIELD_RARITY_INDEX = "rarityIndex";
	/**
	 * Value that can be used to identify the "null value" of a rarity index.
	 */
	public static final int NO_RARITY_INDEX = -1;

	/** That's the INDEX of the rarity, like "common / rare / legendary". */
	public int getRarityIndex();

	public RarityHolder setRarityIndex(int rarityIndex);

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		JSONInt jsonedIndexRarity = new JSONInt(this.getRarityIndex());
		wrapper.addField(FIELD_RARITY_INDEX, jsonedIndexRarity);
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		if (!wrapper.hasField(FIELD_RARITY_INDEX)) {
			this.raiseExceptionMissingField(FIELD_RARITY_INDEX, JSONTypes.Int);
		}
		this.setRarityIndex(wrapper.getFieldValue(FIELD_RARITY_INDEX).asInt());
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}
		if (!jsonMap.containsKey(FIELD_RARITY_INDEX) || !(jsonMap.get(FIELD_RARITY_INDEX) instanceof Integer)) {
			this.raiseExceptionIllegalTypeField(FIELD_RARITY_INDEX);
		}
		this.setRarityIndex((Integer) jsonMap.get(FIELD_RARITY_INDEX));
	}

}