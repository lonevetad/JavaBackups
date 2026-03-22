package games.old;

import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModality;
import games.generic.controlModel.holders.GModalityHolder;
import games.generic.controlModel.objects.TimedObject;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import tools.ObjectWithID;

public interface HealingObject_OLD extends TimedObject, GModalityHolder { //
	public static final int TICKS_PER_TIME_UNIT = 4, LOG_TICKS_PER_TIME_UNIT = 2;

	/** Returns all healing types modeled by this class. */
	public default Set<RechargeableResourceType> getAllHealings() {
		return getCurableResourcesHolders().icrAsSet;
	}

	public default void addHealingType(RechargeableResourceType healType) {
		getCurableResourcesHolders().addHealingType(healType);
	}

//	public void removeHealingType(HealingType healType);

	//

	/** Something that could be healed (gained) and lost, like life and mana. */
	public default int getCurableResourceAmount(RechargeableResourceType healType) {
		return getCurableResourcesHolders().getCurableResourceAmount(healType);
	}

	public int getCurableResourceMax(RechargeableResourceType healType);

	/**
	 * Differently from {@link #getCurableResourceAmount(RechargeableResourceType)}, this method
	 * (and the setter) relies on the implementor, not the helper class
	 * {@link CurableResourcesHolders}, so no default implementation is provided.
	 * <p>
	 * To be meant "per time unit", like "per second".<br>
	 * For example, those could be life regeneration and mana regeneration.<br>
	 */
	// delegated to the implementor
	public int getHealingRegenerationAmount(RechargeableResourceType healType);

	/** Helper for this class, just return a field of this class. */
	public CurableResourcesHolders getCurableResourcesHolders();

	//

	/** See {@link #getCurableResourceAmount(RechargeableResourceType)}. */
	public default void setCurableResourceAmount(RechargeableResourceType healType, int value) {
		getCurableResourcesHolders().setCurableResourceAmount(healType, value);
	}

	/** See {@link #getHealingRegenerationAmount(RechargeableResourceType)}. */
	public void setHealingRegenerationAmount(RechargeableResourceType healType, int value);

	/** See {@link #getCurableResourcesHolders()}. */
	public void setCurableResourcesHolders(CurableResourcesHolders curableResourcesHolders);

	//

	/** Short-hand to getters and setters. */
	public default void increaseCurableResourceAmount(RechargeableResourceType healType, int delta) {
		getCurableResourcesHolders().increaseCurableResourceAmount(healType, delta);
	}

	//

	/**
	 * NON mandatory aspect of this class.<br>
	 * If the "time unit" is subdivided into smaller units which the whole system is
	 * based on and the granularity is too little, then the rigeneration process
	 * could be imperfect (also due to the integer amounts, that makes hard to heal
	 * precisely and efficiently). Then macro-subdivisions of "time-units" and
	 * scaling of those smaller units can both be achieved through tickets.
	 */
	public int getTicksHealing();

	/**
	 * Returns {@link #TICKS_PER_TIME_UNIT}, see {@link #getTicksHealing()}.
	 */
	public default int getTicksPerTimeUnit() {
		return TICKS_PER_TIME_UNIT;
	}

	/**
	 * Each "time unit" could be composed by smaller units, as described in
	 * {@link #getTicksHealing()}. For instance, a second is composed by
	 * <code>1000</code> milliseconds and a "turn" is composed by just a single
	 * moment. This method returns that amount.
	 */
	public int getTimeSubunitsEachTimeUnits();

	/**
	 * Just calls
	 * <code>{@link #getTimeSubunitsEachTimeUnits()}/{@link #getTicksPerTimeUnit()}</code>.
	 */
	public default int getTimeSubunitsEachTicks() {
		return getTimeSubunitsEachTimeUnits() / getTicksPerTimeUnit();
	}

	/**
	 * Protected method, do NOT override.
	 * <p>
	 * Get the accumulated time between each regeneration instance.
	 */
	public int getAccumulatedTimeRegen();

	//

	/** See {@link #getAccumulatedTimeRegen()}. */
	public void setAccumulatedTimeRegen(int newAccumulated);

	/** See {@link #getTicksHealing()}. */
	public void setTicksHealing(int ticks);

	//

	// core algorithms

	//

	public default void performAllHealings(GModality gm) {
		boolean isLastTick;
		int temp, ticksPerTimeUnits;
		CurableResourcesHolders crh;
		temp = getTicksHealing() + 1;
		ticksPerTimeUnits = getTicksPerTimeUnit();
		isLastTick = (temp >= TICKS_PER_TIME_UNIT);
		if (isLastTick)
			setTicksHealing(0);
		else
			setTicksHealing(temp);
		crh = getCurableResourcesHolders();
		// now apply healing to everybody
		this.getAllHealings().forEach(ht -> {
			int amountHealed, indexHealingType, maxAmount, tempTotalResource;
			amountHealed = getHealingRegenerationAmount(ht); // how much does I heal myself?
			indexHealingType = crh.indexCurableResources.get(ht);
			if (isLastTick) {
				// calculate the last piece of the cake not currently healed
				amountHealed -= (ticksPerTimeUnits - 1) * //
				(amountHealed / ticksPerTimeUnits);
			} else {
				amountHealed /= ticksPerTimeUnits;
			}
			if (amountHealed > 0) {
				maxAmount = getCurableResourceMax(ht);
				tempTotalResource = crh.curableResources[indexHealingType] + amountHealed;
				if (tempTotalResource > maxAmount) {
					// check the maximum cap
					amountHealed -= (tempTotalResource - maxAmount);
					tempTotalResource = maxAmount;
				}
				if (amountHealed > 0) { // there is still an healing instance after the cap-check?
					crh.curableResources[indexHealingType] = tempTotalResource;
//			crh.getCurableResourceAmount(ht);
					fireHealingReceived(gm, this, newHealInstance(ht, amountHealed));
				}
			}
		});
	}

	@Override
	public default void act(GModality modality, int timeUnits) {
		int tfinal, subunitsEachTicks;
		if (timeUnits <= 0)
			return;
		tfinal = getAccumulatedTimeRegen() + timeUnits;
		subunitsEachTicks = getTimeSubunitsEachTicks();
		if (tfinal > subunitsEachTicks) {
			do {
				tfinal -= subunitsEachTicks;
				setAccumulatedTimeRegen(tfinal);
				performAllHealings(modality);
			} while (tfinal > subunitsEachTicks);
		} else {
			setAccumulatedTimeRegen(tfinal);
		}
	}

	//

	// healing stuffs

	//

	public ResourceAmountRecharged newHealInstance(RechargeableResourceType healType, int healAmount);

	/**
	 * Make this object receiving a non-negative amount of healing, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public default <SourceHealing extends ObjectWithID> void receiveLifeHealing(GModality gm, SourceHealing source,
			ResourceAmountRecharged healingInstance) {
		int healingAmount;
		healingAmount = healingInstance.getRechargedAmount();
		if (healingAmount > 0) {
			this.increaseCurableResourceAmount(healingInstance.getRechargedResource(), healingAmount);
			fireHealingReceived(gm, source, healingInstance);
		}
	}

	/**
	 * Similar to {@link #fireDamageReceived( GModality, int, int)}, but about
	 * healing.
	 */
	public default <SourceHealing extends ObjectWithID> void fireHealingReceived(GModality gm, SourceHealing source,
			ResourceAmountRecharged healInstance) {
		GEventInterfaceRPG geiRpg;
		geiRpg = (GEventInterfaceRPG) this.getGameModality().getGameObjectsManager().getGEventInterface();
		geiRpg.fireResourceRechargeReceivedEvent((GModalityET) gm, source, (CreatureSimple) this, healInstance);
	}

	//

	// TODO CLASSES

//	public static interface

	public static class CurableResourcesHolders {
		protected int size;
		protected int[] curableResources; // ArrayList-like
		protected MapTreeAVL<RechargeableResourceType, Integer> indexCurableResources;
		protected Set<RechargeableResourceType> icrAsSet;

		public CurableResourcesHolders() {
			this.curableResources = null;
			this.indexCurableResources = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
					MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, RechargeableResourceType.COMPARATOR_RECHARGEABLE_RESOURCE_TYPE);
			this.icrAsSet = this.indexCurableResources.keySet();
		}

		public int getCurableResourceAmount(RechargeableResourceType healType) {
			return curableResources[indexCurableResources.get(healType)];
		}

		/** See {@link #getCurableResourceAmount(RechargeableResourceType)}. */
		public void setCurableResourceAmount(RechargeableResourceType healType, int value) {
			curableResources[indexCurableResources.get(healType)] = value;
		}

		/**
		 * See {@link #getCurableResourceAmount(RechargeableResourceType)}, but the delta could be
		 * negative.
		 */
		public void increaseCurableResourceAmount(RechargeableResourceType healType, int delta) {
			curableResources[indexCurableResources.get(healType)] += delta;
		}

		public void addHealingType(RechargeableResourceType healType) {
			if (this.curableResources == null) {
				this.indexCurableResources.put(healType, 0);
				this.curableResources = new int[2];
				this.size = 1;
				return;
			}
			/*
			 * fast way to check if the healType wasn't present: save the size, add and
			 * check the new size: if changed, then expand the array
			 */
			this.indexCurableResources.put(healType, this.size);
			if (this.size != this.indexCurableResources.size()) {
				if (this.size == this.curableResources.length) {
					int[] newArray;
					newArray = new int[this.size + (this.size >> 1)]; // grow factor of 1.5
					System.arraycopy(this.curableResources, 0, newArray, 0, this.size);
					this.curableResources = newArray;
				}
				this.size++;
			}
		}
//		public void removeHealingType(HealingType healType) {		}
	}

	//

	//

//	public static void main(String[] args) {}
//	static class OH_Test implements ObjectHealing {
}