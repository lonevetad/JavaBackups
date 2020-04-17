package games.generic.controlModel.inventoryAbil;

import java.util.Set;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.RarityHolder;

/**
 * Simply consists in a named collection of {@link AttributeModification}.<br>
 * They differs from them because they are part of a definition of an
 * {@link EquipmentItem} (talking about "static definition", like those provided
 * in a database-like) while this class is just an "upgrade" provided randomly
 * to increase (or decrease) the quality (and the value, maybe) of the equipment
 * having it. <br>
 * (Also, in some game this upgrade could be extracted in some kind of
 * potion-essence and applied to another equipment, that is a very useful and
 * cool feature). aaa
 */
public interface AttributesUpgrade extends RarityHolder, ObjectNamed {
	public Set<AttributeModification> getAttributeModifiers();

	public default AttributesUpgrade addAttributeModifier(AttributeModification am) {
		if (am == null)
			return null;
		getAttributeModifiers().add(am);
		return this;
	}

	/** Should call {@link #addAttributeModifier(AttributeModification)}. */
	public default AttributesUpgrade addAttributeModifier(AttributeIdentifier ai, int value) {
		if (ai == null)
			return null;
		getAttributeModifiers().add(new AttributeModification(ai, value));
		return this;
	}
}