package games.generic.controlModel.subimpl;

import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;

/**
 * Caches all attribute's modifications (as like {@link AttributeModification})
 * in a separated array: use {@link #addAttributeModification(int, int)} and
 * {@link #removeAttributeModification(int, int)}.
 */
public class CreatureAttributesCaching extends CreatureAttributes {

	public CreatureAttributesCaching(int attributesCount) {
		super(attributesCount);
		this.attributesModificationsApplied = new int[attributesCount];
		this.cacheValues = new int[attributesCount];
		this.isCacheAvailable = false;
	}

	protected transient boolean isCacheAvailable = false;
//	protected transient int attributesCountLeftToUpdate;
//	protected final boolean[] attributesUpdated;
	protected int[] attributesModificationsApplied, cacheValues;

	@Override
	public int getValue(int index) {
		throw new UnsupportedOperationException(
				"Cannot invoke on integer index because it cannot perform \"AttributeIdentifier#isStrictlyPositive()\" check");
	}

	@Override
	public int getValue(AttributeIdentifier identifier) {
		int v, index;
		index = identifier.getIndex();
		if (!isCacheAvailable) { recalculateCache(); }
		v = this.cacheValues[index];
		return (identifier.isStrictlyPositive() && v < 0) ? 0 : v;
	}

	protected void recalculateCache() {
		int i, ac;
		final int[] cv, ov, ama;
		isCacheAvailable = true;
		cv = this.cacheValues;
		ov = super.originalValues;
		ama = this.attributesModificationsApplied;
		ac = attributesCount;
		// then others
		i = ac;
		while (--i >= 0) {// update the values
			cv[i] = ov[i] + ama[i];
		}
	}

	/**
	 * Set the attribute's (identified by the index: first parameter) value (second
	 * parameter).
	 */
	@Override
	public void setOriginalValue(int index, int value) {
		this.isCacheAvailable = false;
		this.originalValues[index] = value;
	}

	@Override
	public void applyAttributeModifier(AttributeModification eam) {
		this.isCacheAvailable = false;
		this.attributesModificationsApplied[eam.getAttributeModified().getIndex()] += eam.getValue();
	}

	@Override
	public void removeAttributeModifier(AttributeModification eam) {
		this.isCacheAvailable = false;
		this.attributesModificationsApplied[eam.getAttributeModified().getIndex()] -= eam.getValue();
	}

//	public void addAttributeModification(int index, int value) { this.computedAttributesModifications[index] += value; }
//	public void removeAttributeModification(int index, int value) { this.computedAttributesModifications[index] -= value; }
}