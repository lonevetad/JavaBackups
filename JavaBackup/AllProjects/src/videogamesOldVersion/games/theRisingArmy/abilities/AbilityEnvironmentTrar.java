package games.theRisingArmy.abilities;

import java.io.Serializable;

import videogamesOldVersion.games.theRisingArmy.MainController_TheRisingArmy;
import videogamesOldVersion.games.theRisingArmy.abstractTRAr.AbstractAbilityTRAr;
import videogamesOldVersion.games.theRisingArmy.abstractTRAr.GameObjectTRAr;

/**
 * Define a tiny set of variables needed by {@link AbstractAbilityTRAr}.<br>
 * NODE: This class implements the Singleton pattern, so save it fields before re-use.
 */
public class AbilityEnvironmentTrar implements Serializable {
	private static final long serialVersionUID = -560154106L;
	public static final AbilityEnvironmentTrar instance = new AbilityEnvironmentTrar();

	private AbilityEnvironmentTrar() {
	}

	// public static AbilityTrarEnvironment instance() {return instance;}

	public GameObjectTRAr caster;
	public MainController_TheRisingArmy controller;

	public AbilityEnvironmentTrar set(MainController_TheRisingArmy controller, GameObjectTRAr caster) {
		this.controller = controller;
		this.caster = caster;
		return this;
	}
}