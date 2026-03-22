package games.generic.controlModel.abilities.impl;

import games.generic.controlModel.attributes.AttributeIdentifier;
import games.generic.controlModel.attributes.AttributeModification;

/**
 * It just defines a set of functions, needed to
 * {@link AbilityModifyingAttributesRealTime}.
 */
public interface HelperWithAttributeModifications {

	public AttributeModification newAttributeModification(AttributeIdentifier ai, int indexRarity);

	public void setAttributesToModify(AttributeModification[] attributesModifications);
}
