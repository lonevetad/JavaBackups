package games.old;

import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import tools.ObjectNamedID;

/**
 * Holds data of the player.<br>
 * There are two main subclasses, having lots of differences:
 * {@link PlayerGeneric} and {@link UserAccountGeneric} / "in-menu".<br>
 * For instance, in a trading card game, the "outside" player could be
 * represented by all card's collection and the set of decks, while the "in
 * game" player is the player having life, a deck, a board, an hand, a
 * "graveyard", etc.
 */
@Deprecated
public abstract class PlayerGeneric_OLD implements ObjectNamedID {

	// TODO invent stuffs
	protected String name;

	public PlayerGeneric_OLD() {
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}