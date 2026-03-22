package games.generic.controlModel.providers;

import java.util.Map;

import games.generic.controlModel.GModality;

public interface FactoryGeneric<C> {

	public C create(GModality gm, Object nameOrID, Map<String, Object> constructorParameters);

	//

	public default C create(GModality gm, Map<String, Object> constructorParameters) {
		return this.create(gm, null, constructorParameters);
	}

	public default C create(GModality gm, Object nameOrID) {
		return this.create(gm, nameOrID, null);
	}

	public default C create(GModality gm) {
		return this.create(gm, null, null);
	}
}
