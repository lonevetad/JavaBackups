package games.generic.controlModel.objects;

import games.generic.controlModel.GModality;
import tools.ObjectWithID;

public abstract class RechargeableTimeBased implements TimedObject {
	private static final long serialVersionUID = 1L;

	public static final int SCORE_GRANULARITY = 1024;

	protected int rechargeBonusMalusCache, accumulatedScore, maxScore, timeUnitsForFullRechargeCache,
			timeUnitsAccumulatedFromLastNonZeroContribution;
	protected ObjectWithID whatToRecharge;

	/**
	 * Returns a percentage of bonus (or malus, if negative) of the velocity
	 * recharge rate. <br>
	 * For example, this instance may have a cooldown of 3.5 seconds and a bonus of
	 * 20%, which turns the cooldown down to 2.8 seconds.
	 *
	 * @return
	 */
	public abstract int getBonusRechargeVelocityPercentage();

	public abstract void peformFullRecharge(GModality modality);

	/**
	 * Returns wether this instance is fully charged or not.
	 *
	 * @return
	 */
	public abstract boolean isCharged();

	public int getPercentageOfRecharge() {
		return (int) ( //
		(((long) this.accumulatedScore) * 100) //
				/ this.maxScore);
	}

	/**
	 * �
	 *
	 * Set the amount of "time units" that should elapse in order to recharge this
	 * instances (that means, fire {@link #peformFullRecharge(GModality)}). <br>
	 * Also known as <i>cooldown</i>.
	 *
	 * @param timeUnitsCooldown
	 */
	public void setRechargeTime(int timeUnitsCooldown) {
		this.timeUnitsForFullRechargeCache = timeUnitsCooldown;
		this.maxScore = timeUnitsCooldown << 10;
	}

	public void reset() { this.accumulatedScore = this.timeUnitsAccumulatedFromLastNonZeroContribution = 0; }

	@Override
	public void act(GModality modality, int timeUnits) {
		int bonusMalus;
		long contr;

		if (this.isCharged()) { return; }
		bonusMalus = this.getBonusRechargeVelocityPercentage();

		// TODO check for changes

		contr = ((long) (timeUnits + this.timeUnitsAccumulatedFromLastNonZeroContribution) //
				<< 10); // "<< 10" == " * SCORE_GRANULARITY"
		if (bonusMalus >= 0) {
			contr = (contr * (100 + bonusMalus)) / 100;
		} else {
			contr = (contr * 100) / (100 + (-bonusMalus));
		}
		if (contr > 0) {
			this.accumulatedScore += (int) contr;
			this.timeUnitsAccumulatedFromLastNonZeroContribution = 0; // it's consumed, then it has to be emptied
		} else {
			this.timeUnitsAccumulatedFromLastNonZeroContribution += timeUnits;
		}

		if (this.accumulatedScore >= this.maxScore) {
			// TODO: superat lo score massimo, resettare e invocare.... peformFullRecharge
			this.peformFullRecharge(modality);
			this.accumulatedScore = 0;
		}
	}

	@Override
	public void onAddedToGame(GModality gm) { // TODO Auto-generated method stub
	}

	@Override
	public void onRemovedFromGame(GModality gm) { // TODO Auto-generated method stub
	}

	@Override
	public Long getID() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() { // TODO Auto-generated method stub
		return null;
	}

}
