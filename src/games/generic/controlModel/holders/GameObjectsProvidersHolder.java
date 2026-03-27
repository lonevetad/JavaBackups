package games.generic.controlModel.holders;

import java.util.Map;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.currency.CurrencySet;
import games.generic.controlModel.items.EquipmentSet;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.IEnumAlike;
import games.generic.controlModel.providers.EnumBasedObjectProvider;
import tools.json.JSONable;

/**
 * One of the core classes.
 * <p>
 * Holds and provides a set of {@link GameObjectsProvider}, each identified by a
 * name (usually, the instance class name, but it's not mandatory).
 */
public interface GameObjectsProvidersHolder extends GModalityHolder {

	public void initialize();

	public Map<String, GameObjectsProvider<? extends ObjectNamed>> getProviders();

	public default GameObjectsProvider<? extends ObjectNamed> getProvider(String name) {
		return getProviders().get(name);
	}

	public default void addProvider(String name, GameObjectsProvider<? extends ObjectNamed> provider) {
		getProviders().put(name, provider);
	}

	/**
	 * See {@link #getEnumBasedProviderByClass(Class)}.
	 * 
	 * @param <E>
	 * @param enumClassName
	 * @param provider
	 */
	public default <E extends IEnumAlike> void registerEnumBasedProvider(String enumClassName,
			EnumBasedObjectProvider<E> provider) {
		this.addProvider(enumClassName, provider);
	}

	/**
	 * See {@link #registerEnumBasedProvider(String, EnumBasedObjectProvider)}.
	 * 
	 * @param <E>
	 * @param enumClass
	 * @param provider
	 */
	public default <E extends IEnumAlike> void registerEnumBasedProvider(Class<E> enumClass,
			EnumBasedObjectProvider<E> provider) {
		this.registerEnumBasedProvider(enumClass.getName(), provider);
	}

	/**
	 * See {@link #getEnumBasedProviderByClass(Class, String)}, where the class's
	 * name ({@link Class#getName()}) is passed as the name parameter.
	 * 
	 * @param <E>
	 * @param enumClass
	 * @return
	 */
	public default <E extends IEnumAlike> EnumBasedObjectProvider<E> getEnumBasedProviderByClass(Class<E> enumClass) {
		return this.getEnumBasedProviderByClass(enumClass, enumClass.getName());
	}

	/**
	 * Used in {@link JSONable}'s methods to recycle the Enum's instances. The same
	 * "name" used upon calling
	 * {@link #registerEnumBasedProvider(String, EnumBasedObjectProvider)} is
	 * required.
	 * 
	 * @param <E>
	 * @param enumClass
	 * @return
	 */
	public default <E extends IEnumAlike> EnumBasedObjectProvider<E> getEnumBasedProviderByClass(Class<E> enumClass,
			String name) {
		GameObjectsProvider<? extends ObjectNamed> p = this.getProvider(name);
		if (!(EnumBasedObjectProvider.class.isAssignableFrom(p.getClass()))) {
			throw new IllegalStateException("The provider for class \"" + enumClass.getName() + "\" and key \"" + name
					+ "\" is not an EnumBasedObjectProvider");
		}
		return (EnumBasedObjectProvider<E>) p;
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

	public abstract EquipmentSet newEquipmentSet();

}