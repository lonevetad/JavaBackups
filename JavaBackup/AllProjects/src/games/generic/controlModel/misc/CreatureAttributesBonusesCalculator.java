package games.generic.controlModel.misc;

import java.util.Objects;

import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;

/**
 * Some implementations of {@link CreatureAttributes} could compute some
 * Attribute depending on other attributes. This interface generalize it.
 */
public interface CreatureAttributesBonusesCalculator {
	public CreatureAttributes getCreatureAttributesSet();

	public void setCreatureAttributesSet(CreatureAttributes creatureAttributesSet);

	/** Utility method useful when caching values */
	public default void markCacheAsDirty() {}

	/** Calculate the bonus for a given index's value */
	public default int getBonusFor(int index) {
		AttributeIdentifier ai;
		CreatureAttributes ca;
		IndexToObjectBackmapping itai;
		ca = getCreatureAttributesSet();
		Objects.requireNonNull(ca);
		itai = ca.getIndexToAttributeIdentifier();
		Objects.requireNonNull(itai);
		ai = (AttributeIdentifier) itai.fromIndex(index);
		if (ai == null) {
			throw new IllegalArgumentException("Can't find an AttributeIdentifier with index: " + index);
		}
		return this.getBonusFor(ai);
	}

	public default int getBonusFor(AttributeIdentifier identifier) {
		Objects.requireNonNull(identifier);
		return 0;
	}
}