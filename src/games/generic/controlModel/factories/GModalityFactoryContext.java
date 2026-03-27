package games.generic.controlModel.factories;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;

/**
 * A set of data required to create a new instance of a {@link GModality}
 * through a {@link GModalityFactory}. It could hold anything, as like as a
 * (possibly shared) {@link GameObjectsProvidersHolder}
 */
public abstract class GModalityFactoryContext {

	private static GModalityFactoryContext sharedSingleton = null;

	public GModalityFactoryContext() {
	}

	protected GameObjectsProvidersHolder gameObjectsProvidersHolder;

	// GETTERS

	public GameObjectsProvidersHolder getGameObjectsProvidersHolder() {
		return gameObjectsProvidersHolder;
	}

	// SETTERS

	public void setGameObjectsProvidersHolder(GameObjectsProvidersHolder gameObjectsProvidersHolder) {
		this.gameObjectsProvidersHolder = gameObjectsProvidersHolder;
	}

	//

	public static GModalityFactoryContext getDefaultContext(boolean recycleSharedSingleton) {
		if (recycleSharedSingleton) {
			if (sharedSingleton == null) {
				sharedSingleton = getDefaultContext(false);
			}
			return sharedSingleton;
		}
		return new GModalityFactoryContext() {
		};
	}

	public static GModalityFactoryContext getDefaultContext() {
		return getDefaultContext(true);
	}
}
