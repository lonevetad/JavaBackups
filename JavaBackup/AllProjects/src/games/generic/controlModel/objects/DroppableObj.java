package games.generic.controlModel.objects;

import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.ObjectNamedID;

/***
 * Defines an object that can be dropped into the ground
 * ({@link GameObjectsManager}) in a {@link GModalityRPG} game.
 *
 * @author ottin
 *
 */
public interface DroppableObj extends ObjectNamedID, InteractableObject, ObjectInSpace {

	/**
	 * Defines the actions to take upon dropping this item into the ground
	 *
	 * @param gmRPG
	 */
	public void onDrop(GModalityRPG gmRPG);

	/**
	 * Similar to {@link #onDrop(GModalityRPG)} and
	 */
	public void onPickUp(GModalityRPG gmRPG);

	/**
	 * Defomes the action performed upon being picked up by the
	 * {@link InteractingObj} (second parameter)-
	 *
	 * @param gmRPG
	 * @param pickingUpPerformer
	 */
	public void pickMeUp(GModalityRPG gmRPG, InteractingObj pickingUpPerformer);
}