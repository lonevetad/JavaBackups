package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.enums.AttributesTRAn;

public class AAttrSingleBonusMalusRandomFixedAmount extends AAttrSingleBonusMalusRandom {
	private static final long serialVersionUID = 2154186081264778L;
	public static final int MALUS_AMOUNT = -3, BONUS_AMOUNT = 10;

	public static final String NAME = AAttrSingleBonusMalusRandom.NAME + " absolute";

	public AAttrSingleBonusMalusRandomFixedAmount(GModality gameModality) { super(gameModality, NAME); }

	@Override
	public int getAttributesBonusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr) {
		return BONUS_AMOUNT;
	}

	@Override
	public int getAttributesMalusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr) {
		return MALUS_AMOUNT;
	}

	@Override
	public void onAddedToGame(GModality gm) { // TODO Auto-generated method stub
	}

	@Override
	public void onRemovedFromGame(GModality gm) { // TODO Auto-generated method stub
	}
}