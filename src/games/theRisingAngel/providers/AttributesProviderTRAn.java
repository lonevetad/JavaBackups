package games.theRisingAngel.providers;

import games.generic.controlModel.providers.AttributesProvider;
import games.theRisingAngel.enums.AttributesTRAn;

public class AttributesProviderTRAn extends AttributesProvider<AttributesTRAn> {

    public AttributesProviderTRAn() {
        super();
    }

    @Override
    public AttributesTRAn[] getEnumValues() {
        return AttributesTRAn.ALL_ATTRIBUTES;
    }
}
