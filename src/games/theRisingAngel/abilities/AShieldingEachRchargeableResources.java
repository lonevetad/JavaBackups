package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityBaseWithCustomName;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.objects.LivingObject;
import games.generic.controlModel.rechargeable.resources.RechargeableResourceType;
import games.generic.controlModel.rechargeable.resources.ResourceAmountRecharged;
import games.theRisingAngel.enums.DamageTypesTRAn;
import games.theRisingAngel.enums.RechargeableResourcesTRAn;
import games.theRisingAngel.events.EventDamageTRAn;
import games.theRisingAngel.events.EventResourceRechargeTRAr;
import tools.ObjectWithID;

public class AShieldingEachRchargeableResources extends AbilityBaseWithCustomName implements GEventObserver {
	private static final long serialVersionUID = 1L;
	public static final int MAX_SHIELD = 256, RARITY = 4,
			PRIORITY_DAMAGE_OBSERVER = ALoseManaBeforeLife.PRIORITY_OBSERVER_SHIELDING_THE_TEMPLE << 1;
	public static final String NAME = "Essence insofference";
	protected static List<String> EVENTS_WATCHING = null;

	protected static List<String> getEventsWatching_SECR() {
		DamageTypesTRAn[] dd;
		List<RechargeableResourceType> vv;
		if (EVENTS_WATCHING != null)
			return EVENTS_WATCHING;
		vv = RechargeableResourcesTRAn.ALL_RECHARGEABLE_RESOURCES_TRAn;
		dd = DamageTypesTRAn.values();
		EVENTS_WATCHING = new ArrayList<>(dd.length + vv.size());
		for (RechargeableResourceType ht : vv) {
			EVENTS_WATCHING.add(ht.getName());
		}
		for (DamageTypesTRAn dt : dd) {
			EVENTS_WATCHING.add(dt.getName());
		}
		return EVENTS_WATCHING;
	}

	public AShieldingEachRchargeableResources(String name, int level) {
		super(name, level);
		shields = new int[RechargeableResourcesTRAn.ALL_RECHARGEABLE_RESOURCES_TRAn.size()];
		resetAbility();
	}

	public AShieldingEachRchargeableResources(String name) { this(name, 0); }

	public AShieldingEachRchargeableResources() { this(NAME, 0); }

	protected int[] shields;
//	protected List<String> eventsWatching;

	@Override
	public int getObserverPriority() { return PRIORITY_DAMAGE_OBSERVER; }

	@Override
	public List<String> getEventsWatching() { return getEventsWatching_SECR(); }

	@Override
	public void performAbility(GModality gm, int targetLevel) {}

	@Override
	public void resetAbility() { Arrays.fill(shields, MAX_SHIELD); }

	protected RechargeableResourcesTRAn healForDamage(DamageTypesTRAn dt) {
		if (dt == DamageTypesTRAn.Physical)
			return RechargeableResourcesTRAn.Life;
		else if (dt == DamageTypesTRAn.Magical)
			return RechargeableResourcesTRAn.Mana;
		else
			return null;
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int index;
		if (ge instanceof EventDamageTRAn) {
			// consume the shield to reduce the damage
			int min;
			EventDamageTRAn ed;
			ObjectWithID o;
			RechargeableResourcesTRAn ht;
			DamageGeneric dg;
			ed = (EventDamageTRAn) ge;
			o = getOwner();
			if (!(o instanceof LivingObject && ed.isTarget((LivingObject) o)))
				return;
			dg = ed.getDamageOriginal();
			ht = healForDamage((DamageTypesTRAn) dg.getDamageType());
			if (ht == null)
				return;
			index = ht.ordinal();
			min = Math.min(ed.getDamageReducedByTargetArmors(), this.shields[index]);
			// shields the damage
			this.shields[index] -= min;
			ed.setDamageAmountToBeApplied(ed.getDamageAmountToBeApplied() - min);
		} else if (ge instanceof EventResourceRechargeTRAr<?>) {
			// heal the shield
			int amount;
			RechargeableResourceType ht;
			EventResourceRechargeTRAr<?> eh;
			ResourceAmountRecharged hg;
			eh = (EventResourceRechargeTRAr<?>) ge;
			hg = eh.getResourceAmountRecharged();
			ht = hg.getRechargedResource();
			index = ht.getIndex();
			amount = this.shields[index] + hg.getRechargedAmount();
			if (amount > MAX_SHIELD)
				amount = MAX_SHIELD;
			this.shields[index] = amount;
		}
	}

}