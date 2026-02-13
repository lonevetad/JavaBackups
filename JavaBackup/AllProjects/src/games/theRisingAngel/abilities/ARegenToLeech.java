package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.objects.creature.CreatureSimple;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.misc.CreatureAttributesTRAn;

/**
 * Reduces all resources' regeneration and add a half of the reduced amount as a
 * leech percentage.<br>
 * The final leech percentage can't exceed the upper bound and the regeneration
 * reduction is adjusted accordingly in order to not waste that regeneration.
 * <p>
 * * BEWARE: if the regeneration was negative, then is set to 1 but the resource
 * leech could became negative too.
 */
public class ARegenToLeech extends AbilityModifyingAttributesRealTime {
	private static final long serialVersionUID = -5649806420997L;
	public static final String NAME = "Mors tua vita mea";
	public static final int RARITY = 2;
	protected static final AttributesTRAn[][] RIGEN_TO_LEECH_PAIRS = {
			{ AttributesTRAn.LifeRegen, AttributesTRAn.LifeLeechPercentage },
			{ AttributesTRAn.ManaRegen, AttributesTRAn.ManaLeechPercentage } };

	public ARegenToLeech(GModality gm) { super(gm, NAME, RESOURCES_TO_CONVERT); }

	protected static final AttributesTRAn[] RESOURCES_TO_CONVERT;
	static {
		int i = 0;
		RESOURCES_TO_CONVERT = new AttributesTRAn[RIGEN_TO_LEECH_PAIRS.length << 1];
		// unfold the matrix
		for (AttributesTRAn[] resource : RIGEN_TO_LEECH_PAIRS) {
			for (AttributesTRAn attribute : resource) {
				RESOURCES_TO_CONVERT[i++] = attribute;
			}
		}
	}

	@Override
	public void updateAttributeModifiersValues(GModality gm, CreatureSimple ah, CreatureAttributes ca,
			int levelAbility) {
		int regen, i, n, maxLeechBonus;
		CreatureAttributesTRAn cat;
		AttributesTRAn[] pair;
		AttributesTRAn resourceRegen, resourceLeech;
		cat = (CreatureAttributesTRAn) ca;
		n = RIGEN_TO_LEECH_PAIRS.length;
		i = -1;
		while (++i < n) {
			pair = RIGEN_TO_LEECH_PAIRS[i];
			// pick the original value plus the base-attributes gaining
			resourceRegen = pair[0];
			resourceLeech = pair[1];
			regen = cat.getValue(resourceRegen);
			maxLeechBonus = resourceLeech.upperBound() - cat.getValue(resourceLeech);
			if (regen > 1 && maxLeechBonus > 0) {

				if (regen > (maxLeechBonus << 1)) {
					// do not exceed
					regen = maxLeechBonus << 1;
				}
				// set the rigeneration
				this.attributesToModify[i << 1].setValue((-regen) + 1);
				// recycle regen as temporary variable for leech
				regen >>= 1;
				if (regen == 0) {
					regen = 1; // just do not waste life regeneration
				}
				this.attributesToModify[1 + (i << 1)].setValue(regen);
			}
		}
	}
}