package games.generic.controlModel.misc;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.holders.GameObjectsProvidersHolder;
import games.generic.controlModel.providers.EnumBasedObjectProvider;
import tools.json.JSONable;

/**
 * Marker interface to define {@link Enum}s to both uniformize and simplify
 * their handling and its JSON-serialization. <br>
 * Refer to
 * {@link GameObjectsProvidersHolder#registerEnumBasedProvider(Class, EnumBasedObjectProvider)},
 * {@link GameObjectsProvidersHolder#getEnumBasedProviderByClass(Class, String)}
 * and {@link EnumBasedObjectProvider} to get hints on how override
 * the {@link JSONable}'s methods to properly serialize/deserialize the
 * implementing {@link Enum}s (i.e., retrieve the Enum's instances, rather than
 * istantiating new {@link ObjectNamed}).
 */
public interface IEnumAlike extends IndexableObject {
    public default int getIndex() {
        return this.getID().intValue();
    }
}
