package games.theRisingAngel.enums;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.attributes.AttributeIdentifier;
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
	Strength(0), Constitution(0), Health(0), //
	Defense(0), Dexterity(0), Precision(0), //
	Intelligence(0), Wisdom(0), Faith(0), //
	//
	Luck, // helps with drops, rarity, amount of modifiers and abilities and their rarities, and hitting, avoiding and critical probabilities
	/**
	 * It's a percentage value
	 */
	Velocity(1, 1000), //
	LifeMax(1), LifeRegen, //
	ManaMax(0), ManaRegen, //
	ShieldMax(0), ShieldRegen, //
	StaminaMax(0), StaminaRegen, //
	/**
	 * Expressed in milliseconds, delay from starting the sheild recharging after being depleated.
	 * EDIT 09-03-2026: see Velocity cast/attack formula
	 */
	ShieldDelayReduction, //
	// physical things
	PhysicalDamageBonus, PhysicalDamageMultiplierPercentageBonus, //
	PhysicalDamageReduction, PhysicalDamageMultiplierPercentageReduction, //
	PhysicalProbabilityPerThousandHit, PhysicalProbabilityPerThousandAvoid, //
	VelocityAttackStrikePercentage(1), //  // TODO (2026-02-13) shouldn't it be a "percentage of how much times it's faster"? i.e.: finalVelocity = (originalVelocity * 100) / (100 + Math.max(-99, VelocityAttackStrikePercentage)) ?
	// magical things
	MagicalDamageBonus, MagicalDamageMultiplierPercentageBonus, //
	MagicalDamageReduction, MagicalDamageMultiplierPercentageReduction, //
	MagicalProbabilityPerThousandHit, MagicalProbabilityPerThousandAvoid, //
	// velocity cast/attack
	VelocitySpellCastPercentage(-99, 10000), // TODO (2026-02-13) shouldn't it be a "percentage of how much times it's faster"? i.e.: finalVelocity = (originalVelocity * 100) / (100 + Math.max(-99, VelocitySpellCastPercentage)) ?
	CostCastReductionPercentage(-10000, 99), //
	// crit
	CriticalProbabilityPerThousandHit, CriticalMultiplierPercentageBonus(0), //
	CriticalProbabilityPerThousandAvoid, CriticalMultiplierPercentageReduction, //
	// leech
	LifeLeechPercentage(-1000, 1000), //
	ManaLeechPercentage(-1000, 1000), //
	ShieldLeechPercentage(-1000, 1000), //
	StaminaLeechPercentage(-1000, 1000), //
	// misc
	ReflectionDamagePercentage(0), //
	ExperienceBonusPercentage(0, 10000);

	//

	public static final int FIRST_INDEX_ATTRIBUTE_UPGRADABLE, LAST_INDEX_ATTRIBUTE_UPGRADABLE, INDEX_LUCK,
			ATTRIBUTES_UPGRADABLE_COUNT;
	public static final AttributesTRAn[] ALL_ATTRIBUTES;
	public static final IndexToObjectBackmapping INDEX_TO_ATTRIBUTE_TRAn;
	private static Map<String, AttributesTRAn> attTRArByName = null;	
	public static final String NAME;
	static {
		FIRST_INDEX_ATTRIBUTE_UPGRADABLE = Strength.getIndex();
		LAST_INDEX_ATTRIBUTE_UPGRADABLE = Faith.getIndex();
		INDEX_LUCK = Luck.getIndex();
		ATTRIBUTES_UPGRADABLE_COUNT = 1 + (LAST_INDEX_ATTRIBUTE_UPGRADABLE - FIRST_INDEX_ATTRIBUTE_UPGRADABLE);
		ALL_ATTRIBUTES = AttributesTRAn.values();
		INDEX_TO_ATTRIBUTE_TRAn = (int i) -> AttributesTRAn.ALL_ATTRIBUTES[i];
		NAME = AttributesTRAn.class.getName();
	}

//

	AttributesTRAn() {
		this(Integer.MIN_VALUE + 1);
	}

	AttributesTRAn(int minimum) {
		this(minimum, Integer.MAX_VALUE - 1);
	}

	AttributesTRAn(int minimum, int maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}

	public final int minimum, maximum;

	@Override
	public int getIndex() {
		return this.ordinal();
	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public Long getID() {
		return (long) this.ordinal();
	}

	@Override
	public boolean setID(Long newID) {
		return false;
	}

	@Override
	public int upperBound() {
		return this.maximum;
	}

	@Override
	public int lowerBound() {
		return this.minimum;
	}

	@Override
	public IndexToObjectBackmapping getFromIndexBackmapping() {
		return INDEX_TO_ATTRIBUTE_TRAn;
	}

	public static AttributesTRAn getAttributeTRArByName(String name) {
		AttributesTRAn a;
		Map<String, AttributesTRAn> m;
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null");
		if (attTRArByName == null) {
			m = attTRArByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
			for (AttributesTRAn at : ALL_ATTRIBUTES) {
				m.put(at.name(), at); // using name() instead of getName() just to be faster
			}
		}
		a = attTRArByName.get(name);
		if (a == null)
			throw new IllegalArgumentException("Invalid name for AttributesTRAr: " + name);
		return a;
	}

	public static AttributesTRAn getAttributeTRArByIndex(int index) {
		return ALL_ATTRIBUTES[index];
	}

	public static AttributesTRAn damageReductionByType(DamageTypesTRAn dt) {
		return (dt == DamageTypesTRAn.Physical) ? AttributesTRAn.PhysicalDamageReduction
				: AttributesTRAn.MagicalDamageReduction;
	}

	public static AttributesTRAn damageBonusByType(DamageTypesTRAn dt) {
		return (dt == DamageTypesTRAn.Physical) ? AttributesTRAn.PhysicalDamageBonus
				: AttributesTRAn.MagicalDamageBonus;
	}
}