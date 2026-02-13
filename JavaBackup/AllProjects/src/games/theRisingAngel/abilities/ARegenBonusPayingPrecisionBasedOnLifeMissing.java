package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.theRisingAngel.enums.AttributesTRAn;

/**
 * Augment the amount of life regeneration based on the amount of life missing,
 * capped to 100%.<br>
 * In case of negative life regeneration, it flips and counter-balance that
 * regeneration (the perfect balance would be reached at 0 life, but now you
 * would be dead).
 */
public class ARegenBonusPayingPrecisionBasedOnLifeMissing extends AbilityModifyingAttributesRealTime {
	private static final long serialVersionUID = -5649806420997L;
	public static final String NAME = "Horror vacui";
	public static final int RARITY = 4;
	protected static final AttributesTRAn[] REGEN_LIFE = { AttributesTRAn.LifeRegen,
			AttributesTRAn.PhysicalProbabilityPerThousandHit, AttributesTRAn.MagicalProbabilityPerThousandHit };

	public ARegenBonusPayingPrecisionBasedOnLifeMissing(GModality gameModality) {
		super(gameModality, NAME, REGEN_LIFE);
	}

	@Override
	public void updateAttributeModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca, int level) {
		int val, maxLife, missingLife;
		AttributeModification[] ams;
		ams = this.getAttributesToModify();
		val = ah.getLifeRegeneration();
		if (val != 0) {
			maxLife = ah.getLifeMax();
			if (val < 0)
				val = -val;
			val = (val * (missingLife = maxLife - ah.getLife()) / maxLife);
			ams[0].setValue(val);
			val = ca.getValue(AttributesTRAn.PhysicalProbabilityPerThousandHit);
			ams[1].setValue(-((val * missingLife) / maxLife));
			val = ca.getValue(AttributesTRAn.MagicalProbabilityPerThousandHit);
			ams[2].setValue(-((val * missingLife) / maxLife));
		}
	}
}