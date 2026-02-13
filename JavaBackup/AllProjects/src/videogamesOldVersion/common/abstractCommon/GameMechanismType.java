package common.abstractCommon;

import java.io.Serializable;

import videogamesOldVersion.common.GameMechanism;

/**
 * Designed to be implemented by a {@link Enum}.<br>
 * This interface is used to identify a {@link GameMechanism} and create a new
 * instance of that class.<br>
 * This interface is useful on games having different kind of game-modes.
 */
public interface GameMechanismType extends Serializable {

	public int ordinal();

	public String name();

	public GameMechanism newGameMechanism(MainController mainController);

}