package games.generic.controlModel.objects;

import tools.ObjectWithID;

/**
 * Defines an object who <i>can</i> interact with something else (an
 * {@link InteractableObject} through
 * {@link InteractableObject#acceptInteractionFrom(InteractingObj}).
 */
public interface InteractingObj extends ObjectWithID {
	public default void interactWith(InteractableObject obj) { obj.acceptInteractionFrom(this); }
}
