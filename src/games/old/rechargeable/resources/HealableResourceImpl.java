package games.old.rechargeable.resources;

import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;

/**
 * An implementation of {@link HealableResource}, check it for an example.
 */
@Deprecated
public class HealableResourceImpl extends HealableResource {
	private static final long serialVersionUID = 5850480L;

	public HealableResourceImpl(RechargeableResourceType ht) { super(ht); }

	protected int amount, amountMax, regenerationAmount;

	@Override
	public int getAmount() { return amount; }

	@Override
	public int getAmountMax() { return amountMax; }

	@Override
	public int getRegenerationAmount() { return regenerationAmount; }

	//

	//

	@Override
	public void setAmount(int resourceAmount) {
		if (resourceAmount < this.resourceType.getLowerBound()) { resourceAmount = this.resourceType.getLowerBound(); }
		if (resourceAmount > this.getAmountMax()) { resourceAmount = this.getAmountMax(); }
		this.amount = resourceAmount;
	}

	@Override
	public void setAmountMax(int resourceAmountMax) {
		if (resourceAmountMax < this.resourceType.getLowerBound()) {
			resourceAmountMax = this.resourceType.getLowerBound();
		}
		if (resourceAmountMax > this.resourceType.getUpperBound()) {
			resourceAmountMax = this.resourceType.getUpperBound();
		}
		this.amountMax = resourceAmountMax;
		// set bounds
		if (resourceAmountMax > 0 && resourceAmountMax < amount) { this.amount = resourceAmountMax; }
	}

	@Override
	public void setRegenerationAmount(int regenerationAmount) { this.regenerationAmount = regenerationAmount; }
}