package games.old;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.events.GEventObserver;
import games.generic.controlModel.events.IGEvent;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.creature.BaseCreatureRPG;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityModifyingSingleAttributeRealTime;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.EventsTRAn;
import games.theRisingAngel.events.EventDamageTRAn;
import games.theRisingAngel.events.EventsTRAr;
import games.theRisingAngel.events.EventsTRAr;
import tools.ObjectWithID;

public class AMoreDamageReceivedMoreLifeRegen_OLD extends AbilityModifyingSingleAttributeRealTime
		implements GEventObserver {
	private static final long serialVersionUID = 5411087000163L;
	public static final int VALUE_DECREMENT_PER_TICK = 4;
	public static final String NAME = "Pain Rinvigoring";

	public AMoreDamageReceivedMoreLifeRegen_OLD() {
		super(AttributesTRAn.RegenLife, NAME);
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(
//		this.getAttributeToModify().getAttributeModified().getName()
				EventsTRAn.DamageReceived.getName());
		ticks = 0;
		thresholdTime = 1000;
		accumulatedLifeRegen = 0;
	}

	protected int accumulatedLifeRegen;
	protected long ticks, thresholdTime;
	protected List<String> eventsWatching;
//protected CreatureSimple creatureReferred;

	@Override
	public Integer getObserverID() {
		return getID();
	}

//public CreatureSimple getCreatureReferred() {return creatureReferred;}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public ObjectWithID getOwner() {
		return this.getEquipItem().getCreatureWearingEquipments();
	}

	@Override
	public long getTimeThreshold() {
		return thresholdTime;
	}

//

	@Override
	public void setOwner(ObjectWithID owner) {
		if (owner instanceof BaseCreatureRPG)
//	setCreatureReferred
			this.getEquipItem().getBelongingEquipmentSet().setCreatureWearingEquipments((BaseCreatureRPG) owner);
	}

//public void setCreatureReferred(CreatureSimple creatureReferred) {this.creatureReferred = creatureReferred;}

//

	@Override
	public void resetAbility() {
		super.resetAbility();
		ticks = 0;
		thresholdTime = 1000;
		accumulatedLifeRegen = 0;
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		if (EventsTRAn.DamageReceived.getName() == ge.getName()) {
			int d;
			EventDamageTRAn<?> dEvent;
//	AttributeModification am;
//	CreatureAttributes ca;
			dEvent = (EventDamageTRAn<?>) ge;
			if (dEvent.getTarget() ==
			// check equality because it's bounded to the "wearer"
			this.getEquipItem().getCreatureWearingEquipments() && (d = dEvent.getDamageOriginal().getDamageAmount()) >= 4) {
				// increase of 12.5% of damage, so minimum is 4

//		am = this.getAttributeToModify();
//		d = am.getValue() + (d >> 3);// ">>2" because 25% == /4.0
//		ca = this.getEquipItem().getCreatureWearingEquipments().getAttributes();
//		// update: remove previous, add new value
//		ca.removeAttributeModifier(am);
//		am.setValue(d > 0 ? d : 0);
//		ca.applyAttributeModifier(am);
//		System.out.println("같같같같같� now regen is: " + am.getValue());
				d >>= 3;
				if (d > 0)
					accumulatedLifeRegen += d;
			}
		}
	}

	@Override
	public void updateAttributesModifiersValues(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca) {
		int v, t;
		AttributeModification am;
		am = this.getAttributesToModify()[0];
		v = am.getValue();
		if (v > 0) {
			if (++ticks >= (1000 / AbilityModifyingSingleAttributeRealTime.MILLISEC_ATTRIBUTE_UPDATE))
				ticks = 0;
			t = v >> 3; // 12.5%
			v += accumulatedLifeRegen - (VALUE_DECREMENT_PER_TICK > t ? VALUE_DECREMENT_PER_TICK : t);
			am.setValue(v > 0 ? v : 0); // limit is 0
			System.out.println("______new life regen value : " + am.getValue() + " .... AMoreDamageMoreRegenBLA");
		} else {
			if (accumulatedLifeRegen > 0)
				am.setValue(accumulatedLifeRegen);
		}
		accumulatedLifeRegen = 0;
	}

}