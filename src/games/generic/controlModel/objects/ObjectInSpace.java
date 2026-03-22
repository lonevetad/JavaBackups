package games.generic.controlModel.objects;

import geometry.ObjectLocated;
import geometry.ObjectShaped;

/**
 * Defines an object designed to be put in a "space-based context". Implements:
 * {@link ObjectShaped} (inherit {@link ObjectLocated}) and
 * {@link GameObjectGeneric}.
 */
public interface ObjectInSpace extends ObjectShaped, GameObjectGeneric {
}