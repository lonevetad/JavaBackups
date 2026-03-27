package games.generic.controlModel.loaders;

import java.util.List;

import games.generic.controlModel.GController;
import games.generic.mods.GModInterface;

/**
 * A class that loads the games "mod"s (i.e. a {@link GModInterface}).
 *
 * <p>
 *
 * This class is NOT a {@link LoaderGeneric} because the {@link GController} HAS
 * to invoke this class BEFORE all loading ... unless some crazy magic with
 * reflection and the {@link ClassLoader} happens.
 * 
 * @author ottin
 */
public abstract class LoaderGMod extends LoaderGeneric {

	public LoaderGMod(GController gameController) {
		super();
		this.gameController = gameController;
	}

	protected final GController gameController;

	public GController getGameController() {
		return gameController;
	}

	/**
	 * Returns a list of all {@link GModInterface} that this loader can find.
	 */
	public abstract List<GModInterface> getAllLoadableGameMods();

}