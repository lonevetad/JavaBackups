package games.generic.controlModel.objects.creature;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.holders.RarityHolder;
import tools.json.types.JSONObject;

/**
 * Some creatures, like enemy creatures, could be grouped under sets, like:
 * "goblin", "animal", "undead", "angel", "human", etc.<br>
 * Those sets has, as it's clear, a name and could have associated a kind of
 * "spawn probability". This concept is identified by the interface
 * {@link RarityHolder}.
 */
public interface CreatureType extends ObjectNamed, RarityHolder {
	@Override
	public default int getRarityIndex() {
		return 0;
	}

	@Override
	public default RarityHolder setRarityIndex(int rarityIndex) {
		return this;
	}

	//

	// JSON-related

	//

	@Override
	default void toJSONValue(JSONObject wrapper) {
		ObjectNamed.super.toJSONValue(wrapper);
		RarityHolder.super.toJSONValue(wrapper);
	}

	@Override
	default void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		ObjectNamed.super.loadFromJSONObject(gm, wrapper);
		RarityHolder.super.loadFromJSONObject(gm, wrapper);
	}

	@Override
	default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		ObjectNamed.super.loadFromJSONMap(gm, jsonMap);
		RarityHolder.super.loadFromJSONMap(gm, jsonMap);
	}
}