package games.generic.controlModel.holders;

import java.util.Map;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.misc.GameObjectsProvider;

/**
 * One of the core classes.
 * <p>
 * Holds and provides a set of {@link GameObjectsProvider}, each identified by a
 * name (usually, the instance class name, but it's not mandatory).
 */
public interface GameObjectsProvidersHolder {

	public void initialize();

	public Map<String, GameObjectsProvider<? extends ObjectNamed>> getProviders();

	public default GameObjectsProvider<? extends ObjectNamed> getProvider(String name) {
		return getProviders().get(name);
	}

	public default void addProvider(String name, GameObjectsProvider<? extends ObjectNamed> provider) {
		getProviders().put(name, provider);
	}

	// other factories

	public abstract CurrencySet newCurrencyHolder();

	/**
	 * Instantiate a new Ability, given some kind of context to it (various
	 * initializiation/constructor parameters)
	 * 
	 * @param abilityName
	 * @param abilityContext
	 * @return
	 */
	public abstract AbilityGeneric newAbilityGeneric(String abilityName, Object abilityContext);

}