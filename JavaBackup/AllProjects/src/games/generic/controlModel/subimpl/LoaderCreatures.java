package games.generic.controlModel.subimpl;

import games.generic.controlModel.loaders.LoaderGameObjects;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.objects.creature.CreatureSimple;

public abstract class LoaderCreatures extends LoaderGameObjects<CreatureSimple> {

	public LoaderCreatures(GameObjectsProvider<CreatureSimple> objProvider) {
		super(objProvider);
	}
}