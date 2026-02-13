package games.generic.controlModel.objects;

import games.generic.controlModel.holders.InventoryHolder;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.misc.Currency;
import games.generic.controlModel.player.PlayerGeneric;
import tools.ObjectWithID;

/**
 * Defines an object that something (a {@link InteractingObj}) can interact
 * with. <br>
 * The action performed upon receiving the interaction is
 * {@link #acceptInteractionFrom(InteractingObj)}. <br>
 * Note that if the {@link InteractingObj} could perform different actions
 * depending on this subclasses then the {@link InteractingObj} itself HAS the
 * <i> responsibility</i> of performing that action (for example: a
 * {@link Currency} makes the {@link PlayerGeneric} to earn money, an
 * {@link InventoryItem} has to
 * {@link InventoryHolder#addToInventory(InventoryItem)}, etc).
 *
 * @author ottin
 *
 */
public interface InteractableObject extends ObjectWithID {
	/**
	 * Defines what action should be taken upon receiving the interaction from
	 * someone else (a {@link InteractingObj} object).<br>
	 * Some example of interactable objects are the following:
	 * <ul>
	 * <li>Some {@link Currency} Item, that causes the owner</li>
	 * <li>some food that heals</li>
	 * <li>a leveler, a button, etc</li>
	 * <li>a NPC (like a merchant), opening its GUI</li>
	 * <li>an {@link InventoryItem}, which requires to
	 * {@link InventoryHolder#addToInventory(InventoryItem)}</li>
	 * <li>another {@link PlayerGeneric}, that could open the menu or fire</li>
	 * <li>etc</li>
	 * </ul>
	 */
	public void acceptInteractionFrom(InteractingObj interactionPerformer);
}
