package games.generic.controlModel.items;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.holders.InventoryHolder;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import games.generic.controlModel.objects.AssignableObject;
import games.generic.controlModel.objects.DroppableObj;
import games.generic.controlModel.objects.InteractingObj;
import games.generic.controlModel.subimpl.GModalityRPG;
import geometry.AbstractShape2D;
import tools.ObjectWithID;
import tools.UniqueIDProvider;
import tools.impl.OWIDLongImpl;
import tools.json.JSONTypes;
import tools.json.JSONValue;
import tools.json.types.JSONInt;
import tools.json.types.JSONObject;

/**
 * Marker interface for elements that can be placed into the inventory. <br>
 * Used in RPGS
 */
public abstract class InventoryItem extends OWIDLongImpl
		implements RarityHolder, DroppableObj, AssignableObject {
	private static final long serialVersionUID = 47104252L;

	public static final String FIELD_SELL_PRICE = "sellPrice";
	private static UniqueIDProvider UIDP_INVENTORY = UniqueIDProvider.newBasicIDProvider();
	public static final UIDProviderLoadedListener UIDP_LOADED_LISTENER_INVENTORY = uidp -> {
		if (uidp != null) {
			UIDP_INVENTORY = uidp;
		}
	};

	public static UniqueIDProvider getUniqueIDProvider_Event() {
		return UIDP_INVENTORY;
	}

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

	protected void assignID() {
		this.ID = UIDP_INVENTORY.getNewID();
	}

	@Override
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int getRarityIndex() {
		return this.rarityIndex;
	}

	/**
	 * Returns the top-left corner of the location of this object in the
	 * {@link InventoryItems} it resides.
	 */
	public Point getLocationInInventory() {
		return this.locationInInventory;
	}

	public Dimension getDimensionInInventory() {
		return dimensionInInventory;
	}

	public CurrencySet getSellPrice() {
		return sellPrice;
	}

	@Override
	public ObjectWithID getOwner() {
		return owner;
	}

	@Override
	public GModality getGameModality() {
		return this.gameModality;
	}

	@Override
	public AbstractShape2D getShape() {
		return this.shape;
	}

	@Override
	public void setShape(AbstractShape2D shape) {
		this.shape = shape;
	}

	//

	@Override
	public void setGameModality(GModality gameModality) {
		this.gameModality = gameModality;
	}

	@Override
	public void setOwner(ObjectWithID owner) {
		this.owner = owner;
	}

	public void setSellPrice(CurrencySet sellPrice) {
		this.sellPrice = sellPrice;
	}

	public void setDimensionInInventory(Dimension dimensionInInventory) {
		this.dimensionInInventory = dimensionInInventory;
	}

	@Override
	public ObjectNamed setName(String name) {
		this.name = name;
		return this;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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
	public void setLocationInInventory(Point locationInInventory) {
		this.locationInInventory = locationInInventory;
	}

	//

	@Override
	public void pickMeUp(GModalityRPG gmRPG, InteractingObj pickingUpPerformer) {
		if (pickingUpPerformer instanceof InventoryHolder inventoryHolder) {
			inventoryHolder.addToInventory(this);
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

	// super-chain of calls

	@Override
	public void onAddedToGame(GModality gm) {
		AssignableObject.super.onAddedToGame(gm);
	}

	@Override
	public void onAddingToOwner(GModality gm) {
		AssignableObject.super.onAddingToOwner(gm);
	}

	@Override
	public void onRemovedFromGame(GModality gm) {
		AssignableObject.super.onRemovedFromGame(gm);
	}

	//

	// JSON-related

	//

	@Override
	public void toJSONValue(JSONObject wrapper) {
		RarityHolder.super.toJSONValue(wrapper);
		super.toJSONValue(wrapper);
		//
		JSONInt jsonedIndexRarity = new JSONInt(this.getRarityIndex());
		wrapper.addField(FIELD_RARITY_INDEX, jsonedIndexRarity);
	}

	@Override
	public void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		RarityHolder.super.loadFromJSONObject(gm, wrapper);
		super.loadFromJSONObject(gm, wrapper);
		//
		if (!wrapper.hasField(FIELD_SELL_PRICE)) {
			this.raiseExceptionMissingField(FIELD_SELL_PRICE, JSONTypes.Object);
		}
		JSONValue sellPriceJSONVal = wrapper.getFieldValue(FIELD_SELL_PRICE);
		if (!sellPriceJSONVal.isType(JSONTypes.Object)) {
			this.raiseExceptionIllegalTypeField(FIELD_SELL_PRICE, JSONTypes.Object, sellPriceJSONVal);
		}
		CurrencySet cs = gm.newCurrencyHolder();
		cs.loadFromJSONObject(gm, (JSONObject) sellPriceJSONVal);
		this.setSellPrice(cs);
	}

	@Override
	public void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		if (jsonMap == null) {
			throw new IllegalArgumentException("Provided JSON map cannot be null");
		}
		RarityHolder.super.loadFromJSONMap(gm, jsonMap);
		super.loadFromJSONMap(gm, jsonMap);
		//
		if (!jsonMap.containsKey(FIELD_SELL_PRICE) || !(jsonMap.get(FIELD_SELL_PRICE) instanceof Map<?, ?>)) {
			this.raiseExceptionIllegalTypeField(FIELD_SELL_PRICE, JSONTypes.Object, jsonMap.get(FIELD_SELL_PRICE));
		}
		Map<String, Object> sellPriceMap = (Map<String, Object>) jsonMap.get(FIELD_SELL_PRICE);
		CurrencySet cs = gm.newCurrencyHolder();
		cs.loadFromJSONMap(gm, sellPriceMap);
		this.setSellPrice(cs);
	}

}