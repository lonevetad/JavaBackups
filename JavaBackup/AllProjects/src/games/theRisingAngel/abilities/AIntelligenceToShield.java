package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.IndexableObject;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.generic.controlModel.rechargeable.resources.holders.ShieldHavingObject;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.RaritiesTRAn;

public class AIntelligenceToShield extends AbilityModifyingAttributesRealTime {
	private static final long serialVersionUID = -5601561564544L;
	public static final String NAME = "Repulsive Mindfield";
	public static final IndexableObject RARITY = RaritiesTRAn.Rare;
	protected static final AttributeModification[] SHIELD_PARAMS = {
			new AttributeModification(AttributesTRAn.ShieldMax, 0), //
			new AttributeModification(AttributesTRAn.ShieldRegen, 0), //
	};

	public AIntelligenceToShield(GModality gameModality) {
		super(gameModality, NAME, SHIELD_PARAMS);
		this.shieldAmoutPreUpdate = 0;
		this.hasLostShieldAmountDuringUpdate = false;
	}

	protected boolean hasLostShieldAmountDuringUpdate;
	protected int shieldAmoutPreUpdate;

	protected ShieldHavingObject getAbilityOwner() { return (ShieldHavingObject) this.getOwner(); }

	@Override
	protected void actionPreAttributeModificationUpdates() {
		ShieldHavingObject ownerShield;
		ownerShield = this.getAbilityOwner();
		this.shieldAmoutPreUpdate = ownerShield.getShield();
	}

	@Override
	public void updateAttributeModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca,
			int targetLevel) {
		int intell;
		intell = ca.getValue(AttributesTRAn.Intelligence);
		this.hasLostShieldAmountDuringUpdate = ca.getValue(AttributesTRAn.ShieldMax) < this.shieldAmoutPreUpdate;
		if (intell > 0) {
			this.attributesToModify[1].setValue(intell >> 1); // shield regen
			if (intell > (Integer.MAX_VALUE << 2)) {
				intell = Integer.MAX_VALUE;
			} else {
				intell <<= 2;
			}
			this.attributesToModify[0].setValue(intell); // shield max
		} else {
			this.attributesToModify[0].setValue(0);
			this.attributesToModify[1].setValue(0);
		}
	}

	@Override
	public void performAbility(GModality modality, int targetLevel) {
		super.performAbility(modality, targetLevel);

		// restore the "discarded shield amount", if any
		if (this.hasLostShieldAmountDuringUpdate) {
			this.getAbilityOwner().setShield(this.shieldAmoutPreUpdate);
			this.shieldAmoutPreUpdate = 0;
		}
	}

}
