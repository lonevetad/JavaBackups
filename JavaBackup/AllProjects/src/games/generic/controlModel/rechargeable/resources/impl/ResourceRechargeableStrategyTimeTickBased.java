package games.generic.controlModel.rechargeable.resources.impl;

import java.util.Map;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.holders.ResourceRechargeableHolder;
import games.generic.controlModel.objects.TimedObject;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.rechargeable.resources.RechargableResource;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import games.generic.controlModel.rechargeable.resources.ResourceRechargeableStrategy;
import tools.ObjectWithID;
import tools.impl.OWIDLongImpl;

/**
 *
 * @author ottin
 *
 * @param <Source>: the object is providing the healing: the
 *                  {@link PlayerGeneric} itself, a {@link AbilityGeneric}, etc
 */
public class ResourceRechargeableStrategyTimeTickBased<Source extends ObjectWithID> extends OWIDLongImpl
		implements ResourceRechargeableStrategy, TimedObject {
	private static final long serialVersionUID = 45364398705219L;
	private static long idProg = 0;
	protected static final int TICKS_EACH_TIME_UNIT = 4; // , TIME_SUBUNITS_EACH_TICK;

	public ResourceRechargeableStrategyTimeTickBased(Source whoIsPerformingTheRecharge,
			ResourceRechargeableHolder resourceRechargeableHolder) {
		super();
		this.setID(idProg++);
		this.whoIsPerformingTheRecharge = whoIsPerformingTheRecharge;
		this.resourceRechargeableHolder = resourceRechargeableHolder;
		this.theResourceRecharger = (resType, res) -> this.rechargeResource(res);
		this.rechargedProgressTracker = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				RechargeableResourceType.COMPARATOR_RECHARGEABLE_RESOURCE_TYPE);
	}

	/** Taken from the {@link #act(GModality, int)} */
	protected int subUnitTimeElapsed;
	protected GModality gameModality;
	protected final Source whoIsPerformingTheRecharge;
	protected final ResourceRechargeableHolder resourceRechargeableHolder;
	protected final BiConsumer<RechargeableResourceType, RechargableResource> theResourceRecharger;
	protected final Map<RechargeableResourceType, ResourceRechargeInProgress> rechargedProgressTracker;

	protected void reset() {
//		this.subUnitTimeTotal =
		this.subUnitTimeElapsed = 0;
	}

	protected void unsupportedOperationExc() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This method should not be called");
	}

	@Override
	public void onAddedToGame(GModality gm) { this.reset(); }

	@Override
	public void onRemovedFromGame(GModality gm) {}

	@Override
	public String getName() {
		unsupportedOperationExc();
		return null;
	}

	/**
	 * Returns the amount of "ticks" each {@link TimedObject}'s "time units"
	 *
	 * @return
	 */
	public int getTicksEachTimeUnit() { return TICKS_EACH_TIME_UNIT; }

	/**
	 * Returns an approximation of the amount of "time su
	 * {@link TimedObject#getTimeSubUnitsEachUnit()}
	 */
	public int getTimeSubUnitsEachTicks() { return this.getTimeSubUnitsEachUnit() / this.getTicksEachTimeUnit(); }

	@Override
	public GModality getGameModality() { return gameModality; }

	//

	@Override
	public void setGameModality(GModality gameModality) { this.gameModality = gameModality; }

	//

	@Override
	public void act(GModality modality, int timeUnits) {
		if (timeUnits > 0) {
			this.subUnitTimeElapsed = timeUnits;
			this.rechargeResources(this.resourceRechargeableHolder.getRechargableResources());
			this.subUnitTimeElapsed = 0;
		}
	}

	@Override
	public void rechargeResources(Map<RechargeableResourceType, RechargableResource> resources) {
		if (this.subUnitTimeElapsed > 0) { resources.forEach(this.theResourceRecharger); }
	}

	protected void rechargeResource(RechargableResource resource) {
		int subunitTimeEachUnit, amountToRecharge, fractionAmountToRecharge;
		final int rechargeTarget, ticksPerTimeUnit, timeSubunitPerTick;
		ResourceRechargeInProgress progress;
		progress = rechargedProgressTracker.get(resource.getResourceType());
		rechargeTarget = resource.getRechargeAmount();
		if (progress == null) {
			progress = new ResourceRechargeInProgress(resource);
			progress.targetRegenPerTimeUnit = rechargeTarget;
//			progress.reset(regenTarget);
		}

		resource.advanceElapseTime(this.subUnitTimeElapsed);
		if (!resource.canBeRecharged()) { return; }

		// TODO:refactor as ...
		/**
		 * <ol>
		 * <li>if the subUnitTimeElapsed makes the overflow of subunitTimeEachUnit, then
		 * "amountToHeal = the amount left to recharge in order to fulfill a complete
		 * cycle of recharge". In that case, reset the progress.</li>
		 * <li>do the cycle for "whole jumps of whole cycles"</li>
		 * <li>do the rest</li>
		 * </ol>
		 */
		amountToRecharge = 0;
		subunitTimeEachUnit = this.getTimeSubUnitsEachUnit();
		if (progress.targetRegenPerTimeUnit != rechargeTarget) {
			int rechargeAmountToNotWaste;
			rechargeAmountToNotWaste = (int) ((((long) progress.targetRegenPerTimeUnit)
					* ((long) progress.subunitsTimeAccumulated)) / subunitTimeEachUnit);
			if (rechargeAmountToNotWaste != 0) {
				amountToRecharge += rechargeAmountToNotWaste;
				progress.resetToTargetRegen(rechargeTarget);
			}
		}
		ticksPerTimeUnit = this.getTicksEachTimeUnit();
		timeSubunitPerTick = this.getTimeSubUnitsEachTicks();

		progress.subunitsTimeAccumulated += this.subUnitTimeElapsed;

		if (progress.subunitsTimeAccumulated < timeSubunitPerTick) { return; }

		while (progress.subunitsTimeAccumulated >= timeSubunitPerTick) {
			if (progress.subunitsTimeAccumulated >= subunitTimeEachUnit) {
				// optimize the whole time unit
				amountToRecharge += rechargeTarget;
				progress.subunitsTimeAccumulated -= subunitTimeEachUnit;
			} else {
				fractionAmountToRecharge = ((int) ((((long) rechargeTarget) * ((long) ++progress.ticksElapsed))
						/ ticksPerTimeUnit)) - progress.rechargeAmountAlreadyProvided;
				amountToRecharge += fractionAmountToRecharge;
				progress.subunitsTimeAccumulated -= timeSubunitPerTick;
				if (progress.ticksElapsed >= ticksPerTimeUnit) {
					// whole "time unit" has elapsed, reset the cycle
					progress.ticksElapsed = 0;
					progress.rechargeAmountAlreadyProvided = 0;
				}
			}
		}

		// start-OLD
//		else {
//			if ((progress.subUnitTimeTotalElapsed + this.subUnitTimeElapsed) >= subunitTimeEachUnit) {
//				amountToHeal = progress.targetRegenPerTimeUnit - //
//						(int) (((long) this.subUnitTimeElapsed * ((long) (regenTarget))) / subunitTimeEachUnit);
//
//			}
//		}

//		if (rechargeTarget == 0) { return; }
//		amountToRecharge += rechargeTarget;
//		while (progress.subunitsTimeAccumulated >= subunitTimeEachUnit) {
//
////			resource.performRechargeBy(amountToHeal, this.whoIsPerformingTheRecharge);
//			progress.subunitsTimeAccumulated -= subunitTimeEachUnit;
//			amountToRecharge += rechargeTarget;
//		}
//		if (progress.subunitsTimeAccumulated >= 0) {
//			amountToRecharge = (int) (((long) this.subUnitTimeElapsed * ((long) (rechargeTarget)))
//					/ subunitTimeEachUnit);
//
//		}
		// end-OLD

		if (amountToRecharge != 0) {
			this.resourceRechargeableHolder.performRechargeOf(
					new ResourceAmountRecharged(resource.getResourceType(), amountToRecharge),
					whoIsPerformingTheRecharge);
		}
	}

	//

	protected static class ResourceRechargeInProgress {
		protected int subunitsTimeAccumulated, targetRegenPerTimeUnit, rechargeAmountAlreadyProvided, ticksElapsed;
		protected RechargableResource resource;

		public ResourceRechargeInProgress(RechargableResource resource) {
			super();
			this.resource = resource;
			this.resetToTargetRegen(0);
		}

		void resetToTargetRegen(int targetRegen) {
			this.targetRegenPerTimeUnit = targetRegen;
			this.subunitsTimeAccumulated = 0;
			this.rechargeAmountAlreadyProvided = 0;
			this.ticksElapsed = 0;
		}

// TODO TO COMPLETE
	}
}
