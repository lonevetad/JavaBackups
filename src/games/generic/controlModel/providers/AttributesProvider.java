package games.generic.controlModel.providers;

import games.generic.controlModel.attributes.AttributeIdentifier;

public abstract class AttributesProvider<E extends Enum<E> & AttributeIdentifier>
		extends EnumBasedObjectProvider<E> {

	public AttributesProvider() {
		super();
	}

}
