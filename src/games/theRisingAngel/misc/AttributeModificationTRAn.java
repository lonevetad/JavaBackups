package games.theRisingAngel.misc;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.AttributeIdentifier;
import games.generic.controlModel.attributes.AttributeModification;
import games.theRisingAngel.enums.AttributesTRAn;

public class AttributeModificationTRAn extends AttributeModification {

	private static final long serialVersionUID = 3610769982230586716L;

	public static AttributeModification[] newEmptyArray(AttributeIdentifier[] attributesModified) {
		int n;
		AttributeModification[] r;
		r = new AttributeModification[n = attributesModified.length];
		while (--n >= 0) {
			r[n] = new AttributeModificationTRAn(attributesModified[n], 0);
		}
		return r;
	}

	public AttributeModificationTRAn(AttributeIdentifier attributeModified, int value) {
		super(attributeModified, value);
	}

	@Override
	public void loadInnerObjectNamedID(GModality gm, String typeName) {
		/*
		 * GModalityTRAnBaseWorld gmTRAn = (GModalityTRAnBaseWorld) gm;
		 * GameObjectsProvidersHolderTRAn goProvHolderTRAn =
		 * (GameObjectsProvidersHolderTRAn) gmTRAn .getGameObjectsProvider();
		 * goProvHolderTRAn.getEnumBasedProviderByClass()
		 */
		AttributesTRAn attribute = AttributesTRAn.valueOf(typeName);
		this.setAttributeModified(attribute);
	}

	@Override
	protected AttributeModification newEmptyAttributeModification(AttributeIdentifier attributeModified, int value) {
		return new AttributeModificationTRAn(attributeModified, value);
	}

}
