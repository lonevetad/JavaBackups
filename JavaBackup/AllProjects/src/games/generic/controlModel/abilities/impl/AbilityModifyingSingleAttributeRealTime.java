package games.generic.controlModel.abilities.impl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.AttributeIdentifier;

/**
 * This class is designed to modify a single attribute (identified by a
 * {@link AttributeIdentifier}) through the extension of
 * {@link AbilityModifyingAttributesRealTime}.
 */
public abstract class AbilityModifyingSingleAttributeRealTime extends AbilityModifyingAttributesRealTime {
	private static final long serialVersionUID = 561320350155L;

	public AbilityModifyingSingleAttributeRealTime(GModality gm, String name, AttributeIdentifier attributeModified) {
		super(gm, name, new AttributeIdentifier[] { attributeModified });
	}
}