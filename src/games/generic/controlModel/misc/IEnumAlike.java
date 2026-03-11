package games.generic.controlModel.misc;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.providers.EnumBasedObjectProvider;
import tools.json.JSONable;
import tools.json.types.JSONObject;

/**
 * Marker interface to define {@link Enum}s to both uniformize and simplify
 * their handling and its JSON-serialization. <br>
 * Refer to
 * {@link GameObjectsProvidersHolder#registerEnumBasedProvider(Class, EnumBasedObjectProvider)},
 * {@link GameObjectsProvidersHolder#getEnumBasedProviderByClass(Class, String)}
 * and {@link EnumBasedObjectProvider} to get hints on how override the
 * {@link JSONable}'s methods to properly serialize/deserialize the implementing
 * {@link Enum}s (i.e., retrieve the Enum's instances, rather than istantiating
 * new {@link ObjectNamed}).
 */
public interface IEnumAlike extends IndexableObject {
	@Override
	public default int getIndex() {
		return this.getID().intValue();
	}

	//

	// JSON-related

	//

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		IndexableObject.super.toJSONValue(wrapper); // continue the chain of "super. ..."
	}

	public default String getExceptionMessageLoadJSON() {
		return "The caller should obtain this instance via gm.getGameObjectsProvider().getEnumBasedProviderByClass( "
				+ this.getClass().getName() + ".class).";
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) {
		this.raiseUnsupportedOperationException(this.getExceptionMessageLoadJSON());
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) {
		this.raiseUnsupportedOperationException(this.getExceptionMessageLoadJSON());
	}
}
