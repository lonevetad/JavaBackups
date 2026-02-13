package games.generic.controlModel.subimpl;

import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.loaders.LoaderGameObjects;
import games.generic.controlModel.misc.GameObjectsProvider;

public abstract class LoaderAbilities extends LoaderGameObjects<AbilityGeneric> {

	public LoaderAbilities(GameObjectsProvider<AbilityGeneric> objProvider) {
		super(objProvider);
	}

}