package games.generic.controlModel.subimpl;

import java.util.Objects;

import games.generic.controlModel.CreatureAttributesBaseAndDerived;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributesBonusesCalculator;
import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;

public class CreatureAttributesBaseAndDerivedCaching extends CreatureAttributesBaseAndDerived {

	public CreatureAttributesBaseAndDerivedCaching(int attributesCount, IndexToObjectBackmapping itai) {
		super(attributesCount, itai);
		this.attributesModificationsApplied = new int[attributesCount];
		this.cacheValues = this.newCacheValues(attributesCount);
		this.isCacheAvailable = false;
	}

	protected transient boolean isCacheAvailable = false;
//protected transient int attributesCountLeftToUpdate;
//protected final boolean[] attributesUpdated;
	protected final int[] attributesModificationsApplied, cacheValues;

	@Override
	public int getValue(AttributeIdentifier identifier) {
		if (!isCacheAvailable) { recalculateCache(); }
		return this.cacheValues[identifier.getIndex()];
	}

	//

	protected int[] newCacheValues(int length) { return new int[length]; }

	protected void recalculateCache() {
		int i, tempAttr, tempBound;
		final int[] cv, ov, ama;
		CreatureAttributesBonusesCalculator bc;
		AttributeIdentifier ai;
		IndexToObjectBackmapping itai;
		this.isCacheAvailable = false;
		cv = this.cacheValues;
		ov = super.originalValues;
		ama = this.attributesModificationsApplied;
		i = this.attributesCount;
		if ((bc = this.bonusCalculator) != null) {
			bc.markCacheAsDirty();
			while (--i >= 0) {
				cv[i] = ov[i] + ama[i] + bc.getBonusFor(i);
			}
		} else {
			while (--i >= 0) {
				cv[i] = ov[i] + ama[i];
			}
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
		Objects.requireNonNull(this.attributesModificationsApplied);
		Objects.requireNonNull(eam);
		Objects.requireNonNull(eam.getAttributeModified());
		this.attributesModificationsApplied[eam.getAttributeModified().getIndex()] += eam.getValue();
	}

	@Override
	public void removeAttributeModifier(AttributeModification eam) {
		this.isCacheAvailable = false;
		this.attributesModificationsApplied[eam.getAttributeModified().getIndex()] -= eam.getValue();
	}
}