package games.generic.controlModel.subimpl;

import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;

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
	protected final int[] attributesModificationsApplied, cacheValues;

	@Override
	public int getValue(int index) {
		boolean bcNotNull;
		int v, i;
		CreatureAttributesBonusesCalculator bc;
		if (isCacheAvailable)
			return this.cacheValues[index];
		isCacheAvailable = true;
		i = attributesCount;
		if (bcNotNull = (bc = this.bonusCalculator) != null)
			bc.markCacheAsDirty();

		while (--i >= 0) {// update the values
			v = super.originalValues[i] + this.attributesModificationsApplied[i];
			if (bcNotNull)
				v += bc.getBonusForValue(i);
			this.cacheValues[i] = v;
		}
//		if (bcNotNull) {
//			i = attributesCount;
//			while (--i >= 0) // update the values
//				this.cacheValues[i] += bc.getBonusForValue(i);
//		}
		return this.cacheValues[index];
	}

//	public int[] getComputedAttributesModifications() {
//		return attributesModifications;
//	}
	@Override
	public void setBonusCalculator(CreatureAttributesBonusesCalculator bonusCalculator) {
		this.isCacheAvailable = false;
		super.setBonusCalculator(bonusCalculator);
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