package games.generic.controlModel.objects;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.items.EquipmentItem;
import tools.ObjectNamedID;

/**
 * Marker interface for all objects that could be added to a game. It's not
 * required for them to be "visible" in some way, they just need to be present
 * "at runtime" (i.e., a database-like entry like an {@link EquipmentItem} can,
 * and it does, inherit from this interface or may not directly implement this
 * interface this requiring a wrapper class in order to be "translated" from a
 * "definition" to an "actual object that can appear in a game and can interact
 * with").
 */
public interface GameObjectGeneric extends ObjectNamedID, GModalityHolder {

	/**
	 * Opposite of {@link #onRemovedFromGame(GModality)}, is an actions performed
	 * upon upon being added to the game, in some way (into the "map", usually).<br>
	 * Usually called by {@link GModality#addGameObject(GameObjectGeneric)}.
	 */
	public void onAddedToGame(GModality gm);

	public default void addMeToGame(GModality gm) { gm.addGameObject(this); }

	/**
	 * Opposite of {@link #onAddedToGame(GModality)}, performing clean-up actions
	 * and called by {@link GModality#removeGameObject(GameObjectGeneric)}.
	 */
	public void onRemovedFromGame(GModality gm);

	/**
	 * IMPORTANT notes for overrider:
	 * <ul>
	 * <li>Should invoke {@link GModality#removeGameObject(GameObjectGeneric)}, as
	 * the default implementation does</li>
	 * <li>It's not needed to call {@link #onRemovedFromGame(GModality)}: it's
	 * already called by {@link GModality#removeGameObject(GameObjectGeneric)}.</li>
	 * </ul>
	 */
	public default void removeMeToGame(GModality gm) { gm.removeGameObject(this); }
}