package games.generic.controlModel.objects;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.items.EquipmentItem;
import tools.ObjectWithID;

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
	 * Performs some actions when the owner "gains" this ability. As example, if
	 * this instance is a {@link TimedObject} or a {@link GEventObserver}, then the
	 * method {@link GModality#addGameObject(ObjectWithID)} should be called.
	 */
	public default void onAddingToOwner(GModality gm) { addMeToGame(gm); }

	@Override
	public default void onAddedToGame(GModality gm) {}

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
	public default void onRemovedFromGame(GModality gm) {}
}