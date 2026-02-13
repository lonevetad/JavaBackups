package games.generic.controlModel.misc;

import java.util.Arrays;
import java.util.Objects;

import games.generic.controlModel.GModality;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.holders.AttributesHolder;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentSet;
import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;

/**
 * Implements a set of attributes. "Creature" could be "the player's character",
 * a simple enemy or something else.<br>
 * Currently, attributes are treated as integers.<br>
 * it's advised to separate "original values" and "values obtained from
 * equipments, buffs, auras, etc".
 */
public abstract class CreatureAttributes {
	protected final int attributesCount;
	protected final int[] originalValues;
	protected final IndexToObjectBackmapping indexToAttributeIdentifier;

	public CreatureAttributes(int attributesCount, IndexToObjectBackmapping itai) {
		if (attributesCount < 1) {
			throw new IllegalArgumentException("Cannot have less than 1 attributes: " + attributesCount);
		}
		Objects.requireNonNull(itai);
		this.originalValues = new int[this.attributesCount = attributesCount];
		this.indexToAttributeIdentifier = itai;
	}

	//

	public int getAttributesCount() { return attributesCount; }

	/**
	 * Calls {@link #getOriginalValue(AttributeIdentifier)} through the
	 * {@link IndexToObjectBackmapping} instance provided to this class
	 * ({@link CreatureAttributes}) constructor. Therefore, the pareameter should be
	 * the value returned by {@link AttributeIdentifier#getIndex()}.
	 */
	public int getOriginalValue(int index) {
		AttributeIdentifier ai = (AttributeIdentifier) this.indexToAttributeIdentifier.fromIndex(index);
		if (ai == null) {
			throw new IllegalArgumentException("Can't find an AttributeIdentifier with index: " + index);
		}
		return this.getOriginalValue(ai);
	}

	/**
	 * Get the value of a specific attribute. This method makes use of the
	 * {@link AttributeIdentifier#getIndex()} function.<br>
	 * The returned value is the original value, the ones associated with the object
	 * holding this class's instance (usually instances of {@link AttributesHolder},
	 * for example {@link CreatureOfRPGs}) and that defines them.
	 * <p>
	 * It differs from {@link #getValue(int)} because this methods does not take
	 * into account all {@link AttributeModification}s applied to it through
	 * {@link EquipmentItem}, {@link AbilityGeneric}, spells, auras, etc.
	 */
	public int getOriginalValue(AttributeIdentifier identifier) {
		return this.originalValues[identifier.getIndex()];
	}

	/**
	 * Get the value of a specific attribute, identified by its index.<br>
	 * The index is usually provided through {@link AttributeIdentifier#getIndex()}.
	 * <br>
	 * Could be the result of some calculation, maybe based on
	 * {@link #getOriginalValue(int)} and something else.
	 * <p>
	 * It differs from {@link #getOriginalValue(int)} because this methods compute
	 * the sum of the original value and all of alterations provided by equipments
	 * (i.e.: {@link EquipmentItem} and their {@link AttributeModification}),
	 * effects, auras, magics, abilities, battlefield's influences, etc.
	 */
	protected int getValue(int index) {
		AttributeIdentifier ai = (AttributeIdentifier) this.indexToAttributeIdentifier.fromIndex(index);
		if (ai == null) {
			throw new IllegalArgumentException("Can't find an AttributeIdentifier with index: " + index);
		}
		return this.getValue(ai);
	}

	/**
	 * Calls {@link #getValue(int)} passing, as parameter, the value returned by the
	 * invocation of {@link AttributeIdentifier#getIndex()} over the first
	 * parameter.
	 */
	public int getValue(AttributeIdentifier identifier) { return this.originalValues[identifier.getIndex()]; }

	public IndexToObjectBackmapping getIndexToAttributeIdentifier() { return indexToAttributeIdentifier; }

	//

	/**
	 * Set the attribute's (identified by the index: first parameter) value (second
	 * parameter).
	 */
	public void setOriginalValue(int index, int value) {
		AttributeIdentifier ai = (AttributeIdentifier) this.indexToAttributeIdentifier.fromIndex(index);
		if (ai == null) {
			throw new IllegalArgumentException("Can't find an AttributeIdentifier with index: " + index);
		}
		this.setOriginalValue(ai, value);
	}

	public void setOriginalValue(AttributeIdentifier identifier, int value) {
		int temp;
		if ((temp = identifier.lowerBound()) > value) {
			value = temp;
		} else if ((temp = identifier.upperBound()) < value) { value = temp; }
		this.originalValues[identifier.getIndex()] = value;
	}

	//

//	public abstract void addAttributeModification(int index, int value);
//	public abstract void removeAttributeModification(int index, int value);

	/**
	 * Apply (add) the given {@link AttributeModification} to this set of
	 * attributes.<br>
	 * Usually it's provided by {@link EquipmentItem#getBaseAttributeModifiers()}
	 * and usually this method is invoked during
	 * {@link EquipmentItem#onEquip(GModality)}, that is invoked by
	 * {@link EquipmentSet#addEquipmentItem(GModality, EquipmentItem)}.
	 * <p>
	 * It's advised to apply it to a transient "parallel instance" of
	 * {@link #getOriginalValues()} so that ALL modifiers merges there (making
	 * simple and easy to separate original {@link CreatureOfRPGs}'s value and the
	 * ones provided by abilities and the equipments.
	 */
	public abstract void applyAttributeModifier(AttributeModification eam);

	/** Opposite of {@link #applyAttributeModifier(AttributeModification)}. */
	public abstract void removeAttributeModifier(AttributeModification eam);

	@Override
	public String toString() { return "CreatureAttributes [originalValues=" + Arrays.toString(originalValues) + "]"; }

	//

}