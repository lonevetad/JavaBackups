package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GEventObserver;

/**
 * Deprecated because I don't know how to implement an ability that tracks a
 * damage dealt to check the destruction of an object BUT that forgets the
 * tracked damage (and the object that has received the damage) if the damage
 * wasn't lethal.
 */
@Deprecated
public interface AbilityOnKill extends AbilityGeneric, GEventObserver {

}