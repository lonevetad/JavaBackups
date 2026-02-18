package games.generic.controlModel.rechargeable.resources;

import java.io.Serializable;

import games.generic.controlModel.events.event.EventResourceRecharge;
import games.generic.controlModel.holders.ResourceRechargeableHolder;

/**
 * Identifies an integer amount of {@link RechargeableResourceType} recharged by
 * the execution of {@link ResourceRechargeableStrategy}.
 * <p>
 * Created during the "recharging" of a {@link ResourceRechargeableHolder}, this
 * class links that object to a specific type of
 * {@link RechargeableResourceType} and it's used to define the event
 * {@link EventResourceRecharge}.
 */
public class ResourceAmountRecharged implements Serializable {
	private static final long serialVersionUID = 894363018L;

	public ResourceAmountRecharged(RechargeableResourceType healType, int healAmount) {
		super();
		this.setRechargedResource(resource);
		this.setRechargedAmount(healAmount);
	}

	protected int amount;
	protected RechargeableResourceType resource;

	public int getRechargedAmount() { return this.amount; }

	public RechargeableResourceType getRechargedResource() { return this.resource; }

	public void setRechargedAmount(int rechargedAmount) { this.amount = rechargedAmount; }

	public void setRechargedResource(RechargeableResourceType rechargedResource) { this.resource = rechargedResource; }

	@Override
	public String toString() {
		return "ResourceAmountRecharged [rechargedAmount=" + getRechargedAmount() + ", rechargedResource="
				+ getRechargedResource() + "]";
	}
}