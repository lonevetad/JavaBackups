package games.generic.controlModel.objects.creature;

import java.util.Map;
import java.util.function.BiConsumer;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityAllocation;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.holders.AbilitiesHolder;
import games.generic.controlModel.holders.AttributesHolder;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.holders.RarityHolder;
import games.generic.controlModel.loaders.LoaderUniqueIDProvidersState.UIDPState;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.DestructibleObject;
import games.generic.controlModel.objects.GameObjectGeneric;
import games.generic.controlModel.objects.LivingObject;
import games.generic.controlModel.objects.MovingObject;
import games.generic.controlModel.objects.ObjectInSpace;
import games.generic.controlModel.objects.TimedObject;
import tools.ObjectNamedID;
import tools.UniqueIDProvider;

/**
 * Base interface for a "creature" for almost ALL games (RPG, RTS, etc).
 * <p>
 * Useful classes/interfaces used here:
 * <ol>
 * <li>{@link AttributesHolder} (holding: {@link CreatureAttributes})</li>
 * <li>{@link MovingObject}</li>
 * <li>{@link TimedObject} (inherited by 2)</li>
 * <li>{@link ObjectInSpace} (inherited by 2)</li>
 * <li>{@link LivingObject}</li>
 * <li>{@link DestructibleObject} (inherited by 5)</li>
 * <li>{@link GEventObserver} (inherited by 6 but independent in its
 * functionality and concept, because this instance could listen other
 * events)</li>
 * <li>{@link RarityHolder}: NPC or enemy creatures could be spawned randomly
 * depending on its rarity</li>
 * </ol>
 */
public interface CreatureSimple
		extends AttributesHolder, LivingObject, MovingObject, DamageDealerGeneric, AbilitiesHolder, //
		RarityHolder, GModalityHolder, GameObjectGeneric, ObjectNamedID {
	/**
	 * Since creatures can't be stored there's no need to store the
	 * {@link UniqueIDProvider } {@link UIDPState}
	 */
	public static final UniqueIDProvider UIDP_CREATURE = UniqueIDProvider.newBasicIDProvider();

	public static final int TICKS_PER_SECONDS = 4, LOG_TICKS_PER_SECONDS = 2;
	public static final int MILLIS_REGEN_LIFE_MANA = 1000 / TICKS_PER_SECONDS;

	@Override
	public default int getRarityIndex() { return 0; }

	//

	@Override
	public default RarityHolder setRarityIndex(int rarityIndex) { return this; }

	@Override
	public default void act(GModality modality, int timeUnits) {
		if (isDestroyed())
			return;
		MovingObject.super.act(modality, timeUnits);
//		LivingObject.super.act(modality, timeUnits); // this is the same of the below :
//		ObjectHealing.super.act(modality, timeUnits);
		LivingObject.super.rechargeResources();
	}

	// TODO DOCS AND SUPPORT METHODS, LIKE A for each
	/**
	 * Similar to {@link #getAbilities()}, but returning a map of
	 * {@link AbilityAllocation}.
	 */
	public Map<String, AbilityAllocation> getAbilitiesAllocations();

	public default void forEachAbilityAllocations(BiConsumer<String, AbilityAllocation> action) {
		this.getAbilitiesAllocations().forEach(action);
	}

	//

	@Override
	public default void initSetRechargeableResources() { LivingObject.super.initSetRechargeableResources(); }

	@Override
	public default int getLuckPerThousand() { return LivingObject.super.getLuckPerThousand(); }
}