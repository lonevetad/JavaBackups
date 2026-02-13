package games.generic.controlModel.objects.creature;

import java.util.Set;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventEnteringOnMap;
import games.generic.controlModel.events.event.EventLeavingMap;
import games.generic.controlModel.events.event.EventOnMap;
import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.providers.AbilitiesProvider;
import tools.ObjectNamedID;

/**
 * Marks a "class" of creatures which all of them shares all abilities.<br>
 * To do so, upon entering into the game map, shares all of those abilities.<br>
 * The same is true for the "removing" event, to updae that set.
 * <p>
 * IMPLEMENTATION NOTE:
 * <ul>
 * <li>Each "class" MUST define its own {@link #getCreatureGroupBelonging()} to
 * distinguish each of them.</li>
 * <li>The list returned by {@link GEventObserver#getEventsWatching()} MUST
 * include the concept "creature entering"</li>
 * <li></li>
 * </ul>
 */
public interface CreatureSharingAbilities extends BaseCreatureRPG, GEventObserver {

	/**
	 * Returns the "class" this creature belongs to. It's used to share the
	 * abilities with the correct group,
	 *
	 * @return
	 */
	public ObjectNamedID getCreatureGroupBelonging();

	/** Override designed. */
	public default boolean isEventCreatureEnteringOnMap(IGEvent event) {
		return event instanceof EventEnteringOnMap;
	}

	public default boolean isEventCreatureLeavingMap(IGEvent event) { return event instanceof EventLeavingMap; }

	public String getAbilityToShare();

	public Set<String> getSharedAbilities();

	public void setSharedAbilities(Set<String> abilities);

	@Override
	public default void notifyEvent(GModality modality, IGEvent ge) {
		boolean isEntering, isLeaving;
		EventOnMap eom;
		String abilSharedByObj;
		CreatureSharingAbilities csa;
		Set<String> sharedAbl;
		AbilityGeneric ability;
		GameObjectsProvidersHolderRPG providersHolder;
		AbilitiesProvider aProv;
		isEntering = isEventCreatureEnteringOnMap(ge);
		isLeaving = isEventCreatureLeavingMap(ge);
		if (!(isEntering || isLeaving))
			return;
		eom = (EventOnMap) ge;
		if (!(eom.getObjectInvolved() instanceof CreatureSharingAbilities))
			return;
		csa = (CreatureSharingAbilities) eom.getObjectInvolved();
		if (!this.getCreatureGroupBelonging().equals(csa.getCreatureGroupBelonging()))
			return;
		sharedAbl = csa.getSharedAbilities();
		abilSharedByObj = csa.getAbilityToShare();
		if (isEntering) {
			setSharedAbilities(sharedAbl);
			sharedAbl.add(abilSharedByObj);
			providersHolder = (GameObjectsProvidersHolderRPG) modality.getGameObjectsProvider();
			aProv = providersHolder.getAbilitiesProvider();
			ability = aProv.getAbilityByName(modality, abilSharedByObj);
			if (ability != null) {
				this.getAbilities().put(abilSharedByObj, ability);
				ability.setOwner(this);
				ability.onAddingToOwner(modality);
			}
		} else { // isLeaving
			sharedAbl.remove(abilSharedByObj);
			ability = this.getAbilities().get(abilSharedByObj);
			if (ability != null) {
				ability.onRemovingFromOwner(modality);
				// then remove it
				this.getAbilities().remove(abilSharedByObj);
			}
		}
	}

//	public default void forEachAbilities(Consumer<AbilityGeneric> action) {
//		getAbilities().forEach(action);
//getSharedAbilities().forEach(action);
//	}
}