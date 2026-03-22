package games.generic.controlModel.rechargeable.resources;

import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.objects.TimedObject;
import tools.ObjectNamedID;

/**
 * Defines a resource (of the type {@literal IRechargeableResourceType}) that
 * can be recharged <i>and</i> its current amount, its maximum amount and its
 * amount of regeneration.<br>
 * The implementation is free to store the current and maximum values (and every
 * other ones) or delegates the updates to some other class holding the altered
 * fields value.
 */
public interface RechargableResource extends ObjectNamedID {

	public RechargeableResourceType getResourceType();

	public ResourceRechargeableHolder getResourceHolder();

	@Override
	public default Long getID() {
		return getResourceType().getID();
	}

	@Override
	public default String getName() {
		return getResourceType().getName();
	}

	public abstract int getAmount();

	public abstract int getMaxAmount();

	/** Returns the amount of resources that can be recharged each time. */
	public abstract int getRechargeAmount();

	public default int getLowerBound() {
		return getResourceType().getLowerBound();
	}

	public default int getUpperBound() {
		return getResourceType().getUpperBound();
	}

	/**
	 * Returns a time amount, representing the delay it needs to be elapsed after
	 * the depletion of this resource before the recharging process may begin or be
	 * resumed.
	 *
	 * @return
	 */
	public default int getDelayBeforeRecharge() {
		return 0;
	}

	/**
	 * See {@link #getDelayBeforeRecharge()}, tells if this resource can be
	 * recharged. if {@link #getDelayBeforeRecharge()} is greater than zero and not
	 * enough time has been elapsed, then {@code false} must be returned.
	 * <p>
	 * NOTE: as stated in {@link #setRechargeDelayIndependence(boolean)}, may use
	 * some flag to force the no-delay recharge policy. For instance:
	 *
	 * <pre>
	 * <code>
	 * public boolean canBeRecharged() {
	 *     return this.canBeRechargedFlag || this.getDelayBeforeRecharge() <= 0;
	 * }
	 * </code>
	 * </pre>
	 */
	public default boolean canBeRecharged() {
		return true;
	}

	//

	public abstract void setAmount(int resourceAmount);

	@Override
	public default boolean setID(Long newID) {
		return this.getResourceType().setID(newID);
	}

	public abstract void setAmountMax(int resourceAmountMax);

	/** Sets the amount of resources that can be recharged each time. */
	public abstract void setRechargeAmount(int regenerationAmount);

	/**
	 * See {@link #getDelayBeforeRecharge()}
	 *
	 * @param delayBeforeRecharge
	 */
	public default void setDelayBeforeRecharge(int delayBeforeRecharge) {
	}

	/**
	 * See {@link #canBeRecharged()}.<br>
	 * May set a flag to force the no-delay recharge policy.
	 */
	public default void forceRechargeDelayIndependence() {
		this.setDelayBeforeRecharge(0);
	}

	//

	/**
	 * Alter the current amount ({@link #getAmount()} by the provided integer delta
	 * (could be a negative value).
	 *
	 * @param delta The amount of resource that will be recharged.
	 */
	public default void performRechargeBy(int delta) {
		int a, t;
		RechargeableResourceType rrt;
		rrt = this.getResourceType();
		a = this.getAmount() + delta;
		if (a < (t = rrt.getLowerBound())) {
			a = t;
		}
		if (a > (t = rrt.getUpperBound())) {
			a = t;
		}
		this.setAmount(a);
	}

	/**
	 * Similar to {@link TimedObject#act(games.generic.controlModel.GModality, int)}
	 * in almost every aspects, let some processes to advance over time by a delta
	 * of time units. For example, may deplete the delay imposed by
	 * {@link #getDelayBeforeRecharge()} before the recharging process starts again.
	 *
	 * @param timeUnits
	 */
	public void advanceElapseTime(int timeUnits);

	/**
	 * A possibly empty function that notify this rechargeable resource that it has
	 * to stop getting recharged and has to pause, waiting the delay to be depleted.
	 */
	public void stopRechargeStartDelaying();
}