package games.generic.controlModel.holders;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.objects.GameObjectGeneric;

/**
 * Marks the class as having a set of abilities. It's implemented as a
 * {@link Map} to make easier to check if this instance has a particular ability
 * and/or to remove it.
 */
public interface AbilitiesHolder extends GameObjectGeneric {
	/**
	 * See this class documentation to understand why this is a {@link Map} and not
	 * a {@link Set}.
	 */
	public Map<String, AbilityGeneric> getAbilities();

	public default void forEachAbilities(BiConsumer<String, AbilityGeneric> action) {
		this.getAbilities().forEach(action);
	}

	@Override
	public default void addMeToGame(GModality gm) {
		GameObjectGeneric.super.addMeToGame(gm);
		forEachAbilities((n, a) -> a.addMeToGame(gm));
	}

	@Override
	public default void onAddedToGame(GModality gm) {}

	@Override
	public default void removeMeToGame(GModality gm) {
		GameObjectGeneric.super.removeMeToGame(gm);
		forEachAbilities((n, a) -> a.removeMeToGame(gm));
	}

	@Override
	public default void onRemovedFromGame(GModality gm) {}

	public default AbilitiesHolder addAbility(AbilityGeneric ability) {
		if (ability == null) { return this; }
		this.getAbilities().put(ability.getName(), ability);
		return this;
	}

	public default AbilitiesHolder removeAbility(AbilityGeneric ability) {
		if (ability == null) { return this; }
		this.removeAbilityByName(ability.getName());
		return this;
	}

	public default AbilitiesHolder removeAbilityByName(String abilityName) {
		if (abilityName == null) { return this; }
		this.getAbilities().remove(abilityName);
		return this;
	}
}