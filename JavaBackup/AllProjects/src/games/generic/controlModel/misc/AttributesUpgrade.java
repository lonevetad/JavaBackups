package games.generic.controlModel.misc;

import java.util.SortedSet;

import dataStructures.minorUtils.SortedSetEnhancedDelegating;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EssenceStorage;

/**
 * Simply consists in a named collection of {@link AttributeModification}.<br>
 * They differs from them because they are part of a definition of an
 * {@link EquipmentItem} (talking about "static definition", like those provided
 * in a database-like) while this class is just an "upgrade" provided randomly
 * to increase (or decrease) the quality (and the value, maybe) of the equipment
 * having it. <br>
 * (Also, in some game this upgrade could be extracted in some kind of
 * potion-essence like {@link EssenceStorage} and applied to another equipment,
 * that is a very useful and cool feature).
 */
public interface AttributesUpgrade
		extends RarityHolder, ObjectNamed, SortedSetEnhancedDelegating<AttributeModification> {

	/** BEWARE: do not modify the set. */
	public default SortedSet<AttributeModification> getAttributeModifiers() {
		return this.getDelegator();
	}

	/**
	 * Any attributes could apply a bonus or a malus to the price of everything it's
	 * applied on.
	 */
	public CurrencySet getPricesModifications();

	public void setPricesModifications(CurrencySet priceModifications);

	//

	public default AttributesUpgrade addAttributeModifier(AttributeModification am) {
		if (am == null) { return null; }
		getAttributeModifiers().add(am);
		return this;
	}

	/** Should call {@link #addAttributeModifier(AttributeModification)}. */
	public default AttributesUpgrade addAttributeModifier(AttributeIdentifier ai, int value) {
		return this.addAttributeModifier(new AttributeModification(ai, value));
	}
}