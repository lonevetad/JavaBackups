package games.old.rechargeable.resources;

import java.util.Set;

import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.objects.TimedObject;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import games.generic.controlModel.rechargeable.resources.examples.ExampleHealingType;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import tools.ObjectWithID;

@Deprecated
public interface HealingObject extends TimedObject, GModalityHolder { //

	/** Helper to realize the way to heal stuffs over time */
	public IHealingResourcesOverTimeStrategy getHealingStrategyHelper();

	/** Helper for this class, just return a field of this class. */
	public IHealableResourcesHolder getHealableResourcesHolder();

	/** See {@link #getHealingStrategyHelper()}. */
	public void setHealingStrategyHelper(IHealingResourcesOverTimeStrategy healingStrategyHelper);

	/** See {@link #getHealableResourcesHolder()}. */
	public void setHealableResourcesHolder(IHealableResourcesHolder curableResourcesHolder);

	/**
	 * "Init-like" method, used to set the Set of {@link #getAllHealableResources()}
	 * by calling {{@link #addHealableResource(HealableResource)}.
	 */
	public void defineAllHealableResources();

	//

	// delegators

	//

	/** Returns all healing types modeled by this class. */
	public default Set<RechargeableResourceType> getAllHealableResourceTypes() {
		return this.getHealableResourcesHolder().getHealableResourceTypes();
	}

	/** Returns all healing resources modeled by this class. */
	public default Set<HealableResource> getAllHealableResources() {
		return this.getHealableResourcesHolder().getHealableResources();
	}

	/** Retrives the resource associated with the given type. */
	public default HealableResource getHealableResourceFor(RechargeableResourceType healType) {
		return this.getHealableResourcesHolder().getHealableResourceFor(healType);
	}

	public default void addHealableResourceType(RechargeableResourceType healType) {
		this.getHealableResourcesHolder().addHealableResourceType(healType);
	}

	public default void addHealableResource(HealableResource healType) {
		this.getHealableResourcesHolder().addHealableResource(healType);
	}

	public default void removeHealableResourceType(RechargeableResourceType healType) {
		this.getHealableResourcesHolder().removeHealableResourceType(healType);
	}

	public default void removeHealableResource(HealableResource cr) {
		this.getHealableResourcesHolder().removeHealableResource(cr);
	}

	public default boolean containsHealableResourceType(RechargeableResourceType healType) {
		return this.getHealableResourcesHolder().containsHealableResourceType(healType);
	}

	public default boolean containsHealableResource(HealableResource cr) {
		return this.getHealableResourcesHolder().containsHealableResource(cr);
	}

	//

	public default int getHealableResourceAmount(ExampleHealingType shield) {
		return this.getHealableResourcesHolder().getHealableResourceAmount(shield);
	}

	public default int getHealableResourceAmountMax(RechargeableResourceType healType) {
		return this.getHealableResourcesHolder().getHealableResourceAmountMax(healType);
	}

	public default int getHealableResourceRegeneration(RechargeableResourceType healType) {
		return this.getHealableResourcesHolder().getHealableResourceRegeneration(healType);
	}

	//

	/** See {@link #getHealableResourceAmount(RechargeableResourceType)}. */
	public default void setHealableResourceAmount(RechargeableResourceType healType, int value) {
		this.getHealableResourcesHolder().setHealableResourceAmount(healType, value);
	}

	/** See {@link #getHealableResourceAmountMax(RechargeableResourceType)}. */
	public default void setHealableResourceAmountMax(RechargeableResourceType healType, int value) {
		this.getHealableResourcesHolder().setHealableResourceAmountMax(healType, value);
	}

	/** See {@link #getHealableResourceRegeneration(RechargeableResourceType)}. */
	public default void setHealableRegenerationAmount(RechargeableResourceType healType, int value) {
		this.getHealableResourcesHolder().setHealableRegenerationAmount(healType, value);
	}

	//

	/** Short-hand to getters and setters. */
	public default void alterCurableResourceAmount(RechargeableResourceType healType, int delta) {
		getHealableResourcesHolder().alterHealableResourceAmount(healType, delta);
	}

	// end delegators

	//

	// core algorithms

	//

	@Override
	public default void act(GModality modality, int timeUnits) { getHealingStrategyHelper().act(modality, timeUnits); }

	///

	public abstract ResourceAmountRecharged newHealInstance(RechargeableResourceType healType, int healAmount);

	/**
	 * See
	 * {@link #receiveHealing(GModality, ObjectWithID, ResourceAmountRecharged)},
	 * passing this held instances.
	 */
	public default void healMyself(ResourceAmountRecharged healingInstance) {
		receiveHealing(getGameModality(), this, healingInstance);
	}

	/**
	 * Make this object receiving a non-negative amount of healing, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public default <SourceHealing extends ObjectWithID> void receiveHealing(GModality gm, SourceHealing source,
			ResourceAmountRecharged healingInstance) {
		int healingAmount;
		healingAmount = healingInstance.getRechargedAmount();
		if (healingAmount > 0) {
			this.alterCurableResourceAmount(healingInstance.getRechargedResource(), healingAmount);
			fireHealingReceived(gm, source, healingInstance);
		}
	}

	/**
	 * Similar to {@link #fireDamageReceived( GModality, int, int)}, but about
	 * healing.
	 */
	public default <SourceHealing extends ObjectWithID> void fireHealingReceived(GModality gm, SourceHealing source,
			ResourceAmountRecharged healInstance) {
		GEventInterfaceRPG geiRpg;
		geiRpg = (GEventInterfaceRPG) this.getGameModality().getGameObjectsManager().getGEventInterface();
		geiRpg.fireResourceRechargeReceivedEvent((GModalityET) gm, source, (CreatureSimple) this, healInstance);
	}
}