package games.generic.controlModel.items;

import games.generic.controlModel.GModality;
import games.generic.controlModel.objects.InteractableObject;
import tools.ObjectWithID;

/**
 * Items designed for inventory purpose but not directly equippable, like
 * potions.<br>
 * Most useful for RPG.
 * <p>
 * See
 * {@link InteractableObject#acceptInteractionFrom(games.generic.controlModel.objects.InteractingObj)}.
 */
public abstract class InventoryItemNotEquippable extends InventoryItem {
	private static final long serialVersionUID = 47193518837000L;

	public InventoryItemNotEquippable(GModality gameModality, String name) { super(gameModality, name); }

	@Override
	public void onAddingToOwner(GModality gm) {
		// should not do anything
	}

	@Override
	public void onRemovingFromOwner(GModality gm) {
		// should not do anything
	}

	@Override
	public void setOwner(ObjectWithID owner) {
		// should not do anything
	}
}