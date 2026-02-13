package games.theRisingAngel.abilities;

import java.util.Random;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.impl.AbilAttributesBonusMalusChanging;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.enums.AttributesTRAn;

/**
 * Apply a bonus and a malus of some attribute, at random, changing those two
 * values each time.
 *
 * @author ottin
 *
 */
public abstract class AAttrSingleBonusMalusRandom extends AbilAttributesBonusMalusChanging {
	private static final long serialVersionUID = 305617900000L;
	public static final String NAME = "Amorphous";
	public static final int RARITY = 2;
	public static final long TIME_TRESHOLD_CHANGING = 4000;

	public AAttrSingleBonusMalusRandom(GModality gameModality) { this(gameModality, NAME); }

	public AAttrSingleBonusMalusRandom(GModality gameModality, int level) { this(gameModality, NAME, level); }

	public AAttrSingleBonusMalusRandom(GModality gameModality, String name) { this(gameModality, name, 0); }

	public AAttrSingleBonusMalusRandom(GModality gameModality, String name, int level) {
		super(gameModality, name, level);
		bonus = new AttributeModification(AttributesTRAn.Luck, 0); // Luck will never be changed
		malus = new AttributeModification(AttributesTRAn.Luck, 0); // Luck will never be changed
	}

//	protected AttributesTRAn bonus, malus;
	protected AttributeModification bonus, malus;

	@Override
	public long getTimeThreshold() { return TIME_TRESHOLD_CHANGING; }

	@Override
	public void removeAttributesBonusesMaluses(GModality gm, CreatureAttributes ca, int targetLevel) {
		ca.removeAttributeModifier(bonus);
		ca.removeAttributeModifier(malus);
	}

	@Override
	public void applyAttributesBonusesMaluses(GModality gm, CreatureAttributes ca, int targetLevel) {
		ca.applyAttributeModifier(bonus);
		ca.applyAttributeModifier(malus);
	}

	public abstract int getAttributesBonusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr);

	public abstract int getAttributesMalusValue(GModality gm, CreatureAttributes ca, AttributesTRAn attr);

	@Override
	public void updateAttributesBonusesMaluses(GModality gm, CreatureAttributes ca, int targetLevel) {
		Random r;
		AttributesTRAn newBonus, newMalus;
		r = gm.getRandom();

		// bonus
		do {
			newBonus = AttributesTRAn.ALL_ATTRIBUTES[r.nextInt(AttributesTRAn.ATTRIBUTES_UPGRADABLE_COUNT)
					+ AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE];
			do {
				newMalus = AttributesTRAn.ALL_ATTRIBUTES[r.nextInt(AttributesTRAn.ATTRIBUTES_UPGRADABLE_COUNT)
						+ AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE];
			} while (newBonus == newMalus);

		} while (newBonus == this.bonus.getAttributeModified() && newMalus == this.malus.getAttributeModified());

		this.bonus.setAttributeModified(newBonus);
		this.bonus.setValue(getAttributesBonusValue(gm, ca, newBonus));
		this.malus.setAttributeModified(newMalus);
		this.malus.setValue(getAttributesMalusValue(gm, ca, newMalus));
	}

}