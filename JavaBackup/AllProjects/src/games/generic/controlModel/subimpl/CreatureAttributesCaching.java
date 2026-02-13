package games.generic.controlModel.subimpl;

import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;

/**
 * Caches all attribute's modifications (as like {@link AttributeModification})
 * in a separated array: use {@link #addAttributeModification(int, int)} and
 * {@link #removeAttributeModification(int, int)}.
 */
public class CreatureAttributesCaching extends CreatureAttributes {

	public CreatureAttributesCaching(int attributesCount, IndexToObjectBackmapping itai) {
		super(attributesCount, itai);
		this.attributesModificationsApplied = new int[attributesCount];
		this.cacheValues = new int[attributesCount];
		this.isCacheAvailable = false;
	}

	protected transient boolean isCacheAvailable = false;
//	protected transient int attributesCountLeftToUpdate;
//	protected final boolean[] attributesUpdated;
	protected int[] attributesModificationsApplied, cacheValues;

	@Override
	public int getValue(AttributeIdentifier identifier) {
		if (!isCacheAvailable) { recalculateCache(); }
		return this.cacheValues[identifier.getIndex()];
	}

	protected void recalculateCache() {
		int i, tempAttr, tempBound;
		final int[] cv, ov, ama;
		AttributeIdentifier ai;
		IndexToObjectBackmapping itai;
		isCacheAvailable = true;
		cv = this.cacheValues;
		ov = super.originalValues;
		ama = this.attributesModificationsApplied;
		i = attributesCount;
		while (--i >= 0) {
			cv[i] = ov[i] + ama[i];
		}
		// check bounds
		i = this.attributesCount;
		itai = this.getIndexToAttributeIdentifier();
		while (--i >= 0) {
			tempAttr = cv[i];
			if (tempAttr < (tempBound = (ai = (AttributeIdentifier) itai.fromIndex(i)).lowerBound())) {
				cv[i] = tempBound;
			} else if (tempAttr > (tempBound = ai.upperBound())) { cv[i] = tempBound; }
		}
		this.isCacheAvailable = true;
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