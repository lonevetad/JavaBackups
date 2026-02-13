package games.generic.controlModel.holders;

import java.util.Map;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.misc.GameObjectsProvider;

/**
 * One of the core classes.
 * <p>
 * Holds and provides a set of {@link GameObjectsProvider}, each identified by a
 * name (usually, the instance class name, but it's not mandatory).
 */
public interface GameObjectsProvidersHolder {

	public Map<String, GameObjectsProvider<? extends ObjectNamed>> getProviders();

	public default GameObjectsProvider<? extends ObjectNamed> getProvider(String name) {
		return getProviders().get(name);
	}

	public default void addProvider(String name, GameObjectsProvider<? extends ObjectNamed> provider) {
		getProviders().put(name, provider);
	}
}