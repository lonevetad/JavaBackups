package games.generic.controlModel.holders;

import java.util.Map;
import java.util.Set;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.GEventInterface;
import games.generic.controlModel.events.event.EventResourceRecharge;
import games.generic.controlModel.rechargeable.resources.RechargableResource;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import games.generic.controlModel.rechargeable.resources.ResourceRechargeableStrategy;
import games.generic.controlModel.rechargeable.resources.impl.RechargableResourceImpl;
import games.generic.controlModel.subimpl.GModalityET;
import tools.ObjectWithID;

/**
 * Defines an object having a set of {@link RechargableResource} that can be
 * recharged over time
 *
 * @author ottin
 *
 */
public interface ResourceRechargeableHolder extends ObjectWithID, GModalityHolder {
	public Set<RechargeableResourceType> getRechargableResourcesType();

	public Map<RechargeableResourceType, RechargableResource> getRechargableResources();

	public ResourceRechargeableStrategy getResourceRechargeableStrategy();

	public void setResourceRechargeableStrategy(ResourceRechargeableStrategy resourceRechargeableStrategy);

	/**
	 * An initialization function, for instance the {@link Set}s returned by
	 * {@link #getRechargableResourcesType()} and
	 * {@link #getRechargableResources()}.
	 */
	public default void initRechargeableResourceHolderStuffs() {
		this.initSetRechargeableResourcesType();
		this.initSetRechargeableResources();
	}

	/**
	 * Initialize the {@link Set} returned by
	 * {@link #getRechargableResourcesType()}.
	 */
	public void initSetRechargeableResourcesType();

	/**
	 * Initialize the {@link Set} returned by {@link #getRechargableResources()}.
	 */
	public default void initSetRechargeableResources() {
		this.getRechargableResourcesType().forEach(rrt -> {
			this.addRechargableResource(new RechargableResourceImpl(this, rrt));
		});
	}

	//

	public default int getAmount(RechargeableResourceType resType) {
		RechargableResource res;
		res = this.getRechargableResources().get(resType);
		if (res == null) { throw new IllegalArgumentException("Resource " + resType + " not found."); }
		return res.getAmount();
	}

	public default int getMaxAmount(RechargeableResourceType resType) {
		RechargableResource res;
		res = this.getRechargableResources().get(resType);
		if (res == null) { throw new IllegalArgumentException("Resource " + resType + " not found."); }
		return res.getMaxAmount();
	}

	public default int getRechargeAmount(RechargeableResourceType resType) {
		RechargableResource res;
		res = this.getRechargableResources().get(resType);
		if (res == null) { throw new IllegalArgumentException("Resource " + resType + " not found."); }
		return res.getRechargeAmount();
	}

	//

	public default void setAmount(RechargeableResourceType resType, int amount) {
		RechargableResource res;
		res = this.getRechargableResources().get(resType);
		if (res == null) { throw new IllegalArgumentException("Resource " + resType + " not found."); }
		res.setAmount(amount);
	}

	public default void setMaxAmount(RechargeableResourceType resType, int amount) {
		RechargableResource res;
		res = this.getRechargableResources().get(resType);
		if (res == null) { throw new IllegalArgumentException("Resource " + resType + " not found."); }
		res.setAmountMax(amount);
	}

	public default void setRechargeAmount(RechargeableResourceType resType, int amount) {
		RechargableResource res;
		res = this.getRechargableResources().get(resType);
		if (res == null) { throw new IllegalArgumentException("Resource " + resType + " not found."); }
		res.setRechargeAmount(amount);
	}

	public default boolean hasRechargableResource(RechargeableResourceType resourceType) {
		return this.getRechargableResources().containsKey(resourceType);
	}

	/**
	 * Add the provided {@link RechargableResource} and return {@code true} if the
	 * addition is successful: returns {@code false} if there already exists a
	 * resource with that type (providede by
	 * {@link RechargableResource#getResourceType()}).
	 *
	 * @param resource
	 * @return
	 */
	public default boolean addRechargableResource(RechargableResource resource) {
		Map<RechargeableResourceType, RechargableResource> allResources;
		allResources = this.getRechargableResources();
		if (allResources.containsKey(resource.getResourceType())) { return false; }
		allResources.put(resource.getResourceType(), resource);
		return true;
	}

	/**
	 * See {@link #removeRechargableResource(RechargeableResourceType)}
	 *
	 * @param resource
	 * @return
	 */
	public default boolean removeRechargableResource(RechargableResource resource) {
		return this.removeRechargableResource(resource.getResourceType());
	}

	/**
	 * Similar to {@link #addRechargableResource(RechargableResource)}, but for the
	 * removal.
	 *
	 * @param resource
	 * @return
	 */
	public default boolean removeRechargableResource(RechargeableResourceType resourceType) {
		Map<RechargeableResourceType, RechargableResource> allResources;
		allResources = this.getRechargableResources();
		if (allResources.containsKey(resourceType)) {
			allResources.remove(resourceType);
			return true;
		}
		return false;
	}

	// TODO EVENTS

	/**
	 * Fires the recharge event. Should invoke
	 *
	 * @param <SourceRecharge>           The type of the object is performing the
	 *                                   recharge operation.
	 * @param recharge                   The recharge operation, specifying the
	 *                                   recharged amount of resource and its type.
	 * @param whoIsPerformingTheRecharge The object who is performing the recharge
	 *                                   operation.
	 * @return the generated recharge-event (see {@link EventResourceRecharge}).
	 */
	public default <SourceRecharge extends ObjectWithID> EventResourceRecharge<SourceRecharge> fireRechargeEvent(
			ResourceAmountRecharged recharge, SourceRecharge whoIsPerformingTheRecharge) {
		GEventInterface eventInterface;
		GModality gm;
		gm = this.getGameModality();
		if (!(gm instanceof GModalityET)) { return null; }

		eventInterface = gm.getGameObjectsManager().getGEventInterface();
		eventInterface.fireResourceRechargeGivenEvent((GModalityET) gm, whoIsPerformingTheRecharge, this, recharge);

		return eventInterface.fireResourceRechargeReceivedEvent((GModalityET) gm, whoIsPerformingTheRecharge, this,
				recharge);
	}

	/**
	 * Perform the recharge specified by the first parameter
	 * ({@link ResourceAmountRecharged}). The implementor has to deal with negative
	 * amounts.<br>
	 * The second parameter defines <i>who</i> is performing that recharge operation
	 * in order to fire the related event (see
	 * {@link #fireRechargeEvent(ResourceAmountRecharged, ObjectWithID)}).
	 *
	 * @param <Source>                   The type of the object is performing the
	 *                                   recharge operation.
	 * @param recharge                   The recharge operation, specifying the
	 *                                   recharged amount of resource and its type.
	 * @param whoIsPerformingTheRecharge The object who is performing the recharge
	 *                                   operation.
	 */
	public <Source extends ObjectWithID> void performRechargeOf(ResourceAmountRecharged recharge,
			Source whoIsPerformingTheRecharge);

}