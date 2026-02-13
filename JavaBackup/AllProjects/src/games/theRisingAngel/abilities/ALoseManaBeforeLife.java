package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityBaseImpl;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.events.event.EventDamage;
import games.generic.controlModel.holders.AttributesHolder;
import games.generic.controlModel.objects.LivingObject;
import games.generic.controlModel.rechargeable.resources.holders.ManaHavingObject;
import games.theRisingAngel.enums.EventsTRAn;

public class ALoseManaBeforeLife extends AbilityBaseImpl implements GEventObserver {
	private static final long serialVersionUID = -451558465L;
	public static final String NAME = "Shielding the Temple";
	public static final int RARITY = 3, PRIORITY_OBSERVER_SHIELDING_THE_TEMPLE = 1;

	public ALoseManaBeforeLife() {
		eventsWatching = new ArrayList<>(2);
		eventsWatching.add(EventsTRAn.DamageReceived.getName());
		setRarityIndex(RARITY);
	}

	protected List<String> eventsWatching;

	@Override
	public List<String> getEventsWatching() { return eventsWatching; }

	@Override
	public int getObserverPriority() { return PRIORITY_OBSERVER_SHIELDING_THE_TEMPLE; }

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int damageAmount, manaAmount, min;
		LivingObject lo;
		EventDamage ed;
		ManaHavingObject mho;
		if (ge.getName() != EventsTRAn.DamageReceived.getName())
			return;
		ed = (EventDamage) ge;
		if (!(owner instanceof LivingObject && owner instanceof ManaHavingObject && owner instanceof AttributesHolder))
			return;
		lo = (LivingObject) owner;
		if (!ed.isTarget(lo))
			return;
		mho = (ManaHavingObject) owner;
		manaAmount = mho.getMana();
		damageAmount = ed.getDamageAmountOriginal();
		if (manaAmount <= 0 || damageAmount <= 0)
			return; // no mana to sacrifice
		min = damageAmount > manaAmount ? manaAmount : damageAmount;
		if (min > ed.getDamageAmountToBeApplied()) { min = ed.getDamageAmountToBeApplied(); }
		if (min <= 0) { return; }
		mho.setMana(manaAmount - min);
		ed.setDamageAmountToBeApplied(ed.getDamageAmountToBeApplied() - min);
	}

	@Override
	public void performAbility(GModality gm) {}

	@Override
	public void resetAbility() {}

	@Override
	public void performAbility(GModality gm, int targetLevel) { // TODO Auto-generated method stub
	}
}