package games.generic.controlModel.misc;

import games.generic.controlModel.GMap;
import games.generic.controlModel.GModality;

public class GMapProvider extends GameObjectsProvider<GMap> {
	public static final String NAME_FOR_GOPROVIDER = "mapProv";

	public GMapProvider() {
		super();
	}

	/** Should be preferred over {@link #getObjIdentifiedByID(Integer)}. */
	public GMap getMapByName(GModality gm, String name) {
		if (name == null)
			return null;
//		return getObjByName(name).newInstance(gm);
		return getNewObjByName(gm, name);
	}
}