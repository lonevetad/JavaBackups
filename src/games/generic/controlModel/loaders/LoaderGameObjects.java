package games.generic.controlModel.loaders;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;

/**
 * Loads every definitions of a single game object's concept. See
 * {@link GameObjectsProvider} for examples of those concepts.
 * <p>
 * Those implementation could be a "hard coded" definition, where instances of
 * factories ({@link FactoryObjGModalityBased}) of this class generic parameter
 * are created directly by the code, or could rely to informations stored
 * somewhere. like a text file, a database, etc.
 */
public abstract class LoaderGameObjects<E extends ObjectNamed> extends LoaderGeneric {
	protected GameObjectsProvider<E> objProvider;

	public LoaderGameObjects(GameObjectsProvider<E> objProvider) { this.objProvider = objProvider; }

	public GameObjectsProvider<E> getObjProvider() { return objProvider; }

	public void setObjProvider(GameObjectsProvider<E> ombp) { this.objProvider = ombp; }

	public void saveObjectFactory(String identifierOrName, FactoryObjGModalityBased<E> objectFactory) {
		this.saveObjectFactory(identifierOrName, 0, objectFactory);
	}

	public void saveObjectFactory(String identifierOrName, int objectGroupIdentifier,
			FactoryObjGModalityBased<E> objectFactory) {
		this.getObjProvider().addObj(identifierOrName, objectGroupIdentifier, objectFactory);
	}
}