package games.generic.controlModel.misc;

import java.util.Map;

import games.generic.controlModel.GModality;
import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.providers.EnumBasedObjectProvider;
import tools.json.JSONValue;
import tools.json.JSONable;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

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
	public default JSONValue toJSONValue() {
		return new JSONString(this.getName());
	}

	@Override
	public default void toJSONValue(JSONObject wrapper) {
		wrapper.addField(FIELD_NAME, new JSONString(this.getName()));
	}

	public default String getExceptionMessageLoadJSON() {
		return "The caller should obtain this instance via gm.getGameObjectsProvider().getEnumBasedProviderByClass( "
				+ this.getClass().getName() + ".class).";
	}

	@Override
	public default void loadFromJSONObject(GModality gm, JSONObject wrapper) throws IllegalArgumentException {
		this.raiseUnsupportedOperationException(this.getExceptionMessageLoadJSON());
	}

	@Override
	public default void loadFromJSONMap(GModality gm, Map<String, Object> jsonMap) throws IllegalArgumentException {
		this.raiseUnsupportedOperationException(this.getExceptionMessageLoadJSON());
	}
}
