package games.generic.controlModel.attributes;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.holders.AttributesModificationsHolder;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EssenceStorage;
import tools.json.types.JSONObject;

/**
 * Simply consists in a named collection of {@link AttributeModification}.<br>
 * They differs from them because they are part of a definition of an
 * {@link EquipmentItem} (talking about "static definition", like those provided
 * in a database-like) while this class is just an "upgrade" provided randomly
 * to increase (or decrease) the quality (and the value, maybe) of the equipment
 * having it. <br>
 * (Also, in some game this upgrade could be extracted in some kind of
 * potion-essence like {@link EssenceStorage} and applied to another equipment,
 * that is a very useful and cool feature).
 */
public interface AttributesUpgrade extends RarityHolder, ObjectNamed, AttributesModificationsHolder {

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		ObjectNamed.super.toJSONValue(wrapper);
		RarityHolder.super.toJSONValue(wrapper);
		AttributesModificationsHolder.super.toJSONValue(wrapper);
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		ObjectNamed.super.loadFromJSONObject(gm, wrapper);
		try {
			RarityHolder.super.loadFromJSONObject(gm, wrapper);
		} catch (Exception e) {
			System.err.println("Error while loading " + this.getClass().getName() + " : " + this.getName());
			throw new IllegalArgumentException(e);
		}
		// attribute modifiers
		AttributesModificationsHolder.super.loadFromJSONObject(gm, wrapper);
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		ObjectNamed.super.loadFromJSONMap(gm, jsonMap);
		RarityHolder.super.loadFromJSONMap(gm, jsonMap);
		AttributesModificationsHolder.super.loadFromJSONMap(gm, jsonMap);
	}
}