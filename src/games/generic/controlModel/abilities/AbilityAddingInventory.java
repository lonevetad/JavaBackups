package games.generic.controlModel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityBaseWithCustomName;
import games.generic.controlModel.items.InventoryAdder;
import games.generic.controlModel.items.InventoryItems;

/**
 * Ability that adds an
 *
 * @author ottin
 *
 */
public abstract class AbilityAddingInventory extends AbilityBaseWithCustomName implements InventoryAdder {
	private static final long serialVersionUID = 89567950000332711L;

	public AbilityAddingInventory(String name, int level) { super(name, level); }

	public AbilityAddingInventory(String name) { super(name); }

	protected InventoryItems inventoryToAdd;

	//

	public abstract InventoryItems newInventoryItems(int level);

	@Override
	public InventoryItems getInventoryToAdd() { return inventoryToAdd; }

	@Override
	public void performAbility(GModality gm, int targetLevel) {
		if (inventoryToAdd == null || inventoryToAdd.getLevelInventory() != targetLevel) {
			this.inventoryToAdd = this.newInventoryItems(targetLevel);
		}
	}

	@Override
	public void resetAbility() {
//		this.getInventoryToAdd().empty();
	}
}