package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.LivingObject;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.theRisingAngel.enums.AttributesTRAn;
import tools.ObjectWithID;

/**
 * When the life drops above a certain percentage
 * ({@link #LIFE_PERCENTAGE_THRESHOLD}) Provides a reduction of all kinds of
 * damages based on the current formula:<br>
 * <code> (a/b) * (c*{@link AttributesTRAn#Strength} + d*{@link AttributesTRAn#Constitution} + e*{@link AttributesTRAn#Health
 * })</code>, where all of <i>{a, b, c, d, e}</i> are some positive constants.
 * Currently, all of them are equal to <code>1</code>.
 */
//*  <code>a = 4; b = 1</code> and <code>c = d = e = 1</code>.
public class ADamageReductionOnLifeLowerToPhysicalAttributes extends AbilityModifyingAttributesRealTime
//		implements GEventObserver
{
	private static final long serialVersionUID = -4521020230699090L;
	public static final String NAME = "Trained to Endure";
	public static final int RARITY = 2, LIFE_PERCENTAGE_THRESHOLD = 25;

	protected boolean isActive; // , canBeActivated
//	protected List<String> eventsWatching;

	public ADamageReductionOnLifeLowerToPhysicalAttributes(GModality gm) {
		super(gm, NAME, new AttributeIdentifier[] { AttributesTRAn.MagicalDamageReduction,
				AttributesTRAn.PhysicalDamageReduction });
		this.isActive = false;
//		this.canBeActivated = false;
//		eventsWatching = Arrays.asList(new String[] { EventsTRAn.DamageReceived.getName() });
	}

//	public List<String> getEventsWatching() { return eventsWatching; }

	@Override
	public void resetAbility() {
		this.isActive = false;
//		this.canBeActivated = false;
		super.resetAbility();
	}

//	public void notifyEvent(GModality modality, IGEvent ge) {
//		boolean cba = false;
//		if (ge instanceof EventDamage) {
//			cba = canBeActivated();
//			System.out.println(this.getClass().getSimpleName() + " -> canBeActivated: " + cba);
//		}
//	}

	protected boolean canBeActivated() {
		ObjectWithID o;
		o = this.owner;
		if (o != null && o instanceof LivingObject) { return canBeActivated((LivingObject) o); }
		return false;
	}

	protected boolean canBeActivated(LivingObject lo) {
		return lo.getLife() <= (lo.getLifeMax() >> 2);// LIFE_PERCENTAGE_THRESHOLD %, that is 1/4
	}

	protected int getAmountReductionDamage(CreatureSimple ah, CreatureAttributes ca) {
		return (ca.getValue(AttributesTRAn.Strength) + ca.getValue(AttributesTRAn.Constitution)
				+ ca.getValue(AttributesTRAn.Health)); //
//		<< 2; // *4
	}

	@Override
	public void updateAttributeModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca,
			int targetLevel) {
		int amount;
		if (canBeActivated(ah)) {
//			System.out.println("DEFENCES BEFORE");
			this.isActive = true;
			amount = getAmountReductionDamage(ah, ca);
//			System.out.println(this.getClass().getSimpleName() + " -> amount: " + amount);
//			for (var a : getAttributesToModify()) {
//				var am = a.getAttributeModified();
//				System.out.println(am.getName() + " : " + ca.getValue(am));
//			}
			for (var a : getAttributesToModify()) {
				a.setValue(amount);
			}
		} else if (this.isActive) {
			this.isActive = false;
			System.out.println(this.getClass().getSimpleName() + " -> stopped working");
			for (var a : getAttributesToModify()) {
				a.setValue(0);
			}
		}
	}

	@Override
	public GModality getGameModality() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGameModality(GModality gameModality) { // TODO Auto-generated method stub
	}

}