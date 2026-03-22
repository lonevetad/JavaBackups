package games.generic.controlModel.providers;

import games.generic.controlModel.damage.DamageTypeGeneric;

public abstract class DamageTypesProvider<E2 extends DamageTypeGeneric> extends EnumBasedObjectProvider<E2> {

	public DamageTypesProvider() {
		super();
	}

}
