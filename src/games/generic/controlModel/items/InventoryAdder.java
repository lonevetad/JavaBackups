package games.generic.controlModel.items;

import java.util.Comparator;

import games.generic.controlModel.misc.factories.InventoryItemsFactory;
import games.generic.controlModel.misc.uidp.UIDPLoadableFacade;
import games.generic.controlModel.player.PlayerGeneric;
import tools.Comparators;
import tools.ObjectWithID;
import tools.UniqueIDProvider;

/**
 * Defines an object that adds an {@link InventoryItems} to someone, like a
 * {@link PlayerGeneric}.<br>
 * The sizes of the inventory may vary on the level.
 */
public interface InventoryAdder extends ObjectWithID {
	public static final Comparator<InventoryAdder> COMPARATOR_INVENTORY_ADDER = (ia1, ia2) -> {
		if (ia1 == ia2) { return 0; }
		if (ia1 == null) { return -1; }
		if (null == ia2) { return 1; }
		// return ObjectWithID.COMPARATOR_OWID.compare(ia1, ia2);
		return Comparators.LONG_COMPARATOR.compare(ia1.getID(), ia2.getID());
	};
	public static final UniqueIDProvider UIDP_INVENTORY_ADDER = new UIDPLoadableFacade<>(InventoryAdder.class);

	//

	public InventoryItemsFactory getInventoryItemsFactory();

	public InventoryItems getInventoryToAdd();

//

	public static InventoryAdder newDefaultInventoryAdder(InventoryItemsFactory inventoryFactory) {
		return new InventoryAdderDefault(inventoryFactory);
	}

	public static class InventoryAdderDefault implements InventoryAdder {
		private static final long serialVersionUID = 34508969387582L;

		public InventoryAdderDefault(InventoryItemsFactory inventoryFactory) {
			super();
			this.inventoryFactory = inventoryFactory;
			this.inventory = null;
			this.ID = UIDP_INVENTORY_ADDER.getNewID();
		}

		protected final Long ID;
		protected final InventoryItemsFactory inventoryFactory;
		protected InventoryItems inventory;

		@Override
		public Long getID() { return ID; }

		@Override
		public InventoryItemsFactory getInventoryItemsFactory() { return inventoryFactory; }

		@Override
		public InventoryItems getInventoryToAdd() {
			InventoryItems i;
			if ((i = this.inventory) == null) { this.inventory = i = this.inventoryFactory.newInventoryItems(); }
			return i;
		}

		@Override
		public boolean setID(Long newID) { return false; }
	}
}