package games.generic.controlModel.loaders;

import java.io.Serializable;

import tools.json.JSONValue;

/**
 * Defines an object as being able to be Loaded and save its state
 * <p>
 * Still an experimental version
 *
 * @author ottin
 *
 */
public interface ObjectLoadable extends Serializable {

	// TODO: creare i metodi per diventare una cosa salvabile (serializzare, toJSON,
	// altro) e per caricare

	public JSONValue toJSON();

}
