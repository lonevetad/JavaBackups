package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import java.util.Arrays;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.generic.controlModel.subimpl.CreatureAttributesBaseAndDerivedCaching;

/**
 * Grant bonuses of a set of {@link AttributeIdentifier} (second constructor's
 * parameter, the array) depending on values (taken by the third constructor's
 * parameter, a matrix) computed using some
 * {@link CreatureAttributesBonusesCalculator} implementation.
 * <p>
 * By default,
 * {@link #computeBonusForAttributeDependingOnIdentifier(AttributeModification, CreatureAttributesBonusesCalculator, AttributeIdentifier)}
 * returns the 25% of the value.
 */
public class AbilityBonusDependingOnOtherBonuses extends AbilityModifyingAttributesRealTime {
	private static final long serialVersionUID = 1L;

	public AbilityBonusDependingOnOtherBonuses(String name, AttributeIdentifier[] attributesToModify,
			AttributeIdentifier[][] modifcationsEachAttributes) {
		super(name, attributesToModify);
		if (modifcationsEachAttributes == null || modifcationsEachAttributes.length != attributesToModify.length)
			throw new IllegalArgumentException("Null or incorrect amount of bonuses for attributes to modify (expected "
					+ attributesToModify.length + " rows): " + Arrays.deepToString(modifcationsEachAttributes));
		this.modifcationsEachAttributes = modifcationsEachAttributes;
	}

	protected final AttributeIdentifier[][] modifcationsEachAttributes;

	@Override
	public void updateAttributesModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca) {
		int v, i;
		AttributeModification am;
		AttributeModification[] atm;
		AttributeIdentifier[] row;
		CreatureAttributesBonusesCalculator bonusCalc;
		atm = this.attributesToModify;
		bonusCalc = ((CreatureAttributesBaseAndDerivedCaching) ca).getBonusCalculator();
		i = atm.length;
		while (--i >= 0) {
			v = 0;
			am = atm[i];
			row = modifcationsEachAttributes[i];
			for (AttributeIdentifier ai : row) {
				v += computeBonusForAttributeDependingOnIdentifier(atm[i], bonusCalc, ai);
			}
			am.setValue(v);
		}
	}

	/** Default implementation: 25% of the value returned by */
	public int computeBonusForAttributeDependingOnIdentifier(AttributeModification am,
			CreatureAttributesBonusesCalculator bonusCalc, AttributeIdentifier ai) {
		// ignore am, not needed by default
		return bonusCalc.getBonusFor(ai.getIndex()) >> 2;
	}

}