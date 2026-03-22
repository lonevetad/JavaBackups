package games.old.rechargeable.resources;

import java.io.Serializable;
import java.util.Set;

import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.examples.ExampleHealingType;

/**
 * Defines a way to hold some types of {@link HealableResource}s.<br>
 * See {@link ExampleHealingType}.
 */
public interface IHealableResourcesHolder extends Serializable {
	public Set<RechargeableResourceType> getHealableResourceTypes();

	public Set<HealableResource> getHealableResources();

	//

	/**
	 * See {@link #getHealableResourceAmount(RechargeableResourceType)}, but the delta
	 * could be negative. It's <b>added</b> to the current value.<br>
	 * This should be used in generic, intermediate implementations and should
	 * delegate the optional "event firing" to the end methods, like "healing
	 * gaining" or "resource loss".
	 */
	public void alterHealableResourceAmount(RechargeableResourceType healType, int delta);

	public HealableResource getHealableResourceFor(RechargeableResourceType healType);

	//

	public default int getHealableResourceAmount(RechargeableResourceType healType) {
		return this.getHealableResourceFor(healType).getAmount();
	}

	public default int getHealableResourceAmountMax(RechargeableResourceType healType) {
		return this.getHealableResourceFor(healType).getAmountMax();
	}

	public default int getHealableResourceRegeneration(RechargeableResourceType healType) {
		return this.getHealableResourceFor(healType).getRegenerationAmount();
	}

	/** See {@link #getHealableResourceAmount(RechargeableResourceType)}. */
	public default void setHealableResourceAmount(RechargeableResourceType healType, int value) {
		this.getHealableResourceFor(healType).setAmount(value);
	}

	/** See {@link #getHealableResourceAmountMax(RechargeableResourceType)}. */
	public default void setHealableResourceAmountMax(RechargeableResourceType healType, int value) {
		this.getHealableResourceFor(healType).setAmountMax(value);
	}

	/** See {@link #getHealableResourceRegeneration(RechargeableResourceType)}. */
	public default void setHealableRegenerationAmount(RechargeableResourceType healType, int value) {
		this.getHealableResourceFor(healType).setRegenerationAmount(value);
	}

	//

	public default void addHealableResourceType(RechargeableResourceType healType) {
		addHealableResource(new HealableResourceImpl(healType));
	}

	public default void addHealableResource(HealableResource cr) { getHealableResources().add(cr); }

	public default void removeHealableResourceType(RechargeableResourceType healType) {
		getHealableResourceTypes().remove(healType);
	}

	public default void removeHealableResource(HealableResource cr) { getHealableResources().remove(cr); }

	public default boolean containsHealableResourceType(RechargeableResourceType healType) {
		return getHealableResourceTypes().contains(healType);
	}

	public default boolean containsHealableResource(HealableResource cr) {
		return getHealableResources().contains(cr);
	}
}