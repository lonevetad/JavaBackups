package games.generic.controlModel.holders;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentSet;
import games.generic.controlModel.items.EquipmentSet.ConsumerEquipmentIndex;
import games.generic.controlModel.objects.GameObjectGeneric;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONArray;
import tools.json.types.JSONObject;

public interface EquipmentsHolder extends GModalityHolder, GameObjectGeneric {
	public static final String FIELD_EQUIPMENT_SET = "equipmentSet";

	public EquipmentSet getEquipmentSet();

	/**
	 * Remember to call
	 * {@link EquipmentSet#setCreatureWearingEquipments(BaseCreatureRPG)}
	 */
	public void setEquipmentSet(EquipmentSet equips);

	//

	public EquipmentSet newEquipmentSet();

	//

	public default void equip(EquipmentItem equipment) {
		EquipmentSet es;
		es = this.getEquipmentSet();
		if (es != null) {
			es.addEquipmentItem(getGameModality(), equipment);
		}
	}

	public default void forEachEquipment(ConsumerEquipmentIndex action) {
		getEquipmentSet().forEachEquipment(action);
	}

	@Override
	public default void addMeToGame(GModality gm) {
		GameObjectGeneric.super.addMeToGame(gm);
		forEachEquipment((e, i) -> {
			if (e != null) {
				e.addMeToGame(gm);
			}
		});
	}

	@Override
	public default void onAddedToGame(GModality gm) {
		// nothing in particular to be done: each ability will behave by its own
	}

	@Override
	public default void removeMeToGame(GModality gm) {
		GameObjectGeneric.super.removeMeToGame(gm);
		forEachEquipment((e, i) -> {
			if (e != null) {
				e.removeMeToGame(gm);
			}
		});
	}

	@Override
	public default void onRemovedFromGame(GModality gm) {
		// nothing in particular to be done: each ability will behave by its own
	}

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		GameObjectGeneric.super.toJSONValue(wrapper);
		// equipmentSet
		wrapper.addField(FIELD_EQUIPMENT_SET, this.getEquipmentSet().toJSONValue());
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		GameObjectGeneric.super.loadFromJSONObject(gm, wrapper);
		if (wrapper == null) {
			throw new IllegalArgumentException("Provided JSONObject wrapper cannot be null");
		}
		// equipmentSet
		if (!wrapper.hasField(FIELD_EQUIPMENT_SET)) {
			this.raiseExceptionMissingField(FIELD_EQUIPMENT_SET, JSONTypes.ArrayHomogeneousType);
		}
		JSONValue equipmentSetJSONed_value = wrapper.getFieldValue(FIELD_EQUIPMENT_SET);
		if (!equipmentSetJSONed_value.isType(JSONTypes.ArrayHomogeneousType)) {
			this.raiseExceptionIllegalTypeField(FIELD_EQUIPMENT_SET, JSONTypes.ArrayHomogeneousType,
					equipmentSetJSONed_value);
		}
		JSONArray equipmentSetJSONed = (JSONArray) equipmentSetJSONed_value;
		EquipmentSet equipSet = gm.getGameObjectsProvider().newEquipmentSet();
		equipmentSetJSONed.forEach((index, equipJSONed) -> {
			if (equipJSONed != null) {
				if (!equipJSONed.isType(JSONTypes.Object)) {
					this.raiseExceptionIllegalTypeField(FIELD_EQUIPMENT_SET + SEPARATOR_INDEX + index, JSONTypes.Object,
							equipJSONed);
				}
				JSONObject equipJSONed_obj = (JSONObject) equipJSONed;
				if (!equipJSONed_obj.hasField(FIELD_NAME)) {
					this.raiseExceptionMissingField(
							FIELD_EQUIPMENT_SET + SEPARATOR_INDEX + index + SEPARATOR_FIELD + FIELD_NAME,
							JSONTypes.Object);
				}
				String equipName = equipJSONed_obj.getFieldValue(FIELD_NAME).asString();
				this.equip(equipSet.newEquipmentItemByName(gm, equipName));
			}
		});
		wrapper.addField(FIELD_EQUIPMENT_SET, this.getEquipmentSet().toJSONValue());
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {

	}
}