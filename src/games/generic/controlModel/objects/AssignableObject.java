package games.generic.controlModel.objects;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.items.EquipmentItem;
import tools.ObjectWithID;
import tools.json.types.JSONObject;

/** And be assigned to an owner. */
public interface AssignableObject extends GameObjectGeneric {

	public ObjectWithID getOwner();

	public void setOwner(ObjectWithID owner);

	// utilities

	/**
	 * Override designed.<br>
	 * Some resets action should be done. Let's do it there.
	 */
	public void resetStuffs();

	/**
	 * Override designed.<br>
	 * Performs some actions when the owner "gains" this thing and get somehow
	 * assigned to it.
	 * As example, if this instance is a {@link TimedObject} or a
	 * {@link GEventObserver}, then the method
	 * {@link GModality#addGameObject(ObjectWithID)} should be called.
	 * The latter is performed in this default implementation because it calls
	 * {@link GameObjectGeneric#addMeToGame(GModality)}.
	 */
	public default void onAddingToOwner(GModality gm) {
		this.addMeToGame(gm);
	}

	@Override
	public default void onAddedToGame(GModality gm) {
	}

	/**
	 * Override designed.<br>
	 * Perform clean-up actions, by default by calling {@link #resetStuffs()}, in
	 * certain moments, like the death of the owner, the un-equipment of the
	 * belonging {@link EquipmentItem}, etc.
	 */
	public default void onRemovingFromOwner(GModality gm) {
		resetStuffs();
		setOwner(null);
		removeMeToGame(gm);
	}

	@Override
	public default void onRemovedFromGame(GModality gm) {
	}

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		GameObjectGeneric.super.toJSONValue(wrapper);
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		GameObjectGeneric.super.loadFromJSONObject(gm, wrapper);
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		GameObjectGeneric.super.loadFromJSONMap(gm, jsonMap);
	}

}