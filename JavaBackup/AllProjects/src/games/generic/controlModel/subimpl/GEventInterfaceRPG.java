package games.generic.controlModel.subimpl;

import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.events.event.EventDestructionObj;
import games.generic.controlModel.misc.Currency;
import games.generic.controlModel.objects.DestructibleObject;

public interface GEventInterfaceRPG extends GEventInterface {

	// object living, moving and creature related

	/**
	 * Fire a destruction event. If the event has not been invalidated (by setting
	 * {@link EventDestructionObj#setDestructionValid(boolean)} to
	 * <code>false</code>), then the {@link DestructibleObject} (the second
	 * parameter) should destroy itself.
	 */
	public EventDestructionObj fireDestructionObjectEvent(GModalityET gaModality, DestructibleObject desObj);

	//

// misc

	public void fireCurrencyChangeEvent(GModalityET gm, Currency currency, int oldValue, int newValue);

	public abstract void fireExpGainedEvent(GModalityET gm, int expGained);

	public abstract void fireLevelGainedEvent(GModalityET gm, int levelGained);
}