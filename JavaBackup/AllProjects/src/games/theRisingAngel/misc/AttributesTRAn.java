package games.theRisingAngel.misc;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.misc.AttributeIdentifier;
import tools.Comparators;

/**
 * All attributes for creatures and player.
 * <p>
 * Important notes:
 * <ul>
 * <li>Luck should add up to the above concepts and to the drop (what kinds of
 * drops, rarity, amount of modifiers and abilities and their rarities).</li>
 * <li>Velocity is free to interpretation (usually, "10-th of a unit each
 * second").</li>
 * <li>Probabilities to hit and avoid are competitive: upon dealing receiving an
 * attack, the attacker:</li>
 * </ul>
 */
public enum AttributesTRAn implements AttributeIdentifier {
	// the following 9 are the base attributes
	Strength(true), Constitution(true), Health(true), //
	Defense(true), Dexterity(true), Precision(true), //
	Intelligence(true), Wisdom(true), Faith(true),
	//
	Luck, //
	LifeMax(true), RegenLife, ManaMax(true), RegenMana, ShieldMax(true), RegenShield, //
	Velocity(true),
	//
	DamageBonusPhysical, DamageReductionPhysical, //
	ProbabilityPerThousandHitPhysical, ProbabilityPerThousandAvoidPhysical, //
	DamageBonusMagical, DamageReductionMagical, //
	ProbabilityPerThousandHitMagical, ProbabilityPerThousandAvoidMagical, //
	//
	CriticalProbabilityPerThousand(true), CriticalMultiplierPercentage(true), //
	CriticalProbabilityPerThousandAvoid(true), CriticalMultiplierPercentageReduction(true), //
	LifeLeechPercentage, ManaLeechPercentage, ShieldLeechPercentage//
	;

	public final boolean isStrictlyPositive;

	AttributesTRAn() { this(false); }

	AttributesTRAn(boolean flag) { this.isStrictlyPositive = flag; }

	@Override
	public int getIndex() { return ordinal(); }

	@Override
	public String getName() { return name(); }

	@Override
	public Integer getID() { return ordinal(); }

	//

	public static final int FIRST_INDEX_ATTRIBUTE_UPGRADABLE = Strength.getIndex(),
			LAST_INDEX_ATTRIBUTE_UPGRADABLE = Faith.getIndex(),
			ATTRIBUTES_UPGRADABLE_COUNT = 1 + (LAST_INDEX_ATTRIBUTE_UPGRADABLE - FIRST_INDEX_ATTRIBUTE_UPGRADABLE);;
	public static final AttributesTRAn[] VALUES = AttributesTRAn.values();
	private static Map<String, AttributesTRAn> attTRArByName = null;

	public static AttributesTRAn getAttributeTRArByName(String name) {
		AttributesTRAn a;
		Map<String, AttributesTRAn> m;
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null");
		if (attTRArByName == null) {
			m = attTRArByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
			for (AttributesTRAn at : VALUES) {
				m.put(at.name(), at); // using name() instead of getName() just to be faster
			}
		}
		a = attTRArByName.get(name);
		if (a == null)
			throw new IllegalArgumentException("Invalid name for AttributesTRAr: " + name);
		return a;
	}

	public static AttributesTRAn getAttributeTRArByIndex(int index) { return VALUES[index]; }

	public static AttributesTRAn damageReductionByType(DamageTypesTRAn dt) {
		return (dt == DamageTypesTRAn.Physical) ? AttributesTRAn.DamageReductionPhysical
				: AttributesTRAn.DamageReductionMagical;
	}

	public static AttributesTRAn damageBonusByType(DamageTypesTRAn dt) {
		return (dt == DamageTypesTRAn.Physical) ? AttributesTRAn.DamageBonusPhysical
				: AttributesTRAn.DamageBonusMagical;
	}
}