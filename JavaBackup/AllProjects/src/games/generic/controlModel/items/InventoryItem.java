package games.generic.controlModel.items;

import java.awt.Dimension;
import java.awt.Point;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.holders.InventoryHolder;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import games.generic.controlModel.objects.AssignableObject;
import games.generic.controlModel.objects.DroppableObj;
import games.generic.controlModel.objects.InteractingObj;
import games.generic.controlModel.subimpl.GModalityRPG;
import geometry.AbstractShape2D;
import tools.ObjectNamedID;
import tools.ObjectWithID;
import tools.UniqueIDProvider;
import tools.impl.OWIDLongImpl;

/**
 * Marker interface for elements that can be placed into the inventory. <br>
 * Used in RPGS
 */
public abstract class InventoryItem extends OWIDLongImpl
		implements ObjectNamedID, RarityHolder, GModalityHolder, DroppableObj, AssignableObject {
	private static final long serialVersionUID = 47104252L;

	private static UniqueIDProvider UIDP_INVENTORY = UniqueIDProvider.newBasicIDProvider();
	public static final UIDProviderLoadedListener UIDP_LOADED_LISTENER_INVENTORY = uidp -> {
		if (uidp != null) { UIDP_INVENTORY = uidp; }
	};

	public static UniqueIDProvider getUniqueIDProvider_Event() { return UIDP_INVENTORY; }

	//

	public InventoryItem(GModality gameModality, String name) {
		this.name = name;
		this.gameModality = gameModality;
		this.dimensionInInventory = new Dimension(1, 1);
		this.assignID();
	}

	protected int rarityIndex;
	protected String name, description;
	protected Point locationInInventory;
	protected Dimension dimensionInInventory;
	protected ObjectWithID owner;
	protected GModality gameModality;
	protected CurrencySet sellPrice;
	protected AbstractShape2D shape;

	protected void assignID() { this.ID = UIDP_INVENTORY.getNewID(); }

	@Override
	public String getName() { return name; }

	public String getDescription() { return description; }

	@Override
	public int getRarityIndex() { return this.rarityIndex; }

	/**
	 * Returns the top-left corner of the location of this object in the
	 * {@link InventoryItems} it resides.
	 */
	public Point getLocationInInventory() { return this.locationInInventory; }

	public Dimension getDimensionInInventory() { return dimensionInInventory; }

	public CurrencySet getSellPrice() { return sellPrice; }

	@Override
	public ObjectWithID getOwner() { return owner; }

	@Override
	public GModality getGameModality() { return this.gameModality; }

	@Override
	public AbstractShape2D getShape() { return this.shape; }

	@Override
	public void setShape(AbstractShape2D shape) { this.shape = shape; }

	//

	@Override
	public void setGameModality(GModality gameModality) { this.gameModality = gameModality; }

	@Override
	public void setOwner(ObjectWithID owner) { this.owner = owner; }

	public void setSellPrice(CurrencySet sellPrice) { this.sellPrice = sellPrice; }

	public void setDimensionInInventory(Dimension dimensionInInventory) {
		this.dimensionInInventory = dimensionInInventory;
	}

	public void setName(String name) { this.name = name; }

	public void setDescription(String description) { this.description = description; }

	@Override
	public RarityHolder setRarityIndex(int rarityIndex) {
		this.rarityIndex = rarityIndex;
		return this;
	}

	/**
	 * See {@link #getDimensionInInventory()};
	 *
	 * @param locationInInventory
	 */
	public void setLocationInInventory(Point locationInInventory) { this.locationInInventory = locationInInventory; }

	//

	@Override
	public void pickMeUp(GModalityRPG gmRPG, InteractingObj pickingUpPerformer) {
		if (pickingUpPerformer instanceof InventoryHolder) {
			((InventoryHolder) pickingUpPerformer).addToInventory(this);
			this.setOwner(pickingUpPerformer);
			this.onPickUp(gmRPG);
		}
	}

	@Override
	public void acceptInteractionFrom(InteractingObj interactionPerformer) {
		GModality gm;
		gm = this.getGameModality();
		if (interactionPerformer instanceof InventoryHolder && gm instanceof GModalityRPG) {
			this.pickMeUp((GModalityRPG) gm, interactionPerformer);
		}
	}
}