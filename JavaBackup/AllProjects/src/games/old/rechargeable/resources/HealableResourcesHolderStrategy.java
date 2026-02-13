package games.old.rechargeable.resources;

import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import dataStructures.SetMapped;
import games.generic.controlModel.GModality;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;

/**
 * Both a holder of a set of {@link HealableResource} (i.e., an instance of
 * {@link IHealableResourcesHolder}) and a strategy of healing those resources
 * (i.e. a {@link IHealingResourcesOverTimeStrategy}).<br>
 * Used inside {@link HealingObject}.
 */
public class HealableResourcesHolderStrategy extends IHealingResourcesOverTimeStrategy
		implements IHealableResourcesHolder {
	private static final long serialVersionUID = 636898404756654L;

	public HealableResourcesHolderStrategy(HealingObject objToHeal) {
		super(objToHeal);
		this.backmapHealableResources = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, RechargeableResourceType.COMPARATOR_RECHARGEABLE_RESOURCE_TYPE);
		this.resourceTypes = this.backmapHealableResources.keySet();
		this.healableResources = new SetMapped<Entry<RechargeableResourceType, BridgeHealResourceRecharge>, HealableResource>( //
				backmapHealableResources.entrySet(), //
				e -> e.getValue().resource);
	}

	protected final MapTreeAVL<RechargeableResourceType, BridgeHealResourceRecharge> backmapHealableResources;
	protected final Set<RechargeableResourceType> resourceTypes;
	protected final Set<HealableResource> healableResources;
	protected HealerForEacher healer;

	@Override
	public Set<RechargeableResourceType> getHealableResourceTypes() { return resourceTypes; }

	@Override
	public Set<HealableResource> getHealableResources() { return healableResources; }

	@Override
	public HealableResource getHealableResourceFor(RechargeableResourceType healType) {
		return this.backmapHealableResources.get(healType).resource;
	}

	@Override
	public void alterHealableResourceAmount(RechargeableResourceType healType, int delta) {
		HealableResource hr = getHealableResourceFor(healType);
		hr.setAmount(hr.getAmount() + delta);
	}

	//

	@Override
	public void addHealableResource(HealableResource cr) {
		this.backmapHealableResources.put(cr.getResourceType(), new BridgeHealResourceRecharge(cr));
	}

	@Override
	public void removeHealableResource(HealableResource cr) { removeHealableResourceType(cr.getResourceType()); }

	@Override
	public boolean containsHealableResourceType(RechargeableResourceType healType) {
		return this.backmapHealableResources.containsKey(healType);
	}

	@Override
	public boolean containsHealableResource(HealableResource cr) {
		return this.backmapHealableResources.containsKey(cr.getResourceType());
	}

	//

	@Override
	public void healOverTime(GModality modality, int timeUnits) {
		if (this.backmapHealableResources.isEmpty())
			return;
		if (this.healer == null) { this.healer = new HealerForEacher(); }
		this.healer.timeUnitsElapsed = timeUnits;
		this.backmapHealableResources.forEach(this.healer);
	}

	//

	//

	protected static class BridgeHealResourceRecharge {
		// recharge stuff
		protected int regenCache = 0, amountRecovered = 0, millis = 0;
		// healing resource
		protected final HealableResource resource;

		public BridgeHealResourceRecharge(HealableResource resource) {
			super();
			this.resource = resource;
			this.regenCache = this.amountRecovered = this.millis = 0;
		}
	}

	protected class HealerForEacher implements Consumer<Entry<RechargeableResourceType, BridgeHealResourceRecharge>> {
//		public HealerForEacher(HealingObject objToHeal) {
//			super();
//			this.objToHeal = Objects.requireNonNull(objToHeal);
//		}
//		protected final HealingObject objToHeal;
		protected int timeUnitsElapsed = 0;

		@Override
		public void accept(Entry<RechargeableResourceType, BridgeHealResourceRecharge> t) {
			int total, nextTotalTime, r, timeUnitSuperscale;
			HealableResource res;
			BridgeHealResourceRecharge b;
			res = (b = t.getValue()).resource;
			r = res.getRegenerationAmount();
//			System.out.println(
//					this.getClass().getSimpleName() + " - r: " + r + "\tof " + res.getResourceType().getName());
			timeUnitSuperscale = getTimeSubUnitsEachUnit();
			total = 0;
			if (r != b.regenCache) {// detect changes
				b.regenCache = r;
				total = (b.millis * b.regenCache) / timeUnitSuperscale;
				b.millis = 0;
				b.amountRecovered = 0;
			}
			if (r == 0)
				return;

			// this.millis += timeUnitsElapsed;
			nextTotalTime = b.millis + timeUnitsElapsed;

			if (nextTotalTime >= timeUnitSuperscale) {
				total += (r - b.amountRecovered); // recover the "amount left"
				if (nextTotalTime > timeUnitSuperscale) {
					int superscaleFullroundPassed, newAmountRecoveredAfterFullround;
					/*
					 * at least one "superscale" amount has been passed, so remember it
					 */
					nextTotalTime -= timeUnitSuperscale;
					// then compute the "amount of superscale rounds" and the time remaining:
					superscaleFullroundPassed = 0; // <br>
					while (nextTotalTime >= timeUnitSuperscale) { // <br>
						nextTotalTime -= timeUnitSuperscale; // <br>
						superscaleFullroundPassed++; // <br>
					}
//					superscaleFullroundPassed = nextTotalTime / timeUnitSuperscale; //<br>
//					nextTotalTime /* let's indent the code */ %= timeUnitSuperscale; //<br>
					// then
					if (superscaleFullroundPassed > 0) { total += (r * superscaleFullroundPassed); }
					newAmountRecoveredAfterFullround = (r * nextTotalTime) / timeUnitSuperscale;
					total += newAmountRecoveredAfterFullround;
					b.millis = nextTotalTime;
					b.amountRecovered = newAmountRecoveredAfterFullround;
				} else { // optimize by not doing the remainder operation
					b.millis = 0;
					b.amountRecovered = 0;
				}
			} else {
				int nextCumulativeRecover;
				nextCumulativeRecover = (r * nextTotalTime) / timeUnitSuperscale;
				total += nextCumulativeRecover - b.amountRecovered;
				b.millis = nextTotalTime;
				b.amountRecovered = nextCumulativeRecover;
			}
			// DO THE HEAL
//			System.out.println(this.getClass().getSimpleName() + " - healed by total: " + total);

			if (total > 0) {
				objToHeal.healMyself(objToHeal.newHealInstance(t.getKey(), total));
			} else if (total < 0) { // && objToHeal instanceof DamageReceiverGeneric
				objToHeal.alterCurableResourceAmount(t.getKey(), total);
//				DamageReceiverGeneric drg;
//				drg = (DamageReceiverGeneric) objToHeal;
//				drg.receiveDamage(gm, new DamageGeneric(), this);
			}
		}
	}
}