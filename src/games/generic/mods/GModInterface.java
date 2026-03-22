package games.generic.mods;

import java.util.Map;
import java.util.Set;

import games.generic.controlModel.GController;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.misc.GModalityFactory;

/**
 * Represent the point where a "mod" of a game can be defined, collected and
 * added to the main original game.<br>
 * It has to provide a set of features, interfaces, definitions, loaders etc.
 *
 * @author ottin
 *
 */
public abstract class GModInterface implements ObjectNamed {

	public GModInterface(GController gameController) {
		super();
		this.gameController = gameController;
	}

	protected final GController gameController;

	//

	/**
	 * Should return a static String with this mod name
	 * <p>
	 * Inherited Documentation:<br>
	 * {@inheritDoc}
	 */
	@Override
	public abstract String getName();

	public abstract Map<String, GModalityFactory> getGModalityFactories();

	public abstract Set<LoaderGeneric> getLoaders();
}
