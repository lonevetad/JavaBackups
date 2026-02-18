package games.generic.controlModel.abilities;

import java.util.function.Function;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.AttributesHolder;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import games.generic.controlModel.misc.uidp.UIDPLoadableFacade;
import games.generic.controlModel.objects.AssignableObject;
import games.generic.controlModel.objects.DestructibleObject;
import games.generic.controlModel.objects.GameObjectGeneric;
import tools.ObjectWithID;
import tools.UniqueIDProvider;

public interface AbilityGeneric extends AssignableObject, RarityHolder, GameObjectGeneric {
	public static final Function<AbilityGeneric, String> NAME_EXTRACTOR = AbilityGeneric::getName;// e -> e.getName();
	public static final UniqueIDProvider UIDP_ABILITY = new UIDPLoadableFacade<>(AbilityGeneric.class);
	@SuppressWarnings("unchecked")
	public static final UIDProviderLoadedListener UIDP_LOADED_LISTENER_ABILITY = ((UIDPLoadableFacade<AbilityGeneric>) UIDP_ABILITY)
			.getUidpLoaderListener();

	@Override
	public ObjectWithID getOwner();

	public int getLevel();

	/** A.k.a. "the caster" */
	@Override
	public void setOwner(ObjectWithID owner);

	public void setLevel(int level);

	/**
	 * Perform the ability. The second parameter ("largetLevel") specify the level
	 * this ability implementation should rely on.
	 */
	public void performAbility(GModality gm, int targetLevel);

	/**
	 * Calls {@link #performAbility(GModality, int)} providing {@code 0} as the
	 * second parameter.
	 */
	public default void performAbility(GModality gm) { this.performAbility(gm, 0); }

	/**
	 * Detects and returns if the ability can be performed. Should be called inside
	 * {@link #performAbility(GModality)}
	 */
	public default boolean canBePerformed(GModality gm) {
		ObjectWithID o;
		o = getOwner();
		if (o == null)
			return true;
		if (o instanceof DestructibleObject)
			return !((DestructibleObject) o).isDestroyed();
		return true;
	}

	/**
	 * Reset the ability to its original state. For instance, if this ability tracks
	 * a set of objects, then clear that set, or if it acts over time then reset the
	 * timer(s) to zero.
	 */
	public void resetAbility();

	/**
	 * Simply invokes the more friendly-named {@link #resetAbility()}.
	 * <p>
	 * Inherited documentation:<br>
	 * {@inheritDoc}
	 */
	@Override
	public default void resetStuffs() { resetAbility(); }

	/**
	 * Clone the ability.
	 */
//	public AbilityGeneric cloneAbility();

	/**
	 * Returns by default <code>0</code>.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public default int getRarityIndex() { return 0; }

	@Override
	public default RarityHolder setRarityIndex(int rarityIndex) { return this; }

	@Override
	public default void onAddedToGame(GModality gm) {
		// nothing to do here, right now, except from ...
		AssignableObject.super.onAddedToGame(gm);
		resetAbility();
	}

	@Override
	public default void onAddingToOwner(GModality gm) {
		AssignableObject.super.onAddingToOwner(gm);
		resetAbility();
	}

	@Override
	public default void onRemovedFromGame(GModality gm) {
		AssignableObject.super.onRemovedFromGame(gm);
		resetAbility();
	}

//

	// TODO UTILS

	public default CreatureAttributes getOwnerAttributes() {
		ObjectWithID o;
		o = getOwner();
		if (o == null || (!(o instanceof AttributesHolder)))
			return null;
		return ((AttributesHolder) o).getAttributes();
	}

	/**
	 * Override designed.<br>
	 * Perform clean-up actions, by default by calling {@link #resetAbility()}, in
	 * certain moments, like the death of the owner, the un-equipment of the
	 * belonging {@link EquipmentItem}, etc.
	 */
	public default void onRemoving(GModality gm) {
		resetAbility();
		setOwner(null);
	}
}