package games.generic.controlModel.objects;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.GEventManager;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventDestructionObj;
import games.generic.controlModel.holders.GModalityHolder;

/**
 * Denotes an object that could be destroyed and removed from the game(i.e. has
 * a kind of "state", which one of its value is "destroyed") and could fire
 * events accordingly.
 */
public interface DestructibleObject extends GameObjectGeneric, GModalityHolder, GEventObserver {

	/**
	 * Flag-like method to check if this has been destroyed (and hopefully removed
	 * from the game). <br>
	 * Could be used to check, for instance, if targets are still valid, or if the
	 * tracked object should be still tracked, of if the object should be
	 * activated/animated/"called", etc (for example: a destroyed trap should not
	 * trigger).
	 */
	public boolean isDestroyed();

//	public void setIsDestroyed(boolean isDestroyed);

	/**
	 * Read-only test, that shouldn't have side effects, to check if this object
	 * should be destroyed and then removed (or meant to evolve its internal state),
	 * optionally firing appropriated events accordingly.
	 */
	public boolean shouldBeDestroyed(); // semplice flag o cosa computata, per esempio verificando se vita<=0

	/**
	 * MAKE ME DIE
	 * <P>
	 * Apply the destruction (also setting flags so that {@link #isDestroyed()}
	 * returns <code>true</code>) and performs clean-up operations.
	 */
	public boolean destroy();

	//

	@Override
	public default int getObserverPriority() { return GEventObserver.MIN_PRIORITY; }

	/**
	 * Simply checks if the given �{@link IGEvent} is a destruction event.<<br>
	 * Used in {@link #checkAndFireDestruction()}.
	 */
	public boolean isDestructionEvent(IGEvent maybeDestructionEvent);

	/**
	 * Returns a standardized String (one for each game which object(s) could be
	 * destroyed) to identify the destruction {@link IGEvent}.
	 */
	// not necessary since the event cannot be fired there
//	public String getDestructionEventName();

	/**
	 * When this object is being destroyed (that means: "during the
	 * {@link #destroy()} call") this event should be fired, in case of complex
	 * games, to notify all objects that "responds to a damage-received event" that
	 * this kind of event has occurred, also to make the "objects manager" to remove
	 * this object from the game.
	 * <p>
	 * Note: Originally, the parameter was an instance of {@link GEventManager}, now
	 * it's generalized to allow simpler event notification systems.
	 *
	 * @return an instance of {@link EventDestructionObj} that indicates if
	 *         <code>this</code> object should really call {@link #destroy()} or not
	 *         (through {@link EventDestructionObj#isDestructionValid()}).
	 */
	public EventDestructionObj fireDestructionEvent(GModality gm);

	/**
	 * Check if this instance should be destroyed, fire the destruction's event by
	 * calling {@link #fireDestructionEvent(GEventManager)} and waits if this object
	 * could really be destroied .
	 * <p>
	 * Algorithm pattern designed.
	 */
	public default boolean checkAndFireDestruction() {
		if (isDestroyed())
			return false;
		if (shouldBeDestroyed()) {
			fireDestructionEvent(getGameModality());
			return true;
		}
		return false;
	}

	/**
	 * Algorithm pattern designed implementation for detecting destruction events
	 * (my own, at least)..
	 */
	@Override
	public default void notifyEvent(GModality modality, IGEvent ge) {
		EventDestructionObj doe;
		if (!(this.isDestructionEvent(ge) && (ge instanceof EventDestructionObj)))
			return;
		doe = (EventDestructionObj) ge;
		if (this != doe.getDestructibleObject())
			return; // not me
		if (doe.isDestructionValid())
			destroy();
	}

	// workaround
	@Override
	public default Long getObserverID() { return getID(); }
}