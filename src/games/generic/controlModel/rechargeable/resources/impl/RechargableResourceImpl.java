package games.generic.controlModel.rechargeable.resources.impl;

import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.rechargeable.resources.RechargableResource;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;

public class RechargableResourceImpl implements RechargableResource {
	private static final long serialVersionUID = -635242000005L;

	public RechargableResourceImpl(ResourceRechargeableHolder resourceHolder, RechargeableResourceType resourceType) {
		this.resourceHolder = resourceHolder;
		this.resourceType = resourceType;
		this.maxAmount = Math.min(1, resourceType.getUpperBound());
		this.amount = this.maxAmount;
		this.rechargeAmount = 0;
		this.delayBeforeRecharge = 0;
		this.canBeRecharged = true;
		this.timeDelayElapsed = 0;
	}

	protected boolean canBeRecharged;
	protected int amount, maxAmount, rechargeAmount, delayBeforeRecharge, timeDelayElapsed;
	protected final RechargeableResourceType resourceType;
	/** The one who owns this resource. */
	protected final ResourceRechargeableHolder resourceHolder;

	/**
	 * @return the resourceType
	 */
	@Override
	public RechargeableResourceType getResourceType() {
		return resourceType;
	}

	/**
	 * @return the resourceHolder
	 */
	@Override
	public ResourceRechargeableHolder getResourceHolder() {
		return resourceHolder;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public int getMaxAmount() {
		return maxAmount;
	}

	@Override
	public int getRechargeAmount() {
		return rechargeAmount;
	}

	@Override
	public int getDelayBeforeRecharge() {
		return this.delayBeforeRecharge;
	}

	@Override
	public boolean canBeRecharged() {
		this.checkCanBeRechargedDelay();
		return this.canBeRecharged || this.getDelayBeforeRecharge() <= 0;
	}

	//

	@Override
	public void setAmount(int amount) {
		boolean changed;
		if (amount > this.maxAmount) {
			amount = this.maxAmount;
		}
		changed = this.amount != amount;
		this.amount = amount;
		if (changed) {
			this.stopRechargeStartDelaying();
		}
	}

	@Override
	public void setAmountMax(int amountMax) {
		int t;
		if (amountMax < (t = this.resourceType.getLowerBound())) {
			amountMax = t;
		}
		if (amountMax > (t = this.resourceType.getUpperBound())) {
			amountMax = t;
		}
		this.maxAmount = amountMax;
		if (amountMax < this.amount) {
			this.amount = amountMax;
		}
	}

	@Override
	public void setRechargeAmount(int rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	@Override
	public void setDelayBeforeRecharge(int delayBeforeRecharge) {
		if (delayBeforeRecharge >= 0) {
			this.delayBeforeRecharge = delayBeforeRecharge;
		}
	}

//
	@Override
	public void advanceElapseTime(int timeUnits) {
		this.checkCanBeRechargedDelay();
		if (timeUnits > 0 && (!canBeRecharged)) {
			this.timeDelayElapsed += timeUnits;
			this.checkCanBeRechargedDelay();
		}
	}

	protected void checkCanBeRechargedDelay() {
		if (this.timeDelayElapsed > this.delayBeforeRecharge) {
			this.canBeRecharged = true;
			this.timeDelayElapsed = 0;
		}
	}

	@Override
	public void stopRechargeStartDelaying() {
		this.timeDelayElapsed = 0;
		this.canBeRecharged = false;
	}
}