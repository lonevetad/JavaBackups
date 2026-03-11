package games.generic.controlModel.items;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.player.PlayerGeneric;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONArray;
import tools.json.types.JSONObject;

/**
 * Set of {@link EquipmentItem} equipped to a creature (like a the player:
 * {@link PlayerGeneric}.
 */
public abstract class EquipmentSet implements JSONable {

	private static final long serialVersionUID = -231364785428780L;

	public static interface ConsumerEquipmentIndex {
		public void apply(EquipmentItem equipment, int index);
	}

	public enum EquippingProcessResult {
		Succeeded, Failed, Swapped;
	}

	//

	public EquipmentSet() {
	}

	/** Should be final */
	protected BaseCreatureRPG creatureWearingEquipments;

	// public abstract EquipmentsHolder getEquipmentsHolder();
//	public abstract void setEquipmentsHolder(EquipmentsHolder equipmentsHolder);

	public BaseCreatureRPG getCreatureWearingEquipments() {
		return this.creatureWearingEquipments;
	}

	public void setCreatureWearingEquipments(BaseCreatureRPG creatureWearingEquipments) {
		this.creatureWearingEquipments = creatureWearingEquipments;
	}

	//

	/**
	 * Get the set of {@link EquipmentItem}. Should not be used.<br>
	 * NOTE: unused slots (i.e. "places" where there is no equipped objects for that
	 * type) are <code>null</code>.
	 */
	public abstract EquipmentItem[] getEquippedItems();

	public EquipmentItem getEquippedItemAt(int index) {
		return getEquippedItems()[index];
	}

	/**
	 * Equip the given item into the set NOTE: Should check if is yet equipped an
	 * item in that slot
	 */
	public abstract EquippingProcessResult addEquipmentItem(GModality gm, EquipmentItem ei);

	/**
	 * TODO use player's inventory and
	 * {@link EquipmentItem#getLocationInInventory()} (of the second parameter, if
	 * not <code>null</code>).
	 */
	public abstract EquippingProcessResult swapEquipmentItem(GModality gm, EquipmentItem newEI, EquipmentItem oldEI);

	public abstract void forEachEquipment(ConsumerEquipmentIndex consumer);

	//

	// JSON-related

	//

	@Override
	public void toJSONValue(JSONObject wrapper) {
		JSONable.super.raiseUnsupportedOperationExceptionForMethod(
				"toJSONValue of class: " + this.getClass().getName());
	}

	public abstract <E extends EquipmentItem> E newEquipmentItemByName(GModality gm, String name);

	@Override
	public JSONValue toJSONValue() {
		EquipmentItem[] equips = this.getEquippedItems();
		int index = 0;
		JSONValue[] equipsJSONed = new JSONValue[equips.length];
		for (EquipmentItem e : equips) {
			equipsJSONed[index++] = e.toJSONValue();
		}
		return new JSONArray(true, equipsJSONed, JSONTypes.Object);
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		this.raiseUnsupportedOperationExceptionForMethod("loadFromJSONObject, use loadFromJSONArray instead");
	}

	public void loadFromJSONArray(GModality gm, JSONArray wrapper) {
		if (wrapper == null) {
			throw new IllegalArgumentException("Provided JSONObject wrapper cannot be null");
		}
		if (!wrapper.isType(JSONTypes.ArrayHomogeneousType)) {
			this.raiseExceptionIllegalTypeField("equippedItems_array", JSONTypes.ArrayHomogeneousType, wrapper);
		}
		wrapper.forEach((index, equipJSONed) -> {
			if (!equipJSONed.isType(JSONTypes.Object)) {
				this.raiseExceptionIllegalTypeField(SEPARATOR_INDEX + index, JSONTypes.Object, equipJSONed);
			}
			JSONObject equipJSONed_obj = (JSONObject) equipJSONed;
			JSONValue equipNameJSONed_value = equipJSONed_obj.getFieldValue(ObjectNamed.FIELD_NAME);
			if (!equipNameJSONed_value.isType(JSONTypes.String)) {
				this.raiseExceptionIllegalTypeField(SEPARATOR_INDEX + index + SEPARATOR_FIELD + ObjectNamed.FIELD_NAME,
						JSONTypes.Object, equipJSONed);
			}
			String equipName = equipNameJSONed_value.asString();
			EquipmentItem ei = this.newEquipmentItemByName(gm, equipName);
			ei.loadFromJSONObject(gm, equipJSONed_obj);
			this.addEquipmentItem(gm, ei);
		});
	}

	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
	}
}