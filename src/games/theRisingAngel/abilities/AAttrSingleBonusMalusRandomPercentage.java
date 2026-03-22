package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.generic.controlModel.subimpl.CreatureAttributesBaseAndDerivedCaching;
import games.theRisingAngel.enums.AttributesTRAn;

/**
 * More variable version: bonus are about the <code>25%</code> of the computed
 * value (using {@link CreatureAttributesBonusesCalculator}) and malus are
 * <code>12.5%</code> .
 */
public class AAttrSingleBonusMalusRandomPercentage extends AAttrSingleBonusMalusRandom {
	private static final long serialVersionUID = -2154186081264778L;
	public static final String NAME = AAttrSingleBonusMalusRandom.NAME + " relative";

	public AAttrSingleBonusMalusRandomPercentage(GModality gameModality) { super(gameModality, NAME); }

	@Override
	public int getAttributesBonusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr) {
		CreatureAttributesBaseAndDerivedCaching cab;
		cab = (CreatureAttributesBaseAndDerivedCaching) ca;
		return cab.getBonusCalculator().getBonusFor(attr.getIndex()) >> 2;
	}

	@Override
	public int getAttributesMalusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr) {
		CreatureAttributesBaseAndDerivedCaching cab;
		cab = (CreatureAttributesBaseAndDerivedCaching) ca;
		return -(cab.getBonusCalculator().getBonusFor(attr.getIndex()) >> 3);
	}
}