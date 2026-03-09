package games.theRisingAngel.enums;

import games.generic.controlModel.GModality;
import games.generic.controlModel.attributes.AttributeModification;
import games.theRisingAngel.GModalityTRAnBaseWorld;
import games.theRisingAngel.providers.GameObjectsProvidersHolderTRAn;

public class AttributeModificationTRAn extends AttributeModification {

    public AttributeModificationTRAn(AttributesTRAn attributeModified, int value) {
        super(attributeModified, value);
    }

    @Override
    protected void loadInnerObjectNamedID(GModality gm, String name) {
        GModalityTRAnBaseWorld gmTRAn = (GModalityTRAnBaseWorld) gm;
        GameObjectsProvidersHolderTRAn goProvHolderTRAn = (GameObjectsProvidersHolderTRAn) gmTRAn
                .getGameObjectsProvider();
        AttributesTRAn attribute = goProvHolderTRAn.getEnumBasedProviderByClass(AttributesTRAn.valueOf(name));
        this.setAttributeModified(attribute);
    }

}
