package games.old.rechargeable.resources;

import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import tools.ObjectNamedID;

/**
 * A concrete definition of {@link RechargeableResourceType} (see
 * {@link #getResourceType()} to check what resource actually wraps) holding the
 * current amount, the maximum amount (the minimum is considered <code>0</code>
 * by default) and the amount of regeneration.<br>
 * The <i>regeneration</i> is meant to be <i>provided after passing a specific
 * amount of time, depending on the game's implementation</i>.
 */
@Deprecated
public abstract class HealableResource implements ObjectNamedID {
	private static final long serialVersionUID = -475214589669875230L;

	public HealableResource(RechargeableResourceType resourceType) {
		super();
		this.resourceType = resourceType;
	}

	protected final RechargeableResourceType resourceType;

	/**
	 * The type of resource this instance wraps.
	 * <p>
	 * See {@link RechargeableResourceType}.
	 */
	public RechargeableResourceType getResourceType() { return resourceType; }

	@Override
	public Integer getID() { return resourceType.getID(); }

	@Override
	public String getName() { return resourceType.getName(); }

	public abstract int getAmount();

	public abstract int getAmountMax();

	public abstract int getRegenerationAmount();

	//

	//

	public abstract void setAmount(int resourceAmount);

	public abstract void setAmountMax(int resourceAmountMax);

	public abstract void setRegenerationAmount(int regenerationAmount);

	public void alterResourceAmount(int delta) { setAmount(this.getAmount() + delta); }
}