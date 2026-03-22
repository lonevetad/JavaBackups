package games.generic.controlModel.misc;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;

public interface GModalityFactory {
	/**
	 * Build a new {@link GModality}. To add other parameters on that game
	 * modality, just design this interface's implementation.
	 */
	public GModality newGameModality(GController gc, String gameModalityName);
}