package games.theRisingAngel;

import games.generic.controlModel.abilities.impl.HelperWithAttributeModifications;
import games.generic.controlModel.attributes.AttributeIdentifier;
import games.generic.controlModel.attributes.AttributeModification;
import games.theRisingAngel.misc.AttributeModificationTRAn;

public interface HelperWithAttributeModificationsTRAn extends HelperWithAttributeModifications {
	@Override
	public default AttributeModification newAttributeModification(AttributeIdentifier ai, int indexRarity) {
		return new AttributeModificationTRAn(ai, indexRarity);
	}
}
