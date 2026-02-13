package games.generic.controlModel.rechargeable.resources.impl;

import games.generic.controlModel.rechargeable.resources.RechargableResource;

/**
 * A delay aware {@link RechargableResource}.
 * <p>
 * Note: may be used as a delegated instance inside another
 * {@link RechargableResource} that simply manages the delay-related stuff,
 * while it invokes the container class for the other aspects
 *
 * @author marcoottina
 *
 */
public abstract class RechargableResourceWithDelay implements RechargableResource {
	private static final long serialVersionUID = 203032039863L;

	public RechargableResourceWithDelay() {
		this.delayBeforeRecharge = 0;
		this.canBeRecharged = true;
		this.timeDelayElapsed = 0;
	}

	protected boolean canBeRecharged;
	protected int delayBeforeRecharge, timeDelayElapsed;

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

	protected void checkCanBeRechargedDelay() {
		if (this.timeDelayElapsed > this.delayBeforeRecharge) {
			this.canBeRecharged = true;
			this.timeDelayElapsed = 0;
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

	@Override
	public void stopRechargeStartDelaying() {
		this.timeDelayElapsed = 0;
		this.canBeRecharged = false;
	}
}