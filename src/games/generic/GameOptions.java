package games.generic;

import games.generic.controlModel.GController;

/** Set of game options, in general, loaded from a configuration. */
public abstract class GameOptions {

	public GameOptions(GController gc) { this.gameController = gc; }

	protected final GController gameController;

	public GController getGameController() { return gameController; }
}